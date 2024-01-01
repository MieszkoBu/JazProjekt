package org.example.Controllers;

import org.example.Model.Anime;
import org.example.Model.Manga;
import org.example.Services.AnimeService;
import org.example.Services.MangaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/manga")
public class MangaController {
    private final MangaService mangaService;

    public MangaController(MangaService mangaService) {
        this.mangaService = mangaService;
    }

    @GetMapping
    public ResponseEntity<List<Manga>> getAllMangas() {
        List<Manga> allMangas = mangaService.getAllMangas();
        return ResponseEntity.ok(allMangas);

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Manga> getMangaById(@PathVariable int id) {
        Optional<Manga> manga = mangaService.getMangaById(id);
        return manga.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Manga> createManga(@RequestBody Manga manga) {
        Manga createdAnime = mangaService.saveManga(manga);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAnime);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteManga(@PathVariable int id) {
        mangaService.deleteManga(id);
        return ResponseEntity.noContent().build();
    }
}
