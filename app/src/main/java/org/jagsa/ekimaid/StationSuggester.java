package org.jagsa.ekimaid;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Arata Furukawa on 2016/10/25.
 */

public class StationSuggester extends AsyncTask<String, Void, String[]> {
    @Override
    protected String[] doInBackground(String... str) {
        try {
            URL url = Ekispert.createRequestURL("/v1/json/station/light", "&name=" + str[0]);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                //Log.d("EkiMaid", line);
                builder.append(line + "\n");
            }
            reader.close();
            JSONObject json = new JSONObject(builder.toString());

            JSONArray points = json.getJSONObject("ResultSet").getJSONArray("Point");
            if(points.length() <= 0){
                return null;
            }
            String[] suggests = new String[points.length()];
            for(int i = 0; i < points.length(); ++i){
                suggests[i] = points.getJSONObject(i).getJSONObject("Station").getString("Name");
            }
            return suggests;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
