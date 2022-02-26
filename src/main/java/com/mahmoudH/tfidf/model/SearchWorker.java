package com.mahmoudH.tfidf.model;

import com.mahmoudH.tfidf.networking.OnRequestCallback;
import com.mahmoudH.tfidf.search.TFIDF;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class SearchWorker implements OnRequestCallback {
    @Override
    public byte[] handleRequest(byte[] request) throws FileNotFoundException {
        System.out.println("Recived the req");
        Task task= (Task) SerializationUtil.deserialize(request);
        Results results=new Results(new HashMap<>());
        for(String s: task.documents){
            List<String> words=parseDoc(s);
            DocumentData documentData= TFIDF.calcFreqForDoc(words,task.searchTerms);
            results.addDocument(s,documentData);
        }
        return SerializationUtil.serialize(results);
    }

    private List<String> parseDoc(String s) throws FileNotFoundException {
        FileReader fileReader=new FileReader(s);
        BufferedReader bufferedReader=new BufferedReader(fileReader);
        List<String> lines=bufferedReader.lines().collect(Collectors.toList());
        return TFIDF.getWordsFromLines(lines);
    }

    @Override
    public String getEndPoint() {
        return "/task";
    }
}
