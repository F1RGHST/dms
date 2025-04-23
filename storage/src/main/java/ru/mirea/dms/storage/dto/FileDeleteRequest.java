package ru.mirea.dms.storage.dto;

import lombok.Data;

@Data
public class FileDeleteRequest {
    private String bucketName;
    private String filename;
}