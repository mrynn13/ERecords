package com.example.erecordsv2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder>{
    Context context;
    ArrayList<StudentModel> list;
    ArrayList<String> act_done_prelim;
    ArrayList<String> act_done_midterm;
    ArrayList<String> act_done_finals;

    ArrayList<Integer> p_act_ave;
    ArrayList<Integer> p_ass_ave;
    ArrayList<Integer> p_q_ave;
    ArrayList<Integer> p_ex_ave;
    ArrayList<Integer> p_average;

    ArrayList<Integer> m_act_ave;
    ArrayList<Integer> m_ass_ave;
    ArrayList<Integer> m_q_ave;
    ArrayList<Integer> m_ex_ave;
    ArrayList<Integer> m_average;

    ArrayList<Integer> f_act_ave;
    ArrayList<Integer> f_ass_ave;
    ArrayList<Integer> f_q_ave;
    ArrayList<Integer> f_ex_ave;
    ArrayList<Integer> f_average;

    ArrayList<Float> overall_average;







    Dialog dialog,dialog1;
    Dialog notifyDialog;
    AlertDialog.Builder dialogBuilder;

    FirebaseAuth fAuth;
    FirebaseDatabase rootNode;
    DatabaseReference messageReference,reference,gradesRef,studentInfoRef,prelimReference,midtermReference,finalsReference;

    public static final String CODE = "CODE";
    public static final String ICODE = "ICODE";

    private static final DecimalFormat df = new DecimalFormat("0.00");


    String pActTotals;
    String pAssTotals;
    String pQTotals ;
    String pExTotals;

    String mActTotals;
    String mAssTotals;
    String mQTotals;
    String mExTotals;

    String fActTotals;
    String fAssTotals;
    String fQTotals;
    String fExTotals;

    public StudentAdapter(Context context, ArrayList<StudentModel> list, String pActTotal, String pAssTotal, String pQTotal, String pExTotal, String mActTotal, String mAssTotal, String mQTotal, String mExTotal, String fActTotal, String fAssTotal, String fQTotal, String fExTotal) {
        this.context = context;
        this.list = list;
        pActTotals = pActTotal;
        pAssTotals = pAssTotal;
        pQTotals = pQTotal;
        pExTotals = pExTotal;
        mActTotals = mActTotal;
        mAssTotals = mAssTotal;
        mQTotals = mQTotal;
        mExTotals = mExTotal;
        fActTotals = fActTotal;
        fAssTotals = fAssTotal;
        fQTotals = fQTotal;
        fExTotals = fExTotal;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student,parent,false);
        return new MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setIsRecyclable(false);

        StudentModel studentModel = list.get(position);
        act_done_prelim = new ArrayList<String>();
        act_done_midterm = new ArrayList<String>();
        act_done_finals = new ArrayList<String>();


        p_act_ave = new ArrayList<Integer>();
        p_ass_ave = new ArrayList<Integer>();
        p_q_ave = new ArrayList<Integer>();
        p_ex_ave = new ArrayList<Integer>();
        p_average = new ArrayList<Integer>();

        m_act_ave = new ArrayList<Integer>();
        m_ass_ave = new ArrayList<Integer>();
        m_q_ave = new ArrayList<Integer>();
        m_ex_ave = new ArrayList<Integer>();
        m_average = new ArrayList<Integer>();

        f_act_ave = new ArrayList<Integer>();
        f_ass_ave = new ArrayList<Integer>();
        f_q_ave = new ArrayList<Integer>();
        f_ex_ave = new ArrayList<Integer>();
        f_average = new ArrayList<Integer>();

        overall_average = new ArrayList<Float>();
        df.setRoundingMode(RoundingMode.UP);


        //getIntent
        Intent data = ((Activity) context).getIntent();

        String pActTotal = data.getStringExtra("pActTotal");
        String pAssTotal = data.getStringExtra("pAssTotal");
        String pQTotal = data.getStringExtra("pQTotal");
        String pExTotal = data.getStringExtra("pExTotal");

        String mActTotal = data.getStringExtra("mActTotal");
        String mAssTotal = data.getStringExtra("mAssTotal");
        String mQTotal = data.getStringExtra("mQTotal");
        String mExTotal = data.getStringExtra("mExTotal");

        String fActTotal = data.getStringExtra("fActTotal");
        String fAssTotal = data.getStringExtra("fAssTotal");
        String fQTotal = data.getStringExtra("fQTotal");
        String fExTotal = data.getStringExtra("fExTotal");






        //loading dialog
        LoadingDialog loadingDialog = new LoadingDialog((Activity) context);

        fAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance();
        String userId = fAuth.getCurrentUser().getUid();

        String pText = studentModel.getpActCount();
        String mText = studentModel.getmActCount();
        String fText = studentModel.getfActCount();

        holder.sName.setText(studentModel.getsName());
        holder.sId.setText(studentModel.getsId());
        holder.pActCount.setText(pText);
        holder.mActCount.setText(mText);
        holder.fActCount.setText(fText);





        if(!TextUtils.isEmpty(pText)){
            int prelim = Integer.parseInt(String.valueOf(studentModel.getpActCount()));

            if ( prelim >= 15){
                holder.pIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.safe));
            }else if (prelim > 10 && prelim < 15){
                holder.pIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.warning));
            }else{
                holder.pIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.danger));
            }

        }

        if(!TextUtils.isEmpty(mText)){
            int midterm = Integer.parseInt(String.valueOf(studentModel.getmActCount()));

            if ( midterm >= 15){
                holder.mIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.safe));
            }else if (midterm > 10 && midterm < 15){
                holder.mIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.warning));
            }else{
                holder.mIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.danger));
            }
        }

        if(!TextUtils.isEmpty(fText)){
            int finals = Integer.parseInt(String.valueOf(studentModel.getfActCount()));

            if ( finals >= 15){
                holder.fIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.safe));
            }else if (finals > 10 && finals < 15){
                holder.fIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.warning));
            }else{
                holder.fIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.danger));
            }

        }






        //   Intent intent = ((Activity) context).getIntent();
        final String classKey = GlobalVar.codeToPass;



        //message/notify student
        holder.messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageReference = rootNode.getReference("users/"+userId+"/Students/"+classKey);
                messageReference.child(studentModel.getsId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            if (task.getResult().exists()){
                                DataSnapshot mySnapshot = task.getResult();
                                String sToken = String.valueOf(mySnapshot.child("sToken").getValue());
                                String sClass = String.valueOf(mySnapshot.child("sClass").getValue());

                                notifyDialog = new Dialog(context);

                                notifyDialog.setContentView(R.layout.popup_notify);
                                notifyDialog.setCancelable(false);
                                notifyDialog.setCanceledOnTouchOutside(false);

                                TextView title = notifyDialog.findViewById(R.id.classname_notify);
                                EditText message = notifyDialog.findViewById(R.id.message_notify_et);
                                Button cancelNotifBtn = notifyDialog.findViewById(R.id.cancel_notify_btn);
                                Button notifyBtn = notifyDialog.findViewById(R.id.notify_btn);

                                title.setText(sClass);

                                notifyBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String messageText = message.getText().toString();

                                        if (sToken.equals("")){
                                            Toast.makeText(context, "This User might not have been registered", Toast.LENGTH_SHORT).show();
                                        }else if (TextUtils.isEmpty(messageText)){
                                            message.setError("Required");
                                        }else{

                                            FCMSend.pushNotification(
                                                    context,
                                                    sToken,
                                                    sClass,
                                                    messageText
                                            );


                                            notifyDialog.dismiss();

                                            Toast toast = Toast.makeText(context, "Notification sent!", Toast.LENGTH_SHORT);
                                            TextView toastMessage = (TextView) toast.getView().findViewById(android.R.id.message);
                                            toastMessage.setTextColor(Color.GREEN);
                                            toastMessage.setBackgroundColor(Color.TRANSPARENT);
                                            toast.show();


                                        }



                                    }
                                });

                                cancelNotifBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        notifyDialog.dismiss();
                                    }
                                });



                                //  Toast.makeText(StudentList.this, "You swiped swipe "+sToken, Toast.LENGTH_SHORT).show();
                                notifyDialog.show();
                            }

                        }

                    }
                });
            }
        });







