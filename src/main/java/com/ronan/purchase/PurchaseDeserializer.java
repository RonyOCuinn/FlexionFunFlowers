package com.ronan.purchase;

import com.google.gson.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class PurchaseDeserializer {
    private static final String PURCHASES = "purchases";

    /**
     * @param http The HTTP object with expected JSON for a purchase object.
     * @return Returns a PurchaseImpl object or null.
     */
    public PurchaseImpl deserializePurchase(HttpURLConnection http) {
        JsonElement jsonElement;
        JsonParser jsonParser = new JsonParser();
        try {
            jsonElement = jsonParser.parse(new InputStreamReader((InputStream) http.getContent()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return new Gson().fromJson(jsonElement.getAsJsonObject(), PurchaseImpl.class);
    }

    /**
     * @param http The HTTP object with expected JSON for a purchase array.
     * @return Returns a JsonArray for iteration or null.
     */
    public JsonArray deserializePurchaseArray(HttpURLConnection http) {
        JsonElement jsonElement;
        JsonParser jsonParser = new JsonParser();
        try {
            jsonElement = jsonParser.parse(new InputStreamReader((InputStream) http.getContent()));
            JsonObject root = jsonElement.getAsJsonObject();
            return root.getAsJsonArray(PURCHASES);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}