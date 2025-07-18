// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.typespec.http.client.generator.core.model.clientmodel;

import com.azure.core.http.rest.SimpleResponse;
import com.azure.core.util.CoreUtils;
import com.azure.core.util.UrlBuilder;
import com.azure.core.util.serializer.TypeReference;
import com.microsoft.typespec.http.client.generator.core.extension.model.codemodel.RequestParameterLocation;
import com.microsoft.typespec.http.client.generator.core.extension.plugin.JavaSettings;
import com.microsoft.typespec.http.client.generator.core.implementation.OperationInstrumentationInfo;
import com.microsoft.typespec.http.client.generator.core.mapper.CollectionUtil;
import com.microsoft.typespec.http.client.generator.core.model.javamodel.JavaVisibility;
import com.microsoft.typespec.http.client.generator.core.util.CodeNamer;
import com.microsoft.typespec.http.client.generator.core.util.MethodUtil;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A ClientMethod that exists on a ServiceClient or MethodGroupClient that eventually will call a ProxyMethod.
 */
public class ClientMethod {
    private static final List<String> KNOWN_POLLING_STRATEGIES = Arrays.asList("DefaultPollingStrategy",
        "ChainedPollingStrategy", "OperationResourcePollingStrategy", "LocationPollingStrategy",
        "StatusCheckPollingStrategy", "SyncDefaultPollingStrategy", "SyncChainedPollingStrategy",
        "SyncOperationResourcePollingStrategy", "SyncLocationPollingStrategy", "SyncStatusCheckPollingStrategy");

    private final String crossLanguageDefinitionId;
    /**
     * The description of this ClientMethod.
     */
    private final String description;
    /**
     * The return value of this ClientMethod.
     */
    private final ReturnValue returnValue;
    /**
     * The name of this ClientMethod.
     */
    private final String name;
    /**
     * An immutable list containing all parameters defined for the operation-endpoint, including constants, client
     * and method-scoped parameters.
     */
    private final List<ClientMethodParameter> _parameters;
    /**
     * A copy of {@code _parameters} returned from the {@link ClientMethod#getParameters()}.
     */
    private final List<ClientMethodParameter> parameters;
    /**
     * The subset of parameters in {@code parameters} that are scoped only to the method.
     */
    private final List<ClientMethodParameter> methodParameters;
    /**
     * The subset of parameters in {@code methodParameters} that are required for the method.
     */
    private final List<ClientMethodParameter> methodRequiredParameters;
    /**
     * Whether this ClientMethod has omitted optional parameters.
     */
    private final boolean onlyRequiredParameters;
    /**
     * The type of this ClientMethod.
     */
    private ClientMethodType type = ClientMethodType.values()[0];
    /**
     * The RestAPIMethod that this ClientMethod eventually calls.
     */
    private final ProxyMethod proxyMethod;
    /**
     * The expressions (parameters and service client properties) that need to be validated in this ClientMethod.
     */
    private final Map<String, String> validateExpressions;
    /**
     * The reference to the service client.
     */
    private final String clientReference;
    /**
     * The parameter expressions which are required.
     */
    private final List<String> requiredNullableParameterExpressions;
    /**
     * The parameter that needs to transformed before pagination.
     */
    private final boolean isGroupedParameterRequired;
    /**
     * The type name of groupedParameter.
     */
    private final String groupedParameterTypeName;
    /**
     * The pagination information if this is a paged method.
     */
    private final MethodPageDetails methodPageDetails;
    /**
     * The parameter transformations before calling ProxyMethod.
     */
    private final ParameterTransformations parameterTransformations;

    private final JavaVisibility methodVisibility;

    private final JavaVisibility methodVisibilityInWrapperClient;

    private final ImplementationDetails implementationDetails;

    private final MethodPollingDetails methodPollingDetails;

    private final ExternalDocumentation externalDocumentation;

    private final boolean hasWithContextOverload;
    private final String parametersDeclaration;
    private final String argumentList;
    private final OperationInstrumentationInfo instrumentationInfo;

