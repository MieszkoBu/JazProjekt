package org.example.Services;

import org.example.Model.Anime;
import org.example.Model.Manga;
import org.example.Repositories.AnimeRepository;
import org.example.Repositories.MangaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
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

    public Optional<Manga> getMangaById(int id) {
        return mangaRepository.findById(id);
    }

    public Manga saveManga(Manga manga) {
        return mangaRepository.save(manga);
    }

    public void deleteManga(int id) {
        mangaRepository.deleteById(id);
    }
}
