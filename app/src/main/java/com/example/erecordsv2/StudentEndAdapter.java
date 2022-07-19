package com.example.erecordsv2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class StudentEndAdapter  extends RecyclerView.Adapter<StudentEndAdapter.MyViewHolder> {
    Context context;
    ArrayList<SubjectModel> list;
    ArrayList<String> act_done_prelim;
    ArrayList<String> act_done_midterm;
    ArrayList<String> act_done_finals;


    FirebaseDatabase rootNode;
    DatabaseReference studentRef,adminRef,prelimReference,midtermReference,finalsReference;

    Dialog dialog;

    String userId;

    final static String SID = GlobalVar.idToPass;


    public StudentEndAdapter(Context context, ArrayList<SubjectModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_code,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SubjectModel subjectModel = list.get(position);
        act_done_prelim = new ArrayList<String>();
        act_done_midterm = new ArrayList<String>();
        act_done_finals = new ArrayList<String>();

        rootNode = FirebaseDatabase.getInstance();

        holder.codeBtn.setText(subjectModel.getClassCode());
        holder.ClassNameTv.setText(subjectModel.getClassName());



        holder.codeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Onclick From Display
                //access token first
                studentRef = rootNode.getReference("studentInfo/"+SID+"/Subjects");
                studentRef.child(subjectModel.getClassCode().toUpperCase()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult().exists()){
                                DataSnapshot mySnapshot = task.getResult();
                                String mClass = String.valueOf(mySnapshot.child("className").getValue());
                                String mCode = String.valueOf(mySnapshot.child("classCode").getValue());

                                String userIdT = String.valueOf(mySnapshot.child("instructor").getValue());
                                Toast.makeText(context, mClass, Toast.LENGTH_SHORT).show();
                                GlobalVar.instructPass = userIdT;

                                GoToAdminRef(userIdT,mCode);

                            }
                        }
                    }
                });





            }

            private void GoToAdminRef(String userIdT,String mCode) {
                adminRef = rootNode.getReference("users/"+userIdT+"/Students/"+mCode.toUpperCase()+"/"+SID);
                adminRef.child("sToken").setValue(String.valueOf(GlobalVar.tokenToPass));
                sendONPopUp(userIdT,mCode);

            }

            private void sendONPopUp(String userIdT, String mCode) {

                dialog = new Dialog(context);

                dialog.setContentView(R.layout.popup_grading_term);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);


                EditText p_a1,p_a2,p_a3,p_a4,p_a5,p_ass1,p_ass2,p_ass3,p_ass4,p_ass5,p_q1,p_q2,p_q3,p_q4,p_q5,p_ex1,p_ex2,p_ex3,p_ex4,p_ex5;
                EditText m_a1,m_a2,m_a3,m_a4,m_a5,m_ass1,m_ass2,m_ass3,m_ass4,m_ass5,m_q1,m_q2,m_q3,m_q4,m_q5,m_ex1,m_ex2,m_ex3,m_ex4,m_ex5;
                EditText f_a1,f_a2,f_a3,f_a4,f_a5,f_ass1,f_ass2,f_ass3,f_ass4,f_ass5,f_q1,f_q2,f_q3,f_q4,f_q5,f_ex1,f_ex2,f_ex3,f_ex4,f_ex5;



                Button prelimSaveBtn,midtermSaveBtn,finalSaveBtn,prelimBtn,midtermBtn,finalBtn;
                ImageView closePopUpBtn;
                LinearLayout pLayout,mLayout,fLayout;



                prelimSaveBtn = dialog.findViewById(R.id.prelim_save_btn);
                midtermSaveBtn = dialog.findViewById(R.id.midterm_save_btn);
                finalSaveBtn = dialog.findViewById(R.id.final_save_btn);
                prelimBtn = dialog.findViewById(R.id.prelimBtn);
                midtermBtn = dialog.findViewById(R.id.midtermBtn);
                finalBtn = dialog.findViewById(R.id.finalBtn);
                closePopUpBtn = dialog.findViewById(R.id.closeBtn);

                pLayout = dialog.findViewById(R.id.prelimSheet);
                mLayout = dialog.findViewById(R.id.midtermSheet);
                fLayout = dialog.findViewById(R.id.finalSheet);
                TextView tv = dialog.findViewById(R.id.adtv);
 //hide button
                prelimSaveBtn.setVisibility(View.GONE);
                midtermSaveBtn.setVisibility(View.GONE);
                finalSaveBtn.setVisibility(View.GONE);

                tv.setText("View Score");




//Prelim, dami hahahah
                p_a1 = dialog.findViewById(R.id.p_act1);
                p_a2 = dialog.findViewById(R.id.p_act2);
                p_a3 = dialog.findViewById(R.id.p_act3);
                p_a4 = dialog.findViewById(R.id.p_act4);
                p_a5 = dialog.findViewById(R.id.p_act5);
                p_ass1 = dialog.findViewById(R.id.p_ass1);
                p_ass2 = dialog.findViewById(R.id.p_ass2);
                p_ass3 = dialog.findViewById(R.id.p_ass3);
                p_ass4 = dialog.findViewById(R.id.p_ass4);
                p_ass5 = dialog.findViewById(R.id.p_ass5);
                p_q1 = dialog.findViewById(R.id.p_q1);
                p_q2 = dialog.findViewById(R.id.p_q2);
                p_q3 = dialog.findViewById(R.id.p_q3);
                p_q4 = dialog.findViewById(R.id.p_q4);
                p_q5 = dialog.findViewById(R.id.p_q5);
                p_ex1 = dialog.findViewById(R.id.p_el1);
                p_ex2 = dialog.findViewById(R.id.p_el2);
                p_ex3 = dialog.findViewById(R.id.p_el3);
                p_ex4 = dialog.findViewById(R.id.p_el4);
                p_ex5 = dialog.findViewById(R.id.p_el5);
//disable
                p_a1.setEnabled(false);
                p_a2.setEnabled(false);
                p_a3.setEnabled(false);
                p_a4.setEnabled(false);
                p_a5.setEnabled(false);
                p_ass1.setEnabled(false);
                p_ass2.setEnabled(false);
                p_ass3.setEnabled(false);
                p_ass4.setEnabled(false);
                p_ass5.setEnabled(false);
                p_q1.setEnabled(false);
                p_q2.setEnabled(false);
                p_q3.setEnabled(false);
                p_q4.setEnabled(false);
                p_q5.setEnabled(false);
                p_ex1.setEnabled(false);
                p_ex2.setEnabled(false);
                p_ex3.setEnabled(false);
                p_ex4.setEnabled(false);
                p_ex5.setEnabled(false);
                p_a1.setFocusable(false);
                p_a2.setFocusable(false);
                p_a3.setFocusable(false);
                p_a4.setFocusable(false);
                p_a5.setFocusable(false);
                p_ass1.setFocusable(false);
                p_ass2.setFocusable(false);
                p_ass3.setFocusable(false);
                p_ass4.setFocusable(false);
                p_ass5.setFocusable(false);
                p_q1.setFocusable(false);
                p_q2.setFocusable(false);
                p_q3.setFocusable(false);
                p_q4.setFocusable(false);
                p_q5.setFocusable(false);
                p_ex1.setFocusable(false);
                p_ex2.setFocusable(false);
                p_ex3.setFocusable(false);
                p_ex4.setFocusable(false);
                p_ex5.setFocusable(false);