    public ClientMethod.Builder newBuilder() {
        return new ClientMethod.Builder().description(description)
            .returnValue(returnValue)
            .name(name)
            .parameters(_parameters)
            .onlyRequiredParameters(onlyRequiredParameters)
            .type(type)
            .proxyMethod(proxyMethod)
            .validateExpressions(validateExpressions)
            .clientReference(clientReference)
            .requiredNullableParameterExpressions(requiredNullableParameterExpressions)
            .groupedParameterRequired(isGroupedParameterRequired)
            .groupedParameterTypeName(groupedParameterTypeName)
            .methodPageDetails(methodPageDetails)
            .parameterTransformations(parameterTransformations)
            .methodVisibility(methodVisibility)
            .methodVisibilityInWrapperClient(methodVisibilityInWrapperClient)
            .implementationDetails(implementationDetails)
            .methodPollingDetails(methodPollingDetails)
            .methodDocumentation(externalDocumentation)
            .operationInstrumentationInfo(instrumentationInfo)
            .setCrossLanguageDefinitionId(crossLanguageDefinitionId)
            .hasWithContextOverload(hasWithContextOverload);
    }

    /**
     * Create a new ClientMethod with the provided properties.
     *
     * @param description The description of this ClientMethod.
     * @param returnValue The return value of this ClientMethod.
     * @param name The name of this ClientMethod.
     * @param parameters The parameters of this ClientMethod.
     * @param onlyRequiredParameters Whether this ClientMethod has omitted optional parameters.
     * @param type The type of this ClientMethod.
     * @param proxyMethod The ProxyMethod that this ClientMethod eventually calls.
     * @param validateExpressions The expressions (parameters and service client properties) that need to be validated
     * in this ClientMethod.
     * @param clientReference The reference to the service client.
     * @param requiredNullableParameterExpressions The parameter expressions which are required.
     * @param isGroupedParameterRequired The parameter that needs to transformed before pagination.
     * @param groupedParameterTypeName The type name of groupedParameter.
     * @param methodPageDetails The pagination information if this is a paged method.
     * @param parameterTransformations The parameter transformations before calling ProxyMethod.
     * @param externalDocumentation The external documentation.
     * @param hasWithContextOverload Whether this method has a corresponding {@code Context}-based overload.
     * @param instrumentationInfo The instrumentation information for this ClientMethod.
     */
    protected ClientMethod(String description, ReturnValue returnValue, String name,
        List<ClientMethodParameter> parameters, boolean onlyRequiredParameters, ClientMethodType type,
        ProxyMethod proxyMethod, Map<String, String> validateExpressions, String clientReference,
        List<String> requiredNullableParameterExpressions, boolean isGroupedParameterRequired,
        String groupedParameterTypeName, MethodPageDetails methodPageDetails,
        ParameterTransformations parameterTransformations, JavaVisibility methodVisibility,
        JavaVisibility methodVisibilityInWrapperClient, ImplementationDetails implementationDetails,
        MethodPollingDetails methodPollingDetails, ExternalDocumentation externalDocumentation,
        String crossLanguageDefinitionId, boolean hasWithContextOverload,
        OperationInstrumentationInfo instrumentationInfo) {
        this.description = description;
        this.returnValue = returnValue;
        this.name = name;
        this._parameters = parameters;
        this.parameters = List.copyOf(parameters);
        this.methodParameters = parameters.stream()
            .filter(parameter -> !parameter.isFromClient()
                && parameter.getName() != null
                && !parameter.getName().trim().isEmpty())
            .sorted((p1, p2) -> Boolean.compare(!p1.isRequired(), !p2.isRequired()))
            .collect(Collectors.toUnmodifiableList());
        this.methodRequiredParameters = methodParameters.stream()
            .filter(param -> !param.isConstant() && param.isRequired())
            .collect(Collectors.toUnmodifiableList());
        this.onlyRequiredParameters = onlyRequiredParameters;
        this.type = type;
        this.proxyMethod = proxyMethod;
        this.validateExpressions = validateExpressions;
        this.clientReference = clientReference;
        this.requiredNullableParameterExpressions = requiredNullableParameterExpressions;
        this.isGroupedParameterRequired = isGroupedParameterRequired;
        this.groupedParameterTypeName = groupedParameterTypeName;
        this.methodPageDetails = methodPageDetails;
        this.parameterTransformations = parameterTransformations;
        this.methodVisibility = methodVisibility;
        this.implementationDetails = implementationDetails;
        this.methodPollingDetails = methodPollingDetails;
        this.externalDocumentation = externalDocumentation;
        this.methodVisibilityInWrapperClient = methodVisibilityInWrapperClient;
        this.crossLanguageDefinitionId = crossLanguageDefinitionId;
        this.hasWithContextOverload = hasWithContextOverload;
        if (isPageStreamingType() && methodPageDetails != null) {
            this.parametersDeclaration = getMethodInputParameters().stream()
                .filter(p -> !methodPageDetails.shouldHideParameter(p))
                .map(ClientMethodParameter::getDeclaration)
                .collect(Collectors.joining(", "));
        } else {
            this.parametersDeclaration = getMethodInputParameters().stream()
                .map(ClientMethodParameter::getDeclaration)
                .collect(Collectors.joining(", "));
        }
        this.argumentList
            = getMethodParameters().stream().map(ClientMethodParameter::getName).collect(Collectors.joining(", "));
        this.instrumentationInfo = instrumentationInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ClientMethod that = (ClientMethod) o;
        return onlyRequiredParameters == that.onlyRequiredParameters
            && isGroupedParameterRequired == that.isGroupedParameterRequired
            && Objects.equals(returnValue.getType(), that.returnValue.getType())
            && Objects.equals(name, that.name)
            && Objects.equals(getParametersDeclaration(), that.getParametersDeclaration())
            && type == that.type
            && Objects.equals(requiredNullableParameterExpressions, that.requiredNullableParameterExpressions)
            && Objects.equals(groupedParameterTypeName, that.groupedParameterTypeName)
            && Objects.equals(parameterTransformations, that.parameterTransformations)
            && methodVisibility == that.methodVisibility;
    }

