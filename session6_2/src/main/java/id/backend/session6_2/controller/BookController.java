package id.backend.session6_2.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import id.backend.session6_2.model.Book;
import id.backend.session6_2.service.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createBook(
            @RequestPart("book") String bookJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            // Convert JSON string to Book object
            Book book = objectMapper.readValue(bookJson, Book.class);
            // Now process 'book' and 'image' as needed
            bookService.createBook(book, image).get();
            return ResponseEntity.ok("Book created successfully!");

        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Invalid JSON format for book");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to create book: " + e.getMessage());
        }
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<Book>>> getAllBook() {
        CompletableFuture<ResponseEntity<List<Book>>> future = new CompletableFuture<>();
        DatabaseReference bookRef = bookService.getAllBook();

        bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Book> books = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Book book = snapshot.getValue(Book.class);
                        books.add(book);
                    }
                    future.complete(ResponseEntity.ok(books));
                } else {
                    future.complete(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<Book>> getBook(@PathVariable String id) {
        CompletableFuture<ResponseEntity<Book>> future = new CompletableFuture<>();
        DatabaseReference bookRef = bookService.getBook(id);

        bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Book book = dataSnapshot.getValue(Book.class);
                    future.complete(ResponseEntity.ok(book));
                } else {
                    future.complete(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }

    @PutMapping
    public ResponseEntity<String> updateBook(
            @RequestPart("book") String bookJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            // Convert JSON string to Book object
            Book book = objectMapper.readValue(bookJson, Book.class);
            // Now process 'book' and 'image' as needed
            bookService.updateBook(book, image).get();
            return ResponseEntity.ok("Book updated successfully");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Invalid JSON format for book");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to update book: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable String id) {
        try {
            bookService.deleteBook(id).get();
            return ResponseEntity.ok("Book deleted successfully");
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(500).body("Failed to delete book: " +
                    e.getMessage());
        }
    }
}
