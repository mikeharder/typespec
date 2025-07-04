// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.typespec.http.client.generator.mgmt.template;

import com.microsoft.typespec.http.client.generator.core.extension.plugin.JavaSettings;
import com.microsoft.typespec.http.client.generator.core.model.clientmodel.ClassType;
import com.microsoft.typespec.http.client.generator.core.model.clientmodel.ClientMethod;
import com.microsoft.typespec.http.client.generator.core.model.clientmodel.GenericType;
import com.microsoft.typespec.http.client.generator.core.model.clientmodel.IType;
import com.microsoft.typespec.http.client.generator.core.model.clientmodel.PrimitiveType;
import com.microsoft.typespec.http.client.generator.core.model.clientmodel.ProxyMethod;
import com.microsoft.typespec.http.client.generator.core.model.javamodel.JavaBlock;
import com.microsoft.typespec.http.client.generator.core.model.javamodel.JavaType;
import com.microsoft.typespec.http.client.generator.core.template.ClientMethodTemplate;
import com.microsoft.typespec.http.client.generator.core.util.CodeNamer;
import com.microsoft.typespec.http.client.generator.core.util.MethodNamer;

public class FluentClientMethodTemplate extends ClientMethodTemplate {

    private static final FluentClientMethodTemplate INSTANCE = new FluentClientMethodTemplate();

    public static FluentClientMethodTemplate getInstance() {
        return INSTANCE;
    }

