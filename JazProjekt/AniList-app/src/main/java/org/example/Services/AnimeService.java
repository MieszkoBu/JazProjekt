package org.example.Services;
import lombok.extern.slf4j.Slf4j;
import org.example.AnimeDTO;
import org.example.Model.Anime;
import org.example.Model.Studio;
import org.example.Repositories.AnimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Caching
@Slf4j
public class AnimeService {
    private AnimeRepository animeRepository;
    @Autowired
    public AnimeService(AnimeRepository animeRepository) {
        this.animeRepository = animeRepository;
    }
    public List<Anime> getAllAnimes() {
        try {
            List<Anime> animes = new ArrayList<>(animeRepository.findAll());
            log.info("Retrieved {} animes", animes.size());
            return animes;
        } catch (Exception e) {
            log.error("Error retrieving anime list", e);
            throw new RuntimeException("Error retrieving anime list", e);
        }
    }
    @Cacheable(value = "animeCache", key = "#id")
    public Optional<Anime> getAnimeById(int id) {
        log.info("Fetching anime with ID: {}", id);
        return animeRepository.findById(id);
    }

    public List<AnimeDTO> getAllAnimesDTO() {
        List<Anime> animes = getAllAnimes();
        return animes.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public AnimeDTO convertToDTO(Anime anime) {
        AnimeDTO dto = new AnimeDTO();
        dto.setId(anime.getId());
        dto.setFormat(anime.getFormat());
        dto.setEpisodes(anime.getEpisodes());
        dto.setEpisodes_duration(anime.getEpisodes_duration());
        dto.setStatus(anime.getStatus());
        dto.setStart_date(anime.getStart_date());
        dto.setEnd_date(anime.getEnd_date());
        dto.setAverage_score(anime.getAverage_score());
        dto.setPopularity(anime.getPopularity());
        dto.setFavourites(anime.getFavourites());
        String studios = anime.getStudios().stream()
                .map(Studio::getName)
                .collect(Collectors.joining(", "));
        dto.setStudios(studios);
        dto.setSource(anime.getSource());
        String genres = String.join(", ", anime.getGenres());
        dto.setGenres(genres);
        dto.setRomaji(anime.getRomaji());
        dto.setEnglish(anime.getEnglish());
        dto.setOriginal(anime.getOriginal());

        return dto;
    }
}