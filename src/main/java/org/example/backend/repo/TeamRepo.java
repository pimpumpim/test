package org.example.backend.repo;

import org.example.backend.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeamRepo extends JpaRepository<Team, UUID> {


}
