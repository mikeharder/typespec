package type.union;

import io.clientcore.core.annotations.Metadata;
import io.clientcore.core.annotations.ServiceClient;
import io.clientcore.core.http.exceptions.HttpResponseException;
import io.clientcore.core.http.models.RequestOptions;
import io.clientcore.core.http.models.Response;
import io.clientcore.core.models.binarydata.BinaryData;
import type.union.implementation.MixedTypesImpl;
import type.union.implementation.SendRequest;

/**
 * Initializes a new instance of the synchronous UnionClient type.
 */
@ServiceClient(builder = UnionClientBuilder.class)
public final class MixedTypesClient {
    @Metadata(generated = true)
    private final MixedTypesImpl serviceClient;

    /**
     * Initializes an instance of MixedTypesClient class.
     * 
     * @param serviceClient the service client implementation.
     */
    @Metadata(generated = true)
    MixedTypesClient(MixedTypesImpl serviceClient) {
        this.serviceClient = serviceClient;
    }

    /**
     * The get operation.
     * <p><strong>Response Body Schema</strong></p>
     * 
     * <pre>
     * {@code
     * {
     *     prop (Required): {
     *         model: BinaryData (Required)
     *         literal: BinaryData (Required)
     *         int: BinaryData (Required)
     *         boolean: BinaryData (Required)
     *         array (Required): [
     *             BinaryData (Required)
     *         ]
     *     }
     * }
     * }
     * </pre>
     * 
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the service returns an error.
     * @return the response.
     */
    @Metadata(generated = true)
    public Response<GetResponse> getWithResponse(RequestOptions requestOptions) {
        return this.serviceClient.getWithResponse(requestOptions);
    }

    /**
     * The send operation.
     * <p><strong>Request Body Schema</strong></p>
     * 
     * <pre>
     * {@code
     * {
     *     prop (Required): {
     *         model: BinaryData (Required)
     *         literal: BinaryData (Required)
     *         int: BinaryData (Required)
     *         boolean: BinaryData (Required)
     *         array (Required): [
     *             BinaryData (Required)
     *         ]
     *     }
     * }
     * }
     * </pre>
     * 
     * @param sendRequest The sendRequest parameter.
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the service returns an error.
     * @return the response.
     */
    @Metadata(generated = true)
    public Response<Void> sendWithResponse(BinaryData sendRequest, RequestOptions requestOptions) {
        return this.serviceClient.sendWithResponse(sendRequest, requestOptions);
    }

    /**
     * The get operation.
     * 
     * @throws HttpResponseException thrown if the service returns an error.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the response.
     */
    @Metadata(generated = true)
    public GetResponse get() {
        // Generated convenience method for getWithResponse
        RequestOptions requestOptions = new RequestOptions();
        return getWithResponse(requestOptions).getValue();
    }

    /**
     * The send operation.
     * 
     * @param prop The prop parameter.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws HttpResponseException thrown if the service returns an error.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    @Metadata(generated = true)
    public void send(MixedTypesCases prop) {
        // Generated convenience method for sendWithResponse
        RequestOptions requestOptions = new RequestOptions();
        SendRequest sendRequestObj = new SendRequest(prop);
        BinaryData sendRequest = BinaryData.fromObject(sendRequestObj);
        sendWithResponse(sendRequest, requestOptions).getValue();
    }
}
