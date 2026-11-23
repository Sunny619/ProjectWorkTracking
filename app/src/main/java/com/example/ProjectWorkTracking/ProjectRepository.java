package com.example.ProjectWorkTracking;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Thread-safe singleton repository managing project data persistence via SharedPreferences and Gson.
 */
public class ProjectRepository {

    public static final String PREF_NAME = "shared preferences";
    public static final String KEY_PROJECTS = "ProjectsList";

    private static volatile ProjectRepository sInstance;
    private final SharedPreferences mPrefs;
    private final Gson mGson;
    private final List<Project> mProjects;

    private ProjectRepository(Context context) {
        mPrefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mGson = new Gson();
        mProjects = new ArrayList<>();
        loadFromPreferences();
    }

    public static ProjectRepository getInstance(Context context) {
        if (sInstance == null) {
            synchronized (ProjectRepository.class) {
                if (sInstance == null) {
                    sInstance = new ProjectRepository(context);
                }
            }
        }
        return sInstance;
    }

    /**
     * Resets the singleton instance (primarily for testing purposes).
     */
    public static synchronized void resetInstance() {
        sInstance = null;
    }

    private synchronized void loadFromPreferences() {
        mProjects.clear();
        String json = mPrefs.getString(KEY_PROJECTS, null);
        if (json != null && !json.trim().isEmpty()) {
            try {
                Type type = new TypeToken<ArrayList<Project>>() {}.getType();
                List<Project> loaded = mGson.fromJson(json, type);
                if (loaded != null) {
                    mProjects.addAll(loaded);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Persists the current in-memory project list to SharedPreferences.
     */
    public synchronized void save() {
        SharedPreferences.Editor editor = mPrefs.edit();
        String json = mGson.toJson(mProjects);
        editor.putString(KEY_PROJECTS, json);
        editor.apply();
    }

    /**
     * Returns an unmodifiable view of all projects.
     */
    public synchronized List<Project> getAllProjects() {
        return Collections.unmodifiableList(new ArrayList<>(mProjects));
    }

    /**
     * Returns the underlying mutable list (for adapter direct binding or synchronized access).
     */
    public synchronized ArrayList<Project> getProjectList() {
        return (ArrayList<Project>) mProjects;
    }

    public synchronized int getProjectCount() {
        return mProjects.size();
    }

    public synchronized Project getProjectAt(int index) {
        if (index >= 0 && index < mProjects.size()) {
            return mProjects.get(index);
        }
        return null;
    }

    public synchronized boolean addProject(Project project) {
        if (project == null) return false;
        mProjects.add(project);
        save();
        return true;
    }

    public synchronized boolean updateProject(int index, Project project) {
        if (project == null || index < 0 || index >= mProjects.size()) {
            return false;
        }
        mProjects.set(index, project);
        save();
        return true;
    }

    public synchronized boolean deleteProject(int index) {
        if (index >= 0 && index < mProjects.size()) {
            mProjects.remove(index);
            save();
            return true;
        }
        return false;
    }

    public synchronized boolean isProjectNameTaken(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        String trimmed = name.trim();
        for (Project p : mProjects) {
            if (p != null && p.getName() != null && p.getName().trim().equalsIgnoreCase(trimmed)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void reload() {
        loadFromPreferences();
    }

    public synchronized void clear() {
        mProjects.clear();
        save();
    }
}
