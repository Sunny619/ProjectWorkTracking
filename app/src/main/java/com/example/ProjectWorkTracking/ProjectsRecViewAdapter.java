package com.example.ProjectWorkTracking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            cardTitle = itemView.findViewById(R.id.card_title);
            description = itemView.findViewById(R.id.description);
            description2 = itemView.findViewById(R.id.description2);
            description3 = itemView.findViewById(R.id.description3);
            image = itemView.findViewById(R.id.image);
        }
    }
}
