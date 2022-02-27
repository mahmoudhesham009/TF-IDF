package com.mahmoudH.tfidf.networking;

import com.mahmoudH.tfidf.model.SerializationUtil;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.io.IOUtils;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Collections;

public class WebServer {
    int port;
    HttpServer server;
    OnRequestCallback requestCallback;

    public WebServer(int port, OnRequestCallback onRequestCallback) throws IOException {
        this.port = port;
        this.requestCallback = onRequestCallback;
        server = HttpServer.create(new InetSocketAddress(port), 0);
    }

    public void startServer() {
        HttpContext httpContext = server.createContext(requestCallback.getEndPoint());
        try {
            httpContext.setHandler(this::handler);
            server.start();
        }catch (Exception e){

        }

    }

    void handler(HttpExchange httpExchange){
        if (!httpExchange.getRequestMethod().equalsIgnoreCase("post")) {
            System.out.println("unacceptable request");
            httpExchange.close();
            return;
        }
        byte[] response = new byte[0];
        try {
            byte[] req=IOUtils.toByteArray(httpExchange.getRequestBody());
            if(httpExchange.getRequestURI().getQuery()!=null){
                String terms= httpExchange.getRequestURI().getQuery().split("=")[1];
                terms=terms.replace('+',' ');
                req= SerializationUtil.serialize(terms);
            }
            response = requestCallback.handleRequest(req);
            sendResponse(response,httpExchange);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void destroyServer(){
        server.stop(0);
    }

    private void sendResponse(byte[] response, HttpExchange httpExchange) throws  IOException {
        OutputStream outputStream=httpExchange.getResponseBody();
        System.out.println("sending Response");
        httpExchange.sendResponseHeaders(200, response.length);
        outputStream.write(response);
        outputStream.flush();
        outputStream.close();
    }

}
