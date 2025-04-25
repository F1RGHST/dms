package ru.mirea.dms.storage.service;

import ru.mirea.dms.storage.dto.FileInfo;
import io.minio.*;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

  private final MinioClient minio;
  private final String bucket;

  public FileServiceImpl(MinioClient minio,
                         @Value("${minio.bucket}") String bucket) throws Exception {
    this.minio  = minio;
    this.bucket = bucket;

    boolean exists = minio.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
    if (!exists) {
      minio.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
    }
  }

  @Override
  public FileInfo upload(MultipartFile file) throws Exception {
    String objectName = UUID.randomUUID() + "-" + file.getOriginalFilename();
    PutObjectArgs args = PutObjectArgs.builder()
      .bucket(bucket)
      .object(objectName)
      .stream(file.getInputStream(), file.getSize(), -1)
      .contentType(file.getContentType())
      .build();
    minio.putObject(args);

    FileInfo info = new FileInfo();
    info.setObjectName(objectName);
    info.setSize(file.getSize());
    info.setContentType(file.getContentType());
    info.setLastModified(
      minio.statObject(StatObjectArgs.builder()
         .bucket(bucket).object(objectName).build())
         .lastModified().atZoneSameInstant(ZoneOffset.UTC));
    return info;
  }

  @Override
  public InputStream download(String objectName) throws Exception {
    return minio.getObject(GetObjectArgs.builder()
      .bucket(bucket).object(objectName).build());
  }

  @Override
  public FileInfo update(String objectName, MultipartFile file) throws Exception {
    PutObjectArgs args = PutObjectArgs.builder()
      .bucket(bucket)
      .object(objectName)
      .stream(file.getInputStream(), file.getSize(), -1)
      .contentType(file.getContentType())
      .build();
    minio.putObject(args);
    return upload(file);
  }

  @Override
  public void delete(String objectName) throws Exception {
    minio.removeObject(RemoveObjectArgs.builder()
      .bucket(bucket).object(objectName).build());
  }

  @Override
  public List<FileInfo> listAll() throws Exception {
    List<FileInfo> all = new ArrayList<>();
    Iterable<Result<Item>> results = minio.listObjects(
      ListObjectsArgs.builder().bucket(bucket).build());
    for (Result<Item> r : results) {
      Item item = r.get();
      FileInfo info = new FileInfo();
      info.setObjectName(item.objectName());
      info.setSize(item.size());
      info.setContentType(null);
      info.setLastModified(item.lastModified().atZoneSameInstant(ZoneOffset.UTC));
      all.add(info);
    }
    return all;
  }
}