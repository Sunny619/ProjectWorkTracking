package com.example.ProjectWorkTracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ProjectDetailsActivity extends AppCompatActivity {
    int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);
        Intent intent = getIntent();
        index = intent.getIntExtra("index", 0);
        TextView t = findViewById(R.id.textViewAllDetails);
        t.setText(MainActivity.Projects.get(index).toString());
    }
}