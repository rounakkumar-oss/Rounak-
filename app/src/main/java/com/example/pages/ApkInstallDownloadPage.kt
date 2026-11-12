package com.example.pages

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.InstallMobile
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.assets.ForgeAssetCatalog
import com.example.services.GitHubExportService
import com.example.services.GitHubPushResult
import com.example.types.ForgeProject
import com.example.ui.theme.ForgeBlueLight
import com.example.ui.theme.ForgeCardBorder
import com.example.ui.theme.ForgeDangerRed
import com.example.ui.theme.ForgeElectricBlue
import com.example.ui.theme.ForgeSuccessGreen
import com.example.ui.theme.ForgeSurface
import com.example.ui.theme.ForgeSurfaceElevated
import com.example.ui.theme.ForgeTextPrimary
import com.example.ui.theme.ForgeTextSecondary
import com.example.ui.theme.ForgeVioletAccent
import com.example.ui.theme.ForgeVioletLight
import java.util.Locale
import kotlinx.coroutines.launch

/**
 * Dedicated Full-Screen APK Download & Install Page for WORLD FORGE AI.
 *
 * Gives the user a clean, store-style APK Downloader & Installer page where they can:
 * 1. Tap "1. DOWNLOAD APK TO PHONE" to immediately save `WorldForgeAI-Engine-v0.1.0.apk` into `Downloads/`.
 * 2. Tap "2. INSTALL APK NOW" to open the interactive installer panel, launch the system Downloads
 *    folder, or save a copy of the `.APK` anywhere on the device.
 * 3. Push the `.APK` + Engine files + GitHub Actions workflow directly to their GitHub account.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ApkInstallDownloadPage(
    activeProject: ForgeProject?,
    allProjects: List<ForgeProject>,
    onPostStatusMessage: (String) -> Unit,
    onReturnToDashboard: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val apkSizeLabel = remember(context) { GitHubExportService.getInstalledApkSizeLabel(context) }
    val targetProject = activeProject ?: allProjects.firstOrNull() ?: ForgeProject(
        id = "world-forge-ai-engine",
        name = "World Forge AI Engine",
        description = "WORLD FORGE AI — Open World Game Development Platform"
    )
    val apkFileName = GitHubExportService.DEFAULT_APK_FILENAME
    val targetSlug = remember(targetProject) { GitHubExportService.sanitizeRepoSlug(targetProject.name) }

    var isDownloadingApk by remember { mutableStateOf(false) }
    var downloadProgress by remember { mutableFloatStateOf(0f) }
    var downloadedApkPath by rememberSaveable { mutableStateOf<String?>(null) }
    var showInstallerPanel by rememberSaveable { mutableStateOf(false) }

    // GitHub Direct Push State (Defaults to user's rounakkumar-oss/Rounak- repo)
    var githubOwnerInput by rememberSaveable {
        mutableStateOf(GitHubExportService.getSavedGitHubUsername(context))
    }
    var repoNameInput by rememberSaveable {
        mutableStateOf(GitHubExportService.DEFAULT_GITHUB_REPO)
    }
    var githubTokenInput by rememberSaveable { mutableStateOf("") }
    var isPushingToGitHub by remember { mutableStateOf(false) }
    var pushError by remember { mutableStateOf<String?>(null) }
    var pushSuccessResult by remember { mutableStateOf<GitHubPushResult?>(null) }

    // Custom Folder Save-As Launcher for .APK
    val saveApkCustomFolderLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/vnd.android.package-archive")
    ) { uri ->
        if (uri != null) {
            val res = GitHubExportService.writeInstalledApkToUri(context, uri)
            res.onSuccess { bytes ->
                val mb = String.format(Locale.US, "%.1f MB", bytes.toDouble() / (1024.0 * 1024.0))
                downloadedApkPath = "Custom Folder: $apkFileName ($mb)"
                showInstallerPanel = true
                onPostStatusMessage("Saved $apkFileName ($mb) to your selected folder!")
            }.onFailure { err ->
                onPostStatusMessage("Failed to save APK: ${err.localizedMessage}")
            }
        }
    }

    // Custom Folder Save-As Launcher for .ZIP
    val saveZipLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/zip")
    ) { uri ->
        if (uri != null) {
            val res = GitHubExportService.writeProjectZipToUri(context, uri, targetProject, allProjects)
            res.onSuccess {
                onPostStatusMessage("Saved ${targetSlug}.zip Engine Bundle to phone!")
            }.onFailure { err ->
                onPostStatusMessage("Failed to save ZIP: ${err.localizedMessage}")
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("apk_install_download_page"),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        if (onReturnToDashboard != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onReturnToDashboard,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, ForgeCardBorder),
                    modifier = Modifier.testTag("apk_page_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = ForgeBlueLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Back to Dashboard", color = ForgeTextPrimary)
                }
            }
        }

        // =========================================================================
        // CARD 1: MAIN APK DOWNLOAD & INSTALL STORE CARD (1-Click Download + Install)
        // =========================================================================
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("apk_store_installer_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = ForgeSurface),
            border = BorderStroke(2.dp, ForgeSuccessGreen.copy(alpha = 0.8f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // App Icon + Title + Verified Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = ForgeAssetCatalog.AppIconFgRes),
                        contentDescription = "WORLD FORGE AI Engine Icon",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.5.dp, ForgeElectricBlue, RoundedCornerShape(16.dp))
                    )

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Surface(
                                color = ForgeSuccessGreen.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp),
                                border = BorderStroke(1.dp, ForgeSuccessGreen.copy(alpha = 0.6f))
                            ) {
                                Text(
                                    text = "OFFICIAL ANDROID APK",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ForgeSuccessGreen,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Text(
                            text = "WORLD FORGE AI — Engine APK",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = ForgeTextPrimary
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = ForgeBlueLight,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "$apkFileName · $apkSizeLabel · Android 7.0+ (ARM64 / Universal)",
                                style = MaterialTheme.typography.bodySmall,
                                color = ForgeTextSecondary
                            )
                        }
                    }
                }

                // Specs Row
                Surface(
                    color = ForgeSurfaceElevated,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, ForgeCardBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ApkStatColumn(label = "PACKAGE FILE", value = "WorldForgeAI.apk")
                        ApkStatColumn(label = "APK SIZE", value = apkSizeLabel)
                        ApkStatColumn(label = "VERSION", value = "v0.1.0-step01")
                        ApkStatColumn(label = "STATUS", value = "Ready to Install")
                    }
                }

                // Live Download Progress Indicator
                AnimatedVisibility(visible = isDownloadingApk || downloadedApkPath != null) {
                    Surface(
                        color = ForgeSuccessGreen.copy(alpha = 0.14f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, ForgeSuccessGreen.copy(alpha = 0.6f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (isDownloadingApk) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(18.dp),
                                        color = ForgeSuccessGreen,
                                        strokeWidth = 2.dp
                                    )
                                    Text(
                                        text = "Downloading $apkFileName to Phone (${(downloadProgress * 100).toInt()}%)...",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = ForgeTextPrimary
                                    )
                                }
                                LinearProgressIndicator(
                                    progress = { downloadProgress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(CircleShape),
                                    color = ForgeSuccessGreen,
                                    trackColor = ForgeSurface
                                )
                            } else if (downloadedApkPath != null) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = ForgeSuccessGreen,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Column {
                                        Text(
                                            text = "APK Downloaded Successfully!",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = ForgeSuccessGreen
                                        )
                                        Text(
                                            text = "Saved to: ${downloadedApkPath!!} — Niche '2. INSTALL APK NOW' dabayein!",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = ForgeTextPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // =========================================================================
                // STEP 1 BUTTON: 1-TAP DIRECT APK DOWNLOAD TO PHONE DOWNLOADS FOLDER
                // =========================================================================
                Button(
                    onClick = {
                        isDownloadingApk = true
                        downloadProgress = 0.1f
                        scope.launch {
                            val res = GitHubExportService.saveInstalledApkDirectlyToDownloads(
                                context = context,
                                fileName = apkFileName,
                                onProgress = { progress ->
                                    downloadProgress = progress
                                }
                            )
                            isDownloadingApk = false
                            res.onSuccess { savedPath ->
                                downloadedApkPath = savedPath
                                showInstallerPanel = true
                                onPostStatusMessage("APK Downloaded to $savedPath! Tap 'INSTALL APK NOW' below.")
                            }.onFailure {
                                // Fallback to custom folder picker if MediaStore fails
                                saveApkCustomFolderLauncher.launch(apkFileName)
                            }
                        }
                    },
                    enabled = !isDownloadingApk,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ForgeSuccessGreen,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("apk_page_instant_download_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (downloadedApkPath == null) {
                            "1. DOWNLOAD APK TO PHONE ($apkSizeLabel)"
                        } else {
                            "1. DOWNLOAD APK AGAIN ($apkSizeLabel)"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                // =========================================================================
                // STEP 2 BUTTON: INSTALL APK NOW (DEDICATED INSTALL BUTTON)
                // =========================================================================
                Button(
                    onClick = {
                        if (downloadedApkPath == null) {
                            // Automatically download first, then open installer panel!
                            isDownloadingApk = true
                            downloadProgress = 0.1f
                            scope.launch {
                                val res = GitHubExportService.saveInstalledApkDirectlyToDownloads(
                                    context = context,
                                    fileName = apkFileName,
                                    onProgress = { progress -> downloadProgress = progress }
                                )
                                isDownloadingApk = false
                                res.onSuccess { savedPath ->
                                    downloadedApkPath = savedPath
                                    showInstallerPanel = true
                                    GitHubExportService.openSystemDownloadsFolder(context)
                                    onPostStatusMessage("APK Ready in $savedPath — Tap $apkFileName in Downloads to Install!")
                                }.onFailure {
                                    showInstallerPanel = true
                                    saveApkCustomFolderLauncher.launch(apkFileName)
                                }
                            }
                        } else {
                            showInstallerPanel = true
                            GitHubExportService.openSystemDownloadsFolder(context)
                            onPostStatusMessage("Opening Downloads folder — Tap $apkFileName to Install!")
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ForgeElectricBlue,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("apk_page_install_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.InstallMobile,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "2. INSTALL APK NOW (Direct Installer)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Secondary Actions: Choose Folder to Save APK / Download .ZIP Engine Bundle
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            saveApkCustomFolderLauncher.launch(apkFileName)
                        },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, ForgeBlueLight.copy(alpha = 0.6f)),
                        modifier = Modifier.testTag("apk_page_save_custom_folder_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FolderOpen,
                            contentDescription = null,
                            tint = ForgeBlueLight,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save .APK to Custom Folder", color = ForgeTextPrimary)
                    }

                    OutlinedButton(
                        onClick = {
                            saveZipLauncher.launch("${targetSlug}-engine.zip")
                        },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, ForgeCardBorder),
                        modifier = Modifier.testTag("apk_page_download_zip_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FolderZip,
                            contentDescription = null,
                            tint = ForgeVioletLight,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Download Engine .ZIP", color = ForgeTextPrimary)
                    }
                }

                // =========================================================================
                // INTERACTIVE APK INSTALLER & VERIFICATION PANEL (Appears on Download / Install)
                // =========================================================================
                AnimatedVisibility(visible = showInstallerPanel) {
                    Surface(
                        color = ForgeSurfaceElevated,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.5.dp, ForgeElectricBlue)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Android,
                                    contentDescription = null,
                                    tint = ForgeSuccessGreen,
                                    modifier = Modifier.size(22.dp)
                                )
                                Text(
                                    text = "WORLD FORGE AI — APK Installer Guide & Status",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ForgeTextPrimary
                                )
                            }

                            Text(
                                text = "• File Saved: ${downloadedApkPath ?: "Downloads/$apkFileName ($apkSizeLabel)"}\n" +
                                    "• Package ID: com.aistudio.worldforge.wfai01 (Signed Release/Debug Key)\n" +
                                    "• Kaise Install Karein: Apne phone ke 'Downloads' ya 'Files' app me jaakar '$apkFileName' par tap karein aur 'Install' / 'Update' dabayein.",
                                style = MaterialTheme.typography.bodySmall,
                                color = ForgeTextSecondary
                            )

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        val opened = GitHubExportService.openSystemDownloadsFolder(context)
                                        if (!opened) {
                                            saveApkCustomFolderLauncher.launch(apkFileName)
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = ForgeSuccessGreen),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FolderOpen,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Open Phone Downloads Folder", color = Color.White, fontWeight = FontWeight.Bold)
                                }

                                OutlinedButton(
                                    onClick = {
                                        saveApkCustomFolderLauncher.launch(apkFileName)
                                    },
                                    border = BorderStroke(1.dp, ForgeElectricBlue),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Download,
                                        contentDescription = null,
                                        tint = ForgeBlueLight,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Save APK Copy As...", color = ForgeTextPrimary)
                                }
                            }
                        }
                    }
                }
            }
        }

        // =========================================================================
        // CARD 2: UPLOAD ENGINE + APK + GITHUB ACTIONS TO YOUR GITHUB REPO
        // =========================================================================
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("apk_page_github_uploader_card"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = ForgeSurface),
            border = BorderStroke(1.dp, ForgeCardBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudUpload,
                        contentDescription = null,
                        tint = ForgeBlueLight,
                        modifier = Modifier.size(24.dp)
                    )
                    Column {
                        Text(
                            text = "GitHub pe Engine + APK + Actions Upload Karein",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = ForgeTextPrimary
                        )
                        Text(
                            text = "Yeh aapke GitHub Project me 'apk/' folder, 'install-apk.html', GitHub Actions workflow (.yml), aur GitHub Releases me '$apkFileName' ($apkSizeLabel) upload kar dega:",
                            style = MaterialTheme.typography.bodySmall,
                            color = ForgeTextSecondary
                        )
                    }
                }

                HorizontalDivider(color = ForgeCardBorder)

                // Quick 1-Tap Direct GitHub APK Links for rounakkumar-oss/Rounak-
                val activeOwner = githubOwnerInput.trim().ifBlank { GitHubExportService.DEFAULT_GITHUB_USERNAME }
                val activeRepo = repoNameInput.trim().ifBlank { GitHubExportService.DEFAULT_GITHUB_REPO }
                Surface(
                    color = ForgeSurfaceElevated,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, ForgeSuccessGreen.copy(alpha = 0.6f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Direct GitHub Links ($activeOwner/$activeRepo):",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = ForgeSuccessGreen
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    GitHubExportService.openBrowserUrl(
                                        context,
                                        "https://github.com/$activeOwner/$activeRepo/releases/latest/download/$apkFileName"
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ForgeSuccessGreen),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("apk_page_quick_github_apk_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Android,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Download APK from GitHub", color = Color.White, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = {
                                    GitHubExportService.openBrowserUrl(
                                        context,
                                        "https://github.com/$activeOwner/$activeRepo/releases"
                                    )
                                },
                                border = BorderStroke(1.dp, ForgeElectricBlue),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                    contentDescription = null,
                                    tint = ForgeBlueLight,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Open GitHub Releases", color = ForgeTextPrimary)
                            }

                            OutlinedButton(
                                onClick = {
                                    GitHubExportService.openBrowserUrl(
                                        context,
                                        "https://github.com/$activeOwner/$activeRepo/actions"
                                    )
                                },
                                border = BorderStroke(1.dp, ForgeVioletLight),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                    contentDescription = null,
                                    tint = ForgeVioletLight,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Open GitHub Actions", color = ForgeTextPrimary)
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = githubOwnerInput,
                        onValueChange = {
                            githubOwnerInput = it
                            GitHubExportService.saveGitHubUsername(context, it)
                        },
                        label = { Text("GitHub Username") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ForgeElectricBlue,
                            unfocusedBorderColor = ForgeCardBorder,
                            focusedTextColor = ForgeTextPrimary,
                            unfocusedTextColor = ForgeTextPrimary
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = repoNameInput,
                        onValueChange = { repoNameInput = it },
                        label = { Text("Repository Name") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ForgeElectricBlue,
                            unfocusedBorderColor = ForgeCardBorder,
                            focusedTextColor = ForgeTextPrimary,
                            unfocusedTextColor = ForgeTextPrimary
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = githubTokenInput,
                        onValueChange = {
                            githubTokenInput = it
                            if (pushError != null) pushError = null
                        },
                        label = { Text("GitHub Personal Access Token (ghp_...)") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ForgeElectricBlue,
                            unfocusedBorderColor = ForgeCardBorder,
                            focusedTextColor = ForgeTextPrimary,
                            unfocusedTextColor = ForgeTextPrimary
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedButton(
                        onClick = {
                            GitHubExportService.openGitHubGenerateTokenPage(context)
                        },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, ForgeCardBorder)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = null,
                            tint = ForgeVioletLight,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Get Token", color = ForgeTextPrimary)
                    }
                }

                if (pushError != null) {
                    Surface(
                        color = ForgeDangerRed.copy(alpha = 0.14f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, ForgeDangerRed.copy(alpha = 0.45f))
                    ) {
                        Text(
                            text = pushError!!,
                            style = MaterialTheme.typography.bodySmall,
                            color = ForgeDangerRed,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

                if (pushSuccessResult != null) {
                    val result = pushSuccessResult!!
                    Surface(
                        color = ForgeSuccessGreen.copy(alpha = 0.14f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.5.dp, ForgeSuccessGreen)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "✅ GitHub Project, Actions & APK Live: ${result.repoFullName}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = ForgeSuccessGreen
                            )

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (!result.apkDownloadUrl.isNullOrBlank()) {
                                    Button(
                                        onClick = {
                                            GitHubExportService.openBrowserUrl(context, result.apkDownloadUrl)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = ForgeSuccessGreen),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Android,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Download APK from GitHub", color = Color.White, fontWeight = FontWeight.Bold)
                                    }

                                    OutlinedButton(
                                        onClick = {
                                            GitHubExportService.copyToClipboard(
                                                context = context,
                                                label = "GitHub Direct APK Link",
                                                text = result.apkDownloadUrl
                                            )
                                            onPostStatusMessage("Copied Direct GitHub APK Link!")
                                        },
                                        border = BorderStroke(1.dp, ForgeSuccessGreen),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ContentCopy,
                                            contentDescription = null,
                                            tint = ForgeSuccessGreen,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Copy APK Link", color = ForgeTextPrimary)
                                    }
                                }

                                Button(
                                    onClick = {
                                        GitHubExportService.openBrowserUrl(context, result.releasesPageUrl)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = ForgeElectricBlue),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                        contentDescription = null,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Open GitHub Releases", color = Color.White)
                                }

                                OutlinedButton(
                                    onClick = {
                                        GitHubExportService.openBrowserUrl(context, result.actionsPageUrl)
                                    },
                                    border = BorderStroke(1.dp, ForgeVioletLight),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                        contentDescription = null,
                                        tint = ForgeVioletLight,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Open GitHub Actions", color = ForgeTextPrimary)
                                }
                            }
                        }
                    }
                }

                Button(
                    onClick = {
                        pushError = null
                        pushSuccessResult = null
                        isPushingToGitHub = true
                        scope.launch {
                            val res = GitHubExportService.pushProjectDirectlyToGitHubApi(
                                context = context,
                                personalAccessToken = githubTokenInput,
                                repoName = repoNameInput,
                                description = targetProject.description,
                                isPrivate = false,
                                uploadApkRelease = true,
                                project = targetProject,
                                allProjects = allProjects
                            )
                            isPushingToGitHub = false
                            res.onSuccess { pushed ->
                                pushSuccessResult = pushed
                                onPostStatusMessage("Engine + APK + Actions uploaded to GitHub (${pushed.repoFullName})!")
                            }.onFailure { err ->
                                pushError = err.localizedMessage ?: "Failed to push to GitHub."
                            }
                        }
                    },
                    enabled = !isPushingToGitHub,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ForgeVioletAccent,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("apk_page_push_github_button")
                ) {
                    if (isPushingToGitHub) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Uploading Engine + APK + Actions to GitHub...")
                    } else {
                        Icon(
                            imageVector = Icons.Default.CloudUpload,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Upload Engine + APK + Actions to GitHub",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ApkStatColumn(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = ForgeTextSecondary
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = ForgeTextPrimary
        )
    }
}
