package com.example.erecordsv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AvailableClass extends AppCompatActivity {
    //RecycleView
    RecyclerView recyclerView;
    AvailableClassAdapter availableClassAdapter;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseAuth fAuth;
    ArrayList<ClassModel> list;
    String userId;
    String token;
    String name;




    public static final String ID = "ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_class);

        rootNode = FirebaseDatabase.getInstance();
        recyclerView = findViewById(R.id.recycle_view_av);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();

        Intent getinfo = getIntent();
        Bundle getStr = getinfo.getExtras();
         userId = (String) getStr.get("RoomCode");
         token = (String) getStr.get("STOKEN");
         name = (String) getStr.get("SNAME");

        availableClassAdapter = new AvailableClassAdapter(this,list);
        availableClassAdapter.setHasStableIds(true);
        recyclerView.setAdapter(availableClassAdapter);

        reference = rootNode.getReference("users/"+userId).child("Subjects");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        ClassModel classModel = dataSnapshot.getValue(ClassModel.class);
                        list.add(classModel);
                    }

                   }else {
                    Toast.makeText(AvailableClass.this, "No Available Code, Ask Your Teacher", Toast.LENGTH_SHORT).show();
                }


                availableClassAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void ClickBack(View view) {
        Intent intent = new Intent(AvailableClass.this, DisplayClasses.class);
        intent.putExtra("StudentID",GlobalVar.idToPass);
        startActivity(intent);
        finish();
        //super.onBackPressed();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AvailableClass.this, DisplayClasses.class);
        intent.putExtra("StudentID",GlobalVar.idToPass);
        startActivity(intent);
        finish();
        //super.onBackPressed();
    }

}