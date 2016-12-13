package org.jagsa.ekimaid;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

/**
 * Created by Arata Furukawa on 2016/10/25.
 */

public class StationSuggester extends AsyncTask<String, Void, String[]> {
    @Override
    protected String[] doInBackground(String... str) {
        try {
            JSONObject json = new JSONObject(API.request(
                    Ekispert.createRequestURL("/v1/json/station/light", "&name=" + str[0])));

            JSONArray points = json.getJSONObject("ResultSet").getJSONArray("Point");
            if(points.length() <= 0){
                return null;
            }
            String[] suggests = new String[points.length()];
            for(int i = 0; i < points.length(); ++i){
                suggests[i] = points.getJSONObject(i).getJSONObject("Station").getString("Name");
            }
            return suggests;
        } catch (MalformedURLException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
