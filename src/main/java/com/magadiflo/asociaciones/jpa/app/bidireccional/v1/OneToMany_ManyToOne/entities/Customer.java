package com.magadiflo.asociaciones.jpa.app.bidireccional.v1.OneToMany_ManyToOne.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal o padre
 */
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String dni;
    @JsonIgnoreProperties(value = {"customer"})
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "customer")
    private List<Invoice> invoices = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    // Estableciendo asociación en ambos sentidos ----

    /**
     * (1) Para que guarde la FK de customer en la tabla Invoices (customer_id)
     */
    public void addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
        invoice.setCustomer(this); // (1)
    }

    /**
     * (2) Al invoice le quitamos la FK de customer estableciéndolo en null, quedando huérfana,
     * de esa manera como tenemos el orphanRemoval=true, se eliminará
     */
    public void deleteInvoice(Invoice invoice) {
        this.invoices.remove(invoice);
        invoice.setCustomer(null); // (2)
    }
    // -----------------------------------------------

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Customer{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", dni='").append(dni).append('\'');
        sb.append(", invoices=").append(invoices);
        sb.append('}');
        return sb.toString();
    }
}
