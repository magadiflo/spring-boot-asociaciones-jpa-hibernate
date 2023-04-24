package com.magadiflo.asociaciones.jpa.app.unidireccional.v1.OneToMany.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un equipo de fútbol
 */
@Entity
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @JoinColumn(name = "team_id")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players = new ArrayList<>();

    public Team() {
    }

    public Team(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Team(Long id, String name, List<Player> players) {
        this.id = id;
        this.name = name;
        this.players = players;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    //Método auxiliar para agregar y eliminar jugador a la lista ------------
    public void addPlayer(Player player) {
        this.players.add(player);
    }
    //------------------------------------------------------------

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Team{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", players=").append(players);
        sb.append('}');
        return sb.toString();
    }
}
