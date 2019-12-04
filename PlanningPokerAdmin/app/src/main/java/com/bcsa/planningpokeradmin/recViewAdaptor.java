package com.bcsa.planningpokeradmin;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class recViewAdaptor extends RecyclerView.Adapter<recViewAdaptor.ViewHolder> {

    private static final String TAG ="RecyclerViewAdapter";

    List<classQuestion> Question;
    Context mContext;
    private com.google.firebase.database.DatabaseReference mDatabase;
    private String active_group;

    public recViewAdaptor(Context mContext, List<classQuestion>Question)
    {
        this.Question = Question;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public recViewAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_listquestions, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final recViewAdaptor.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: Called");

        holder.switchState.setChecked(Question.get(position).getState());

        mDatabase = FirebaseDatabase.getInstance().getReference();

        holder.switchState.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                        if(isChecked)
                        {
                            Question.get(position).setState(true);
                            Question.get(position).setGroup(active_group);
                            Log.d("uzenet", "onCheckedChanged: ");
                            mDatabase.child("Questions").child(Question.get(position).getQuestion()).setValue(Question.get(position));

                        }
                        else {
                            Question.get(position).setState(false);
                            mDatabase.child("Questions").child(Question.get(position).getQuestion()).setValue(Question.get(position));
                        }
                    }
                });

        holder.switchState.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    holder.switchState.getParent().requestDisallowInterceptTouchEvent(true);
                }
                // always return false since we don't care about handling tapping, flinging, etc.
                return false;
            }
        });

        holder.Questions.setText(Question.get(position).getQuestion());
    }

    @Override
    public int getItemCount() {
        return Question.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView Questions;
        private Switch switchState;
        private EditText activeGroup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Questions = itemView.findViewById(R.id.qID);
            switchState = itemView.findViewById(R.id.activeSwitch);
            activeGroup = itemView.findViewById(R.id.activeGroup);

            activeGroup.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    active_group = activeGroup.getText().toString();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }
}
