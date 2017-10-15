package lagcc.studybuddy;

import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by yiuchungyau on 10/14/17.
 */

public class SearchResult extends Fragment {
    public SearchResult(){
        //

    }


    private static final String TAG ="" ;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference myRef = null;
    private ArrayList<String> listItems = new ArrayList<String>();
    private ArrayAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        Bundle bundle = getArguments();
        String search_keyword="";
        search_keyword = bundle.getString("search_keyword", search_keyword);



        myRef = database.child(search_keyword);
        final View v = inflater.inflate(R.layout.activity_search_result, container, false);
        final ListView l1 = v.findViewById(R.id.list1);
        TextView title = v.findViewById(R.id.title);
        title.setText("Your Study Buddy for \""+search_keyword+"\" :");
        adapter= new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listItems);
        l1.setAdapter(adapter);
        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                Toast.makeText(getActivity(), "You Clicked at " +listItems.get(+ position), Toast.LENGTH_SHORT).show();
                String user_id = listItems.get(+ position);
                System.out.println(user_id);
                int count = 0;
                String user_name ="";
                for(String word:user_id.split("\\s")){
                    count++;
                    user_name=user_name+" "+word;
                    if(count>=3){
                        break;
                    }
                }
                user_name = user_name.trim();

                Bundle bundle = new Bundle();
                bundle.putString("user", user_name);

                Fragment fragment=new Profile();
                fragment.setArguments(bundle);
                FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainFrame,fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        myRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        if(listItems.size()<3) {
                            String info = user.child("info").getValue().toString();
                            String miles = user.child("miles").getValue().toString();
                            String online = user.child("online").getValue().toString();
                            listItems.add("\n" + user.getKey() + "\t\t\t               " + miles + " miles away" + "\n" + info);
                            adapter.notifyDataSetChanged();
                        }

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });


//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_history);
        return v;
    }
}