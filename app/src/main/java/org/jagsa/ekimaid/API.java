package org.jagsa.ekimaid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
    static String request(URL url) {
        BufferedReader reader = null;
        StringBuilder response = new StringBuilder();
        try {
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                //Log.d("EkiMaid", line);
                response.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
    static Bitmap loadBitmap(URL url) {
        InputStream in = null;
        try {
            in = url.openStream();
            Bitmap out = BitmapFactory.decodeStream(in);
            return out;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
