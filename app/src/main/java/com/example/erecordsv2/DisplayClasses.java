package com.example.erecordsv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class DisplayClasses extends AppCompatActivity {


    //RecycleView
    RecyclerView recyclerView;
    StudentEndAdapter studentEndAdapter;
    DrawerLayout drawerLayout;

    Dialog dialog;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog1;
    TextView aboutText;


    FirebaseDatabase rootNode;
    DatabaseReference reference,adminReference;
    FirebaseAuth fAuth;
    ArrayList<SubjectModel> list;

    Button joinClass,scan;

    String userId;
    EditText join_subject_code,join_room_code;
    Button cancelBtn, saveBtn,okBtn;
    TextView idnumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_classes);

        rootNode = FirebaseDatabase.getInstance();
        recyclerView = findViewById(R.id.recycle_view);
        dialog = new Dialog(this);
        joinClass = findViewById(R.id.join_class_btn);
        drawerLayout = findViewById(R.id.drawer_layout_student_end);
        idnumber = findViewById(R.id.retrieve_idnum);
        fAuth = FirebaseAuth.getInstance();

        userId = fAuth.getCurrentUser().getUid();





        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();

        Intent getinfo = getIntent();
        String sId = getinfo.getStringExtra("StudentID");
        String sToken = getinfo.getStringExtra("TOKEN");
        String sNAme = getinfo.getStringExtra("Name");

        //retrieve again the name


        //assign studentid in global
        GlobalVar.idToPass = sId;

        String SID = GlobalVar.idToPass;

        idnumber.setText(sId);

        //loading dialog
        LoadingDialog loadingDialog = new LoadingDialog( DisplayClasses.this);



        studentEndAdapter = new StudentEndAdapter(this,list);
        studentEndAdapter.setHasStableIds(true);
        recyclerView.setAdapter(studentEndAdapter);

        reference = rootNode.getReference("studentInfo/"+SID).child("Subjects");
        GlobalVar.idToPass = SID;

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                if (snapshot1.hasChildren()){
                    for (DataSnapshot dataSnapshot: snapshot1.getChildren()){

                        SubjectModel subjectModel = dataSnapshot.getValue(SubjectModel.class);
                        list.add(subjectModel);
                    }

                }else{
                    Toast.makeText(DisplayClasses.this, "No Available Class, Ask Your Teacher", Toast.LENGTH_SHORT).show();

                }

                studentEndAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        joinClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.popup_join);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                join_room_code = dialog.findViewById(R.id.room_code);
                saveBtn = dialog.findViewById(R.id.join_save_btn);
                cancelBtn = dialog.findViewById(R.id.join_cancel_btn);
                scan = dialog.findViewById(R.id.scan_qr);

                dialog.show();

                scan.setOnClickListener(view->{
                    Intent intent = new Intent(DisplayClasses.this, ScanActivity.class);
                    intent.putExtra("SNAME", GlobalVar.nameToPass);
                    intent.putExtra("STOKEN",  GlobalVar.tokenToPass);
                    GlobalVar.nameToPass = sNAme;
                    GlobalVar.tokenToPass = sToken;
                    startActivity(intent);
                });

                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String joinRoomCode = join_room_code.getText().toString();
                        String sCountAct = "";

                        if( TextUtils.isEmpty(joinRoomCode)){
                            join_room_code.setError("Required");
                        }else{

                            Intent intent = new Intent(DisplayClasses.this, AvailableClass.class);
                            intent.putExtra("RoomCode", joinRoomCode);
                            intent.putExtra("SNAME", GlobalVar.nameToPass);
                            intent.putExtra("STOKEN",  GlobalVar.tokenToPass);
                            GlobalVar.roomToPass = joinRoomCode;
                            GlobalVar.nameToPass = sNAme;
                            GlobalVar.tokenToPass = sToken;
                            startActivity(intent);

                          /*  //insert data
                            adminReference = rootNode.getReference("users/"+joinRoomCode+"/Students/"+joinSubjectCode);
                            StudentModel studentModel = new StudentModel(sName,sPhone,sId,joinSubjectCode,sCountAct);
                            adminReference.child(sId).setValue(studentModel);

                            SubjectModel subjectModel = new SubjectModel(joinSubjectCode.toUpperCase());
                            list.clear();
                            reference = rootNode.getReference("studentInfo/"+userId);
                            reference.child("Subjects").setValue(subjectModel);
                            //  studentControl.add(studentModel);
                            loadingDialog.startLoadingDialog();
                            Handler handler = new Handler();
                            handler.postDelayed((Runnable) () -> {
                                loadingDialog.dismissDialog();
                            },1000);

                            Toast.makeText( DisplayClasses.this, "Joined", Toast.LENGTH_SHORT).show();
*/
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

    }
    public void ClickMore(View view){
        //open more
        PopupMenu popup = new PopupMenu(this,view);
        popup.setOnMenuItemClickListener(this::onMenuItemClick);
        popup.inflate(R.menu.actions_menu);
        popup.show();
    }


    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.a_z:
                //sort here a - z
                Collections.sort(list,SubjectModel.ClassNameAZComparator);
                Toast.makeText( DisplayClasses.this, "Class has been sorted A - Z", Toast.LENGTH_SHORT).show();
                studentEndAdapter.notifyDataSetChanged();

                return  true;
            case R.id.z_a:
                //sort here z - a
                Collections.sort(list,SubjectModel.ClassNameZAComparator);
                Toast.makeText( DisplayClasses.this, "Class has been sorted Z - A", Toast.LENGTH_SHORT).show();
                studentEndAdapter.notifyDataSetChanged();

                return  true;
            default:
                return  false;
        }
    }


    public void ClickMenu(View view){
        //open drawer
        openDrawer(drawerLayout);
    }


    public static void openDrawer(DrawerLayout drawerLayout) {
        // open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public void ClickArea(View view){
        //close drawer
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        //close drawer
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view){
        //Homeactivity
        recreate();
    }
    public void ClickLogout(View view){
        //logout
        logout(this);
    }

    public static void logout(Activity activity) {
        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //set title
        builder.setTitle("Exit");
        //set message
        builder.setMessage("Are you sure you want to Exit?");
        //positive yes
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish activity
                FirebaseAuth.getInstance().signOut();

                activity.finishAffinity();
                //exit app
                System.exit(0);
            }
        });
        //negative no
        builder.setNegativeButton("NO", (dialog, which) -> {
            //dismiss dialog
            dialog.dismiss();
        });
        builder.show();


    }
    public void ClickAbout(View view) {
        //redirect activity to about page
        //redirectActivity(this,AboutActivity.class);

        about();
    }

    private void about() {

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

    @Override
    protected void onPause(){
        super.onPause();
        //close drawer
        closeDrawer(drawerLayout);
    }

    @Override
    public void onBackPressed() {
        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //set title
        builder.setTitle("Exit");
        //set message
        builder.setMessage("Are you sure you want to Exit?");
        //positive yes
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish activity
                finishAffinity();
                System.exit(0);
            }
        });
        //negative no
        builder.setNegativeButton("NO", (dialog, which) -> {
            //dismiss dialog
            dialog.dismiss();
        });
        builder.show();
    }

    public void ClickCal(View view){
        //Homeactivity
        Intent intent = new Intent(DisplayClasses.this, CalendarActivity.class);
        startActivity(intent);
    }

}