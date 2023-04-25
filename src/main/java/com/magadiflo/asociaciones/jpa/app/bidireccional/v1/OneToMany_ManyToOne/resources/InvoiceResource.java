package com.magadiflo.asociaciones.jpa.app.bidireccional.v1.OneToMany_ManyToOne.resources;

import com.magadiflo.asociaciones.jpa.app.bidireccional.v1.OneToMany_ManyToOne.entities.Invoice;
import com.magadiflo.asociaciones.jpa.app.bidireccional.v1.OneToMany_ManyToOne.repositories.IInvoiceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/bidireccional/v1/one-to-many_many-to-one/invoices")
public class InvoiceResource {

    private final IInvoiceRepository invoiceRepository;

    public InvoiceResource(IInvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        return ResponseEntity.ok((List<Invoice>) this.invoiceRepository.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable Long id) {
        return ResponseEntity.ok(this.invoiceRepository.findById(id).orElseThrow());
    }


}
