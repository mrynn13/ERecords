package com.example.erecordsv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;


public class CalendarActivity extends AppCompatActivity {
    // Initialize variable
    CompactCalendarView compactCalendar;
    Dialog dialog;
    TextView date;
    String dates;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    FirebaseDatabase root;
    DatabaseReference ref;

    RecyclerView recyclerView;
    EventAdapter eventAdapter;
    ArrayList<EventModel> list;
    DatabaseReference reference;

    Button addEv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        date = findViewById(R.id.textMonth);
        recyclerView = findViewById(R.id.eventList);
        addEv = findViewById(R.id.addEv);
        Date dateToday = new Date();

        if (GlobalVar.user.equals("teacher")){
            addEv.setVisibility(View.VISIBLE);
        }

        root = FirebaseDatabase.getInstance();

        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);

        date.setText(dateFormatMonth.format(dateToday));

        //Set an event foredel fat adha
        Event ev1 = new Event(Color.GREEN, 1657324800000L, "Eid al-Adha");
        compactCalendar.addEvent(ev1);
        Event ev2 = new Event(Color.GREEN, 1661731200000L, "National Heroes Day");
        compactCalendar.addEvent(ev2);
        Event ev3 = new Event(Color.GREEN, 1669680000000L, "Bonifacio Day");
        compactCalendar.addEvent(ev3);
        Event ev4 = new Event(Color.GREEN, 1670457600000L, "Immaculate");
        compactCalendar.addEvent(ev4);
        Event ev5 = new Event(Color.GREEN, 1671926400000L, "Christmas Day");
        compactCalendar.addEvent(ev5);
        Event ev6 = new Event(Color.GREEN, 1672358400000L, "Rizal Day");
        compactCalendar.addEvent(ev6);

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();

//                if (dateClicked.toString().compareTo("Fri Oct 21 00:00:00 AST 2016") == 0) {
//                    Toast.makeText(context, "Teachers' Professional Day", Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(context, "No Events Planned for that day", Toast.LENGTH_SHORT).show();
//                }


            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                date.setText(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });

        reference = root.getReference("Events");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        eventAdapter = new EventAdapter(this,list);
        recyclerView.setAdapter(eventAdapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChildren()){
                    Toast.makeText(CalendarActivity.this, "No Events Yet", Toast.LENGTH_SHORT).show();
                }
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                    EventModel classModel = dataSnapshot.getValue(EventModel.class);
                    list.add(classModel);
                }

                eventAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public void AddEvents(View view) {
        dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.popup_event);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

       EditText evName = dialog.findViewById(R.id.event_name_et);
        EditText evdate = dialog.findViewById(R.id.event_date);
        Button saveBtn = dialog.findViewById(R.id.save_btn);
        Button cancelBtn = dialog.findViewById(R.id.cancel_btn);
        evdate.setFocusable(false);
        Calendar calendar;
        calendar = Calendar.getInstance();

        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);



        evdate.setOnClickListener(v->{
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    CalendarActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month = month+1;
                    dates = month+"-"+day+"-"+year;
                    evdate.setText(dates);

                }
            },year,month,day);
            datePickerDialog.show();

        });

        dialog.show();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventName = evName.getText().toString().trim();
                String eventDate = evdate.getText().toString();
                ref = root.getReference("Events");


                if (TextUtils.isEmpty(eventName)){
                    evName.setError("Required");
                }
                if( TextUtils.isEmpty(eventDate)){
                    evdate.setError("Required");
                }
                String randId = UUID.randomUUID().toString();


                if (!TextUtils.isEmpty(eventName) && !TextUtils.isEmpty(eventDate)){
                    //insert data
                    EventModel eventModel = new EventModel(eventName,eventDate,randId);
                    ref.child(randId).setValue(eventModel);
                    finish();
                    overridePendingTransition(0,0);
                    startActivity(getIntent());
                    overridePendingTransition(0,0);
                    Toast.makeText( CalendarActivity.this, "Event Added", Toast.LENGTH_SHORT).show();

                }else{
                    evName.setError("Required");
                    evdate.setError("Required");
                }
                dialog.dismiss();

            }
        });

        cancelBtn.setOnClickListener((View view1)->{
            //cancel
            dialog.dismiss();
        });


    }

    @Override
    public void onBackPressed() {
        if (GlobalVar.user.equalsIgnoreCase("teacher")){
            Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
            startActivity(intent);
            finishAffinity();
        }else{
            super.onBackPressed();
        }

    }
}