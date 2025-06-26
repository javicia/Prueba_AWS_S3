package org.aws.s3.core.domain.port;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor

public class File {

    private final UUID Id;
    private final String fileName;
    private final byte[] content;

}
