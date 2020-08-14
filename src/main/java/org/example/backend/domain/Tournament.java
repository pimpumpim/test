package org.example.backend.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Comparator;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Tournament {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    private UUID id;
    private String name;
    private StatusTournament status;

    @OneToMany(mappedBy = "tournament")
    private Set<Match> matches;

    @ManyToMany
    private Set<Team> teams;


    public void setStatus(StatusTournament newStatus) {
        switch (newStatus) {
            case COMMAND_SET:
                if (status == null) {
                    status = newStatus;
                }
                break;
            case IN_PRECESS:
                if (conceptToStart()) {
                    status = newStatus;
                }
                break;
            case END:
                Match playForThirdPlace = matches.stream().
                        filter(match -> match.getRound() == 0).findFirst().get();
                int lastRound = matches.stream().max(Comparator.
                        comparingInt(Match::getRound))
                        .get().getRound();
                Match finalMatch = matches.stream().
                        filter(match -> match.getRound() == lastRound).findFirst().get();
                if (finalMatch.getWinner() != null && playForThirdPlace.getWinner() != null) {
                    status = newStatus;
                }
        }
    }

    public boolean conceptToStart() {
        int quantity = teams.size();
        if (quantity >= 7) {
            int powOfTwo = (int) Math.ceil(Math.log10(quantity) / Math.log10(2));
            int pow = (int) Math.pow(2, powOfTwo);
            if (quantity == pow || quantity == pow - 1) {
                return true;
            }
        }
        return false;
    }

    public void addTeam(Team team) {
        teams.add(team);
//        return conceptToStart();
    }

    public void removeTeam(Team team) {
        teams.removeIf(element -> element.equals(team));
    }
}