    @Override
    protected void generatePagedAsyncSinglePage(ClientMethod clientMethod, JavaType typeBlock, JavaSettings settings) {
        final ProxyMethod restAPIMethod = clientMethod.getProxyMethod();
        boolean addContextParameter = !contextInParameters(clientMethod);
        boolean mergeContextParameter = contextInParameters(clientMethod);
        boolean isLroPagination = GenericType.Mono(GenericType.Response(GenericType.FLUX_BYTE_BUFFER))
            .equals(restAPIMethod.getReturnType().getClientType());
        String endOfLine = addContextParameter ? "" : ";";
        String contextParam
            = mergeContextParameter ? "context" : String.format("%s.getContext()", clientMethod.getClientReference());

        typeBlock.annotation("ServiceMethod(returns = ReturnType.SINGLE)");
        String restAPIMethodArgumentList = String.join(", ", clientMethod.getProxyMethodArguments(settings));
        String serviceMethodCall = String.format("service.%s(%s)", restAPIMethod.getName(), restAPIMethodArgumentList);
        if (clientMethod.getMethodPageDetails().nonNullNextLink()) {
            writeMethod(typeBlock, clientMethod.getMethodVisibility(), clientMethod.getDeclaration(), function -> {
                addValidations(function, clientMethod, settings);
                addOptionalAndConstantVariables(function, clientMethod, settings);
                applyParameterTransformations(function, clientMethod, settings);
                convertClientTypesToWireTypes(function, clientMethod);
                if (mergeContextParameter) {
                    function
                        .line(String.format("context = %s.mergeContext(context);", clientMethod.getClientReference()));
                }
                if (addContextParameter) {
                    if (!isLroPagination) {
                        function.line(String.format("return FluxUtil.withContext(context -> %s)", serviceMethodCall));
                    } else {
                        function.line("return FluxUtil.withContext(context -> {");
                        function.indent(() -> {
                            function.line(String.format("%s mono = %s.cache();",
                                clientMethod.getProxyMethod().getReturnType().toString(), serviceMethodCall));

                            IType classType = clientMethod.getMethodPageDetails().getLroIntermediateType();
                            function.line(String.format(
                                "return Mono.zip(mono, %1$s.<%2$s, %2$s>getLroResult(mono, %1$s.getHttpPipeline(), %2$s.class, %2$s.class, %3$s).last().flatMap(%1$s::getLroFinalResultOrError));",
                                clientMethod.getClientReference(), classType, contextParam));
                        });
                        function.line("})");
                    }
                } else {
                    if (!isLroPagination) {
                        function.line(String.format("return %s", serviceMethodCall));
                    } else {
                        function.line(String.format("%s mono = %s.cache();",
                            clientMethod.getProxyMethod().getReturnType().toString(), serviceMethodCall));

                        IType classType = clientMethod.getMethodPageDetails().getLroIntermediateType();
                        function.line(String.format(
                            "return Mono.zip(mono, %1$s.<%2$s, %2$s>getLroResult(mono, %1$s.getHttpPipeline(), %2$s.class, %2$s.class, %3$s).last().flatMap(%1$s::getLroFinalResultOrError))",
                            clientMethod.getClientReference(), classType, contextParam));
                    }
                }
                function.indent(() -> {
                    if (addContextParameter) {
                        function.line(String.format(".<%s>map(res -> new PagedResponseBase<>(",
                            returnTypeWithoutMono(clientMethod.getReturnValue().getType())));
                    } else {
                        function.line(".map(res -> new PagedResponseBase<>(");
                    }
                    function.indent(() -> {
                        if (!isLroPagination) {
                            function.line("res.getRequest(),");
                            function.line("res.getStatusCode(),");
                            function.line("res.getHeaders(),");
                            function.line("res.getValue().%s(),", CodeNamer.getModelNamer()
                                .modelPropertyGetterName(clientMethod.getMethodPageDetails().getItemName()));
                            function.line(nextLinkLine(clientMethod));
                            IType responseType
                                = ((GenericType) clientMethod.getProxyMethod().getReturnType()).getTypeArguments()[0];
                            if (responseType instanceof ClassType) {
                                function.line(String.format("res.getDeserializedHeaders()))%s", endOfLine));
                            } else {
                                function.line(String.format("null))%s", endOfLine));
                            }
                        } else {
                            function.line("res.getT1().getRequest(),");
                            function.line("res.getT1().getStatusCode(),");
                            function.line("res.getT1().getHeaders(),");
                            function.line("res.getT2().%s(),", CodeNamer.getModelNamer()
                                .modelPropertyGetterName(clientMethod.getMethodPageDetails().getItemName()));
                            function.line(nextLinkLine(clientMethod, "getT2()"));
                            IType responseType
                                = ((GenericType) clientMethod.getProxyMethod().getReturnType()).getTypeArguments()[0];
                            if (responseType instanceof ClassType) {
                                function.line(String.format("res.getT2().getDeserializedHeaders()))%s", endOfLine));
                            } else {
                                function.line(String.format("null))%s", endOfLine));
                            }
                        }
                    });
                    if (addContextParameter) {
                        function.line(String.format(
                            ".contextWrite(context -> context.putAll(FluxUtil.toReactorContext(%s.getContext()).readOnly()));",
                            clientMethod.getClientReference()));
                    }
                });
            });
        } else {
            writeMethod(typeBlock, clientMethod.getMethodVisibility(), clientMethod.getDeclaration(), function -> {
                addValidations(function, clientMethod, settings);
                addOptionalAndConstantVariables(function, clientMethod, settings);
                applyParameterTransformations(function, clientMethod, settings);
                convertClientTypesToWireTypes(function, clientMethod);
                if (mergeContextParameter) {
                    function
                        .line(String.format("context = %s.mergeContext(context);", clientMethod.getClientReference()));
                }
                if (addContextParameter) {
                    if (!isLroPagination) {
                        function.line(String.format("return FluxUtil.withContext(context -> %s)", serviceMethodCall));
                    } else {
                        function.line("return FluxUtil.withContext(context -> {");
                        function.indent(() -> {
                            function.line(String.format("%s mono = %s.cache();",
                                clientMethod.getProxyMethod().getReturnType().toString(), serviceMethodCall));

                            IType classType = clientMethod.getMethodPageDetails().getLroIntermediateType();
                            function.line(String.format(
                                "return Mono.zip(mono, %s.<%s, %s>getLroResult(mono, %s.getHttpPipeline(), %s.class, %s.class, %s).last().flatMap(%s::getLroFinalResultOrError));",
                                clientMethod.getClientReference(), classType.toString(), classType,
                                clientMethod.getClientReference(), classType, classType, contextParam,
                                clientMethod.getClientReference()));
                        });
                        function.line("})");
                    }
                } else {
                    if (!isLroPagination) {
                        function.line(String.format("return %s", serviceMethodCall));
                    } else {
                        function.line(String.format("%s mono = %s.cache();",
                            clientMethod.getProxyMethod().getReturnType().toString(), serviceMethodCall));

                        IType classType = clientMethod.getMethodPageDetails().getLroIntermediateType();
                        function.line(String.format(
                            "return Mono.zip(mono, %s.<%s, %s>getLroResult(mono, %s.getHttpPipeline(), %s.class, %s.class, %s).last().flatMap(%s::getLroFinalResultOrError))",
                            clientMethod.getClientReference(), classType.toString(), classType,
                            clientMethod.getClientReference(), classType, classType, contextParam,
                            clientMethod.getClientReference()));
                    }
                }
                function.indent(() -> {
                    if (addContextParameter) {
                        function.line(String.format(".<%s>map(res -> new PagedResponseBase<>(",
                            returnTypeWithoutMono(clientMethod.getReturnValue().getType())));
                    } else {
                        function.line(".map(res -> new PagedResponseBase<>(");
                    }
                    function.indent(() -> {
                        if (!isLroPagination) {
                            function.line("res.getRequest(),");
                            function.line("res.getStatusCode(),");
                            function.line("res.getHeaders(),");
                            function.line("res.getValue().%s(),", CodeNamer.getModelNamer()
                                .modelPropertyGetterName(clientMethod.getMethodPageDetails().getItemName()));
                            function.line("null,");
                            IType responseType
                                = ((GenericType) clientMethod.getProxyMethod().getReturnType()).getTypeArguments()[0];
                            if (responseType instanceof ClassType) {
                                function.line(String.format("res.getDeserializedHeaders()))%s", endOfLine));
                            } else {
                                function.line(String.format("null))%s", endOfLine));
                            }
                        } else {
                            function.line("res.getT1().getRequest(),");
                            function.line("res.getT1().getStatusCode(),");
                            function.line("res.getT1().getHeaders(),");
                            function.line("res.getT2().%s(),", CodeNamer.getModelNamer()
                                .modelPropertyGetterName(clientMethod.getMethodPageDetails().getItemName()));
                            function.line("null,");
                            IType responseType
                                = ((GenericType) clientMethod.getProxyMethod().getReturnType()).getTypeArguments()[0];
                            if (responseType instanceof ClassType) {
                                function.line(String.format("res.getT2().getDeserializedHeaders()))%s", endOfLine));
                            } else {
                                function.line(String.format("null))%s", endOfLine));
                            }
                        }
                    });
                    if (addContextParameter) {
                        function.line(String.format(
                            ".contextWrite(context -> context.putAll(FluxUtil.toReactorContext(%s.getContext()).readOnly()));",
                            clientMethod.getClientReference()));
                    }
                });
            });
        }
    }

