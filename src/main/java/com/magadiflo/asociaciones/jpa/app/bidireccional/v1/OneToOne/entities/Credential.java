package com.magadiflo.asociaciones.jpa.app.bidireccional.v1.OneToOne.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * Clase dueña de la relación
 */
@Entity
@Table(name = "credentials")
public class Credential {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @JsonIgnoreProperties(value = {"credential"})
    @JoinColumn(name = "user_id", unique = true)
    @OneToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //Estableciendo relación en ambos sentidos con User--------
    public void addUser(User user) {
        this.user = user;
        user.setCredential(this);
    }

    public void deleteUser() {
        this.user.setCredential(null);
        this.user = null;
    }
    //--------

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Credential{");
        sb.append("id=").append(id);
        sb.append(", username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
