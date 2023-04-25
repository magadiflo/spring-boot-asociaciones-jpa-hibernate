package com.magadiflo.asociaciones.jpa.app.bidireccional.v1.OneToOne.resources;

import com.magadiflo.asociaciones.jpa.app.bidireccional.v1.OneToOne.entities.Credential;
import com.magadiflo.asociaciones.jpa.app.bidireccional.v1.OneToOne.entities.User;
import com.magadiflo.asociaciones.jpa.app.bidireccional.v1.OneToOne.repositories.IUserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/bidireccional/v1/one-to-one/users")
public class UserResource {
    private final IUserRepository userRepository;

    public UserResource(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok((List<User>) this.userRepository.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(this.userRepository.findById(id).orElseThrow());
    }

    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        // Antes de registrar un User establecemos la relación en ambos sentidos con Credential
        if (user.getCredential() != null) {
            Credential credential = user.getCredential();
            user.addCredential(credential);
        }
        return ResponseEntity.ok(this.userRepository.save(user));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return this.userRepository.findById(id)
                .map(userBD -> {
                    userBD.setNombre(user.getNombre());
                    userBD.setRole(user.getRole());

                    if (user.getCredential() != null) {
                        if (userBD.getCredential() != null) {
                            userBD.getCredential().setUsername(user.getCredential().getUsername());
                            userBD.getCredential().setPassword(user.getCredential().getPassword());
                        } else {
                            // Creando un credencial para el User - Estableciendo la relación
                            Credential credential = user.getCredential();
                            userBD.addCredential(credential);
                        }
                    }

                    return ResponseEntity.ok(this.userRepository.save(userBD));

                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return this.userRepository.findById(id)
                .map(userBD -> {
                    this.userRepository.deleteById(id);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
