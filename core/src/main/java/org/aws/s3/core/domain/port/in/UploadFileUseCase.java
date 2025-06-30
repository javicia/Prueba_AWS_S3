package org.aws.s3.core.domain.port.in;

import lombok.RequiredArgsConstructor;
import org.aws.s3.core.domain.File;
import org.aws.s3.core.domain.port.out.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UploadFileUseCase {

    private final FileRepositoryPort repository;
    private final FileContentStoragePort storage;
    private final NotificationPort notifier;
    private final LambdaInvokerPort lambdaInvoker;
    private final MetricsPort metrics;


    public UUID handle(File file, InputStream content) {
        String location = storage.store(file, content);
        repository.save(file);
        notifier.notifyFileUploaded(file.getId(), location);
        lambdaInvoker.invokeFileUploded(file.getId(), location);
        metrics.recordFileUpload();
        return file.getId();
    }
}
