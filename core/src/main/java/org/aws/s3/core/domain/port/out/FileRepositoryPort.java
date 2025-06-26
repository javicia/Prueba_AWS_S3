package org.aws.s3.core.domain.port.out;

import org.aws.s3.core.domain.File;

import java.util.Optional;
import java.util.UUID;

public interface FileRepositoryPort {
    void save(File file);
    Optional<File> findById(UUID id);
}
