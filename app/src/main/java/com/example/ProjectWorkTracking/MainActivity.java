package com.example.ProjectWorkTracking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.ProjectWorkTracking.MESSAGE";
    public static ArrayList<Project> Projects;
    public RecyclerView projectsRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Projects == null)
            Projects = new ArrayList<>();
//        Boolean[] a = new Boolean[3];
//        a[0] = true;
//        a[1] = true;
//        a[2] = true;
//        Projects.add(new Project("2","2","2","2","2", 2,3, a, false ));
//        Projects.add(new Project("2","2","2","2","2", 2,3, a, false ));
//        Projects.add(new Project("2","2","2","2","2", 2,3, a, false ));
        projectsRecView = findViewById(R.id.Recycle);
        ProjectsRecViewAdapter adapter = new ProjectsRecViewAdapter();
        adapter.setProjects(Projects);
        projectsRecView.setAdapter(adapter);
        projectsRecView.setLayoutManager(new LinearLayoutManager(this));
    }
    public void addNewProject(View view) {
        Intent intent = new Intent(this, AddNewProjectActivity.class);
        startActivity(intent);
    }
    //TODO: create functionality for Add new Project button
}