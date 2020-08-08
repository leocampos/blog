package de.campos.bigdata.monitoring;

import com.google.api.Metric;
import com.google.api.MonitoredResource;
import com.google.cloud.monitoring.v3.MetricServiceClient;
import com.google.monitoring.v3.*;
import com.google.protobuf.util.Timestamps;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MetricSender {
    private final String metricName;
    private final String namespace;
    private final String nodeId;
    private final MetricServiceClient client;
    private long time;
    private Double value; // Using Double to keep the null value as not set.
    private final Map<String, String> labels = new HashMap<>();

    public MetricSender(String metricName, String namespace, String nodeId, MetricServiceClient client) {
        this.metricName = Objects.requireNonNull(metricName, "The metric name cannot be null.");
        this.namespace = Objects.requireNonNull(namespace, "The namespace cannot be null.");
        this.nodeId = Objects.requireNonNull(nodeId, "The nodeId cannot be null.");
        this.client = Objects.requireNonNull(client, "The service client cannot be null.");
        time = new Date().getTime();
    }

    public MetricSender when(long time) {
        this.time = time;
        return this;
    }

    public void sendTo(String projectId, String location) {
        Objects.requireNonNull(value, "Value must have been set before 'send' is invoked.");

        Point point = createPoint();

        Metric metric = Metric.newBuilder().setType(metricName).putAllLabels(labels).build();
        // Prepares the time series request, it puts everything we created together
        TimeSeries timeSeries =
                TimeSeries.newBuilder()
                        .setMetric(metric)
                        .setResource(MonitoredResource.newBuilder()
                                .setType("generic_node")
                                .putLabels("project_id", projectId)
                                .putLabels("namespace", namespace)
                                .putLabels("location", location)
                                .putLabels("node_id", nodeId))
                        .addPoints(point)
                        .build();

        final String resourceName = ProjectName.of(projectId).toString();
        CreateTimeSeriesRequest request =
                CreateTimeSeriesRequest.newBuilder()
                        .setName(resourceName)
                        .addTimeSeries(timeSeries)
                        .build();

        client.createTimeSeries(request);
    }

    private Point createPoint() {
        // Creating the value (setDoubleValue because this is just an example to send a double)
        final TypedValue typedValue = TypedValue.newBuilder().setDoubleValue(value).build();

        // This tells the WHEN of this data point.
        TimeInterval interval =
                TimeInterval.newBuilder().setEndTime(Timestamps.fromMillis(time)).build();

        // Create the data point
        return Point.newBuilder().setInterval(interval).setValue(typedValue).build();
    }

    public MetricSender value(Double value) {
        this.value = value;
        return this;
    }

    public MetricSender addLabel(String labelName, String labelValue) {
        labels.put(labelName, labelValue);

        return this;
    }
}
