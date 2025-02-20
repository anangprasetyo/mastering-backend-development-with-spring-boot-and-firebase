package id.project.education.service;

import com.google.firebase.auth.*;
import com.google.firebase.auth.UserRecord.CreateRequest;
import org.springframework.stereotype.Service;

@Service
public class FirebaseService {
    public String createFirebaseUser(String email, String password) throws FirebaseAuthException {
        CreateRequest request = new CreateRequest()
                .setEmail(email)
                .setPassword(password);
        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        return userRecord.getUid(); // Kembalikan UID Firebase
    }

    public void updateFirebaseUser(String uid, String email, String password) throws FirebaseAuthException {
        UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(uid)
                .setEmail(email)
                .setPassword(password);

        FirebaseAuth.getInstance().updateUser(request);
    }

    public void deleteFirebaseUser(String uid) throws FirebaseAuthException {
        FirebaseAuth.getInstance().deleteUser(uid);
    }
}
