package com.magadiflo.asociaciones.jpa.app.unidireccional.v1.OneToMany.resources;

import com.magadiflo.asociaciones.jpa.app.unidireccional.v1.OneToMany.entities.Player;
import com.magadiflo.asociaciones.jpa.app.unidireccional.v1.OneToMany.entities.Team;
import com.magadiflo.asociaciones.jpa.app.unidireccional.v1.OneToMany.repositories.ITeamRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/unidireccional/v1/one-to-many/teams")
public class TeamResource {

    private final ITeamRepository teamRepository;

    public TeamResource(ITeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams() {
        return ResponseEntity.ok((List<Team>) this.teamRepository.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Team> getTeam(@PathVariable Long id) {
        return ResponseEntity.ok(this.teamRepository.findById(id).orElseThrow());
    }

    @PostMapping
    public ResponseEntity<Team> saveTeam(@RequestBody Team team) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.teamRepository.save(team));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable Long id, @RequestBody Team team) {
        return this.teamRepository.findById(id)
                .map(teamBD -> {
                    teamBD.setName(team.getName());
                    if (!team.getPlayers().isEmpty()) {
                        List<Player> playerToCreate = team.getPlayers().stream().filter(player -> player.getId() == null).toList();
                        List<Player> playerToUpdate = team.getPlayers().stream().filter(player -> player.getId() != null).toList();

                        // CASO 01: Eliminando players de la BD para este Team, porque no se mandó en requestBody
                        teamBD.getPlayers().removeIf(playerBD -> {
                            boolean noExiste = true;
                            for (Player player : playerToUpdate) {
                                if (playerBD.getId().equals(player.getId())) {
                                    noExiste = false;
                                    break;
                                }
                            }
                            return noExiste;
                        });


                        // CASO 02: Actualiza los players que ya están en la BD para este Team
                        teamBD.getPlayers().forEach(playerBD -> {
                            for (Player player : playerToUpdate) {
                                if (Objects.equals(playerBD.getId(), player.getId())) {
                                    playerBD.setName(player.getName());
                                    playerBD.setNumber(player.getNumber());
                                    break;
                                }
                            }
                        });

                        // CASO 03: Agrega nuevos players al Team
                        playerToCreate.forEach(teamBD::addPlayer);
                    }
                    return ResponseEntity.ok().body(this.teamRepository.save(teamBD));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteTeam(@PathVariable Long id) {
        return this.teamRepository.findById(id)
                .map(team -> {
                    this.teamRepository.deleteById(id);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
