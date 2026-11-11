package com.example.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.assets.ForgeAssetCatalog
import com.example.components.EmptyProjectsStateCard
import com.example.components.ForgeProjectCard
import com.example.components.OpenedProjectWorkspaceBanner
import com.example.components.QuickAccessToolCard
import com.example.engine.EngineArchitectureRegistry
import com.example.engine.EngineSubsystemSpec
import com.example.projectmanager.ProjectsUiState
import com.example.styles.ForgeStyles
import com.example.types.ForgeNavDestination
import com.example.types.ForgeProject
import com.example.ui.theme.ForgeBlueLight
import com.example.ui.theme.ForgeCardBorder
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DashboardPage(
    projectsState: ProjectsUiState,
    activeOpenedProject: ForgeProject?,
    onCreateNewProject: () -> Unit,
    onOpenProject: (ForgeProject) -> Unit,
    onRenameProject: (ForgeProject) -> Unit,
    onDeleteProject: (ForgeProject) -> Unit,
    onExportToGitHub: (ForgeProject?) -> Unit = {},
    onPostStatusMessage: (String) -> Unit = {},
    onCloseOpenedProject: () -> Unit,
    onNavigateTo: (ForgeNavDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    val allProjects = (projectsState as? ProjectsUiState.Success)?.projects.orEmpty()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 0. Prominent Direct Android .APK & GitHub Download Center (Impossible to miss!)
        com.example.components.DirectApkAndGitHubBannerCard(
            activeProject = activeOpenedProject ?: allProjects.firstOrNull(),
            allProjects = allProjects,
            onOpenApkInstallPage = {
                onNavigateTo(ForgeNavDestination.APK_DOWNLOAD_INSTALL)
            },
            onOpenGitHubApkHub = {
                onExportToGitHub(activeOpenedProject ?: allProjects.firstOrNull())
            },
            onStatusMessage = onPostStatusMessage
        )

        // 1. Hero Welcome & Step 01 Foundation Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("dashboard_welcome_card"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = ForgeSurface),
            border = BorderStroke(1.dp, ForgeCardBorder)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = ForgeAssetCatalog.HeroBannerRes),
                    contentDescription = "World Forge AI Engine Viewport Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(ForgeStyles.HeroOverlayBrush)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            color = ForgeElectricBlue.copy(alpha = 0.22f),
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(1.dp, ForgeElectricBlue.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = "ENGINE BUILD v0.1.0 · STEP 01 FOUNDATION",
                                style = MaterialTheme.typography.labelSmall,
                                color = ForgeBlueLight,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Text(
                        text = "Welcome to WORLD FORGE AI",
                        style = MaterialTheme.typography.displayMedium,
                        color = ForgeTextPrimary
                    )

                    Text(
                        text = "Professional AI-Powered Open World Game Development Platform. Create, configure, and persist your game projects with modular architecture ready for 3D scene rendering, procedural biome generation, physics, and Android APK/AAB export.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ForgeTextSecondary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onCreateNewProject,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ForgeElectricBlue,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.testTag("dashboard_create_project_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Create New Project",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedButton(
                            onClick = { onNavigateTo(ForgeNavDestination.MY_PROJECTS) },
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, ForgeCardBorder),
                            modifier = Modifier.testTag("dashboard_view_all_projects_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.FolderOpen,
                                contentDescription = null,
                                tint = ForgeBlueLight,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Browse My Projects",
                                style = MaterialTheme.typography.labelLarge,
                                color = ForgeTextPrimary
                            )
                        }

                        Button(
                            onClick = { onNavigateTo(ForgeNavDestination.APK_DOWNLOAD_INSTALL) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ForgeSuccessGreen,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.testTag("dashboard_github_download_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "APK Download & Install Page",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // 2. First Development Version Notice (Explicit Requirement 5)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("first_dev_version_banner"),
            color = ForgeSurfaceElevated,
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, ForgeVioletAccent.copy(alpha = 0.45f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "First Development Stage Notice",
                    tint = ForgeVioletLight,
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .size(22.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "First Development Version — Step 01: Foundation & Main Dashboard",
                        style = MaterialTheme.typography.titleMedium,
                        color = ForgeTextPrimary
                    )
                    Text(
                        text = "This release delivers the core WORLD FORGE AI application shell, modular architecture, responsive navigation, and persistent project management (Create, Open, Rename, Delete, Cloud Sync, and GitHub Export). Future engine modules (Three.js 3D Viewport, AI Open World Generator, 3D World Importer & Scanner, Physics, Visual Scripting, and APK/AAB Export) are cleanly separated into dedicated module boundaries for upcoming stages.",
                        style = MaterialTheme.typography.bodySmall,
                        color = ForgeTextSecondary
                    )
                }
            }
        }

        // 3. Active Opened Project Inspector (if user opened a project)
        if (activeOpenedProject != null) {
            OpenedProjectWorkspaceBanner(
                project = activeOpenedProject,
                onRename = onRenameProject,
                onDelete = onDeleteProject,
                onExportToGitHub = { onExportToGitHub(it) },
                onCloseInspector = onCloseOpenedProject
            )
        }

        // 4. Recent Projects Section
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Recent Projects",
                        style = MaterialTheme.typography.headlineMedium,
                        color = ForgeTextPrimary
                    )
                    Text(
                        text = "Recently created or modified game development projects",
                        style = MaterialTheme.typography.bodySmall,
                        color = ForgeTextSecondary
                    )
                }
                TextButton(
                    onClick = { onNavigateTo(ForgeNavDestination.MY_PROJECTS) },
                    modifier = Modifier.testTag("recent_projects_see_all_button")
                ) {
                    Text("All Projects", color = ForgeBlueLight)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = ForgeBlueLight,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            when (projectsState) {
                is ProjectsUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = ForgeElectricBlue)
                    }
                }
                is ProjectsUiState.Error -> {
                    Surface(
                        color = ForgeSurface,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, ForgeWarningAmber.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = projectsState.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = ForgeWarningAmber,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                is ProjectsUiState.Success -> {
                    if (projectsState.projects.isEmpty()) {
                        EmptyProjectsStateCard(onCreateNewProject = onCreateNewProject)
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            projectsState.projects.take(4).forEach { project ->
                                ForgeProjectCard(
                                    project = project,
                                    onOpenProject = onOpenProject,
                                    onRenameProject = onRenameProject,
                                    onDeleteProject = onDeleteProject,
                                    onExportToGitHub = { onExportToGitHub(it) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // 5. Quick Access Tools Section
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Column {
                Text(
                    text = "Quick Access Engine Tools",
                    style = MaterialTheme.typography.headlineMedium,
                    color = ForgeTextPrimary
                )
                Text(
                    text = "Jump directly to active workspace tools or inspect upcoming engine module specifications",
                    style = MaterialTheme.typography.bodySmall,
                    color = ForgeTextSecondary
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                QuickAccessToolCard(
                    title = "Create New Game Project",
                    subtitle = "Configure game type, target platform (Web/Android/Both), graphics preset & orientation",
                    badgeText = "ACTIVE · STEP 01",
                    isReadyInStep01 = true,
                    icon = Icons.Default.Add,
                    onClick = onCreateNewProject,
                    testTag = "quick_tool_create_project"
                )
                QuickAccessToolCard(
                    title = "GitHub Export & Direct Download Hub",
                    subtitle = "Download project .JSON manifest, README.md, or create a GitHub repository directly",
                    badgeText = "ACTIVE · STEP 01",
                    isReadyInStep01 = true,
                    icon = Icons.Default.Download,
                    onClick = { onExportToGitHub(activeOpenedProject) },
                    testTag = "quick_tool_github_export"
                )
                QuickAccessToolCard(
                    title = "AI Open World Generator",
                    subtitle = "Procedural terrain heightfields, multi-biome synthesis, and road/river splines",
                    badgeText = "COMING SOON · STEP 03",
                    isReadyInStep01 = false,
                    icon = Icons.Default.Public,
                    onClick = { onNavigateTo(ForgeNavDestination.AI_WORLD_GENERATOR) },
                    testTag = "quick_tool_ai_world_gen"
                )
                QuickAccessToolCard(
                    title = "Import 3D World & Auto-Scanner",
                    subtitle = "Ingest GLTF/GLB/FBX worlds with automatic AI topology scanning and collider fitting",
                    badgeText = "COMING SOON · STEP 03",
                    isReadyInStep01 = false,
                    icon = Icons.Default.ViewInAr,
                    onClick = { onNavigateTo(ForgeNavDestination.IMPORT_3D_WORLD) },
                    testTag = "quick_tool_import_3d"
                )
                QuickAccessToolCard(
                    title = "AI Game Developer & Visual Scripting",
                    subtitle = "Node-based gameplay graphs, NPC/Vehicle logic, and AI scripting assistant",
                    badgeText = "COMING SOON · STEP 04–05",
                    isReadyInStep01 = false,
                    icon = Icons.Default.Code,
                    onClick = { onNavigateTo(ForgeNavDestination.AI_GAME_DEVELOPER) },
                    testTag = "quick_tool_ai_dev"
                )
                QuickAccessToolCard(
                    title = "Asset Library",
                    subtitle = "Modular characters, rigged NPCs, raycast vehicles, PBR materials & props",
                    badgeText = "COMING SOON · STEP 04",
                    isReadyInStep01 = false,
                    icon = Icons.Default.AccountTree,
                    onClick = { onNavigateTo(ForgeNavDestination.ASSET_LIBRARY) },
                    testTag = "quick_tool_asset_library"
                )
                QuickAccessToolCard(
                    title = "Engine Settings & Build Targets",
                    subtitle = "Inspect rendering pipeline defaults, physics step rates, and Android APK/AAB export architecture",
                    badgeText = "ACTIVE · STEP 01",
                    isReadyInStep01 = true,
                    icon = Icons.Default.Settings,
                    onClick = { onNavigateTo(ForgeNavDestination.ENGINE_SETTINGS) },
                    testTag = "quick_tool_engine_settings"
                )
            }
        }

        // 6. Development Status Section
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Column {
                Text(
                    text = "Platform Development Status & Roadmap",
                    style = MaterialTheme.typography.headlineMedium,
                    color = ForgeTextPrimary
                )
                Text(
                    text = "Transparent subsystem readiness across the WORLD FORGE AI architecture",
                    style = MaterialTheme.typography.bodySmall,
                    color = ForgeTextSecondary
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                EngineArchitectureRegistry.subsystems.forEach { spec ->
                    SubsystemStatusCard(spec = spec)
                }
            }
        }
    }
}

@Composable
private fun SubsystemStatusCard(spec: EngineSubsystemSpec) {
    val isReady = spec.stage.isReadyInStep01
    val statusColor = if (isReady) ForgeSuccessGreen else ForgeBlueLight

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ForgeSurface),
        border = BorderStroke(1.dp, ForgeCardBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = if (isReady) Icons.Default.CheckCircle else Icons.Default.Schedule,
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = spec.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = ForgeTextPrimary
                    )
                }
                Surface(
                    color = statusColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = spec.stage.badgeText,
                        style = MaterialTheme.typography.labelSmall,
                        color = statusColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Text(
                text = spec.summary,
                style = MaterialTheme.typography.bodySmall,
                color = ForgeTextSecondary
            )

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                spec.technicalSpecs.forEach { item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .clip(CircleShape)
                                .background(statusColor)
                        )
                        Text(
                            text = item,
                            style = MaterialTheme.typography.labelSmall,
                            color = ForgeTextMuted
                        )
                    }
                }
            }
        }
    }
}
