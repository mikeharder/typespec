// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) TypeSpec Code Generator.

package server.path.multiple.generated;

// The Java test files under 'generated' package are generated for your reference.
// If you wish to modify these files, please copy them out of the 'generated' package, and modify there.
// See https://aka.ms/azsdk/dpg/java/tests for guide on adding a test.

import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.http.policy.HttpLogOptions;
import com.azure.core.test.TestMode;
import com.azure.core.test.TestProxyTestBase;
import com.azure.core.util.Configuration;
import server.path.multiple.MultipleClient;
import server.path.multiple.MultipleClientBuilder;

class MultipleClientTestBase extends TestProxyTestBase {
    protected MultipleClient multipleClient;

    @Override
    protected void beforeTest() {
        MultipleClientBuilder multipleClientbuilder
            = new MultipleClientBuilder().endpoint(Configuration.getGlobalConfiguration().get("ENDPOINT", "endpoint"))
                .httpClient(getHttpClientOrUsePlayback(getHttpClients().findFirst().orElse(null)))
                .httpLogOptions(new HttpLogOptions().setLogLevel(HttpLogDetailLevel.BASIC));
        if (getTestMode() == TestMode.RECORD) {
            multipleClientbuilder.addPolicy(interceptorManager.getRecordPolicy());
        }
        multipleClient = multipleClientbuilder.buildClient();

    }
}
