package id.backend.session_6.utils;

import com.google.auth.Credentials;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class FirebaseTokenUtil extends AbstractAuthenticationToken {

    private final FirebaseToken firebaseToken;

    public FirebaseTokenUtil(FirebaseToken firebaseToken) {
        super(null);
        this.firebaseToken = firebaseToken;
        setAuthenticated(true);
    }

    @Override
    public Credentials getCredentials() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return (Credentials) securityContext.getAuthentication().getCredentials();
    }

    @Override
    public Object getPrincipal() {
        return this.firebaseToken;
    }

    public FirebaseToken getFirebaseToken() {
        return firebaseToken;
    }
}
