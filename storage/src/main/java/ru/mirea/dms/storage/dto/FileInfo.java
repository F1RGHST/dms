package ru.mirea.dms.storage.dto

import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.Instant;
import java.time.OffsetDateTime;

public class FileInfo {
    private String objectName;
    private long size;
    private String contentType;
    private Instant lastModified;

    public String getObjectName() { return objectName; }
    public void setObjectName(String objectName) { this.objectName = objectName; }
    public long getSize() { return size; }
    public void setSize(long size) { this.size = size; }
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public Instant getLastModified() { return lastModified; }
    public void setLastModified(Instant lastModified) { this.lastModified = lastModified; }
}