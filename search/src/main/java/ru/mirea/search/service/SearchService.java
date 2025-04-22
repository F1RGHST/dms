// service/SearchService.java
package ru.mirea.dms.search.service;

import org.springframework.stereotype.Service;
import ru.mirea.dms.search.model.DocumentMeta;
import ru.mirea.dms.search.repo.DocumentMetaRepository;

import java.util.List;

@Service
public class SearchService {
    private final DocumentMetaRepository repo;
    public SearchService(DocumentMetaRepository repo) { this.repo = repo; }

    public DocumentMeta save(DocumentMeta m) { return repo.save(m); }

    public List<DocumentMeta> search(String q) { return repo.search(q); }
}