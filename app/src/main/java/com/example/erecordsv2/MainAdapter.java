package com.example.erecordsv2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {

    Context context;
    ArrayList<ClassModel> list;



    Dialog dialog,dialog1;
    AlertDialog.Builder dialogBuilder;

    FirebaseAuth fAuth;
    FirebaseDatabase rootNode;
    DatabaseReference studentRef,subjectReference,studentInfoRef;

    String userId;
    ArrayList<String> studentListId;

    public MainAdapter(Context context, ArrayList<ClassModel> list) {
        this.context = context;
        this.list = list;
    }
    
    

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ClassModel classModel = list.get(position);
        studentListId = new ArrayList<>();

        //loading dialog
         LoadingDialog loadingDialog = new  LoadingDialog((Activity) context);

        fAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance();
        userId = fAuth.getCurrentUser().getUid();


        holder.subjectCode.setText(classModel.getSubjectCode());
        holder.subjectName.setText(classModel.getSubjectName());
        holder.subjectSched.setText(classModel.getSubjectSched());
        holder.subjectRoom.setText(classModel.getSubjectRoom());


        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                 Operation operate = new  Operation();

                //alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                //set title
                builder.setTitle("Remove Class");
                //set message
                builder.setMessage("Are you sure you want to remove?");
                //positive yes
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //remove class
                        list.clear();
                        loadingDialog.startLoadingDialog();
                        Handler handler = new Handler();
                        handler.postDelayed((Runnable) loadingDialog::dismissDialog,1000);

                        studentRef = rootNode.getReference("users/"+userId+"/Students").child(classModel.getSubjectCode());
                        //access studentId first
                        studentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChildren()){
                                    collectIDS((Map<String,Object>) snapshot.getValue());
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        studentRef.child(classModel.getSubjectCode()).removeValue();
                        //calling operate class to remove
                        operate.remove(classModel.getSubjectCode()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                dialog.dismiss();
                                Toast.makeText(context.getApplicationContext(), "Removed Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                });
                //negative no
                builder.setNegativeButton("NO", (dialog, which) -> {
                    //dismiss dialog
                    dialog.dismiss();
                });
                builder.create();
                builder.show();
            }

            private void collectIDS(Map<String, Object> value) {
                ArrayList<String> IDS = new ArrayList<>();

                for (Map.Entry<String,Object> entry : value.entrySet()){
                    //get id map
                    Map singleUser = (Map) entry.getValue();
                    //get field
                    IDS.add((String) singleUser.get("sId"));

                    removeList(IDS);


                }
            }

            private void removeList(ArrayList<String> ids) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                String userId = firebaseAuth.getCurrentUser().getUid();
                for (int i = 0; i < ids.size(); i++){
                    studentInfoRef = rootNode.getReference("users/"+userId+"/Students/"+classModel.getSubjectCode());
                    //remove also from student info
                    studentInfoRef.child(ids.get(i)).removeValue();
                }
                removeStudentInfo(ids);

            }

            private void removeStudentInfo(ArrayList<String> ids) {
                for (int i = 0; i < ids.size(); i++){
                    studentInfoRef = rootNode.getReference("studentInfo/"+ids.get(i)+"/Subjects");
                    //remove also from student info
                    studentInfoRef.child(classModel.getSubjectCode()).removeValue();

                }
            }

        });

        holder.viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 GlobalVar.codeToPass = classModel.getSubjectCode();
                 GlobalVar.subjectToPass = classModel.getSubjectName();

                Intent intent = new Intent(context,  StudentList.class);
                intent.putExtra("pActTotal",classModel.getPrelimActTotal());
                intent.putExtra("pAssTotal",classModel.getPrelimAssTotal());
                intent.putExtra("pQTotal",classModel.getPrelimQTotal());
                intent.putExtra("pExTotal",classModel.getPrelimExtotal());

                intent.putExtra("mActTotal",classModel.getMidtermActTotal());
                intent.putExtra("mAssTotal",classModel.getMidtermAssTotal());
                intent.putExtra("mQTotal",classModel.getMidtermQTotal());
                intent.putExtra("mExTotal",classModel.getMidtermExtotal());

                intent.putExtra("fActTotal",classModel.getFinalsActTotal());
                intent.putExtra("fAssTotal",classModel.getFinalsAssTotal());
                intent.putExtra("fQTotal",classModel.getFinalsQTotal());
                intent.putExtra("fExTotal",classModel.getFinalsExtotal());

                 context.startActivity(intent);


            }
        });


        holder.editBtn.setOnClickListener(v -> {
             Operation operate = new  Operation();

            dialog = new Dialog(context);

            dialog.setContentView(R.layout.popup1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            TextView subject_code = dialog.findViewById(R.id.subject_code_display);
            EditText subject_name = dialog.findViewById(R.id.subject_name_display);
            EditText subject_sched = dialog.findViewById(R.id.subject_sched_display);
            EditText subject_room = dialog.findViewById(R.id.subject_room_display);
            Button saveBtn = dialog.findViewById(R.id.save_btn);
            Button cancelBtn = dialog.findViewById(R.id.cancel_btn);

            subject_code.setText(classModel.getSubjectCode());
            subject_name.setText(classModel.getSubjectName());
            subject_sched.setText(classModel.getSubjectSched());
            subject_room.setText(classModel.getSubjectRoom());

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String subjectName = subject_name.getText().toString();
                    String subjectSched = subject_sched.getText().toString();
                    String subjectRoom = subject_room.getText().toString();
                    if (TextUtils.isEmpty(subjectName) || !(subjectName.trim().length()>0) || TextUtils.isEmpty(subjectSched)  || TextUtils.isEmpty(subjectRoom)){
                        Toast.makeText(context, "fields cannot be empty", Toast.LENGTH_SHORT).show();
                    }else if(subjectName.equals(classModel.getSubjectName()) && subjectSched.equals(classModel.getSubjectSched()) && subjectRoom.equals(classModel.getSubjectRoom())){
                        Toast.makeText(context, "Nothing Changed", Toast.LENGTH_SHORT).show();
                    }else {
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("subjectName",subjectName);
                        hashMap.put("subjectSched",subjectSched);
                        hashMap.put("subjectRoom",subjectRoom);

                        list.clear();
                        operate.update(classModel.getSubjectCode(),hashMap);
                        loadingDialog.startLoadingDialog();
                        Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            loadingDialog.dismissDialog();
                        },1000);

                        Toast.makeText(context, "Class Changed", Toast.LENGTH_SHORT).show();


                    }

                    dialog.dismiss();
                }
            });

            cancelBtn.setOnClickListener(v1 -> {
                dialog.dismiss();

            });


            dialog.show();

        });

        holder.totalAct.setOnClickListener(v -> {
            dialog1 = new Dialog(context);

            dialog1.setContentView(R.layout.average_popup1);
            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            EditText p_act = dialog1.findViewById(R.id.ptotal_act);
            EditText p_ass = dialog1.findViewById(R.id.ptotal_ass);
            EditText p_q = dialog1.findViewById(R.id.ptotal_q);
            EditText p_ex = dialog1.findViewById(R.id.ptotal_ex);

            EditText m_act = dialog1.findViewById(R.id.mtotal_act);
            EditText m_ass = dialog1.findViewById(R.id.mtotal_ass);
            EditText m_q = dialog1.findViewById(R.id.mtotal_q);
            EditText m_ex = dialog1.findViewById(R.id.mtotal_ex);

            EditText f_act = dialog1.findViewById(R.id.ftotal_act);
            EditText f_ass = dialog1.findViewById(R.id.ftotal_ass);
            EditText f_q = dialog1.findViewById(R.id.ftotal_q);
            EditText f_ex = dialog1.findViewById(R.id.ftotal_ex);

            p_act.setText(classModel.getPrelimActTotal());
            p_ass.setText(classModel.getPrelimAssTotal());
            p_q.setText(classModel.getPrelimQTotal());
            p_ex.setText(classModel.getPrelimExtotal());

            m_act.setText(classModel.getMidtermActTotal());
            m_ass.setText(classModel.getMidtermAssTotal());
            m_q.setText(classModel.getMidtermQTotal());
            m_ex.setText(classModel.getMidtermExtotal());

            f_act.setText(classModel.getFinalsActTotal());
            f_ass.setText(classModel.getFinalsAssTotal());
            f_q.setText(classModel.getFinalsQTotal());
            f_ex.setText(classModel.getFinalsExtotal());


            Button add = dialog1.findViewById(R.id.add_now_btn);
            Button later = dialog1.findViewById(R.id.later_btn);

            add.setOnClickListener(v12 -> {
                Operation operation = new Operation();

                subjectReference = rootNode.getReference("users/"+userId+"/Subjects/"+classModel.getSubjectCode());

                String prelimAct = p_act.getText().toString();
                String prelimAss = p_ass.getText().toString();
                String prelimQ = p_q.getText().toString();
                String prelimEx = p_ex.getText().toString();

                String midtermAct = m_act.getText().toString();
                String midtermAss = m_ass.getText().toString();
                String midtermQ = m_q.getText().toString();
                String midtermEx = m_ex.getText().toString();

                String finalsAct = f_act.getText().toString();
                String finalsAss = f_ass.getText().toString();
                String finalsQ = f_q.getText().toString();
                String finalsEx = f_ex.getText().toString();

                ClassModel classModel1 = new ClassModel(classModel.getSubjectCode(),classModel.getSubjectName(),classModel.getSubjectSched(),classModel.getSubjectRoom(),prelimAct,prelimAss,prelimQ,prelimEx,
                        midtermAct,midtermAss,midtermQ,midtermEx,finalsAct,finalsAss,finalsQ,finalsEx);
                operation.add(classModel1);
                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
                list.clear();
              /*  notifyDataSetChanged();*/



               /* subjectReference.child("prelimActTotal").setValue(prelimAct);
                subjectReference.child("prelimAssTotal").setValue(prelimAss);
                subjectReference.child("prelimQTotal").setValue(prelimQ);
                subjectReference.child("prelimExTotal").setValue(prelimEx);

                subjectReference.child("midtermActTotal").setValue(midtermAct);
                subjectReference.child("midtermAssTotal").setValue(midtermAss);
                subjectReference.child("midtermQTotal").setValue(midtermQ);
                subjectReference.child("midtermExTotal").setValue(midtermEx);

                subjectReference.child("finalsActTotal").setValue(finalsAct);
                subjectReference.child("finalsAssTotal").setValue(finalsAss);
                subjectReference.child("finalsQTotal").setValue(finalsQ);
                subjectReference.child("finalsExTotal").setValue(finalsEx);
                
                */
              /*  Intent intent = new Intent(context.getApplicationContext(),MainActivity.class);*/
                dialog1.dismiss();
               /* context.startActivity(intent);*/

            });
/*

            later.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog1.dismiss();
                }
            });
*/

            dialog1.show();



        });



    }



    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends  RecyclerView.ViewHolder{

        TextView subjectCode,subjectName,subjectSched,subjectRoom;
        Button removeBtn,viewBtn,editBtn;
        ImageView totalAct;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            subjectCode = itemView.findViewById(R.id.subject_code_display);
            subjectName = itemView.findViewById(R.id.subject_name_display);
            subjectSched = itemView.findViewById(R.id.subject_sched_display);
            subjectRoom = itemView.findViewById(R.id.subject_room_display);
            removeBtn = itemView.findViewById(R.id.remove_class);
            viewBtn = itemView.findViewById(R.id.view_class);
            editBtn = itemView.findViewById(R.id.edit_class);
            totalAct = itemView.findViewById(R.id.activity_given);

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
