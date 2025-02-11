package id.backend.session4_2.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import id.backend.session4_2.model.Book;

@Service
public class BookService {
    private final FirebaseDatabase firebaseDatabase;

    @Autowired
    public BookService(FirebaseApp firebaseApp) {
        this.firebaseDatabase = FirebaseDatabase.getInstance(firebaseApp);
    }

    private DatabaseReference getDatabaseReference() {
        return firebaseDatabase.getReference("books");
    }

    public CompletableFuture<Void> createBook(Book book) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                getDatabaseReference().child(String.valueOf(book.getId())).setValueAsync(book).get();
                return null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public DatabaseReference getAllBook() {
        return getDatabaseReference();
    }

    public DatabaseReference getBook(String id) {
        return getDatabaseReference().child(id);
    }

    public CompletableFuture<Void> updateBook(Book book) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                getDatabaseReference().child(String.valueOf(book.getId())).setValueAsync(book).get();
                return null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<Void> deleteBook(String id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                getDatabaseReference().child(id).removeValueAsync().get();
                return null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
