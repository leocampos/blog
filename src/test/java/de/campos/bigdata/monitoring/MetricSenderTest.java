package de.campos.bigdata.monitoring;

import com.google.cloud.monitoring.v3.MetricServiceClient;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Given that the most import method for us inside MetricServiceClient is final, Mockito alone can't be used to verify
 * it, so the tests here ended up not being that big of a deal :"(
 */
@RunWith(MockitoJUnitRunner.class)
public class MetricSenderTest extends TestCase {
    private static final String METRIC_NAME = "metric-name";
    private static final String NAMESPACE = "namespace";
    private static final String NODE_ID = "node-id";

    @Mock
    private MetricServiceClient client;

    private MetricSender metricSender;

    @Before
    public void setUp() {
        metricSender = new MetricSender(METRIC_NAME, NAMESPACE, NODE_ID, client);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorMetricNameShouldNotBeNull() {
        new MetricSender(null, NAMESPACE, NODE_ID, client);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNamespaceShouldNotBeNull() {
        new MetricSender( METRIC_NAME, null, NODE_ID, client);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNodeIdShouldNotBeNull() {
        new MetricSender( METRIC_NAME, NAMESPACE, null, client);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorClientShouldNotBeNull() {
        new MetricSender(null, NAMESPACE, NODE_ID, client);
    }

    @Test(expected = NullPointerException.class)
    public void testSendToWithoutValueShouldThrowNPE() {
        metricSender.sendTo("projectId", "location");
    }
}