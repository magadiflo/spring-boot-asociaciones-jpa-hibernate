package com.magadiflo.asociaciones.jpa.app.unidireccional.v1.ManyToMany.resources;

import com.magadiflo.asociaciones.jpa.app.unidireccional.v1.ManyToMany.entities.Course;
import com.magadiflo.asociaciones.jpa.app.unidireccional.v1.ManyToMany.entities.Student;
import com.magadiflo.asociaciones.jpa.app.unidireccional.v1.ManyToMany.repositories.IStudentRepository;
import com.magadiflo.asociaciones.jpa.app.unidireccional.v1.OneToMany.entities.Player;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/unidireccional/v1/many-to-many/students")
public class StudentResource {
    private final IStudentRepository studentRepository;

    public StudentResource(IStudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok((List<Student>) this.studentRepository.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        return ResponseEntity.ok(this.studentRepository.findById(id).orElseThrow());
    }

    @PostMapping
    public ResponseEntity<Student> saveStudent(@RequestBody Student student) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.studentRepository.save(student));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        return this.studentRepository.findById(id)
                .map(studentBD -> {
                    studentBD.setCode(student.getCode());
                    studentBD.setName(student.getName());

                    if (!student.getCourses().isEmpty()) {
                        List<Course> coursesToCreate = student.getCourses().stream().filter(course -> course.getId() == null).toList();
                        List<Course> coursesToUpdate = student.getCourses().stream().filter(course -> course.getId() != null).toList();

                        // CASO 01: Eliminando courses de la BD para este Student, porque no se mandó en requestBody
                        studentBD.getCourses().removeIf(courseBD -> {
                            boolean noExiste = true;
                            for (Course course : coursesToUpdate) {
                                if (courseBD.getId().equals(course.getId())) {
                                    noExiste = false;
                                    break;
                                }
                            }
                            return noExiste;
                        });

                        // CASO 02: Actualiza los players que ya están en la BD para este Team
                        studentBD.getCourses().forEach(courseBD -> {
                            for (Course course : coursesToUpdate) {
                                if (Objects.equals(courseBD.getId(), course.getId())) {
                                    courseBD.setName(course.getName());
                                    courseBD.setCredits(course.getCredits());
                                    break;
                                }
                            }
                        });

                        // CASO 03: Agrega nuevos cursos al student
                        coursesToCreate.forEach(studentBD::addCourse);
                    }

                    return ResponseEntity.ok(this.studentRepository.save(studentBD));
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        return this.studentRepository.findById(id)
                .map(studentBD -> {
                    this.studentRepository.deleteById(id);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
