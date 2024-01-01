package org.example.Controllers;

import org.example.Model.Studio;
import org.example.Services.StudioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
    public ResponseEntity<List<Studio>> getAllCompanies() {
        List<Studio> companies = studioService.getAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Studio> getCompanyById(@PathVariable int id) {
        Optional<Studio> company = studioService.getCompanyById(id);
        return company.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Studio> createCompany(@RequestBody Studio studio) {
        Studio createdStudio = studioService.SaveCompany(studio);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudio);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable int id) {
        studioService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
