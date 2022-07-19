package com.example.erecordsv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentEndLogin extends AppCompatActivity {
    EditText sPassword, sIdNumber;
    TextView register;
    Button sLoginBtn;

    FirebaseAuth fAuth;
    FirebaseDatabase rootNode;
    DatabaseReference sRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_end_login);


        sPassword = findViewById(R.id.student_password_et);
        sLoginBtn = findViewById(R.id.login_btn);
        register = findViewById(R.id.sign_up_link);
        sIdNumber = findViewById(R.id.student_idnum_login_et);
        rootNode = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();

        sPassword.setSelection(sPassword.getText().length());
        sIdNumber.setSelection(sIdNumber.getText().length());

        sRef = rootNode.getReference("studentUser");




//loading dialog
        LoadingDialog loadingDialog = new LoadingDialog( StudentEndLogin.this);

        sLoginBtn.setOnClickListener((View v) -> {
            //content login here


            String sidNumber =  sIdNumber.getText().toString();
            String sPassLogin = sPassword.getText().toString();


            loadingDialog.startLoadingDialog();

            if (TextUtils.isEmpty(sidNumber)) {
                sIdNumber.setError("Idno. is Required");
                loadingDialog.dismissDialog();
            } else if (TextUtils.isEmpty(sPassLogin)) {
                sPassword.setError("Password is Required");
                loadingDialog.dismissDialog();
            }
            if((!sidNumber.equals("") && !sPassLogin.equals(""))){

                String newEmailLogin = sidNumber+"@records.com";

                fAuth.signInWithEmailAndPassword(newEmailLogin, sPassLogin).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            loadingDialog.dismissDialog();
                            sRef.child(sidNumber).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (task.isSuccessful()){
                                        GlobalVar.user ="notTeacher";
                                        if (task.getResult().exists()){
                                            DataSnapshot mySnapshot = task.getResult();

                                            String studentNAME = String.valueOf(mySnapshot.child("name").getValue());
                                            String studentToken = String.valueOf(mySnapshot.child("deviceToken").getValue());
                                            GlobalVar.nameToPass = studentNAME;
                                            GlobalVar.tokenToPass = studentToken;


                                            Toast.makeText( StudentEndLogin.this, "Logged In", Toast.LENGTH_SHORT).show();
                                           // Toast.makeText(StudentEndLogin.this, studentNAME + studentToken, Toast.LENGTH_SHORT).show();


                                            Intent getinfo = getIntent();
                                          /*  String sName = getinfo.getStringExtra("Name");
                                            String sId = getinfo.getStringExtra("StudentID");*/
                                            Intent intent = new Intent(StudentEndLogin.this, DisplayClasses.class);
                                            intent.putExtra("Name",studentNAME);
                                            intent.putExtra("TOKEN",studentToken);
                                            intent.putExtra("StudentID",sidNumber);
                                            startActivity(intent);
                                            finish();



                                        }else
                                        {
                                            Toast.makeText(StudentEndLogin.this, "get result error", Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(StudentEndLogin.this, "Some error in tas is success", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });



                        } else {

                            Toast.makeText( StudentEndLogin.this, "Error Occurred" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();
                        }
                    }
                });
            }


            //end of login
        });

        register.setOnClickListener((View v) -> {
            loadingDialog.startLoadingDialog();
            Intent intent = new Intent(StudentEndLogin.this, StudentRegister.class);
            startActivity(intent);
            loadingDialog.dismissDialog();
        });

       /* register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/



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
                Intent exit = new Intent(Intent.ACTION_MAIN);
                exit.addCategory(Intent.CATEGORY_HOME);
                exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(exit);
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

    public void ShowHidePass(View view) {
        if(view.getId()==R.id.show_pass_btn){
            if(sPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_visible);
                //Show Password
                sPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_invisible);
                //Hide Password
                sPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }
}