    @Override
    protected void pagedSinglePageResponseConversion(ProxyMethod restAPIMethod, ClientMethod clientMethod,
        JavaSettings settings, JavaBlock function) {
        boolean contextInParameters = contextInParameters(clientMethod);
        boolean isLroPagination
            = GenericType.Response(ClassType.BINARY_DATA).equals(restAPIMethod.getReturnType().getClientType());
        if (isLroPagination && settings.isSyncStackEnabled()) {
            IType classType = clientMethod.getMethodPageDetails().getLroIntermediateType();
            // get final result
            function.line(String.format(
                "%1$s lroPageableResult = this.client.<%1$s, %1$s>getLroResult(res, %1$s.class, %1$s.class, %3$s).getFinalResult();",
                classType, clientMethod.getClientReference(), contextInParameters ? "context" : "Context.NONE"));
            // return line
            String nextLink = clientMethod.getMethodPageDetails().nonNullNextLink()
                ? nextLinkLine(clientMethod, null, "lroPageableResult")
                : "null";
            function.methodReturn(String.format(
                "new PagedResponseBase<>(res.getRequest(), res.getStatusCode(), res.getHeaders(), lroPageableResult.%1$s(), %2$s, null)",
                clientMethod.getMethodPageDetails().getSerializedItemName(), nextLink));
        } else {
            super.pagedSinglePageResponseConversion(restAPIMethod, clientMethod, settings, function);
        }
    }

