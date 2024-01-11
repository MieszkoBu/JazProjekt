package org.example.Controllers;

import org.example.Model.Studio;
import org.example.Services.StudioService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/studios")
public class StudioController {
    private final StudioService studioService;

    public StudioController(StudioService studioService) {
        this.studioService = studioService;
    }

    @GetMapping
    public ResponseEntity<List<Studio>> getAllStudios() {
        List<Studio> companies = studioService.getAllStudios();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Studio> getStudioById(@PathVariable int id) {
        return studioService.getStudioById(id)
                .map(studio -> ResponseEntity.ok(studio))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/show-studios")
    public String showStudiosList(Model model) {
        List<Studio> studioList = studioService.getAllStudios();
        model.addAttribute("studios", studioList);
        return "studios";
    }
}