//remove student
        holder.removeStudentBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                //alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                //set title
                builder.setTitle("Remove Student");
                //set message
                builder.setMessage("Are you sure you want to remove?");
                //positive yes
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.clear();

                        //   Intent intent = ((Activity) context).getIntent();
                        //  final String classKey = GlobalVar.codeToPass;

                        //String studentId = studentModel.getsId();
                        //remove class

                        loadingDialog.startLoadingDialog();
                        Handler handler = new Handler();
                        handler.postDelayed((Runnable) loadingDialog::dismissDialog,1000);

                        reference = rootNode.getReference("users/"+userId+"/Students/"+classKey);
                        gradesRef = rootNode.getReference("users/"+userId+"/Grades");
                        studentInfoRef = rootNode.getReference("studentInfo/"+studentModel.getsId()+"/Subjects");

                        //remove also from student info
                        studentInfoRef.child(classKey).removeValue();

                        // remove also the grades of that student
                        gradesRef.child(studentModel.getsId()).removeValue();


                        // remove
                        reference.child(studentModel.getsId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                notifyItemRemoved(position);



                                dialog.dismiss();

                                  Intent intent = new Intent(context, StudentList.class);
                                  intent.putExtra(StudentList.CODE,classKey);
                                   context.startActivity(intent);
                                Toast.makeText(context.getApplicationContext(), "Removed Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                        list.clear();




                    }
                });
                //negative no
                builder.setNegativeButton("NO", (dialog, which) -> {
                    //dismiss dialog
                    dialog.dismiss();
                });
                builder.create();
                builder.show();


            }
        });

        //edit scores
        holder.editScoreBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                //get class code
                // String sClass = GlobalVar.codeToPass;

                //get the sId of current item
                String studentID = studentModel.getsId();
                String sClass =studentModel.getsClass();


                dialog = new Dialog(context);

                dialog.setContentView(R.layout.popup_grading_term);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);

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


                p_a1.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                p_a2.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                p_a3.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                p_a4.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                p_a5.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                p_ass1.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                p_ass2.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                p_ass3.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                p_ass4.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                p_ass5.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                p_q1.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                p_q2.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                p_q3.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                p_q4.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                p_q5.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                p_ex1.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                p_ex2.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                p_ex3.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                p_ex4.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                p_ex5.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);


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

                m_a1.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                m_a2.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                m_a3.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                m_a4.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                m_a5.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                m_ass1.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                m_ass2.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                m_ass3.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                m_ass4.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                m_ass5.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                m_q1.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                m_q2.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                m_q3.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                m_q4.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                m_q5.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                m_ex1.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                m_ex2.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                m_ex3.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                m_ex4.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                m_ex5.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
//Midterm, dami hahahah
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

                f_a1.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                f_a2.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                f_a3.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                f_a4.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                f_a5.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                f_ass1.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                f_ass2.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                f_ass3.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                f_ass4.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                f_ass5.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                f_q1.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                f_q2.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                f_q3.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                f_q4.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                f_q5.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                f_ex1.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                f_ex2.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                f_ex3.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                f_ex4.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                f_ex5.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);




                //start of prelimBtn
                prelimBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list.clear();

                        prelimReference = rootNode.getReference("users/"+userId+"/Grades/"+studentModel.getsId()+"/"+classKey+"/Prelim");

                        if (pLayout.getVisibility() == View.VISIBLE || mLayout.getVisibility() == View.VISIBLE || fLayout.getVisibility() == View.VISIBLE){
                            pLayout.setVisibility(View.GONE);
                            mLayout.setVisibility(View.GONE);
                            fLayout.setVisibility(View.GONE);
                        }else{
                            pLayout.setVisibility(View.VISIBLE);
                        }


