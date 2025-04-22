package ru.mirea.dms.search.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "document_meta",
       indexes = @Index(name = "idx_docmeta_fts", columnList = "fts"))
public class DocumentMeta {

    @Id
    private String id;

    private String originalName;
    private String author;
    private String category;

    private Instant createdAt;

    @Column(columnDefinition = "tsvector", insertable = false, updatable = false)
    private String fts;

    public DocumentMeta() { }


    public String getId() { return id; }
    public void   setId(String id) { this.id = id; }

    public String getOriginalName() { return originalName; }
    public void   setOriginalName(String n) { this.originalName = n; }

    public String getAuthor() { return author; }
    public void   setAuthor(String a) { this.author = a; }

    public String getCategory() { return category; }
    public void   setCategory(String c) { this.category = c; }

    public Instant getCreatedAt() { return createdAt; }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    public String getFts() { return fts; }
}