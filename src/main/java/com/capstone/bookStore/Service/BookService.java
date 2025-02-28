package com.capstone.bookStore.Service;

import com.capstone.bookStore.Model.Book;
import com.capstone.bookStore.Repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        System.out.println("Fetched books: " + books);
        return books;
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public List<Book> searchAndFilterBooks(String search, String category, Double minPrice, Double maxPrice) {
        return bookRepository.findBooksByFilters(search, category, minPrice, maxPrice);
    }

    public List<String> getAllCategories() {
        return bookRepository.findDistinctCategories();
    }


}