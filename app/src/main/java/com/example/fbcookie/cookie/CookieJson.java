package com.example.fbcookie.cookie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CookieJson {

    public static String convertCookiesToJson(String cookieString) {
        JSONArray jsonArray = new JSONArray();
        if (cookieString != null) {
            try {
                String[] cookiePairs = cookieString.split("; ");
                for (String cookiePair : cookiePairs) {
                    String[] parts = cookiePair.split("=", 2);
                    if (parts.length == 2) {
                        String cookieName = parts[0].trim();
                        String cookieValue = parts[1].trim();

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("key", cookieName);
                        jsonObject.put("value", cookieValue);
                        jsonObject.put("domain", "facebook.com"); // You might want to make this dynamic
                        jsonObject.put("path", "/");
                        jsonObject.put("hostOnly", false);
                        long creationTime = System.currentTimeMillis();
                        jsonObject.put("creation", formatDate(creationTime));
                        long lastAccessedTime = System.currentTimeMillis();
                        jsonObject.put("lastAccessed", formatDate(lastAccessedTime));
                        jsonArray.put(jsonObject);
                    }
                }
            } catch (JSONException e) { 
                e.printStackTrace();
                return ""; // Return an empty string or handle the error appropriately
            }
        }
        try {
            return jsonArray.toString(2); // This line throws JSONException
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String formatDate(long timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        return dateFormat.format(new Date(timestamp));
    }
}