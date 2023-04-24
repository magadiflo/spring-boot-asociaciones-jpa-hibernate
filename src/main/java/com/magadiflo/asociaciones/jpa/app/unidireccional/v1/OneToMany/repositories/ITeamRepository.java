package com.magadiflo.asociaciones.jpa.app.unidireccional.v1.OneToMany.repositories;

import com.magadiflo.asociaciones.jpa.app.unidireccional.v1.OneToMany.entities.Team;
import org.springframework.data.repository.CrudRepository;

public interface ITeamRepository extends CrudRepository<Team, Long> {
}
