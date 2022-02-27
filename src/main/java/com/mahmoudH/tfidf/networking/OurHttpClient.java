package com.mahmoudH.tfidf.networking;


import com.mahmoudH.tfidf.model.Results;
import com.mahmoudH.tfidf.model.SerializationUtil;
import com.mahmoudH.tfidf.model.Task;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class OurHttpClient {
    HttpClient client;

    public OurHttpClient() {
        this.client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
    }

    public CompletableFuture<Results> sendTasks(String worker, Task task){
        HttpRequest httpRequest=HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofByteArray(SerializationUtil.serialize(task)))
                .uri(URI.create(worker))
                .build();

        return client.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofByteArray())
                .thenApply(HttpResponse::body)
                .thenApply(res -> (Results) SerializationUtil.deserialize(res));
    }
}
