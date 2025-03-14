// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) TypeSpec Code Generator.

package type.model.inheritance.enumdiscriminator;

import com.azure.core.annotation.Generated;
import com.azure.core.annotation.ReturnType;
import com.azure.core.annotation.ServiceClient;
import com.azure.core.annotation.ServiceMethod;
import com.azure.core.exception.ClientAuthenticationException;
import com.azure.core.exception.HttpResponseException;
import com.azure.core.exception.ResourceModifiedException;
import com.azure.core.exception.ResourceNotFoundException;
import com.azure.core.http.rest.RequestOptions;
import com.azure.core.http.rest.Response;
import com.azure.core.util.BinaryData;
import com.azure.core.util.FluxUtil;
import reactor.core.publisher.Mono;
import type.model.inheritance.enumdiscriminator.implementation.EnumDiscriminatorClientImpl;
import type.model.inheritance.enumdiscriminator.models.Dog;
import type.model.inheritance.enumdiscriminator.models.Snake;

/**
 * Initializes a new instance of the asynchronous EnumDiscriminatorClient type.
 */
@ServiceClient(builder = EnumDiscriminatorClientBuilder.class, isAsync = true)
public final class EnumDiscriminatorAsyncClient {
    @Generated
    private final EnumDiscriminatorClientImpl serviceClient;

    /**
     * Initializes an instance of EnumDiscriminatorAsyncClient class.
     * 
     * @param serviceClient the service client implementation.
     */
    @Generated
    EnumDiscriminatorAsyncClient(EnumDiscriminatorClientImpl serviceClient) {
        this.serviceClient = serviceClient;
    }

