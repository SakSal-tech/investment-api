
//Reasons putting marketdata package in asset management:
//The API fetches market/asset data, so it's logically part of asset management, not clients or risk.
//Keeps  RiskService clean as it only calculates risks using data from assets, without worrying about API calls.
// Easy to extend later with multiple APIs (AlphaVantageClient, MarketstackClient, etc.).
package com.sakhiya.investment.portfoliomanagement.asset.marketdata;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Collections;

/* Tutorials:
https://fasterxml.github.io/jackson-core/javadoc/2.15/com/fasterxml/jackson/core/io/JsonEOFException.html
Jackson documentation: https://github.com/FasterXML/jackson
Tutorials: Search "Jackson JSON Java tutorial" for guides on using Jackson for JSON processing.
Baeldung Jackson guide: https://www.baeldung.com/jackson
Java Brains video: "Jackson JSON Processor - Java Brains" (YouTube) */

// Used to parse and create JSON objects from API responses
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// Exception thrown when the end of a JSON input is reached unexpectedly (Jackson library)
import com.fasterxml.jackson.core.io.JsonEOFException;
// Exception thrown for errors in JSON parsing or formatting (org.json library)
import org.json.JSONException;

/*Class Responsibilities:
- Make HTTP requests to the API
- Parse JSON into a usable format (e.g., List<Double> for historical prices)
- Return the data to be stored in an Asset objec
*/
//Component is a Spring annotation that marks a class as a Spring-managed bean.
//This means Spring will automatically create an instance of the class and manage its lifecycle, allowing me to use features like dependency injection
//Because of want Spring to inject values or dependencies (with @Autowired), the class must be a Spring bean
@Component
public class AlphaVantageClient {
    //  inject the value of the property named alphaVantage.apiKey from local.properties
    @Value("${alphaVantage.apiKey}")
    private  String apiKey;

    public AlphaVantageClient(){}

    /* Fetches daily historical prices for a given asset symbol from Alpha Vantage
     *  @param symbol The stock/asset symbol (e.g., "IBM")
     * @return List of closing prices in chronological order (oldest to newest)

     */
    public List<Double> getDailyPrices(String symbol) throws IOException, InterruptedException, JsonEOFException, JSONException{
       // price list is used to collect all the closing prices in chronological order.
        List<Double> price = new ArrayList<>();

        //TIME_SERIES_DAILY = daily historical stock prices
        //outputsize=compact tells Alpha Vantage how much data I want. compact → ~100 most recent data points (default)
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol="
                     + symbol + "&outputsize=compact&apikey=" + apiKey;

        // Building API request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest  request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        //the response returned by the server (Alpha Vantage). 
        //The <String> means the body of the response will be treated as a plain text String.
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        // Parse JSON response

        JSONObject json = new JSONObject(response.body());
        JSONObject timeSeriesDaily = json.getJSONObject("Time Series (Daily)");

        // Create a Map to store closing prices keyed by date.
        // Key: LocalDate (the trading date), Value: Double (closing price)
        Map<LocalDate, Double> closingPrices = new HashMap<>();

        // Get an iterator over all the keys in the "Time Series (Daily)" JSON object.
        // Each key is a date string like "2025-09-03", "2025-09-02", etc.
        Iterator<String> dates = timeSeriesDaily.keys();

        // Loop through all the dates in the JSON object
        while (dates.hasNext()){
            // Get the next date string from the iterator
           String dateStr = dates.next();

           // Get the nested JSON object for this date, which contains
           // "1. open", "2. high", "3. low", "4. close", "5. volume"
           JSONObject dailyData = timeSeriesDaily.getJSONObject(dateStr);

            // Extract the closing price as a String from the JSON
            String closingStr = dailyData.getString("4. close"); //"4. close" is the closing price of that day

            // Convert the closing price string to a double
            double closingPrice = Double.parseDouble(closingStr);

            // Why I converted the date from String. LocalDate objects can be sorted naturally.
            //If I leave it as a string, sorting might not behave as expected for other formats.
            //can easily compute differences between dates, add/subtract days, or check ranges
            closingPrices.put(LocalDate.parse(dateStr), closingPrice);

        }  
        // Collect prices in chronological order (oldest to newest)
        //closingPrices.keySet() returns a Set<LocalDate>—all the dates (keys) in the map.
        List<LocalDate> sortedDates = new ArrayList<>(closingPrices.keySet());
        //sorts the dates in chronological order
        Collections.sort(sortedDates);
        for (LocalDate date : sortedDates) {
            price.add(closingPrices.get(date));
        }
        return price;
    }


