package com.example.android.dometodoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.android.dometodoapp.Adapter.ToDoAdapter;
import com.example.android.dometodoapp.Model.ToDoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
public class TodayActivity extends AppCompatActivity implements OnDialogCloseListener{
private RecyclerView rcvToday;// id = recv_today
private FloatingActionButton fabToday;//id = fab_today
    private FirebaseFirestore firestore;
    private ToDoAdapter adapter;
    private ArrayList<ToDoModel>list;
    private Query query;
    private ListenerRegistration listenerRegistration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);
      //  setting the custom toolbar
        Toolbar tb = (Toolbar)findViewById(R.id.toolbar);
        tb.setTitle("Today");

        setSupportActionBar(tb);
        //setting the back button in custom toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrowback);

        // from here work of recycler view starts
        rcvToday = (RecyclerView)findViewById(R.id.recv_today);
        fabToday =(FloatingActionButton)findViewById(R.id.fab_today);
        rcvToday.setHasFixedSize(true);
        rcvToday.setLayoutManager(new LinearLayoutManager(TodayActivity.this));
        fabToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);
            }
        });


        firestore = FirebaseFirestore.getInstance();
        list = new ArrayList<>();
        adapter = new ToDoAdapter(TodayActivity.this,list);

        // swipe decorator
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(rcvToday);


       retrieveData();
        rcvToday.setAdapter(adapter);
    }
    private void retrieveData(){
        query =firestore.collection("Today").orderBy("time", Query.Direction.DESCENDING);
               listenerRegistration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
             for(DocumentChange documentChange:value.getDocumentChanges()){
                 if(documentChange.getType()==DocumentChange.Type.ADDED){
                     String id = documentChange.getDocument().getId();
                     ToDoModel toDoModel = documentChange.getDocument().toObject(ToDoModel.class).withId(id);
                     list.add(toDoModel);
                     adapter.notifyDataSetChanged();
                 }
             }
             listenerRegistration.remove();

            }
        });
    }
    public Context getContext(){
        return TodayActivity.this;
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
     list.clear();
        retrieveData();
        adapter.notifyDataSetChanged();
    }

}