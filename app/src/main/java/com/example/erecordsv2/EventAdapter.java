package com.example.erecordsv2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder>{
    FirebaseDatabase root;
    DatabaseReference ref;
    Context context;
    ArrayList<EventModel> list;

    public EventAdapter(Context context, ArrayList<EventModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event,parent,false);
        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        EventModel eventModel = list.get(position);
        root = FirebaseDatabase.getInstance();
        if (GlobalVar.user.equals("teacher")){
            holder.trash.setVisibility(View.VISIBLE);
        }
        ref = root.getReference("Events");
        holder.evName.setText(eventModel.getEventName());
        holder.evDate.setText(eventModel.getEventDate());
        holder.trash.setOnClickListener(v->{
            ref.child(eventModel.getEventId()).removeValue();
            Toast.makeText(context, "Removed Event", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(v.getContext(), CalendarActivity.class);
            context.startActivity(intent);
            ((Activity)context).finishAffinity();
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView evName,evDate;
        ImageView trash;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            evName = itemView.findViewById(R.id.eventNameDisplay);
            evDate = itemView.findViewById(R.id.eventDateDisplay);
            trash = itemView.findViewById(R.id.delEvent);
        }
    }
}
