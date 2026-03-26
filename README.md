#  Universal Android Builder

Build any Android project directly in GitHub no Android Studio.

---

##  How to use

1. Upload your Android project
2. Click **Commit**
3. Go to **Actions**
4. Open latest run
5. Download **android-build**

---

## Features

* Auto-detect project location
* Auto-detect SDK version
* Uses project Gradle if available
* Fallback Gradle support
* Builds:

  * Debug APK
  * Release APK
  * AAB (Play Store bundle)

---

## Output files

* built-app-debug.apk
* built-app-release.apk
* built-app-release.aab

---

## Notes

* Works for most Android projects
* Some projects may require fixes
* Release builds may need signing

---

##  Tip

If build fails → check logs in Actions
