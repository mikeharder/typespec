import {
  createDiagnosticCollector,
  Diagnostic,
  DiagnosticResult,
  Interface,
  Namespace,
  Operation,
  Program,
} from "@typespec/compiler";
import { isSharedRoute } from "./decorators/shared-route.js";
import { createDiagnostic, HttpStateKeys } from "./lib.js";
import { getOperationParameters } from "./parameters.js";
import {
  HttpOperation,
  HttpOperationParameter,
  HttpOperationParameters,
  HttpOperationPathParameter,
  HttpOperationQueryParameter,
  PathParameterOptions,
  RouteOptions,
  RoutePath,
  RouteProducer,
  RouteProducerResult,
  RouteResolutionOptions,
} from "./types.js";
import { parseUriTemplate, UriTemplate } from "./uri-template.js";

// The set of allowed segment separator characters
const AllowedSegmentSeparators = ["/", ":"];

function needsSlashPrefix(fragment: string) {
  return !(
    AllowedSegmentSeparators.indexOf(fragment[0]) !== -1 ||
    (fragment[0] === "{" && fragment[1] === "/")
  );
}

function normalizeFragment(fragment: string, trimLast = false) {
  if (fragment.length > 0 && needsSlashPrefix(fragment)) {
    // Insert the default separator
    fragment = `/${fragment}`;
  }

  if (trimLast && fragment[fragment.length - 1] === "/") {
    return fragment.slice(0, -1);
  }
  return fragment;
}

export function joinPathSegments(rest: string[]) {
  let current = "";
  for (const [index, segment] of rest.entries()) {
    current += normalizeFragment(segment, index < rest.length - 1);
  }
  return current;
}

function buildPath(pathFragments: string[]) {
  // Join all fragments with leading and trailing slashes trimmed
  const path = pathFragments.length === 0 ? "/" : joinPathSegments(pathFragments);

  // The final path must start with a '/' or {/ (path expansion)
  return path[0] === "/" || (path[0] === "{" && path[1] === "/") ? path : `/${path}`;
}

export function resolvePathAndParameters(
  program: Program,
  operation: Operation,
  overloadBase: HttpOperation | undefined,
  options: RouteResolutionOptions,
): DiagnosticResult<{
  readonly uriTemplate: string;
  path: string;
  parameters: HttpOperationParameters;
}> {
  const diagnostics = createDiagnosticCollector();
  const { uriTemplate, parameters } = diagnostics.pipe(
    getUriTemplateAndParameters(program, operation, overloadBase, options),
  );

  const parsedUriTemplate = parseUriTemplate(uriTemplate);

  // Pull out path parameters to verify what's in the path string
  const paramByName = new Set(
    parameters.parameters
      .filter(({ type }) => type === "path" || type === "query")
      .map((x) => x.name),
  );

  validateDoubleSlash(parsedUriTemplate, operation, parameters).forEach((d) => diagnostics.add(d));

  // Ensure that all of the parameters defined in the route are accounted for in
  // the operation parameters and are correctly defined when optional
  for (const routeParam of parsedUriTemplate.parameters) {
    const decoded = decodeURIComponent(routeParam.name);
    if (!paramByName.has(routeParam.name) && !paramByName.has(decoded)) {
      diagnostics.add(
        createDiagnostic({
          code: "missing-uri-param",
          format: { param: routeParam.name },
          target: operation,
        }),
      );
    }
  }

  const path = produceLegacyPathFromUriTemplate(parsedUriTemplate);
  return diagnostics.wrap({
    uriTemplate,
    path,
    parameters,
  });
}

function validateDoubleSlash(
  parsedUriTemplate: UriTemplate,
  operation: Operation,
  parameters: HttpOperationParameters,
): readonly Diagnostic[] {
  const diagnostics = createDiagnosticCollector();
  if (parsedUriTemplate.segments) {
    const [firstSeg, ...rest] = parsedUriTemplate.segments;
    let lastSeg = firstSeg;
    for (const seg of rest) {
      if (typeof seg !== "string") {
        const parameter = parameters.parameters.find((x) => x.name === seg.name);

        if (seg.operator === "/") {
          if (typeof lastSeg === "string" && lastSeg.endsWith("/")) {
            diagnostics.add(
              createDiagnostic({
                code: "double-slash",
                messageId: parameter?.param.optional ? "optionalUnset" : "default",
                format: { paramName: seg.name },
                target: operation,
              }),
            );
          }
        }
        lastSeg = seg;
      }
    }
  }
  return diagnostics.diagnostics;
}

function produceLegacyPathFromUriTemplate(uriTemplate: UriTemplate) {
  let result = "";

  for (const segment of uriTemplate.segments ?? []) {
    if (typeof segment === "string") {
      result += segment;
    } else if (segment.operator !== "?" && segment.operator !== "&") {
      result += `{${segment.name}}`;
    }
  }

  return result;
}

function collectSegmentsAndOptions(
  program: Program,
  source: Interface | Namespace | undefined,
): [string[], RouteOptions] {
  if (source === undefined) return [[], {}];

  const [parentSegments, parentOptions] = collectSegmentsAndOptions(program, source.namespace);

  const route = getRoutePath(program, source)?.path;
  const options =
    source.kind === "Namespace" ? (getRouteOptionsForNamespace(program, source) ?? {}) : {};

  return [[...parentSegments, ...(route ? [route] : [])], { ...parentOptions, ...options }];
}

