package com.example.android.dometodoapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class AddNewTaskAllTask extends BottomSheetDialogFragment {
    private static final String TAG = "AddNewTaskAllTask";
    private AllTasksActivity allTasksActivity;
    private EditText mTaskEdit; //task_edit_today

    private TextView mVoiceType,mSave,msetDue,mCancel; //voice_type_today, save_task_today ,set_due_today, cancel_task_today

    private FirebaseFirestore firebaseFirestore;

    private Context context;
    private String dueDate="";
    private String dueDateUpdate="";
    private String id="";
    public static AddNewTaskAllTask newInstance(){
        return new AddNewTaskAllTask();
    }

    @Nullable

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_task_alltask,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mTaskEdit = (EditText)view.findViewById(R.id.task_edit_alltask);
        mVoiceType=(TextView)view.findViewById(R.id.voice_type_alltask);
        mSave=(TextView)view.findViewById(R.id.save_task_alltask);
        msetDue=(TextView)view.findViewById(R.id.set_due_alltask);
        mCancel=(TextView)view.findViewById(R.id.cancel_task_alltask);
        firebaseFirestore = FirebaseFirestore.getInstance();

        boolean isUpdate =false;
        final Bundle bundle =getArguments();
        if(bundle!=null){
            isUpdate = true;
            String task = bundle.getString("task");
            id =bundle.getString("id");
            dueDateUpdate = bundle.getString("due");
            mTaskEdit.setText(task);
            msetDue.setText(dueDateUpdate);
            if(task.length()>0){
                mSave.setEnabled(false);
                mSave.setBackgroundColor(Color.GRAY);
            }
        }

        mTaskEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    mSave.setEnabled(false);
                    mSave.setBackgroundColor(Color.GRAY);
                }
                else{
                    mSave.setEnabled(true);
                    mSave.setBackgroundColor(Color.GREEN);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        msetDue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int MONTH=calendar.get(Calendar.MONTH);
                int YEAR =calendar.get(Calendar.YEAR);
                int DAY = calendar.get(Calendar.DATE);
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month +1;
                        msetDue.setText(dayOfMonth+"/"+month+"/"+year);
                        dueDate =dayOfMonth+"/"+month+"/"+year;
                    }
                },YEAR,MONTH,DAY);
                datePickerDialog.show();
            }
        });

        //voice type
        mVoiceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak to text");
                try{
                    startActivityForResult(intent,1);
                }
                catch (ActivityNotFoundException e){
                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

        boolean finalIsUpdate = isUpdate;
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String task = mTaskEdit.getText().toString();

                if(finalIsUpdate){
                    firebaseFirestore.collection("Today").document(id).update("tasks",task,"due",dueDate);
                    Toast.makeText(context,"Task Updated",Toast.LENGTH_LONG).show();
                }
                else {
                    if (task.isEmpty()) {
                        Toast.makeText(context, "Empty task not Allowed!!", Toast.LENGTH_SHORT).show();
                    } else {

                        Map<String, Object> taskMap = new HashMap<>();
                        taskMap.put("tasks", task);
                        taskMap.put("due", dueDate);
                        taskMap.put("status", 0);
                        taskMap.put("star_status",R.drawable.ic_baseline_star_border_24);
                        taskMap.put("time", FieldValue.serverTimestamp());
                        // it is for table of today activity
                        firebaseFirestore.collection("Today").add(taskMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Task Saved", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });

                    }
                }


                dismiss();
            }
        });

        // super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity =getActivity();
        if(activity instanceof OnDialogCloseListener){
            ((OnDialogCloseListener)activity).onDialogClose(dialog);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK&&data!=null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mTaskEdit.setText(result.get(0));
                }
                break;
        }
    }
}
