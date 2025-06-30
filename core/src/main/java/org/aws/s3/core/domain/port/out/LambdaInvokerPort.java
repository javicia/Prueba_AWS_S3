package org.aws.s3.core.domain.port.out;

import java.util.UUID;

public interface LambdaInvokerPort {

    void invokeFileUploded(UUID fileId, String location);
}
