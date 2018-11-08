package com.ronan;

import com.flexionmobile.codingchallenge.integration.Integration;
import com.flexionmobile.codingchallenge.integration.Purchase;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.HttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IntegrationImpl implements Integration {

    private final String INTEGRATION_REST_API = "http://sandbox.flexionmobile.com/javachallenge/rest";
    URLConnection apiConnection;
    JsonParser jsonParser = new JsonParser();

    HttpClient httpClient;
    private HttpURLConnection http;

    public IntegrationImpl() {
    }


    @Override
    public Purchase buy(String s) {

        String completedUrl = INTEGRATION_REST_API + "/developer/ronanocuinn/buy/" + s;
        URL buyUrl;
        JsonElement jsonElement;
        PurchaseImpl purchase = new PurchaseImpl();

        try {
            buyUrl = new URL(completedUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        try {
            apiConnection = buyUrl.openConnection();
            http = (HttpURLConnection) apiConnection;
            http.setRequestMethod("POST");
            http.connect();
            jsonElement = jsonParser.parse(new InputStreamReader((InputStream) apiConnection.getContent()));
            JsonObject root = jsonElement.getAsJsonObject();
            purchase.setId(root.get("id").getAsString());
            purchase.setConsumed(root.get("consumed").getAsBoolean());
            purchase.setItemId(root.get("itemId").getAsString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return purchase;
    }

    @Override
    public List<Purchase> getPurchases() {
        String completedUrl = INTEGRATION_REST_API + "/developer/ronanocuinn/all";
        URL getAllUrl;
        JsonElement jsonElement;
        PurchaseImpl newPurchase;

        try {
            getAllUrl = new URL(completedUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        List<Purchase> purchaseList = new ArrayList<Purchase>();

        try {
            apiConnection = getAllUrl.openConnection();
            http = (HttpURLConnection) apiConnection;
            http.setRequestMethod("GET");
            http.connect();
            jsonElement = jsonParser.parse(new InputStreamReader((InputStream) apiConnection.getContent()));
            JsonObject root = jsonElement.getAsJsonObject();
            JsonArray purchases = root.getAsJsonArray("purchases");

            for (JsonElement purchase : purchases){
                JsonObject purchaseElement = purchase.getAsJsonObject();
                String id = purchaseElement.get("id").getAsString();
                String itemId = purchaseElement.get("itemId").getAsString();
                boolean consumed =  purchaseElement.get("consumed").getAsBoolean();
                purchaseList.add(new PurchaseImpl(id, itemId, consumed));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return purchaseList;
    }

    @Override
    public void consume(Purchase purchase) {
        String completedUrl = INTEGRATION_REST_API + "/developer/ronanocuinn/consume/" + purchase.getId();
        URL consumeUrl = null;

        try {
            consumeUrl = new URL(completedUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            apiConnection = consumeUrl.openConnection();
            http = (HttpURLConnection) apiConnection;
            http.setRequestMethod("POST");
            http.connect();
            Map map = http.getHeaderFields();
            System.out.println(map);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
