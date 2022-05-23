package com.example.android.dometodoapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.dometodoapp.AddNewTask;
import com.example.android.dometodoapp.MainActivity;

import com.example.android.dometodoapp.Model.ToDoModel;
import com.example.android.dometodoapp.R;
import com.example.android.dometodoapp.TodayActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

// this is adapter for Today Activity

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {
    private ArrayList<ToDoModel>todaylist;
    private TodayActivity activity;
    private FirebaseFirestore firestore;

    //constructor to set data
    public ToDoAdapter(TodayActivity todayActivity,ArrayList<ToDoModel>todaylist) {
     this.activity=todayActivity;
     this.todaylist= todaylist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.singlerow,parent,false);
        firestore = FirebaseFirestore.getInstance();
        return new MyViewHolder(view);
    }

    public void deleteTask(int position){
        ToDoModel toDoModel = todaylist.get(position);
        firestore.collection("Today").document(toDoModel.TaskId).delete();
        todaylist.remove(position);
        notifyItemRemoved(position);
    }
    public Context getContext(){
        return activity;
    }

    public void editTask(int position){
        ToDoModel toDoModel = todaylist.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("task",toDoModel.getTasks());
        bundle.putString("Due",toDoModel.getDue());
        bundle.putString("id",toDoModel.TaskId);

        AddNewTask addNewTask =new AddNewTask();
        addNewTask.setArguments(bundle);
        addNewTask.show(activity.getSupportFragmentManager(), addNewTask.getTag());
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoAdapter.MyViewHolder holder, int position) {
        ToDoModel toDoModel = todaylist.get(position);
        holder.mCheckBox.setText(toDoModel.getTasks());
        holder.mDueDateTv.setText("Due On "+toDoModel.getDue());
        holder.mCheckBox.setChecked(convert(toDoModel.getStatus()));
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    // here i have to write code for removing the marked item from today activity,pending activity and important activity but not from all task activity
                    firestore.collection("Today").document(toDoModel.TaskId).update("status",1);
                }
                else{
                    //here i  have to retreive the task again to important ,pending and today
                    firestore.collection("Today").document(toDoModel.TaskId).update("status",0);
                }
            }
        });
    }
    private boolean convert(int status){
        return status!=0;
    }

    @Override
    public int getItemCount() {
        return todaylist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView mDueDateTv;
        CheckBox mCheckBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mDueDateTv = itemView.findViewById(R.id.due_date_tv);
            mCheckBox = itemView.findViewById(R.id.mcheckbox);
        }
    }
}
