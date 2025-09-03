//Reasons putting marketdata package in asset management:
//The API fetches market/asset data, so it's logically part of asset management, not clients or risk.
//Keeps  RiskService clean as it only calculates risks using data from assets, without worrying about API calls.
// Easy to extend later if you want multiple APIs (AlphaVantageClient, MarketstackClient, etc.).
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
// Exception thrown when the end of a JSON input is reached unexpectedly (Jackson library)
import com.fasterxml.jackson.core.io.JsonEOFException;
// Exception thrown for errors in JSON parsing or formatting (org.json library)
import org.json.JSONException;

/*Class Responsibilities:
- Make HTTP requests to the API
- Parse JSON into a usable format (e.g., List<Double> for historical prices)
- Return the data to be stored in an Asset objec
*/
public class AlphaVantageClient {

    private final String apiKey;

    public AlphaVantageClient(String apiKey){
        this.apiKey = apiKey;
    }

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


}
