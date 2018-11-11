package com.ronan.integration;

import com.flexionmobile.codingchallenge.integration.Integration;
import com.flexionmobile.codingchallenge.integration.Purchase;
import com.google.gson.*;
import com.ronan.connection.Connection;
import com.ronan.purchase.PurchaseDeserializer;
import com.ronan.purchase.PurchaseImpl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class IntegrationImpl implements Integration {

    private final PurchaseDeserializer purchaseDeserializer = new PurchaseDeserializer();

    @Override
    public Purchase buy(String s) {

        final Connection connection = new Connection();

        final String completedUrl = Connection.INTEGRATION_REST_API + "/developer/ronanocuinn/buy/" + s;
        final HttpURLConnection http = connection.connectToApi(completedUrl, "POST");
        if (http == null) {
            return null;
        }

        PurchaseImpl purchase = purchaseDeserializer.deserializePurchase(http);
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

        List<Purchase> purchaseList = new ArrayList<>();

        final String completedUrl = Connection.INTEGRATION_REST_API + "/developer/ronanocuinn/all";
        final Connection connection = new Connection();
        final HttpURLConnection http = connection.connectToApi(completedUrl, "GET");
        if (http == null) {
            return purchaseList;
        }

        JsonArray purchases = purchaseDeserializer.deserializePurchaseArray(http);
        if(purchases == null){
            return purchaseList;
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
        final String completedUrl = Connection.INTEGRATION_REST_API + "/developer/ronanocuinn/consume/" + purchase.getId();
        final Connection connection = new Connection();
        final HttpURLConnection http = connection.connectToApi(completedUrl, "POST");
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
}
