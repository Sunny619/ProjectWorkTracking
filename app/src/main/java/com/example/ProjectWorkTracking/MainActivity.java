package com.example.ProjectWorkTracking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<Project> Projects;
    public RecyclerView projectsRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadData();
        projectsRecView = findViewById(R.id.Recycle);
        ProjectsRecViewAdapter adapter = new ProjectsRecViewAdapter(this);
        adapter.setProjects(Projects);
        projectsRecView.setAdapter(adapter);
        projectsRecView.setLayoutManager(new LinearLayoutManager(this));
    }
    public void addNewProject(View view) {
        Intent intent = new Intent(this, AddNewProjectActivity.class);
        startActivity(intent);
    }
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("ProjectsList", null);
        Type type = new TypeToken<ArrayList<Project>>() {}.getType();
        Projects = gson.fromJson(json, type);

        if (Projects == null) {
            Projects = new ArrayList<>();
        }
    }
    //TODO: create functionality for Add new Project button
}