package com.app.socket;

import com.app.model.Beer;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/beer/{username}", decoders = BeerDecoder.class, encoders = BeerEncoder.class)
public class BeerPriceEndpoint {
    private Session session;
    private static Set<BeerPriceEndpoint> beerPriceEndpoint
            = new CopyOnWriteArraySet<>();
    private static HashMap<String, String> users = new HashMap<>();


    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {
        this.session = session;
        beerPriceEndpoint.add(this);
        users.put(session.getId(), username);

    }

    @OnMessage
    public void onMessage(Session session, Beer beer) throws IOException, EncodeException {
        beer.setPrice(beer.getPrice() + 1);
        broadcast(beer);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        // WebSocket connection closes
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }

    private static void broadcast(Beer beer) throws IOException, EncodeException {
        beerPriceEndpoint.forEach(endpoint -> {
                    synchronized (endpoint) {
                        try {
                            endpoint.session.getBasicRemote()
                                    .sendObject(beer);
                        } catch (IOException | EncodeException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }
}