    @Override
    public int hashCode() {
        return Objects.hash(returnValue.getType(), name, getParametersDeclaration(), onlyRequiredParameters, type,
            requiredNullableParameterExpressions, isGroupedParameterRequired, groupedParameterTypeName,
            parameterTransformations, methodVisibility);
    }

    public String getCrossLanguageDefinitionId() {
        return crossLanguageDefinitionId;
    }

    public final String getDescription() {
        return description;
    }

    public final ReturnValue getReturnValue() {
        return returnValue;
    }

    public final String getName() {
        return name;
    }

    public final List<ClientMethodParameter> getParameters() {
        return parameters;
    }

    public final boolean getOnlyRequiredParameters() {
        return onlyRequiredParameters;
    }

    public final ClientMethodType getType() {
        return type;
    }

    /**
     * Check if this method is page streaming method (i.e. if the method returns PagedIterable&lt;T&gt; or
     * PagedFlux&lt;T&gt;).
     *
     * @return true if this method is a page streaming method, false otherwise.
     */
    public final boolean isPageStreamingType() {
        return type == ClientMethodType.PagingAsync || type == ClientMethodType.PagingSync;
    }

    public final ProxyMethod getProxyMethod() {
        return proxyMethod;
    }

    public final Map<String, String> getValidateExpressions() {
        return validateExpressions;
    }

    public final String getClientReference() {
        return clientReference;
    }

    /**
     * Get the comma-separated list of parameter declarations for this ClientMethod.
     */
    public final String getParametersDeclaration() {
        return parametersDeclaration;
    }

    public boolean hasParameterDeclaration() {
        return parametersDeclaration != null && !parametersDeclaration.isEmpty();
    }

    /**
     * Get the comma-separated list of parameter names for this ClientMethod.
     */
    public final String getArgumentList() {
        return argumentList;
    }

