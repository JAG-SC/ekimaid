package org.jagsa.ekimaid;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.widget.ImageView;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ornew on 2016/10/26.
 */

public class GooglePlace {
    static URL createRequestURL(String endpoint, String options) throws MalformedURLException {
        return new URL("https://maps.googleapis.com/maps/api/place" + endpoint
                + "?key=" + APIKEY.google_place + options);
    }
    static public class ImageLoader extends AsyncTask<String, Void, Bitmap> {
        // 第一引数: 画像ID
        // 第二引数: 画像幅
        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                return API.loadBitmap(
                        GooglePlace.createRequestURL("/photo", "&maxwidth=" + params[1]
                                + "&photoreference=" + params[0]));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    static public class ImageViewLoader extends ImageLoader {
        private ImageView target;
        ImageViewLoader(ImageView target){
            this.target = target;
        }
        @Override
        protected void onPostExecute(Bitmap img) {
            super.onPostExecute(img);
            target.setImageBitmap(img);
        }
    }
}
