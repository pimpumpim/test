package org.example.backend.controller;

import org.example.backend.domain.Team;
import org.example.backend.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/team")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @GetMapping
    public ResponseEntity all() {
        List<Team> teams = teamService.getAll();

        if (!teams.isEmpty()) {
            return ResponseEntity.ok().body(teams);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found tournaments");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity byId(@PathVariable("id") Team team) {
        if (team != null) {
            return ResponseEntity.ok().body(team);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team not found");
        }
    }

    @PostMapping
    public ResponseEntity create(@RequestBody String name) {
        try {
            String id = teamService.create(name);
            return ResponseEntity.ok().body(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/changeRating/{id}")
    public ResponseEntity changeRating(@RequestBody double deltaRating, @PathVariable String id) {
        try {
            double newRating = teamService.changeRating(id, deltaRating);
            return ResponseEntity.ok().body(newRating);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team not found");
        }
    }

}
