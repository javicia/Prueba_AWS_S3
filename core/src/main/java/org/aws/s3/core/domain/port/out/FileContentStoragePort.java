package org.aws.s3.core.domain.port.out;

import org.aws.s3.core.domain.File;

import java.io.InputStream;

public interface FileContentStoragePort {
    String store(File file, InputStream content);
}
