package org.example.Repositories;

import org.example.Model.Studio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StudioRepository extends JpaRepository<Studio,Integer>, JpaSpecificationExecutor<Studio> {
}
