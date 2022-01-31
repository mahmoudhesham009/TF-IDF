package com.mahmoudH.tfidf.clasterManagment;

import com.mahmoudH.tfidf.model.SearchWorker;
import com.mahmoudH.tfidf.networking.WebServer;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ElectionCallBackImp implements onElectionCallBack {
    ServiceRegistry serviceRegistry;
    int port;

    public ElectionCallBackImp(ServiceRegistry serviceRegistry, int port) throws KeeperException, InterruptedException {
        this.serviceRegistry=serviceRegistry;
        this.port=port;
        serviceRegistry.createServiceRegistry();
    }

    public void onBeingLeader() {
        try {
            serviceRegistry.unregisterService();
            serviceRegistry.updateAddress();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onBeingWorker() {
        try {
            SearchWorker searchWorker=new SearchWorker();
            WebServer webServer=new WebServer(port,searchWorker);
            serviceRegistry.registerServiceToCluster("http://"+ InetAddress.getLocalHost().getCanonicalHostName()+"/"+port+"/"+searchWorker.getEndPoint());
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
