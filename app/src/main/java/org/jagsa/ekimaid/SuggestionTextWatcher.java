package org.jagsa.ekimaid;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

/**
 * Created by Arata Furukawa on 2016/10/25.
 */

public class SuggestionTextWatcher implements TextWatcher {
    Context context;
    AutoCompleteTextView target;
    SuggestionTextWatcher(Context context, AutoCompleteTextView target){
        this.context = context;
        this.target = target;
    }
    StationSuggester suggester = null;
    private void complete(String str){
        if(str.length() > 0) {
            if(suggester != null){
                switch (suggester.getStatus()){
                    case FINISHED:
                        suggester = null;
                        break;
                    case PENDING:
                        suggester.cancel(true);
                        break;
                    case RUNNING:
                        suggester.cancel(true);
                        break;
                }
            }
            suggester = new StationSuggester(){
                @Override
                protected void onPostExecute(String[] suggests) {
                    super.onPostExecute(suggests);
                    if(suggests != null) {
                        ArrayAdapter<String> adapter =
                                new ArrayAdapter<>(context, R.layout.suggests, suggests);
                        target.setAdapter(adapter);
                        target.showDropDown();
                    }
                }
            };
            suggester.execute(str);
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        complete(s.toString());
    }
    @Override
    public void afterTextChanged(Editable s) {
    }
}