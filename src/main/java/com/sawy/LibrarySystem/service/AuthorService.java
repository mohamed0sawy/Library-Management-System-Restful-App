package com.sawy.LibrarySystem.service;

import com.sawy.LibrarySystem.model.Author;
import com.sawy.LibrarySystem.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Cacheable(value = "authors")
    public Page<Author> getAllAuthors(int page, int size, String sortBy, String sortOrder) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return authorRepository.findAll(pageable);
    }

    @Cacheable(value = "author", key = "#id")
    public Optional<Author> getAuthorById(Long id) {
        return authorRepository.findById(id);
    }

    @CacheEvict(value = "authors", allEntries = true)
    public Author createAuthor(Author author) {
        return authorRepository.save(author);
    }

    @CacheEvict(value = "authors", allEntries = true)
    @CachePut(value = "author", key = "#id")
    public Optional<Author> updateAuthor(Long id, Author authorDetails) {
        Optional<Author> authorOptional = authorRepository.findById(id);
        if (authorOptional.isPresent()) {
            Author author = authorOptional.get();
            author.setName(authorDetails.getName());
            author.setBirthDate(authorDetails.getBirthDate());
            author.setNationality(authorDetails.getNationality());
            return Optional.of(authorRepository.save(author));
        } else {
            return Optional.empty();
        }
    }

    @Caching(evict = {
            @CacheEvict(value = "authors", allEntries = true),
            @CacheEvict(value = "author", key = "#id")
    })
    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }
}
