package id.backend.session_6.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import id.backend.session_6.model.Member;
import id.backend.session_6.service.MemberService;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    @Autowired
    private MemberService firebaseDatabaseService;

    @PostMapping
    public ResponseEntity<String> createMember(@RequestBody Member member) {
        try {
            firebaseDatabaseService.createMember(member).get();
            return ResponseEntity.ok("Member created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to create member: " + e.getMessage());
        }
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<Member>>> getAllMember() {
        CompletableFuture<ResponseEntity<List<Member>>> future = new CompletableFuture<>();
        DatabaseReference memberRef = firebaseDatabaseService.getAllMember();

        memberRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Member> members = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Member member = snapshot.getValue(Member.class);
                        members.add(member);
                    }
                    future.complete(ResponseEntity.ok(members));
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
    public CompletableFuture<ResponseEntity<Member>> getMember(@PathVariable String id) {
        CompletableFuture<ResponseEntity<Member>> future = new CompletableFuture<>();
        DatabaseReference memberRef = firebaseDatabaseService.getMember(id);

        memberRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Member member = dataSnapshot.getValue(Member.class);
                    future.complete(ResponseEntity.ok(member));
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
    public ResponseEntity<String> updateMember(@RequestBody Member member) {
        try {
            firebaseDatabaseService.updateMember(member).get();
            return ResponseEntity.ok("Member updated successfully");
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(500).body("Failed to update member: " +
                    e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMember(@PathVariable String id) {
        try {
            firebaseDatabaseService.deleteMember(id).get();
            return ResponseEntity.ok("Member deleted successfully");
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(500).body("Failed to delete member: " +
                    e.getMessage());
        }
    }
}