//start of prelim reference
                        prelimReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //start of prelim !snaphot
                                if (!snapshot.hasChildren()){
                                    //start of prelim save btn
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

                                            prelimReference = rootNode.getReference("users/"+userId+"/Grades/"+studentModel.getsId()+"/"+classKey);

                                            PrelimGradeModel prelimGradeModel = new PrelimGradeModel(
                                                    prelimAct1,prelimAct2,prelimAct3,prelimAct4,prelimAct5,
                                                    prelimAss1,prelimAss2,prelimAss3,prelimAss4,prelimAss5,
                                                    prelimQ1,prelimQ2,prelimQ3,prelimQ4,prelimQ5,
                                                    prelimE1,prelimE2,prelimE3,prelimE4,prelimE5);

                                            prelimReference.child("Prelim").setValue(prelimGradeModel);




                                            Toast.makeText(context.getApplicationContext(), "Prelim Saved", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                    //end of prelim save btn

                                }
                                //end of prelim !snaphot
                                //start of else Prelim !snapshot
                                else{
                                    prelimReference = rootNode.getReference("users/"+userId+"/Grades/"+studentModel.getsId()+"/"+classKey);

                                    prelimReference.child("Prelim").get().addOnCompleteListener(task -> {

                                        if (task.isSuccessful()){
                                            if (task.getResult().exists()){
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


                                                if (!mySnapshot.child("prelimAct1").getValue().toString().trim().equals("")){
                                                    act_done_prelim.add("prelimAct1");
                                                    p_act_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("prelimAct1").getValue().toString().trim())));
                                                }
                                                if (!mySnapshot.child("prelimAct2").getValue().toString().trim().equals("")){
                                                    act_done_prelim.add("prelimAct2");
                                                    p_act_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("prelimAct2").getValue().toString().trim())));
                                                }
                                                if (!mySnapshot.child("prelimAct3").getValue().toString().trim().equals("")){
                                                    act_done_prelim.add("prelimAct3");
                                                    p_act_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("prelimAct3").getValue().toString().trim())));
                                                }
                                                if (!mySnapshot.child("prelimAct4").getValue().toString().trim().equals("")){
                                                    act_done_prelim.add("prelimAct4");
                                                    p_act_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("prelimAct4").getValue().toString().trim())));
                                                }
                                                if (!mySnapshot.child("prelimAct5").getValue().toString().trim().equals("")){
                                                    act_done_prelim.add("prelimAct5");
                                                    p_act_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("prelimAct5").getValue().toString().trim())));
                                                }
                                                if (!mySnapshot.child("prelimAss1").getValue().toString().trim().equals("")){
                                                    act_done_prelim.add("prelimAss1");
                                                    p_ass_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("prelimAss1").getValue().toString().trim())));
                                                }
                                                if (!mySnapshot.child("prelimAss2").getValue().toString().trim().equals("")){
                                                    act_done_prelim.add("prelimAss2");
                                                    p_ass_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("prelimAss2").getValue().toString().trim())));
                                                }
                                                if (!mySnapshot.child("prelimAss3").getValue().toString().trim().equals("")){
                                                    act_done_prelim.add("prelimAss3");
                                                    p_ass_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("prelimAss3").getValue().toString().trim())));
                                                }
                                                if (!mySnapshot.child("prelimAss4").getValue().toString().trim().equals("")){
                                                    act_done_prelim.add("prelimAss4");
                                                    p_ass_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("prelimAss4").getValue().toString().trim())));
                                                }
                                                if (!mySnapshot.child("prelimAss5").getValue().toString().trim().equals("")){
                                                    act_done_prelim.add("prelimAss5");
                                                    p_ass_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("prelimAss5").getValue().toString().trim())));
                                                }
                                                if (!mySnapshot.child("prelimQ1").getValue().toString().trim().equals("")){
                                                    act_done_prelim.add("prelimQ1");
                                                    p_q_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("prelimQ1").getValue().toString().trim())));
                                                }
                                                if (!mySnapshot.child("prelimQ2").getValue().toString().trim().equals("")){
                                                    act_done_prelim.add("prelimQ2");
                                                    p_q_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("prelimQ2").getValue().toString().trim())));
                                                }
                                                if (!mySnapshot.child("prelimQ3").getValue().toString().trim().equals("")){
                                                    act_done_prelim.add("prelimQ3");
                                                    p_q_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("prelimQ3").getValue().toString().trim())));
                                                }
                                                if (!mySnapshot.child("prelimQ4").getValue().toString().trim().equals("")){
                                                    act_done_prelim.add("prelimQ4");
                                                    p_q_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("prelimQ4").getValue().toString().trim())));
                                                }
                                                if (!mySnapshot.child("prelimQ5").getValue().toString().trim().equals("")){
                                                    act_done_prelim.add("prelimQ5");
                                                    p_q_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("prelimQ5").getValue().toString().trim())));
                                                }
                                                if (!mySnapshot.child("prelimE1").getValue().toString().trim().equals("")){
                                                    act_done_prelim.add("prelimE1");
                                                    p_ex_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("prelimE1").getValue().toString().trim())));
                                                }
                                                if (!mySnapshot.child("prelimE2").getValue().toString().trim().equals("")){
                                                    act_done_prelim.add("prelimE2");
                                                    p_ex_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("prelimE2").getValue().toString().trim())));
                                                }
                                                if (!mySnapshot.child("prelimE3").getValue().toString().trim().equals("")){
                                                    act_done_prelim.add("prelimE3");
                                                    p_ex_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("prelimE3").getValue().toString().trim())));
                                                }
                                                if (!mySnapshot.child("prelimE4").getValue().toString().trim().equals("")){
                                                    act_done_prelim.add("prelimE4");
                                                    p_ex_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("prelimE4").getValue().toString().trim())));
                                                }
                                                if (!mySnapshot.child("prelimE5").getValue().toString().trim().equals("")){
                                                    act_done_prelim.add("prelimE5");
                                                    p_ex_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("prelimE5").getValue().toString().trim())));
                                                }

                                                float p_act_sum = 0.0f;
                                                float p_ass_sum=0.0f;
                                                float p_q_sum = 0.0f;
                                                float p_ex_sum = 0.0f;


                                                for (int i = 0; i < p_act_ave.size(); i++){
                                                    p_act_sum += p_act_ave.get(i);
                                                }


                                                for (int i = 0; i < p_ass_ave.size(); i++){
                                                    p_ass_sum += p_ass_ave.get(i);
                                                }


                                                for (int i = 0; i < p_q_ave.size(); i++){
                                                    p_q_sum += p_q_ave.get(i);
                                                }

                                                for (int i = 0; i < p_ex_ave.size(); i++){
                                                    p_ex_sum += p_ex_ave.get(i);
                                                }


                                               /* float p_act_average = p_act_sum /p_act_ave.size();
                                                String sp_act_average = df.format(p_act_average);

                                                float p_ass_average = p_ass_sum /p_ass_ave.size();
                                                String sp_ass_average = df.format(p_ass_average);

                                                float p_q_average = p_q_sum /p_q_ave.size();
                                                String sp_q_average = df.format(p_q_average);


                                                float p_ex_average = p_ex_sum /p_ex_ave.size();
                                                String sp_ex_average = df.format(p_ex_average);

                                                float p_overall_sum_av = p_act_average + p_ass_average + p_q_average + p_ex_average;

                                                float p_overall_average = p_overall_sum_av/4;
                                                overall_average.add(p_overall_average);
                                                String sp_overall_average = df.format(p_overall_average);*/




                                               /* sendAveragePrelim(sp_act_average,sp_ass_average,sp_q_average,sp_ex_average,sp_overall_average,act_done_prelim);
*/
                                                sendAveragePrelim(p_act_sum,p_ass_sum,p_q_sum,p_ex_sum,null,act_done_prelim);






                                                
