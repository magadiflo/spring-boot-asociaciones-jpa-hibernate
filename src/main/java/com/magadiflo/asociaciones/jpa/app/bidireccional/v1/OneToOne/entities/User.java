package com.magadiflo.asociaciones.jpa.app.bidireccional.v1.OneToOne.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * Clase principal o entidad padre
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String role;
    @JsonIgnoreProperties(value = {"user"})
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private Credential credential;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    // Asociando ambos lados de la relación --------------------------
    public void addCredential(Credential credential) {
        this.credential = credential;
        credential.setUser(this);
    }

    public void deleteCredential() {
        this.credential.setUser(null);
        this.credential = null;
    }
    //----------------------------------------------------------------

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(id);
        sb.append(", nombre='").append(nombre).append('\'');
        sb.append(", role='").append(role).append('\'');
        sb.append(", credential=").append(credential);
        sb.append('}');
        return sb.toString();
    }
}