    /**
     * The full declaration of this ClientMethod.
     */
    public final String getDeclaration() {
        return getReturnValue().getType() + " " + getName() + "(" + getParametersDeclaration() + ")";
    }

    /**
     * Get the input parameters of the client method, taking configure of onlyRequiredParameters.
     */
    public final List<ClientMethodParameter> getMethodInputParameters() {
        return onlyRequiredParameters ? getMethodRequiredParameters() : getMethodParameters();
    }

    public final List<ClientMethodParameter> getMethodParameters() {
        return methodParameters;
    }

    /**
     * Check if this method has a parameter of the given type.
     *
     * @param type the type to check.
     * @return true if this method has a parameter of the given type, false otherwise.
     */
    public boolean hasMethodParameterOfType(IType type) {
        return methodParameters.stream().anyMatch(p -> type.equals(p.getClientType()));
    }

    public final List<ClientMethodParameter> getMethodRequiredParameters() {
        return methodRequiredParameters;
    }

    public final List<String> getRequiredNullableParameterExpressions() {
        return requiredNullableParameterExpressions;
    }

    public final boolean isGroupedParameterRequired() {
        return isGroupedParameterRequired;
    }

    public final String getGroupedParameterTypeName() {
        return groupedParameterTypeName;
    }

    public final MethodPageDetails getMethodPageDetails() {
        return methodPageDetails;
    }

    public final ParameterTransformations getParameterTransformations() {
        return parameterTransformations;
    }

    public ExternalDocumentation getMethodDocumentation() {
        return externalDocumentation;
    }

    public final List<String> getProxyMethodArguments(JavaSettings settings) {
        List<String> restAPIMethodArguments = getProxyMethod().getParameters().stream().map(parameter -> {
            String parameterName = parameter.getParameterReference();
            IType parameterWireType = parameter.getWireType();
            if (parameter.isNullable()) {
                parameterWireType = parameterWireType.asNullable();
            }
            IType parameterClientType = parameter.getClientType();

            if (parameterClientType != ClassType.BASE_64_URL
                && parameter.getRequestParameterLocation() != RequestParameterLocation.BODY /*
                                                                                             * && parameter.
                                                                                             * getRequestParameterLocation
                                                                                             * () !=
                                                                                             * RequestParameterLocation.
                                                                                             * FormData
                                                                                             */
                && (parameterClientType instanceof ArrayType || parameterClientType instanceof IterableType)) {
                parameterWireType = ClassType.STRING;
            }

            String parameterWireName = (parameterClientType != parameterWireType)
                ? CodeNamer.toCamelCase(CodeNamer.removeInvalidCharacters(parameterName)) + "Converted"
                : parameterName;

            String result;
            if (getParameterTransformations().hasOutParameter(parameterName + "1")) {
                result = parameterName + "1";
            } else {
                result = parameterWireName;
            }
            return result;
        }).collect(Collectors.toList());
        return restAPIMethodArguments;
    }

    public JavaVisibility getMethodVisibility() {
        return methodVisibility;
    }

    public JavaVisibility getMethodVisibilityInWrapperClient() {
        return methodVisibilityInWrapperClient;
    }

    public ImplementationDetails getImplementationDetails() {
        return implementationDetails;
    }

    public boolean isImplementationOnly() {
        return implementationDetails != null && implementationDetails.isImplementationOnly();
    }

    public MethodPollingDetails getMethodPollingDetails() {
        return methodPollingDetails;
    }

    /**
     * Whether this {@link ClientMethod} has a corresponding {@link ClientMethod} that has an equivalent overload that
     * contains an additional {@code Context} parameter.
     *
     * @return whether this method has a corresponding {@code Context}-based overload
     */
    public boolean hasWithContextOverload() {
        return hasWithContextOverload;
    }

    /**
     * Get the instrumentation information for this ClientMethod.
     *
     * @return the instrumentation information for this ClientMethod.
     */
    public OperationInstrumentationInfo getOperationInstrumentationInfo() {
        return instrumentationInfo;
    }

