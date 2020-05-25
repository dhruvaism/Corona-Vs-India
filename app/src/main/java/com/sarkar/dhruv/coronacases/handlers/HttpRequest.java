package com.sarkar.dhruv.coronacases.handlers;


import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static com.sarkar.dhruv.coronacases.constants.Constant.STATEWISE;
import static com.sarkar.dhruv.coronacases.constants.Constant.STATEWISE_DATA;

public class HttpRequest {

    private static final String TAG = HttpRequest.class.getSimpleName();
    private static HttpRequest httpRequest = null;

    public static HttpRequest getInstance(){
        if (httpRequest == null){
            httpRequest = new HttpRequest();
        }

        return httpRequest;
    }


    private String execute(String urlStr){

        final String REQUEST_METHOD = "GET";
        final int READ_TIMEOUT = 30000;
        final int CONNECTION_TIMEOUT = 30000;

        String result = "";
        String inputLine;

        HttpURLConnection connection = null;

        try {
            URL url = new URL(urlStr);

            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod(REQUEST_METHOD);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);

            connection.connect();

            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();

            while ((inputLine = reader.readLine()) != null){
                stringBuilder.append(inputLine);
            }

            reader.close();
            streamReader.close();

            result = stringBuilder.toString();

        } catch (MalformedURLException e) {

            Log.e(TAG, "MalformedURLException: " + e.getMessage());

        } catch (ProtocolException e) {

            Log.e(TAG, "ProtocolException: " + e.getMessage());

        } catch (IOException e) {

            Log.e(TAG, "IOException: " + e.getMessage());

        } catch (Exception e) {

            Log.e(TAG, "Exception: " + e.getMessage());

        }finally {

            if (connection != null){
                connection.disconnect();
            }

        }

        return result;
    }

    private JSONArray convertToJSONArray(String str){
        JSONArray jsonArray = null;
        if(!str.isEmpty()){
            try {
                jsonArray = new JSONArray(str);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }

    private JSONObject convertToJSONObject(String str)
    {
        JSONObject jsonObject = null;
        if(!str.isEmpty())
        {
            try {
                jsonObject = new JSONObject(str);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return jsonObject;
    }
    public JSONObject getStateWise() {
        String result = execute(STATEWISE_DATA);
        System.out.println(result);
        return convertToJSONObject(result);
    }






}
