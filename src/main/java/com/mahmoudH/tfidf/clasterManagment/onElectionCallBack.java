package com.mahmoudH.tfidf.clasterManagment;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;

public interface onElectionCallBack {
    void onBeingLeader() throws IOException, InterruptedException, KeeperException;
    void onBeingWorker();
}
