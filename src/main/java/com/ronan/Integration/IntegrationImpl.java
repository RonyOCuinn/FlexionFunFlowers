package com.ronan.Integration;

import com.flexionmobile.codingchallenge.integration.Integration;
import com.flexionmobile.codingchallenge.integration.Purchase;
import com.google.gson.*;
import com.ronan.Purchase.PurchaseImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class IntegrationImpl implements Integration {

    private static final String PURCHASES = "purchases";
    private final String INTEGRATION_REST_API = "http://sandbox.flexionmobile.com/javachallenge/rest";
    private JsonParser jsonParser = new JsonParser();


    @Override
    public Purchase buy(String s) {

        final String completedUrl = INTEGRATION_REST_API + "/developer/ronanocuinn/buy/" + s;
        final HttpURLConnection http = connectToApi(completedUrl, "POST");
        if (http == null) {
            return null;
        }

        PurchaseImpl purchase = deserializePurchase(http);
        if (purchase != null) {
            System.out.println("Successfully purchased id: " + purchase.getId());
        } else {
            System.out.println("Error! Failed to deserialize purchase.");
            System.out.println("URL was: " + http.getURL());
        }
        return purchase;
    }

    @Override
    public List<Purchase> getPurchases() {

        final String completedUrl = INTEGRATION_REST_API + "/developer/ronanocuinn/all";
        final HttpURLConnection http = connectToApi(completedUrl, "GET");
        if (http == null) {
            return null;
        }

        List<Purchase> purchaseList = new ArrayList<>();
        JsonArray purchases = deserializePurchaseArray(http);
        if(purchases == null){
            return null;
        }
        System.out.println("Fetched " + purchases.size() + " purchases.");

        for (JsonElement purchase : purchases) {
            PurchaseImpl deserialisedPurchase = new Gson().fromJson(purchase, PurchaseImpl.class);
            purchaseList.add(deserialisedPurchase);
        }
        return purchaseList;

    }

    @Override
    public void consume(Purchase purchase) {
        final String completedUrl = INTEGRATION_REST_API + "/developer/ronanocuinn/consume/" + purchase.getId();
        final HttpURLConnection http = connectToApi(completedUrl, "POST");
        if (http != null) {
            try {
                int responseCode = http.getResponseCode();
                if (responseCode == 200) {
                    System.out.println("Successfully consumed purchase id: " + purchase.getId());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param completedUrl Fully qualified URL to be connected to.
     * @param requestMethod Request method to use. E.G. POST.
     * @return Returns a connected HttpURLConnection object or null.
     */
    private HttpURLConnection connectToApi(String completedUrl, String requestMethod) {

        URL apiEndpoint;
        try {
            apiEndpoint = new URL(completedUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        HttpURLConnection http;
        try {
            URLConnection apiConnection;
            apiConnection = apiEndpoint.openConnection();
            http = (HttpURLConnection) apiConnection;
            http.setRequestMethod(requestMethod);
            http.connect();
            http.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return http;
    }

    /**
     *
     * @param http The HTTP object with expected JSON for a purchase object.
     * @return Returns a PurchaseImpl object or null.
     */
    private PurchaseImpl deserializePurchase(HttpURLConnection http) {

        JsonElement jsonElement;
        try {
            jsonElement = jsonParser.parse(new InputStreamReader((InputStream) http.getContent()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return new Gson().fromJson(jsonElement.getAsJsonObject(), PurchaseImpl.class);
    }

    /**
     *
     * @param http The HTTP object with expected JSON for a purchase array.
     * @return Returns a JsonArray for iteration or null.
     */
    private JsonArray deserializePurchaseArray(HttpURLConnection http){
        try {
            JsonElement jsonElement;
            jsonElement = jsonParser.parse(new InputStreamReader((InputStream) http.getContent()));
            JsonObject root = jsonElement.getAsJsonObject();
            return root.getAsJsonArray(PURCHASES);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
