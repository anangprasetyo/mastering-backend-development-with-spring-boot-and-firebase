package id.backend.session_4.model;

import lombok.Data;

@Data
public class Member {
    private String id;
    private String name;
    private String email;
    private String telephone;
    private String imageUrl; // URL of the uploaded image
}