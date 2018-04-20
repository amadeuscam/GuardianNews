package com.example.lucian.guardiannews;

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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }


    /*CALL ALL THE FUNCTIONS OF THIS FILE MAKE REQUEST */
    public static List<Guardian> fetchNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;


        try {
            jsonResponse = makehttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error in making http request", e);
        }
        List<Guardian> result = extractNews(jsonResponse);
        return result;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error in Creating URL", e);
        }
        return url;
    }

    /*CONNECT WITH THE API AND GET THE RESPONSE*/
    private static String makehttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error in connection!! Bad Response ");
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the guardian news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;

    }

    /*Convert JSON TO STRING */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /*EXTRACT JSON FROM URL*/
    private static List<Guardian> extractNews(String newsJSON) {
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }
        ArrayList<Guardian> guardianNews = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONObject responseObj = baseJsonResponse.getJSONObject("response");
            JSONArray results = responseObj.getJSONArray("results");
            JSONObject currentAuthor = null;
            for (int i = 0; i < results.length(); i++) {
                JSONObject currentNews = results.getJSONObject(i);
                JSONArray currentTags = currentNews.getJSONArray("tags");


                for (int j = 0; j < currentTags.length(); j++) {
                     currentAuthor = currentTags.getJSONObject(j);

                }

                /* POPULATE THE GUARDIAN OBJECT AND ADD TO ARRAYLIST*/
                String title = currentNews.getString("webTitle");
                String publicationDate = currentNews.getString("webPublicationDate");
                String url = currentNews.getString("webUrl");
                String sectionName = currentNews.getString("sectionName");
                String author = null;
                if (currentAuthor != null) {
                    author = currentAuthor.getString("webTitle");
                }
                Guardian gNews = new Guardian(publicationDate, title, url, sectionName, author);

                guardianNews.add(gNews);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error in fetching data", e);
        }

        return guardianNews;
    }

}
