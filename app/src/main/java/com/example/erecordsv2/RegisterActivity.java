package com.example.erecordsv2;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText emailSignUp,passSignUp,confirmPass;
    TextView loginLinkText;
    Button signUpBtn;
    private LinearLayout layout;
    FirebaseAuth fAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailSignUp = findViewById(R.id.sign_up_email_et);
        passSignUp = findViewById(R.id.sign_up_password_et);
        confirmPass = findViewById(R.id.confirm_password_et);
        loginLinkText = findViewById(R.id.login_text_link);
        signUpBtn = findViewById(R.id.sign_up_btn);
        layout = findViewById(R.id.layout_white2);
        fAuth = FirebaseAuth.getInstance();

        emailSignUp.setSelection(emailSignUp.getText().length());
        passSignUp.setSelection(passSignUp.getText().length());
        confirmPass.setSelection(confirmPass.getText().length());

        //loading dialog
        LoadingDialog loadingDialog = new LoadingDialog( RegisterActivity.this);


        loginLinkText.setOnClickListener((View v)->{
            Intent toLogin = new Intent( RegisterActivity.this, LoginActivity.class);

            Pair[] pair = new Pair[1];
            pair[0] = new Pair<View, String>(layout,"layoutTransition");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation( RegisterActivity.this,pair);
            loadingDialog.startLoadingDialog();
            startActivity(toLogin, options.toBundle());
           loadingDialog.dismissDialog();
            finish();
        });

        signUpBtn.setOnClickListener((View v)->{
            String email = emailSignUp.getText().toString();
            String pass = passSignUp.getText().toString();
            String conPass = confirmPass.getText().toString();

            if (TextUtils.isEmpty(email)){
                emailSignUp.setError("Email is Required");
                return;
            }else if (TextUtils.isEmpty(pass)){
                passSignUp.setError("Password is Required");
                return;
            } else if (pass.length() < 6){
                passSignUp.setError("Too short Password!");
                return;
            }else if (TextUtils.isEmpty(conPass)){
                confirmPass.setError("Please Confirm Password");
                return;
            }else if (!pass.equals(conPass)){
                passSignUp.setError("Password didn't matched");
                confirmPass.setError("Password didn't matched");
                return;
            }else {
                loadingDialog.startLoadingDialog();

                fAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText( RegisterActivity.this,"Registration is Successful",Toast.LENGTH_SHORT).show();
                            rootNode = FirebaseDatabase.getInstance();
                            reference = rootNode.getReference("users");

                            String userId = fAuth.getCurrentUser().getUid();

                           /* Teachers teacher = new Teachers(email);

                            reference.child(userId).setValue(teacher);*/
                            loadingDialog.dismissDialog();
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        }else{
                            Toast.makeText( RegisterActivity.this,"Error Occurred"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();

                        }
                    }
                });
            }
        });
    }

    public void ShowHidePass(View view) {
        if(view.getId()==R.id.show_pass_btn){
            if(passSignUp.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_visible);
                //Show Password
                passSignUp.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_invisible);
                //Hide Password
                passSignUp.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    public void ShowHideCPass(View view) {
        if(view.getId()==R.id.show_cpass_btn){
            if(confirmPass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_visible);
                //Show Password
                confirmPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_invisible);
                //Hide Password
                confirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }


}
