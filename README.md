# Project Work Tracking

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)
[![Target SDK](https://img.shields.io/badge/Target%20SDK-30-blue.svg)](https://developer.android.com/about/versions/11)
[![Java](https://img.shields.io/badge/Language-Java%208-orange.svg)](https://www.oracle.com/java/)
[![Gradle](https://img.shields.io/badge/Gradle-7.0.2-blueviolet.svg)](https://gradle.org)

**Project Work Tracking** is a native Android application designed to help managers and teams track project execution, multi-team progress milestones, budget utilization, and critical project deadlines.

---

## Table of Contents

- [Overview](#overview)
- [Key Features](#key-features)
- [Application Flow & Architecture](#application-flow--architecture)
- [Tech Stack & Dependencies](#tech-stack--dependencies)
- [Directory Structure](#directory-structure)
- [Data Storage & Persistence](#data-storage--persistence)
- [Quick Start](#quick-start)
- [Testing](#testing)
- [Roadmap & Enhancements](#roadmap--enhancements)
- [License](#license)

---

## Overview

Managing concurrent development projects requires real-time visibility into multiple variables: cross-team completion percentages, financial burn rate against allotted budget, and countdown to delivery deadlines. 

**Project Work Tracking** solves this with an intuitive Material Design interface that provides:
1. High-level dashboard views of all active and completed projects.
2. Comprehensive project creation forms with built-in date validation and team assignments.
3. In-depth tracking views featuring animated, multi-ring concentric circular progress indicators for Design, Development, and Testing phases.
4. Dynamic budget tracking via interactive SeekBars and real-time deadline calculation.

---

## Key Features

### 1. Dashboard & Project Management (`MainActivity`)
- **RecyclerView Cards**: Displays all saved projects in a clean card-based layout featuring project name, description, assigned manager, budget, active status, and deadline.
- **Quick Actions**:
  - **Details**: Opens the comprehensive milestone and budget tracking dashboard for that project.
  - **Delete**: Prompts a Material Confirmation Dialog (`MaterialAlertDialogBuilder`) to prevent accidental deletion before updating persistent storage.
- **Floating Action Button (FAB)**: Quick access to create a new project.

### 2. Comprehensive Project Creation (`AddNewProjectActivity`)
- **Project Metadata**: Fields for Project Name, Multi-line Description, Project Manager, and Allocated Budget.
- **Interactive Date Pickers**: Custom `DatePickerDialog` modals for Start Date and Due Date ensuring standard `dd/MM/yyyy` format.
- **Cross-Functional Team Assignment**: Multi-selection checkboxes to associate departments:
  - Tech Team
  - Finance Team
  - Marketing Team
- **Priority Selector**: Dropdown spinner to assign priority (`High`, `Medium`, `Low`).
- **Initial Status**: Radio group to specify initial project state (Active / Inactive).
- **Validation**: Enforces duplicate name prevention across existing projects with user-friendly Toast notifications.

### 3. Detailed Milestone & Budget Tracking (`ProjectDetailsActivity`)
- **Multi-Ring Circular Progress Indicators**:
  - **Design Progress**: Yellow indicator (`#E2D810`)
  - **Development Progress**: Magenta indicator (`#D9138A`)
  - **Testing Progress**: Blue indicator (`#12A4D9`)
  - Incremented and decremented in 10% steps using dedicated controls with smooth `ObjectAnimator` transitions.
- **Interactive Budget Tracker**:
  - Dynamic `SeekBar` capped at the project's allocated maximum budget.
  - Tracks and saves budget consumption in real-time as the slider moves.
- **Automated Deadline Countdown**:
  - Parses project end date using `SimpleDateFormat`.
  - Calculates remaining days in real-time (`diff = endDate - curDate`).
  - Displays explicit **"Overdue"** alert if the deadline has passed.
- **One-Tap Priority Toggle**:
  - Cycles dynamically through **LOW**, **MEDIUM**, and **HIGH**.
  - Color-coded button state (`#2CC990` for Low, `#FCB942` for Medium, `#FC6042` for High).
- **Active / Inactive Status Switch**:
  - Switch component to toggle active status with immediate color-coded text updates.

---

## Application Flow & Architecture

```
                      +-----------------------------+
                      |         MainActivity        |
                      |  - Loads projects from SP   |
                      |  - RecyclerView Card list   |
                      +--------------+--------------+
                                     |
               +---------------------+---------------------+
               |                                           |
               v                                           v
+-------------------------------+           +-------------------------------+
|     AddNewProjectActivity     |           |     ProjectDetailsActivity    |
| - Project details form        |           | - Concentric progress rings   |
| - DatePickerDialog calendar   |           |   (Design, Dev, Testing)      |
| - Teams & priority assignment |           | - Interactive budget SeekBar  |
| - Duplicate validation        |           | - Due date countdown / Overdue|
| - Saves to SharedPreferences  |           | - Priority toggle button      |
+-------------------------------+           | - Auto-syncs to SharedPrefs   |
                                            +-------------------------------+
```

---

## Tech Stack & Dependencies

- **Language**: Java 8 (`JavaVersion.VERSION_1_8`)
- **Min SDK**: API Level 16 (Android 4.1 Jelly Bean)
- **Compile & Target SDK**: API Level 30 (Android 11)
- **Build System**: Gradle 7.0.2 with Android Gradle Plugin (AGP) 7.0.1
- **Key Libraries**:
  - **Material Components** (`com.google.android.material:material:1.4.0`): CircularProgressIndicator, MaterialCardView, MaterialButton, MaterialAlertDialog.
  - **AndroidX AppCompat** (`androidx.appcompat:appcompat:1.2.0`): Backwards-compatible UI framework.
  - **ConstraintLayout** (`androidx.constraintlayout:constraintlayout:2.0.4`): Flexible, flat view hierarchy layouts.
  - **Gson** (`com.google.code.gson:gson:2.8.8`): High-performance JSON serialization and deserialization for local persistence.
- **Testing**:
  - JUnit 4 (`junit:junit:4.+`)
  - AndroidX Test Ext (`androidx.test.ext:junit:1.1.2`)
  - Espresso Core (`androidx.test.espresso:espresso-core:3.3.0`)

---

## Directory Structure

```
ProjectWorkTracking/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/ProjectWorkTracking/
│   │   │   │   ├── AddNewProjectActivity.java    # Project creation and validation
│   │   │   │   ├── MainActivity.java             # Entry point & project card listing
│   │   │   │   ├── Project.java                  # Data model
│   │   │   │   ├── ProjectDetailsActivity.java   # Milestone, budget, & status tracking
│   │   │   │   └── ProjectsRecViewAdapter.java   # RecyclerView adapter & delete handling
│   │   │   ├── res/
│   │   │   │   ├── drawable/                     # Assets & project card imagery
│   │   │   │   ├── layout/                       # UI XML layout definitions
│   │   │   │   │   ├── activity_add_new_project.xml
│   │   │   │   │   ├── activity_main.xml
│   │   │   │   │   ├── activity_project_details.xml
│   │   │   │   │   └── cards_template.xml
│   │   │   │   ├── values/                       # Colors, strings, themes
│   │   │   │   │   ├── colors.xml
│   │   │   │   │   ├── strings.xml
│   │   │   │   │   └── themes.xml
│   │   │   │   └── AndroidManifest.xml           # Application manifest
│   │   ├── test/                                 # Local unit tests
│   │   └── androidTest/                          # Instrumented UI tests
│   ├── build.gradle                              # App-level build config & dependencies
│   └── proguard-rules.pro                        # ProGuard obfuscation rules
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties             # Gradle 7.0.2 distribution
├── build.gradle                                  # Top-level project build script
├── gradle.properties                             # JVM args & AndroidX enablement
├── gradlew                                       # Linux/macOS Gradle wrapper script
├── gradlew.bat                                   # Windows Gradle wrapper script
├── README.md                                     # Project overview and documentation
└── run_instructions.md                           # Build, run, and troubleshooting guide
```

---

## Data Storage & Persistence

The application uses Android's private `SharedPreferences` storage with key `"shared preferences"` to persist application state without requiring an external database:
- The project collection (`ArrayList<Project>`) is serialized into JSON using Google **Gson** and stored under key `"ProjectsList"`.
- Data is saved on every state change:
  - Adding a new project
  - Deleting an existing project
  - Updating budget progress via SeekBar
  - Adjusting design/development/testing progress bars
  - Cycling priority levels
  - Toggling active/inactive state

---

## Quick Start

For detailed step-by-step setup, IDE configuration, CLI commands, and troubleshooting, please read [run_instructions.md](run_instructions.md).

### Quick Build (Command Line)

```bash
# Clone the repository
git clone https://github.com/Sunny619/ProjectWorkTracking.git
cd ProjectWorkTracking

# Build Debug APK (Windows)
gradlew.bat assembleDebug

# Build Debug APK (macOS / Linux)
./gradlew assembleDebug
```

---

## Testing

Run unit tests:
```bash
./gradlew testDebugUnitTest
```

Run connected instrumented tests on an active device/emulator:
```bash
./gradlew connectedAndroidTest
```

---

## Roadmap & Enhancements

- [ ] **Room Persistence**: Migrate `SharedPreferences` + Gson to Android Jetpack Room DB with SQLite for scalable relational queries.
- [ ] **Data Validation**: Enhance input validation for budget bounds and manager names.
- [ ] **Search & Filtering**: Search projects by name and filter by status, priority, or department team.
- [ ] **Material 3 / Dynamic Colors**: Update themes to latest Material You design specs.
- [ ] **Export & Share**: Generate PDF/CSV progress summary reports to share with stakeholders.

---

## License

This project is licensed under the [MIT License](LICENSE) (or default repository license). Feel free to use and adapt it for your own project tracking needs.
