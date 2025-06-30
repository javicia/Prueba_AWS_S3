package org.aws.s3.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.aws.s3.core.domain.port.out.MetricsPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.MetricDatum;
import software.amazon.awssdk.services.cloudwatch.model.PutMetricDataRequest;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class CloudWatchMetricsAdapter implements MetricsPort {

    private final CloudWatchClient cw;

    @Value("${app.aws.cloudwatch.namespace:prueba_AWS_S3}")
    private String namespace;

    @Override
    public void recordFileUpload() {
        MetricDatum datum = MetricDatum.builder()
                .metricName("FilesUploaded")
                .value(1.0)
                .timestamp(Instant.now())
                .build();

        cw.putMetricData(
                        PutMetricDataRequest.builder()
                        .namespace(namespace)
                        .metricData(datum)
                        .build()
        );

    }
}
