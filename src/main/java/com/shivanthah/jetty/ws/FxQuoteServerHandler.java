package com.shivanthah.jetty.ws;

import java.io.IOException;
import java.math.BigDecimal;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yahoofinance.YahooFinance;
import yahoofinance.quotes.fx.FxQuote;


@WebSocket
public class FxQuoteServerHandler {
    final static Logger LOG = LoggerFactory.getLogger(FxQuoteServer.class);

    @OnWebSocketMessage
    public void onMessage(Session session, String quoteSymbol) throws IOException {
        LOG.info("Message received:" + quoteSymbol);
        if (session.isOpen()) {
            BigDecimal quotePrice = getPrice(quoteSymbol);
            session.getRemote().sendString(String.format("{symbol : %s,price : %s }",quoteSymbol,quotePrice.toPlainString()));
            LOG.info("response sent");
        }
    }

    @OnWebSocketConnect
    public void onConnect(Session session) throws IOException {
        LOG.info(session.getRemoteAddress().getHostString() + " connected!");
    }

    @OnWebSocketClose
    public void onClose(Session session, int status, String reason) {
        LOG.info(session.getRemoteAddress().getHostString() + " closed!");
    }


    final static BigDecimal getPrice(String quoteSymbol) {
        try {
            FxQuote fx = YahooFinance.getFx(quoteSymbol);
            LOG.info("FxQuote received {}", fx.getPrice());
            return fx.getPrice();
        } catch (IOException e) {
            LOG.error("error getting quote price", e);
        }
        return BigDecimal.ZERO;
    }

}
