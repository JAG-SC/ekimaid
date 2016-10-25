package org.jagsa.ekimaid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ornew on 2016/10/26.
 */

public class API {
    static JSONObject request(URL url) throws IOException, JSONException {
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
        return new JSONObject(builder.toString());
    }
    static Bitmap loadBitmap(URL url) throws IOException, JSONException {
        InputStream in = url.openStream();
        Bitmap out = BitmapFactory.decodeStream(in);
        in.close();
        return out;
    }
}
