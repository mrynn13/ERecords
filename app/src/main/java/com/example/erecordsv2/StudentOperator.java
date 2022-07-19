package com.example.erecordsv2;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class StudentOperator{
    private DatabaseReference studentDbReference;
    String userId;
    FirebaseAuth fAuth;



    public StudentOperator(){
        StudentModel sTM = new StudentModel();
        String key = sTM.getsClass();

        FirebaseDatabase sdb = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        studentDbReference = sdb.getReference("users/"+userId+"/"+"C77");
    }


    public Task<Void> add(StudentModel studentModel){
        return studentDbReference.child(studentModel.getsId()).setValue(studentModel);
    }

    public Task<Void> update(String key, HashMap<String,Object> hashmap){
        return studentDbReference.child(key).updateChildren(hashmap);
    }
    public Task<Void> remove(String key){
        return studentDbReference.child(key).removeValue();
    }

}
