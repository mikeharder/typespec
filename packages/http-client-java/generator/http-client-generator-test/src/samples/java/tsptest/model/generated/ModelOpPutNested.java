// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) TypeSpec Code Generator.

package tsptest.model.generated;

import com.azure.core.util.Configuration;
import tsptest.model.ModelClient;
import tsptest.model.ModelClientBuilder;
import tsptest.model.models.NestedModel;

public class ModelOpPutNested {
    public static void main(String[] args) {
        ModelClient modelClient
            = new ModelClientBuilder().endpoint(Configuration.getGlobalConfiguration().get("ENDPOINT")).buildClient();
        // BEGIN:tsptest.model.generated.modelopputnested.modelopputnested
        NestedModel response = modelClient.putNested(null);
        // END:tsptest.model.generated.modelopputnested.modelopputnested
    }
}