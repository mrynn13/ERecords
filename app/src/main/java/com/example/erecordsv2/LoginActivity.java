package com.example.erecordsv2;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    TextView signUp, forgotPass;
    Button loginBtn;
    FirebaseAuth fAuth;
    private LinearLayout layout;

    MainAdapter mainAdapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email_et);
        password = findViewById(R.id.password_et);
        loginBtn = findViewById(R.id.login_btn);
        signUp = findViewById(R.id.sign_up_link);
        forgotPass = findViewById(R.id.reset_link);
        layout = findViewById(R.id.layout_white);
        fAuth = FirebaseAuth.getInstance();

        email.setSelection(email.getText().length());
        password.setSelection(password.getText().length());

//loading dialog
        LoadingDialog loadingDialog = new LoadingDialog( LoginActivity.this);

        loginBtn.setOnClickListener((View v) -> {
            //content login here
            String emailLogin = email.getText().toString();
            String passLogin = password.getText().toString();

            loadingDialog.startLoadingDialog();

            if (TextUtils.isEmpty(emailLogin)) {
                email.setError("Email is Required");
                loadingDialog.dismissDialog();
                return;
            } else if (TextUtils.isEmpty(passLogin)) {
                password.setError("Password is Required");
                loadingDialog.dismissDialog();
                return;
            } else if(!TextUtils.isEmpty(emailLogin) || !TextUtils.isEmpty(passLogin)) {

                fAuth.signInWithEmailAndPassword(emailLogin, passLogin).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            GlobalVar.user = "teacher";
                            loadingDialog.dismissDialog();
                            Toast.makeText( LoginActivity.this, "Logged In", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {

                            Toast.makeText( LoginActivity.this, "Error Occurred" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();
                        }
                    }
                });
            }


            //end of login
        });

        signUp.setOnClickListener((View v) -> {
            loadingDialog.startLoadingDialog();
            Intent toRegister = new Intent( LoginActivity.this,  RegisterActivity.class);
            Handler handler = new Handler();
            handler.postDelayed((Runnable) () -> {
                loadingDialog.dismissDialog();
            },1000);

            Pair[] pair = new Pair[1];
            pair[0] = new Pair<View, String>(layout, "layoutTransition");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation( LoginActivity.this, pair);
            startActivity(toRegister, options.toBundle());
        });

        forgotPass.setOnClickListener((View v) -> {
            //content here
            EditText resetMail = new EditText(v.getContext());
            AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
            passwordResetDialog.setTitle("Reset Password");
            passwordResetDialog.setMessage("Enter your Email");
            passwordResetDialog.setView(resetMail);

            passwordResetDialog.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //extract emailand sent rest link
                    String mail = resetMail.getText().toString();
                    fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText( LoginActivity.this, "Reset link has been sent.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText( LoginActivity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });

            passwordResetDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //close dialog
                }
            });


            passwordResetDialog.create().show();


        });


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
            if(password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_visible);
                //Show Password
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_invisible);
                //Hide Password
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }
}

