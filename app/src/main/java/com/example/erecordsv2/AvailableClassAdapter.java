package com.example.erecordsv2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class AvailableClassAdapter extends RecyclerView.Adapter<AvailableClassAdapter.MyViewHolder> {

    Context context;
    ArrayList<ClassModel> list;


    FirebaseDatabase rootNode;
    DatabaseReference studentRef,adminRef;


    public static final String ID = GlobalVar.idToPass;
    public static final String ROOM_ID = GlobalVar.roomToPass;
    public static final String TOKEN = GlobalVar.tokenToPass;
    public static final String NAME = GlobalVar.nameToPass;



    public AvailableClassAdapter(Context context, ArrayList<ClassModel>list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_available_code,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ClassModel classModel = list.get(position);


        rootNode = FirebaseDatabase.getInstance();




        holder.codeBtn.setText(classModel.getSubjectCode());
        holder.mySubjectTv.setText(classModel.getSubjectName());
        String sId = ID;
        String class_code = classModel.getSubjectCode();
        String classname = classModel.getSubjectName();


        studentRef = rootNode.getReference("studentInfo/"+sId+"/Subjects");
        // reference = rootNode.getReference("users/"+userId+"/Students/"+sClass);


        holder.codeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                //alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                //set title
                builder.setTitle("Joining Class");
                //set message
                builder.setMessage("Are you sure you belong to this class?\nSubject: "+classname+"\n"+"Code: "+classModel.getSubjectCode());
                //positive yes
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //add subject
                        SubjectModel subjectModel = new SubjectModel(classname,classModel.getSubjectCode().toUpperCase(),ROOM_ID);

                        list.clear();
                        studentRef.child(classModel.getSubjectCode().toUpperCase()).setValue(subjectModel);
                        adminRef = rootNode.getReference("users/"+ROOM_ID+"/Students/"+classModel.getSubjectCode());

                        StudentModel studentModel = new StudentModel(NAME,ID,classModel.getSubjectCode(),null,null,null,TOKEN,"0.0","0.0","0.0","0.0","0.0","0.0","0.0","0.0","0.0","0.0","0.0","0.0",null,null,null,null);
                        list.clear();
                        adminRef.child(sId).setValue(studentModel);


                        Intent intent = new Intent(v.getContext(), DisplayClasses.class);
                        intent.putExtra("StudentID",sId);
                        intent.putExtra("TOKEN",TOKEN);
                        intent.putExtra("Name",NAME);
                        GlobalVar.idToPass = sId;
                        GlobalVar.tokenToPass = TOKEN;
                        GlobalVar.nameToPass = NAME;
                        GlobalBool.hasToken = true;


                        //toast
                        Toast.makeText(context, "You can now view scores and receive\n notification from your teacher", Toast.LENGTH_LONG).show();
                        //dismiss dialog
                        dialog.dismiss();
                        //start intent
                        context.startActivity(intent);
                        ((Activity)context).finishAffinity();



                    }
                });
                //negative no
                builder.setNegativeButton("NO", (dialog, which) -> {
                    //dismiss dialog
                    dialog.dismiss();
                });
                builder.create();
                builder.show();















              /*  HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("sToken",TOKEN);
                list.clear();
                adminRef.child(sId).updateChildren(hashMap);
*/


            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button codeBtn;
        TextView mySubjectTv;
     //   View parentLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            codeBtn = itemView.findViewById(R.id.available_code);
            mySubjectTv = itemView.findViewById(R.id.available_subject);
           // parentLayout = itemView.findViewById(android.R.id.content);
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
