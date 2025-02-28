package com.capstone.bookStore.Controller;

import com.capstone.bookStore.Model.Book;
import com.capstone.bookStore.Service.BookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookRestController {
    private final BookService bookService;

    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Retrieves a list of books based on the user's filter parameters.
     * Presents all the books by default when no filters are set.
     *
     * @param search   filters the books by Author or Title
     * @param category filters the books by category
     * @param minPrice minimum price to filter books
     * @param maxPrice maximum price to filter books
     *
     */
    @GetMapping
    public List<Book> getBooks(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ) {
        // RETURNS ALL BOOKS IF NO FILTERS ARE SET
        if ((search == null || search.isEmpty()) &&
                (category == null || category.isEmpty()) &&
                minPrice == null &&
                maxPrice == null) {
            return bookService.getAllBooks();
        }
        // RETURNS THE FILTERED BOOKS BASED ON THE USER'S FILTERS
        return bookService.searchAndFilterBooks(search, category, minPrice, maxPrice);
    }
}