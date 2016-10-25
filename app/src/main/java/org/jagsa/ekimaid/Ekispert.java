package org.jagsa.ekimaid;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Arata Furukawa on 2016/10/25.
 */

public class Ekispert {
    static URL createRequestURL(String endpoint, String options) throws MalformedURLException {
        return new URL("http://api.ekispert.jp" + endpoint + "?key=" + APIKEY.value + options);
    }
}
