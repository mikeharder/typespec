// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) TypeSpec Code Generator.

package tsptest.longrunning.generated;

import com.azure.core.util.Configuration;
import com.azure.core.util.polling.SyncPoller;
import java.util.HashMap;
import java.util.Map;
import tsptest.longrunning.LongRunningClient;
import tsptest.longrunning.LongRunningClientBuilder;
import tsptest.longrunning.models.JobData;
import tsptest.longrunning.models.JobResult;
import tsptest.longrunning.models.JobResultResult;

public class LongRunningCreateJob {
    public static void main(String[] args) {
        LongRunningClient longRunningClient
            = new LongRunningClientBuilder().endpoint(Configuration.getGlobalConfiguration().get("ENDPOINT"))
                .buildClient();
        // BEGIN:tsptest.longrunning.generated.create-job.long-running-create-job
        SyncPoller<JobResult, JobResultResult> response = longRunningClient
            .beginCreateJob(new JobData(mapOf("max", 15.0D, "min", 14.0D, "average", 14.3D)).setConfiguration("{}"));
        // END:tsptest.longrunning.generated.create-job.long-running-create-job
    }

    // Use "Map.of" if available
    @SuppressWarnings("unchecked")
    private static <T> Map<String, T> mapOf(Object... inputs) {
        Map<String, T> map = new HashMap<>();
        for (int i = 0; i < inputs.length; i += 2) {
            String key = (String) inputs[i];
            T value = (T) inputs[i + 1];
            map.put(key, value);
        }
        return map;
    }
}
