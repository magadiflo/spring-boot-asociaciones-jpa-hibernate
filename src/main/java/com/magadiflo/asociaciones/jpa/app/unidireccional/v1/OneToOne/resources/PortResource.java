package com.magadiflo.asociaciones.jpa.app.unidireccional.v1.OneToOne.resources;

import com.magadiflo.asociaciones.jpa.app.unidireccional.v1.OneToOne.entities.Port;
import com.magadiflo.asociaciones.jpa.app.unidireccional.v1.OneToOne.repositories.IPortRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/unidireccional/v1/one-to-one/ports")
public class PortResource {

    private final IPortRepository portRepository;

    public PortResource(IPortRepository portRepository) {
        this.portRepository = portRepository;
    }

    @GetMapping
    public ResponseEntity<List<Port>> getAllPorts() {
        return ResponseEntity.ok((List<Port>) this.portRepository.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Port> showPort(@PathVariable Long id) {
        return ResponseEntity.ok(this.portRepository.findById(id).orElseThrow());
    }

    @PostMapping
    public ResponseEntity<Port> savePort(@RequestBody Port port) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.portRepository.save(port));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Port> updatePort(@PathVariable Long id, @RequestBody Port port) {
        return this.portRepository.findById(id)
                .map(portBD -> {
                    portBD.setNumber(port.getNumber());
                    portBD.setType(port.getType());

                    if (port.getService() != null) {
                        if (portBD.getService() != null) {
                            portBD.getService().setName(port.getService().getName());
                            portBD.getService().setPath(port.getService().getPath());
                        } else {
                            portBD.setService(port.getService());
                        }
                    }
                    return ResponseEntity.ok().body(this.portRepository.save(portBD));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deletePort(@PathVariable Long id) {
        return this.portRepository.findById(id)
                .map(portBD -> {
                    this.portRepository.deleteById(id);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
