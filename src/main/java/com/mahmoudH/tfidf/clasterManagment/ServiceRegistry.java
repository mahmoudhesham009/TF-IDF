package com.mahmoudH.tfidf.clasterManagment;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.List;

public class ServiceRegistry implements Watcher {
    public static final String REGISTRY_PATH = "/service_registry";
    ZooKeeper zooKeeper;
    List<String> address;
    String currentNode;


    public ServiceRegistry(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public void createServiceRegistry() throws KeeperException, InterruptedException {
        if (zooKeeper.exists(REGISTRY_PATH, false) == null)
            zooKeeper.create(REGISTRY_PATH, new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    public void registerServiceToCluster(String address) throws KeeperException, InterruptedException {
        String fullPath = REGISTRY_PATH + "/n_";
        currentNode = zooKeeper.create(fullPath, address.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("node: " + currentNode.replace(REGISTRY_PATH + "/", "") + " regist successfully ");
    }

    public void updateAddress() throws KeeperException, InterruptedException {
        List<String> children = zooKeeper.getChildren(REGISTRY_PATH, this);
        address=new ArrayList<>();
        for (String s : children) {
            Stat stat = zooKeeper.exists(REGISTRY_PATH+"/"+s, false);
            byte[] data=zooKeeper.getData(REGISTRY_PATH+"/"+s, false, stat);
            if (data!=null)
                address.add(new String(data));
        }
    }

    void unregisterService() throws KeeperException, InterruptedException {
        if (currentNode != null&&zooKeeper.exists(currentNode, false) != null )
            zooKeeper.delete(currentNode, -1);
    }

    public List<String> getAllAddress() throws KeeperException, InterruptedException {
        if (address == null)
            updateAddress();
        return address;
    }


    public void process(WatchedEvent watchedEvent) {
        switch (watchedEvent.getType()) {
            case NodeChildrenChanged:
                try {
                    updateAddress();
                    System.out.println("all service:");
                    getAllAddress().forEach(i -> System.out.println(i.toString()));
                    System.out.println();
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
    }
}