    /**
     * Add this ClientMethod's imports to the provided set of imports.
     *
     * @param imports The set of imports to add to.
     * @param includeImplementationImports Whether to include imports that are only necessary for method
     * implementations.
     */
    public void addImportsTo(Set<String> imports, boolean includeImplementationImports, JavaSettings settings) {

        Annotation.SERVICE_METHOD.addImportsTo(imports);
        Annotation.RETURN_TYPE.addImportsTo(imports);

        imports.add("java.util.Objects");
        imports.add("java.util.stream.Collectors");
        ClassType.BINARY_DATA.addImportsTo(imports, includeImplementationImports);
        ClassType.RESPONSE.addImportsTo(imports, includeImplementationImports);
        ClassType.SIMPLE_RESPONSE.addImportsTo(imports, includeImplementationImports);
        ClassType.HTTP_HEADER_NAME.addImportsTo(imports, false);

        if (settings.isDataPlaneClient()) {
            // for some processing on RequestOptions (get/set header)

            // for query parameter modification in RequestOptions (UrlBuilder.parse)
            imports.add(UrlBuilder.class.getName());
            imports.add("io.clientcore.core.utils.UriBuilder");
        }

        getReturnValue().addImportsTo(imports, includeImplementationImports);

        for (ClientMethodParameter parameter : getParameters()) {
            parameter.addImportsTo(imports, includeImplementationImports);
        }

        if (includeImplementationImports) {
            ClassType.CONTEXT.addImportsTo(imports, false);

            if (proxyMethod != null) {
                proxyMethod.addImportsTo(imports, includeImplementationImports, settings);
            }

            if (getReturnValue().getType() == ClassType.INPUT_STREAM) {
                imports.add("com.fasterxml.jackson.databind.util.ByteBufferBackedInputStream");
                imports.add("java.io.SequenceInputStream");
                imports.add("java.util.Enumeration");
                imports.add("java.util.Iterator");
            }

            // Add FluxUtil as an import if this is an asynchronous method and the last parameter isn't the Context
            // parameter.
            if (proxyMethod != null
                && !proxyMethod.isSync()
                && (CoreUtils.isNullOrEmpty(parameters)
                    || parameters.get(parameters.size() - 1) != ClientMethodParameter.CONTEXT_PARAMETER)) {
                imports.add("com.azure.core.util.FluxUtil");
            }

            if (getMethodPageDetails() != null) {
                imports.add("com.azure.core.http.rest.PagedResponseBase");

                if (settings.isDataPlaneClient()) {
                    imports.add("java.util.List");
                    imports.add("java.util.Map");
                    ClassType.BINARY_DATA.addImportsTo(imports, includeImplementationImports);
                }
            }

            if (type == ClientMethodType.LongRunningBeginAsync || type == ClientMethodType.LongRunningBeginSync) {
                if (settings.isFluent()) {
                    if (((GenericType) this.getReturnValue().getType().getClientType())
                        .getTypeArguments()[0] instanceof GenericType) {
                        // pageable LRO
                        if (settings.isStreamStyleSerialization()) {
                            imports.add(TypeReference.class.getName());
                        } else {
                            imports.add("com.fasterxml.jackson.core.type.TypeReference");
                        }
                    }
                } else {
                    imports.add(TypeReference.class.getName());
                    if (!JavaSettings.getInstance().isAzureV1()) {
                        imports.add(Type.class.getName());
                        imports.add(ParameterizedType.class.getName());
                    }

                    imports.add("java.time.Duration");

                    ClassType.POLLING_STRATEGY_OPTIONS.addImportsTo(imports, false);

                    if (getMethodPollingDetails() != null) {
                        for (String pollingStrategy : KNOWN_POLLING_STRATEGIES) {
                            if (getMethodPollingDetails().getPollingStrategy().contains(pollingStrategy)
                                || getMethodPollingDetails().getSyncPollingStrategy().contains(pollingStrategy)) {

                                if (JavaSettings.getInstance().isAzureV2()) {
                                    imports.add("com.azure.v2.core.http.polling." + pollingStrategy);
                                } else {
                                    imports.add("com.azure.core.util.polling." + pollingStrategy);
                                }
                            }
                        }
                    }
                }
            }

            if (type == ClientMethodType.PagingAsyncSinglePage
                || type == ClientMethodType.PagingSyncSinglePage && this.getMethodPageDetails() != null) {
                if (this.getMethodPageDetails() != null
                    && this.getMethodPageDetails().getLroIntermediateType() != null) {
                    // pageable + LRO
                    this.getMethodPageDetails()
                        .getLroIntermediateType()
                        .addImportsTo(imports, includeImplementationImports);
                }
            }

            if (MethodUtil.isMethodIncludeRepeatableRequestHeaders(this.proxyMethod)) {
                // Repeatable Requests
                ClassType.CORE_UTILS.addImportsTo(imports, false);
                ClassType.DATE_TIME.addImportsTo(imports, false);
                ClassType.DATE_TIME_RFC_1123.addImportsTo(imports, false);
                imports.add("java.util.UUID");
            }

            if (type == ClientMethodType.SendRequestAsync || type == ClientMethodType.SendRequestSync) {
                imports.add(SimpleResponse.class.getName());
                ClassType.BINARY_DATA.addImportsTo(imports, false);
                ClassType.HTTP_REQUEST.addImportsTo(imports, false);
            }
            // sync-stack, lro (+ pageable)
            if (settings.isSyncStackEnabled() && settings.isFluent()) {
                boolean isLroPageable = (type == ClientMethodType.PagingSyncSinglePage
                    && proxyMethod != null
                    && GenericType.Response(ClassType.BINARY_DATA).equals(proxyMethod.getReturnType().getClientType()));
                if (type == ClientMethodType.LongRunningBeginSync || isLroPageable) {
                    ClassType.SYNC_POLLER_FACTORY.addImportsTo(imports, false);
                }
            }
        }
    }

