package ru.mirea.dms.storage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.mirea.dms.storage.dto.FileUploadRequest;
import ru.mirea.dms.storage.dto.FileDeleteRequest;
import ru.mirea.dms.storage.dto.FileResponse;
import ru.mirea.dms.storage.service.FileService;

@RestController
@RequestMapping("/api/storage")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<FileResponse> upload(@RequestBody FileUploadRequest request) {
        return ResponseEntity.ok(fileService.uploadFile(request));
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam String bucketName,
                                           @RequestParam String filename) {
        byte[] data = fileService.downloadFile(bucketName, filename);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());

        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<FileResponse> delete(@RequestBody FileDeleteRequest request) {
        return ResponseEntity.ok(fileService.deleteFile(request));
    }
}