package com.example.ProjectWorkTracking;

import java.util.Arrays;

public class Project {
    String name, description, startDate, endDate, manager;
    int budget,budgetUsed;
    int priority;
    Boolean[] teams;
    Boolean status;
    int[] progress;

    public Project(String name, String description, String startDate, String endDate, String manager, int budget, int priority, Boolean[] teams, Boolean status, int[] progress) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.manager = manager;
        this.budget = budget;
        this.priority = priority;
        this.teams = teams;
        this.status = status;
        this.progress = progress;
        this.budgetUsed = 0;
    }

    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", manager='" + manager + '\'' +
                ", budget=" + budget +
                ", priority=" + priority +
                ", teams=" + Arrays.toString(teams) +
                ", status=" + status +
                ", progress=" + Arrays.toString(progress) +
                '}';
    }
}