    public static ClientMethod getAsyncSendRequestClientMethod(boolean isInMethodGroup) {
        return new Builder().name("sendRequestAsync")
            .description("Sends the {@code httpRequest}.")
            .clientReference(isInMethodGroup ? "this.client" : "this")
            .methodVisibility(JavaVisibility.Public)
            .onlyRequiredParameters(false)
            .type(ClientMethodType.SendRequestAsync)
            .parameters(ClientMethodParameter.HTTP_REQUEST_PARAMETER)
            .returnValue(new ReturnValue("the response body on successful completion of {@link Mono}",
                GenericType.Mono(GenericType.Response(ClassType.BINARY_DATA))))
            .build();
    }

    public static ClientMethod getSyncSendRequestClientMethod(boolean isInMethodGroup) {
        return new Builder().name("sendRequest")
            .description("Sends the {@code httpRequest}.")
            .clientReference(isInMethodGroup ? "this.client" : "this")
            .methodVisibility(JavaVisibility.Public)
            .onlyRequiredParameters(false)
            .type(ClientMethodType.SendRequestSync)
            .parameters(ClientMethodParameter.HTTP_REQUEST_PARAMETER, ClientMethodParameter.CONTEXT_PARAMETER)
            .returnValue(new ReturnValue("the response body along with {@link Response}",
                GenericType.Response(ClassType.BINARY_DATA)))
            .build();
    }

    public static class Builder {
        protected String description;
        protected ReturnValue returnValue;
        protected String name;
        protected List<ClientMethodParameter> parameters;
        protected boolean onlyRequiredParameters;
        protected ClientMethodType type = ClientMethodType.values()[0];
        protected ProxyMethod proxyMethod;
        protected Map<String, String> validateExpressions;
        protected String clientReference;
        protected List<String> requiredNullableParameterExpressions;
        protected boolean isGroupedParameterRequired;
        protected String groupedParameterTypeName;
        protected MethodPageDetails methodPageDetails;
        protected ParameterTransformations parameterTransformations;
        protected JavaVisibility methodVisibility = JavaVisibility.Public;
        protected JavaVisibility methodVisibilityInWrapperClient = JavaVisibility.Public;
        protected ImplementationDetails implementationDetails;
        protected MethodPollingDetails methodPollingDetails;
        protected ExternalDocumentation externalDocumentation;
        protected String crossLanguageDefinitionId;
        protected boolean hasWithContextOverload;
        protected boolean hidePageableParams;
        protected OperationInstrumentationInfo instrumentationInfo;

