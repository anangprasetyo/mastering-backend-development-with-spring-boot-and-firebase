package id.backend.session_4.service;

import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import id.backend.session_4.model.Member;

@Service
public class MemberService {
    private final FirebaseDatabase firebaseDatabase;
    private final LocalStorageService localStorageService;

    @Autowired
    public MemberService(FirebaseApp firebaseApp, LocalStorageService localStorageService) {
        this.firebaseDatabase = FirebaseDatabase.getInstance(firebaseApp);
        this.localStorageService = localStorageService;
    }

    private DatabaseReference getDatabaseReference() {
        return firebaseDatabase.getReference("members");
    }

    public CompletableFuture<Void> createMember(Member member, MultipartFile imageFile) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Handle image upload to local storage
                if (imageFile != null && !imageFile.isEmpty()) {
                    String imagePath = localStorageService.uploadFile(imageFile);
                    member.setImageUrl(imagePath);
                }

                // Save member data to Firebase
                getDatabaseReference()
                        .child(String.valueOf(member.getId()))
                        .setValueAsync(member)
                        .get();

                return null;
            } catch (Exception e) {
                throw new RuntimeException("Failed to create member: " + e.getMessage(), e);
            }
        });
    }

    public DatabaseReference getAllMember() {
        return getDatabaseReference();
    }

    public DatabaseReference getMember(String id) {
        return getDatabaseReference().child(id);
    }

    public CompletableFuture<Void> updateMember(Member member, MultipartFile imageFile) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Get existing member to check for old image
                Member oldMember = new CompletableFuture<Member>().completeAsync(() -> {
                    final Member[] result = new Member[1];
                    getDatabaseReference()
                            .child(String.valueOf(member.getId()))
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    result[0] = dataSnapshot.getValue(Member.class);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    throw new RuntimeException("Failed to get member: " + databaseError.getMessage());
                                }
                            });
                    return result[0];
                }).get();

                // Handle image update
                if (imageFile != null && !imageFile.isEmpty()) {
                    // Delete old image if exists
                    if (oldMember != null && oldMember.getImageUrl() != null) {
                        localStorageService.deleteFile(oldMember.getImageUrl());
                    }
                    // Upload new image
                    String imagePath = localStorageService.uploadFile(imageFile);
                    member.setImageUrl(imagePath);
                } else if (oldMember != null) {
                    // Keep old image URL if no new image is provided
                    member.setImageUrl(oldMember.getImageUrl());
                }

                // Update member data in Firebase
                getDatabaseReference()
                        .child(String.valueOf(member.getId()))
                        .setValueAsync(member)
                        .get();

                return null;
            } catch (Exception e) {
                throw new RuntimeException("Failed to update member: " + e.getMessage(), e);
            }
        });
    }

    public CompletableFuture<Void> deleteMember(String id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Get member to check for image
                Member member = new CompletableFuture<Member>().completeAsync(() -> {
                    final Member[] result = new Member[1];
                    getDatabaseReference()
                            .child(id)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    result[0] = dataSnapshot.getValue(Member.class);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    throw new RuntimeException("Failed to get member: " + databaseError.getMessage());
                                }
                            });
                    return result[0];
                }).get();

                // Delete image if exists
                if (member != null && member.getImageUrl() != null) {
                    localStorageService.deleteFile(member.getImageUrl());
                }

                // Delete member data from Firebase
                getDatabaseReference()
                        .child(id)
                        .removeValueAsync()
                        .get();

                return null;
            } catch (Exception e) {
                throw new RuntimeException("Failed to delete member: " + e.getMessage(), e);
            }
        });
    }
}