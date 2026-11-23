package com.example.ProjectWorkTracking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Activity for creating and configuring a new Project with comprehensive validation.
 */
public class AddNewProjectActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextDescription;
    private EditText editTextManager;
    private EditText editTextBudget;
    private EditText editTextStartDate;
    private EditText editTextEndDate;
    private Spinner spinnerPriority;
    private CheckBox checkTech;
    private CheckBox checkFinance;
    private CheckBox checkMarketing;
    private RadioGroup radioGroupStatus;

    private ProjectRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_project);

        repository = ProjectRepository.getInstance(this);

        initViews();
        setupDatePickerListeners();
    }

    private void initViews() {
        editTextName = findViewById(R.id.editTextPname);
        editTextDescription = findViewById(R.id.editTextPdescription);
        editTextManager = findViewById(R.id.editTextTextPmanager);
        editTextBudget = findViewById(R.id.editTextBudget);
        editTextStartDate = findViewById(R.id.editTextStartDate);
        editTextEndDate = findViewById(R.id.editTextEndDate);
        spinnerPriority = findViewById(R.id.spinner);
        checkTech = findViewById(R.id.checkBox);
        checkFinance = findViewById(R.id.checkBox2);
        checkMarketing = findViewById(R.id.checkBox3);
        radioGroupStatus = findViewById(R.id.radioGroupStatus);
    }

    private void setupDatePickerListeners() {
        editTextStartDate.setOnClickListener(v -> showDatePicker(editTextStartDate));
        editTextEndDate.setOnClickListener(v -> showDatePicker(editTextEndDate));
    }

    private void showDatePicker(EditText targetField) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formatted = DateUtils.formatDate(selectedDay, selectedMonth, selectedYear);
                    targetField.setText(formatted);
                }, year, month, day);
        datePickerDialog.show();
    }

    public void addProject(View view) {
        String name = editTextName.getText().toString().trim();
        if (name.isEmpty()) {
            editTextName.setError(getString(R.string.error_empty_name));
            editTextName.requestFocus();
            return;
        }

        String description = editTextDescription.getText().toString().trim();
        String manager = editTextManager.getText().toString().trim();
        String startDate = editTextStartDate.getText().toString().trim();
        String endDate = editTextEndDate.getText().toString().trim();

        if (startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, R.string.error_empty_dates, Toast.LENGTH_SHORT).show();
            return;
        }

        String budgetStr = editTextBudget.getText().toString().trim();
        int budget;
        try {
            budget = Integer.parseInt(budgetStr);
            if (budget < 0) {
                editTextBudget.setError(getString(R.string.error_invalid_budget));
                editTextBudget.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            editTextBudget.setError(getString(R.string.error_invalid_budget));
            editTextBudget.requestFocus();
            return;
        }

        if (repository.isProjectNameTaken(name)) {
            Toast.makeText(this, R.string.error_duplicate_name, Toast.LENGTH_SHORT).show();
            return;
        }

        Boolean[] teams = new Boolean[]{
                checkTech.isChecked(),
                checkFinance.isChecked(),
                checkMarketing.isChecked()
        };

        boolean isActive = radioGroupStatus.getCheckedRadioButtonId() == R.id.radioButton2;

        int priorityLevel;
        String priorityStr = spinnerPriority.getSelectedItem() != null ?
                spinnerPriority.getSelectedItem().toString() : "";
        if ("High".equalsIgnoreCase(priorityStr)) {
            priorityLevel = Project.PRIORITY_HIGH;
        } else if ("Medium".equalsIgnoreCase(priorityStr)) {
            priorityLevel = Project.PRIORITY_MEDIUM;
        } else if ("Low".equalsIgnoreCase(priorityStr)) {
            priorityLevel = Project.PRIORITY_LOW;
        } else {
            priorityLevel = Project.PRIORITY_NONE;
        }

        Project newProject = new Project(name, description, startDate, endDate, manager,
                budget, priorityLevel, teams, isActive, new int[]{0, 0, 0});

        repository.addProject(newProject);

        Toast.makeText(this, R.string.project_created_success, Toast.LENGTH_SHORT).show();
        finish();
    }
}
