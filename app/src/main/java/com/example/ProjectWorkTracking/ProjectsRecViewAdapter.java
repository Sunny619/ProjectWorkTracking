package com.example.ProjectWorkTracking;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ProjectsRecViewAdapter extends RecyclerView.Adapter<ProjectsRecViewAdapter.ViewHolder> {
    private ArrayList<Project> projects = new ArrayList<>();
    private final Context context;
    public ProjectsRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_template, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectsRecViewAdapter.ViewHolder holder, int Position) {
       //set
        holder.cardTitle.setText(projects.get(holder.getAdapterPosition()).name);
        holder.description.setText(projects.get(holder.getAdapterPosition()).description);
        holder.description2.setText("Manager: " + projects.get(holder.getAdapterPosition()).manager + "       Budget: "
                + projects.get(holder.getAdapterPosition()).budget);
        holder.description3.setText("Active: " + projects.get(holder.getAdapterPosition()).status +  "       Due Date: "
                + projects.get(holder.getAdapterPosition()).endDate);
        holder.image.setImageResource(R.drawable.imagepro);
        holder.delete.setOnClickListener(v -> {
            showDialog(projects.get(holder.getAdapterPosition()).name, holder.getAdapterPosition());

        });
        holder.details.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProjectDetailsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("index",holder.getAdapterPosition());
            context.startActivity(intent);
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

        //private final com.google.android.material.card.MaterialCardView card;
        private final TextView cardTitle;
        private final TextView description;
        private final TextView description2;
        private final TextView description3;
        private final ImageView image;
        private final com.google.android.material.button.MaterialButton delete;
        private final com.google.android.material.button.MaterialButton details;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //card = itemView.findViewById(R.id.card);
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
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(MainActivity.Projects);
        editor.putString("ProjectsList", json);
        editor.apply();
    }
    public void showDialog(String Title, int pos)
    {
        new MaterialAlertDialogBuilder(context)
                .setTitle(Title)
                .setMessage("Delete Project?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("Working");
                        projects.remove(pos);
                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(pos,getItemCount());
                        saveData();
                    }
                })
        .show();
    }

}
