// controller/SearchController.java
package ru.mirea.dms.search.controller;

import org.springframework.web.bind.annotation.*;
import ru.mirea.dms.search.model.DocumentMeta;
import ru.mirea.dms.search.service.SearchService;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService svc;
    public SearchController(SearchService svc) { this.svc = svc; }

    /* POST /api/search/metadata */
    @PostMapping("/metadata")
    public DocumentMeta upsert(@RequestBody DocumentMeta meta) {
        return svc.save(meta);
    }

    /* GET /api/search?q=... */
    @GetMapping
    public List<DocumentMeta> query(@RequestParam("q") String q) {
        return svc.search(q);
    }
}