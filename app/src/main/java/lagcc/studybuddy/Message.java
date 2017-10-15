package lagcc.studybuddy;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.IntegerRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by yiuchungyau on 10/15/17.
 */

public class Message extends Fragment {
    public Message(){
        //
    }

    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference myRef = null;
    private int total_count;
    private int count = 0;
//    private String curr_user = "Tommy Yau";
    private String curr_user = "Alex Chan";
    private boolean other_user = false;
    private boolean justIn = true;
    private ArrayList<String> listItems = new ArrayList<String>();
    private ArrayAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       Bundle bundle = getArguments();
        String name="";
        name = bundle.getString("name", name);
        final View v = inflater.inflate(R.layout.activity_message, container, false);
        final ListView msg_list = v.findViewById(R.id.msg_list);
        adapter= new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listItems);
        msg_list.setAdapter(adapter);
        TextView t1 = v.findViewById(R.id.chat_name2);
        t1.setText(name);
        myRef = database.child(name);



        myRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {

                    for (DataSnapshot msg_key : dataSnapshot.getChildren()) {
                            if(msg_key.getKey().toString().equals("1 count")){
                                total_count=Integer.parseInt(msg_key.getValue().toString());
                            }else {
                                if(listItems.size()<=total_count){
                                for (DataSnapshot msg_key_child : msg_key.getChildren()) {
                                        boolean print = true;
                                        String username="";
                                        String msg = msg_key_child.getValue().toString();
                                        for(String word:msg_key_child.getKey().split(" ")){
                                            count++;
                                            if(count==3){
                                                if (Integer.parseInt(word)<=total_count){
                                                    print = false;
                                                }
                                                count=0;
                                                break;
                                            }
                                            if(count==1 || count ==2) {
                                                username = username + " " + word;
                                            }


                                        }
                                        if(justIn){
                                            print=true;
                                        }
//                                        if(!username.trim().equals(curr_user)){
//                                            other_user=true;
//                                        }
//                                        if(other_user){
//                                            print=false;
//                                        }
                                        if(print) {

                                            listItems.add(username.trim() + "\n" + msg + "\n");
                                            adapter.notifyDataSetChanged();
                                        }

                                    }


                                }
                            }




                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

                // ...
            }
        });


        ImageButton button = v.findViewById(R.id.send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vv) {
                EditText e1 = v.findViewById(R.id.your_msg);
                String msg = "";
                TextView t2 = v.findViewById(R.id.chat_name2);

                msg = e1.getText().toString().trim();
                total_count++;
                myRef.child("chat").child(curr_user+" "+total_count).setValue(msg);
                myRef.child("1 count").setValue(total_count);
                justIn = false;
                e1.setText("");
                View view1 = getActivity().getCurrentFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
            }
        });

        return v;
    }
}