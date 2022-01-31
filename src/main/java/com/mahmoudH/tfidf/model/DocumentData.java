package com.mahmoudH.tfidf.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DocumentData implements Serializable {
    private Map<String, Double> termToFreq=new HashMap<String, Double>();

    public void putTerm(String term, double freq){
        termToFreq.put(term,freq);
    }

    public double getFreq(String term){
        return termToFreq.get(term);
    }
}
