package org.example.Controllers;

import lombok.extern.slf4j.Slf4j;
import org.example.Repositories.AnimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.example.AnimeDTO;
import org.example.Model.Anime;
import org.example.Services.AnimeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class AnimeController {

    private final AnimeService animeService;
    @Autowired
    private AnimeRepository animeRepository;

    public AnimeController(AnimeService animeService) {
        this.animeService = animeService;
    }

    @GetMapping(value = "/all-animes")
    public ResponseEntity<List<Anime>> getAllAnimes() {
        return ResponseEntity.ok(animeService.getAllAnimes());
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<AnimeDTO> getAnimeById(@PathVariable int id) {
        return animeService.getAnimeById(id)
                .map(anime -> ResponseEntity.ok(animeService.convertToDTO(anime)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/all-animes-dto")
    public ResponseEntity<List<AnimeDTO>> getAllAnimesDTO() {
        return ResponseEntity.ok(animeService.getAllAnimesDTO());
    }

    @GetMapping("/anime")
    public String showAnimeList(Model model) {
        List<AnimeDTO> animeDTOList = animeService.getAllAnimesDTO();
        model.addAttribute("animes", animeDTOList);
        return "animes";
    }
}