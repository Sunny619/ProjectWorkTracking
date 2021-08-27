package com.example.ProjectWorkTracking;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

public class ProjectDetailsActivity extends AppCompatActivity {
    int index;
    com.google.android.material.progressindicator.CircularProgressIndicator[] progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);
        Intent intent = getIntent();
        index = intent.getIntExtra("index", 0);
        TextView t = findViewById(R.id.textViewAllDetails);
        t.setText(MainActivity.Projects.get(index).name);
        progressBar = new com.google.android.material.progressindicator.CircularProgressIndicator[3];
        progressBar[0]= findViewById(R.id.progressBar1);
        progressBar[1]= findViewById(R.id.progressBar2);
        progressBar[2]= findViewById(R.id.progressBar3);

        //progressBar.setProgress(70);
        //ObjectAnimator.ofInt(progressBar, "progress", 79).start();
        for(int i = 0;i<3;i++)
        {
               setProgressAnimate(progressBar[i],MainActivity.Projects.get(index).progress[i]);
        }
    }
    public void incrementProgress(View v)
    {
        int pos = Integer.parseInt((String)v.getTag())-1;
        MainActivity.Projects.get(index).progress[pos] += 10;
        setProgressAnimate(progressBar[pos], MainActivity.Projects.get(index).progress[pos]);
        saveData();
    }
    public void decrementProgress(View v)
    {
        int pos = Integer.parseInt((String)v.getTag())-1;
        MainActivity.Projects.get(index).progress[pos] -= 10;
        setProgressAnimate(progressBar[pos], MainActivity.Projects.get(index).progress[pos]);
        saveData();
    }
    private void setProgressAnimate(com.google.android.material.progressindicator.CircularProgressIndicator pb, int progressTo)
    {
        ObjectAnimator animation = ObjectAnimator.ofInt(pb, "progress", pb.getProgress(), progressTo * 100);
        animation.setDuration(500);
        animation.start();
    }
    public void  saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(MainActivity.Projects);
        editor.putString("ProjectsList", json);
        editor.apply();
    }
}