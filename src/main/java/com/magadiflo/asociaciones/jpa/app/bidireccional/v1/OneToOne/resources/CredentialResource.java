package com.magadiflo.asociaciones.jpa.app.bidireccional.v1.OneToOne.resources;

import com.magadiflo.asociaciones.jpa.app.bidireccional.v1.OneToOne.entities.Credential;
import com.magadiflo.asociaciones.jpa.app.bidireccional.v1.OneToOne.entities.User;
import com.magadiflo.asociaciones.jpa.app.bidireccional.v1.OneToOne.repositories.ICredentialRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/bidireccional/v1/one-to-one/credentials")
public class CredentialResource {
    private final ICredentialRepository credentialRepository;

    public CredentialResource(ICredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    @GetMapping
    public ResponseEntity<List<Credential>> getAllCredentials() {
        return ResponseEntity.ok((List<Credential>) this.credentialRepository.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Credential> getCredential(@PathVariable Long id) {
        return ResponseEntity.ok(this.credentialRepository.findById(id).orElseThrow());
    }

    @PostMapping
    public ResponseEntity<Credential> saveCredential(@RequestBody Credential credential) {
        // Antes de registrar un Credential establecemos la relación en ambos sentidos con User
        if (credential.getUser() != null) {
            User user = credential.getUser();
            credential.addUser(user);
        }
        return ResponseEntity.ok(this.credentialRepository.save(credential));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Credential> updateCredential(@PathVariable Long id, @RequestBody Credential credential) {
        return this.credentialRepository.findById(id)
                .map(credentialBD -> {
                    credentialBD.setUsername(credential.getUsername());
                    credentialBD.setPassword(credential.getPassword());

                    if (credential.getUser() != null) {
                        if (credentialBD.getUser() != null) {
                            credentialBD.getUser().setNombre(credential.getUser().getNombre());
                            credentialBD.getUser().setRole(credential.getUser().getRole());
                        } else {
                            // Creando un user para el Credencial - Estableciendo la relación
                            User user = credential.getUser();
                            credentialBD.addUser(user);
                        }
                    }

                    return ResponseEntity.ok(this.credentialRepository.save(credentialBD));

                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteCredential(@PathVariable Long id) {
        return this.credentialRepository.findById(id)
                .map(credentialDB -> {
                    // Al quitar la asociación entre credential y user, y luego hacer el save de credential
                    // lo que hará hibernate es eliminar solo la credential asociado al usuario en la BD
                    credentialDB.deleteUser();
                    this.credentialRepository.save(credentialDB);

                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
