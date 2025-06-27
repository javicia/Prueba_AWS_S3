package org.aws.s3.infrastructure.aws;

import lombok.RequiredArgsConstructor;
import org.aws.s3.core.domain.port.out.NotificationPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsClient;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SnsNotificationAdapter implements NotificationPort {

    private final SnsClient sns;

    @Value("${app.aws.sns.topicArn}")
    private String topicArn;

    @Override
    public void notifyFileUploaded(UUID fileId, String location) {
    String msg = String.format("New file uploaded: %s (%s)", fileId, location);
        sns.publish(r -> r.topicArn(topicArn)
                .message(msg));
    }
}
