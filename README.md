# WORLD FORGE AI — Open World Game Development Engine (Android APK)

**WORLD FORGE AI** is a professional AI-Powered Open World Game Development Platform built for Android and Desktop workflows.

---

## Direct Android `.APK` Download & Install

1. **Inside the App (1-Tap APK Download & Install Page):**
   - Open the **`APK Download & Install`** tab from the Main Navigation or Dashboard.
   - Tap **`1-Tap Download APK to Phone`** to immediately save `WorldForgeAI-Engine-v0.1.0.apk` to your phone's `Downloads` folder, then tap **`Install / Save APK`**.

2. **From GitHub Releases:**
   - Go to the **[Releases](../../releases)** section of this repository.
   - Download **`WorldForgeAI-Engine-v0.1.0.apk`** under **Assets** and tap it on your Android phone to install.

3. **From GitHub Actions:**
   - Open the **[Actions](../../actions)** tab -> click the latest **`Build & Release WORLD FORGE AI Android APK`** workflow run -> download the **`WorldForgeAI-Engine-APK`** artifact.

---

## Engine Architecture (Step 01 Foundation)
- **`components/` & `pages/`**: Dark professional engine studio UI, Project Inspector, and dedicated **`APK Download & Install`** page.
- **`projectmanager/` & `storage/`**: Real-time persistent project CRUD (`Create`, `Open`, `Rename`, `Delete`) backed by Cloud Firestore Enterprise.
- **`services/GitHubExportService.kt`**: Direct 1-click `.APK` extractor (`MediaStore.Downloads` + SAF), `.ZIP` repository bundler, and GitHub REST API Release `.APK` uploader.
- **`engine/`**: Modular subsystem registry for Three.js/WebGL2 viewport, AI Open World Generator, 3D World Scanner, Physics, Visual Scripting, and Android APK/AAB Exporter.
