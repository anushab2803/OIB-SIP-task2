package com.example.todoapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText titleInput, descriptionInput;
    Button addButton, logoutButton;
    ListView taskList;
    TaskAdapter adapter;
    ArrayList<Task> tasks;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        titleInput = findViewById(R.id.title);
        descriptionInput = findViewById(R.id.description);
        addButton = findViewById(R.id.addButton);
        logoutButton = findViewById(R.id.logoutButton);
        taskList = findViewById(R.id.taskList);

        tasks = new ArrayList<>();
        adapter = new TaskAdapter(this, tasks);
        taskList.setAdapter(adapter);

        Intent intent = getIntent();
        userId = intent.getIntExtra("user_id", -1);

        loadTasks();

        addButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString();
            String description = descriptionInput.getText().toString();
            if (!title.isEmpty() && !description.isEmpty()) {
                if (db.addTask(title, description, userId)) {
                    Toast.makeText(this, "Task Added", Toast.LENGTH_SHORT).show();
                    loadTasks();
                }
            } else {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        logoutButton.setOnClickListener(v -> {
            Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(logoutIntent);
            finish();
        });
    }

    private void loadTasks() {
        Cursor cursor = db.getTasks(userId);
        tasks.clear();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                tasks.add(new Task(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
            }
            adapter.notifyDataSetChanged();
        }
    }
}
