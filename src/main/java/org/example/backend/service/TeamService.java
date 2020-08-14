package org.example.backend.service;

import org.example.backend.domain.Team;
import org.example.backend.repo.TeamRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class TeamService {

    @Autowired
    private TeamRepo teamRepo;

    public String create(String name) {
        Team team = new Team();
        team.setName(name);

        //initial start rating
        team.setRating(50.0);
        teamRepo.save(team);
        return team.getId().toString();
    }

    public List<Team> getAll() {
        return teamRepo.findAll();
    }




    public double changeRating(String id, double deltaRating) {
        Team team = teamRepo.findById(UUID.fromString(id)).get();
        double newRating = team.getRating() + deltaRating;
        newRating = Math.round(newRating * 100.0) / 100.0;
        team.setRating(newRating);

        teamRepo.save(team);
        return team.getRating();
    }

    public Team find(String teamId) {
        return teamRepo.findById(UUID.fromString(teamId)).get();
    }
}