/*
                                                float finalP_act_sum = p_act_sum;
                                                float finalP_ass_sum = p_ass_sum;
                                                float finalP_q_sum = p_q_sum;
                                                float finalP_ex_sum = p_ex_sum;
                                                */

                                               /* sendListPrelim(act_done_prelim);*/

                                                prelimSaveBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v1) {

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

                                                        prelimReference = rootNode.getReference("users/"+userId+"/Grades/"+studentModel.getsId()+"/"+classKey);

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
                                                    }
                                                });
                                                //end of else prelim save btn

                                            }else { Toast.makeText(context.getApplicationContext(), "No Score Yet", Toast.LENGTH_SHORT).show();}
                                        }else{ Toast.makeText(context.getApplicationContext(), "There is some error", Toast.LENGTH_SHORT).show(); }

                                    });

                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
//end of prelim reference
                    }
                });
//end of prelimBtn


                //start of midtermBtn
                midtermBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list.clear();

                        midtermReference = rootNode.getReference("users/"+userId+"/Grades/"+studentModel.getsId()+"/"+classKey+"/Midterm");

                        if (pLayout.getVisibility() == View.VISIBLE || mLayout.getVisibility() == View.VISIBLE || fLayout.getVisibility() == View.VISIBLE){
                            pLayout.setVisibility(View.GONE);
                            mLayout.setVisibility(View.GONE);
                            fLayout.setVisibility(View.GONE);
                        }else{
                            mLayout.setVisibility(View.VISIBLE);
                        }

//start of midterm reference
                        midtermReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //start of prelim !snaphot
                                if (!snapshot.hasChildren()){
                                    //start of prelim save btn
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

                                            midtermReference = rootNode.getReference("users/"+userId+"/Grades/"+studentModel.getsId()+"/"+classKey);

                                            MidtermGradeModel midtermGradeModel = new MidtermGradeModel(
                                                    midtermAct1,midtermAct2,midtermAct3,midtermAct4,midtermAct5,
                                                    midtermAss1,midtermAss2,midtermAss3,midtermAss4,midtermAss5,
                                                    midtermQ1,midtermQ2,midtermQ3,midtermQ4,midtermQ5,
                                                    midtermE1,midtermE2,midtermE3,midtermE4,midtermE5);

                                            midtermReference.child("Midterm").setValue(midtermGradeModel);


                                            Toast.makeText(context.getApplicationContext(), "Midterm Saved", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                    //end of prelim save btn

                                }
                                //end of prelim !snaphot
                                //start of else Prelim !snapshot
                                else{
                                    midtermReference = rootNode.getReference("users/"+userId+"/Grades/"+studentModel.getsId()+"/"+classKey);

                                    midtermReference.child("Midterm").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {

                                            if (task.isSuccessful()){
                                                if (task.getResult().exists()){
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

                                                    if (!mySnapshot.child("midtermAct1").getValue().toString().trim().equals("")){
                                                        act_done_midterm.add("midtermAct1");
                                                        m_act_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("midtermAct1").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("midtermAct2").getValue().toString().trim().equals("")){
                                                        act_done_midterm.add("midtermAct2");
                                                        m_act_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("midtermAct2").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("midtermAct3").getValue().toString().trim().equals("")){
                                                        act_done_midterm.add("midtermAct3");
                                                        m_act_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("midtermAct3").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("midtermAct4").getValue().toString().trim().equals("")){
                                                        act_done_midterm.add("midtermAct4");
                                                        m_act_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("midtermAct4").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("midtermAct5").getValue().toString().trim().equals("")){
                                                        act_done_midterm.add("midtermAct5");
                                                        m_act_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("midtermAct5").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("midtermAss1").getValue().toString().trim().equals("")){
                                                        act_done_midterm.add("midtermAss1");
                                                        m_ass_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("midtermAss1").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("midtermAss2").getValue().toString().trim().equals("")){
                                                        act_done_midterm.add("midtermAss2");
                                                        m_ass_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("midtermAss2").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("midtermAss3").getValue().toString().trim().equals("")){
                                                        act_done_midterm.add("midtermAss3");
                                                        m_ass_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("midtermAss3").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("midtermAss4").getValue().toString().trim().equals("")){
                                                        act_done_midterm.add("midtermAss4");
                                                        m_ass_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("midtermAss4").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("midtermAss5").getValue().toString().trim().equals("")){
                                                        act_done_midterm.add("midtermAss5");
                                                        m_ass_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("midtermAss5").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("midtermQ1").getValue().toString().trim().equals("")){
                                                        act_done_midterm.add("midtermQ1");
                                                        m_q_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("midtermQ1").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("midtermQ2").getValue().toString().trim().equals("")){
                                                        act_done_midterm.add("midtermQ2");
                                                        m_q_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("midtermQ2").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("midtermQ3").getValue().toString().trim().equals("")){
                                                        act_done_midterm.add("midtermQ3");
                                                        m_q_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("midtermQ3").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("midtermQ4").getValue().toString().trim().equals("")){
                                                        act_done_midterm.add("midtermQ4");
                                                        m_q_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("midtermQ4").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("midtermQ5").getValue().toString().trim().equals("")){
                                                        act_done_midterm.add("midtermQ5");
                                                        m_q_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("midtermQ5").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("midtermE1").getValue().toString().trim().equals("")){
                                                        act_done_midterm.add("midtermE1");
                                                        m_ex_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("midtermE1").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("midtermE2").getValue().toString().trim().equals("")){
                                                        act_done_midterm.add("midtermE2");
                                                        m_ex_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("midtermE2").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("midtermE3").getValue().toString().trim().equals("")){
                                                        act_done_midterm.add("midtermE3");
                                                        m_ex_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("midtermE3").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("midtermE4").getValue().toString().trim().equals("")){
                                                        act_done_midterm.add("midtermE4");
                                                        m_ex_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("midtermE4").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("midtermE5").getValue().toString().trim().equals("")){
                                                        act_done_midterm.add("midtermE5");
                                                        m_ex_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("midtermE5").getValue().toString().trim())));
                                                    }

                                                    float m_act_sum = 0.0f;
                                                    float m_ass_sum=0.0f;
                                                    float m_q_sum = 0.0f;
                                                    float m_ex_sum = 0.0f;


                                                    for (int i = 0; i < m_act_ave.size(); i++){
                                                        m_act_sum += m_act_ave.get(i);
                                                    }


                                                    for (int i = 0; i < m_ass_ave.size(); i++){
                                                        m_ass_sum += m_ass_ave.get(i);
                                                    }


                                                    for (int i = 0; i < m_q_ave.size(); i++){
                                                        m_q_sum += m_q_ave.get(i);
                                                    }

                                                    for (int i = 0; i < m_ex_ave.size(); i++){
                                                        m_ex_sum += m_ex_ave.get(i);
                                                    }

/*

                                                    float m_act_average = m_act_sum /m_act_ave.size();
                                                    String sm_act_average = df.format(m_act_average);

                                                    float m_ass_average = m_ass_sum /m_ass_ave.size();
                                                    String sm_ass_average = df.format(m_ass_average);

                                                    float m_q_average = m_q_sum /m_q_ave.size();
                                                    String sm_q_average = df.format(m_q_average);


                                                    float m_ex_average = m_ex_sum /m_ex_ave.size();
                                                    String sm_ex_average = df.format(m_ex_average);

                                                    float m_overall_sum_av = m_act_average + m_ass_average + m_q_average + m_ex_average;

                                                    float m_overall_average = m_overall_sum_av/4;
                                                    overall_average.add(m_overall_average);
                                                    String sm_overall_average = df.format(m_overall_average);
*/



                                                  /*  sendAverageMidterm(sm_act_average,sm_ass_average,sm_q_average,sm_ex_average,sm_overall_average,act_done_midterm);
*/
                                                    sendAverageMidterm(m_act_sum,m_ass_sum,m_q_sum,m_ex_sum,null,act_done_midterm);



                                                   /* sendListMidterm(act_done_midterm);*/

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

                                                            midtermReference = rootNode.getReference("users/"+userId+"/Grades/"+studentModel.getsId()+"/"+classKey);

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
//end of midterm reference
                    }
                });
