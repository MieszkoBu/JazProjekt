package org.example.Controllers;

import org.example.Model.Anime;
import org.example.Model.Manga;
import org.example.Services.MangaService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
        return mangaService.getMangaById(id)
                .map(manga -> ResponseEntity.ok(manga))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/mangas")
    public String listMangas(Model model) {
        List<Manga> mangas = mangaService.getAllMangas();
        model.addAttribute("mangas", mangas);
        return "mangas";
    }
}
