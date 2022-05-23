package com.example.android.dometodoapp.Model;

public class ToDoModel extends TaskId {
   private String tasks,due;
    private int status;
public ToDoModel(){

}
    public ToDoModel(String tasks, String due, int status) {
        this.tasks = tasks;
        this.due = due;
        this.status = status;
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
