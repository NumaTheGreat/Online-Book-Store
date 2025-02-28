package com.capstone.bookStore.Repository;

import com.capstone.bookStore.Model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE " +
            "(:search IS NULL OR LOWER(b.title) LIKE %:search% OR LOWER(b.author) LIKE %:search%) AND " +
            "(:category IS NULL OR b.category = :category) AND " +
            "(:minPrice IS NULL OR b.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR b.price <= :maxPrice)")
    List<Book> findBooksByFilters(String search, String category, Double minPrice, Double maxPrice);

    @Query("SELECT DISTINCT b.category FROM Book b")
    List<String> findDistinctCategories();

}
