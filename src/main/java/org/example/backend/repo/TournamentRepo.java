package org.example.backend.repo;

import org.example.backend.domain.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TournamentRepo extends JpaRepository<Tournament, UUID> {

}