        /**
     * Fetches daily historical prices for a given asset symbol from Alpha Vantage, returning a list of AlphaVantagePriceDTO (date and price).
     * @param symbol The stock/asset symbol (e.g., "IBM")
     * @return List of AlphaVantagePriceDTO in chronological order (oldest to newest)
     */
    /**
     * Fetches daily historical prices for a given asset symbol from Alpha Vantage, returning a list of AlphaVantagePriceDTO (date and price).
     *
     * @param symbol The stock/asset symbol (e.g., "IBM")
     * @return List of AlphaVantagePriceDTO in chronological order (oldest to newest)
     * @throws IOException If there is a network or IO error
     * @throws InterruptedException If the HTTP request is interrupted
     * @throws JsonEOFException If the JSON is incomplete
     * @throws JSONException If the JSON cannot be parsed
     *
     * Steps:
     * 1. Build the Alpha Vantage API URL for daily prices.
     * 2. Make an HTTP GET request to fetch the JSON response.
     * 3. Parse the JSON to extract the "Time Series (Daily)" object.
     * 4. For each date, extract the closing price and store it in a map keyed by LocalDate.
     * 5. Sort the dates in chronological order.
     * 6. For each date, create an AlphaVantagePriceDTO with the date and closing price.
     * 7. Return the list of DTOs (oldest to newest).
     */
    public List<AlphaVantagePriceDTO> getDailyPriceDTOs(String symbol) throws IOException, InterruptedException, JsonEOFException, JSONException {
        // List to hold the result DTOs
        List<AlphaVantagePriceDTO> dtos = new ArrayList<>();

        // 1. Build the API URL for the given symbol
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol="
                + symbol + "&outputsize=compact&apikey=" + apiKey;

        // 2. Make the HTTP GET request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // 3. Parse the JSON response
        JSONObject json = new JSONObject(response.body());
        JSONObject timeSeriesDaily = json.getJSONObject("Time Series (Daily)");

        // 4. Extract closing prices for each date
        // 4. Extract closing prices for each date in the JSON response
        Map<LocalDate, Double> closingPrices = new HashMap<>();
        Iterator<String> dates = timeSeriesDaily.keys();
        while (dates.hasNext()) {
            // Get the next date string (e.g., "2025-09-03")
            String dateStr = dates.next();
            // Get the nested JSON object for this date (contains open, high, low, close, volume)
            JSONObject dailyData = timeSeriesDaily.getJSONObject(dateStr);
            // Extract the closing price as a string (e.g., "144.23")
            String closingStr = dailyData.getString("4. close");
            // Convert the closing price string to a double value
            double closingPrice = Double.parseDouble(closingStr);
            // Convert the date string to LocalDate and store the closing price in the map
            closingPrices.put(LocalDate.parse(dateStr), closingPrice);
        }

        // 5. Sort the dates in chronological order (oldest to newest)
        List<LocalDate> sortedDates = new ArrayList<>(closingPrices.keySet());
        Collections.sort(sortedDates);

        // 6. For each sorted date, create a DTO with the date and its closing price
        for (LocalDate date : sortedDates) {
            dtos.add(new AlphaVantagePriceDTO(date, closingPrices.get(date)));
        }

        // 7. Return the list of DTOs (each contains date and closing price)
        return dtos;
    }

}
