package id.backend.session_6.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    private static final String FIREBASE_KEY_PATH = "firebase-key.json";
    private static final String DATABASE_URL = "https://rest-api-e65c7-default-rtdb.firebaseio.com";

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        // Use ClassPathResource to properly handle files in JAR
        Resource resource = new ClassPathResource(FIREBASE_KEY_PATH);

        if (!resource.exists()) {
            throw new IOException("Firebase configuration file not found: " + FIREBASE_KEY_PATH);
        }

        // Use try-with-resources to ensure proper resource cleanup
        try (InputStream serviceAccount = resource.getInputStream()) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(DATABASE_URL)
                    .build();

            // Check if Firebase is already initialized
            if (FirebaseApp.getApps().isEmpty()) {
                return FirebaseApp.initializeApp(options);
            }

            return FirebaseApp.getInstance();
        } catch (IOException e) {
            throw new IOException("Error initializing Firebase: " + e.getMessage(), e);
        }
    }
}