    /**
     * Receive model with extensible enum discriminator type.
     * <p><strong>Response Body Schema</strong></p>
     * 
     * <pre>
     * {@code
     * {
     *     kind: String(golden) (Required)
     *     weight: int (Required)
     * }
     * }
     * </pre>
     * 
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return test extensible enum type for discriminator along with {@link Response} on successful completion of
     * {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<BinaryData>> getExtensibleModelWithResponse(RequestOptions requestOptions) {
        return this.serviceClient.getExtensibleModelWithResponseAsync(requestOptions);
    }

    /**
     * Send model with extensible enum discriminator type.
     * <p><strong>Request Body Schema</strong></p>
     * 
     * <pre>
     * {@code
     * {
     *     kind: String(golden) (Required)
     *     weight: int (Required)
     * }
     * }
     * </pre>
     * 
     * @param input Dog to create.
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> putExtensibleModelWithResponse(BinaryData input, RequestOptions requestOptions) {
        return this.serviceClient.putExtensibleModelWithResponseAsync(input, requestOptions);
    }

    /**
     * Get a model omitting the discriminator.
     * <p><strong>Response Body Schema</strong></p>
     * 
     * <pre>
     * {@code
     * {
     *     kind: String(golden) (Required)
     *     weight: int (Required)
     * }
     * }
     * </pre>
     * 
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return a model omitting the discriminator along with {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<BinaryData>>
        getExtensibleModelMissingDiscriminatorWithResponse(RequestOptions requestOptions) {
        return this.serviceClient.getExtensibleModelMissingDiscriminatorWithResponseAsync(requestOptions);
    }

    /**
     * Get a model containing discriminator value never defined.
     * <p><strong>Response Body Schema</strong></p>
     * 
     * <pre>
     * {@code
     * {
     *     kind: String(golden) (Required)
     *     weight: int (Required)
     * }
     * }
     * </pre>
     * 
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return a model containing discriminator value never defined along with {@link Response} on successful completion
     * of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<BinaryData>> getExtensibleModelWrongDiscriminatorWithResponse(RequestOptions requestOptions) {
        return this.serviceClient.getExtensibleModelWrongDiscriminatorWithResponseAsync(requestOptions);
    }

    /**
     * Receive model with fixed enum discriminator type.
     * <p><strong>Response Body Schema</strong></p>
     * 
     * <pre>
     * {@code
     * {
     *     kind: String(cobra) (Required)
     *     length: int (Required)
     * }
     * }
     * </pre>
     * 
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return test fixed enum type for discriminator along with {@link Response} on successful completion of
     * {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<BinaryData>> getFixedModelWithResponse(RequestOptions requestOptions) {
        return this.serviceClient.getFixedModelWithResponseAsync(requestOptions);
    }

    /**
     * Send model with fixed enum discriminator type.
     * <p><strong>Request Body Schema</strong></p>
     * 
     * <pre>
     * {@code
     * {
     *     kind: String(cobra) (Required)
     *     length: int (Required)
     * }
     * }
     * </pre>
     * 
     * @param input Snake to create.
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> putFixedModelWithResponse(BinaryData input, RequestOptions requestOptions) {
        return this.serviceClient.putFixedModelWithResponseAsync(input, requestOptions);
    }

    /**
     * Get a model omitting the discriminator.
     * <p><strong>Response Body Schema</strong></p>
     * 
     * <pre>
     * {@code
     * {
     *     kind: String(cobra) (Required)
     *     length: int (Required)
     * }
     * }
     * </pre>
     * 
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return a model omitting the discriminator along with {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<BinaryData>> getFixedModelMissingDiscriminatorWithResponse(RequestOptions requestOptions) {
        return this.serviceClient.getFixedModelMissingDiscriminatorWithResponseAsync(requestOptions);
    }

    /**
     * Get a model containing discriminator value never defined.
     * <p><strong>Response Body Schema</strong></p>
     * 
     * <pre>
     * {@code
     * {
     *     kind: String(cobra) (Required)
     *     length: int (Required)
     * }
     * }
     * </pre>
     * 
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return a model containing discriminator value never defined along with {@link Response} on successful completion
     * of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<BinaryData>> getFixedModelWrongDiscriminatorWithResponse(RequestOptions requestOptions) {
        return this.serviceClient.getFixedModelWrongDiscriminatorWithResponseAsync(requestOptions);
    }

    /**
     * Receive model with extensible enum discriminator type.
     * 
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return test extensible enum type for discriminator on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Dog> getExtensibleModel() {
        // Generated convenience method for getExtensibleModelWithResponse
        RequestOptions requestOptions = new RequestOptions();
        return getExtensibleModelWithResponse(requestOptions).flatMap(FluxUtil::toMono)
            .map(protocolMethodData -> protocolMethodData.toObject(Dog.class));
    }

    /**
     * Send model with extensible enum discriminator type.
     * 
     * @param input Dog to create.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return A {@link Mono} that completes when a successful response is received.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Void> putExtensibleModel(Dog input) {
        // Generated convenience method for putExtensibleModelWithResponse
        RequestOptions requestOptions = new RequestOptions();
        return putExtensibleModelWithResponse(BinaryData.fromObject(input), requestOptions).flatMap(FluxUtil::toMono);
    }

    /**
     * Get a model omitting the discriminator.
     * 
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return a model omitting the discriminator on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Dog> getExtensibleModelMissingDiscriminator() {
        // Generated convenience method for getExtensibleModelMissingDiscriminatorWithResponse
        RequestOptions requestOptions = new RequestOptions();
        return getExtensibleModelMissingDiscriminatorWithResponse(requestOptions).flatMap(FluxUtil::toMono)
            .map(protocolMethodData -> protocolMethodData.toObject(Dog.class));
    }

    /**
     * Get a model containing discriminator value never defined.
     * 
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return a model containing discriminator value never defined on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Dog> getExtensibleModelWrongDiscriminator() {
        // Generated convenience method for getExtensibleModelWrongDiscriminatorWithResponse
        RequestOptions requestOptions = new RequestOptions();
        return getExtensibleModelWrongDiscriminatorWithResponse(requestOptions).flatMap(FluxUtil::toMono)
            .map(protocolMethodData -> protocolMethodData.toObject(Dog.class));
    }

    /**
     * Receive model with fixed enum discriminator type.
     * 
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return test fixed enum type for discriminator on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Snake> getFixedModel() {
        // Generated convenience method for getFixedModelWithResponse
        RequestOptions requestOptions = new RequestOptions();
        return getFixedModelWithResponse(requestOptions).flatMap(FluxUtil::toMono)
            .map(protocolMethodData -> protocolMethodData.toObject(Snake.class));
    }

    /**
     * Send model with fixed enum discriminator type.
     * 
     * @param input Snake to create.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return A {@link Mono} that completes when a successful response is received.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Void> putFixedModel(Snake input) {
        // Generated convenience method for putFixedModelWithResponse
        RequestOptions requestOptions = new RequestOptions();
        return putFixedModelWithResponse(BinaryData.fromObject(input), requestOptions).flatMap(FluxUtil::toMono);
    }

    /**
     * Get a model omitting the discriminator.
     * 
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return a model omitting the discriminator on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Snake> getFixedModelMissingDiscriminator() {
        // Generated convenience method for getFixedModelMissingDiscriminatorWithResponse
        RequestOptions requestOptions = new RequestOptions();
        return getFixedModelMissingDiscriminatorWithResponse(requestOptions).flatMap(FluxUtil::toMono)
            .map(protocolMethodData -> protocolMethodData.toObject(Snake.class));
    }

    /**
     * Get a model containing discriminator value never defined.
     * 
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return a model containing discriminator value never defined on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Snake> getFixedModelWrongDiscriminator() {
        // Generated convenience method for getFixedModelWrongDiscriminatorWithResponse
        RequestOptions requestOptions = new RequestOptions();
        return getFixedModelWrongDiscriminatorWithResponse(requestOptions).flatMap(FluxUtil::toMono)
            .map(protocolMethodData -> protocolMethodData.toObject(Snake.class));
    }
}
