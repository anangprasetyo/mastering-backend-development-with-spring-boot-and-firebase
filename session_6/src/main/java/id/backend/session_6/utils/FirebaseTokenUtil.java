package id.backend.session_6.utils;

import com.google.firebase.auth.FirebaseToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class FirebaseTokenUtil extends AbstractAuthenticationToken {

    private final FirebaseToken firebaseToken;

    public FirebaseTokenUtil(FirebaseToken firebaseToken) {
        super(null);
        this.firebaseToken = firebaseToken;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.firebaseToken;
    }

    public FirebaseToken getFirebaseToken() {
        return firebaseToken;
    }
}
