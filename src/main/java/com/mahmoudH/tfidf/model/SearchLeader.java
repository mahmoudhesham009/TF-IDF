package com.mahmoudH.tfidf.model;

import com.mahmoudH.tfidf.clasterManagment.ServiceRegistry;
import com.mahmoudH.tfidf.networking.OnRequestCallback;
import com.mahmoudH.tfidf.search.TFIDF;
import org.apache.zookeeper.KeeperException;
import org.w3c.dom.ls.LSInput;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SearchLeader implements OnRequestCallback {
    ServiceRegistry workers;
    public SearchLeader(ServiceRegistry workers) {
        this.workers=workers;

    }

    @Override
    public byte[] handleRequest(byte[] request) throws FileNotFoundException, InterruptedException, KeeperException {
        String terms=(String)SerializationUtil.deserialize(request);
        List<String> docs=readDocList();
        List<List<String>> distWork=distributedWork(docs,workers);

        return new byte[0];
    }

    @Override
    public String getEndPoint() {
        return null;
    }


    List<Results> sendTasks(List<List<String>> distWork, String terms){
        List<String> termsInList= TFIDF.getWordsFromLine(terms);
        for(List<String>dist:distWork){
            Task task=new Task(termsInList,dist);

        }
    }


    List<List<String>> distributedWork(List<String> docs,ServiceRegistry serviceRegistry) throws InterruptedException, KeeperException {
        int numOfWorkers=serviceRegistry.getAllAddress().size();
        if(numOfWorkers==0){
            System.out.println("there is no workers available");
            return null;
        }
        int pointer=0;
        List<List<String>> disDoc=new ArrayList<>();
        for(int i=0;i<disDoc.size()-1;i++){
            disDoc.add(new ArrayList<>());
        }

        for(String doc: docs){
            if(pointer==numOfWorkers)
                pointer=0;

            List<String> list=disDoc.get(pointer);
            list.add(doc);
            pointer++;
        }
        return disDoc;
    }

    List<String> readDocList(){
        File file=new File("src/main/resources/books");
        return Arrays.asList(file.list())
                .stream().map(name->"src/main/resources/books/"+name)
                .collect(Collectors.toList());
    }
}
