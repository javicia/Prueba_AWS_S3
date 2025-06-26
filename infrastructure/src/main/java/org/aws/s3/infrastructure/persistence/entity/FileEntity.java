package org.aws.s3.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "files")
@Getter
@Setter
public class FileEntity {
    @Id
    private UUID id;
    private String fileName;
    private long size;
    private String location;
}
