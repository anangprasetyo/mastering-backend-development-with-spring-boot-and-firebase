package id.backend.session1_2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class Book {
    private int id;
    private String title;
    private String author;
}
