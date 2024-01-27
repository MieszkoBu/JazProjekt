package org.example.Services;

import lombok.extern.slf4j.Slf4j;
import org.example.Model.Anime;
import org.example.Model.Studio;
import org.example.Repositories.StudioRepository;
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
public class StudioService {
    private final StudioRepository studioRepository;
    @Autowired
    public StudioService(StudioRepository studioRepository) {
        this.studioRepository = studioRepository;
    }

    public List<Studio> getAllStudios() {
        try {
            List<Studio> studios = new ArrayList<>(studioRepository.findAll());
            log.info("Retrieved {} studios", studios.size());
            return studios;
        }catch (Exception e){
            log.error("Error retrieving studio list", e);
            throw new RuntimeException("Error retrieving studio list", e);
        }
    }
    @Cacheable(value = "studioCache", key = "#id")
    public Optional<Studio> getStudioById(int id) {
        log.info("Fetching studio with ID: {}", id);
        return studioRepository.findById(id);
    }

}
