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
import com.example.android.dometodoapp.AllTasksActivity;
import com.example.android.dometodoapp.ImportantActivity;
import com.example.android.dometodoapp.Model.ToDoModel;
import com.example.android.dometodoapp.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AllTasksAdapter extends RecyclerView.Adapter<AllTasksAdapter.AllTasksViewHolder> {
    private ArrayList<ToDoModel> alltasklist;
    private AllTasksActivity allTasksActivity;
    private FirebaseFirestore firestore;
    public AllTasksAdapter(AllTasksActivity activity, ArrayList<ToDoModel> alltasklist) {
        this.alltasklist = alltasklist;
        this.allTasksActivity = activity;
    }
    public Context getContext(){
        return allTasksActivity;
    }
    @NonNull
    @Override
    public AllTasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(allTasksActivity).inflate(R.layout.singlerow,parent,false);
        firestore = FirebaseFirestore.getInstance();
        return new AllTasksViewHolder(view);
    }

    //changes made
    public void deleteTaskAllTask(int position){
        ToDoModel toDoModel = alltasklist.get(position);
        firestore.collection("Today").document(toDoModel.TaskId).delete();
        alltasklist.remove(position);
        notifyItemRemoved(position);
    }

    //chnages made
    public void editTaskAllTask(int position){
        ToDoModel toDoModel = alltasklist.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("task",toDoModel.getTasks());
        bundle.putString("Due",toDoModel.getDue());
        bundle.putString("id",toDoModel.TaskId);

        AddNewTask addNewTask =new AddNewTask();
        addNewTask.setArguments(bundle);
        addNewTask.show(allTasksActivity.getSupportFragmentManager(), addNewTask.getTag());
    }


    @Override
    public void onBindViewHolder(@NonNull  AllTasksAdapter.AllTasksViewHolder holder, int position) {
        ToDoModel toDoModel = alltasklist.get(position);
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
        return alltasklist.size();
    }

    public class AllTasksViewHolder extends RecyclerView.ViewHolder{
        TextView mDueDateTv;
        CheckBox mCheckBox;
        ImageView mstar;
        public AllTasksViewHolder(@NonNull View itemView) {
            super(itemView);
            mDueDateTv = itemView.findViewById(R.id.due_date_tv);
            mCheckBox = itemView.findViewById(R.id.mcheckbox);
            mstar = itemView.findViewById(R.id.mark_imp);
        }
    }
}