function getUriTemplateAndParameters(
  program: Program,
  operation: Operation,
  overloadBase: HttpOperation | undefined,
  options: RouteResolutionOptions,
): DiagnosticResult<RouteProducerResult> {
  const [parentSegments, parentOptions] = collectSegmentsAndOptions(
    program,
    operation.interface ?? operation.namespace,
  );

  const routeProducer = getRouteProducer(program, operation) ?? DefaultRouteProducer;
  const [result, diagnostics] = routeProducer(program, operation, parentSegments, overloadBase, {
    ...parentOptions,
    ...options,
  });

  return [
    { uriTemplate: buildPath([result.uriTemplate]), parameters: result.parameters },
    diagnostics,
  ];
}

/** @experimental */
export function DefaultRouteProducer(
  program: Program,
  operation: Operation,
  parentSegments: string[],
  overloadBase: HttpOperation | undefined,
  options: RouteOptions,
): DiagnosticResult<RouteProducerResult> {
  const diagnostics = createDiagnosticCollector();
  const routePath = getRoutePath(program, operation)?.path;
  const uriTemplate =
    !routePath && overloadBase
      ? overloadBase.uriTemplate
      : joinPathSegments([...parentSegments, ...(routePath ? [routePath] : [])]);

  const parsedUriTemplate = parseUriTemplate(uriTemplate);
  const parameters: HttpOperationParameters = diagnostics.pipe(
    getOperationParameters(program, operation, uriTemplate, overloadBase, options.paramOptions),
  );

  // Pull out path parameters to verify what's in the path string
  const unreferencedPathParamNames = new Map(
    parameters.parameters
      .filter(({ type }) => type === "path" || type === "query")
      .map((x) => [x.name, x]),
  );

  // Compile the list of all route params that aren't represented in the route
  for (const uriParam of parsedUriTemplate.parameters) {
    unreferencedPathParamNames.delete(uriParam.name);
  }

  const resolvedUriTemplate = addOperationTemplateToUriTemplate(uriTemplate, [
    ...unreferencedPathParamNames.values(),
  ]);
  return diagnostics.wrap({
    uriTemplate: resolvedUriTemplate,
    parameters,
  });
}

const styleToOperator: Record<PathParameterOptions["style"], string> = {
  matrix: ";",
  label: ".",
  simple: "",
  path: "/",
  fragment: "#",
};

export function getUriTemplatePathParam(param: HttpOperationPathParameter) {
  const operator = param.param.optional
    ? "/"
    : param.allowReserved
      ? "+"
      : styleToOperator[param.style];
  return `{${operator}${param.name}${param.explode ? "*" : ""}}`;
}

function getUriTemplateQueryParamPart(param: HttpOperationQueryParameter) {
  return `${escapeUriTemplateParamName(param.name)}${param.explode ? "*" : ""}`;
}

export function addQueryParamsToUriTemplate(uriTemplate: string, params: HttpOperationParameter[]) {
  const queryParams = params.filter((x) => x.type === "query");

  return (
    uriTemplate +
    (queryParams.length > 0
      ? `{?${queryParams.map((x) => getUriTemplateQueryParamPart(x)).join(",")}}`
      : "")
  );
}

function addOperationTemplateToUriTemplate(uriTemplate: string, params: HttpOperationParameter[]) {
  const pathParams = params.filter((x) => x.type === "path").map(getUriTemplatePathParam);
  const queryParams = params.filter((x) => x.type === "query");

  const pathPart = joinPathSegments([uriTemplate, ...pathParams]);
  return addQueryParamsToUriTemplate(pathPart, queryParams);
}

function escapeUriTemplateParamName(name: string) {
  return encodeURIComponent(name).replace(/[:-]/g, function (c) {
    return "%" + c.charCodeAt(0).toString(16).toUpperCase();
  });
}

export function setRouteProducer(
  program: Program,
  operation: Operation,
  routeProducer: RouteProducer,
): void {
  program.stateMap(HttpStateKeys.routeProducer).set(operation, routeProducer);
}

export function getRouteProducer(program: Program, operation: Operation): RouteProducer {
  return program.stateMap(HttpStateKeys.routeProducer).get(operation);
}

export function setRouteOptionsForNamespace(
  program: Program,
  namespace: Namespace,
  options: RouteOptions,
) {
  program.stateMap(HttpStateKeys.routeOptions).set(namespace, options);
}

export function getRouteOptionsForNamespace(
  program: Program,
  namespace: Namespace,
): RouteOptions | undefined {
  return program.stateMap(HttpStateKeys.routeOptions).get(namespace);
}

export function getRoutePath(
  program: Program,
  entity: Namespace | Interface | Operation,
): RoutePath | undefined {
  const path = program.stateMap(HttpStateKeys.routes).get(entity);
  return path
    ? {
        path,
        shared: entity.kind === "Operation" && isSharedRoute(program, entity as Operation),
      }
    : undefined;
}
