package com.example.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
import com.example.ui.theme.ForgeTextMuted
import com.example.ui.theme.ForgeTextPrimary
import com.example.ui.theme.ForgeTextSecondary
import com.example.ui.theme.ForgeVioletAccent
import com.example.ui.theme.ForgeVioletLight
import com.example.ui.theme.ForgeWarningAmber
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GitHubExportDialog(
    project: ForgeProject?,
    allProjects: List<ForgeProject>,
    onDismiss: () -> Unit,
    onExportStatusMessage: (String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val targetSlug = remember(project) {
        if (project != null) GitHubExportService.sanitizeRepoSlug(project.name)
        else "world-forge-ai-workspace"
    }

    var repoNameInput by rememberSaveable(targetSlug) { mutableStateOf(targetSlug) }
    var githubTokenInput by rememberSaveable { mutableStateOf("") }
    var createAsPublicRepo by rememberSaveable { mutableStateOf(true) }
    var isPushingToGitHub by remember { mutableStateOf(false) }
    var pushError by remember { mutableStateOf<String?>(null) }
    var pushSuccessResult by remember { mutableStateOf<GitHubPushResult?>(null) }

    var existingGithubUsername by rememberSaveable {
        mutableStateOf(GitHubExportService.getSavedGitHubUsername(context))
    }
    var existingRepoName by rememberSaveable(targetSlug) { mutableStateOf(targetSlug) }
    var existingBranchName by rememberSaveable { mutableStateOf("main") }

    var showReadmePreview by remember { mutableStateOf(false) }

    val jsonPayload = remember(project, allProjects) {
        if (project != null) {
            GitHubExportService.buildProjectManifestJson(project)
        } else {
            GitHubExportService.buildWorkspaceBackupJson(allProjects)
        }
    }

    val readmePayload = remember(project) {
        if (project != null) {
            GitHubExportService.buildGitHubReadmeMarkdown(project)
        } else {
            "# WORLD FORGE AI Workspace\n\nContains ${allProjects.size} game development project(s)."
        }
    }

    // 1. Direct .ZIP Archive Download to Device Storage (No GitHub login needed, zero 404 risk)
    val downloadZipLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/zip")
    ) { uri ->
        if (uri != null) {
            val res = GitHubExportService.writeProjectZipToUri(context, uri, project, allProjects)
            res.onSuccess {
                onExportStatusMessage("Saved ${targetSlug}.zip starter repository bundle to device storage.")
            }.onFailure { err ->
                onExportStatusMessage("Failed to save ZIP archive: ${err.localizedMessage}")
            }
        }
    }

    // 2. Direct .JSON Manifest Download
    val downloadJsonLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        if (uri != null) {
            val res = GitHubExportService.writeContentToUri(context, uri, jsonPayload)
            res.onSuccess {
                onExportStatusMessage("Saved ${targetSlug}.project.json to device storage.")
            }.onFailure { err ->
                onExportStatusMessage("Failed to save JSON file: ${err.localizedMessage}")
            }
        }
    }

    // 3. Direct README.md Download
    val downloadReadmeLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/markdown")
    ) { uri ->
        if (uri != null) {
            val res = GitHubExportService.writeContentToUri(context, uri, readmePayload)
            res.onSuccess {
                onExportStatusMessage("Saved README.md for $targetSlug to device storage.")
            }.onFailure { err ->
                onExportStatusMessage("Failed to save README.md: ${err.localizedMessage}")
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .widthIn(max = 660.dp)
                .clip(RoundedCornerShape(18.dp))
                .border(1.dp, ForgeCardBorder, RoundedCornerShape(18.dp))
                .testTag("github_export_dialog"),
            color = ForgeSurface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(ForgeElectricBlue.copy(alpha = 0.18f))
                                .border(1.dp, ForgeElectricBlue.copy(alpha = 0.45f), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = null,
                                tint = ForgeBlueLight,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "GitHub Direct Push & ZIP Download",
                                style = MaterialTheme.typography.headlineMedium,
                                color = ForgeTextPrimary
                            )
                            Text(
                                text = if (project != null) {
                                    "Project: ${project.name} ($targetSlug)"
                                } else {
                                    "Full Workspace Bundle (${allProjects.size} project(s))"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = ForgeTextSecondary
                            )
                        }
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_github_dialog_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = ForgeTextSecondary
                        )
                    }
                }

                // Why GitHub 404 Happens & How This Solves It Banner
                Surface(
                    color = ForgeWarningAmber.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, ForgeWarningAmber.copy(alpha = 0.45f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = ForgeWarningAmber,
                            modifier = Modifier
                                .padding(top = 2.dp)
                                .size(18.dp)
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(
                                text = "Why did GitHub show '404 — Need to sign in?'",
                                style = MaterialTheme.typography.titleSmall,
                                color = ForgeTextPrimary
                            )
                            Text(
                                text = "GitHub shows a 404 page when a repository is Private (and your phone browser isn't signed into GitHub) or when the repository hasn't been created yet. Use Option 1 below to download the .ZIP directly to your phone without GitHub login, or Option 2 to auto-create a Public GitHub repo with 1 tap!",
                                style = MaterialTheme.typography.bodySmall,
                                color = ForgeTextSecondary
                            )
                        }
                    }
                }

                // OPTION 1: Instant Offline .ZIP & File Download (Works 100% without GitHub Login)
                Surface(
                    color = ForgeSurfaceElevated,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.5.dp, ForgeSuccessGreen.copy(alpha = 0.6f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FolderZip,
                                contentDescription = null,
                                tint = ForgeSuccessGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Option 1: Direct Phone .ZIP Download (No Login Needed)",
                                style = MaterialTheme.typography.titleMedium,
                                color = ForgeTextPrimary
                            )
                        }
                        Text(
                            text = "Save the complete GitHub repository structure (README.md, worldforge.project.json, .gitignore, and scenes/main_world.scene.json) directly into your phone's Downloads folder as a .ZIP file.",
                            style = MaterialTheme.typography.bodySmall,
                            color = ForgeTextSecondary
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    downloadZipLauncher.launch("${targetSlug}.zip")
                                },
                                shape = RoundedCornerShape(9.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ForgeSuccessGreen,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier.testTag("download_project_zip_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FolderZip,
                                    contentDescription = null,
                                    modifier = Modifier.size(17.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Download Project .ZIP",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            OutlinedButton(
                                onClick = {
                                    downloadJsonLauncher.launch("${targetSlug}.project.json")
                                },
                                shape = RoundedCornerShape(9.dp),
                                border = BorderStroke(1.dp, ForgeCardBorder),
                                modifier = Modifier.testTag("download_project_json_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = null,
                                    tint = ForgeBlueLight,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Save .JSON", color = ForgeTextPrimary)
                            }

                            OutlinedButton(
                                onClick = {
                                    downloadReadmeLauncher.launch("README.md")
                                },
                                shape = RoundedCornerShape(9.dp),
                                border = BorderStroke(1.dp, ForgeCardBorder),
                                modifier = Modifier.testTag("download_readme_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Description,
                                    contentDescription = null,
                                    tint = ForgeBlueLight,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Save README.md", color = ForgeTextPrimary)
                            }

                            if (project != null) {
                                OutlinedButton(
                                    onClick = {
                                        GitHubExportService.shareProjectBundle(context, project)
                                    },
                                    shape = RoundedCornerShape(9.dp),
                                    border = BorderStroke(1.dp, ForgeCardBorder),
                                    modifier = Modifier.testTag("share_project_bundle_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = null,
                                        tint = ForgeVioletLight,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Share", color = ForgeTextPrimary)
                                }
                            }
                        }
                    }
                }

                // OPTION 2: 1-Click Direct GitHub Repository Creation & Push (via GitHub REST API)
                Surface(
                    color = ForgeSurfaceElevated,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, ForgeElectricBlue.copy(alpha = 0.55f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudUpload,
                                contentDescription = null,
                                tint = ForgeBlueLight,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Option 2: Auto-Create & Push to GitHub (No 404)",
                                style = MaterialTheme.typography.titleMedium,
                                color = ForgeTextPrimary
                            )
                        }

                        Text(
                            text = "Creates the repository directly on your GitHub account as PUBLIC by default so its URL and .ZIP download link work immediately in any browser without 404 errors.",
                            style = MaterialTheme.typography.bodySmall,
                            color = ForgeTextSecondary
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("github_repo_name_input")
                        )

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
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("github_pat_input")
                            )

                            OutlinedButton(
                                onClick = {
                                    GitHubExportService.openGitHubGenerateTokenPage(context)
                                },
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, ForgeCardBorder),
                                modifier = Modifier.testTag("get_github_token_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Key,
                                    contentDescription = null,
                                    tint = ForgeVioletLight,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Get Token", style = MaterialTheme.typography.labelSmall, color = ForgeTextPrimary)
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LockOpen,
                                    contentDescription = null,
                                    tint = ForgeSuccessGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = if (createAsPublicRepo) {
                                        "Public Repository (Recommended — avoids 404 without sign-in)"
                                    } else {
                                        "Private Repository (Requires GitHub sign-in to view)"
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ForgeTextSecondary
                                )
                            }
                            Switch(
                                checked = createAsPublicRepo,
                                onCheckedChange = { createAsPublicRepo = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = ForgeSuccessGreen
                                )
                            )
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
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, ForgeSuccessGreen.copy(alpha = 0.5f))
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = ForgeSuccessGreen,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Text(
                                            text = "Live on GitHub: ${result.repoFullName}",
                                            style = MaterialTheme.typography.titleSmall,
                                            color = ForgeSuccessGreen
                                        )
                                    }
                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = {
                                                GitHubExportService.openBrowserUrl(context, result.zipDownloadUrl)
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = ForgeSuccessGreen),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Download,
                                                contentDescription = null,
                                                modifier = Modifier.size(15.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Download GitHub main.zip", color = Color.White)
                                        }
                                        OutlinedButton(
                                            onClick = {
                                                GitHubExportService.openBrowserUrl(context, result.repoHtmlUrl)
                                            },
                                            border = BorderStroke(1.dp, ForgeSuccessGreen),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                                contentDescription = null,
                                                tint = ForgeSuccessGreen,
                                                modifier = Modifier.size(15.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Open Repo on GitHub", color = ForgeTextPrimary)
                                        }
                                    }
                                }
                            }
                        }

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    pushError = null
                                    pushSuccessResult = null
                                    isPushingToGitHub = true
                                    scope.launch {
                                        val res = GitHubExportService.pushProjectDirectlyToGitHubApi(
                                            personalAccessToken = githubTokenInput,
                                            repoName = repoNameInput,
                                            description = project?.description ?: "WORLD FORGE AI Game Project",
                                            isPrivate = !createAsPublicRepo,
                                            project = project,
                                            allProjects = allProjects
                                        )
                                        isPushingToGitHub = false
                                        res.onSuccess { pushed ->
                                            pushSuccessResult = pushed
                                            val owner = pushed.repoFullName.substringBefore("/")
                                            if (owner.isNotBlank()) {
                                                existingGithubUsername = owner
                                                GitHubExportService.saveGitHubUsername(context, owner)
                                            }
                                            onExportStatusMessage("Successfully created & pushed ${pushed.repoFullName} on GitHub!")
                                        }.onFailure { err ->
                                            pushError = err.localizedMessage ?: "Failed to push to GitHub."
                                        }
                                    }
                                },
                                enabled = !isPushingToGitHub,
                                shape = RoundedCornerShape(9.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ForgeElectricBlue,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier.testTag("push_to_github_api_button")
                            ) {
                                if (isPushingToGitHub) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        color = Color.White,
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Creating Repo on GitHub...")
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.CloudUpload,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Create & Push Repo via API", fontWeight = FontWeight.Bold)
                                }
                            }

                            OutlinedButton(
                                onClick = {
                                    GitHubExportService.copyToClipboard(
                                        context = context,
                                        label = "World Forge Project Manifest",
                                        text = jsonPayload
                                    )
                                    GitHubExportService.openGitHubLoginAwareNewRepo(context, project)
                                    onExportStatusMessage("Opening GitHub Sign-In -> Create New Repo...")
                                },
                                shape = RoundedCornerShape(9.dp),
                                border = BorderStroke(1.dp, ForgeCardBorder),
                                modifier = Modifier.testTag("open_github_new_repo_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                    contentDescription = null,
                                    tint = ForgeVioletLight,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Sign In & Create on GitHub Web", color = ForgeTextPrimary)
                            }
                        }
                    }
                }

                // OPTION 3: Direct ZIP Download from an Existing Public GitHub Repo
                Surface(
                    color = ForgeSurfaceElevated,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, ForgeCardBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Code,
                                contentDescription = null,
                                tint = ForgeVioletLight,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Option 3: Download ZIP from Existing Public GitHub Repo",
                                style = MaterialTheme.typography.titleMedium,
                                color = ForgeTextPrimary
                            )
                        }
                        Text(
                            text = "Already have a public repository on GitHub? Enter your GitHub username and repo name to download its branch .ZIP directly (make sure the repo is set to Public in GitHub Settings so it doesn't return 404):",
                            style = MaterialTheme.typography.bodySmall,
                            color = ForgeTextSecondary
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = existingGithubUsername,
                                onValueChange = {
                                    existingGithubUsername = it
                                    GitHubExportService.saveGitHubUsername(context, it)
                                },
                                label = { Text("GitHub Username") },
                                placeholder = { Text("e.g. rounak...", color = ForgeTextMuted) },
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
                                value = existingRepoName,
                                onValueChange = { existingRepoName = it },
                                label = { Text("Repo Name") },
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

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val cleanOwner = existingGithubUsername.trim()
                            val cleanRepo = existingRepoName.trim()
                            val cleanBranch = existingBranchName.trim().ifEmpty { "main" }

                            Button(
                                onClick = {
                                    if (cleanOwner.isEmpty() || cleanRepo.isEmpty()) {
                                        onExportStatusMessage("Please enter both your GitHub Username and Repo Name.")
                                    } else {
                                        val zipUrl = "https://github.com/$cleanOwner/$cleanRepo/archive/refs/heads/$cleanBranch.zip"
                                        GitHubExportService.openBrowserUrl(context, zipUrl)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ForgeVioletAccent),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Download Branch .ZIP from GitHub", color = Color.White)
                            }

                            OutlinedButton(
                                onClick = {
                                    if (cleanOwner.isEmpty()) {
                                        GitHubExportService.openBrowserUrl(context, "https://github.com/login")
                                    } else {
                                        val path = if (cleanRepo.isNotEmpty()) "/$cleanOwner/$cleanRepo" else "/$cleanOwner"
                                        val loginUrl = "https://github.com/login?return_to=${Uri.encode(path)}"
                                        GitHubExportService.openBrowserUrl(context, loginUrl)
                                    }
                                },
                                border = BorderStroke(1.dp, ForgeCardBorder),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                    contentDescription = null,
                                    tint = ForgeBlueLight,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Sign In & Open Repo", color = ForgeTextPrimary)
                            }
                        }
                    }
                }

                // Live Preview Section
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = { showReadmePreview = false },
                                border = BorderStroke(
                                    1.dp,
                                    if (!showReadmePreview) ForgeElectricBlue else ForgeCardBorder
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "worldforge.project.json",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (!showReadmePreview) ForgeBlueLight else ForgeTextSecondary
                                )
                            }
                            OutlinedButton(
                                onClick = { showReadmePreview = true },
                                border = BorderStroke(
                                    1.dp,
                                    if (showReadmePreview) ForgeElectricBlue else ForgeCardBorder
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "README.md",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (showReadmePreview) ForgeBlueLight else ForgeTextSecondary
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                val textToCopy = if (showReadmePreview) readmePayload else jsonPayload
                                val label = if (showReadmePreview) "README.md" else "worldforge.project.json"
                                GitHubExportService.copyToClipboard(context, label, textToCopy)
                                onExportStatusMessage("Copied $label to clipboard.")
                            },
                            modifier = Modifier.testTag("copy_export_payload_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy file content",
                                tint = ForgeBlueLight,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    val displayedText = if (showReadmePreview) readmePayload else jsonPayload
                    Surface(
                        color = Color(0xFF070A0F),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, ForgeCardBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp, max = 180.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(12.dp)
                                .verticalScroll(rememberScrollState())
                                .horizontalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = displayedText,
                                style = MaterialTheme.typography.labelMedium,
                                color = ForgeTextPrimary
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = ForgeSurfaceElevated),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Done", color = ForgeTextPrimary)
                    }
                }
            }
        }
    }
}
