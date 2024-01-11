package org.example.Repositories;

import org.example.Model.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
@Repository
public interface AnimeRepository extends JpaRepository<Anime, Integer>, JpaSpecificationExecutor<Anime> {
}
