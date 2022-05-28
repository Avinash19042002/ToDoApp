package com.example.android.dometodoapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.dometodoapp.AddNewTask;
import com.example.android.dometodoapp.AddNewTaskImportant;
import com.example.android.dometodoapp.ImportantActivity;
import com.example.android.dometodoapp.Model.ToDoModel;
import com.example.android.dometodoapp.R;
import com.example.android.dometodoapp.TodayActivity;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;

public class ImportantAdapter extends RecyclerView.Adapter<ImportantAdapter.ImportantViewHolder> {
    private ArrayList<ToDoModel>importantlist;
    private ImportantActivity importantactivity;
    private FirebaseFirestore firestore;
    public ImportantAdapter(ImportantActivity activity,ArrayList<ToDoModel> importantlist) {
        this.importantlist = importantlist;
        this.importantactivity = activity;
    }

    @NonNull
    @Override
    public ImportantViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(importantactivity).inflate(R.layout.singlerow,parent,false);
        firestore = FirebaseFirestore.getInstance();
        return new ImportantViewHolder(view);
    }
    public Context getContext(){
        return importantactivity;
    }
    //changes made
    public void deleteTaskImportant(int position){
        ToDoModel toDoModel = importantlist.get(position);
        firestore.collection("Today").document(toDoModel.TaskId).delete();
        importantlist.remove(position);
        notifyItemRemoved(position);
    }

    //changes made
    public void editTaskImportant(int position){
        ToDoModel toDoModel = importantlist.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("task",toDoModel.getTasks());
        bundle.putString("Due",toDoModel.getDue());
        bundle.putString("id",toDoModel.TaskId);

        AddNewTaskImportant addNewTaskImportant =new AddNewTaskImportant();
        addNewTaskImportant.setArguments(bundle);
        addNewTaskImportant.show(importantactivity.getSupportFragmentManager(), addNewTaskImportant.getTag());
    }


    @Override
    public void onBindViewHolder(@NonNull ImportantAdapter.ImportantViewHolder holder, int position) {
        ToDoModel toDoModel = importantlist.get(position);
        holder.mCheckBox.setText(toDoModel.getTasks());
        holder.mDueDateTv.setText("Due On "+toDoModel.getDue());
        holder.mCheckBox.setChecked(convert(toDoModel.getStatus()));
        holder.mstar.setImageResource(R.drawable.ic_baseline_star_border_24);
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


        holder.mstar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state = toDoModel.getStatus_star();
                if(state ==R.drawable.ic_baseline_star_border_24){
                    holder.mstar.setImageResource(R.drawable.ic_baseline_star_24);
                    toDoModel.setStatus_star(R.drawable.ic_baseline_star_24);

                    firestore.collection("Today").document(toDoModel.TaskId).update("star_status",R.drawable.ic_baseline_star_24);
                }
                else if(state ==R.drawable.ic_baseline_star_24){
                    toDoModel.setStatus_star(R.drawable.ic_baseline_star_border_24);
                    holder.mstar.setImageResource(R.drawable.ic_baseline_star_border_24);
                    firestore.collection("Today").document(toDoModel.TaskId).update("star_status",R.drawable.ic_baseline_star_border_24);

                }
            }
        });

    }
    private boolean convert(int status){
        return status!=0;
    }

    @Override
    public int getItemCount() {
        return importantlist.size();
    }

    public class ImportantViewHolder extends RecyclerView.ViewHolder{
        TextView mDueDateTv;
        CheckBox mCheckBox;
        ImageView mstar;
        public ImportantViewHolder(@NonNull  View itemView) {
            super(itemView);
            mDueDateTv = itemView.findViewById(R.id.due_date_tv);
            mCheckBox = itemView.findViewById(R.id.mcheckbox);
            mstar = itemView.findViewById(R.id.mark_imp);
        }
    }
}
