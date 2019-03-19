package com.example.cs492final;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

public class AlphaVantageUtils {

    public static final String EXTRA_ALPHA_VANTAGE_ITEM = "com.example.cs492final.Item";

    final static String ALPHA_VANTAGE_BASE_URL = "http://www.alphavantage.co/query";
    final static String ALPHA_VANTAGE_FUNCTION_PARAM = "function";
    //final static String ALPHA_VANTAGE_FUNCTION_VALUE = "TIME_SERIES_DAILY";
    final static String ALPHA_VANTAGE_FUNCTION_VALUE = "TIME_SERIES_INTRADAY";
    final static String ALPHA_VANTAGE_SYMBOL_PARAM = "symbol";
    //get the symbol from user
    final static String ALPHA_VANTAGE_INTERVAL_PARAM = "interval";
    final static String ALPHA_VANTAGE_INTERVAL_VALUE = "60min";    //maybe have as preference
    final static String ALPHA_VANTAGE_OUTPUTSIZE_PARAM = "outputsize";
    final static String ALPHA_VANTAGE_OUTPUTSIZE_VALUE = "compact";   //optional
    final static String ALPHA_VANTAGE_DATATYPE_PARAM = "datatype";
    final static String ALPHA_VANTAGE_DATATYPE_VALUE = "json";

    final static String ALPHA_VANTAGE_APIKEY_PARAM = "apikey";
    final static String ALPHA_VANTAGE_APIKEY_VALUE = "YMGJHC0E1GGT4EUB";  //get Uma's

    //https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=MSFT&interval=60min&outputsize=compact&datatype=json&apikey=YMGJHC0E1GGT4EUB

    public static class AlphaVantageRepo implements Serializable{
        @SerializedName("1. open")
        public String open;
        @SerializedName("2. high")
        public String high;
        @SerializedName("3. low")
        public String low;
        @SerializedName("4. close")
        public String close;
        @SerializedName("5. volume")
        public String volume;
    }

    public static class AlphaVantageSearchResults implements Serializable {
        @SerializedName("Time Series (60min)")
        public Map<String, AlphaVantageRepo> time_series;
    }


    public static String buildAlphaVantageURL(String query) {
        return Uri.parse(ALPHA_VANTAGE_BASE_URL).buildUpon()
                .appendQueryParameter(ALPHA_VANTAGE_FUNCTION_PARAM, ALPHA_VANTAGE_FUNCTION_VALUE)
                .appendQueryParameter(ALPHA_VANTAGE_SYMBOL_PARAM, query)
                .appendQueryParameter(ALPHA_VANTAGE_INTERVAL_PARAM, ALPHA_VANTAGE_INTERVAL_VALUE)
                .appendQueryParameter(ALPHA_VANTAGE_OUTPUTSIZE_PARAM, ALPHA_VANTAGE_OUTPUTSIZE_VALUE)
                .appendQueryParameter(ALPHA_VANTAGE_DATATYPE_PARAM, ALPHA_VANTAGE_DATATYPE_VALUE)
                .appendQueryParameter(ALPHA_VANTAGE_APIKEY_PARAM, ALPHA_VANTAGE_APIKEY_VALUE)
                .build()
                .toString();
    }

    public static Map<String, AlphaVantageRepo> parseGitHubSearchResults(String json) {
        Gson gson = new Gson();
        AlphaVantageSearchResults results = gson.fromJson(json, AlphaVantageSearchResults.class);
        if (results != null && results.time_series != null) {
            Log.d("AlphaVantageUtils","results time series: " + results.time_series);
            return results.time_series;
        } else {
            return null;
        }
    }
}