//Midterm, dami hahahah
                m_a1 = dialog.findViewById(R.id.m_act1);
                m_a2 = dialog.findViewById(R.id.m_act2);
                m_a3 = dialog.findViewById(R.id.m_act3);
                m_a4 = dialog.findViewById(R.id.m_act4);
                m_a5 = dialog.findViewById(R.id.m_act5);
                m_ass1 = dialog.findViewById(R.id.m_ass1);
                m_ass2 = dialog.findViewById(R.id.m_ass2);
                m_ass3 = dialog.findViewById(R.id.m_ass3);
                m_ass4 = dialog.findViewById(R.id.m_ass4);
                m_ass5 = dialog.findViewById(R.id.m_ass5);
                m_q1 = dialog.findViewById(R.id.m_q1);
                m_q2 = dialog.findViewById(R.id.m_q2);
                m_q3 = dialog.findViewById(R.id.m_q3);
                m_q4 = dialog.findViewById(R.id.m_q4);
                m_q5 = dialog.findViewById(R.id.m_q5);
                m_ex1 = dialog.findViewById(R.id.m_el1);
                m_ex2 = dialog.findViewById(R.id.m_el2);
                m_ex3 = dialog.findViewById(R.id.m_el3);
                m_ex4 = dialog.findViewById(R.id.m_el4);
                m_ex5 = dialog.findViewById(R.id.m_el5);
 //disable
                m_a1.setEnabled(false);
                m_a2.setEnabled(false);
                m_a3.setEnabled(false);
                m_a4.setEnabled(false);
                m_a5.setEnabled(false);
                m_ass1.setEnabled(false);
                m_ass2.setEnabled(false);
                m_ass3.setEnabled(false);
                m_ass4.setEnabled(false);
                m_ass5.setEnabled(false);
                m_q1.setEnabled(false);
                m_q2.setEnabled(false);
                m_q3.setEnabled(false);
                m_q4.setEnabled(false);
                m_q5.setEnabled(false);
                m_ex1.setEnabled(false);
                m_ex2.setEnabled(false);
                m_ex3.setEnabled(false);
                m_ex4.setEnabled(false);
                m_ex5.setEnabled(false);
                m_a1.setFocusable(false);
                m_a2.setFocusable(false);
                m_a3.setFocusable(false);
                m_a4.setFocusable(false);
                m_a5.setFocusable(false);
                m_ass1.setFocusable(false);
                m_ass2.setFocusable(false);
                m_ass3.setFocusable(false);
                m_ass4.setFocusable(false);
                m_ass5.setFocusable(false);
                m_q1.setFocusable(false);
                m_q2.setFocusable(false);
                m_q3.setFocusable(false);
                m_q4.setFocusable(false);
                m_q5.setFocusable(false);
                m_ex1.setFocusable(false);
                m_ex2.setFocusable(false);
                m_ex3.setFocusable(false);
                m_ex4.setFocusable(false);
                m_ex5.setFocusable(false);

//finals, dami hahahah
                f_a1 = dialog.findViewById(R.id.f_act1);
                f_a2 = dialog.findViewById(R.id.f_act2);
                f_a3 = dialog.findViewById(R.id.f_act3);
                f_a4 = dialog.findViewById(R.id.f_act4);
                f_a5 = dialog.findViewById(R.id.f_act5);
                f_ass1 = dialog.findViewById(R.id.f_ass1);
                f_ass2 = dialog.findViewById(R.id.f_ass2);
                f_ass3 = dialog.findViewById(R.id.f_ass3);
                f_ass4 = dialog.findViewById(R.id.f_ass4);
                f_ass5 = dialog.findViewById(R.id.f_ass5);
                f_q1 = dialog.findViewById(R.id.f_q1);
                f_q2 = dialog.findViewById(R.id.f_q2);
                f_q3 = dialog.findViewById(R.id.f_q3);
                f_q4 = dialog.findViewById(R.id.f_q4);
                f_q5 = dialog.findViewById(R.id.f_q5);
                f_ex1 = dialog.findViewById(R.id.f_el1);
                f_ex2 = dialog.findViewById(R.id.f_el2);
                f_ex3 = dialog.findViewById(R.id.f_el3);
                f_ex4 = dialog.findViewById(R.id.f_el4);
                f_ex5 = dialog.findViewById(R.id.f_el5);
