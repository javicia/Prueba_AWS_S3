package org.aws.s3.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.aws.s3.core.domain.File;
import org.aws.s3.core.domain.port.out.FileRepositoryPort;
import org.aws.s3.infrastructure.persistence.entity.FileEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FileRepositoryAdapter implements FileRepositoryPort {

    private final SpringDataFileRepository jpa;

    @Value("${app.aws.s3.bucket}")
    private String bucket;


    @Override
    public void save(File file) {
        FileEntity entity = new FileEntity();
        entity.setId(file.getId());
        entity.setFileName(file.getFileName());
        entity.setSize(file.getSize());
        entity.setLocation("s3://" + bucket + "/" + file.getFileName());
        jpa.save(entity);
    }

    @Override
    public Optional<File> findById(UUID id) {
        return jpa.findById(id).map(e -> new File(e.getId(), e.getFileName(), e.getSize()));
    }
}
