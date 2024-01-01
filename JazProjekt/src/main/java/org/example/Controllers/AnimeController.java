package org.example.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.example.AnimeDTO;
import org.example.Model.Anime;
import org.example.Services.AnimeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/anime")
public class AnimeController {

    private final AnimeService animeService;

    public AnimeController(AnimeService animeService) {
        this.animeService = animeService;
    }

    @GetMapping(value = "/all-animes")
    public ResponseEntity<List<Anime>> getAllAnimes() {
        List<Anime> allAnimes = animeService.getAllAnimes();
        return ResponseEntity.ok(allAnimes);
    }

    @GetMapping(value = "/{id}")
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

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteAnime(@PathVariable int id) {
        animeService.deleteAnime(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/all-animes-dto")
    public ResponseEntity<List<AnimeDTO>> getAllAnimesDTO() {
        return ResponseEntity.ok(animeService.getAllAnimesDTO());
    }
    @GetMapping("/show-anime")
    public String showAnimeList(Model model) {
        List<AnimeDTO> animeDTOList = animeService.getAllAnimesDTO();
        model.addAttribute("animes", animeDTOList);
        return "index";
    }
}