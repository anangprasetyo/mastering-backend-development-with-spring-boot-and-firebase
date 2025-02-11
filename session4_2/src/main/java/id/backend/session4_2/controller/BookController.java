package id.backend.session4_2.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import id.backend.session4_2.model.Book;
import id.backend.session4_2.service.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService firebaseDatabaseService;

    @PostMapping
    public ResponseEntity<String> createBook(@RequestBody Book book) {
        try {
            firebaseDatabaseService.createBook(book).get();
            return ResponseEntity.ok("Book created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to create book: " + e.getMessage());
        }
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<Book>>> getAllBook() {
        CompletableFuture<ResponseEntity<List<Book>>> future = new CompletableFuture<>();
        DatabaseReference bookRef = firebaseDatabaseService.getAllBook();

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
        DatabaseReference bookRef = firebaseDatabaseService.getBook(id);

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
    public ResponseEntity<String> updateBook(@RequestBody Book book) {
        try {
            firebaseDatabaseService.updateBook(book).get();
            return ResponseEntity.ok("Book updated successfully");
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(500).body("Failed to update book: " +
                    e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable String id) {
        try {
            firebaseDatabaseService.deleteBook(id).get();
            return ResponseEntity.ok("Book deleted successfully");
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(500).body("Failed to delete book: " +
                    e.getMessage());
        }
    }
}
