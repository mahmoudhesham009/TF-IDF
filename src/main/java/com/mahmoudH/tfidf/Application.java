package com.mahmoudH.tfidf;

import com.mahmoudH.tfidf.clasterManagment.ElectionCallBackImp;
import com.mahmoudH.tfidf.clasterManagment.LeaderElection;
import com.mahmoudH.tfidf.clasterManagment.ServiceRegistry;
import com.mahmoudH.tfidf.model.DocumentData;
import com.mahmoudH.tfidf.search.TFIDF;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Application implements Watcher{
    final static public String PATH = "src/main/resources/books";
    final static public String ZOOKEEPER_ADDRESS="localhost:2181";
    private static final int TIME_OUT = 5000;
    private ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        int currentServerPort=args.length==1?Integer.parseInt(args[0]):8080;
        Application application=new Application();
        application.connectZooKeeper();
        LeaderElection leaderElection=new LeaderElection(application.zooKeeper, new ElectionCallBackImp(new ServiceRegistry(application.zooKeeper),currentServerPort));
        leaderElection.leaderVlo();
        application.run();
    }

    void connectZooKeeper() throws IOException {
        zooKeeper=new ZooKeeper(ZOOKEEPER_ADDRESS,TIME_OUT,this);
    }

    void run() throws InterruptedException {
        synchronized (zooKeeper) {
            zooKeeper.wait();
        }
    }

    void close() {
        synchronized (zooKeeper) {
            zooKeeper.notify();
        }
        System.err.println("Zookeeper is closed");
    }

    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
            System.err.println("zookeeper connected");
        }
        if (watchedEvent.getState() == Watcher.Event.KeeperState.Disconnected) {
            System.err.println("zookeeper disconnected");
            close();
        }
    }

}
