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
- [Architecture & Design Patterns](#architecture--design-patterns)
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

**Project Work Tracking** provides an intuitive, robust interface with:
1. High-level dashboard views of all active and completed projects with optimized `DiffUtil` card rendering.
2. Comprehensive project creation forms with strict validation, zero-padded `DatePickerDialog` modals, and team assignments.
3. In-depth tracking views featuring animated, multi-ring concentric circular progress indicators for Design, Development, and Testing phases with safe boundary clamping [0–100%].
4. Dynamic budget tracking via interactive SeekBars and real-time deadline countdowns with automated overdue detection.
5. Centralized `ProjectRepository` persistence architecture isolating storage and serialization from UI activities.

---

## Key Features

### 1. Dashboard & Project Management (`MainActivity`)
- **RecyclerView Cards with DiffUtil**: Displays all saved projects in a clean card-based layout featuring project name, description, assigned manager, budget, active status, and deadline. `DiffUtil` calculations avoid unnecessary re-binds.
- **Lifecycle-Aware Auto-Refresh**: Hooks into `onResume` to instantly reflect newly added, edited, or deleted projects when navigating back.
- **Quick Actions**:
  - **Details**: Opens the comprehensive milestone and budget tracking dashboard for that project.
  - **Delete**: Prompts a Material Confirmation Dialog (`MaterialAlertDialogBuilder`) before delegating removal to `ProjectRepository`.
- **Floating Action Button (FAB)**: Quick access to create a new project.

### 2. Comprehensive Project Creation (`AddNewProjectActivity`)
- **Input Validation**: Strict validation for non-empty project names, valid numeric positive budgets, and non-empty dates.
- **Duplicate Prevention**: Case-insensitive check via `ProjectRepository` prevents duplicate project names.
- **Standardized Date Pickers**: Formats and zero-pads dates (`dd/MM/yyyy`) using `DateUtils` to guarantee format consistency across the app.
- **Cross-Functional Team Assignment**: Multi-selection checkboxes to associate departments (Tech, Finance, Marketing).
- **Priority Selector**: Dropdown spinner to assign priority (`High`, `Medium`, `Low`).
- **Initial Status**: Radio group to specify initial project state (Active / Inactive).
- **Seamless UX**: Automatically calls `finish()` upon successful addition to return the user directly to the refreshed dashboard.

### 3. Detailed Milestone & Budget Tracking (`ProjectDetailsActivity`)
- **Multi-Ring Circular Progress Indicators**:
  - **Design Progress**: Yellow indicator (`#E2D810`)
  - **Development Progress**: Magenta indicator (`#D9138A`)
  - **Testing Progress**: Blue indicator (`#12A4D9`)
  - Stepped in 10% increments with strictly clamped bounds [0%–100%] and smooth `ObjectAnimator` transitions.
- **Interactive Budget Tracker**:
  - Dynamic `SeekBar` capped at the project's allocated maximum budget.
  - Tracks and saves budget consumption in real-time as the slider moves.
- **Automated Deadline Countdown (`DateUtils`)**:
  - Calculates normalized days remaining until deadline (`diff = targetDate - today`).
  - Displays explicit **"Overdue"** alert if the deadline has passed.
- **One-Tap Priority Toggle**:
  - Cycles dynamically through **LOW**, **MEDIUM**, and **HIGH**.
  - Color-coded button state (`#2CC990` for Low, `#FCB942` for Medium, `#FC6042` for High).
- **Active / Inactive Status Switch**:
  - Switch component to toggle active status with immediate color-coded visual indicator.
- **Safe Index Handling**: Validates project existence before rendering to prevent out-of-bounds crashes.

---

## Architecture & Design Patterns

```
                                  +-----------------------+
                                  |     MainActivity      |
                                  |  - RecyclerView List  |
                                  |  - DiffUtil Adapter   |
                                  +-----------+-----------+
                                              |
                        +---------------------+---------------------+
                        |                                           |
                        v                                           v
         +-----------------------------+             +-----------------------------+
         |    AddNewProjectActivity    |             |    ProjectDetailsActivity   |
         |  - Form & validation        |             |  - Multi-ring progress rings|
         |  - DateUtils pickers        |             |  - Budget SeekBar & deadline|
         +--------------+--------------+             +--------------+--------------+
                        |                                           |
                        +---------------------+---------------------+
                                              |
                                              v
                               +-----------------------------+
                               |      ProjectRepository      |
                               |  - Singleton persistence    |
                               |  - In-memory cache & CRUD   |
                               |  - SharedPreferences + Gson |
                               +-----------------------------+
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
  - **Gson** (`com.google.code.gson:gson:2.8.8`): JSON serialization and deserialization for local persistence.
- **Testing**:
  - JUnit 4 (`junit:junit:4.13.2`)
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
│   │   │   │   ├── DateUtils.java                # Date formatting, parsing & countdown helpers
│   │   │   │   ├── MainActivity.java             # Entry point & project card listing
│   │   │   │   ├── Project.java                  # Encapsulated data model
│   │   │   │   ├── ProjectDetailsActivity.java   # Milestone, budget, & status tracking
│   │   │   │   ├── ProjectRepository.java        # Centralized persistence repository
│   │   │   │   └── ProjectsRecViewAdapter.java   # DiffUtil RecyclerView adapter
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
│   │   │   └── java/com/example/ProjectWorkTracking/
│   │   │       ├── DateUtilsTest.java            # Unit tests for DateUtils
│   │   │       ├── ExampleUnitTest.java          # Sample unit test
│   │   │       └── ProjectTest.java              # Unit tests for Project model
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

The application uses `ProjectRepository`, a singleton storage layer encapsulating Android's private `SharedPreferences` with Google **Gson**:
- Encapsulates key `"ProjectsList"` and file `"shared preferences"`.
- Features in-memory caching for sub-millisecond retrieval.
- Auto-syncs across state mutations:
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

- [x] **Repository Layer**: Centralized data access and persistence via `ProjectRepository`.
- [x] **Safe Date Utils**: Robust parsing and deadline countdowns via `DateUtils`.
- [x] **DiffUtil Rendering**: Seamless RecyclerView updates without UI flicker.
- [x] **Comprehensive Unit Tests**: Full coverage for `Project` model and `DateUtils`.
- [ ] **Room Persistence**: Migrate `SharedPreferences` + Gson to Android Jetpack Room DB with SQLite for scalable relational queries.
- [ ] **Search & Filtering**: Search projects by name and filter by status, priority, or department team.
- [ ] **Material 3 / Dynamic Colors**: Update themes to latest Material You design specs.
- [ ] **Export & Share**: Generate PDF/CSV progress summary reports to share with stakeholders.

---

## License

This project is licensed under the [MIT License](LICENSE) (or default repository license). Feel free to use and adapt it for your own project tracking needs.
