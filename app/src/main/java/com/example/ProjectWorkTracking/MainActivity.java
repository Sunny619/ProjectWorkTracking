package com.example.ProjectWorkTracking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.ProjectWorkTracking.MESSAGE";
    public static ArrayList<Project> Projects;
    public RecyclerView projectsRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();
        buildRecyclerView();


        Button buttonSave = findViewById(R.id.button_save);
//        buttonSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveData();
//            }
//        });





    }

    private void buildRecyclerView() {
        projectsRecView = findViewById(R.id.Recycle);
        ProjectsRecViewAdapter adapter = new ProjectsRecViewAdapter();
        adapter.setProjects(Projects);
        projectsRecView.setAdapter(adapter);
        projectsRecView.setLayoutManager(new LinearLayoutManager(this));
    }



    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<Project>>() {}.getType();
        Projects = gson.fromJson(json, type);

        if (Projects == null) {
            Projects = new ArrayList<>();
        }
    }

    public void addNewProject(View view) {
        Intent intent = new Intent(this, AddNewProjectActivity.class);
        startActivity(intent);
    }
    //TODO: create functionality for Add new Project button



}