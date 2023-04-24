package com.magadiflo.asociaciones.jpa.app.unidireccional.v1.ManyToOne.repositories;

import com.magadiflo.asociaciones.jpa.app.unidireccional.v1.ManyToOne.entities.Employee;
import org.springframework.data.repository.CrudRepository;

public interface IEmployeeRepository extends CrudRepository<Employee, Long> {
}
