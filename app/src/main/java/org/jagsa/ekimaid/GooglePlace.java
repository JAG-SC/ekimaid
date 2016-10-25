package org.jagsa.ekimaid;

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
}
