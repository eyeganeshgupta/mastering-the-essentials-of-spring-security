package io.spring.controller;

import io.spring.entity.Book;
import io.spring.response.ApiResponse;
import io.spring.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Book>> createOrUpdateBook(@RequestBody Book book) {
        Book savedBook = bookService.saveOrUpdateBook(book);
        ApiResponse<Book> response = new ApiResponse<>(true, "Book saved successfully", savedBook);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Book>> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.getBookById(id);
        ApiResponse<Book> response;
        if (book.isPresent()) {
            response = new ApiResponse<>(true, "Book found", book.get());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response = new ApiResponse<>(false, "Book not found", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Iterable<Book>>> getAllBooks() {
        Iterable<Book> books = bookService.getAllBooks();
        ApiResponse<Iterable<Book>> response = new ApiResponse<>(true, "Books retrieved successfully", books);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable Long id) {
        boolean isDeleted = bookService.deleteBook(id);
        ApiResponse<Void> response;
        if (isDeleted) {
            response = new ApiResponse<>(true, "Book deleted successfully", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response = new ApiResponse<>(false, "Failed to delete book: Book not found", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
