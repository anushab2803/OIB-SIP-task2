package com.example.todoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class TaskAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Task> tasks;
    private DatabaseHelper db;

    public TaskAdapter(Context context, ArrayList<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
        db = new DatabaseHelper(context);
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tasks.get(position).getId();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
        }

        Task task = tasks.get(position);

        TextView titleView = convertView.findViewById(R.id.taskTitle);
        TextView descriptionView = convertView.findViewById(R.id.taskDescription);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);

        titleView.setText(task.getTitle());
        descriptionView.setText(task.getDescription());

        deleteButton.setOnClickListener(v -> {
            db.deleteTask(task.getId());
            tasks.remove(position);
            notifyDataSetChanged();
        });

        return convertView;
    }
}
