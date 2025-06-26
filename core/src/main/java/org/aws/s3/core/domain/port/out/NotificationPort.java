package org.aws.s3.core.domain.port.out;

import java.util.UUID;

public interface NotificationPort {
    void notifyFileUploaded(UUID fileId, String location);
}
