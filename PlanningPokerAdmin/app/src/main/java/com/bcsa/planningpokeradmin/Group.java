package com.bcsa.planningpokeradmin;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Group extends Fragment {
    private Button createButton, addQBtn;
    private EditText groupName, groupID, questionText;
    private DatabaseReference mDatabase;
    private DatabaseReference questionRef;
    private RecyclerView recyclerView;
    private ArrayList<Group> myDataset;
    private ArrayList<classQuestion> questionList;
    private classQuestion question;
    private classQuestion activeQuestion;
    private recViewAdaptor recyclerViewAdaptor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_group, container, false);

        createButton = v.findViewById(R.id.createButton);
        addQBtn = v.findViewById(R.id.addQBtn);
        groupName = v.findViewById(R.id.nameText);
        groupID = v.findViewById(R.id.idText);
        questionText = v.findViewById(R.id.questionText);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerView = v.findViewById(R.id.recView);

        questionList = new ArrayList<classQuestion>();

        mDatabase.child("Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questionList.clear();
                for(DataSnapshot iterator : dataSnapshot.getChildren())
                {
                    classQuestion q = iterator.getValue(classQuestion.class);
                    questionList.add(q);

                    if(q.getState())
                    {
                        mDatabase.child("Groups").child(q.getGroup()).child("question").setValue(q);
                        //refreshGroup();
                    }
                }
                recyclerViewAdaptor = new recViewAdaptor(getContext(),questionList);
                recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
                recyclerView.setAdapter(recyclerViewAdaptor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String groupNameString = groupName.getText().toString();
                final String groupIDString = groupID.getText().toString();
                if(!TextUtils.isEmpty(groupNameString) && !TextUtils.isEmpty(groupIDString)) {
                    mDatabase.child("Groups").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.hasChild(groupNameString)) {
                                Toast toast = Toast.makeText(getContext(), "Group already exists!", Toast.LENGTH_LONG);
                                toast.show();
                            } else {
                                Map<String, String> groupInfo = new HashMap<>();
                                groupInfo.put("groupID", groupIDString);
                                groupInfo.put("groupName", groupNameString);
                                mDatabase.child("Groups").child(groupNameString).setValue(groupInfo);

                                Toast toast = Toast.makeText(getContext(), "Group created!", Toast.LENGTH_LONG);
                                toast.show();
                                groupID.getText().clear();
                                groupName.getText().clear();
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

        addQBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String questionTextString = questionText.getText().toString();
                if(!TextUtils.isEmpty(questionTextString)) {
                    mDatabase.child("Questions").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.hasChild(questionTextString)) {
                                Toast toast = Toast.makeText(getContext(), "Question already exists!", Toast.LENGTH_LONG);
                                toast.show();
                            } else {
                                Map<String, String> groupInfo = new HashMap<>();
                                groupInfo.put("questionText", questionTextString);
                                mDatabase.child("Questions").child(questionTextString).setValue(groupInfo);

                                Toast toast = Toast.makeText(getContext(), "Question created!", Toast.LENGTH_LONG);
                                toast.show();
                                questionText.getText().clear();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                    });

                } else {
                    Toast toast = Toast.makeText(getContext(), "Field should not be empty!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        return v;
    }
}
