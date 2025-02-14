package id.backend.session6_2.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import id.backend.session6_2.model.Book;

@Service
public class BookService {
    private final FirebaseDatabase firebaseDatabase;
    private final LocalStorageService localStorageService;

    @Autowired
    public BookService(FirebaseApp firebaseApp, LocalStorageService localStorageService) {
        this.firebaseDatabase = FirebaseDatabase.getInstance(firebaseApp);
        this.localStorageService = localStorageService;
    }

    private DatabaseReference getDatabaseReference() {
        return firebaseDatabase.getReference("books");
    }

    public CompletableFuture<Void> createBook(Book book, MultipartFile imageFile) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Handle image upload to local storage
                if (imageFile != null && !imageFile.isEmpty()) {
                    String imagePath = localStorageService.uploadFile(imageFile);
                    book.setImageUrl(imagePath);
                }
                // create book data in Firebase
                getDatabaseReference()
                        .child(String.valueOf(book.getId()))
                        .setValueAsync(book)
                        .get();
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

    public CompletableFuture<Void> updateBook(Book book, MultipartFile imageFile) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Get existing book to check for old image
                CountDownLatch latch = new CountDownLatch(1);
                final Book[] bookHold = new Book[1];

                getDatabaseReference().child(book.getId())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                bookHold[0] = dataSnapshot.getValue(Book.class);
                                latch.countDown();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                latch.countDown();
                            }
                        });

                latch.await();

                Book oldBook = bookHold[0];

                // Handle image update
                if (imageFile != null && !imageFile.isEmpty()) {
                    // Delete old image if exists
                    if (oldBook != null && oldBook.getImageUrl() != null) {
                        localStorageService.deleteFile(oldBook.getImageUrl());
                    }
                    // Upload new image
                    String imagePath = localStorageService.uploadFile(imageFile);
                    book.setImageUrl(imagePath);
                } else if (oldBook != null) {
                    // Keep old image URL if no new image is provided
                    book.setImageUrl(oldBook.getImageUrl());
                }

                // update book data in Firebase
                getDatabaseReference()
                        .child(String.valueOf(book.getId()))
                        .setValueAsync(book)
                        .get();
                return null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<Void> deleteBook(String id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Get book to check for image
                CountDownLatch latch = new CountDownLatch(1);
                final Book[] bookHold = new Book[1];

                getDatabaseReference().child(id)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                bookHold[0] = dataSnapshot.getValue(Book.class);
                                latch.countDown();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                latch.countDown();
                            }
                        });

                latch.await();

                Book book = bookHold[0];

                // Delete image if exists
                if (book != null && book.getImageUrl() != null) {
                    localStorageService.deleteFile(book.getImageUrl());
                }

                getDatabaseReference()
                        .child(id)
                        .removeValueAsync()
                        .get();
                return null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
