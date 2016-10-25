package org.jagsa.ekimaid;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private AutoCompleteTextView input_departure;
    private Button button_search;

    class OnFocusChange implements View.OnFocusChangeListener {
        void hideIME(View view) {
            InputMethodManager imm = (InputMethodManager) getApplicationContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);    //IM取得
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);    //IMを隠す
        }

        private void offFocus(AutoCompleteTextView view, boolean has) {
            if (!has) { // Focus out
                hideIME(view);
            } else {
                if (view.getText().length() != 0) {
                    view.showDropDown();
                }
            }
        }

        @Override
        public void onFocusChange(View view, boolean has) {
            switch (view.getId()) {
                case R.id.input_departure:
                    offFocus((AutoCompleteTextView) findViewById(view.getId()), has);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input_departure = (AutoCompleteTextView) findViewById(R.id.input_departure);
        input_departure.setOnFocusChangeListener(new OnFocusChange());
        input_departure.addTextChangedListener(
                new SuggestionTextWatcher(this, input_departure));

        button_search = (Button) findViewById(R.id.button_search);
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button_search.setEnabled(false);
                input_departure.setEnabled(false);
                new SearchMaidCafeFromStationName() {
                    @Override
                    protected void onPostExecute(JSONArray results) {
                        super.onPostExecute(results);
                        if (results != null) {
                            try {
                                LinearLayout cardHolder = (LinearLayout) findViewById(R.id.cardHolder);
                                cardHolder.removeAllViews();
                                for (int i = 0; i < results.length(); ++i) {
                                    JSONObject data = results.getJSONObject(i);
                                    String name = data.getString("name");

                                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                                    LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.card, null);
                                    CardView card = (CardView) linearLayout.findViewById(R.id.card);
                                    TextView text = (TextView) linearLayout.findViewById(R.id.cardName);
                                    ImageView img = (ImageView) linearLayout.findViewById(R.id.cardImage);
                                    text.setText(name);
                                    card.setTag(i);
                                    card.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                        }
                                    });
                                    JSONObject photo = data.getJSONArray("photos").getJSONObject(0);
                                    new GooglePlace.ImageViewLoader(img).execute(
                                            photo.getString("photo_reference"),
                                            photo.getString("width"));
                                    cardHolder.addView(linearLayout);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            button_search.setEnabled(true);
                            input_departure.setEnabled(true);
                        }
                    }
                }.execute(input_departure.getText().toString());
            }
        });
    }
}
