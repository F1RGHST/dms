package ru.mirea.dms.storage.service;

import ru.mirea.dms.storage.dto.FileUploadRequest;
import ru.mirea.dms.storage.dto.FileDeleteRequest;
import ru.mirea.dms.storage.dto.FileResponse;

public interface FileService {
    FileResponse uploadFile(FileUploadRequest request);
    byte[] downloadFile(String bucketName, String filename);
    FileResponse deleteFile(FileDeleteRequest request);
}