package com.magadiflo.asociaciones.jpa.app.unidireccional.v1.ManyToOne.resources;

import com.magadiflo.asociaciones.jpa.app.unidireccional.v1.ManyToOne.entities.Employee;
import com.magadiflo.asociaciones.jpa.app.unidireccional.v1.ManyToOne.repositories.IEmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/unidireccional/v1/many-to-one")
public class EmployeeResource {

    private final IEmployeeRepository employeeRepository;

    public EmployeeResource(IEmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok((List<Employee>) this.employeeRepository.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(this.employeeRepository.findById(id).orElseThrow());
    }

    @PostMapping
    public ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.employeeRepository.save(employee));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        return this.employeeRepository.findById(id)
                .map(employeeBD -> {
                    employeeBD.setFullName(employee.getFullName());
                    employeeBD.setEmail(employee.getEmail());
                    employeeBD.setPhone(employee.getPhone());
                    if (employee.getDepartment() != null) {
                        employeeBD.setDepartment(employee.getDepartment());
                    }
                    return ResponseEntity.ok(this.employeeRepository.save(employeeBD));
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        return this.employeeRepository.findById(id)
                .map(employeeBD -> {
                    this.employeeRepository.deleteById(id);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
