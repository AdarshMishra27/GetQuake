package com.amworks.getquake;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class Utils {

    private static final String LOG_TAG="LOG_TAG";

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createURL(String url)
    {
        URL url1=null;
        try {
            url1 = new URL(url);
        }catch (MalformedURLException e)
        {
            Log.e(LOG_TAG,"Problem building the URL ", e);
        }
        return url1;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException{

        String jsonResponse="";

        if(url==null)
            return jsonResponse;

        HttpURLConnection httpURLConnection=null;
        InputStream inputStream=null;

        try{

            httpURLConnection=(HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if(httpURLConnection.getResponseCode()==200)
            {
                inputStream=httpURLConnection.getInputStream();
                jsonResponse=readFromsStream(inputStream);
            }else
            {
                Log.e(LOG_TAG, "Error response code: " + httpURLConnection.getResponseCode());
            }

        }catch (IOException e)
        {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        }finally {
            if(httpURLConnection!=null){
                httpURLConnection.disconnect();
            }

            if(inputStream!=null){
                inputStream.close();
            }
        }

        return jsonResponse;

    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromsStream(InputStream inputStream) throws IOException{

        StringBuilder output=new StringBuilder();
        if(inputStream!=null){
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            String line=bufferedReader.readLine();
            while (line!=null){
                output.append(line);
                line=bufferedReader.readLine();
            }

        }

        return output.toString();

    }

    /**
     * Return a list of { Earthquake} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<EarthquakeData> extractFeatureFromJSON(String earthquakeJSON)
    {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }

        List<EarthquakeData> earthquakes=new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try
        {
            JSONObject baseResponse=new JSONObject(earthquakeJSON);
            JSONArray root=baseResponse.getJSONArray("features");
            for(int i=0;i<root.length();i++) {
                JSONObject root1 = root.getJSONObject(i);
                JSONObject root2 = root1.getJSONObject("properties");
                float mag=(float)root2.getDouble("mag");
                String place= (String) root2.get("place");
                String [] parts2=place.split(",");
                String toBeDisplayed="";
                for(String x:parts2)
                    toBeDisplayed+=x+"\n";

                long timeInMilliseconds=root2.getLong("time");
                //converting UNIX time to readable format
                Date dateObject=new Date(timeInMilliseconds);
                SimpleDateFormat simpleDateFormater=new SimpleDateFormat("MMM DD, YYYY");
                String dateToDisplay=simpleDateFormater.format(dateObject);
                SimpleDateFormat simpleTimeFormater=new SimpleDateFormat("h:mm a");
                String timeToDisplay=simpleTimeFormater.format(dateObject);
                String toBeDisplayedInView=dateToDisplay+"\n"+timeToDisplay;

                EarthquakeData obj=new EarthquakeData(mag,toBeDisplayed,toBeDisplayedInView);
                earthquakes.add(obj);
            }
        }catch (JSONException e){
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }

        return earthquakes;
    }

    /**
     * Query the USGS dataset and return a list of {Earthquake} objects.
     */
    public static List<EarthquakeData> fetchData(String requestURL){

        // Create URL object
        URL url=createURL(requestURL);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse=null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<EarthquakeData> earthquakes = extractFeatureFromJSON(jsonResponse);

        // Return the list of {@link Earthquake}s
        return earthquakes;

    }

}
