package com.mahmoudH.tfidf.clasterManagment;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class LeaderElection implements Watcher {
    ZooKeeper zooKeeper;
    String ELECTION_NAMESPACE = "/election";
    String zNodeName = "";

    onElectionCallBack electionCallBack;



    public LeaderElection(ZooKeeper zooKeeper, onElectionCallBack electionCallBack) {
        this.zooKeeper = zooKeeper;
        this.electionCallBack = electionCallBack;
        try {
            if(zooKeeper.exists("/election",false)==null){
                zooKeeper.create("/election",new byte[]{},ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void leaderVlo() throws KeeperException, InterruptedException {
        String znodePrefix = ELECTION_NAMESPACE + "/c_";
        String znodeFullPath = zooKeeper.create(znodePrefix, new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.err.println(znodeFullPath);
        zNodeName = znodeFullPath.replace(ELECTION_NAMESPACE + "/", "");
    }

    public void electTheLeader() throws KeeperException, InterruptedException, IOException {
        Stat stat = null;
        String preZNode;
        List<String> allZNodes;
        while (stat == null) {
            allZNodes = zooKeeper.getChildren(ELECTION_NAMESPACE, false);
            Collections.sort(allZNodes);
            if (allZNodes.get(0).equals(zNodeName)) {
                System.err.println("this is the leader");
                electionCallBack.onBeingLeader();
                return;
            }
            else {
                System.err.println("this is not the leader the leader is " + allZNodes.get(0));
                preZNode=allZNodes.get(Collections.binarySearch(allZNodes, zNodeName) - 1);
                stat = zooKeeper.exists(ELECTION_NAMESPACE + "/" + preZNode, this);
            }
            System.out.println("I am watching "+ preZNode);
            electionCallBack.onBeingWorker();
        }

    }

    public void process(WatchedEvent watchedEvent) {
        switch (watchedEvent.getType()) {
            case NodeDeleted:
                try {
                    electTheLeader();
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
