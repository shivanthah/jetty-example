package com.shivanthah.jetty.ws;


import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebSocket
public class FxQuoteClientHandler {
    final static Logger LOG = LoggerFactory.getLogger(FxQuoteClientHandler.class);
    private Session session;

    CountDownLatch latch = new CountDownLatch(1);

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        LOG.info("Message received from server:" + message);
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        LOG.info("Connected to server");
        this.session = session;
        latch.countDown();
    }

    public void sendMessage(String str) {
        try {
            session.getRemote().sendString(str);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
