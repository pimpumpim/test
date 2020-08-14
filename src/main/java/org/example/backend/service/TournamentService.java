package org.example.backend.service;

import org.example.backend.domain.Match;
import org.example.backend.domain.StatusTournament;
import org.example.backend.domain.Team;
import org.example.backend.domain.Tournament;
import org.example.backend.repo.TournamentRepo;
import org.example.backend.service.exception.NotAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class TournamentService {

    @Autowired
    private TournamentRepo tournamentRepo;

    @Autowired
    private TeamService teamService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private EntityManager em;


    public List<Tournament> getAllList() {
        return tournamentRepo.findAll();
    }

    public Tournament findById(String id) {
        return tournamentRepo.findById(UUID.fromString(id)).get();
    }

    public String create(String name) {

        Tournament tournament = new Tournament();
        tournament.setName(name);
        tournament.setStatus(StatusTournament.COMMAND_SET);
        tournamentRepo.save(tournament);
        return tournament.getId().toString();
    }

    //WARNING!!! return permission to start
    public boolean addParticipant(String tournamentId, String teamId) throws NotAvailableException {

        Tournament tournament = tournamentRepo.
                findById(UUID.fromString(tournamentId)).get();
        if (tournament.getStatus().equals(StatusTournament.COMMAND_SET)) {
            Team team = teamService.find(teamId);
            tournament.addTeam(team);
            tournamentRepo.save(tournament);
            return tournament.conceptToStart();
        } else {
            throw new NotAvailableException("Deleted not available in " + tournament.getStatus() + "mode");
        }


    }

    public boolean removeParticipant(String tournamentId, String teamId) throws NotAvailableException {
        Tournament tournament = tournamentRepo.
                findById(UUID.fromString(tournamentId)).get();
        if (tournament.getStatus().equals(StatusTournament.COMMAND_SET)) {
            Team team = teamService.find(teamId);
            tournament.removeTeam(team);

            tournamentRepo.save(tournament);
            return tournament.conceptToStart();
        } else {
            throw new NotAvailableException("Deleted not available in " + tournament.getStatus() + "mode");
        }
    }

    public Tournament startTournament(String tournamentId) throws NotAvailableException {
        Tournament tournament = tournamentRepo.
                findById(UUID.fromString(tournamentId)).get();
        if (tournament.conceptToStart()) {

            tournament.setStatus(StatusTournament.IN_PRECESS);
            tournamentRepo.save(tournament);

            List<Team> teams = new LinkedList<>();
            teams.addAll(tournament.getTeams());

            if (teams.size() % 2 != 0) {
                Team teamWithHighestRating = teams.stream().max(Comparator.comparing(Team::getRating))
                        .orElse(teams.get((int) (Math.random() * teams.size())));
                Match match = matchService.create(teamWithHighestRating, null, tournament, 1);
                try {
                    matchService.setWinnerInMatch(match, teamWithHighestRating);
                } catch (MatchService.NotFound notFound) {
                    notFound.printStackTrace();
                }
                teams.remove(teamWithHighestRating);
            }

            createRound(teams, tournament, 1);

            return tournament;
        } else {
            throw new NotAvailableException("Starting with " + tournament.getTeams().size() + " players is not possible");
        }
    }

    public void nextRound(Tournament tournament, int currentRound) {
        List<Match> matches = tournament.getMatches().stream().
                filter(match -> match.getRound() == currentRound).
                collect(Collectors.toList());

        boolean allGamesPlayed = matches.
                stream().
                allMatch(match -> match.getWinner() != null);

        boolean existNextRound = tournament.getMatches().
                stream().
                anyMatch(match -> match.getRound() == currentRound + 1);

        if (matches.size() != 1 && allGamesPlayed && !existNextRound) {

            List<Team> teams = matches.stream().map(Match::getWinner).
                    collect(Collectors.toList());

            createRound(teams, tournament, currentRound + 1);

            if (matches.size() <= 2) {
                List<Team> teamsToPlayForThirdPlace = matches.stream().
                        map(Match::requestLoser).collect(Collectors.toList());
                createRound(teamsToPlayForThirdPlace, tournament, 0);
            }
        } else if (matches.size() == 1) {
            tournament.setStatus(StatusTournament.END);
            tournamentRepo.save(tournament);
        }

    }

    private void createRound(List<Team> teams, Tournament tournament, int round) {
        while (teams.size() > 0) {
            System.out.println((int) Math.random() * teams.size());
            Team firstTeam = teams.get((int) (Math.random() * teams.size()));
            teams.remove(firstTeam);

            Team secondTeam = teams.get((int) (Math.random() * teams.size()));
            teams.remove(secondTeam);

            matchService.create(firstTeam, secondTeam, tournament, round);
        }
        em.refresh(tournament);
    }


}
