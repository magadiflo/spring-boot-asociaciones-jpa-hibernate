package com.magadiflo.asociaciones.jpa.app.unidireccional.v1.ManyToMany.repositories;

import com.magadiflo.asociaciones.jpa.app.unidireccional.v1.ManyToMany.entities.Student;
import org.springframework.data.repository.CrudRepository;

public interface IStudentRepository extends CrudRepository<Student, Long> {
}
