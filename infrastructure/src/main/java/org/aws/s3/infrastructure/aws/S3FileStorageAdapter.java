package org.aws.s3.infrastructure.aws;

import lombok.RequiredArgsConstructor;
import org.aws.s3.core.domain.File;
import org.aws.s3.core.domain.port.out.FileContentStoragePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class S3FileStorageAdapter implements FileContentStoragePort {

    private final S3Client s3;

    @Value("${app.aws.s3.bucket}")
    private String bucket;

    @Override
    public String store(File file, InputStream content) {
        String key = file.getId() + "-" + file.getFileName();
        PutObjectRequest req = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        s3.putObject(req, RequestBody.fromInputStream(content, file.getSize()));
        return String.format("s3://%s/%s", bucket, key);
    }
}