//end of midtermBtn



//start of finalBtn
                finalBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list.clear();

                        finalsReference = rootNode.getReference("users/"+userId+"/Grades/"+studentModel.getsId()+"/"+classKey+"/Finals");

                        if (pLayout.getVisibility() == View.VISIBLE || mLayout.getVisibility() == View.VISIBLE || fLayout.getVisibility() == View.VISIBLE){
                            pLayout.setVisibility(View.GONE);
                            mLayout.setVisibility(View.GONE);
                            fLayout.setVisibility(View.GONE);
                        }else{
                            fLayout.setVisibility(View.VISIBLE);
                        }

//start of final reference
                        finalsReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //start of final !snaphot
                                if (!snapshot.hasChildren()){
                                    //start of final save btn
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

                                            finalsReference = rootNode.getReference("users/"+userId+"/Grades/"+studentModel.getsId()+"/"+classKey);

                                            FinalsGradeModel finalsGradeModel = new FinalsGradeModel(
                                                    finalsAct1,finalsAct2,finalsAct3,finalsAct4,finalsAct5,
                                                    finalsAss1,finalsAss2,finalsAss3,finalsAss4,finalsAss5,
                                                    finalsQ1,finalsQ2,finalsQ3,finalsQ4,finalsQ5,
                                                    finalsE1,finalsE2,finalsE3,finalsE4,finalsE5);

                                            finalsReference.child("Finals").setValue(finalsGradeModel);



                                            Toast.makeText(context.getApplicationContext(), "Finals Saved", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                    //end of finals save btn

                                }
                                //end of finals !snaphot
                                //start of else finals !snapshot
                                else{
                                    finalsReference = rootNode.getReference("users/"+userId+"/Grades/"+studentModel.getsId()+"/"+classKey);

                                    finalsReference.child("Finals").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {

                                            if (task.isSuccessful()){
                                                if (task.getResult().exists()){
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


                                                    if (!mySnapshot.child("finalsAct1").getValue().toString().trim().equals("")){
                                                        act_done_finals.add("finalsAct1");
                                                        f_act_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("finalsAct1").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("finalsAct2").getValue().toString().trim().equals("")){
                                                        act_done_finals.add("finalsAct2");
                                                        f_act_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("finalsAct2").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("finalsAct3").getValue().toString().trim().equals("")){
                                                        act_done_finals.add("finalsAct3");
                                                        f_act_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("finalsAct3").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("finalsAct4").getValue().toString().trim().equals("")){
                                                        act_done_finals.add("finalsAct4");
                                                        f_act_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("finalsAct4").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("finalsAct5").getValue().toString().trim().equals("")){
                                                        act_done_finals.add("finalsAct5");
                                                        f_act_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("finalsAct5").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("finalsAss1").getValue().toString().trim().equals("")){
                                                        act_done_finals.add("finalsAss1");
                                                        f_ass_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("finalsAss1").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("finalsAss2").getValue().toString().trim().equals("")){
                                                        act_done_finals.add("finalsAss2");
                                                        f_ass_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("finalsAss2").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("finalsAss3").getValue().toString().trim().equals("")){
                                                        act_done_finals.add("finalsAss3");
                                                        f_ass_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("finalsAss3").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("finalsAss4").getValue().toString().trim().equals("")){
                                                        act_done_finals.add("finalsAss4");
                                                        f_ass_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("finalsAss4").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("finalsAss5").getValue().toString().trim().equals("")){
                                                        act_done_finals.add("finalsAss5");
                                                        f_ass_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("finalsAss5").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("finalsQ1").getValue().toString().trim().equals("")){
                                                        act_done_finals.add("finalsQ1");
                                                        f_q_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("finalsQ1").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("finalsQ2").getValue().toString().trim().equals("")){
                                                        act_done_finals.add("finalsQ2");
                                                        f_q_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("finalsQ2").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("finalsQ3").getValue().toString().trim().equals("")){
                                                        act_done_finals.add("finalsQ3");
                                                        f_q_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("finalsQ3").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("finalsQ4").getValue().toString().trim().equals("")){
                                                        act_done_finals.add("finalsQ4");
                                                        f_q_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("finalsQ4").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("finalsQ5").getValue().toString().trim().equals("")){
                                                        act_done_finals.add("finalsQ5");
                                                        f_q_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("finalsQ5").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("finalsE1").getValue().toString().trim().equals("")){
                                                        act_done_finals.add("finalsE1");
                                                        f_ex_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("finalsE1").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("finalsE2").getValue().toString().trim().equals("")){
                                                        act_done_finals.add("finalsE2");
                                                        f_ex_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("finalsE2").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("finalsE3").getValue().toString().trim().equals("")){
                                                        act_done_finals.add("finalsE3");
                                                        f_ex_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("finalsE3").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("finalsE4").getValue().toString().trim().equals("")){
                                                        act_done_finals.add("finalsE4");
                                                        f_ex_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("finalsE4").getValue().toString().trim())));
                                                    }
                                                    if (!mySnapshot.child("finalsE5").getValue().toString().trim().equals("")){
                                                        act_done_finals.add("finalsE5");
                                                        f_ex_ave.add(Integer.parseInt(String.valueOf(mySnapshot.child("finalsE5").getValue().toString().trim())));
                                                    }

                                                    float f_act_sum = 0.0f;
                                                    float f_ass_sum=0.0f;
                                                    float f_q_sum = 0.0f;
                                                    float f_ex_sum = 0.0f;


                                                    for (int i = 0; i < f_act_ave.size(); i++){
                                                        f_act_sum += f_act_ave.get(i);
                                                    }


                                                    for (int i = 0; i < f_ass_ave.size(); i++){
                                                        f_ass_sum += f_ass_ave.get(i);
                                                    }


                                                    for (int i = 0; i < f_q_ave.size(); i++){
                                                        f_q_sum += f_q_ave.get(i);
                                                    }

                                                    for (int i = 0; i < f_ex_ave.size(); i++){
                                                        f_ex_sum += f_ex_ave.get(i);
                                                    }

/*
                                                    float f_act_average = f_act_sum /f_act_ave.size();
                                                    String sf_act_average = df.format(f_act_average);

                                                    float f_ass_average = f_ass_sum /f_ass_ave.size();
                                                    String sf_ass_average = df.format(f_ass_average);

                                                    float f_q_average = f_q_sum /f_q_ave.size();
                                                    String sf_q_average = df.format(f_q_average);


                                                    float f_ex_average = f_ex_sum /f_ex_ave.size();
                                                    String sf_ex_average = df.format(f_ex_average);

                                                    float f_overall_sum_av = f_act_average + f_ass_average + f_q_average + f_ex_average;

                                                    float f_overall_average = f_overall_sum_av/4;
                                                    overall_average.add(f_overall_average);
                                                    String sf_overall_average = df.format(f_overall_average);*/


                                                   /* sendAverageFinals(sf_act_average,sf_ass_average,sf_q_average,sf_ex_average,sf_overall_average,act_done_finals);
*/
                                                    sendAverageFinals(f_act_sum,f_ass_sum,f_q_sum,f_ex_sum,null,act_done_finals);

                                                   /* sendListFinals(act_done_finals);*/



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

                                                            finalsReference = rootNode.getReference("users/"+userId+"/Grades/"+studentModel.getsId()+"/"+classKey);

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
//end of finals reference
                    }
                });
