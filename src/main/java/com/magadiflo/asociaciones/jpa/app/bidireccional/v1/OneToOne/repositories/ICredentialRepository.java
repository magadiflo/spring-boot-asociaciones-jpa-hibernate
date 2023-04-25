package com.magadiflo.asociaciones.jpa.app.bidireccional.v1.OneToOne.repositories;

import com.magadiflo.asociaciones.jpa.app.bidireccional.v1.OneToOne.entities.Credential;
import org.springframework.data.repository.CrudRepository;

public interface ICredentialRepository extends CrudRepository<Credential, Long> {
}
