package org.jagsa.ekimaid;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private AutoCompleteTextView input_departure;
    private Button button_search;
    class OnFocusChange implements View.OnFocusChangeListener {
        void hideIME(View view){
            InputMethodManager imm = (InputMethodManager)getApplicationContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);	//IM取得
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);	//IMを隠す
        }
        private void offFocus(AutoCompleteTextView view, boolean has){
            if(!has){ // Focus out
                hideIME(view);
            } else {
                if(view.getText().length() != 0) {
                    view.showDropDown();
                }
            }
        }
        @Override
        public void onFocusChange(View view, boolean has) {
            switch (view.getId()){
                case R.id.input_departure:
                    offFocus((AutoCompleteTextView)findViewById(view.getId()), has);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input_departure = (AutoCompleteTextView)findViewById(R.id.input_departure);
        input_departure.setOnFocusChangeListener(new OnFocusChange());
        input_departure.addTextChangedListener(
                new SuggestionTextWatcher(this, input_departure));

        button_search = (Button) findViewById(R.id.button_search);
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button_search.setEnabled(false);
                input_departure.setEnabled(false);
                new AsyncTask<String, Void, String>(){
                    @Override
                    protected String doInBackground(String... strings) {
                        try {
                            StringBuilder builder = new StringBuilder();
                            JSONObject station = API.request(
                                    Ekispert.createRequestURL("/v1/json/station", "&name=" + strings[0]));
                            JSONObject results = station.getJSONObject("ResultSet");
                            JSONArray points = results.optJSONArray("Point");
                            JSONObject geo;
                            if(points != null){
                                geo = points.getJSONObject(0).getJSONObject("GeoPoint");
                            } else {
                                geo = results.getJSONObject("Point").getJSONObject("GeoPoint");
                            }
                            String geo_point = geo.getString("lati_d") + "," + geo.getString("longi_d");
                            builder.append(geo_point + '\n');
                            Log.d("EkiMaid", geo_point);

                            JSONObject maid = API.request(
                                    GooglePlace.createRequestURL("/nearbysearch/json",
                                            "&keyword=橙幻郷+or+メイド喫茶&location=" + geo_point
                                                    + "&radius=1000&rankby=prominence"));

                            Log.d("EkiMaid", "" + maid.toString());

                            JSONArray list = maid.getJSONArray("results");
                            for(int i = 0; i < list.length(); ++i){
                                String name = list.getJSONObject(i).getString("name");
                                builder.append(name + '\n');
                            }
                            return builder.toString();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                    @Override
                    protected void onPostExecute(String results) {
                        super.onPostExecute(results);
                        TextView text = (TextView) findViewById(R.id.results);
                        text.setText(results);
                        button_search.setEnabled(true);
                        input_departure.setEnabled(true);
                    }
                }.execute(input_departure.getText().toString());
            }
        });
    }
}
