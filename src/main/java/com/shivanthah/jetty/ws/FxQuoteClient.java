package com.shivanthah.jetty.ws;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;
import java.util.ListIterator;

public class FxQuoteClient {
    final static Logger LOG = LoggerFactory.getLogger(FxQuoteClient.class);
    public static void main(String[] args) {
        String dest = "ws://localhost:8080/quotes";
        WebSocketClient client = new WebSocketClient();
        try {
            FxQuoteClientHandler socket = new FxQuoteClientHandler();
            client.start();
            URI echoUri = new URI(dest);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            client.connect(socket, echoUri, request);
            socket.getLatch().await();

            List<String> symbolsList = supportedSymbolsList();
            ListIterator<String> listIterator = symbolsList.listIterator();
            while(listIterator.hasNext()) {
                socket.sendMessage(listIterator.next());
                try {
                    // delay quote request by 1 sec to get a feel
                    // of input output streaming
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOG.error("Sleep interrupted, no worries", e);
                }
            }
            Thread.sleep(10000l);

        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                client.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static List<String> supportedSymbolsList() {
        return List.of(
                "USDGBP=X", "USDEUR=X", "USDAUD=X", "USDCHF=X","USDJPY=X","USDCAD=X",
                "USDSGD=X", "USDNZD=X", "USDHKD=X", "GBPUSD=X", "GBPEUR=X", "GBPAUD=X", "GBPCHF=X",
                "GBPJPY=X", "GBPCAD=X", "GBPSGD=X", "GBPNZD=X", "GBPHKD=X", "EURUSD=X", "EURGBP=X",
                "EURAUD=X", "EURCHF=X", "EURJPY=X", "EURCAD=X", "EURSGD=X", "EURNZD=X", "EURHKD=X",
                "AUDUSD=X", "AUDGBP=X", "AUDEUR=X", "AUDCHF=X", "AUDJPY=X", "AUDCAD=X", "AUDSGD=X",
                "AUDNZD=X", "AUDHKD=X", "CHFGBP=X", "CHFEUR=X", "CHFAUD=X", "CHFJPY=X", "CHFCAD=X",
                "CHFSGD=X", "CHFNZD=X", "CHFHKD=X", "JPYUSD=X", "JPYGBP=X", "JPYEUR=X", "JPYAUD=X",
                "JPYCHF=X", "JPYCAD=X", "JPYSGD=X", "JPYNZD=X", "JPYHKD=X", "CADUSD=X", "CADGBP=X",
                "CADEUR=X", "CADAUD=X", "CADCHF=X", "CADJPY=X", "CADSGD=X", "CADNZD=X", "CADHKD=X",
                "SGDUSD=X", "SGDGBP=X", "SGDEUR=X", "SGDAUD=X", "SGDCHF=X", "SGDJPY=X", "SGDCAD=X",
                "SGDNZD=X", "SGDHKD=X", "NZDUSD=X", "NZDGBP=X", "NZDEUR=X", "NZDAUD=X", "NZDCHF=X",
                "NZDJPY=X", "NZDCAD=X", "NZDSGD=X", "NZDHKD=X", "HKDUSD=X", "HKDGBP=X", "HKDEUR=X",
                "HKDAUD=X", "HKDCHF=X", "HKDJPY=X", "HKDCAD=X", "HKDSGD=X", "HKDNZD=X"
        );
    }
}
