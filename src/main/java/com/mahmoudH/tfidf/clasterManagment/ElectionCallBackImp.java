package com.mahmoudH.tfidf.clasterManagment;

import com.mahmoudH.tfidf.model.SearchLeader;
import com.mahmoudH.tfidf.model.SearchWorker;
import com.mahmoudH.tfidf.networking.WebServer;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ElectionCallBackImp implements onElectionCallBack {
    WorkersServiceRegistry workersServiceRegistry;
    CoordinatorServiceRegistry coordinatorServiceRegistry;
    WebServer webServer;
    int port;

    public ElectionCallBackImp(WorkersServiceRegistry workersServiceRegistry, CoordinatorServiceRegistry coordinatorServiceRegistry,int port) throws KeeperException, InterruptedException {
        this.workersServiceRegistry = workersServiceRegistry;
        this.port=port;
        this.coordinatorServiceRegistry=coordinatorServiceRegistry;
        this.coordinatorServiceRegistry.createServiceRegistry();
        this.workersServiceRegistry.createServiceRegistry();
    }

    public void onBeingLeader() throws IOException, InterruptedException, KeeperException {
        SearchLeader searchLeader=new SearchLeader(workersServiceRegistry);
        if(webServer!=null)
            webServer.destroyServer();
        webServer=new WebServer(port, searchLeader);
        webServer.startServer();
        coordinatorServiceRegistry.registerServiceToCluster("http://"+ InetAddress.getLocalHost().getCanonicalHostName()+":"+port+searchLeader.getEndPoint());
        try {
            workersServiceRegistry.unregisterService();
            workersServiceRegistry.updateAddress();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onBeingWorker() {
        try {
            SearchWorker searchWorker=new SearchWorker();
            if(webServer!=null)
                webServer.destroyServer();
            webServer=new WebServer(port,searchWorker);
            webServer.startServer();
            workersServiceRegistry.registerServiceToCluster("http://"+ InetAddress.getLocalHost().getCanonicalHostName()+":"+port+searchWorker.getEndPoint());
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
