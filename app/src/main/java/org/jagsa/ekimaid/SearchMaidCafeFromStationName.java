package org.jagsa.ekimaid;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Arata Furukawa on 2016/10/26.
 */

public class SearchMaidCafeFromStationName extends AsyncTask<String, Void, JSONArray> {
    @Override
    protected JSONArray doInBackground(String... strings) {
        try {
            JSONObject station = API.request(
                    Ekispert.createRequestURL("/v1/json/station", "&name=" + strings[0]));
            JSONObject results = station.getJSONObject("ResultSet");
            JSONArray points = results.optJSONArray("Point");
            JSONObject geo;
            if (points != null) {
                geo = points.getJSONObject(0).getJSONObject("GeoPoint");
            } else {
                geo = results.getJSONObject("Point").getJSONObject("GeoPoint");
            }
            String geo_point = geo.getString("lati_d") + "," + geo.getString("longi_d");
            Log.d("EkiMaid", geo_point);

            JSONObject maid = API.request(
                    GooglePlace.createRequestURL("/nearbysearch/json",
                            "&keyword=橙幻郷+or+メイド喫茶&location=" + geo_point
                                    + "&radius=1000&rankby=prominence"));

            Log.d("EkiMaid", "" + maid.toString());

            return maid.getJSONArray("results");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONArray results) {
        super.onPostExecute(results);
    }
}