        public Builder setCrossLanguageDefinitionId(String crossLanguageDefinitionId) {
            this.crossLanguageDefinitionId = crossLanguageDefinitionId;
            return this;
        }

        /**
         * Sets the description of this ClientMethod.
         *
         * @param description the description of this ClientMethod
         * @return the Builder itself
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        /**
         * Sets the return value of this ClientMethod.
         *
         * @param returnValue the return value of this ClientMethod
         * @return the Builder itself
         */
        public Builder returnValue(ReturnValue returnValue) {
            this.returnValue = returnValue;
            return this;
        }

        /**
         * Sets the name of this ClientMethod.
         *
         * @param name the name of this ClientMethod
         * @return the Builder itself
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the parameters of this ClientMethod.
         *
         * @param parameters the parameters of this ClientMethod
         * @return the Builder itself
         */
        public Builder parameters(List<ClientMethodParameter> parameters) {
            this.parameters = parameters;
            return this;
        }

        private Builder parameters(ClientMethodParameter... parameters) {
            this.parameters = CoreUtils.isNullOrEmpty(parameters) ? null : Arrays.asList(parameters);
            return this;
        }

        /**
         * Sets whether this ClientMethod has omitted optional parameters.
         *
         * @param onlyRequiredParameters whether this ClientMethod has omitted optional parameters
         * @return the Builder itself
         */
        public Builder onlyRequiredParameters(boolean onlyRequiredParameters) {
            this.onlyRequiredParameters = onlyRequiredParameters;
            return this;
        }

        /**
         * Sets the type of this ClientMethod.
         *
         * @param type the type of this ClientMethod
         * @return the Builder itself
         */
        public Builder type(ClientMethodType type) {
            this.type = type;
            return this;
        }

        /**
         * Sets the RestAPIMethod that this ClientMethod eventually calls.
         *
         * @param proxyMethod the RestAPIMethod that this ClientMethod eventually calls
         * @return the Builder itself
         */
        public Builder proxyMethod(ProxyMethod proxyMethod) {
            this.proxyMethod = proxyMethod;
            return this;
        }

        /**
         * Sets the expressions ( (parameters and service client properties) that need to be validated in this
         * ClientMethod.
         *
         * @param validateExpressions the expressions (parameters and service client properties) that need to be
         * validated in this ClientMethod
         * @return the Builder itself
         */
        public Builder validateExpressions(Map<String, String> validateExpressions) {
            this.validateExpressions = validateExpressions;
            return this;
        }

        /**
         * Sets the reference to the service client.
         *
         * @param clientReference the reference to the service client
         * @return the Builder itself
         */
        public Builder clientReference(String clientReference) {
            this.clientReference = clientReference;
            return this;
        }

        /**
         * Sets the parameter expressions which are required.
         *
         * @param requiredNullableParameterExpressions the parameter expressions which are required
         * @return the Builder itself
         */
        public Builder requiredNullableParameterExpressions(List<String> requiredNullableParameterExpressions) {
            this.requiredNullableParameterExpressions = requiredNullableParameterExpressions;
            return this;
        }

        /**
         * Sets the parameter that needs to transformed before pagination.
         *
         * @param isGroupedParameterRequired the parameter that needs to transformed before pagination
         * @return the Builder itself
         */
        public Builder groupedParameterRequired(boolean isGroupedParameterRequired) {
            this.isGroupedParameterRequired = isGroupedParameterRequired;
            return this;
        }

        /**
         * Sets the type name of groupedParameter.
         *
         * @param groupedParameterTypeName the type name of groupedParameter
         * @return the Builder itself
         */
        public Builder groupedParameterTypeName(String groupedParameterTypeName) {
            this.groupedParameterTypeName = groupedParameterTypeName;
            return this;
        }

