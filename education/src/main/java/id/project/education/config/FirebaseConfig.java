package id.project.education.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.io.InputStream;

// Deployment
// import java.io.ByteArrayInputStream;
// import com.google.cloud.storage.Blob;
// import com.google.cloud.storage.Storage;
// import com.google.cloud.storage.StorageOptions;

@Configuration
public class FirebaseConfig {

    // Local path to the Firebase key file
    private static final String FIREBASE_KEY_PATH = "firebase-service-account.json";

    // Deployment path to the Firebase key file
    // Storage storage = StorageOptions.getDefaultInstance().getService();
    // String bucketName = "springboot-restapi.appspot.com";
    // String filePath = "firebase-service-account.json";

    private static final String DATABASE_URL = "https://rest-api-e65c7-default-rtdb.firebaseio.com";

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        // Local path load Firebase key file
        // Use ClassPathResource to properly handle files in JAR
        Resource resource = new ClassPathResource(FIREBASE_KEY_PATH);

        if (!resource.exists()) {
            throw new IOException("Firebase configuration file not found: " + FIREBASE_KEY_PATH);
        }

        try (InputStream serviceAccount = resource.getInputStream()) {

            // Deployment path load Firebase key file from GCS bucket
            // Blob blob = storage.get(bucketName, filePath);

            // if (blob == null) {
            // throw new RuntimeException("Firebase key file not found in GCS bucket!");
            // }

            // byte[] keyFileBytes = blob.getContent();
            // ByteArrayInputStream serviceAccount = new ByteArrayInputStream(keyFileBytes);

            // try {
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