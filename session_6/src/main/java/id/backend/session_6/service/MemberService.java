package id.backend.session_6.service;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import id.backend.session_6.model.Member;

@Service
public class MemberService {

    private final FirebaseDatabase firebaseDatabase;

    @Autowired
    public MemberService(FirebaseApp firebaseApp) {
        this.firebaseDatabase = FirebaseDatabase.getInstance(firebaseApp);
    }

    private DatabaseReference getDatabaseReference() {
        return firebaseDatabase.getReference("members");
    }

    public CompletableFuture<Void> createMember(Member member) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                getDatabaseReference().child(String.valueOf(member.getId())).setValueAsync(member).get();
                return null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public DatabaseReference getAllMember() {
        return getDatabaseReference();
    }

    public DatabaseReference getMember(String id) {
        return getDatabaseReference().child(id);
    }

    public CompletableFuture<Void> updateMember(Member member) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                getDatabaseReference().child(String.valueOf(member.getId())).setValueAsync(member).get();
                return null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<Void> deleteMember(String id) {
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
