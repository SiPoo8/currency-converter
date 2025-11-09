package com.converter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class CurrencyService {
    private final Map<String, Double> rates = new HashMap<>();
    private final String apiKey = "1668f2f7cf13a498a46167bb";
    private final HttpClient client = HttpClient.newHttpClient();

    public CurrencyService() {
        fetchRates("USD");
    }

    public void fetchRates(String baseCurrency) {
        String apiUrl = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + baseCurrency;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject json = new Gson().fromJson(response.body(), JsonObject.class);
            JsonObject ratesObj = json.getAsJsonObject("conversion_rates");

            rates.clear();
            for (String currency : ratesObj.keySet()) {
                rates.put(currency, ratesObj.get(currency).getAsDouble());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error fetching rates: " + e.getMessage());
        }
    }

    public double getExchangeRate(String from, String to) {
        if (!rates.containsKey(from)) {
            fetchRates(from);
        }
        double fromRate = rates.getOrDefault(from, 1.0);
        double toRate = rates.getOrDefault(to, 1.0);
        return toRate / fromRate;
    }

    public List<String> getAvailableCurrencies() {
        return new ArrayList<>(rates.keySet());
    }
}