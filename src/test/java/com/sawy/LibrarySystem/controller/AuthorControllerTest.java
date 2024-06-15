package com.sawy.LibrarySystem.controller;

import com.sawy.LibrarySystem.model.Author;
import com.sawy.LibrarySystem.service.AuthorService;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @Test
    @DisplayName("Get All Authors")
    void getAllAuthors_GetRequest_ReturnsPageOfAuthors() throws Exception {
        int page = 0;
        int size = 10;
        String sortBy = "id";
        String sortOrder = "asc";

        Page<Author> authors = createMockAuthorsPage();
        when(authorService.getAllAuthors(page, size, sortBy, sortOrder)).thenReturn(authors);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/authors")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sortBy", sortBy)
                        .param("sortOrder", sortOrder))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value(authors.getContent().get(0).getName()));

        verify(authorService, times(1)).getAllAuthors(page, size, sortBy, sortOrder);
    }

    @Test
    @DisplayName("Get Author By ID")
    void getAuthorById_ExistingId_ReturnsAuthor() throws Exception {
        Long id = 1L;

        Author author = new Author();
        author.setId(id);
        author.setName("sawy");
        when(authorService.getAuthorById(id)).thenReturn(Optional.of(author));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/authors/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(author.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(author.getName()));

        verify(authorService, times(1)).getAuthorById(id);
    }

    @Test
    @DisplayName("Get Author By ID if not exist")
    void getAuthorById_NonExistingId_ReturnsNotFound() throws Exception {
        Long id = 88L;

        when(authorService.getAuthorById(id)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/authors/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(authorService, times(1)).getAuthorById(id);
    }

    @Test
    @DisplayName("Create Author")
    void createAuthor_Author_ReturnsCreatedAuthor() throws Exception {

        Author requestAuthor = new Author();
        requestAuthor.setName("sawy");

        Author createdAuthor = new Author();
        createdAuthor.setId(1L);
        createdAuthor.setName(requestAuthor.getName());
        when(authorService.createAuthor(any(Author.class))).thenReturn(createdAuthor);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/authors")
                        .content(asJsonString(requestAuthor))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdAuthor.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(createdAuthor.getName()));

        verify(authorService, times(1)).createAuthor(any(Author.class));
    }

    @Test
    @DisplayName("Update Existing Author")
    void updateAuthor_IdAndAuthor_ReturnsUpdatedAuthor() throws Exception {
        Long id = 1L;

        Author requestAuthor = new Author();
        requestAuthor.setName("Updated Name");

        Author updatedAuthor = new Author();
        updatedAuthor.setId(id);
        updatedAuthor.setName(requestAuthor.getName());
        when(authorService.updateAuthor(eq(id), any(Author.class))).thenReturn(Optional.of(updatedAuthor));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/authors/{id}", id)
                        .content(asJsonString(requestAuthor))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(updatedAuthor.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(updatedAuthor.getName()));

        verify(authorService, times(1)).updateAuthor(eq(id), any(Author.class));
    }

    @Test
    @DisplayName("Update non Existing Author")
    void updateAuthor_NonExistingId_ReturnsNotFound() throws Exception {
        Long id = 88L;

        Author requestAuthor = new Author();
        requestAuthor.setName("Updated Name");

        when(authorService.updateAuthor(eq(id), any(Author.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/authors/{id}", id)
                        .content(asJsonString(requestAuthor))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(authorService, times(1)).updateAuthor(eq(id), any(Author.class));
    }

    @Test
    @DisplayName("Delete Existing Author")
    void deleteAuthor_ExistingId_ReturnsNoContent() throws Exception {
        Long id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/authors/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(authorService, times(1)).deleteAuthor(id);
    }

    private String asJsonString(Object object) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Page<Author> createMockAuthorsPage() {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author("sawy",LocalDate.of(1999, Month.APRIL,5), "egyptian"));
        authors.add(new Author("joe",LocalDate.of(1980, Month.FEBRUARY, 1), "italian"));

        return new PageImpl<>(authors, PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id")), authors.size());
    }
}