package com.magadiflo.asociaciones.jpa.app.bidireccional.v1.OneToOne.repositories;

import com.magadiflo.asociaciones.jpa.app.bidireccional.v1.OneToOne.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface IUserRepository extends CrudRepository<User, Long> {
}
