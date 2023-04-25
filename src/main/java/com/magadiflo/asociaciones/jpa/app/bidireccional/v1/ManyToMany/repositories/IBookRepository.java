package com.magadiflo.asociaciones.jpa.app.bidireccional.v1.ManyToMany.repositories;

import com.magadiflo.asociaciones.jpa.app.bidireccional.v1.ManyToMany.entities.Book;
import org.springframework.data.repository.CrudRepository;

public interface IBookRepository extends CrudRepository<Book, Long> {
}
