package com.example.erecordsv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

public class RoleChooser extends AppCompatActivity {
    public static  final String SHARED_TEACHER = "iAmTeacher";
    public static  final String SHARED_STUDENT = "iAmStudent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_chooser);
    }

    public void ClickTeacher(View view) {
        Intent intent = new Intent(RoleChooser.this, LoginActivity.class);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(SHARED_TEACHER, true);
        editor.apply();
        startActivity(intent);
        finishAffinity();


    }

    public void ClickStudent(View view) {
        Intent intent = new Intent(RoleChooser.this, StudentEndLogin.class);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(SHARED_STUDENT, true);
        editor.apply();
        startActivity(intent);
        finishAffinity();
    }
}