package com.example.myschedule30;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

public class ProgresActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressAdapter adapter;
    private List<ScheduleItem> completedTasks;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progres);

        dbHelper = new DatabaseHelper(this);
        completedTasks = dbHelper.getCompletedTasks();

        recyclerView = findViewById(R.id.progressRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProgressAdapter(completedTasks);
        recyclerView.setAdapter(adapter);
    }
}
