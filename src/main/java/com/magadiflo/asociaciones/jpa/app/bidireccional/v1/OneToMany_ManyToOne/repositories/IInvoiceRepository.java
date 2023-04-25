package com.magadiflo.asociaciones.jpa.app.bidireccional.v1.OneToMany_ManyToOne.repositories;

import com.magadiflo.asociaciones.jpa.app.bidireccional.v1.OneToMany_ManyToOne.entities.Invoice;
import org.springframework.data.repository.CrudRepository;

public interface IInvoiceRepository extends CrudRepository<Invoice, Long> {
}
