package versioning.renamedfrom;

import io.clientcore.core.annotations.Metadata;
import io.clientcore.core.annotations.MetadataProperties;
import io.clientcore.core.annotations.ReturnType;
import io.clientcore.core.annotations.ServiceClient;
import io.clientcore.core.annotations.ServiceMethod;
import io.clientcore.core.http.models.HttpResponseException;
import io.clientcore.core.http.models.RequestContext;
import io.clientcore.core.http.models.Response;
import versioning.renamedfrom.implementation.NewInterfacesImpl;

/**
 * Initializes a new instance of the synchronous RenamedFromClient type.
 */
@ServiceClient(builder = RenamedFromClientBuilder.class)
public final class NewInterfaceClient {
    @Metadata(properties = { MetadataProperties.GENERATED })
    private final NewInterfacesImpl serviceClient;

    /**
     * Initializes an instance of NewInterfaceClient class.
     * 
     * @param serviceClient the service client implementation.
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    NewInterfaceClient(NewInterfacesImpl serviceClient) {
        this.serviceClient = serviceClient;
    }

    /**
     * The newOpInNewInterface operation.
     * 
     * @param body The body parameter.
     * @param requestContext The context to configure the HTTP request before HTTP client sends it.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws HttpResponseException thrown if the service returns an error.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the response.
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Response<NewModel> newOpInNewInterfaceWithResponse(NewModel body, RequestContext requestContext) {
        return this.serviceClient.newOpInNewInterfaceWithResponse(body, requestContext);
    }

    /**
     * The newOpInNewInterface operation.
     * 
     * @param body The body parameter.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws HttpResponseException thrown if the service returns an error.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the response.
     */
    @Metadata(properties = { MetadataProperties.GENERATED })
    @ServiceMethod(returns = ReturnType.SINGLE)
    public NewModel newOpInNewInterface(NewModel body) {
        return newOpInNewInterfaceWithResponse(body, RequestContext.none()).getValue();
    }
}
