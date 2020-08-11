package de.campos.bigdata.monitoring;

import com.google.cloud.monitoring.v3.MetricServiceClient;

import java.util.*;

/**
 * @author Leonardo Campos
 * This client is just an example on how to use the class MetricSender to send a single data point to Google Cloud
 * Monitoring.
 */
public class MetricsClient {
    public static void main(String[] args) throws Exception {
        final long timestampOfTheDataPoint = new Date().getTime();
        double valueToSend = 5.1;
        String metricName = "custom.googleapis.com/bigquery/my-metric-name";
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
