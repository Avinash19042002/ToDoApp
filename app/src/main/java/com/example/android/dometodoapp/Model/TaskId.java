package com.example.android.dometodoapp.Model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

import javax.annotation.Nullable;

public class TaskId {
    @Exclude
    public String TaskId;
    public <T extends TaskId>T withId(@Nullable final String id){
        this.TaskId=id;
        return (T) this;
    }
}