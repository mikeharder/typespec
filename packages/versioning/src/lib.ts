import { createTypeSpecLibrary, paramMessage } from "@typespec/compiler";

export const $lib = createTypeSpecLibrary({
  name: "@typespec/versioning",
  diagnostics: {
    "versioned-dependency-tuple": {
      severity: "error",
      messages: {
        default: `Versioned dependency mapping must be a tuple [SourceVersion, TargetVersion].`,
      },
    },
    "versioned-dependency-tuple-enum-member": {
      severity: "error",
      messages: {
        default: `Versioned dependency mapping must be between enum members.`,
      },
    },
    "versioned-dependency-same-namespace": {
      severity: "error",
      messages: {
        default: `Versioned dependency mapping must all point to the same namespace but 2 versions have different namespaces '${"namespace1"}' and '${"namespace2"}'.`,
      },
    },
    "versioned-dependency-not-picked": {
      severity: "error",
      messages: {
        default: paramMessage`The versionedDependency decorator must provide a version of the dependency '${"dependency"}'.`,
      },
    },
    "version-not-found": {
      severity: "error",
      messages: {
        default: paramMessage`The provided version '${"version"}' from '${"enumName"}' is not declared as a version enum. Use '@versioned(${"enumName"})' on the containing namespace.`,
      },
    },
    "version-duplicate": {
      severity: "error",
      messages: {
        default: paramMessage`Multiple versions from '${"name"}' resolve to the same value. Version enums must resolve to unique values.`,
      },
    },
    "invalid-renamed-from-value": {
      severity: "error",
      messages: {
        default: "@renamedFrom.oldName cannot be empty string.",
      },
    },
    "incompatible-versioned-reference": {
      severity: "error",
      messages: {
        default: paramMessage`'${"sourceName"}' is referencing versioned type '${"targetName"}' but is not versioned itself.`,
        addedAfter: paramMessage`'${"sourceName"}' was added in version '${"sourceAddedOn"}' but referencing type '${"targetName"}' added in version '${"targetAddedOn"}'.`,
        dependentAddedAfter: paramMessage`'${"sourceName"}' was added in version '${"sourceAddedOn"}' but contains type '${"targetName"}' added in version '${"targetAddedOn"}'.`,
        removedBefore: paramMessage`'${"sourceName"}' was removed in version '${"sourceRemovedOn"}' but referencing type '${"targetName"}' removed in version '${"targetRemovedOn"}'.`,
        dependentRemovedBefore: paramMessage`'${"sourceName"}' was removed in version '${"sourceRemovedOn"}' but contains type '${"targetName"}' removed in version '${"targetRemovedOn"}'.`,
        versionedDependencyAddedAfter: paramMessage`'${"sourceName"}' is referencing type '${"targetName"}' added in version '${"targetAddedOn"}' but version used is '${"dependencyVersion"}'.`,
        versionedDependencyRemovedBefore: paramMessage`'${"sourceName"}' is referencing type '${"targetName"}' removed in version '${"targetAddedOn"}' but version used is '${"dependencyVersion"}'.`,
        doesNotExist: paramMessage`'${"sourceName"}' is referencing type '${"targetName"}' which does not exist in version '${"version"}'.`,
      },
    },
    "incompatible-versioned-namespace-use-dependency": {
      severity: "error",
      messages: {
        default:
          "The useDependency decorator can only be used on a Namespace if the namespace is unversioned. For versioned namespaces, put the useDependency decorator on the version enum members.",
      },
    },
    "made-optional-not-optional": {
      severity: "error",
      messages: {
        default: paramMessage`Property '${"name"}' marked with @madeOptional but is required. Should be '${"name"}?'`,
      },
    },
    "made-required-optional": {
      severity: "error",
      messages: {
        default: paramMessage`Property '${"name"}?' marked with @madeRequired but is optional. Should be '${"name"}'`,
      },
    },
    "renamed-duplicate-property": {
      severity: "error",
      messages: {
        default: paramMessage`Property '${"name"}' marked with '@renamedFrom' conflicts with existing property in version ${"version"}.`,
      },
    },
  },
  state: {
    versionIndex: { description: "Version index" },

    addedOn: { description: "State for @addedOn decorator" },
    removedOn: { description: "State for @removedOn decorator" },
    versions: { description: "State for @versioned decorator" },
    useDependencyNamespace: { description: "State for @useDependency decorator on Namespaces" },
    useDependencyEnum: { description: "State for @useDependency decorator on Enums" },
    renamedFrom: { description: "State for @renamedFrom decorator" },
    madeOptional: { description: "State for @madeOptional decorator" },
    madeRequired: { description: "State for @madeRequired decorator" },
    typeChangedFrom: { description: "State for @typeChangedFrom decorator" },
    returnTypeChangedFrom: { description: "State for @returnTypeChangedFrom decorator" },
  },
});

export const { reportDiagnostic, createStateSymbol, stateKeys: VersioningStateKeys } = $lib;
