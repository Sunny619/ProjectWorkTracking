# Project Work Tracking - Run & Setup Instructions

This document provides step-by-step instructions for configuring the development environment, building the project, and running the application on an Android Virtual Device (Emulator) or a physical hardware device.

---

## Table of Contents

- [System Prerequisites](#system-prerequisites)
- [Environment Configuration](#environment-configuration)
- [Running via Android Studio (Recommended)](#running-via-android-studio-recommended)
- [Running via Command Line Interface (CLI)](#running-via-command-line-interface-cli)
- [Running Tests](#running-tests)
- [Troubleshooting & Common Issues](#troubleshooting--common-issues)

---

## System Prerequisites

Before building or running the project, verify that your machine has the following installed:

| Requirement | Recommended Version | Notes |
|---|---|---|
| **Operating System** | Windows 10/11, macOS (10.14+), or Linux | Supported by Android SDK & Gradle |
| **Java Development Kit (JDK)** | JDK 8 or JDK 11 | Gradle 7.0.2 / AGP 7.0.1 requires Java 8 or 11 |
| **Android Studio** | Arctic Fox (2020.3.1) or newer | Recommended IDE for Android development |
| **Android SDK Platform** | API Level 30 (Android 11.0 R) | Required compile and target SDK |
| **Android SDK Build-Tools** | `30.0.3` | Matches project build configuration |
| **Target Device / Emulator** | API 16 or newer (API 28–33 recommended) | Android Emulator or physical phone with USB debugging |

---

## Environment Configuration

### 1. Configure `JAVA_HOME`

Verify that your `JAVA_HOME` points to a compatible JDK 8 or JDK 11:

#### Windows (PowerShell):
```powershell
# Check current Java version
java -version

# Set JAVA_HOME temporarily if needed:
$env:JAVA_HOME = "C:\Program Files\Java\jdk-11"
$env:Path = "$env:JAVA_HOME\bin;" + $env:Path
```

#### macOS / Linux (Bash/Zsh):
```bash
# Check current Java version
java -version

# Set JAVA_HOME if using OpenJDK 11:
export JAVA_HOME=$(/usr/libexec/java_home -v 11)
export PATH=$JAVA_HOME/bin:$PATH
```

### 2. Configure Android SDK Environment Variables

Set `ANDROID_HOME` (or `ANDROID_SDK_ROOT`) and add platform-tools to your `PATH`:

#### Windows:
```powershell
$env:ANDROID_HOME = "$env:LOCALAPPDATA\Android\Sdk"
$env:Path = "$env:ANDROID_HOME\platform-tools;$env:ANDROID_HOME\tools;$env:Path"
```

#### macOS / Linux:
```bash
export ANDROID_HOME=$HOME/Library/Android/sdk # (macOS) or $HOME/Android/Sdk (Linux)
export PATH=$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/cmdline-tools/latest/bin
```

---

## Running via Android Studio (Recommended)

### Step 1: Clone the Repository
```bash
git clone https://github.com/Sunny619/ProjectWorkTracking.git
cd ProjectWorkTracking
```

### Step 2: Open in Android Studio
1. Launch **Android Studio**.
2. Select **Open** (or `File` > `Open...`).
3. Navigate to the `ProjectWorkTracking` folder and click **OK**.
4. Allow Android Studio to complete project indexing and Gradle initialization.

### Step 3: Verify Gradle JDK
1. Go to **File** > **Settings** (or **Android Studio** > **Preferences** on macOS).
2. Navigate to **Build, Execution, Deployment** > **Build Tools** > **Gradle**.
3. Under **Gradle JDK**, ensure either **Embedded JDK** or a configured **JDK 11 / JDK 8** is selected.
4. Click **Apply** and **OK**.

### Step 4: Sync Project with Gradle
- Click the **Sync Project with Gradle Files** icon in the toolbar (elephant with blue arrow), or choose **File** > **Sync Project with Gradle Files**.
- Wait until the build sync completes successfully in the **Build** window.

### Step 5: Set Up a Target Device

#### Option A: Android Virtual Device (AVD / Emulator)
1. In Android Studio, open **Device Manager** (`Tools` > `Device Manager`).
2. Click **Create Device**.
3. Select a phone model (e.g., Pixel 4 or Pixel 5) and click **Next**.
4. Select a system image (e.g., **API 30** "R" or **API 31**).
5. Click **Next**, then **Finish**.
6. Launch the emulator by clicking the green **Play** button next to it.

#### Option B: Physical Android Device
1. On your Android phone, enable **Developer Options**:
   - Go to **Settings** > **About Phone**.
   - Tap **Build Number** 7 times until you see "You are now a developer!".
2. Enable **USB Debugging**:
   - Go to **Settings** > **System** > **Developer Options**.
   - Enable **USB Debugging**.
3. Connect your phone via USB cable.
4. When prompted on your phone, select **Always allow from this computer** and tap **Allow**.

### Step 6: Run the App
1. Select `app` in the run configuration dropdown on the top toolbar.
2. Select your connected device or running emulator from the device dropdown.
3. Click the green **Run** button (or press `Shift + F10`).
4. The APK will be built, installed, and launched automatically.

---

## Running via Command Line Interface (CLI)

You can build, install, and execute the app without Android Studio using the Gradle wrapper (`gradlew`).

### 1. Verify Connected Device

Ensure an emulator is running or a device is plugged in:
```bash
adb devices
```
*Expected output shows your device ID with the state `device`:*
```
List of devices attached
emulator-5554    device
```

### 2. Clean Build Cache
```bash
# Windows
.\gradlew.bat clean

# macOS / Linux
./gradlew clean
```

### 3. Build the Debug APK
```bash
# Windows
.\gradlew.bat assembleDebug

# macOS / Linux
./gradlew assembleDebug
```
The compiled APK will be generated at:
`app/build/outputs/apk/debug/app-debug.apk`

### 4. Install the Debug APK
```bash
# Direct install via Gradle:
# Windows:
.\gradlew.bat installDebug

# macOS / Linux:
./gradlew installDebug

# Or install manually via ADB:
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### 5. Launch the Application via ADB
```bash
adb shell am start -n com.example.ProjectWorkTracking/.MainActivity
```

### 6. Monitor Logs (Logcat)
To view runtime logs filtered to application activities:
```bash
adb logcat -s MainActivity AddNewProjectActivity ProjectDetailsActivity
```

---

## Running Tests

### Unit Tests
Local JVM unit tests can be executed without an emulator:
```bash
# Windows
.\gradlew.bat testDebugUnitTest

# macOS / Linux
./gradlew testDebugUnitTest
```
Test results will be available at:
`app/build/reports/tests/testDebugUnitTest/index.html`

### Instrumented Tests
To run UI and integration tests on an active device/emulator:
```bash
# Windows
.\gradlew.bat connectedAndroidTest

# macOS / Linux
./gradlew connectedAndroidTest
```
Test reports will be available at:
`app/build/reports/androidTests/connected/index.html`

---

## Troubleshooting & Common Issues

### 1. `Unsupported class file major version` or Java Version Incompatibility
- **Cause**: Gradle 7.0.2 does not support JDK 17 or higher. Running with JDK 17+ produces:
  `Unsupported class file major version 61`
- **Solution**:
  - In Android Studio: Go to `Settings` > `Build, Execution, Deployment` > `Build Tools` > `Gradle` > change `Gradle JDK` to JDK 11.
  - In CLI: Ensure your `JAVA_HOME` environment variable points to a JDK 11 or JDK 8 installation.

### 2. `SDK location not found`
- **Cause**: Missing `local.properties` file indicating where Android SDK is installed.
- **Solution**:
  Create a file named `local.properties` in the project root directory with the path to your Android SDK:
  - Windows:
    ```properties
    sdk.dir=C\:\\Users\\<YourUsername>\\AppData\\Local\\Android\\Sdk
    ```
  - macOS:
    ```properties
    sdk.dir=/Users/<YourUsername>/Library/Android/sdk
    ```
  - Linux:
    ```properties
    sdk.dir=/home/<YourUsername>/Android/Sdk
    ```

### 3. Missing Build Tools `30.0.3` or Platform `android-30`
- **Cause**: The specified SDK platform or build-tools package is not installed.
- **Solution**:
  - In Android Studio: Open **Tools** > **SDK Manager** > **SDK Platforms**, check **Android 11.0 (R)** and click **Apply**. Under **SDK Tools**, check **Show Package Details**, select **30.0.3** under **Android SDK Build-Tools**, and click **Apply**.
  - In CLI:
    ```bash
    sdkmanager "platforms;android-30" "build-tools;30.0.3"
    ```

### 4. License Acceptance Error (`Failed to install the following Android SDK packages as some licences have not been accepted`)
- **Solution**: Run the license acceptance tool:
  ```bash
  # Windows
  %ANDROID_HOME%\cmdline-tools\latest\bin\sdkmanager.bat --licenses

  # macOS / Linux
  $ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager --licenses
  ```
  Press `y` to accept all licenses.

### 5. `adb: device unauthorized` or Device Not Found
- **Cause**: Device has not authorized the computer's RSA key or USB debugging is disabled.
- **Solution**:
  1. Disconnect and reconnect the USB cable.
  2. Unlock your phone screen and accept the "Allow USB debugging?" dialog.
  3. Restart ADB server if needed:
     ```bash
     adb kill-server
     adb start-server
     adb devices
     ```

### 6. Gradle Daemon Locked or Memory Pressure
- **Solution**: Stop hanging daemon instances and clean:
  ```bash
  ./gradlew --stop
  ./gradlew clean
  ```
