package org.example.backend.service;

import org.example.backend.domain.Match;
import org.example.backend.domain.StatusTournament;
import org.example.backend.domain.Team;
import org.example.backend.domain.Tournament;
import org.example.backend.repo.MatchRepo;
import org.example.backend.service.exception.NotAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class MatchService {

    @Autowired
    private MatchRepo matchRepo;

    @Autowired
    private TeamService teamService;

    @Autowired
    private TournamentService tournamentService;

    public List<Match> getAll() {
        return matchRepo.findAll();
    }

    public Match create(Team firstTeam, Team secondTeam, Tournament tournament, int round) {
        Match match = new Match(firstTeam, secondTeam);
        match.setTournament(tournament);
        match.setRound(round);
        matchRepo.save(match);
        return match;
    }

    public Tournament setWinnerInMatch(String idMatch, String idTeam) throws NotFound, NotAvailableException {
        Match match = matchRepo.findById(UUID.fromString(idMatch)).get();
        Team team = teamService.find(idTeam);
        setWinnerInMatch(match, team);
        tournamentService.nextRound(match.getTournament(),
                match.getRound());
        return tournamentService.findById(match.getTournament().getId().toString());
    }

    public void setWinnerInMatch(Match match, Team team) throws NotFound, NotAvailableException {

        if (match.getWinner() == null && (match.getFirstTeam().equals(team) || match.getSecondTeam().equals(team))
                && match.getTournament().getStatus().equals(StatusTournament.IN_PRECESS)) {

            match.setWinner(team);
            matchRepo.save(match);

        } else if (match.getTournament().getStatus().equals(StatusTournament.IN_PRECESS)) {
            throw new NotAvailableException("Impossible put winner in " +
                    match.getTournament().getStatus().toString() + "mode");
        } else if (!match.getFirstTeam().equals(team) || !match.getSecondTeam().equals(team)) {
            throw new NotFound("Teams did't participate in this match");
        }
    }

    public class NotFound extends Exception {
        public NotFound(String message) {
            super(message);
        }
    }

}
