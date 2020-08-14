package org.example.backend.controller;

import org.example.backend.domain.Tournament;
import org.example.backend.service.MatchService;
import org.example.backend.service.exception.NotAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/match")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @GetMapping
    public ResponseEntity getAllMatches() {
        return ResponseEntity.ok().body(matchService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity getAllMatches(@PathVariable String id) {
        return ResponseEntity.ok().build();//.body(matchService.getAll());
    }

    @PutMapping("/putWinner/{idMatch}/{idWinner}")
    public ResponseEntity setWinner(@PathVariable("idMatch") String idMatch,
                                    @PathVariable("idWinner") String idWinner) {
        try {
            Tournament tournament = matchService.setWinnerInMatch(idMatch, idWinner);
            return ResponseEntity.ok().body(tournament);
        } catch (NoSuchElementException | MatchService.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (NotAvailableException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body(e.getMessage());
        }
    }
}
