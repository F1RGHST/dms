package ru.mirea.dms.storage.dto;

import lombok.Data;

@Data
public class FileUploadRequest {
    private String bucketName;
    private String filename;
    private String base64Content;
    private String contentType;
}