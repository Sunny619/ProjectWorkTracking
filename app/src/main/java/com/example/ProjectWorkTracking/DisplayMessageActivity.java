package com.example.ProjectWorkTracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        TextView textView = findViewById(R.id.textView);
        textView.setText(message+"\n This is a test project!");
    }
    public void increaseProgress(View view) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar3);
        progressBar.incrementProgressBy(10);
    }
    public void decreaseProgress(View view) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar3);
        progressBar.incrementProgressBy(-10);
    }
}