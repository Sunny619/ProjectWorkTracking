package com.example.ProjectWorkTracking;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Arrays;

public class ProjectDetailsActivity extends AppCompatActivity {
    int index;
    String[] priorityStrings = new String[]{"LOW","MEDIUM","HIGH"};
    int[] priorityColors;
    com.google.android.material.progressindicator.CircularProgressIndicator[] progressBar;
    Button priorityButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        priorityColors = new int[]{getResources().getColor(R.color.low_priority),getResources().getColor(R.color.medium_priority),getResources().getColor(R.color.high_priority)};
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);
        Intent intent = getIntent();
        index = intent.getIntExtra("index", 0);
        TextView title = findViewById(R.id.textViewTitle);
        TextView description = findViewById(R.id.textViewDescription);
        title.setText(MainActivity.Projects.get(index).name);
        description.setText(MainActivity.Projects.get(index).description);
        //System.out.println(priorityButtons[0][1]);
        progressBar = new com.google.android.material.progressindicator.CircularProgressIndicator[3];
        progressBar[0]= findViewById(R.id.progressBar1);
        progressBar[1]= findViewById(R.id.progressBar2);
        progressBar[2]= findViewById(R.id.progressBar3);
        priorityButton = findViewById(R.id.buttonPriority);
        setPriority(MainActivity.Projects.get(index).priority-1);
        //progressBar.setProgress(70);
        //ObjectAnimator.ofInt(progressBar, "progress", 79).start();
        for(int i = 0;i<3;i++)
        {
               setProgressAnimate(progressBar[i],MainActivity.Projects.get(index).progress[i]);
        }
    }
    public void priorityButtonClickFunc(View view)
    {
        int priority = Integer.parseInt((String)view.getTag());
        if(priority == 2)
            priority = 0;
        else
            priority++;
        setPriority(priority);
        MainActivity.Projects.get(index).priority = priority+1;
        saveData();
    }
    public void setPriority(int priority)
    {
        priorityButton.setBackgroundColor(priorityColors[priority]);
        priorityButton.setText(priorityStrings[priority]);
        priorityButton.setTag(Integer.toString(priority));
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