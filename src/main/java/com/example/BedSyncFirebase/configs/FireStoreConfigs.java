package com.example.BedSyncFirebase.configs;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Objects;

@Configuration
public class FireStoreConfigs {

    @Bean
    public Firestore firestore() throws IOException {
        // Load your Google Cloud Platform credentials file (serviceAccountKey.json)
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                Objects.requireNonNull(getClass().getResourceAsStream("/serviceAccountKey.json")));

        // Build FirestoreOptions with the credentials
        FirestoreOptions options = FirestoreOptions.newBuilder()
                .setCredentials(credentials)
                .build();

        return options.getService();
    }
}
