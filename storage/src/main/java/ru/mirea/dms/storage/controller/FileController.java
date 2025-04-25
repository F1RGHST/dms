package ru.mirea.dms.storage.controller;

import ru.mirea.dms.storage.dto.FileInfo;
import ru.mirea.dms.storage.service.FileService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

  private final FileService fileService;
  public FileController(FileService fileService) {
    this.fileService = fileService;
  }

  @PostMapping
  public FileInfo upload(@RequestParam("file") MultipartFile file) throws Exception {
    return fileService.upload(file);
  }

  @GetMapping
  public List<FileInfo> list() throws Exception {
    return fileService.listAll();
  }

  @GetMapping("/{objectName}")
  public ResponseEntity<InputStreamResource> download(
      @PathVariable String objectName) throws Exception {

    InputStream is = fileService.download(objectName);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + objectName + "\"")
        .body(new InputStreamResource(is));
  }

  @PutMapping("/{objectName}")
  public FileInfo update(@PathVariable String objectName,
                         @RequestParam("file") MultipartFile file) throws Exception {
    return fileService.update(objectName, file);
  }

  @DeleteMapping("/{objectName}")
  public ResponseEntity<Void> delete(@PathVariable String objectName) throws Exception {
    fileService.delete(objectName);
    return ResponseEntity.noContent().build();
  }
}