package com.phlammariont.tangela.service;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class DaoService {
    private Firestore db;

    public void init() throws IOException {
        System.out.println("connecting the DB");
        // Use a service account
        InputStream serviceAccount = new FileInputStream("/Users/lruedag/development/ncprojects/tangela/nurse-chansey-firebase-adminsdk-mz8iz-8fb4227f08.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .build();
        FirebaseApp.initializeApp(options);

        db = FirestoreClient.getFirestore();
    }

    public void saveNewBestSolution(Map<String, Object> data) {
        data.put("BestSolution", false);
        this.saveSolution(data);
    }
    public void saveBestSolution(Map<String, Object> data) {
        data.put("BestSolution", true);
        this.saveBestSolution(data);
    }

    public void saveSolution(Map<String, Object> data) {
        DocumentReference docRef = db.collection("planner").document((String) data.get("id"));
        //asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);
        // ...
        // result.get() blocks on response
        try {
            System.out.println("Update time : " + result.get().getUpdateTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
