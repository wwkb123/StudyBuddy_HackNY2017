package lagcc.studybuddy;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by yiuchungyau on 10/14/17.
 */

public class Home extends Fragment {
    public Home(){
        //
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_history);
        final View v = inflater.inflate(R.layout.activity_home, container, false);
//        ImageButton button = v.findViewById(R.id.photo_btn);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View vv) {
//
//            }
//        });
        return v;
    }
}