package org.example.backend.controller;

import org.example.backend.domain.Tournament;
import org.example.backend.service.TournamentService;
import org.example.backend.service.exception.NotAvailableException;
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
@RequestMapping("/tournaments")
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;


    @GetMapping
    public ResponseEntity getAllList() {
        List<Tournament> tournamentList = tournamentService.getAllList();
        if (!tournamentList.isEmpty()) {
            return ResponseEntity.ok().body(tournamentList);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found tournaments");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable Tournament tournament) {
        if (tournament != null) {
            return ResponseEntity.ok().body(tournament);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tournament with this id not found");
        }
    }

    @PostMapping
    public ResponseEntity createTournament(@RequestBody String name) {

        try {
            String id = tournamentService.create(name);
            return ResponseEntity.ok().body(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    //WARNING!!! return permission to start
    @PutMapping("/addParticipant/{tournament}/{team}")
    public ResponseEntity addParticipantInToTournament(@PathVariable("tournament") String tournament,
                                                       @PathVariable("team") String team) {

        try {
            boolean permission = tournamentService.addParticipant(tournament, team);

            return ResponseEntity.ok().body(permission);
        } catch (
                NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (NotAvailableException notAvailableException) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(notAvailableException.getMessage());
        }
    }

    @PutMapping("/removeParticipant/{tournament}/{team}")
    public ResponseEntity removeParticipantFromTournament(@PathVariable("tournament") String tournament,
                                                          @PathVariable("team") String team) {
        try {
            boolean permission = tournamentService.removeParticipant(tournament, team);
            return ResponseEntity.ok().body(permission);
        } catch (
                NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (NotAvailableException notAvailableException) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body(notAvailableException.getMessage());
        }
    }

    @PutMapping("/startTournament/{id}")
    public ResponseEntity startTournament(@PathVariable("id") String tournamentId) {
        Tournament tournament = null;
        try {
            tournament = tournamentService.startTournament(tournamentId);
            return ResponseEntity.ok().body(tournament);
        } catch (NotAvailableException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body(e.getMessage());
        }
    }

}
