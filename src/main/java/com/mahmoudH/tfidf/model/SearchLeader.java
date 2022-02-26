package com.mahmoudH.tfidf.model;

import com.mahmoudH.tfidf.clasterManagment.ServiceRegistry;
import com.mahmoudH.tfidf.networking.OnRequestCallback;
import com.mahmoudH.tfidf.networking.OurHttpClient;
import com.mahmoudH.tfidf.search.TFIDF;
import org.apache.zookeeper.KeeperException;
import org.w3c.dom.ls.LSInput;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class SearchLeader implements OnRequestCallback {
    ServiceRegistry workers;
    OurHttpClient client;
    public SearchLeader(ServiceRegistry workers) {
        this.workers=workers;
        client=new OurHttpClient();
    }

    @Override
    public byte[] handleRequest(byte[] request) throws FileNotFoundException, InterruptedException, KeeperException {
        String terms=(String)SerializationUtil.deserialize(request);
        List<String> docs=readDocList();
        List<List<String>> distWork=distributedWork(docs,workers);

        List<Results> results=sendTasks(distWork,terms);

        Map<String , DocumentData> finalResults=new HashMap<>();
        for(Results res:results){
            finalResults.putAll(res.getResults());
        }

        Map<String , Double > finalRes= TFIDF.getDocsSortedByScore(Arrays.asList(terms.split(" ").clone()), finalResults);

        for(String s:finalRes.keySet()){
            System.out.println(s+": "+finalRes.get(s));
        }

        return SerializationUtil.serialize(finalRes);
    }

    @Override
    public String getEndPoint() {
        return "/search";
    }


    List<Results> sendTasks(List<List<String>> distWork, String terms) throws KeeperException, InterruptedException {
        List<String> termsInList= TFIDF.getWordsFromLine(terms);
        List<Task> tasks=new ArrayList<>();
        for(List<String>dist:distWork){
            Task task=new Task(termsInList,dist);
            tasks.add(task);
        }

        CompletableFuture<Results>[] results=new CompletableFuture[workers.getAllAddress().size()];
        for(int i=0;i<tasks.size();i++){
            CompletableFuture<Results> result=client.sendTasks(workers.getAllAddress().get(i),tasks.get(i));
            results[i]=result;
        }

        List<Results> resultsList=new ArrayList<>();
        for(CompletableFuture<Results> result: results){
            try {
                resultsList.add(result.get());
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return resultsList;
    }


    List<List<String>> distributedWork(List<String> docs,ServiceRegistry serviceRegistry) throws InterruptedException, KeeperException {
        int numOfWorkers=serviceRegistry.getAllAddress().size();
        if(numOfWorkers==0){
            System.out.println("there is no workers available");
            return null;
        }
        int pointer=0;
        List<List<String>> disDoc=new ArrayList<>();
        for(int i=0;i<numOfWorkers;i++){
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
