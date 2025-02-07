package id.backend.session1_2.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import id.backend.session1_2.model.Book;

@Repository
public class BookRepository {

    private final List<Book> books = new ArrayList<>();
    private int nextId = 3; // Since we start with 2 books

    public BookRepository() {
        books.add(new Book(1, "Book Title 1", "Author 1"));
        books.add(new Book(2, "Book Title 2", "Author 2"));
    }

    public List<Book> findAll() {
        return new ArrayList<>(books);
    }

    public Optional<Book> findById(int id) {
        return books.stream()
                .filter(book -> book.getId() == id)
                .findFirst();
    }

    public Book save(Book book) {
        if (book.getId() == 0) {
            // New book
            book.setId(nextId++);
            books.add(book);
        } else {
            // Update existing book
            deleteById(book.getId());
            books.add(book);
        }
        return book;
    }

    public boolean deleteById(int id) {
        return books.removeIf(book -> book.getId() == id);
    }

}
