package com.magadiflo.asociaciones.jpa.app.bidireccional.v1.OneToMany_ManyToOne.resources;

import com.magadiflo.asociaciones.jpa.app.bidireccional.v1.OneToMany_ManyToOne.entities.Customer;
import com.magadiflo.asociaciones.jpa.app.bidireccional.v1.OneToMany_ManyToOne.entities.Invoice;
import com.magadiflo.asociaciones.jpa.app.bidireccional.v1.OneToMany_ManyToOne.repositories.ICustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/bidireccional/v1/one-to-many_many-to-one/customers")
public class CustomerResource {
    private final ICustomerRepository customerRepository;

    public CustomerResource(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok((List<Customer>) this.customerRepository.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(this.customerRepository.findById(id).orElseThrow());
    }

    @PostMapping
    public ResponseEntity<Customer> saveCustomer(@RequestBody Customer customer) {
        // Estableciendo asociación en ambos sentidos entre customer y sus invoices
        /*
         * Si lo dejamos así: List<Invoice> invoices = customer.getInvoices();
         * cuando le hagamos el .clear(), también eliminará los elementos de invoices, ya que en teoría
         * son la misma referencia. Por esa razón es que necesitamos generar una nueva lista con los
         * elementos, pero que apunten a otra referencia y eso nos los da el stream()
         * */
        List<Invoice> invoices = customer.getInvoices().stream().toList();
        customer.getInvoices().clear();
        invoices.forEach(customer::addInvoice);

        return ResponseEntity.status(HttpStatus.CREATED).body(this.customerRepository.save(customer));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        return this.customerRepository.findById(id)
                .map(customerDB -> {
                    customerDB.setName(customer.getName());
                    customerDB.setDni(customer.getDni());

                    if (!customer.getInvoices().isEmpty()) {
                        List<Invoice> invoicesToCreate = customer.getInvoices().stream().filter(invoice -> invoice.getId() == null).toList();
                        List<Invoice> invoicesToUpdate = customer.getInvoices().stream().filter(invoice -> invoice.getId() != null).toList();

                        // CASO 01: Eliminando invoices de la BD para este Customer, porque no se mandó en requestBody
                        List<Invoice> noVienenEnRequestBody = new ArrayList<>();
                        customerDB.getInvoices().forEach(invoiceDB -> {
                            boolean noExiste = true;
                            for (Invoice invoice : invoicesToUpdate) {
                                if (invoiceDB.getId().equals(invoice.getId())) {
                                    noExiste = false;
                                    break;
                                }
                            }
                            if (noExiste) {
                                noVienenEnRequestBody.add(invoiceDB);
                            }
                        });
                        noVienenEnRequestBody.forEach(customerDB::deleteInvoice);


                        // CASO 02: Actualiza los invoices que ya están en la BD para este Customer
                        customerDB.getInvoices().forEach(invoiceDB -> {
                            for (Invoice invoice : invoicesToUpdate) {
                                if (Objects.equals(invoiceDB.getId(), invoice.getId())) {
                                    invoiceDB.setNumber(invoice.getNumber());
                                    invoiceDB.setTotal(invoice.getTotal());
                                    invoiceDB.setCreateAt(invoice.getCreateAt());
                                    break;
                                }
                            }
                        });

                        // CASO 03: Agrega nuevos invoices al Customer
                        invoicesToCreate.forEach(customerDB::addInvoice);
                    }
                    return ResponseEntity.ok().body(this.customerRepository.save(customerDB));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        return this.customerRepository.findById(id)
                .map(customerDB -> {
                    this.customerRepository.deleteById(id);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


}
