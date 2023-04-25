package com.magadiflo.asociaciones.jpa.app.bidireccional.v1.OneToMany_ManyToOne.repositories;

import com.magadiflo.asociaciones.jpa.app.bidireccional.v1.OneToMany_ManyToOne.entities.Customer;
import org.springframework.data.repository.CrudRepository;

public interface ICustomerRepository extends CrudRepository<Customer, Long> {
}
