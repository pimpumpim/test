package org.example.backend.repo;

import org.example.backend.domain.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MatchRepo extends JpaRepository<Match, UUID> {


}
