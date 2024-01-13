package org.example.Services;
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
public class MangaService {
    private MangaRepository mangaRepository;
    @Autowired
    public MangaService(MangaRepository mangaRepository) {
        this.mangaRepository = mangaRepository;
    }

    public List<Manga> getAllMangas() {
        try {
            return new ArrayList<>(mangaRepository.findAll());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving anime list", e);
        }
    }
    @Cacheable(value = "mangaCache", key = "#id")
    public Optional<Manga> getMangaById(int id) {
        return mangaRepository.findById(id);
    }


}
