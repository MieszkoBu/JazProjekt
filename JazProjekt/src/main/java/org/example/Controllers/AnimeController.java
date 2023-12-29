package org.example.Controllers;

import org.example.Model.Anime;
import org.example.Services.AnimeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class AnimeController {
    private final AnimeService animeService;

    public AnimeController(AnimeService animeService) {
        this.animeService = animeService;
    }

    @GetMapping("/api/all-animes")
    public Iterable<Anime> getAllAnimes() {
        return animeService.getAllAnimes();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Anime> getAnimeById(@PathVariable int id) {
        Optional<Anime> anime = animeService.getAnimeById(id);
        return anime.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Anime> createAnime(@RequestBody Anime anime) {
        Anime createdAnime = animeService.saveAnime(anime);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAnime);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnime(@PathVariable int id) {
        animeService.deleteAnime(id);
        return ResponseEntity.noContent().build();
    }
}
