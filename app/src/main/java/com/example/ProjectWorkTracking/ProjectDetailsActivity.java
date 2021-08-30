package com.example.ProjectWorkTracking;

import static java.lang.String.valueOf;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ProjectDetailsActivity extends AppCompatActivity {
    int index;
    String[] priorityStrings = new String[]{"LOW","MEDIUM","HIGH"};
    int[] priorityColors;
    com.google.android.material.progressindicator.CircularProgressIndicator[] progressBar;
    Button priorityButton;
    Switch status;
    TextView statusText, maxBudget, usedBudget, maxBudget2, dueDate, daysLeft;
    SeekBar budget;
    SimpleDateFormat sdf;
    Date enddate, curDate;
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
        progressBar = new com.google.android.material.progressindicator.CircularProgressIndicator[3];
        progressBar[0]= findViewById(R.id.progressBar1);
        progressBar[1]= findViewById(R.id.progressBar2);
        progressBar[2]= findViewById(R.id.progressBar3);
        priorityButton = findViewById(R.id.buttonPriority);
        status = findViewById(R.id.switchStatus);
        statusText = findViewById(R.id.textViewStatus);
        maxBudget = findViewById(R.id.textViewMaxBudget);
        maxBudget2 = findViewById(R.id.textViewMaxBudget2);
        usedBudget = findViewById(R.id.textViewUsedBudget);
        budget = findViewById(R.id.seekBarBudget);
        dueDate = findViewById(R.id.textViewDueDate);
        daysLeft = findViewById(R.id.textViewDaysLeft);
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            enddate = sdf.parse(MainActivity.Projects.get(index).endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        curDate = new Date();
        long diff = TimeUnit.DAYS.convert(enddate.getTime() - curDate.getTime(), TimeUnit.MILLISECONDS);
        if(diff<0)
            daysLeft.setText("Overdue");
        else
            daysLeft.setText(String.valueOf(diff));
        setPriority(MainActivity.Projects.get(index).priority-1);
        status.setChecked(MainActivity.Projects.get(index).status);
        setPriorityText(MainActivity.Projects.get(index).status);
        maxBudget.setText(Integer.toString(MainActivity.Projects.get(index).budget));
        maxBudget2.setText(Integer.toString(MainActivity.Projects.get(index).budget));
        usedBudget.setText(Integer.toString(MainActivity.Projects.get(index).budgetUsed));
        dueDate.setText(MainActivity.Projects.get(index).endDate);
        budget.setMax(MainActivity.Projects.get(index).budget);
        budget.setProgress(MainActivity.Projects.get(index).budgetUsed);
        for(int i = 0;i<3;i++)
        {
               setProgressAnimate(progressBar[i],MainActivity.Projects.get(index).progress[i]);
        }

        status.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setPriorityText(isChecked);
            MainActivity.Projects.get(index).status = isChecked;
            saveData();
        });
        budget.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setBudgetText(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    void initComp()
    {

    }
    public void setBudgetText(int progress)
    {
        usedBudget.setText(Integer.toString(progress));
        MainActivity.Projects.get(index).budgetUsed = progress;
        saveData();
    }
    public void setPriorityText(Boolean isChecked)
    {
        if(isChecked)
        {
            statusText.setText("ACTIVE");
            statusText.setTextColor(getResources().getColor(R.color.low_priority));
        }
        else
        {
            statusText.setText("INACTIVE");
            statusText.setTextColor(getResources().getColor(R.color.high_priority));
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