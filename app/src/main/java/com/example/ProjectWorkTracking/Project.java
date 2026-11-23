package com.example.ProjectWorkTracking;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Model class representing a project, its allocated resources, cross-functional teams,
 * budget utilization, and phase completion milestones.
 */
public class Project implements Serializable {

    public static final int NUM_PHASES = 3;
    public static final int PHASE_DESIGN = 0;
    public static final int PHASE_DEVELOPMENT = 1;
    public static final int PHASE_TESTING = 2;

    public static final int NUM_TEAMS = 3;
    public static final int TEAM_TECH = 0;
    public static final int TEAM_FINANCE = 1;
    public static final int TEAM_MARKETING = 2;

    public static final int PRIORITY_NONE = 0;
    public static final int PRIORITY_LOW = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_HIGH = 3;

    public static final int PROGRESS_MIN = 0;
    public static final int PROGRESS_MAX = 100;
    public static final int PROGRESS_STEP = 10;

    // Fields maintained for serialization compatibility with Gson
    String name;
    String description;
    String startDate;
    String endDate;
    String manager;
    int budget;
    int budgetUsed;
    int priority;
    Boolean[] teams;
    Boolean status;
    int[] progress;

    public Project() {
        this("", "", "", "", "", 0, PRIORITY_LOW, new Boolean[]{false, false, false}, false, new int[]{0, 0, 0});
    }

    public Project(String name, String description, String startDate, String endDate,
                   String manager, int budget, int priority, Boolean[] teams,
                   Boolean status, int[] progress) {
        this(name, description, startDate, endDate, manager, budget, 0, priority, teams, status, progress);
    }

    public Project(String name, String description, String startDate, String endDate,
                   String manager, int budget, int budgetUsed, int priority,
                   Boolean[] teams, Boolean status, int[] progress) {
        this.name = name != null ? name : "";
        this.description = description != null ? description : "";
        this.startDate = startDate != null ? startDate : "";
        this.endDate = endDate != null ? endDate : "";
        this.manager = manager != null ? manager : "";
        this.budget = Math.max(0, budget);
        this.budgetUsed = Math.max(0, budgetUsed);
        this.priority = priority;

        if (teams != null && teams.length == NUM_TEAMS) {
            this.teams = teams.clone();
        } else {
            this.teams = new Boolean[]{false, false, false};
        }

        this.status = status != null ? status : false;

        if (progress != null && progress.length == NUM_PHASES) {
            this.progress = new int[NUM_PHASES];
            for (int i = 0; i < NUM_PHASES; i++) {
                this.progress[i] = clampProgress(progress[i]);
            }
        } else {
            this.progress = new int[]{0, 0, 0};
        }
    }

    // --- Getters and Setters ---

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name : "";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description != null ? description : "";
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate != null ? startDate : "";
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate != null ? endDate : "";
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager != null ? manager : "";
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = Math.max(0, budget);
    }

    public int getBudgetUsed() {
        return budgetUsed;
    }

    public void setBudgetUsed(int budgetUsed) {
        this.budgetUsed = Math.max(0, budgetUsed);
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Boolean[] getTeams() {
        return teams != null ? teams.clone() : new Boolean[]{false, false, false};
    }

    public void setTeams(Boolean[] teams) {
        if (teams != null && teams.length == NUM_TEAMS) {
            this.teams = teams.clone();
        }
    }

    public boolean isTeamAssigned(int teamIndex) {
        if (teams != null && teamIndex >= 0 && teamIndex < teams.length) {
            return teams[teamIndex] != null && teams[teamIndex];
        }
        return false;
    }

    public void setTeamAssigned(int teamIndex, boolean assigned) {
        if (teams != null && teamIndex >= 0 && teamIndex < teams.length) {
            teams[teamIndex] = assigned;
        }
    }

    public Boolean getStatus() {
        return status != null ? status : false;
    }

    public boolean isStatusActive() {
        return status != null && status;
    }

    public void setStatus(Boolean status) {
        this.status = status != null ? status : false;
    }

    public int[] getProgress() {
        return progress != null ? progress.clone() : new int[]{0, 0, 0};
    }

    public void setProgress(int[] progress) {
        if (progress != null && progress.length == NUM_PHASES) {
            this.progress = new int[NUM_PHASES];
            for (int i = 0; i < NUM_PHASES; i++) {
                this.progress[i] = clampProgress(progress[i]);
            }
        }
    }

    public int getPhaseProgress(int phaseIndex) {
        if (progress != null && phaseIndex >= 0 && phaseIndex < progress.length) {
            return progress[phaseIndex];
        }
        return 0;
    }

    public void setPhaseProgress(int phaseIndex, int value) {
        if (progress != null && phaseIndex >= 0 && phaseIndex < progress.length) {
            progress[phaseIndex] = clampProgress(value);
        }
    }

    public void incrementPhaseProgress(int phaseIndex) {
        if (progress != null && phaseIndex >= 0 && phaseIndex < progress.length) {
            progress[phaseIndex] = clampProgress(progress[phaseIndex] + PROGRESS_STEP);
        }
    }

    public void decrementPhaseProgress(int phaseIndex) {
        if (progress != null && phaseIndex >= 0 && phaseIndex < progress.length) {
            progress[phaseIndex] = clampProgress(progress[phaseIndex] - PROGRESS_STEP);
        }
    }

    public static int clampProgress(int value) {
        if (value < PROGRESS_MIN) return PROGRESS_MIN;
        if (value > PROGRESS_MAX) return PROGRESS_MAX;
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return budget == project.budget &&
                budgetUsed == project.budgetUsed &&
                priority == project.priority &&
                Objects.equals(name, project.name) &&
                Objects.equals(description, project.description) &&
                Objects.equals(startDate, project.startDate) &&
                Objects.equals(endDate, project.endDate) &&
                Objects.equals(manager, project.manager) &&
                Arrays.equals(teams, project.teams) &&
                Objects.equals(status, project.status) &&
                Arrays.equals(progress, project.progress);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, description, startDate, endDate, manager, budget, budgetUsed, priority, status);
        result = 31 * result + Arrays.hashCode(teams);
        result = 31 * result + Arrays.hashCode(progress);
        return result;
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
                ", budgetUsed=" + budgetUsed +
                ", priority=" + priority +
                ", teams=" + Arrays.toString(teams) +
                ", status=" + status +
                ", progress=" + Arrays.toString(progress) +
                '}';
    }
}
