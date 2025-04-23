package ru.mirea.dms.storage.service;

import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.mirea.dms.storage.dto.FileUploadRequest;
import ru.mirea.dms.storage.dto.FileDeleteRequest;
import ru.mirea.dms.storage.dto.FileResponse;
import ru.mirea.dms.storage.model.FileEntity;
import ru.mirea.dms.storage.repository.FileRepository;
import ru.mirea.dms.storage.service.FileService;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.time.LocalDateTime;

@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    private final MinioClient minioClient;

    public FileServiceImpl(@Value("${minio.url}") String url,
                           @Value("${minio.access-key}") String accessKey,
                           @Value("${minio.secret-key}") String secretKey,
                           FileRepository fileRepository) {
        this.minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
        this.fileRepository = fileRepository;
    }

    @Override
    public FileResponse uploadFile(FileUploadRequest request) {
        try {
            byte[] data = Base64.getDecoder().decode(request.getBase64Content());

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(request.getBucketName())
                            .object(request.getFilename())
                            .stream(new ByteArrayInputStream(data), data.length, -1)
                            .contentType(request.getContentType())
                            .build()
            );

            FileEntity fileEntity = new FileEntity();
            fileEntity.setFilename(request.getFilename());
            fileEntity.setBucket(request.getBucketName());
            fileEntity.setUploadDate(LocalDateTime.now());
            fileRepository.save(fileEntity);

            return new FileResponse("Файл успешно загружен");

        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки файла", e);
        }
    }

    @Override
    public byte[] downloadFile(String bucketName, String filename) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filename)
                            .build()
            ).readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка скачивания файла", e);
        }
    }

    @Override
    public FileResponse deleteFile(FileDeleteRequest request) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(request.getBucketName())
                            .object(request.getFilename())
                            .build()
            );
            return new FileResponse("Файл успешно удален");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка удаления файла", e);
        }
    }
}
