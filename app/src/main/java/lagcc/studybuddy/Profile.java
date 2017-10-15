package lagcc.studybuddy;

import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by yiuchungyau on 10/14/17.
 */

public class Profile extends Fragment {
    public Profile() {
        //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        String user_name = "";
        user_name = bundle.getString("user", user_name);
        final View v = inflater.inflate(R.layout.activity_profile, container, false);

        TextView t1 = v.findViewById(R.id.name);
        t1.setText(user_name);

        ImageButton button = v.findViewById(R.id.msg_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vv) {
                TextView t2 = v.findViewById(R.id.name);
                String name = t2.getText().toString().trim();
                Bundle bundle = new Bundle();
                bundle.putString("name", name);

                Fragment fragment = new Message();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainFrame, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return v;
    }
}