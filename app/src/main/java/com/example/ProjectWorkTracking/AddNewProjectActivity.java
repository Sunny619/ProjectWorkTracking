package com.example.ProjectWorkTracking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Calendar;

public class AddNewProjectActivity extends AppCompatActivity {
    private int mYear, mMonth, mDay;
    EditText startDate;
    EditText endDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_project);
        Intent intent = getIntent();
        startDate = findViewById(R.id.editTextStartDate);
        endDate = findViewById(R.id.editTextEndDate);
        listeners();
    }
    void listeners()
    {
        startDate.setOnClickListener(v-> {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            System.out.println("workingggg");

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            startDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();

        });
        endDate.setOnClickListener(v-> {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            endDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();

        });
    }
    public void addProject(View view) {
        EditText name = findViewById(R.id.editTextPname);
        EditText description = findViewById(R.id.editTextPdescription);


        EditText manager = findViewById(R.id.editTextTextPmanager);
        EditText budget = findViewById(R.id.editTextBudget);
        Spinner priority  = findViewById(R.id.spinner);
        CheckBox tech = findViewById(R.id.checkBox);
        CheckBox finance = findViewById(R.id.checkBox2);
        CheckBox marketing = findViewById(R.id.checkBox3);
        RadioGroup status = findViewById(R.id.radioGroupStatus);
        Boolean[] teams = new Boolean[3];
        teams[0] = tech.isChecked();
        teams[1] = finance.isChecked();
        teams[2] = marketing.isChecked();
        Boolean status1= false;
        if(status.getCheckedRadioButtonId() == R.id.radioButton2)
            status1 = true;
        int priority1;
        if(priority.getSelectedItem().toString().equals("High"))
            priority1=3;
        else if(priority.getSelectedItem().toString().equals("Medium"))
            priority1=2;
        else if(priority.getSelectedItem().toString().equals("Low"))
            priority1=1;
        else
            priority1=0;

        Project project = new Project(name.getText().toString(), description.getText().toString(), startDate.getText().toString(),
                endDate.getText().toString(), manager.getText().toString(),Integer.parseInt(budget.getText().toString()),
                priority1, teams, status1, new int[]{0,0,0});
        //System.out.println(project.toString());
        boolean duplicate = false;
        for(int i =0; i<MainActivity.Projects.size();i++)
        {
            if(MainActivity.Projects.get(i).name.equals(name.getText().toString()) )
            {
                duplicate =true;
                break;
            }

        }
        if(duplicate)
        {
            Toast.makeText(this, "This Project name already exists!", Toast.LENGTH_SHORT).show();
            System.out.println("Duplicate");
        }
        else
        {
            MainActivity.Projects.add(project);
            //System.out.println(MainActivity.Projects.toString());
            saveData();
            Toast.makeText(this, "Project added Successfully!", Toast.LENGTH_SHORT).show();
            System.out.println("Created");
        }

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