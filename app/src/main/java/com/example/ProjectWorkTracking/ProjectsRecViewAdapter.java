package com.example.ProjectWorkTracking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class ProjectsRecViewAdapter extends RecyclerView.Adapter<ProjectsRecViewAdapter.ViewHolder> {
    private ArrayList<Project> projects = new ArrayList<>();
    public ProjectsRecViewAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_template, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectsRecViewAdapter.ViewHolder holder, int position) {
       //set
        holder.cardTitle.setText(projects.get(position).name);
        holder.description.setText(projects.get(position).description);
        holder.description2.setText("Manager: " + projects.get(position).manager + "       Budget: "
                + projects.get(position).budget);
        holder.description3.setText("Active: " + projects.get(position).status +  "       Due Date: "
                + projects.get(position).endDate);
        holder.image.setImageResource(R.drawable.imagepro);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                projects.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,getItemCount());
                saveData();
            }
        });
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.getContextOfApplication(), ProjectDetailsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);;
                intent.putExtra("index",position);
                MainActivity.getContextOfApplication().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private com.google.android.material.card.MaterialCardView card;
        private TextView cardTitle;
        private TextView description;
        private TextView description2;
        private TextView description3;
        private ImageView image;
        private com.google.android.material.button.MaterialButton delete;
        private com.google.android.material.button.MaterialButton details;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            cardTitle = itemView.findViewById(R.id.card_title);
            description = itemView.findViewById(R.id.description);
            description2 = itemView.findViewById(R.id.description2);
            description3 = itemView.findViewById(R.id.description3);
            image = itemView.findViewById(R.id.image);
            delete = itemView.findViewById(R.id.delete);
            details = itemView.findViewById(R.id.detailsButton);
        }
    }
    public void  saveData(){
        Context applicationContext = MainActivity.getContextOfApplication();
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences("shared preferences", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(MainActivity.Projects);
        editor.putString("ProjectsList", json);
        editor.apply();
    }

}
