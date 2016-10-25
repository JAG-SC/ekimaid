package org.jagsa.ekimaid;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;

public class MainActivity extends AppCompatActivity {

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

        AutoCompleteTextView input_departure =
                (AutoCompleteTextView)findViewById(R.id.input_departure);
        input_departure.setOnFocusChangeListener(new OnFocusChange());
        input_departure.addTextChangedListener(
                new SuggestionTextWatcher(this, (AutoCompleteTextView)findViewById(R.id.input_departure)));
    }
}
