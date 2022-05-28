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
import com.example.android.dometodoapp.AddNewTaskPending;
import com.example.android.dometodoapp.ImportantActivity;
import com.example.android.dometodoapp.Model.ToDoModel;
import com.example.android.dometodoapp.PendingActivity;
import com.example.android.dometodoapp.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.PendingViewHolder> {

    private ArrayList<ToDoModel> pendinglist;
    private PendingActivity pendingActivity;
    private FirebaseFirestore firestore;
    public PendingAdapter(PendingActivity activity,ArrayList<ToDoModel>pendinglist) {
        this.pendinglist = pendinglist;
        this.pendingActivity = activity;
    }
    @NonNull
    @Override
    public PendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(pendingActivity).inflate(R.layout.singlerow,parent,false);
        firestore = FirebaseFirestore.getInstance();
        return new PendingViewHolder(view);
    }
    public Context getContext(){
        return pendingActivity;
    }

    //changes made
    public void deleteTaskPending(int position){
        ToDoModel toDoModel = pendinglist.get(position);
        firestore.collection("Today").document(toDoModel.TaskId).delete();
        pendinglist.remove(position);
        notifyItemRemoved(position);
    }

    //changes made
    public void editTaskPending(int position){
        ToDoModel toDoModel = pendinglist.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("task",toDoModel.getTasks());
        bundle.putString("Due",toDoModel.getDue());
        bundle.putString("id",toDoModel.TaskId);

        AddNewTaskPending addNewTaskPending =new AddNewTaskPending();
        addNewTaskPending.setArguments(bundle);
        addNewTaskPending.show(pendingActivity.getSupportFragmentManager(), addNewTaskPending.getTag());
    }


    @Override
    public void onBindViewHolder(@NonNull PendingAdapter.PendingViewHolder holder, int position) {
        ToDoModel toDoModel = pendinglist.get(position);
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
        return pendinglist.size();
    }

    public class PendingViewHolder extends RecyclerView.ViewHolder{
        TextView mDueDateTv;
        CheckBox mCheckBox;
        ImageView mstar;
        public PendingViewHolder(@NonNull View itemView) {
            super(itemView);
            mDueDateTv = itemView.findViewById(R.id.due_date_tv);
            mCheckBox = itemView.findViewById(R.id.mcheckbox);
            mstar = itemView.findViewById(R.id.mark_imp);
        }
    }
}
