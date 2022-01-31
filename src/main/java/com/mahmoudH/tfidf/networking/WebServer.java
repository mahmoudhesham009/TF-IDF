package com.mahmoudH.tfidf.networking;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class WebServer {
    int port;
    HttpServer server;
    OnRequestCallback requestCallback;

    public WebServer(int port, OnRequestCallback onRequestCallback) throws IOException {
        this.port = port;
        this.requestCallback = onRequestCallback;
        server = HttpServer.create(new InetSocketAddress(port), 0);
    }

    void startServer() {
        HttpContext httpContext = server.createContext(requestCallback.getEndPoint());
        httpContext.setHandler(this::handler);
    }

    void handler(HttpExchange httpExchange) throws IOException {
        if (!httpExchange.getRequestMethod().equalsIgnoreCase("post")) {
            httpExchange.close();
            return;
        }
        byte[] response =requestCallback.handleRequest(IOUtils.toByteArray(httpExchange.getRequestBody()));

        sendResponse(response,httpExchange);
    }

    private void sendResponse(byte[] response, HttpExchange httpExchange) throws  IOException {
        OutputStream outputStream=httpExchange.getResponseBody();
        outputStream.write(response);
        outputStream.flush();
        outputStream.close();
    }

}
