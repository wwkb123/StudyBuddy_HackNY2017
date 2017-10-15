package lagcc.studybuddy;

/**
 * Created by yiuchungyau on 10/14/17.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ny on 6/4/2017.
 */
public class Help extends Fragment {
    public Help(){
        //
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_history);
        return inflater.inflate(R.layout.activity_help,null);
    }
}