    @Override
    protected void generateSimpleAsyncRestResponse(ClientMethod clientMethod, JavaType typeBlock,
        JavaSettings settings) {
        final ProxyMethod restAPIMethod = clientMethod.getProxyMethod();
        boolean addContextParameter = !contextInParameters(clientMethod);
        boolean mergeContextParameter = !addContextParameter;

        typeBlock.annotation("ServiceMethod(returns = ReturnType.SINGLE)");
        writeMethod(typeBlock, clientMethod.getMethodVisibility(), clientMethod.getDeclaration(), function -> {
            addValidations(function, clientMethod, settings);
            addOptionalAndConstantVariables(function, clientMethod, settings);
            applyParameterTransformations(function, clientMethod, settings);
            convertClientTypesToWireTypes(function, clientMethod);

            String restAPIMethodArgumentList = String.join(", ", clientMethod.getProxyMethodArguments(settings));
            String serviceMethodCall
                = String.format("service.%s(%s)", restAPIMethod.getName(), restAPIMethodArgumentList);
            if (mergeContextParameter) {
                function.line(String.format("context = %s.mergeContext(context);", clientMethod.getClientReference()));
            }
            if (addContextParameter) {
                function.line(String.format("return FluxUtil.withContext(context -> %s)", serviceMethodCall));
                function.indent(() -> {
                    function.line(String.format(
                        ".contextWrite(context -> context.putAll(FluxUtil.toReactorContext(%s.getContext()).readOnly()));",
                        clientMethod.getClientReference()));
                });
            } else {
                function.methodReturn(serviceMethodCall);
            }
        });
    }

    @Override
    protected void generateLongRunningAsync(ClientMethod clientMethod, JavaType typeBlock, JavaSettings settings) {
        final ProxyMethod restAPIMethod = clientMethod.getProxyMethod();

        typeBlock.annotation("ServiceMethod(returns = ReturnType.SINGLE)");
        String beginAsyncMethodName = MethodNamer.getLroBeginAsyncMethodName(restAPIMethod.getName());
        writeMethod(typeBlock, clientMethod.getMethodVisibility(), clientMethod.getDeclaration(), function -> {
            addOptionalVariables(function, clientMethod);
            function.line("return %s(%s)", beginAsyncMethodName, clientMethod.getArgumentList());
            function.indent(() -> {
                function.line(".last()");
                function
                    .line(String.format(".flatMap(%s::getLroFinalResultOrError);", clientMethod.getClientReference()));
            });
        });
    }

    @Override
    protected void generateLongRunningPlainSync(ClientMethod clientMethod, JavaType typeBlock, JavaSettings settings) {
        final ProxyMethod restAPIMethod = clientMethod.getProxyMethod();

        typeBlock.annotation("ServiceMethod(returns = ReturnType.SINGLE)");
        writeMethod(typeBlock, clientMethod.getMethodVisibility(), clientMethod.getDeclaration(), function -> {
            addOptionalVariables(function, clientMethod);
            String line = String.format("%s(%s).getFinalResult()",
                MethodNamer.getLroBeginMethodName(restAPIMethod.getName()), clientMethod.getArgumentList());
            if (clientMethod.getReturnValue().getType() != PrimitiveType.VOID) {
                function.methodReturn(line);
            } else {
                function.line(line + ";");
            }
        });
    }

