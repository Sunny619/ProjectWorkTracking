package com.example.ProjectWorkTracking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * Main activity displaying the list of all tracked projects in a RecyclerView.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Maintained for backward compatibility with existing components.
     * Backed by ProjectRepository.
     */
    public static ArrayList<Project> Projects = new ArrayList<>();

    private RecyclerView projectsRecView;
    private ProjectsRecViewAdapter adapter;
    private ProjectRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repository = ProjectRepository.getInstance(this);

        projectsRecView = findViewById(R.id.Recycle);
        projectsRecView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ProjectsRecViewAdapter(this);
        projectsRecView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        if (fab != null) {
            fab.setOnClickListener(this::addNewProject);
        }

        refreshData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    /**
     * Reloads project list from repository and refreshes the adapter.
     */
    private void refreshData() {
        if (repository != null) {
            repository.reload();
            Projects = repository.getProjectList();
            if (adapter != null) {
                adapter.setProjects(repository.getAllProjects());
            }
        }
    }

    /**
     * Navigates to the AddNewProjectActivity screen.
     */
    public void addNewProject(View view) {
        Intent intent = new Intent(this, AddNewProjectActivity.class);
        startActivity(intent);
    }
}
