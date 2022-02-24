package com.mahmoudH.tfidf.networking;

import org.apache.zookeeper.KeeperException;

import java.io.FileNotFoundException;

public interface OnRequestCallback {

    byte[] handleRequest(byte[] request) throws FileNotFoundException, InterruptedException, KeeperException;
    String getEndPoint();


}
