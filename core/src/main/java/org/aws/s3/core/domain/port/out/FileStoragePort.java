package org.aws.s3.core.domain.port.out;

import org.aws.s3.core.domain.port.File;

public interface FileStoragePort {
    void save(File file);
}
