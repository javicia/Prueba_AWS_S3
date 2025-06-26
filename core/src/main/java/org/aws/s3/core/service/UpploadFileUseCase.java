package org.aws.s3.core.service;

import lombok.RequiredArgsConstructor;
import org.aws.s3.core.domain.port.File;
import org.aws.s3.core.domain.port.out.FileStoragePort;
import org.aws.s3.core.domain.port.out.NotificationPort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpploadFileUseCase {

    private final FileStoragePort storage;
    private final NotificationPort notification;

    public void uploadF(File file) {
        storage.save(file);
        notification.notifyFileUploaded(file.getId().toString());
    }
}
