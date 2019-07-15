package com.app.socket;

import com.app.model.Beer;
import com.google.gson.Gson;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class BeerDecoder implements Decoder.Text<Beer> {
    private static Gson gson = new Gson();


    @Override
    public Beer decode(String s) throws DecodeException {
        Beer beer = gson.fromJson(s, Beer.class);
        return beer;
    }

    @Override
    public boolean willDecode(String s) {
        return false;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
