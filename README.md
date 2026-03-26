#  Universal Android APK Builder

Build any Android project directly on GitHub — no Android Studio needed.

---

##  How to use

1. Upload your Android project
2. Click **Commit changes**
3. Go to **Actions**
4. Open latest run
5. Download **android-build**

---

##  Output

* built-app-debug.apk
* built-app-release.apk
* built-app-release.aab

---

##  Features

* Auto-detects project
* Auto-detects SDK version
* Uses project Gradle if available
* Builds debug + signed release
* Generates signing key automatically

---

##  Notes

* Works for most Android projects
* Release key is temporary (new every build)
* Not ideal for Play Store updates

---

##  Tip

If build fails → check logs in Actions
