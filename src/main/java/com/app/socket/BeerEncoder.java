package com.app.socket;

import com.app.model.Beer;
import com.google.gson.Gson;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class BeerEncoder implements Encoder.Text<Beer> {
    private static Gson gson = new Gson();

    @Override
    public String encode(Beer beer) throws EncodeException {
        String json = gson.toJson(beer);
        return json;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
