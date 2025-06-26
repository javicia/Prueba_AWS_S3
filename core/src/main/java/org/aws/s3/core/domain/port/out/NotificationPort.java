package org.aws.s3.core.domain.port.out;

public interface NotificationPort {
    void notifyFileUploaded(String fileId);
}
