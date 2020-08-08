package de.campos.bigdata.monitoring;

import com.google.cloud.monitoring.v3.MetricServiceClient;

import java.util.*;

public class MetricsClient {
    public static void main(String[] args) throws Exception {
        String metricName = "custom.googleapis.com/bigquery/my-metric-name";
        final long timestampOfTheDataPoint = new Date().getTime();
        double valueToSend = 5.1;

        String namespace = "bigquery.teste";
        String nodeId = "test";

        new MetricSender(metricName, namespace, nodeId, MetricServiceClient.create())
                .when(timestampOfTheDataPoint)
                .value(valueToSend)
                .addLabel("description", "This is just an example of metric.")
                .addLabel("org_unit", "your department")
                .sendTo("your-project-id", "europe-west3");
    }
}