        /**
         * Sets the pagination information if this is a paged method.
         *
         * @param methodPageDetails the pagination information if this is a paged method
         * @return the Builder itself
         */
        public Builder methodPageDetails(MethodPageDetails methodPageDetails) {
            this.methodPageDetails = methodPageDetails;
            return this;
        }

        /**
         * Sets the parameter transformations before calling ProxyMethod.
         *
         * @param parameterTransformations the parameter transformations before calling ProxyMethod
         * @return the Builder itself
         */
        public Builder parameterTransformations(ParameterTransformations parameterTransformations) {
            this.parameterTransformations = parameterTransformations;
            return this;
        }

        /**
         * Sets the parameter method visibility.
         *
         * @param methodVisibility the method visibility, default is Public.
         * @return the Builder itself
         */
        public Builder methodVisibility(JavaVisibility methodVisibility) {
            this.methodVisibility = methodVisibility;
            return this;
        }

        /**
         * Sets the parameter method visibility in wrapper client.
         *
         * @param methodVisibilityInWrapperClient the method visibility in wrapper client, default is Public.
         * @return the Builder itself
         */
        public Builder methodVisibilityInWrapperClient(JavaVisibility methodVisibilityInWrapperClient) {
            this.methodVisibilityInWrapperClient = methodVisibilityInWrapperClient;
            return this;
        }

        /**
         * Sets the polling information if this is a long running method.
         *
         * @param methodPollingDetails the polling information
         * @return the Builder itself
         */
        public Builder methodPollingDetails(MethodPollingDetails methodPollingDetails) {
            this.methodPollingDetails = methodPollingDetails;
            return this;
        }

        /**
         * Sets the implementation details for the method.
         *
         * @param implementationDetails the implementation details.
         * @return the Builder itself
         */
        public Builder implementationDetails(ImplementationDetails implementationDetails) {
            this.implementationDetails = implementationDetails;
            return this;
        }

        /**
         * Sets method documentation
         *
         * @param externalDocumentation method level documentation
         * @return the Builder itself
         */
        public Builder methodDocumentation(ExternalDocumentation externalDocumentation) {
            this.externalDocumentation = externalDocumentation;
            return this;
        }

        /**
         * Whether this {@link ClientMethod} has a corresponding {@link ClientMethod} that has an additional
         * {@code Context} parameter.
         * <p>
         * When this is true, when this method generates its client method it will call into the {@code Context}-based
         * overload instead of generating a method body. This helps to avoid generating duplicate method bodies that
         * only differ by the presence of a {@code Context} parameter.
         *
         * @param hasWithContextOverload whether this method has a corresponding {@code Context}-based overload
         * @return the Builder itself
         */
        public Builder hasWithContextOverload(boolean hasWithContextOverload) {
            this.hasWithContextOverload = hasWithContextOverload;
            return this;
        }

        /**
         * Sets the operation associated with this ClientMethod.
         *
         * @param instrumentationInfo the operation instrumentation information
         * @return the Builder itself
         */
        public Builder operationInstrumentationInfo(OperationInstrumentationInfo instrumentationInfo) {
            this.instrumentationInfo = instrumentationInfo;
            return this;
        }

        /**
         * @return an immutable ClientMethod instance with the configurations on this builder.
         */
        public ClientMethod build() {
            return new ClientMethod(description, returnValue, name, CollectionUtil.toImmutableList(parameters),
                onlyRequiredParameters, type, proxyMethod, CollectionUtil.toImmutableMap(validateExpressions),
                clientReference, CollectionUtil.toImmutableList(requiredNullableParameterExpressions),
                isGroupedParameterRequired, groupedParameterTypeName, methodPageDetails, parameterTransformations,
                methodVisibility, methodVisibilityInWrapperClient, implementationDetails, methodPollingDetails,
                externalDocumentation, crossLanguageDefinitionId, hasWithContextOverload, instrumentationInfo);
        }
    }
}
