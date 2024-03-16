package com.example.BedSyncFirebase.repos;

import com.example.BedSyncFirebase.models.Bed;
import com.example.BedSyncFirebase.models.Patient;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Repository
public class BedRepository {

    @Autowired
    private Firestore firestore;

    public List<Bed> findAll() throws ExecutionException, InterruptedException {
        CollectionReference beds = firestore.collection("beds");
        List<Bed> bedList = new ArrayList<>();

        ApiFuture<QuerySnapshot> querySnapshot = beds.get();
        for (QueryDocumentSnapshot documentSnapshot : querySnapshot.get().getDocuments()) {
            Bed bed = documentSnapshot.toObject(Bed.class);
            bedList.add(bed);
        }

        return bedList;
    }

    public Optional<Bed> findById(String id) {
        DocumentReference documentReference = firestore.collection("beds").document(id);
        ApiFuture<DocumentSnapshot> documentSnapshot = documentReference.get();

        try {
            DocumentSnapshot snapshot = documentSnapshot.get();
            if (snapshot.exists()) {
                Bed bed = snapshot.toObject(Bed.class);
                return Optional.ofNullable(bed);
            } else {
                return Optional.empty();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
    public Bed save(Bed bed) throws ExecutionException, InterruptedException {
        CollectionReference beds = firestore.collection("beds");
        ApiFuture<DocumentReference> result = beds.add(bed);

        // Optionally, you can set the generated ID back to the FirebaseBed object
        // bed.setId(result.get().getId());

        return null; // Return the saved FirebaseBed object
    }

    public Bed updateBed(Bed bed) throws ExecutionException, InterruptedException {
        DocumentReference bedRef = firestore.collection("beds").document(bed.getId());
        ApiFuture<WriteResult> writeResultApiFuture = bedRef.set(bed);
        return bed;
    }
    public void deleteById(String id) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = firestore.collection("beds").document(id);
        ApiFuture<WriteResult> result = documentReference.delete();
    }
    public List<Bed> findByWardId(String wardId) throws ExecutionException, InterruptedException {
        CollectionReference beds = firestore.collection("beds");
        ApiFuture<QuerySnapshot> querySnapshot = beds.whereEqualTo("wardId", wardId).get();

        List<Bed> bedList = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
            Bed bed = document.toObject(Bed.class);
            bedList.add(bed);
        }
        return bedList;
    }

     public List<Bed> findByIsAvailable(boolean available) throws ExecutionException, InterruptedException {
                CollectionReference beds = firestore.collection("beds");
                QuerySnapshot querySnapshot = beds.whereEqualTo("available", available).get().get();

                List<Bed> bedList = new ArrayList<>();
                for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                    Bed bed =document.toObject(Bed.class);
            bedList.add(bed);
        }
        return bedList;
    }

    public List<Bed> findIfClean(boolean clean) throws ExecutionException, InterruptedException {
        CollectionReference beds = firestore.collection("beds");
        QuerySnapshot querySnapshot = beds.whereEqualTo("clean", clean).get().get();

        List<Bed> bedList = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            Bed bed =document.toObject(Bed.class);
            bedList.add(bed);
        }
        return bedList;
    }
    public List<Bed> findAvailableBedsByWardId(String wardId) throws ExecutionException, InterruptedException {
        CollectionReference beds = firestore.collection("beds");
        ApiFuture<QuerySnapshot> querySnapshot = beds.whereEqualTo("wardId", wardId)
                .whereEqualTo("available", true)
                .get();

        List<Bed> bedList = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
            Bed bed = document.toObject(Bed.class);
            bedList.add(bed);
        }
        return bedList;
    }
    public List<Bed> findByTimestampBetween(LocalDateTime startTimestamp, LocalDateTime endTimestamp) throws ExecutionException, InterruptedException {
        CollectionReference beds = firestore.collection("beds");
        ApiFuture<QuerySnapshot> querySnapshot = beds.whereGreaterThanOrEqualTo("timestamp", startTimestamp)
                .whereLessThanOrEqualTo("timestamp", endTimestamp).get();

        List<Bed> bedList = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
            Bed bed = document.toObject(Bed.class);
            bedList.add(bed);
        }

        return bedList;
    }
}
