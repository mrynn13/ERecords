package com.example.erecordsv2;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.WriterException;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;


public class StudentList extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    //initialize variable
    DrawerLayout drawerLayout;
    TextView aboutText, noStudent,teacherCode;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog1;
    Button okBtn,saveBtn,cancelBtn,copyCode,generate,saveQr;
    ImageButton add;
    EditText student_code,student_name,student_phone,searchFilter;
    EditText message;
    TextView title;
    Button cancelNotifBtn, notifyBtn;
    TextView title_toolbar;
    ImageView qrImg;


    Dialog dialog,notifyDialog;


    //firebase
    FirebaseAuth fAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference,studentRef,messageReference;
    String userId;
    LinearLayout cal;

    //RecycleView
    RecyclerView recyclerView;
    StudentAdapter studentAdapter;
    ArrayList<StudentModel>list;

    QRGEncoder qrgEncoder;
    Bitmap bitmap;
    //String savePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/QRCode/";


    private static String serverKey = "AAAAaDFE0Vw:APA91bHBeNzrKUGFkOWCd_Jl8unV2UPW6dpNgElw-yZ8QHRuEHzOD0OaDGbzqNAaUb9-TaxBEibh8vjxS4CI9Sm-gLu5ImnAQWwQKvAfdLqcgzKBLULvDcR7GOLiA6Ej9a1EqZNG_6_0";



    public static final String CODE = "CODE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_list);

        //assign by its id
        drawerLayout = findViewById(R.id.drawer_layout);
        add          = findViewById(R.id.add_student);
        dialog = new Dialog(this);
        fAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance();
        recyclerView = findViewById(R.id.recycle_view);
        noStudent = findViewById(R.id.no_student_tv);
        searchFilter = findViewById(R.id.search_view);
        teacherCode = findViewById(R.id.teacher_code);
        copyCode = findViewById(R.id.copy_btn);
        notifyDialog = new Dialog(this);
        title_toolbar = findViewById(R.id.toolbar_title);
        generate = findViewById(R.id.generate_btn);
        saveQr = findViewById(R.id.saveQR);
        qrImg = findViewById(R.id.qrImge);

        String className = this.getClass().getSimpleName();
        cal = findViewById(R.id.calendar);
        cal.setVisibility(View.VISIBLE);
        if(bitmap == null){
            saveQr.setVisibility(View.GONE);
        }

        if (className.equalsIgnoreCase("StudentList")){
            title_toolbar.setText("Students");
        }

        Intent data = getIntent();

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
        LoadingDialog loadingDialog = new LoadingDialog( StudentList.this);

        //getId in database
        userId = fAuth.getCurrentUser().getUid();

        //set code for room
        String roomCode = userId;
        teacherCode.setText(roomCode);

        //search functionality
        searchFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });



        //getintent
      //  Intent getString = getIntent();
       // String thisCode = getString.getStringExtra("CODE");

      // final String classsCode =thisCode;
        final String classCode =GlobalVar.codeToPass;
       // String studentNo = studentModel.getsId();




        //recycleview

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);


        list = new ArrayList<>();





        reference = rootNode.getReference("users/"+userId+"/Students/"+classCode);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    noStudent.setVisibility(View.INVISIBLE);
                }
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                    StudentModel sModel = dataSnapshot.getValue(StudentModel.class);
                    list.add(sModel);

                }
                studentAdapter = new StudentAdapter(StudentList.this,list,pActTotal,pAssTotal,pQTotal,pExTotal,
                        mActTotal,mAssTotal,mQTotal,mExTotal,fActTotal,fAssTotal,fQTotal,fExTotal);

                recyclerView.setAdapter(studentAdapter);


                studentAdapter.notifyDataSetChanged();

                if (studentAdapter.getItemCount()==0|| list.size()==0){
                    noStudent.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        copyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("TextCode", roomCode);
                clipboard.setPrimaryClip(clip);

                Toast.makeText( StudentList.this, "Copied", Toast.LENGTH_SHORT).show();

            }
        });

        generate.setOnClickListener(v->{
            qrgEncoder = new QRGEncoder(roomCode,null, QRGContents.Type.TEXT,200);
            try {
                bitmap = qrgEncoder.encodeAsBitmap();
                qrImg.setImageBitmap(bitmap);
                saveQr.setVisibility(View.VISIBLE);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        });

        saveQr.setOnClickListener(v->{
            boolean save;
            String result;
            int myVersion = Build.VERSION.SDK_INT;
            if(myVersion > Build.VERSION_CODES.LOLLIPOP_MR1){
                if (!checkIfAlreadyHavePermiss()){
                    requestSpecificPermision();
                }else{
                    try {
//                       Environment.getDataDirectory().getAbsolutePath() + "/storage/emulated/0/MyFolder"

                        File sdCard = Environment.getExternalStorageDirectory();
                        File dir = new File(sdCard.getAbsolutePath() + "/QRCode");
                        dir.mkdirs();
                        Toast.makeText(this, dir.getAbsolutePath().toString(), Toast.LENGTH_LONG).show();

                        // String savePath = Environment.getExternalStorageDirectory().getPath()+"/QRCodes/".toString();
                        save = QRGSaver.save(String.valueOf(dir)+"/","Teacher's Code",bitmap,QRGContents.ImageType.IMAGE_PNG);
                        result = save ? "QR Saved" : "Something Went Wrong";
                        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }

                }
            }

        });


        //add student
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.popup_student);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                student_name = dialog.findViewById(R.id.student_name_et);
                student_code = dialog.findViewById(R.id.student_id_et);
                saveBtn = dialog.findViewById(R.id.save_btn);
                cancelBtn = dialog.findViewById(R.id.cancel_btn);

                dialog.show();

                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String sName = student_name.getText().toString();
                        String sId = student_code.getText().toString();
                       final String sClass = GlobalVar.codeToPass;
                       final String sSubjectName = GlobalVar.subjectToPass;
                        String sToken = "";


                        if (TextUtils.isEmpty(sName)){
                            student_name.setError("Required");
                            return;
                        }
                        if( TextUtils.isEmpty(sId)){
                            student_code.setError("Required");
                            return;
                        }


                        if (!TextUtils.isEmpty(sName) && !TextUtils.isEmpty(sId)){
                            //insert data
                            //reference path


                            reference = rootNode.getReference("users/"+userId+"/Students/"+sClass);
                            StudentModel studentModel = new StudentModel(sName,sId,sClass,null,null,null,sToken,"0.0","0.0","0.0","0.0","0.0","0.0","0.0","0.0","0.0","0.0","0.0","0.0",String.valueOf(0),String.valueOf(0),String.valueOf(0),null);
                            list.clear();
                            reference.child(sId).setValue(studentModel);

                            studentRef = rootNode.getReference("studentInfo/"+sId+"/Subjects");
                            SubjectModel subjectModel = new SubjectModel(sSubjectName,sClass,userId);
                            GlobalBool.hasToken = false;
                            studentRef.child(sClass).setValue(subjectModel);
                          //  studentControl.add(studentModel);
                            loadingDialog.startLoadingDialog();
                            Handler handler = new Handler();
                            handler.postDelayed((Runnable) () -> {
                                loadingDialog.dismissDialog();
                            },1000);

                           // Intent intent = new Intent(getApplicationContext(), StudentList.class);
                        //    intent.putExtra(StudentAdapter.CODE,sClass);
                           // setResult(Activity.RESULT_OK,intent);
                         //   intent.putExtra(StudentAdapter.ICODE,sId);

                           // startActivity(intent);
                            //   finish();

                            studentAdapter.notifyDataSetChanged();

                            Toast.makeText( StudentList.this, "Student Added", Toast.LENGTH_SHORT).show();


                        }else{
                            student_code.setError("Required");
                            student_name.setError("Required");
                        }
                        dialog.dismiss();

                    }
                });

                cancelBtn.setOnClickListener((View view)->{
                    //cancel
                    dialog.dismiss();
                });

            }

        });


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    StudentModel recyclerData = null;


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();

            switch (direction){
                case ItemTouchHelper.LEFT:
                    // message function here
                    StudentModel recyclerData = list.get(position);
                    String sId = list.get(position).getsId();
                    list.remove(position);
                    studentAdapter.notifyItemRemoved(position);
                    final String classCode =GlobalVar.codeToPass;

                    messageReference = rootNode.getReference("users/"+userId+"/Students/"+classCode);
                    messageReference.child(sId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if(task.isSuccessful()){
                                if (task.getResult().exists()){
                                    DataSnapshot mySnapshot = task.getResult();
                                    String sToken = String.valueOf(mySnapshot.child("sToken").getValue());
                                    String sClass = String.valueOf(mySnapshot.child("sClass").getValue());

                                    notifyDialog.setContentView(R.layout.popup_notify);
                                    notifyDialog.setCancelable(false);
                                    notifyDialog.setCanceledOnTouchOutside(false);

                                    title = notifyDialog.findViewById(R.id.classname_notify);
                                    message = notifyDialog.findViewById(R.id.message_notify_et);
                                    cancelNotifBtn = notifyDialog.findViewById(R.id.cancel_notify_btn);
                                    notifyBtn = notifyDialog.findViewById(R.id.notify_btn);

                                    title.setText(sClass);

                                    notifyBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                         String messageText = message.getText().toString();

                                            if (sToken.equals("")){
                                                Toast.makeText(StudentList.this, "This User either not registered or\nInactive to this App", Toast.LENGTH_SHORT).show();
                                            }else if (TextUtils.isEmpty(messageText)){
                                                message.setError("Required");
                                            }else{

                                                FCMSend.pushNotification(
                                                        StudentList.this,
                                                        sToken,
                                                        sClass,
                                                        messageText
                                                );

                                                list.add(position,recyclerData);
                                                studentAdapter.notifyItemInserted(position);
                                                notifyDialog.dismiss();

                                               Toast toast = Toast.makeText(StudentList.this, "Notification sent!", Toast.LENGTH_SHORT);
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
                                            list.add(position,recyclerData);
                                            studentAdapter.notifyItemInserted(position);
                                            notifyDialog.dismiss();
                                        }
                                    });



                                  //  Toast.makeText(StudentList.this, "You swiped swipe "+sToken, Toast.LENGTH_SHORT).show();
                                        notifyDialog.show();
                                }

                            }

                        }
                    });
                    studentAdapter.notifyItemInserted(position);

                    break;

                default:
                    Toast.makeText(StudentList.this, "Swipe to Left to message", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

    private void filter(String textSearch){
        ArrayList<StudentModel> filteredList = new ArrayList<>();

        for (StudentModel item : list){
            if (item.getsName().toLowerCase().contains(textSearch.toLowerCase())){
                filteredList.add(item);
            }else
            if (item.getsId().contains(textSearch)){
                filteredList.add(item);
            }
        }

        studentAdapter.filterList(filteredList);
    }


    public void ClickMore(View view){
        //open more
        PopupMenu popup = new PopupMenu(this,view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.actions_menu);
        popup.show();
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.a_z:
                //sort here a - z
                Collections.sort(list,StudentModel.StudentNameAZComparator);
                Toast.makeText( StudentList.this, "Class has been sorted A - Z", Toast.LENGTH_SHORT).show();
                studentAdapter.notifyDataSetChanged();

                return  true;
            case R.id.z_a:
                //sort here z - a
                Collections.sort(list,StudentModel.StudentNameZAComparator);
                Toast.makeText( StudentList.this, "Class has been sorted Z - A", Toast.LENGTH_SHORT).show();
                studentAdapter.notifyDataSetChanged();
                return  true;
            default:
                return  false;
        }
    }

    private void requestSpecificPermision() {
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        },1);
    }

    private boolean checkIfAlreadyHavePermiss() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){
            return  true;
        }else{
            return false;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission Granted, Kindly save again the QR code", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default: super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }


    }
    public  void ClickCal(View view){
        //Homeactivity
        Intent intent = new Intent(StudentList.this, CalendarActivity.class);
        startActivity(intent);
    }
    public  void ClickMenu(View view){
        MainActivity.openDrawer(drawerLayout);
    }
    public  void ClickArea(View view){
        MainActivity.closeDrawer(drawerLayout);
    }
    public  void ClickHome(View view){
        MainActivity.redirectActivity(this,MainActivity.class);
    }
    public  void ClickAbout(View view){

        dialogBuilder = new AlertDialog.Builder(this);
        View aboutPopup = getLayoutInflater().inflate(R.layout.about,null);
        aboutText = aboutPopup.findViewById(R.id.about_text);
        okBtn = aboutPopup.findViewById(R.id.ok_btn);

        dialogBuilder.setView(aboutPopup);
        dialog1 = dialogBuilder.create();
        dialog1.show();

        okBtn.setOnClickListener(v -> {
            //ok
            dialog1.dismiss();
        });

    }
    public  void ClickLogout(View view){
        MainActivity.logout(this);
    }
    @Override
    protected void onPause(){
        super.onPause();
        MainActivity.closeDrawer(drawerLayout);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent( StudentList.this,MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }


}