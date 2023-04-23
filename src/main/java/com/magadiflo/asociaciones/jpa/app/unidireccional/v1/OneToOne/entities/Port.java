package com.magadiflo.asociaciones.jpa.app.unidireccional.v1.OneToOne.entities;

import javax.persistence.*;

/**
 * Clase que representa un puerto tcp/udp
 */
@Entity
@Table(name = "ports")
public class Port {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer number;
    private String type;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "service_id", unique = true)
    private Service service;

    public Port() {
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Port{");
        sb.append("id=").append(id);
        sb.append(", number=").append(number);
        sb.append(", type='").append(type).append('\'');
        sb.append(", service=").append(service);
        sb.append('}');
        return sb.toString();
    }
}
