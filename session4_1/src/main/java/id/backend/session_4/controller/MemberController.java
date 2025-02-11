package id.backend.session_4.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import id.backend.session_4.model.Member;
import id.backend.session_4.service.MemberService;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createMember(
            @RequestPart("member") String memberJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            // Convert JSON string to Member object
            Member member = objectMapper.readValue(memberJson, Member.class);
            // Now process 'member' and 'image' as needed
            memberService.createMember(member, image).get();
            return ResponseEntity.ok("Member created successfully!");

        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Invalid JSON format for member");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to create member: " + e.getMessage());
        }
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<Member>>> getAllMember() {
        CompletableFuture<ResponseEntity<List<Member>>> future = new CompletableFuture<>();
        DatabaseReference memberRef = memberService.getAllMember();

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
        DatabaseReference memberRef = memberService.getMember(id);

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

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateMember(
            @RequestPart("member") String memberJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            // Convert JSON string to Member object
            Member member = objectMapper.readValue(memberJson, Member.class);
            // Now process 'member' and 'image' as needed
            memberService.updateMember(member, image).get();
            return ResponseEntity.ok("Member updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to update member: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMember(@PathVariable String id) {
        try {
            memberService.deleteMember(id).get();
            return ResponseEntity.ok("Member deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to delete member: " + e.getMessage());
        }
    }
}