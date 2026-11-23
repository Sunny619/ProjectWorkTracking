package com.example.ProjectWorkTracking;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Modernized RecyclerView Adapter for displaying project summary cards with DiffUtil support.
 */
public class ProjectsRecViewAdapter extends RecyclerView.Adapter<ProjectsRecViewAdapter.ViewHolder> {

    public interface OnProjectDeletedListener {
        void onProjectDeleted(int position, Project project);
    }

    private final List<Project> projects = new ArrayList<>();
    private final Context context;
    private OnProjectDeletedListener deleteListener;

    public ProjectsRecViewAdapter(Context context) {
        this.context = context;
    }

    public void setOnProjectDeletedListener(OnProjectDeletedListener listener) {
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_template, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int bindingPos = holder.getBindingAdapterPosition();
        if (bindingPos == RecyclerView.NO_POSITION || bindingPos >= projects.size()) {
            return;
        }

        Project current = projects.get(bindingPos);
        holder.cardTitle.setText(current.getName());
        holder.description.setText(current.getDescription());
        holder.description2.setText(context.getString(R.string.manager_budget_format,
                current.getManager(), current.getBudget()));
        holder.description3.setText(context.getString(R.string.status_duedate_format,
                String.valueOf(current.getStatus()), current.getEndDate()));
        holder.image.setImageResource(R.drawable.imagepro);

        holder.delete.setOnClickListener(v -> {
            int currentPos = holder.getBindingAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION && currentPos < projects.size()) {
                showDialog(projects.get(currentPos).getName(), currentPos);
            }
        });

        holder.details.setOnClickListener(v -> {
            int currentPos = holder.getBindingAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION && currentPos < projects.size()) {
                Intent intent = new Intent(context, ProjectDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("index", currentPos);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    /**
     * Efficiently updates the project list using DiffUtil.
     */
    public void setProjects(List<Project> newProjects) {
        final List<Project> oldList = new ArrayList<>(this.projects);
        final List<Project> newList = newProjects != null ? new ArrayList<>(newProjects) : new ArrayList<>();

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldList.size();
            }

            @Override
            public int getNewListSize() {
                return newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                Project oldItem = oldList.get(oldItemPosition);
                Project newItem = newList.get(newItemPosition);
                return oldItem.getName() != null && oldItem.getName().equals(newItem.getName());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                Project oldItem = oldList.get(oldItemPosition);
                Project newItem = newList.get(newItemPosition);
                return oldItem.equals(newItem);
            }
        });

        this.projects.clear();
        this.projects.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView cardTitle;
        final TextView description;
        final TextView description2;
        final TextView description3;
        final ImageView image;
        final MaterialButton delete;
        final MaterialButton details;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardTitle = itemView.findViewById(R.id.card_title);
            description = itemView.findViewById(R.id.description);
            description2 = itemView.findViewById(R.id.description2);
            description3 = itemView.findViewById(R.id.description3);
            image = itemView.findViewById(R.id.image);
            delete = itemView.findViewById(R.id.delete);
            details = itemView.findViewById(R.id.detailsButton);
        }
    }

    public void showDialog(String title, int pos) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(context.getString(R.string.delete_project_confirm))
                .setNegativeButton(context.getString(R.string.action_cancel), null)
                .setPositiveButton(context.getString(R.string.action_delete), (dialog, which) -> {
                    if (pos >= 0 && pos < projects.size()) {
                        Project removedProject = projects.get(pos);
                        ProjectRepository.getInstance(context).deleteProject(pos);
                        // Also maintain backwards-compatibility if MainActivity.Projects is still referenced
                        if (MainActivity.Projects != null && pos < MainActivity.Projects.size()) {
                            MainActivity.Projects.remove(pos);
                        }
                        projects.remove(pos);
                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(pos, getItemCount());

                        if (deleteListener != null) {
                            deleteListener.onProjectDeleted(pos, removedProject);
                        }
                    }
                })
                .show();
    }
}
