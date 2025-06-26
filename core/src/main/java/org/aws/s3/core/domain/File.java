package org.aws.s3.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor

public class File {

    private final UUID id;
    private final String fileName;
    private final long size;

}
