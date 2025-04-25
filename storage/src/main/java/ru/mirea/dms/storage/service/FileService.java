package ru.mirea.dms.storage.service;

import ru.mirea.dms.storage.dto.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface FileService {
    FileInfo upload(MultipartFile file) throws Exception;
    InputStream download(String objectName) throws Exception;
    FileInfo update(String objectName, MultipartFile file) throws Exception;
    void delete(String objectName) throws Exception;
    List<FileInfo> listAll() throws Exception;
}