package com.bcsa.planningpokeruser;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Login extends Fragment {
    private Button goButton;
    private EditText userName, groupID;
    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        goButton = v.findViewById(R.id.goButton);
        userName = v.findViewById(R.id.userName);
        groupID = v.findViewById(R.id.groupID);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        goButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String userNameString = userName.getText().toString();
                final String groupIDString = groupID.getText().toString();
                if(!TextUtils.isEmpty(userNameString) && !TextUtils.isEmpty(groupIDString)) {
                    mDatabase.child("Groups").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.hasChild(groupIDString)) {
                                Toast toast = Toast.makeText(getContext(), "Group already exists!", Toast.LENGTH_LONG);
                                toast.show();
                            } else {
                                Map<String, String> groupInfo = new HashMap<>();
                                groupInfo.put("groupID", groupIDString);
                                groupInfo.put("groupName", userNameString);
                                mDatabase.child("Groups").child(userNameString).setValue(groupInfo);

                                Toast toast = Toast.makeText(getContext(), "Group created!", Toast.LENGTH_LONG);
                                toast.show();
                                groupID.getText().clear();
                                userName.getText().clear();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                    });

                } else {
                    Toast toast = Toast.makeText(getContext(), "Fields should not be empty!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        return v;
    }
}
