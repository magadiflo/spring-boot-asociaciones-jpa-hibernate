package com.magadiflo.asociaciones.jpa.app.bidireccional.v1.OneToMany_ManyToOne.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Clase dueña de la relación
 */
@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer number;
    private Double total;
    private LocalDate createAt;
    @JsonIgnoreProperties(value = {"invoices"})
    @JoinColumn(name = "customer_id")
    @ManyToOne
    private Customer customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Invoice{");
        sb.append("id=").append(id);
        sb.append(", number=").append(number);
        sb.append(", total=").append(total);
        sb.append(", createAt=").append(createAt);
        sb.append('}');
        return sb.toString();
    }
}