//end of finalBtn

                closePopUpBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        //overall comment
                     /*   float overall_ave_sum = 0.0f;


                        for (int i = 0; i < overall_average.size(); i++){
                            overall_ave_sum += overall_average.get(i);
                        }

                        float final_average = overall_ave_sum /3;
                        String sfinal_average = df.format(final_average);

                        reference = rootNode.getReference("users/"+userId+"/Students/"+classKey+"/"+studentModel.getsId());
                        reference.child("overallAverage").setValue(sfinal_average);
                        overall_average.clear();*/



                        Intent intent = new Intent(context, StudentList.class);
                        intent.putExtra("pActTotal",pActTotal);
                        intent.putExtra("pAssTotal",pAssTotal);
                        intent.putExtra("pQTotal",pQTotal);
                        intent.putExtra("pExTotal",pExTotal);

                        intent.putExtra("mActTotal",mActTotal);
                        intent.putExtra("mAssTotal",mAssTotal);
                        intent.putExtra("mQTotal",mQTotal);
                        intent.putExtra("mExTotal",mExTotal);

                        intent.putExtra("fActTotal",fActTotal);
                        intent.putExtra("fAssTotal",fAssTotal);
                        intent.putExtra("fQTotal",fQTotal);
                        intent.putExtra("fExTotal",fExTotal);
                        context.startActivity(intent);
                        ((Activity) context).finishAffinity();
                        notifyDataSetChanged();
                        dialog.dismiss();


                    }
                });



                dialog.show();

            }

            private void sendAveragePrelim(float p_act_average, float p_ass_average, float p_q_average, float p_ex_average, String p_overall_average, ArrayList<String> act_done_prelim) {
                reference = rootNode.getReference("users/"+userId+"/Students/"+classKey+"/"+studentModel.getsId());
                reference.child("pActAverage").setValue(String.valueOf(p_act_average));
                reference.child("pAssAverage").setValue(String.valueOf(p_ass_average));
                reference.child("pQAverage").setValue(String.valueOf(p_q_average));
                reference.child("pEAverage").setValue(String.valueOf(p_ex_average));
                reference.child("pOverAllAverage").setValue("null");
                reference.child("pActCount").setValue(String.valueOf(act_done_prelim.size()));
                p_act_ave.clear();
                p_ass_ave.clear();
                p_q_ave.clear();
                p_ex_ave.clear();
                p_average.clear();
                act_done_prelim.clear();
                notifyDataSetChanged();

                return;

            }
            private void sendAverageMidterm(float m_act_average, float m_ass_average, float m_q_average, float m_ex_average, String m_overall_average, ArrayList<String> act_done_prelim) {
                reference = rootNode.getReference("users/"+userId+"/Students/"+classKey+"/"+studentModel.getsId());
                reference.child("mActAverage").setValue(String.valueOf(m_act_average));
                reference.child("mAssAverage").setValue(String.valueOf(m_ass_average));
                reference.child("mQAverage").setValue(String.valueOf(m_q_average));
                reference.child("mEAverage").setValue(String.valueOf(m_ex_average));
                reference.child("mOverAllAverage").setValue(String.valueOf(m_overall_average));
                reference.child("mActCount").setValue(String.valueOf(act_done_midterm.size()));
                m_act_ave.clear();
                m_ass_ave.clear();
                m_q_ave.clear();
                m_ex_ave.clear();
                m_average.clear();
                act_done_midterm.clear();
                notifyDataSetChanged();

                return;

            }
            private void sendAverageFinals(float f_act_average, float f_ass_average, float f_q_average, float f_ex_average, String f_overall_average, ArrayList<String> act_done_finals) {
                reference = rootNode.getReference("users/"+userId+"/Students/"+classKey+"/"+studentModel.getsId());
                reference.child("fActAverage").setValue(String.valueOf(f_act_average));
                reference.child("fAssAverage").setValue(String.valueOf(f_ass_average));
                reference.child("fQAverage").setValue(String.valueOf(f_q_average));
                reference.child("fEAverage").setValue(String.valueOf(f_ex_average));
                reference.child("fOverAllAverage").setValue(String.valueOf(f_overall_average));
                reference.child("fActCount").setValue(String.valueOf(act_done_finals.size()));
                f_act_ave.clear();
                f_ass_ave.clear();
                f_q_ave.clear();
                f_ex_ave.clear();
                f_average.clear();
                act_done_finals.clear();
                notifyDataSetChanged();

                return;

            }




        });
