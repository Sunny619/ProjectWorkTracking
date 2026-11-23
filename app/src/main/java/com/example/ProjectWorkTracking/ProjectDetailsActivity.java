package com.example.ProjectWorkTracking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.Locale;

/**
 * Activity presenting detailed views of project milestones, interactive budget consumption,
 * phase completion progress rings, and delivery timeline calculations.
 */
public class ProjectDetailsActivity extends AppCompatActivity {

    private int index;
    private Project project;
    private ProjectRepository repository;

    private static final String[] PRIORITY_STRINGS = new String[]{"LOW", "MEDIUM", "HIGH"};
    private int[] priorityColors;

    private CircularProgressIndicator[] progressBar;
    private Button priorityButton;
    private Switch statusSwitch;
    private TextView statusText;
    private TextView maxBudget;
    private TextView usedBudget;
    private TextView maxBudget2;
    private TextView dueDate;
    private TextView daysLeft;
    private TextView title;
    private TextView description;
    private SeekBar budgetSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        repository = ProjectRepository.getInstance(this);

        priorityColors = new int[]{
                ContextCompat.getColor(this, R.color.low_priority),
                ContextCompat.getColor(this, R.color.medium_priority),
                ContextCompat.getColor(this, R.color.high_priority)
        };

        Intent intent = getIntent();
        index = intent.getIntExtra("index", -1);
        project = repository.getProjectAt(index);

        if (project == null) {
            Toast.makeText(this, R.string.error_project_not_found, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initComponents();
        populateData();
        setupListeners();
    }

    private void initComponents() {
        title = findViewById(R.id.textViewTitle);
        description = findViewById(R.id.textViewDescription);

        progressBar = new CircularProgressIndicator[Project.NUM_PHASES];
        progressBar[0] = findViewById(R.id.progressBar1);
        progressBar[1] = findViewById(R.id.progressBar2);
        progressBar[2] = findViewById(R.id.progressBar3);

        priorityButton = findViewById(R.id.buttonPriority);
        statusSwitch = findViewById(R.id.switchStatus);
        statusText = findViewById(R.id.textViewStatus);
        maxBudget = findViewById(R.id.textViewMaxBudget);
        maxBudget2 = findViewById(R.id.textViewMaxBudget2);
        usedBudget = findViewById(R.id.textViewUsedBudget);
        budgetSeekBar = findViewById(R.id.seekBarBudget);
        dueDate = findViewById(R.id.textViewDueDate);
        daysLeft = findViewById(R.id.textViewDaysLeft);
    }

    private void populateData() {
        title.setText(project.getName());
        description.setText(project.getDescription());

        String maxBudgetStr = String.valueOf(project.getBudget());
        maxBudget.setText(maxBudgetStr);
        maxBudget2.setText(maxBudgetStr);
        usedBudget.setText(String.valueOf(project.getBudgetUsed()));

        dueDate.setText(project.getEndDate());

        int priorityIdx = Math.max(0, Math.min(2, project.getPriority() - 1));
        setPriorityUI(priorityIdx);

        statusSwitch.setChecked(project.isStatusActive());
        updateStatusText(project.isStatusActive());

        budgetSeekBar.setMax(project.getBudget());
        budgetSeekBar.setProgress(Math.min(project.getBudgetUsed(), project.getBudget()));

        for (int i = 0; i < Project.NUM_PHASES; i++) {
            if (progressBar[i] != null) {
                setProgressAnimate(progressBar[i], project.getPhaseProgress(i));
            }
        }

        updateDueDateDisplay();
    }

    private void updateDueDateDisplay() {
        Long days = DateUtils.calculateDaysRemaining(project.getEndDate());
        if (days == null) {
            daysLeft.setText(project.getEndDate());
        } else if (days < 0) {
            daysLeft.setText(getString(R.string.status_overdue));
            daysLeft.setTextColor(ContextCompat.getColor(this, R.color.high_priority));
        } else {
            daysLeft.setText(String.format(Locale.getDefault(), "%d days", days));
            daysLeft.setTextColor(ContextCompat.getColor(this, R.color.low_priority));
        }
    }

    private void setupListeners() {
        statusSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateStatusText(isChecked);
            project.setStatus(isChecked);
            repository.save();
        });

        budgetSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                usedBudget.setText(String.valueOf(progress));
                project.setBudgetUsed(progress);
                repository.save();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void updateStatusText(boolean isActive) {
        if (isActive) {
            statusText.setText(getString(R.string.status_active));
            statusText.setTextColor(ContextCompat.getColor(this, R.color.low_priority));
        } else {
            statusText.setText(getString(R.string.status_inactive));
            statusText.setTextColor(ContextCompat.getColor(this, R.color.high_priority));
        }
    }

    public void priorityButtonClickFunc(View view) {
        int currentPriority = 0;
        if (view.getTag() != null) {
            try {
                currentPriority = Integer.parseInt((String) view.getTag());
            } catch (NumberFormatException ignored) {}
        }

        int nextPriority = (currentPriority + 1) % PRIORITY_STRINGS.length;
        setPriorityUI(nextPriority);
        project.setPriority(nextPriority + 1);
        repository.save();
    }

    private void setPriorityUI(int priorityIndex) {
        if (priorityIndex >= 0 && priorityIndex < PRIORITY_STRINGS.length) {
            priorityButton.setBackgroundColor(priorityColors[priorityIndex]);
            priorityButton.setText(PRIORITY_STRINGS[priorityIndex]);
            priorityButton.setTag(String.valueOf(priorityIndex));
        }
    }

    public void incrementProgress(View v) {
        int pos = getPhaseIndexFromView(v);
        if (pos >= 0 && pos < Project.NUM_PHASES) {
            project.incrementPhaseProgress(pos);
            setProgressAnimate(progressBar[pos], project.getPhaseProgress(pos));
            repository.save();
        }
    }

    public void decrementProgress(View v) {
        int pos = getPhaseIndexFromView(v);
        if (pos >= 0 && pos < Project.NUM_PHASES) {
            project.decrementPhaseProgress(pos);
            setProgressAnimate(progressBar[pos], project.getPhaseProgress(pos));
            repository.save();
        }
    }

    private int getPhaseIndexFromView(View v) {
        if (v != null && v.getTag() != null) {
            try {
                return Integer.parseInt((String) v.getTag()) - 1;
            } catch (NumberFormatException ignored) {}
        }
        return -1;
    }

    private void setProgressAnimate(CircularProgressIndicator pb, int progressTo) {
        if (pb == null) return;
        // CircularProgressIndicator scale max is 10000 in layout XML (progressTo * 100 maps 0-100% to 0-10000)
        int target = Math.max(0, Math.min(10000, progressTo * 100));
        ObjectAnimator animation = ObjectAnimator.ofInt(pb, "progress", pb.getProgress(), target);
        animation.setDuration(400);
        animation.start();
    }
}
