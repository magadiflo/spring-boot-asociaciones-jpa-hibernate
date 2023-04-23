package com.magadiflo.asociaciones.jpa.app.unidireccional.v1.OneToOne.repositories;

import com.magadiflo.asociaciones.jpa.app.unidireccional.v1.OneToOne.entities.Port;
import org.springframework.data.repository.CrudRepository;

public interface IPortRepository extends CrudRepository<Port, Long> {

}
