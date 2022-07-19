package com.example.erecordsv2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isTeacher = prefs.getBoolean(RoleChooser.SHARED_TEACHER,false);
        boolean isStudent = prefs.getBoolean(RoleChooser.SHARED_STUDENT,false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (isTeacher){
                    Intent loginTeacherActivity = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(loginTeacherActivity);
                    finish();

                }else if(isStudent){
                    Intent loginStudentActivity = new Intent(SplashActivity.this,StudentEndLogin.class);
                    startActivity(loginStudentActivity);
                    finish();
                }else{
                    Intent chooseRole = new Intent(SplashActivity.this,RoleChooser.class);
                    startActivity(chooseRole);
                    finish();
                }

            }
        },3000);




    }
}