package com.example.sparkpart.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CountryOfOriginAPIService {

    private final String endPoint = "http://192.168.68.103:8080/api/cars/";

    public String getCountryOfOrigin(String carModel) throws IOException {

        URL url = new URL(endPoint + carModel);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            String[] objects = response.toString().split("\"");
            if(objects.length > 1){
                return objects[9];
            }
            else{
                return null;
            }
        } else {
            throw new RuntimeException("Failed: " + responseCode);
        }
    }
}
