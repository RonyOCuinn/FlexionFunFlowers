package com.ronan.connection;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Connection {
    public static final String INTEGRATION_REST_API = "http://sandbox.flexionmobile.com/javachallenge/rest";

    /**
     * @param completedUrl  Fully qualified URL to be connected to.
     * @param requestMethod Request method to use. E.G. POST.
     * @return Returns a connected HttpURLConnection object or null.
     */
    public HttpURLConnection connectToApi(String completedUrl, String requestMethod) {

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
}