package com.magadiflo.asociaciones.jpa.app.bidireccional.v1.ManyToMany.resources;

import com.magadiflo.asociaciones.jpa.app.bidireccional.v1.ManyToMany.entities.Author;
import com.magadiflo.asociaciones.jpa.app.bidireccional.v1.ManyToMany.entities.Book;
import com.magadiflo.asociaciones.jpa.app.bidireccional.v1.ManyToMany.repositories.IBookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/bidireccional/v1/many-to-many/books")
public class BookResource {
    private final IBookRepository bookRepository;

    public BookResource(IBookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok((List<Book>) this.bookRepository.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        return ResponseEntity.ok(this.bookRepository.findById(id).orElseThrow());
    }

    @PostMapping
    public ResponseEntity<Book> saveBook(@RequestBody Book book) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.bookRepository.save(book));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        return this.bookRepository.findById(id)
                .map(bookDB -> {
                    bookDB.setTitle(book.getTitle());
                    bookDB.setPublicationYear(book.getPublicationYear());

                    if (!book.getAuthors().isEmpty()) {
                        List<Author> authorsToCreate = book.getAuthors().stream().filter(author -> author.getId() == null).toList();
                        List<Author> authorsToUpdate = book.getAuthors().stream().filter(author -> author.getId() != null).toList();

                        // CASO 01: Eliminando authors de la BD para este Book, porque no se mandó en requestBody
                        List<Author> noVienenEnRequestBody = new ArrayList<>();
                        bookDB.getAuthors().forEach(authorBD -> {
                            boolean noExiste = true;
                            for (Author author : authorsToUpdate) {
                                if (authorBD.getId().equals(author.getId())) {
                                    noExiste = false;
                                    break;
                                }
                            }
                            if (noExiste) {
                                noVienenEnRequestBody.add(authorBD);
                            }
                        });
                        noVienenEnRequestBody.forEach(bookDB::deleteAuthor);

                        // CASO 02: Actualiza los authors que ya están en la BD para este Book
                        bookDB.getAuthors().forEach(authorDB -> {
                            for (Author author : authorsToUpdate) {
                                if (Objects.equals(authorDB.getId(), author.getId())) {
                                    authorDB.setName(author.getName());
                                    authorDB.setNacionalidad(author.getNacionalidad());
                                    break;
                                }
                            }
                        });

                        // CASO 03: Agrega nuevos authors al Book
                        authorsToCreate.forEach(bookDB::addAuthor);
                    }

                    return ResponseEntity.ok(this.bookRepository.save(bookDB));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        return this.bookRepository.findById(id)
                .map(bookDB -> {
                    this.bookRepository.deleteById(id);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
