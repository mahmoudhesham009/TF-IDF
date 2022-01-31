package com.mahmoudH.tfidf.model;

import java.io.Serializable;
import java.util.Map;

public class Results implements Serializable {
    Map<String, DocumentData> results;

    public Results(Map<String, DocumentData> results) {
        this.results = results;
    }

    void addDocument(String s, DocumentData doc){
        results.put(s,doc);
    }

    public Map<String, DocumentData> getResults() {
        return results;
    }

    public void setResults(Map<String, DocumentData> results) {
        this.results = results;
    }
}
