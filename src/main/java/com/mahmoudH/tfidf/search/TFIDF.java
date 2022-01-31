package com.mahmoudH.tfidf.search;

import com.mahmoudH.tfidf.model.DocumentData;

import java.util.*;

public class TFIDF {

    public static double calcTermFreq(List<String> bookWords, String term) {
        double counter = 0;
        for (String word : bookWords) {
            if (word.equalsIgnoreCase(term))
                counter++;
        }
        return counter / bookWords.size();
    }

    public static DocumentData calcFreqForDoc(List<String> bookWords, List<String> terms) {
        DocumentData doc=new DocumentData();
        for(String term:terms){
            doc.putTerm(term,calcTermFreq(bookWords,term));
        }
        return doc;
    }

    public static double getInverseDocFreq(String term, Map<String,DocumentData> docs){
        int counter=0;
        for(String doc: docs.keySet()){
            DocumentData docData=docs.get(doc);
            if(docData.getFreq(term)>0)
                counter++;
        }
        return counter==0?0:Math.log10(docs.size()/counter);
    }

    public static Map<String,Double> getInverseDocFreqForTerms(List<String> terms, Map<String,DocumentData> docs){
        Map<String,Double> idf=new HashMap<String, Double>();

        for(String term: terms){
            idf.put(term,getInverseDocFreq(term,docs));
        }

        return idf;
    }

    static double calcScoreForDoc(List<String> terms, DocumentData doc, Map<String,Double> idf){
        double score=0;
        for (String term : terms) {
            score+=doc.getFreq(term)*idf.get(term);
        }
        return score;
    }

    public static Map<String, Double> getDocsSortedByScore(List<String> terms, Map<String, DocumentData> docResult){
        Map<String,Double> results=new HashMap<>();

        Map<String,Double> termToIdf=getInverseDocFreqForTerms(terms, docResult);

        for(String doc: docResult.keySet()){
            double score=calcScoreForDoc(terms,docResult.get(doc),termToIdf);
            results.put(doc,score);
        }
        return results;
    }


    public static List<String> getWordsFromLines(List<String> lines) {
        List<String> words = new ArrayList<>();
        for (String l : lines) {
            String[] w = l.split("(\\.)+|(,)+|( )+|(-)+|(\\?)+|(!)+|(;)+|(:)+|(/d)+|(/n)+");
            for (String i : w)
                words.add(i);
        }
        return words;
    }

    public static List<String> getWordsFromLine(String line) {
        List<String> words = new ArrayList<>();

        String[] w = line.split("(\\.)+|(,)+|( )+|(-)+|(\\?)+|(!)+|(;)+|(:)+|(/d)+|(/n)+");
        for (String i : w)
            words.add(i);

        return words;
    }


}
