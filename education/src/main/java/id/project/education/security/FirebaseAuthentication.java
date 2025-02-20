package id.project.education.security;

import com.google.firebase.auth.FirebaseToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class FirebaseAuthentication extends AbstractAuthenticationToken {
    private final FirebaseToken firebaseToken;

    public FirebaseAuthentication(FirebaseToken firebaseToken) {
        super(getAuthorities(firebaseToken));
        this.firebaseToken = firebaseToken;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null; // Tidak menyimpan kredensial setelah autentikasi
    }

    @Override
    public Object getPrincipal() {
        return firebaseToken;
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(FirebaseToken token) {
        String role = (String) token.getClaims().getOrDefault("role", "USER"); // Ambil role dari claims (jika ada)
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
    }
}
