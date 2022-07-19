package com.example.erecordsv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Pair;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class StudentRegister extends AppCompatActivity {
    EditText nameEt, studentIdEt,sPassword;
    Button proceedBtn;
    TextView loginLinkText;
    FirebaseAuth fAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;


    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        nameEt = findViewById(R.id.name_et);
        studentIdEt = findViewById(R.id.id_et);
        proceedBtn = findViewById(R.id.proceed_btn);
        loginLinkText = findViewById(R.id.s_login_text_link);
        sPassword = findViewById(R.id.pass_et);
        rootNode = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();

        nameEt.setSelection(nameEt.getText().length());
        studentIdEt.setSelection(studentIdEt.getText().length());
        sPassword.setSelection(sPassword.getText().length());

        //getId in database
        //userId = fAuth.getCurrentUser().getUid();

        //loading dialog
        LoadingDialog loadingDialog = new LoadingDialog( StudentRegister.this);


        loginLinkText.setOnClickListener((View v)->{
            Intent toLogin = new Intent( StudentRegister.this, StudentEndLogin.class);
            loadingDialog.startLoadingDialog();
            startActivity(toLogin);
            loadingDialog.dismissDialog();
            finish();
        });


        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEt.getText().toString();
                String studentID = studentIdEt.getText().toString();
                String pass = sPassword.getText().toString();

                if (studentID.isEmpty()){
                    studentIdEt.setError("Required");
                }
                if (name.isEmpty()){
                    nameEt.setError("Required");
                }

                if (pass.isEmpty()){
                    sPassword.setError("Required");
                }
                if(((!studentID.equals("") && (!name.equals(""))) && !pass.equals(""))){

                    loadingDialog.startLoadingDialog();
                    String newEmail = studentID+"@records.com";

                    fAuth.createUserWithEmailAndPassword(newEmail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText( StudentRegister.this,"Registration is Successful",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(StudentRegister.this,StudentEndLogin.class);
                               /* intent.putExtra("Name",name);
                                intent.putExtra("StudentID",studentID);
*/
                                GlobalVar.nameToPass = name;
                                reference = rootNode.getReference("studentUser");

                                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                    @Override
                                    public void onComplete(@NonNull Task<String> task) {

                                        if (!task.isSuccessful()){
                                            return;
                                        }
                                        String deviceToken = task.getResult();
                                        GlobalVar.tokenToPass = deviceToken;
                                        StudentEndModel studentUser = new StudentEndModel(name,studentID,deviceToken);
                                        reference.child(studentID).setValue(studentUser);
                                    }
                                });

                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText( StudentRegister.this,"Error Occurred"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                loadingDialog.dismissDialog();

                            }
                        }
                    });

                }


            }
        });

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