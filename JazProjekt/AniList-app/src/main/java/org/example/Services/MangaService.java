package org.example.Services;
import lombok.extern.slf4j.Slf4j;
import org.example.Model.Anime;
import org.example.Model.Manga;
import org.example.Repositories.MangaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@Caching
@Slf4j
public class MangaService {
    private MangaRepository mangaRepository;
    @Autowired
    public MangaService(MangaRepository mangaRepository) {
        this.mangaRepository = mangaRepository;
    }

    public List<Manga> getAllMangas() {
        try {
            List<Manga> mangas = new ArrayList<>(mangaRepository.findAll());
            log.info("Retrieved {} mangas", mangas.size());
            return mangas;
        } catch (Exception e) {
            log.error("Error retrieving manga list", e);
            throw new RuntimeException("Error retrieving anime list", e);
        }
    }
    @Cacheable(value = "mangaCache", key = "#id")
    public Optional<Manga> getMangaById(int id) {
        log.info("Fetching manga with ID: {}", id);
        return mangaRepository.findById(id);
    }


}
