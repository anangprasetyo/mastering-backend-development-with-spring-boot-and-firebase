package id.project.education.config;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    // private static final String FIREBASE_KEY_PATH =
    // "firebase-service-account.json";
    Storage storage = StorageOptions.getDefaultInstance().getService();
    String bucketName = "springboot-restapi.appspot.com";
    String filePath = "firebase-service-account.json";

    private static final String DATABASE_URL = "https://rest-api-e65c7-default-rtdb.firebaseio.com";

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        Blob blob = storage.get(bucketName, filePath);

        if (blob == null) {
            throw new RuntimeException("Firebase key file not found in GCS bucket!");
        }

        byte[] keyFileBytes = blob.getContent();
        ByteArrayInputStream serviceAccount = new ByteArrayInputStream(keyFileBytes);

        // Use try-with-resources to ensure proper resource cleanup
        try {
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