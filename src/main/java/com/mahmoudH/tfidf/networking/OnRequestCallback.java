package com.mahmoudH.tfidf.networking;

import java.io.FileNotFoundException;

public interface OnRequestCallback {

    byte[] handleRequest(byte[] request) throws FileNotFoundException;
    String getEndPoint();


}
