package org.aws.s3.api.controller;

import lombok.RequiredArgsConstructor;

import org.aws.s3.core.domain.File;
import org.aws.s3.core.domain.port.in.UploadFileUseCase;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@ComponentScan(basePackages = "org.aws.s3.core.domain.port.in")
public class FileController {

    private final UploadFileUseCase upload;

    @PostMapping("/api/files")
    public ResponseEntity<String> upload(
            @RequestParam String fileName,
            @RequestParam MultipartFile file) throws IOException {

        File domainFile = new File(UUID.randomUUID(), fileName, file.getSize());
        String location = String.valueOf(upload.handle(domainFile, file.getInputStream()));
        return ResponseEntity.ok(location);
    }
}
