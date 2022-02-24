package com.mahmoudH.tfidf.networking;


import com.mahmoudH.tfidf.model.Results;
import com.mahmoudH.tfidf.model.Task;

import java.net.http.HttpClient;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class OurHttpClient {
    HttpClient client;

    public OurHttpClient() {
        this.client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
    }

    CompletableFuture<Results> sendTasks(List<String>workers, List<Task> tasks){
        
    }
}
