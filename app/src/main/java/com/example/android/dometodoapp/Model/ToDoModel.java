package com.example.android.dometodoapp.Model;

import com.example.android.dometodoapp.R;

public class ToDoModel extends TaskId {
   private String tasks,due;
    private int status;
    private int status_star= R.drawable.ic_baseline_star_border_24;


    public int getStatus_star() {
        return status_star;
    }

    public void setStatus_star(int status_star) {
        this.status_star = status_star;
    }

    public String getTasks() {
        return tasks;
    }

    public String getDue() {
        return due;
    }

    public int getStatus() {
        return status;
    }
}
