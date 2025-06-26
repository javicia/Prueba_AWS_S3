package org.aws.s3.infrastructure.persistence.repository;

import org.aws.s3.infrastructure.persistence.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataFileRepository extends JpaRepository<FileEntity, UUID> {

}