//end of edit score onclick
       

        holder.averageBtn.setOnClickListener(v -> {


            if (!TextUtils.isEmpty(pActTotals) ||  !TextUtils.isEmpty(pAssTotals) || !TextUtils.isEmpty(pQTotals) || !TextUtils.isEmpty(pExTotals)){
                goToDialog(pActTotals,pAssTotals,pQTotals,pExTotals,mActTotals,mAssTotals,mQTotals,mExTotals,fActTotals,fAssTotal,fQTotals,fExTotals,
                        pActTotal,pAssTotal,pQTotal,pExTotal,mActTotal,mAssTotal,mQTotal,mExTotal,fActTotal,fAssTotal,fQTotal,fExTotal,
                        studentModel.getpActAverage(),studentModel.getpAssAverage(),studentModel.getpQAverage(),studentModel.getpEAverage(),studentModel.getpOverAllAverage(),
                        studentModel.getmActAverage(),studentModel.getmAssAverage(),studentModel.getmQAverage(),studentModel.getmEAverage(),studentModel.getmOverAllAverage(),
                        studentModel.getfActAverage(),studentModel.getfAssAverage(),studentModel.getfQAverage(),studentModel.getfEAverage(),studentModel.getfOverAllAverage());
            }else if (!TextUtils.isEmpty(mActTotals) ||  !TextUtils.isEmpty(mAssTotals) || !TextUtils.isEmpty(mQTotals) || !TextUtils.isEmpty(mExTotals)){
                goToDialog(pActTotals,pAssTotals,pQTotals,pExTotals,mActTotals,mAssTotals,mQTotals,mExTotals,fActTotals,fAssTotal,fQTotals,fExTotals,
                        pActTotal,pAssTotal,pQTotal,pExTotal,mActTotal,mAssTotal,mQTotal,mExTotal,fActTotal,fAssTotal,fQTotal,fExTotal,
                        studentModel.getpActAverage(),studentModel.getpAssAverage(),studentModel.getpQAverage(),studentModel.getpEAverage(),studentModel.getpOverAllAverage(),
                        studentModel.getmActAverage(),studentModel.getmAssAverage(),studentModel.getmQAverage(),studentModel.getmEAverage(),studentModel.getmOverAllAverage(),
                        studentModel.getfActAverage(),studentModel.getfAssAverage(),studentModel.getfQAverage(),studentModel.getfEAverage(),studentModel.getfOverAllAverage());
            }else if (!TextUtils.isEmpty(fActTotals) ||  !TextUtils.isEmpty(fAssTotals) || !TextUtils.isEmpty(fQTotals) || !TextUtils.isEmpty(fExTotals)){
                goToDialog(pActTotals,pAssTotals,pQTotals,pExTotals,mActTotals,mAssTotals,mQTotals,mExTotals,fActTotals,fAssTotal,fQTotals,fExTotals,
                        pActTotal,pAssTotal,pQTotal,pExTotal,mActTotal,mAssTotal,mQTotal,mExTotal,fActTotal,fAssTotal,fQTotal,fExTotal,
                        studentModel.getpActAverage(),studentModel.getpAssAverage(),studentModel.getpQAverage(),studentModel.getpEAverage(),studentModel.getpOverAllAverage(),
                        studentModel.getmActAverage(),studentModel.getmAssAverage(),studentModel.getmQAverage(),studentModel.getmEAverage(),studentModel.getmOverAllAverage(),
                        studentModel.getfActAverage(),studentModel.getfAssAverage(),studentModel.getfQAverage(),studentModel.getfEAverage(),studentModel.getfOverAllAverage());
            }else {
                Toast.makeText(context, "Go Back to the Subject section and click the folder icon", Toast.LENGTH_LONG).show();
            }


        });
        
   
    }


    private void goToDialog(String pActTotals, String pAssTotals, String pQTotals, String pExTotals, String mActTotals, String mAssTotals, String mQTotals, String mExTotals, String fActTotals, String fAssTotals, String fQTotals, String fExTotals,String pActTotal, String pAssTotal, String pQTotal, String pExTotal, String mActTotal, String mAssTotal, String mQTotal, String mExTotal, String fActTotal, String fAssTotal, String fQTotal, String fExTotal, String getpActAverage, String getpAssAverage, String getpQAverage, String getpEAverage, String getpOverAllAverage, String getmActAverage, String getmAssAverage, String getmQAverage, String getmEAverage, String getmOverAllAverage, String getfActAverage, String getfAssAverage, String getfQAverage, String getfEAverage, String getfOverAllAverage) {
        if (TextUtils.isEmpty(pActTotal)){
            pActTotal="0.0";
        }
        if (TextUtils.isEmpty(pAssTotal)){
            pAssTotal="0.0";
        }
        if (TextUtils.isEmpty(pQTotal)){
            pQTotal="0.0";
        }
        if (TextUtils.isEmpty(pExTotal)){
            pExTotal="0.0";
        }
        if (TextUtils.isEmpty(mActTotal)){
            mActTotal="0.0";
        }
        if (TextUtils.isEmpty(mAssTotal)){
            mAssTotal="0.0";
        }
        if (TextUtils.isEmpty(mQTotal)){
            mQTotal="0.0";
        }
        if (TextUtils.isEmpty(mExTotal)){
            mExTotal="0.0";
        }
        if (TextUtils.isEmpty(fActTotal)){
            fActTotal="0.0";
        }
        if (TextUtils.isEmpty(fAssTotal)){
            fAssTotal="0.0";
        }
        if (TextUtils.isEmpty(fQTotal)){
            fQTotal="0.0";
        }
        if (TextUtils.isEmpty(fExTotal)){
            fExTotal="0.0";
        }

        if (TextUtils.isEmpty(pActTotals)){
            pActTotals=pActTotal;
        }
        if (TextUtils.isEmpty(pAssTotals)){
            pAssTotals=pAssTotal;
        }
        if (TextUtils.isEmpty(pQTotals)){
            pQTotals=pQTotal;
        }
        if (TextUtils.isEmpty(pExTotals)){
            pExTotals=pExTotal;
        }
        if (TextUtils.isEmpty(mActTotals)){
            mActTotals=mActTotal;
        }
        if (TextUtils.isEmpty(mAssTotals)){
            mAssTotals=mAssTotal;
        }
        if (TextUtils.isEmpty(mQTotals)){
            mQTotals=mQTotal;
        }
        if (TextUtils.isEmpty(mExTotals)){
            mExTotals=mExTotal;
        }
        if (TextUtils.isEmpty(fActTotals)){
            fActTotals=fActTotal;
        }
        if (TextUtils.isEmpty(fAssTotals)){
            fAssTotals=fAssTotal;
        }
        if (TextUtils.isEmpty(fQTotals)){
            fQTotals=fQTotal;
        }
        if (TextUtils.isEmpty(fExTotals)){
            fExTotals=fExTotal;
        }

        float p_act_average = Float.parseFloat(getpActAverage) /Float.parseFloat(pActTotals);
        String sp_act_average = df.format(p_act_average);

        float p_ass_average = Float.parseFloat(getpAssAverage) /Float.parseFloat(pAssTotals);
        String sp_ass_average = df.format(p_ass_average);

        float p_q_average = Float.parseFloat(getpQAverage) /Float.parseFloat(pQTotals);
        String sp_q_average = df.format(p_q_average);


        float p_ex_average = Float.parseFloat(getpEAverage) /Float.parseFloat(pExTotals);
        String sp_ex_average = df.format(p_ex_average);

        float p_overall_av = (p_act_average + p_ass_average + p_q_average + p_ex_average)/4;
        String sp_overall_av = df.format(p_overall_av);








        float m_act_average = Float.parseFloat(getmActAverage) /Float.parseFloat(mActTotals);
        String sm_act_average = df.format(m_act_average);

        float m_ass_average = Float.parseFloat(getmAssAverage) /Float.parseFloat(mAssTotals);
        String sm_ass_average = df.format(m_ass_average);

        float m_q_average = Float.parseFloat(getmQAverage) /Float.parseFloat(mQTotals);
        String sm_q_average = df.format(m_q_average);

        float m_ex_average = Float.parseFloat(getmEAverage) /Float.parseFloat(mExTotals);
        String sm_ex_average = df.format(m_ex_average);

        float m_overall_av = (m_act_average + m_ass_average + m_q_average + m_ex_average)/4;
        String sm_overall_av= df.format(m_overall_av);









        float f_act_average = Float.parseFloat(getfActAverage) /Float.parseFloat(fActTotals);
        String sf_act_average = df.format(f_act_average);

        float f_ass_average = Float.parseFloat(getfAssAverage) /Float.parseFloat(fAssTotals);
        String sf_ass_average = df.format(f_ass_average);

        float f_q_average = Float.parseFloat(getfQAverage) /Float.parseFloat(fQTotals);
        String sf_q_average = df.format(f_q_average);

        float f_ex_average = Float.parseFloat(getfEAverage) /Float.parseFloat(fExTotals);
        String sf_ex_average = df.format(f_ex_average);

        float f_overall_av = (f_act_average + f_ass_average + f_q_average + f_ex_average)/4;
        String sf_overall_av= df.format(f_overall_av);


        float final_average = (p_overall_av + m_overall_av + f_overall_av) / 3;
        String sfinal_average= df.format(final_average);




        dialog1 = new Dialog(context);

        dialog1.setContentView(R.layout.average_popup);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView p_act = dialog1.findViewById(R.id.p_act_ave);
        TextView p_ass = dialog1.findViewById(R.id.p_ass_ave);
        TextView p_q = dialog1.findViewById(R.id.p_q_ave);
        TextView p_ex = dialog1.findViewById(R.id.p_ex_ave);
        TextView p_final = dialog1.findViewById(R.id.p_ave);

        TextView m_act = dialog1.findViewById(R.id.m_act_ave);
        TextView m_ass = dialog1.findViewById(R.id.m_ass_ave);
        TextView m_q = dialog1.findViewById(R.id.m_q_ave);
        TextView m_ex = dialog1.findViewById(R.id.m_ex_ave);
        TextView m_final = dialog1.findViewById(R.id.m_ave);

        TextView f_act = dialog1.findViewById(R.id.f_act_ave);
        TextView f_ass = dialog1.findViewById(R.id.f_ass_ave);
        TextView f_q = dialog1.findViewById(R.id.f_q_ave);
        TextView f_ex = dialog1.findViewById(R.id.f_ex_ave);
        TextView f_final = dialog1.findViewById(R.id.f_ave);

        TextView overall = dialog1.findViewById(R.id.overall_ave);

        p_act.setText( sp_act_average);
        p_ass.setText( sp_ass_average);

        p_q.setText( sp_q_average);
        p_ex.setText( sp_ex_average);
        p_final.setText(sp_overall_av);

        m_act.setText( sm_act_average);
        m_ass.setText( sm_ass_average);
        m_q.setText( sm_q_average);
        m_ex.setText( sm_ex_average);
        m_final.setText(sm_overall_av);

        f_act.setText( sf_act_average);
        f_ass.setText( sf_ass_average);
        f_q.setText( sf_q_average);
        f_ex.setText( sf_ex_average);
        f_final.setText(sf_overall_av);

        overall.setText(sfinal_average);

        Button okBtn = dialog1.findViewById(R.id.average_close);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

        dialog1.show();

    }


   


    private void indicates() {
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(ArrayList<StudentModel> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView sName,sId,pActCount,mActCount,fActCount;
        ImageButton removeStudentBtn,editScoreBtn,averageBtn;
        ImageButton messageBtn;
        View pIndicator,mIndicator,fIndicator;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            sName = itemView.findViewById(R.id.student_name_display);
            sId = itemView.findViewById(R.id.student_id_display);
            removeStudentBtn = itemView.findViewById(R.id.remove_student);
            editScoreBtn = itemView.findViewById(R.id.edit_scores);
            messageBtn = itemView.findViewById(R.id.message_btn);
            pActCount = itemView.findViewById(R.id.preCount);
            mActCount = itemView.findViewById(R.id.midCount);
            fActCount = itemView.findViewById(R.id.finCount);
            pIndicator = itemView.findViewById(R.id.prelimIndicator);
            mIndicator = itemView.findViewById(R.id.midtermIndicator);
            fIndicator = itemView.findViewById(R.id.finalsIndicator);
            averageBtn = itemView.findViewById(R.id.average_scores);

        }


    }

    @Override
    public long getItemId(int position) {
        return (position);
    }

    @Override
    public int getItemViewType(int position) {
        return (position);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }
}
