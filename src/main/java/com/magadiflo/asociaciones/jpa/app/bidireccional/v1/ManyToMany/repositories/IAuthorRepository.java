package com.magadiflo.asociaciones.jpa.app.bidireccional.v1.ManyToMany.repositories;

import com.magadiflo.asociaciones.jpa.app.bidireccional.v1.ManyToMany.entities.Author;
import org.springframework.data.repository.CrudRepository;

public interface IAuthorRepository extends CrudRepository<Author, Long> {
}
