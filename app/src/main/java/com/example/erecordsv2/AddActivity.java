package com.example.erecordsv2;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class AddActivity extends AppCompatActivity {
    //initialize variable
    DrawerLayout drawerLayout;
    TextView aboutText, noClass;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog1;
    Button okBtn;


    //Operation class
    StudentOperator studentOperate = new StudentOperator();

    EditText nameEt, idEt;
    Button cancel, save;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);



        //assign by its id
        drawerLayout = findViewById(R.id.drawer_layout);
        save = findViewById(R.id.save_btn_tab);
        cancel = findViewById(R.id.cancel_btn_tab);




        Intent tocode = getIntent();
        String codeClass = tocode.getStringExtra("MYCODE");
        final String sActCount = GlobalVar.scoreCount;

        //Save student info
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String studentName = nameEt.getText().toString();
                String studentId = idEt.getText().toString();
                String studentCode = codeClass;
                String studentAct = sActCount;
                String studentPhone = "";


                if (TextUtils.isEmpty(studentName)){
                    nameEt.setError("Required");
                }
                if( TextUtils.isEmpty(studentId)){
                    idEt.setError("Required");
                }

                if (!TextUtils.isEmpty(studentName) && !TextUtils.isEmpty(studentId)){
                    //insert data
                     StudentModel studentModel = new  StudentModel(studentName,studentId,studentCode,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,String.valueOf(0),String.valueOf(0),String.valueOf(0),null);
                    studentOperate.add(studentModel);

                    Intent intent = new Intent(getApplicationContext(),  StudentList.class);
                    startActivity(intent);
                    finishAffinity();

                    Toast.makeText(getApplicationContext(), "Student Added", Toast.LENGTH_SHORT).show();

                }else{
                    nameEt.setError("Required");
                    idEt.setError("Required");
                }
            }
        });

    }

}