    @Override
    protected void generateLongRunningBeginAsync(ClientMethod clientMethod, JavaType typeBlock, JavaSettings settings) {
        boolean mergeContextParameter = contextInParameters(clientMethod);
        String contextParam
            = mergeContextParameter ? "context" : String.format("%s.getContext()", clientMethod.getClientReference());;

        typeBlock.annotation("ServiceMethod(returns = ReturnType.LONG_RUNNING_OPERATION)");
        writeMethod(typeBlock, clientMethod.getMethodVisibility(), clientMethod.getDeclaration(), function -> {
            IType classType
                = ((GenericType) clientMethod.getReturnValue().getType().getClientType()).getTypeArguments()[1];

            addOptionalVariables(function, clientMethod);
            if (mergeContextParameter) {
                function.line(String.format("context = %s.mergeContext(context);", clientMethod.getClientReference()));
            }
            function.line("%s mono = %s(%s);", clientMethod.getProxyMethod().getReturnType().toString(),
                clientMethod.getProxyMethod().getSimpleAsyncRestResponseMethodName(), clientMethod.getArgumentList());
            if (classType instanceof GenericType) {
                // pageable LRO
                String typeReferenceGetType;
                if (settings.isStreamStyleSerialization()) {
                    typeReferenceGetType = "getJavaType";
                } else {
                    typeReferenceGetType = "getType";
                }
                function.line(
                    "return %1$s.<%2$s, %2$s>getLroResult(mono, %1$s.getHttpPipeline(), new TypeReference<%2$s>() {}.%3$s(), new TypeReference<%2$s>() {}.%3$s(), %4$s);",
                    clientMethod.getClientReference(), classType.toString(), typeReferenceGetType, contextParam);
            } else {
                function.line("return %s.<%s, %s>getLroResult(mono, %s.getHttpPipeline(), %s.class, %s.class, %s);",
                    clientMethod.getClientReference(), classType.toString(), classType.toString(),
                    clientMethod.getClientReference(), classType.toString(), classType.toString(), contextParam);
            }
        });
    }

    @Override
    protected void generateLongRunningBeginSync(ClientMethod clientMethod, JavaType typeBlock, JavaSettings settings) {
        final ProxyMethod restAPIMethod = clientMethod.getProxyMethod();
        boolean contextInParameters = contextInParameters(clientMethod);

        typeBlock.annotation("ServiceMethod(returns = ReturnType.LONG_RUNNING_OPERATION)");
        typeBlock.publicMethod(clientMethod.getDeclaration(), function -> {
            addOptionalVariables(function, clientMethod);
            function.line(String.format("Response<BinaryData> response = %s(%s);",
                MethodNamer.getSimpleRestResponseMethodName(restAPIMethod.getName()), clientMethod.getArgumentList()));
            IType classType
                = ((GenericType) clientMethod.getReturnValue().getType().getClientType()).getTypeArguments()[1];
            String contextArgument = contextInParameters ? "context" : "Context.NONE";
            String typeExpression;
            if (classType instanceof GenericType) {
                // pageable LRO
                String typeReferenceGetType;
                if (settings.isStreamStyleSerialization()) {
                    typeReferenceGetType = "getJavaType";
                } else {
                    typeReferenceGetType = "getType";
                }
                typeExpression = String.format("new TypeReference<%s>() {}.%s()", classType, typeReferenceGetType);
            } else {
                typeExpression = String.format("%s.class", classType);
            }
            function.methodReturn(String.format("this.client.<%3$s, %3$s>getLroResult(response, %1$s, %1$s, %2$s)",
                typeExpression, contextArgument, classType));
        });
    }

    private static IType returnTypeWithoutMono(IType returnType) {
        // need e.g. PagedResponse<T>
        IType returnTypeWithoutMono = returnType;
        if (returnType instanceof GenericType && ((GenericType) returnType).getName().equals("Mono")) {
            returnTypeWithoutMono = ((GenericType) returnType).getTypeArguments()[0];
        }
        return returnTypeWithoutMono;
    }
}
