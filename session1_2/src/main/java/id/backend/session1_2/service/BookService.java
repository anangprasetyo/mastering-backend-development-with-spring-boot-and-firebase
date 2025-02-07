package id.backend.session1_2.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import id.backend.session1_2.model.Book;
import id.backend.session1_2.repository.BookRepository;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(int id) {
        return bookRepository.findById(id);
    }

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public Optional<Book> updateBook(int id, Book book) {
        if (!bookRepository.findById(id).isPresent()) {
            return Optional.empty();
        }
        book.setId(id);
        return Optional.of(bookRepository.save(book));
    }

    public boolean deleteBook(int id) {
        return bookRepository.deleteById(id);
    }
}
