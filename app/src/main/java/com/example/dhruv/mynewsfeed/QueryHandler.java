package com.example.dhruv.mynewsfeed;

import android.net.Uri;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by dhruv on 11/8/2017.
 */
// class to handle the query, parse the json and return the list
public class QueryHandler {
    //To build the url using the various query parameters
    private static URL createUrl(String sec) {
        String u = "https://content.guardianapis.com/search?";
        Uri base = Uri.parse(u);
        Uri.Builder builder = base.buildUpon();
        builder.appendQueryParameter("order-by", "newest");
        builder.appendQueryParameter("show-references", "author");
        builder.appendQueryParameter("show-tags", "contributor");
        builder.appendQueryParameter("q", sec);
        builder.appendQueryParameter("api-key", "7b3160e6-b154-4d65-8061-36f32b896b62");
        String finalUrl = builder.toString();
        URL url = null;
        try {
            url = new URL(finalUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        Log.i("url", String.valueOf(url));

        return url;
    }

    //Formatting the date to our required style from the one retrieved from json
    private static String dateFormat(String preDate) {
        String datePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern, Locale.getDefault());
        try {
            Date newDate = simpleDateFormat.parse(preDate);
            String finalPattern = "MMM d, yyy";
            SimpleDateFormat finalDateFormatter = new SimpleDateFormat(finalPattern, Locale.getDefault());
            return finalDateFormatter.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String makeHttpRequest(URL url) throws IOException {
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        String json = "";
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                json = readFromStream(inputStream);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return json;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream == null) {
            return null;
        }
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }

    //function to extract the required features from the json
    private static List<News> extractFromJson(String json) {
        List<News> list = new ArrayList<>();
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            JSONArray results = new JSONArray();
            JSONObject base = new JSONObject(json);
            JSONObject responseObject = base.getJSONObject("response");
            results = responseObject.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject currentObject = results.getJSONObject(i);
                String webTitle = currentObject.getString("webTitle");
                String sectionName = currentObject.getString("sectionName");
                String date = currentObject.getString("webPublicationDate");
                if (date != null) {
                    date = dateFormat(date);
                }
                String webUrl = currentObject.getString("webUrl");
                JSONArray tagsArray = currentObject.getJSONArray("tags");
                String author = "";
                if (tagsArray.length() == 0) {
                    author = null;
                } else {
                    for (int j = 0; j < tagsArray.length(); j++) {
                        JSONObject object = tagsArray.getJSONObject(j);
                        author += object.getString("webTitle") + ". ";
                    }
                }
                list.add(new News(webTitle, author, webUrl, sectionName, date));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    //function to fetch the news and return the list using all of the above functions
    static List<News> fetchNews(String sec) {
        URL url = createUrl(sec);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return extractFromJson(jsonResponse);
    }
}

