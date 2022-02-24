package com.mahmoudH.tfidf.clasterManagment;

import java.io.IOException;

public interface onElectionCallBack {
    void onBeingLeader() throws IOException;
    void onBeingWorker();
}
