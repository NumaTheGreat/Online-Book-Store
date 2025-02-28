package com.capstone.bookStore.Controller;

import com.capstone.bookStore.Model.Book;
import com.capstone.bookStore.Service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Displays all the books in the Book Catalog page.
     * Retrieves all books and categories, applies any search or filter criteria,
     * and prepares the model for rendering the book catalog view.
     *
     * @param search   search term to filter books by title or author
     * @param category category to filter books
     * @param minPrice minimum price to filter books
     * @param maxPrice maximum price to filter books
     */

    @GetMapping("/bookCatalog")
    public String bookCatalog(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            Model model) {

        List<Book> books = bookService.getAllBooks();
        List<String> categories = bookService.getAllCategories();

        model.addAttribute("books", books);
        model.addAttribute("categories", categories);
        model.addAttribute("search", search);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("cartId", 1L);

        return "bookCatalog";
    }
}