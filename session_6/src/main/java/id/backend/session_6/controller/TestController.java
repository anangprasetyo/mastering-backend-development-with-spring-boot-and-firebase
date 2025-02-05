package id.backend.session_6.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import id.backend.session_6.model.Member;
import id.backend.session_6.service.MemberService;

@RestController
@RequestMapping("/api")
public class TestController {
    @Autowired
    private MemberService firebaseDatabaseService;

    @GetMapping("/public")
    public String getUserInfo() {
        return "Public endpoint";
    }

    @GetMapping("/protected")
    public String protectedEndpoint() {
        return "Valid";
    }
}
