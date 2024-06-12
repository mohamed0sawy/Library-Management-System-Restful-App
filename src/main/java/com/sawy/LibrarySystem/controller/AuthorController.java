package com.sawy.LibrarySystem.controller;

import com.sawy.LibrarySystem.model.Author;
import com.sawy.LibrarySystem.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @Operation(summary = "Get all authors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authors found"),
            @ApiResponse(responseCode = "404", description = "Authors not found", content = @Content)
    })
    @GetMapping("/authors")
    public ResponseEntity<Page<Author>> getAllAuthors(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      @RequestParam(defaultValue = "id") String sortBy,
                                                      @RequestParam(defaultValue = "asc") String sortOrder) {
        Page<Author> authors = authorService.getAllAuthors(page, size, sortBy, sortOrder);
        return ResponseEntity.ok(authors);
    }

    @Operation(summary = "Get specific author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author found"),
            @ApiResponse(responseCode = "404", description = "Author not found", content = @Content)
    })
    @GetMapping("/authors/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        Optional<Author> author = authorService.getAuthorById(id);
        return author.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create new author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author created"),
            @ApiResponse(responseCode = "400", description = "Author can't created", content = @Content)
    })
    @PostMapping("/authors")
    public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
        Author createdAuthor = authorService.createAuthor(author);
        return ResponseEntity.ok(createdAuthor);
    }

    @Operation(summary = "Update author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author updated"),
            @ApiResponse(responseCode = "400", description = "Author not updated", content = @Content)
    })
    @PutMapping("/authors/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @RequestBody Author authorDetails) {
        Optional<Author> updatedAuthor = authorService.updateAuthor(id, authorDetails);
        return updatedAuthor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "delete author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author deleted"),
            @ApiResponse(responseCode = "404", description = "Author not deleted", content = @Content)
    })
    @DeleteMapping("/authors/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}
