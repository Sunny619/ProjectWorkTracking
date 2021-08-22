package com.example.ProjectWorkTracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.ProjectWorkTracking.MESSAGE";
    public static ArrayList<Project> Projects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Projects = new ArrayList<>();
    }
    public void addNewProject(View view) {
        Intent intent = new Intent(this, AddNewProjectActivity.class);
        startActivity(intent);
    }
    //TODO: create functionality for Add new Project button
}