//disable
                f_a1.setEnabled(false);
                f_a2.setEnabled(false);
                f_a3.setEnabled(false);
                f_a4.setEnabled(false);
                f_a5.setEnabled(false);
                f_ass1.setEnabled(false);
                f_ass2.setEnabled(false);
                f_ass3.setEnabled(false);
                f_ass4.setEnabled(false);
                f_ass5.setEnabled(false);
                f_q1.setEnabled(false);
                f_q2.setEnabled(false);
                f_q3.setEnabled(false);
                f_q4.setEnabled(false);
                f_q5.setEnabled(false);
                f_ex1.setEnabled(false);
                f_ex2.setEnabled(false);
                f_ex3.setEnabled(false);
                f_ex4.setEnabled(false);
                f_ex5.setEnabled(false);
                f_a1.setFocusable(false);
                f_a2.setFocusable(false);
                f_a3.setFocusable(false);
                f_a4.setFocusable(false);
                f_a5.setFocusable(false);
                f_ass1.setFocusable(false);
                f_ass2.setFocusable(false);
                f_ass3.setFocusable(false);
                f_ass4.setFocusable(false);
                f_ass5.setFocusable(false);
                f_q1.setFocusable(false);
                f_q2.setFocusable(false);
                f_q3.setFocusable(false);
                f_q4.setFocusable(false);
                f_q5.setFocusable(false);
                f_ex1.setFocusable(false);
                f_ex2.setFocusable(false);
                f_ex3.setFocusable(false);
                f_ex4.setFocusable(false);
                f_ex5.setFocusable(false);







                prelimBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (pLayout.getVisibility() == View.VISIBLE || mLayout.getVisibility() == View.VISIBLE || fLayout.getVisibility() == View.VISIBLE){
                            pLayout.setVisibility(View.GONE);
                            mLayout.setVisibility(View.GONE);
                            fLayout.setVisibility(View.GONE);
                        }else{
                            pLayout.setVisibility(View.VISIBLE);
                        }

                        prelimReference = rootNode.getReference("users/"+userIdT+"/Grades/"+SID+"/"+subjectModel.getClassCode()+"/Prelim");
                        prelimReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (!snapshot.hasChildren()){
                                    prelimSaveBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String prelimAct1 = p_a1.getText().toString();
                                            String prelimAct2 = p_a2.getText().toString();
                                            String prelimAct3 = p_a3.getText().toString();
                                            String prelimAct4 = p_a4.getText().toString();
                                            String prelimAct5 = p_a5.getText().toString();
                                            String prelimAss1 = p_ass1.getText().toString();
                                            String prelimAss2 = p_ass2.getText().toString();
                                            String prelimAss3 = p_ass3.getText().toString();
                                            String prelimAss4 = p_ass4.getText().toString();
                                            String prelimAss5 = p_ass5.getText().toString();
                                            String prelimQ1 = p_q1.getText().toString();
                                            String prelimQ2 = p_q2.getText().toString();
                                            String prelimQ3 = p_q3.getText().toString();
                                            String prelimQ4 = p_q4.getText().toString();
                                            String prelimQ5 = p_q5.getText().toString();
                                            String prelimE1 = p_ex1.getText().toString();
                                            String prelimE2 = p_ex2.getText().toString();
                                            String prelimE3 = p_ex3.getText().toString();
                                            String prelimE4 = p_ex4.getText().toString();
                                            String prelimE5 = p_ex5.getText().toString();

                                            prelimReference = rootNode.getReference("users/"+userIdT+"/Grades/"+SID+"/"+subjectModel.getClassCode());

                                            PrelimGradeModel prelimGradeModel = new PrelimGradeModel(
                                                    prelimAct1,prelimAct2,prelimAct3,prelimAct4,prelimAct5,
                                                    prelimAss1,prelimAss2,prelimAss3,prelimAss4,prelimAss5,
                                                    prelimQ1,prelimQ2,prelimQ3,prelimQ4,prelimQ5,
                                                    prelimE1,prelimE2,prelimE3,prelimE4,prelimE5);

                                            prelimReference.child("Prelim").setValue(prelimGradeModel);
                                            Toast.makeText(context.getApplicationContext(), "Prelim Saved", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                }else{
                                    prelimReference = rootNode.getReference("users/"+userIdT+"/Grades/"+SID+"/"+subjectModel.getClassCode());

                                    prelimReference.child("Prelim").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                if (task.getResult().exists()) {
                                                    DataSnapshot mySnapshot = task.getResult();

                                                    p_a1.setText(String.valueOf(mySnapshot.child("prelimAct1").getValue()));
                                                    p_a2.setText(String.valueOf(mySnapshot.child("prelimAct2").getValue()));
                                                    p_a3.setText(String.valueOf(mySnapshot.child("prelimAct3").getValue()));
                                                    p_a4.setText(String.valueOf(mySnapshot.child("prelimAct4").getValue()));
                                                    p_a5.setText(String.valueOf(mySnapshot.child("prelimAct5").getValue()));
                                                    p_ass1.setText(String.valueOf(mySnapshot.child("prelimAss1").getValue()));
                                                    p_ass2.setText(String.valueOf(mySnapshot.child("prelimAss2").getValue()));
                                                    p_ass3.setText(String.valueOf(mySnapshot.child("prelimAss3").getValue()));
                                                    p_ass4.setText(String.valueOf(mySnapshot.child("prelimAss4").getValue()));
                                                    p_ass5.setText(String.valueOf(mySnapshot.child("prelimAss5").getValue()));
                                                    p_q1.setText(String.valueOf(mySnapshot.child("prelimQ1").getValue()));
                                                    p_q2.setText(String.valueOf(mySnapshot.child("prelimQ2").getValue()));
                                                    p_q3.setText(String.valueOf(mySnapshot.child("prelimQ3").getValue()));
                                                    p_q4.setText(String.valueOf(mySnapshot.child("prelimQ4").getValue()));
                                                    p_q5.setText(String.valueOf(mySnapshot.child("prelimQ5").getValue()));
                                                    p_ex1.setText(String.valueOf(mySnapshot.child("prelimE1").getValue()));
                                                    p_ex2.setText(String.valueOf(mySnapshot.child("prelimE2").getValue()));
                                                    p_ex3.setText(String.valueOf(mySnapshot.child("prelimE3").getValue()));
                                                    p_ex4.setText(String.valueOf(mySnapshot.child("prelimE4").getValue()));
                                                    p_ex5.setText(String.valueOf(mySnapshot.child("prelimE5").getValue()));

                                                    if (!mySnapshot.child("prelimAct1").getValue().equals("")){
                                                        act_done_prelim.add("prelimAct1");
                                                        p_a1.setEnabled(false);
                                                        p_a1.setFocusable(false);
                                                        p_a1.setFocusableInTouchMode(false);
                                                        p_a1.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("prelimAct2").getValue().equals("")){
                                                        act_done_prelim.add("prelimAct2");
                                                        p_a2.setEnabled(false);
                                                        p_a2.setFocusable(false);
                                                        p_a2.setFocusableInTouchMode(false);
                                                        p_a2.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("prelimAct3").getValue().equals("")){
                                                        act_done_prelim.add("prelimAct3");
                                                        p_a3.setEnabled(false);
                                                        p_a3.setFocusable(false);
                                                        p_a3.setFocusableInTouchMode(false);
                                                        p_a3.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("prelimAct4").getValue().equals("")){
                                                        act_done_prelim.add("prelimAct4");
                                                        p_a4.setEnabled(false);
                                                        p_a4.setFocusable(false);
                                                        p_a4.setFocusableInTouchMode(false);
                                                        p_a4.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("prelimAct5").getValue().equals("")){
                                                        act_done_prelim.add("prelimAct5");
                                                        p_a5.setEnabled(false);
                                                        p_a5.setFocusable(false);
                                                        p_a5.setFocusableInTouchMode(false);
                                                        p_a5.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("prelimAss1").getValue().equals("")){
                                                        act_done_prelim.add("prelimAss1");
                                                        p_ass1.setEnabled(false);
                                                        p_ass1.setFocusable(false);
                                                        p_ass1.setFocusableInTouchMode(false);
                                                        p_ass1.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("prelimAss2").getValue().equals("")){
                                                        act_done_prelim.add("prelimAss2");
                                                        p_ass2.setEnabled(false);
                                                        p_ass2.setFocusable(false);
                                                        p_ass2.setFocusableInTouchMode(false);
                                                        p_ass2.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("prelimAss3").getValue().equals("")){
                                                        act_done_prelim.add("prelimAss3");
                                                        p_ass3.setEnabled(false);
                                                        p_ass3.setFocusable(false);
                                                        p_ass3.setFocusableInTouchMode(false);
                                                        p_ass3.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("prelimAss4").getValue().equals("")){
                                                        act_done_prelim.add("prelimAss4");
                                                        p_ass4.setEnabled(false);
                                                        p_ass4.setFocusable(false);
                                                        p_ass4.setFocusableInTouchMode(false);
                                                        p_ass4.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("prelimAss5").getValue().equals("")){
                                                        act_done_prelim.add("prelimAss5");
                                                        p_ass5.setEnabled(false);
                                                        p_ass5.setFocusable(false);
                                                        p_ass5.setFocusableInTouchMode(false);
                                                        p_ass5.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("prelimQ1").getValue().equals("")){
                                                        act_done_prelim.add("prelimQ1");
                                                        p_q1.setEnabled(false);
                                                        p_q1.setFocusable(false);
                                                        p_q1.setFocusableInTouchMode(false);
                                                        p_q1.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("prelimQ2").getValue().equals("")){
                                                        act_done_prelim.add("prelimQ2");
                                                        p_q2.setEnabled(false);
                                                        p_q2.setFocusable(false);
                                                        p_q2.setFocusableInTouchMode(false);
                                                        p_q2.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("prelimQ3").getValue().equals("")){
                                                        act_done_prelim.add("prelimQ3");
                                                        p_q3.setEnabled(false);
                                                        p_q3.setFocusable(false);
                                                        p_q3.setFocusableInTouchMode(false);
                                                        p_q3.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("prelimQ4").getValue().equals("")){
                                                        act_done_prelim.add("prelimQ4");
                                                        p_q4.setEnabled(false);
                                                        p_q4.setFocusable(false);
                                                        p_q4.setFocusableInTouchMode(false);
                                                        p_q4.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("prelimQ5").getValue().equals("")){
                                                        act_done_prelim.add("prelimQ5");
                                                        p_q5.setEnabled(false);
                                                        p_q5.setFocusable(false);
                                                        p_q5.setFocusableInTouchMode(false);
                                                        p_q5.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("prelimE1").getValue().equals("")){
                                                        act_done_prelim.add("prelimE1");
                                                        p_ex1.setEnabled(false);
                                                        p_ex1.setFocusable(false);
                                                        p_ex1.setFocusableInTouchMode(false);
                                                        p_ex1.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("prelimE2").getValue().equals("")){
                                                        act_done_prelim.add("prelimE2");
                                                        p_ex2.setEnabled(false);
                                                        p_ex2.setFocusable(false);
                                                        p_ex2.setFocusableInTouchMode(false);
                                                        p_ex2.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("prelimE3").getValue().equals("")){
                                                        act_done_prelim.add("prelimE3");
                                                        p_ex3.setEnabled(false);
                                                        p_ex3.setFocusable(false);
                                                        p_ex3.setFocusableInTouchMode(false);
                                                        p_ex3.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("prelimE4").getValue().equals("")){
                                                        act_done_prelim.add("prelimE4");
                                                        p_ex4.setEnabled(false);
                                                        p_ex4.setFocusable(false);
                                                        p_ex4.setFocusableInTouchMode(false);
                                                        p_ex4.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("prelimE5").getValue().equals("")){
                                                        act_done_prelim.add("prelimE5");
                                                        p_ex5.setEnabled(false);
                                                        p_ex5.setFocusable(false);
                                                        p_ex5.setFocusableInTouchMode(false);
                                                        p_ex5.setTextColor(Color.parseColor("#00c853"));
                                                    }

                                                    sendListPrelim(act_done_prelim,userIdT,mCode,SID);

                                                    prelimSaveBtn.setOnClickListener(v1 -> {

                                                        String prelimAct1 = p_a1.getText().toString();
                                                        String prelimAct2 = p_a2.getText().toString();
                                                        String prelimAct3 = p_a3.getText().toString();
                                                        String prelimAct4 = p_a4.getText().toString();
                                                        String prelimAct5 = p_a5.getText().toString();
                                                        String prelimAss1 = p_ass1.getText().toString();
                                                        String prelimAss2 = p_ass2.getText().toString();
                                                        String prelimAss3 = p_ass3.getText().toString();
                                                        String prelimAss4 = p_ass4.getText().toString();
                                                        String prelimAss5 = p_ass5.getText().toString();
                                                        String prelimQ1 = p_q1.getText().toString();
                                                        String prelimQ2 = p_q2.getText().toString();
                                                        String prelimQ3 = p_q3.getText().toString();
                                                        String prelimQ4 = p_q4.getText().toString();
                                                        String prelimQ5 = p_q5.getText().toString();
                                                        String prelimE1 = p_ex1.getText().toString();
                                                        String prelimE2 = p_ex2.getText().toString();
                                                        String prelimE3 = p_ex3.getText().toString();
                                                        String prelimE4 = p_ex4.getText().toString();
                                                        String prelimE5 = p_ex5.getText().toString();

                                                        prelimReference = rootNode.getReference("users/"+userIdT+"/Grades/"+SID+"/"+subjectModel.getClassCode());

                                                        HashMap<String,Object> hashMap = new HashMap<>();
                                                        hashMap.put("prelimAct1",prelimAct1);
                                                        hashMap.put("prelimAct2",prelimAct2);
                                                        hashMap.put("prelimAct3",prelimAct3);
                                                        hashMap.put("prelimAct4",prelimAct4);
                                                        hashMap.put("prelimAct5",prelimAct5);
                                                        hashMap.put("prelimAss1",prelimAss1);
                                                        hashMap.put("prelimAss2",prelimAss2);
                                                        hashMap.put("prelimAss3",prelimAss3);
                                                        hashMap.put("prelimAss4",prelimAss4);
                                                        hashMap.put("prelimAss5",prelimAss5);
                                                        hashMap.put("prelimQ1",prelimQ1);
                                                        hashMap.put("prelimQ2",prelimQ2);
                                                        hashMap.put("prelimQ3",prelimQ3);
                                                        hashMap.put("prelimQ4",prelimQ4);
                                                        hashMap.put("prelimQ5",prelimQ5);
                                                        hashMap.put("prelimE1",prelimE1);
                                                        hashMap.put("prelimE2",prelimE2);
                                                        hashMap.put("prelimE3",prelimE3);
                                                        hashMap.put("prelimE4",prelimE4);
                                                        hashMap.put("prelimE5",prelimE5);

                                                        prelimReference.child("Prelim").updateChildren(hashMap);

                                                        Toast.makeText(context.getApplicationContext(), "Points Have been saved", Toast.LENGTH_SHORT).show();
                                                    });
                                                    //end of else prelim save btn




                                                }else { Toast.makeText(context.getApplicationContext(), "No Score Yet", Toast.LENGTH_SHORT).show();}
                                            }else{ Toast.makeText(context.getApplicationContext(), "There is some error", Toast.LENGTH_SHORT).show(); }

                                        }
                                    });
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                //midterm
                midtermBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (pLayout.getVisibility() == View.VISIBLE || mLayout.getVisibility() == View.VISIBLE || fLayout.getVisibility() == View.VISIBLE){
                            pLayout.setVisibility(View.GONE);
                            mLayout.setVisibility(View.GONE);
                            fLayout.setVisibility(View.GONE);
                        }else{
                            mLayout.setVisibility(View.VISIBLE);
                        }

                        midtermReference = rootNode.getReference("users/"+userIdT+"/Grades/"+SID+"/"+subjectModel.getClassCode()+"/Midterm");
                        midtermReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (!snapshot.hasChildren()){
                                    midtermSaveBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String midtermAct1 = m_a1.getText().toString();
                                            String midtermAct2 = m_a2.getText().toString();
                                            String midtermAct3 = m_a3.getText().toString();
                                            String midtermAct4 = m_a4.getText().toString();
                                            String midtermAct5 = m_a5.getText().toString();
                                            String midtermAss1 = m_ass1.getText().toString();
                                            String midtermAss2 = m_ass2.getText().toString();
                                            String midtermAss3 = m_ass3.getText().toString();
                                            String midtermAss4 = m_ass4.getText().toString();
                                            String midtermAss5 = m_ass5.getText().toString();
                                            String midtermQ1 = m_q1.getText().toString();
                                            String midtermQ2 = m_q2.getText().toString();
                                            String midtermQ3 = m_q3.getText().toString();
                                            String midtermQ4 = m_q4.getText().toString();
                                            String midtermQ5 = m_q5.getText().toString();
                                            String midtermE1 = m_ex1.getText().toString();
                                            String midtermE2 = m_ex2.getText().toString();
                                            String midtermE3 = m_ex3.getText().toString();
                                            String midtermE4 = m_ex4.getText().toString();
                                            String midtermE5 = m_ex5.getText().toString();

                                            midtermReference = rootNode.getReference("users/"+userIdT+"/Grades/"+SID+"/"+subjectModel.getClassCode());

                                            MidtermGradeModel midtermGradeModel = new MidtermGradeModel(
                                                    midtermAct1,midtermAct2,midtermAct3,midtermAct4,midtermAct5,
                                                    midtermAss1,midtermAss2,midtermAss3,midtermAss4,midtermAss5,
                                                    midtermQ1,midtermQ2,midtermQ3,midtermQ4,midtermQ5,
                                                    midtermE1,midtermE2,midtermE3,midtermE4,midtermE5);

                                            midtermReference.child("Midterm").setValue(midtermGradeModel);
                                            Toast.makeText(context.getApplicationContext(), "Midterm Saved", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                }else{
                                    midtermReference = rootNode.getReference("users/"+userIdT+"/Grades/"+SID+"/"+subjectModel.getClassCode());

                                    midtermReference.child("Midterm").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                if (task.getResult().exists()) {
                                                    DataSnapshot mySnapshot = task.getResult();

                                                    m_a1.setText(String.valueOf(mySnapshot.child("midtermAct1").getValue()));
                                                    m_a2.setText(String.valueOf(mySnapshot.child("midtermAct2").getValue()));
                                                    m_a3.setText(String.valueOf(mySnapshot.child("midtermAct3").getValue()));
                                                    m_a4.setText(String.valueOf(mySnapshot.child("midtermAct4").getValue()));
                                                    m_a5.setText(String.valueOf(mySnapshot.child("midtermAct5").getValue()));
                                                    m_ass1.setText(String.valueOf(mySnapshot.child("midtermAss1").getValue()));
                                                    m_ass2.setText(String.valueOf(mySnapshot.child("midtermAss2").getValue()));
                                                    m_ass3.setText(String.valueOf(mySnapshot.child("midtermAss3").getValue()));
                                                    m_ass4.setText(String.valueOf(mySnapshot.child("midtermAss4").getValue()));
                                                    m_ass5.setText(String.valueOf(mySnapshot.child("midtermAss5").getValue()));
                                                    m_q1.setText(String.valueOf(mySnapshot.child("midtermQ1").getValue()));
                                                    m_q2.setText(String.valueOf(mySnapshot.child("midtermQ2").getValue()));
                                                    m_q3.setText(String.valueOf(mySnapshot.child("midtermQ3").getValue()));
                                                    m_q4.setText(String.valueOf(mySnapshot.child("midtermQ4").getValue()));
                                                    m_q5.setText(String.valueOf(mySnapshot.child("midtermQ5").getValue()));
                                                    m_ex1.setText(String.valueOf(mySnapshot.child("midtermE1").getValue()));
                                                    m_ex2.setText(String.valueOf(mySnapshot.child("midtermE2").getValue()));
                                                    m_ex3.setText(String.valueOf(mySnapshot.child("midtermE3").getValue()));
                                                    m_ex4.setText(String.valueOf(mySnapshot.child("midtermE4").getValue()));
                                                    m_ex5.setText(String.valueOf(mySnapshot.child("midtermE5").getValue()));

                                                    if (!mySnapshot.child("midtermAct1").getValue().equals("")){
                                                        act_done_midterm.add("midtermAct1");
                                                        m_a1.setEnabled(false);
                                                        m_a1.setFocusable(false);
                                                        m_a1.setFocusableInTouchMode(false);
                                                        m_a1.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("midtermAct2").getValue().equals("")){
                                                        act_done_midterm.add("midtermAct2");
                                                        m_a2.setEnabled(false);
                                                        m_a2.setFocusable(false);
                                                        m_a2.setFocusableInTouchMode(false);
                                                        m_a2.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("midtermAct3").getValue().equals("")){
                                                        act_done_midterm.add("midtermAct3");
                                                        m_a3.setEnabled(false);
                                                        m_a3.setFocusable(false);
                                                        m_a3.setFocusableInTouchMode(false);
                                                        m_a3.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("midtermAct4").getValue().equals("")){
                                                        act_done_midterm.add("midtermAct4");
                                                        m_a4.setEnabled(false);
                                                        m_a4.setFocusable(false);
                                                        m_a4.setFocusableInTouchMode(false);
                                                        m_a4.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("midtermAct5").getValue().equals("")){
                                                        act_done_midterm.add("midtermAct5");
                                                        m_a5.setEnabled(false);
                                                        m_a5.setFocusable(false);
                                                        m_a5.setFocusableInTouchMode(false);
                                                        m_a5.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("midtermAss1").getValue().equals("")){
                                                        act_done_midterm.add("midtermAss1");
                                                        m_ass1.setEnabled(false);
                                                        m_ass1.setFocusable(false);
                                                        m_ass1.setFocusableInTouchMode(false);
                                                        m_ass1.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("midtermAss2").getValue().equals("")){
                                                        act_done_midterm.add("midtermAss2");
                                                        m_ass2.setEnabled(false);
                                                        m_ass2.setFocusable(false);
                                                        m_ass2.setFocusableInTouchMode(false);
                                                        m_ass2.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("midtermAss3").getValue().equals("")){
                                                        act_done_midterm.add("midtermAss3");
                                                        m_ass3.setEnabled(false);
                                                        m_ass3.setFocusable(false);
                                                        m_ass3.setFocusableInTouchMode(false);
                                                        m_ass3.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("midtermAss4").getValue().equals("")){
                                                        act_done_midterm.add("midtermAss4");
                                                        m_ass4.setEnabled(false);
                                                        m_ass4.setFocusable(false);
                                                        m_ass4.setFocusableInTouchMode(false);
                                                        m_ass4.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("midtermAss5").getValue().equals("")){
                                                        act_done_midterm.add("midtermAss5");
                                                        m_ass5.setEnabled(false);
                                                        m_ass5.setFocusable(false);
                                                        m_ass5.setFocusableInTouchMode(false);
                                                        m_ass5.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("midtermQ1").getValue().equals("")){
                                                        act_done_midterm.add("midtermQ1");
                                                        m_q1.setEnabled(false);
                                                        m_q1.setFocusable(false);
                                                        m_q1.setFocusableInTouchMode(false);
                                                        m_q1.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("midtermQ2").getValue().equals("")){
                                                        act_done_midterm.add("midtermQ2");
                                                        m_q2.setEnabled(false);
                                                        m_q2.setFocusable(false);
                                                        m_q2.setFocusableInTouchMode(false);
                                                        m_q2.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("midtermQ3").getValue().equals("")){
                                                        act_done_midterm.add("midtermQ3");
                                                        m_q3.setEnabled(false);
                                                        m_q3.setFocusable(false);
                                                        m_q3.setFocusableInTouchMode(false);
                                                        m_q3.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("midtermQ4").getValue().equals("")){
                                                        act_done_midterm.add("midtermQ4");
                                                        m_q4.setEnabled(false);
                                                        m_q4.setFocusable(false);
                                                        m_q4.setFocusableInTouchMode(false);
                                                        m_q4.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("midtermQ5").getValue().equals("")){
                                                        act_done_midterm.add("midtermQ5");
                                                        m_q5.setEnabled(false);
                                                        m_q5.setFocusable(false);
                                                        m_q5.setFocusableInTouchMode(false);
                                                        m_q5.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("midtermE1").getValue().equals("")){
                                                        act_done_midterm.add("midtermE1");
                                                        m_ex1.setEnabled(false);
                                                        m_ex1.setFocusable(false);
                                                        m_ex1.setFocusableInTouchMode(false);
                                                        m_ex1.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("midtermE2").getValue().equals("")){
                                                        act_done_midterm.add("midtermE2");
                                                        m_ex2.setEnabled(false);
                                                        m_ex2.setFocusable(false);
                                                        m_ex2.setFocusableInTouchMode(false);
                                                        m_ex2.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("midtermE3").getValue().equals("")){
                                                        act_done_midterm.add("midtermE3");
                                                        m_ex3.setEnabled(false);
                                                        m_ex3.setFocusable(false);
                                                        m_ex3.setFocusableInTouchMode(false);
                                                        m_ex3.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("midtermE4").getValue().equals("")){
                                                        act_done_midterm.add("midtermE4");
                                                        m_ex4.setEnabled(false);
                                                        m_ex4.setFocusable(false);
                                                        m_ex4.setFocusableInTouchMode(false);
                                                        m_ex4.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("midtermE5").getValue().equals("")){
                                                        act_done_midterm.add("midtermE5");
                                                        m_ex5.setEnabled(false);
                                                        m_ex5.setFocusable(false);
                                                        m_ex5.setFocusableInTouchMode(false);
                                                        m_ex5.setTextColor(Color.parseColor("#00c853"));
                                                    }

                                                    sendListMidterm(act_done_midterm,userIdT,mCode,SID);

                                                    midtermSaveBtn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            String midtermAct1 = m_a1.getText().toString();
                                                            String midtermAct2 = m_a2.getText().toString();
                                                            String midtermAct3 = m_a3.getText().toString();
                                                            String midtermAct4 = m_a4.getText().toString();
                                                            String midtermAct5 = m_a5.getText().toString();
                                                            String midtermAss1 = m_ass1.getText().toString();
                                                            String midtermAss2 = m_ass2.getText().toString();
                                                            String midtermAss3 = m_ass3.getText().toString();
                                                            String midtermAss4 = m_ass4.getText().toString();
                                                            String midtermAss5 = m_ass5.getText().toString();
                                                            String midtermQ1 = m_q1.getText().toString();
                                                            String midtermQ2 = m_q2.getText().toString();
                                                            String midtermQ3 = m_q3.getText().toString();
                                                            String midtermQ4 = m_q4.getText().toString();
                                                            String midtermQ5 = m_q5.getText().toString();
                                                            String midtermE1 = m_ex1.getText().toString();
                                                            String midtermE2 = m_ex2.getText().toString();
                                                            String midtermE3 = m_ex3.getText().toString();
                                                            String midtermE4 = m_ex4.getText().toString();
                                                            String midtermE5 = m_ex5.getText().toString();

                                                            midtermReference = rootNode.getReference("users/"+userIdT+"/Grades/"+SID+"/"+subjectModel.getClassCode());

                                                            HashMap<String,Object> hashMap = new HashMap<>();
                                                            hashMap.put("midtermAct1",midtermAct1);
                                                            hashMap.put("midtermAct2",midtermAct2);
                                                            hashMap.put("midtermAct3",midtermAct3);
                                                            hashMap.put("midtermAct4",midtermAct4);
                                                            hashMap.put("midtermAct5",midtermAct5);
                                                            hashMap.put("midtermAss1",midtermAss1);
                                                            hashMap.put("midtermAss2",midtermAss2);
                                                            hashMap.put("midtermAss3",midtermAss3);
                                                            hashMap.put("midtermAss4",midtermAss4);
                                                            hashMap.put("midtermAss5",midtermAss5);
                                                            hashMap.put("midtermQ1",midtermQ1);
                                                            hashMap.put("midtermQ2",midtermQ2);
                                                            hashMap.put("midtermQ3",midtermQ3);
                                                            hashMap.put("midtermQ4",midtermQ4);
                                                            hashMap.put("midtermQ5",midtermQ5);
                                                            hashMap.put("midtermE1",midtermE1);
                                                            hashMap.put("midtermE2",midtermE2);
                                                            hashMap.put("midtermE3",midtermE3);
                                                            hashMap.put("midtermE4",midtermE4);
                                                            hashMap.put("midtermE5",midtermE5);

                                                            midtermReference.child("Midterm").updateChildren(hashMap);

                                                            Toast.makeText(context.getApplicationContext(), "Points Have been saved", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                    //end of else midterm save btn




                                                }else { Toast.makeText(context.getApplicationContext(), "No Score Yet", Toast.LENGTH_SHORT).show();}
                                            }else{ Toast.makeText(context.getApplicationContext(), "There is some error", Toast.LENGTH_SHORT).show(); }

                                        }
                                    });
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                //finals
                finalBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (pLayout.getVisibility() == View.VISIBLE || mLayout.getVisibility() == View.VISIBLE || fLayout.getVisibility() == View.VISIBLE){
                            pLayout.setVisibility(View.GONE);
                            mLayout.setVisibility(View.GONE);
                            fLayout.setVisibility(View.GONE);
                        }else{
                            fLayout.setVisibility(View.VISIBLE);
                        }

                        finalsReference = rootNode.getReference("users/"+userIdT+"/Grades/"+SID+"/"+subjectModel.getClassCode()+"/Finals");
                        finalsReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (!snapshot.hasChildren()){
                                    finalSaveBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String finalsAct1 = f_a1.getText().toString();
                                            String finalsAct2 = f_a2.getText().toString();
                                            String finalsAct3 = f_a3.getText().toString();
                                            String finalsAct4 = f_a4.getText().toString();
                                            String finalsAct5 = f_a5.getText().toString();
                                            String finalsAss1 = f_ass1.getText().toString();
                                            String finalsAss2 = f_ass2.getText().toString();
                                            String finalsAss3 = f_ass3.getText().toString();
                                            String finalsAss4 = f_ass4.getText().toString();
                                            String finalsAss5 = f_ass5.getText().toString();
                                            String finalsQ1 = f_q1.getText().toString();
                                            String finalsQ2 = f_q2.getText().toString();
                                            String finalsQ3 = f_q3.getText().toString();
                                            String finalsQ4 = f_q4.getText().toString();
                                            String finalsQ5 = f_q5.getText().toString();
                                            String finalsE1 = f_ex1.getText().toString();
                                            String finalsE2 = f_ex2.getText().toString();
                                            String finalsE3 = f_ex3.getText().toString();
                                            String finalsE4 = f_ex4.getText().toString();
                                            String finalsE5 = f_ex5.getText().toString();

                                            finalsReference = rootNode.getReference("users/"+userIdT+"/Grades/"+SID+"/"+subjectModel.getClassCode());

                                            FinalsGradeModel finalsGradeModel = new FinalsGradeModel(
                                                    finalsAct1,finalsAct2,finalsAct3,finalsAct4,finalsAct5,
                                                    finalsAss1,finalsAss2,finalsAss3,finalsAss4,finalsAss5,
                                                    finalsQ1,finalsQ2,finalsQ3,finalsQ4,finalsQ5,
                                                    finalsE1,finalsE2,finalsE3,finalsE4,finalsE5);

                                            finalsReference.child("Finals").setValue(finalsGradeModel);
                                            Toast.makeText(context.getApplicationContext(), "Finals Saved", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                }else{
                                    finalsReference = rootNode.getReference("users/"+userIdT+"/Grades/"+SID+"/"+subjectModel.getClassCode());

                                    finalsReference.child("Finals").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                if (task.getResult().exists()) {
                                                    DataSnapshot mySnapshot = task.getResult();

                                                    f_a1.setText(String.valueOf(mySnapshot.child("finalsAct1").getValue()));
                                                    f_a2.setText(String.valueOf(mySnapshot.child("finalsAct2").getValue()));
                                                    f_a3.setText(String.valueOf(mySnapshot.child("finalsAct3").getValue()));
                                                    f_a4.setText(String.valueOf(mySnapshot.child("finalsAct4").getValue()));
                                                    f_a5.setText(String.valueOf(mySnapshot.child("finalsAct5").getValue()));
                                                    f_ass1.setText(String.valueOf(mySnapshot.child("finalsAss1").getValue()));
                                                    f_ass2.setText(String.valueOf(mySnapshot.child("finalsAss2").getValue()));
                                                    f_ass3.setText(String.valueOf(mySnapshot.child("finalsAss3").getValue()));
                                                    f_ass4.setText(String.valueOf(mySnapshot.child("finalsAss4").getValue()));
                                                    f_ass5.setText(String.valueOf(mySnapshot.child("finalsAss5").getValue()));
                                                    f_q1.setText(String.valueOf(mySnapshot.child("finalsQ1").getValue()));
                                                    f_q2.setText(String.valueOf(mySnapshot.child("finalsQ2").getValue()));
                                                    f_q3.setText(String.valueOf(mySnapshot.child("finalsQ3").getValue()));
                                                    f_q4.setText(String.valueOf(mySnapshot.child("finalsQ4").getValue()));
                                                    f_q5.setText(String.valueOf(mySnapshot.child("finalsQ5").getValue()));
                                                    f_ex1.setText(String.valueOf(mySnapshot.child("finalsE1").getValue()));
                                                    f_ex2.setText(String.valueOf(mySnapshot.child("finalsE2").getValue()));
                                                    f_ex3.setText(String.valueOf(mySnapshot.child("finalsE3").getValue()));
                                                    f_ex4.setText(String.valueOf(mySnapshot.child("finalsE4").getValue()));
                                                    f_ex5.setText(String.valueOf(mySnapshot.child("finalsE5").getValue()));

                                                    if (!mySnapshot.child("finalsAct1").getValue().equals("")){
                                                        act_done_finals.add("finalsAct1");
                                                        f_a1.setEnabled(false);
                                                        f_a1.setFocusable(false);
                                                        f_a1.setFocusableInTouchMode(false);
                                                        f_a1.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("finalsAct2").getValue().equals("")){
                                                        act_done_finals.add("finalsAct2");
                                                        f_a2.setEnabled(false);
                                                        f_a2.setFocusable(false);
                                                        f_a2.setFocusableInTouchMode(false);
                                                        f_a2.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("finalsAct3").getValue().equals("")){
                                                        act_done_finals.add("finalsAct3");
                                                        f_a3.setEnabled(false);
                                                        f_a3.setFocusable(false);
                                                        f_a3.setFocusableInTouchMode(false);
                                                        f_a3.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("finalsAct4").getValue().equals("")){
                                                        act_done_finals.add("finalsAct4");
                                                        f_a4.setEnabled(false);
                                                        f_a4.setFocusable(false);
                                                        f_a4.setFocusableInTouchMode(false);
                                                        f_a4.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("finalsAct5").getValue().equals("")){
                                                        act_done_finals.add("finalsAct5");
                                                        f_a5.setEnabled(false);
                                                        f_a5.setFocusable(false);
                                                        f_a5.setFocusableInTouchMode(false);
                                                        f_a5.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("finalsAss1").getValue().equals("")){
                                                        act_done_finals.add("finalsAss1");
                                                        f_ass1.setEnabled(false);
                                                        f_ass1.setFocusable(false);
                                                        f_ass1.setFocusableInTouchMode(false);
                                                        f_ass1.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("finalsAss2").getValue().equals("")){
                                                        act_done_finals.add("finalsAss2");
                                                        f_ass2.setEnabled(false);
                                                        f_ass2.setFocusable(false);
                                                        f_ass2.setFocusableInTouchMode(false);
                                                        f_ass2.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("finalsAss3").getValue().equals("")){
                                                        act_done_finals.add("finalsAss3");
                                                        f_ass3.setEnabled(false);
                                                        f_ass3.setFocusable(false);
                                                        f_ass3.setFocusableInTouchMode(false);
                                                        f_ass3.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("finalsAss4").getValue().equals("")){
                                                        act_done_finals.add("finalsAss4");
                                                        f_ass4.setEnabled(false);
                                                        f_ass4.setFocusable(false);
                                                        f_ass4.setFocusableInTouchMode(false);
                                                        f_ass4.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("finalsAss5").getValue().equals("")){
                                                        act_done_finals.add("finalsAss5");
                                                        f_ass5.setEnabled(false);
                                                        f_ass5.setFocusable(false);
                                                        f_ass5.setFocusableInTouchMode(false);
                                                        f_ass5.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("finalsQ1").getValue().equals("")){
                                                        act_done_finals.add("finalsQ1");
                                                        f_q1.setEnabled(false);
                                                        f_q1.setFocusable(false);
                                                        f_q1.setFocusableInTouchMode(false);
                                                        f_q1.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("finalsQ2").getValue().equals("")){
                                                        act_done_finals.add("finalsQ2");
                                                        f_q2.setEnabled(false);
                                                        f_q2.setFocusable(false);
                                                        f_q2.setFocusableInTouchMode(false);
                                                        f_q2.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("finalsQ3").getValue().equals("")){
                                                        act_done_finals.add("finalsQ3");
                                                        f_q3.setEnabled(false);
                                                        f_q3.setFocusable(false);
                                                        f_q3.setFocusableInTouchMode(false);
                                                        f_q3.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("finalsQ4").getValue().equals("")){
                                                        act_done_finals.add("finalsQ4");
                                                        f_q4.setEnabled(false);
                                                        f_q4.setFocusable(false);
                                                        f_q4.setFocusableInTouchMode(false);
                                                        f_q4.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("finalsQ5").getValue().equals("")){
                                                        act_done_finals.add("finalsQ5");
                                                        f_q5.setEnabled(false);
                                                        f_q5.setFocusable(false);
                                                        f_q5.setFocusableInTouchMode(false);
                                                        f_q5.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("finalsE1").getValue().equals("")){
                                                        act_done_finals.add("finalsE1");
                                                        f_ex1.setEnabled(false);
                                                        f_ex1.setFocusable(false);
                                                        f_ex1.setFocusableInTouchMode(false);
                                                        f_ex1.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("finalsE2").getValue().equals("")){
                                                        act_done_finals.add("finalsE2");
                                                        f_ex2.setEnabled(false);
                                                        f_ex2.setFocusable(false);
                                                        f_ex2.setFocusableInTouchMode(false);
                                                        f_ex2.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("finalsE3").getValue().equals("")){
                                                        act_done_finals.add("finalsE3");
                                                        f_ex3.setEnabled(false);
                                                        f_ex3.setFocusable(false);
                                                        f_ex3.setFocusableInTouchMode(false);
                                                        f_ex3.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("finalsE4").getValue().equals("")){
                                                        act_done_finals.add("finalsE4");
                                                        f_ex4.setEnabled(false);
                                                        f_ex4.setFocusable(false);
                                                        f_ex4.setFocusableInTouchMode(false);
                                                        f_ex4.setTextColor(Color.parseColor("#00c853"));
                                                    }
                                                    if (!mySnapshot.child("finalsE5").getValue().equals("")){
                                                        act_done_finals.add("finalsE5");
                                                        f_ex5.setEnabled(false);
                                                        f_ex5.setFocusable(false);
                                                        f_ex5.setFocusableInTouchMode(false);
                                                        f_ex5.setTextColor(Color.parseColor("#00c853"));
                                                    }

                                                    sendListFinals(act_done_finals,userIdT,mCode,SID);

                                                    finalSaveBtn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            String finalsAct1 = f_a1.getText().toString();
                                                            String finalsAct2 = f_a2.getText().toString();
                                                            String finalsAct3 = f_a3.getText().toString();
                                                            String finalsAct4 = f_a4.getText().toString();
                                                            String finalsAct5 = f_a5.getText().toString();
                                                            String finalsAss1 = f_ass1.getText().toString();
                                                            String finalsAss2 = f_ass2.getText().toString();
                                                            String finalsAss3 = f_ass3.getText().toString();
                                                            String finalsAss4 = f_ass4.getText().toString();
                                                            String finalsAss5 = f_ass5.getText().toString();
                                                            String finalsQ1 = f_q1.getText().toString();
                                                            String finalsQ2 = f_q2.getText().toString();
                                                            String finalsQ3 = f_q3.getText().toString();
                                                            String finalsQ4 = f_q4.getText().toString();
                                                            String finalsQ5 = f_q5.getText().toString();
                                                            String finalsE1 = f_ex1.getText().toString();
                                                            String finalsE2 = f_ex2.getText().toString();
                                                            String finalsE3 = f_ex3.getText().toString();
                                                            String finalsE4 = f_ex4.getText().toString();
                                                            String finalsE5 = f_ex5.getText().toString();

                                                            finalsReference = rootNode.getReference("users/"+userIdT+"/Grades/"+SID+"/"+subjectModel.getClassCode());

                                                            HashMap<String,Object> hashMap = new HashMap<>();
                                                            hashMap.put("finalsAct1",finalsAct1);
                                                            hashMap.put("finalsAct2",finalsAct2);
                                                            hashMap.put("finalsAct3",finalsAct3);
                                                            hashMap.put("finalsAct4",finalsAct4);
                                                            hashMap.put("finalsAct5",finalsAct5);
                                                            hashMap.put("finalsAss1",finalsAss1);
                                                            hashMap.put("finalsAss2",finalsAss2);
                                                            hashMap.put("finalsAss3",finalsAss3);
                                                            hashMap.put("finalsAss4",finalsAss4);
                                                            hashMap.put("finalsAss5",finalsAss5);
                                                            hashMap.put("finalsQ1",finalsQ1);
                                                            hashMap.put("finalsQ2",finalsQ2);
                                                            hashMap.put("finalsQ3",finalsQ3);
                                                            hashMap.put("finalsQ4",finalsQ4);
                                                            hashMap.put("finalsQ5",finalsQ5);
                                                            hashMap.put("finalsE1",finalsE1);
                                                            hashMap.put("finalsE2",finalsE2);
                                                            hashMap.put("finalsE3",finalsE3);
                                                            hashMap.put("finalsE4",finalsE4);
                                                            hashMap.put("finalsE5",finalsE5);

                                                            finalsReference.child("Finals").updateChildren(hashMap);

                                                            Toast.makeText(context.getApplicationContext(), "Points Have been saved", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                    //end of else finals save btn




                                                }else { Toast.makeText(context.getApplicationContext(), "No Score Yet", Toast.LENGTH_SHORT).show();}
                                            }else{ Toast.makeText(context.getApplicationContext(), "There is some error", Toast.LENGTH_SHORT).show(); }

                                        }
                                    });
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });



                closePopUpBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });




                dialog.show();

            }

            private void sendListPrelim(ArrayList<String> act_done_prelim, String userIdT, String mCode,String SID) {
                adminRef = rootNode.getReference("users/"+userIdT+"/Students/"+mCode.toUpperCase()+"/"+SID);
                adminRef.child("pActCount").setValue(String.valueOf(act_done_prelim.size()));
                act_done_prelim.clear();

            }

            private void sendListMidterm(ArrayList<String> act_done_midterm, String userIdT, String mCode,String SID) {
                adminRef = rootNode.getReference("users/"+userIdT+"/Students/"+mCode.toUpperCase()+"/"+SID);
                adminRef.child("mActCount").setValue(String.valueOf(act_done_midterm.size()));
                act_done_midterm.clear();

            }

            private void sendListFinals(ArrayList<String> act_done_finals, String userIdT, String mCode,String SID) {
                adminRef = rootNode.getReference("users/"+userIdT+"/Students/"+mCode.toUpperCase()+"/"+SID);
                adminRef.child("fActCount").setValue(String.valueOf(act_done_finals.size()));
                act_done_finals.clear();

            }


        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button codeBtn;
        TextView ClassNameTv;
        CardView myView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            codeBtn = itemView.findViewById(R.id.my_code);
            ClassNameTv = itemView.findViewById(R.id.my_subject);
            myView = itemView.findViewById(R.id.mycardview);

        }
    }

}
