package org.example.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Match {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne
    private Team firstTeam;

    @ManyToOne
    private Team secondTeam;

    @ManyToOne
    private Team winner;

    @ManyToOne
    @JsonIgnore
    private Tournament tournament;

    private int round;

    public Match(Team firstTeam, Team secondTeam){
        this.firstTeam = firstTeam;
        this.secondTeam = secondTeam;
    }

    public Team requestLoser(){
        return winner.equals(firstTeam) ? secondTeam : firstTeam;
    }

}
