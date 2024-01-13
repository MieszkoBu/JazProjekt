package org.example.Services;

import org.example.Model.Studio;
import org.example.Repositories.StudioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Caching
public class StudioService {
    private final StudioRepository studioRepository;
    @Autowired
    public StudioService(StudioRepository studioRepository) {
        this.studioRepository = studioRepository;
    }

    public List<Studio> getAllStudios() {
        return studioRepository.findAll();
    }
    @Cacheable(value = "studioCache", key = "#id")
    public Optional<Studio> getStudioById(int id) {
        return studioRepository.findById(id);
    }

}
