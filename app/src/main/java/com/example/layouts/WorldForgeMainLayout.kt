package com.example.layouts

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import com.example.assets.ForgeAssetCatalog
import com.example.components.CreateNewProjectDialog
import com.example.components.DeleteProjectConfirmDialog
import com.example.components.ForgeBrandHeaderBadge
import com.example.components.GitHubExportDialog
import com.example.components.RenameProjectDialog
import com.example.pages.ComingSoonModulePage
import com.example.pages.DashboardPage
import com.example.pages.EngineSettingsPage
import com.example.pages.MyProjectsPage
import com.example.projectmanager.ProjectManagerViewModel
import com.example.projectmanager.ProjectsUiState
import com.example.services.AuthService
import com.example.styles.engineGridBackground
import com.example.types.ForgeNavDestination
import com.example.ui.theme.ForgeBlueLight
import com.example.ui.theme.ForgeCardBorder
import com.example.ui.theme.ForgeCharcoalBg
import com.example.ui.theme.ForgeElectricBlue
import com.example.ui.theme.ForgeSuccessGreen
import com.example.ui.theme.ForgeSurface
import com.example.ui.theme.ForgeSurfaceElevated
import com.example.ui.theme.ForgeTextMuted
import com.example.ui.theme.ForgeTextPrimary
import com.example.ui.theme.ForgeTextSecondary
import com.example.ui.theme.ForgeVioletLight
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorldForgeMainLayout(
    viewModel: ProjectManagerViewModel,
    userEmail: String?
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember(context) { CredentialManager.create(context) }

    val projectsState by viewModel.projectsState.collectAsState()
    val currentDestination by viewModel.currentDestination.collectAsState()
    val activeOpenedProjectId by viewModel.activeOpenedProjectId.collectAsState()
    val showCreateDialog by viewModel.showCreateDialog.collectAsState()
    val projectToRename by viewModel.projectToRename.collectAsState()
    val projectToDelete by viewModel.projectToDelete.collectAsState()
    val showGitHubExportModal by viewModel.showGitHubExportModal.collectAsState()
    val projectForGitHubExport by viewModel.projectForGitHubExport.collectAsState()
    val statusBannerMessage by viewModel.statusBannerMessage.collectAsState()
    val isSubmitting by viewModel.isSubmitting.collectAsState()

    val projectsList = (projectsState as? ProjectsUiState.Success)?.projects.orEmpty()
    val activeOpenedProject = projectsList.find { it.id == activeOpenedProjectId }

    // BackHandler for secondary screens or active inspector
    BackHandler(enabled = currentDestination != ForgeNavDestination.DASHBOARD || activeOpenedProject != null) {
        if (currentDestination != ForgeNavDestination.DASHBOARD) {
            viewModel.navigateTo(ForgeNavDestination.DASHBOARD)
        } else if (activeOpenedProject != null) {
            viewModel.closeOpenedProjectInspector()
        }
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // Dialogs
    if (showCreateDialog) {
        CreateNewProjectDialog(
            isSubmitting = isSubmitting,
            onDismiss = { viewModel.closeCreateProjectDialog() },
            onCreateProject = { draft ->
                viewModel.createProject(draft)
            }
        )
    }

    if (projectToRename != null) {
        RenameProjectDialog(
            project = projectToRename!!,
            isSubmitting = isSubmitting,
            onDismiss = { viewModel.dismissRenameDialog() },
            onConfirmRename = { updatedName ->
                viewModel.confirmRenameProject(updatedName)
            }
        )
    }

    if (projectToDelete != null) {
        DeleteProjectConfirmDialog(
            project = projectToDelete!!,
            isSubmitting = isSubmitting,
            onDismiss = { viewModel.dismissDeleteDialog() },
            onConfirmDelete = {
                viewModel.confirmDeleteProject()
            }
        )
    }

    if (showGitHubExportModal) {
        GitHubExportDialog(
            project = projectForGitHubExport ?: activeOpenedProject,
            allProjects = projectsList,
            onDismiss = { viewModel.closeGitHubExportModal() },
            onExportStatusMessage = { msg ->
                viewModel.postStatusMessage(msg)
            }
        )
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isExpandedScreen = maxWidth >= 840.dp

        if (isExpandedScreen) {
            // Wide / Tablet / Landscape Desktop Layout with Permanent Sidebar
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ForgeCharcoalBg)
            ) {
                Surface(
                    modifier = Modifier
                        .width(280.dp)
                        .fillMaxHeight(),
                    color = ForgeSurface,
                    border = BorderStroke(1.dp, ForgeCardBorder)
                ) {
                    ForgeNavigationSidebarContent(
                        currentDestination = currentDestination,
                        userEmail = userEmail,
                        onSelectDestination = { dest ->
                            viewModel.navigateTo(dest)
                        },
                        onOpenGitHubHub = {
                            viewModel.openGitHubExportModal(activeOpenedProject)
                        },
                        onSignOut = {
                            AuthService.signOut(credentialManager, scope)
                        }
                    )
                }

                Scaffold(
                    modifier = Modifier.weight(1f),
                    containerColor = ForgeCharcoalBg,
                    contentWindowInsets = WindowInsets.safeDrawing,
                    topBar = {
                        ForgeTopAppBar(
                            currentDestination = currentDestination,
                            showMenuButton = false,
                            onMenuClick = {},
                            onGitHubDownloadClick = {
                                viewModel.openGitHubExportModal(activeOpenedProject)
                            },
                            onQuickCreateClick = { viewModel.openCreateProjectDialog() }
                        )
                    }
                ) { innerPadding ->
                    WorkspaceMainContentArea(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        currentDestination = currentDestination,
                        projectsState = projectsState,
                        activeOpenedProject = activeOpenedProject,
                        statusBannerMessage = statusBannerMessage,
                        currentUserId = viewModel.currentUserId,
                        onDismissBanner = { viewModel.clearStatusBanner() },
                        onCreateNewProject = { viewModel.openCreateProjectDialog() },
                        onOpenProject = { viewModel.openProject(it) },
                        onRenameProject = { viewModel.requestRenameProject(it) },
                        onDeleteProject = { viewModel.requestDeleteProject(it) },
                        onExportToGitHub = { viewModel.openGitHubExportModal(it) },
                        onCloseOpenedProject = { viewModel.closeOpenedProjectInspector() },
                        onNavigateTo = { viewModel.navigateTo(it) }
                    )
                }
            }
        } else {
            // Portrait / Compact Mobile Layout with Modal Navigation Drawer
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet(
                        drawerContainerColor = ForgeSurface,
                        drawerContentColor = ForgeTextPrimary,
                        modifier = Modifier.width(300.dp)
                    ) {
                        ForgeNavigationSidebarContent(
                            currentDestination = currentDestination,
                            userEmail = userEmail,
                            onSelectDestination = { dest ->
                                scope.launch { drawerState.close() }
                                viewModel.navigateTo(dest)
                            },
                            onOpenGitHubHub = {
                                scope.launch { drawerState.close() }
                                viewModel.openGitHubExportModal(activeOpenedProject)
                            },
                            onSignOut = {
                                scope.launch { drawerState.close() }
                                AuthService.signOut(credentialManager, scope)
                            }
                        )
                    }
                }
            ) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = ForgeCharcoalBg,
                    contentWindowInsets = WindowInsets.safeDrawing,
                    topBar = {
                        ForgeTopAppBar(
                            currentDestination = currentDestination,
                            showMenuButton = true,
                            onMenuClick = {
                                scope.launch { drawerState.open() }
                            },
                            onGitHubDownloadClick = {
                                viewModel.openGitHubExportModal(activeOpenedProject)
                            },
                            onQuickCreateClick = { viewModel.openCreateProjectDialog() }
                        )
                    }
                ) { innerPadding ->
                    WorkspaceMainContentArea(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        currentDestination = currentDestination,
                        projectsState = projectsState,
                        activeOpenedProject = activeOpenedProject,
                        statusBannerMessage = statusBannerMessage,
                        currentUserId = viewModel.currentUserId,
                        onDismissBanner = { viewModel.clearStatusBanner() },
                        onCreateNewProject = { viewModel.openCreateProjectDialog() },
                        onOpenProject = { viewModel.openProject(it) },
                        onRenameProject = { viewModel.requestRenameProject(it) },
                        onDeleteProject = { viewModel.requestDeleteProject(it) },
                        onExportToGitHub = { viewModel.openGitHubExportModal(it) },
                        onCloseOpenedProject = { viewModel.closeOpenedProjectInspector() },
                        onNavigateTo = { viewModel.navigateTo(it) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ForgeTopAppBar(
    currentDestination: ForgeNavDestination,
    showMenuButton: Boolean,
    onMenuClick: () -> Unit,
    onGitHubDownloadClick: () -> Unit,
    onQuickCreateClick: () -> Unit
) {
    Column {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = ForgeSurface,
                titleContentColor = ForgeTextPrimary
            ),
            navigationIcon = {
                if (showMenuButton) {
                    IconButton(
                        onClick = onMenuClick,
                        modifier = Modifier.testTag("mobile_drawer_menu_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Open navigation menu",
                            tint = ForgeTextPrimary
                        )
                    }
                }
            },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = currentDestination.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = ForgeTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!currentDestination.isCoreImplemented) {
                        Surface(
                            color = ForgeVioletLight.copy(alpha = 0.16f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "COMING SOON",
                                style = MaterialTheme.typography.labelSmall,
                                color = ForgeVioletLight,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            },
            actions = {
                IconButton(
                    onClick = onGitHubDownloadClick,
                    modifier = Modifier.testTag("topbar_github_export_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "GitHub Export & Direct Download",
                        tint = ForgeBlueLight
                    )
                }
                Button(
                    onClick = onQuickCreateClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ForgeElectricBlue,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .testTag("topbar_create_project_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("New Game", style = MaterialTheme.typography.labelLarge)
                }
            }
        )
        HorizontalDivider(color = ForgeCardBorder)
    }
}

@Composable
private fun ForgeNavigationSidebarContent(
    currentDestination: ForgeNavDestination,
    userEmail: String?,
    onSelectDestination: (ForgeNavDestination) -> Unit,
    onOpenGitHubHub: () -> Unit,
    onSignOut: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            ForgeBrandHeaderBadge(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(color = ForgeCardBorder)
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "MAIN NAVIGATION",
                style = MaterialTheme.typography.labelSmall,
                color = ForgeTextMuted,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )

            ForgeNavDestination.entries.forEach { destination ->
                val isSelected = currentDestination == destination
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = ForgeAssetCatalog.iconForDestination(destination),
                            contentDescription = destination.title,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    label = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = destination.title,
                                style = MaterialTheme.typography.titleSmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            if (!destination.isCoreImplemented) {
                                Surface(
                                    color = ForgeSurfaceElevated,
                                    shape = RoundedCornerShape(4.dp),
                                    border = BorderStroke(1.dp, ForgeCardBorder)
                                ) {
                                    Text(
                                        text = "SOON",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = ForgeVioletLight,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }
                    },
                    selected = isSelected,
                    onClick = { onSelectDestination(destination) },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = ForgeElectricBlue.copy(alpha = 0.2f),
                        selectedIconColor = ForgeBlueLight,
                        selectedTextColor = Color.White,
                        unselectedContainerColor = Color.Transparent,
                        unselectedIconColor = ForgeTextSecondary,
                        unselectedTextColor = ForgeTextSecondary
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("nav_item_${destination.route}")
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            HorizontalDivider(color = ForgeCardBorder)
            OutlinedButton(
                onClick = onOpenGitHubHub,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, ForgeElectricBlue.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("sidebar_github_download_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = null,
                    tint = ForgeBlueLight,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "GitHub / Download",
                    style = MaterialTheme.typography.labelLarge,
                    color = ForgeBlueLight
                )
            }
            Surface(
                color = ForgeSurfaceElevated,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, ForgeCardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = userEmail ?: "Authenticated Developer",
                            style = MaterialTheme.typography.labelMedium,
                            color = ForgeTextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Workspace Connected",
                            style = MaterialTheme.typography.labelSmall,
                            color = ForgeSuccessGreen
                        )
                    }
                    IconButton(
                        onClick = onSignOut,
                        modifier = Modifier.testTag("sign_out_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Sign Out",
                            tint = ForgeTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WorkspaceMainContentArea(
    modifier: Modifier = Modifier,
    currentDestination: ForgeNavDestination,
    projectsState: ProjectsUiState,
    activeOpenedProject: com.example.types.ForgeProject?,
    statusBannerMessage: String?,
    currentUserId: String,
    onDismissBanner: () -> Unit,
    onCreateNewProject: () -> Unit,
    onOpenProject: (com.example.types.ForgeProject) -> Unit,
    onRenameProject: (com.example.types.ForgeProject) -> Unit,
    onDeleteProject: (com.example.types.ForgeProject) -> Unit,
    onExportToGitHub: (com.example.types.ForgeProject?) -> Unit,
    onCloseOpenedProject: () -> Unit,
    onNavigateTo: (ForgeNavDestination) -> Unit
) {
    val projectCount = (projectsState as? ProjectsUiState.Success)?.projects?.size ?: 0

    Box(
        modifier = modifier
            .background(ForgeCharcoalBg)
            .engineGridBackground(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 1040.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AnimatedVisibility(visible = statusBannerMessage != null) {
                if (statusBannerMessage != null) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("status_feedback_banner"),
                        color = ForgeSurfaceElevated,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, ForgeElectricBlue)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = statusBannerMessage,
                                style = MaterialTheme.typography.bodyMedium,
                                color = ForgeTextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = onDismissBanner) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Dismiss notification",
                                    tint = ForgeTextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            when (currentDestination) {
                ForgeNavDestination.DASHBOARD,
                ForgeNavDestination.CREATE_NEW_GAME -> {
                    DashboardPage(
                        projectsState = projectsState,
                        activeOpenedProject = activeOpenedProject,
                        onCreateNewProject = onCreateNewProject,
                        onOpenProject = onOpenProject,
                        onRenameProject = onRenameProject,
                        onDeleteProject = onDeleteProject,
                        onExportToGitHub = onExportToGitHub,
                        onCloseOpenedProject = onCloseOpenedProject,
                        onNavigateTo = onNavigateTo
                    )
                }
                ForgeNavDestination.MY_PROJECTS -> {
                    MyProjectsPage(
                        projectsState = projectsState,
                        activeOpenedProject = activeOpenedProject,
                        onCreateNewProject = onCreateNewProject,
                        onOpenProject = onOpenProject,
                        onRenameProject = onRenameProject,
                        onDeleteProject = onDeleteProject,
                        onExportToGitHub = onExportToGitHub,
                        onCloseOpenedProject = onCloseOpenedProject
                    )
                }
                ForgeNavDestination.ENGINE_SETTINGS -> {
                    EngineSettingsPage(
                        currentUserId = currentUserId,
                        projectCount = projectCount,
                        onOpenGitHubExportHub = { onExportToGitHub(activeOpenedProject) },
                        onReturnToDashboard = { onNavigateTo(ForgeNavDestination.DASHBOARD) }
                    )
                }
                ForgeNavDestination.AI_WORLD_GENERATOR,
                ForgeNavDestination.IMPORT_3D_WORLD,
                ForgeNavDestination.AI_GAME_DEVELOPER,
                ForgeNavDestination.ASSET_LIBRARY -> {
                    ComingSoonModulePage(
                        destination = currentDestination,
                        onReturnToDashboard = { onNavigateTo(ForgeNavDestination.DASHBOARD) },
                        onCreateNewProject = onCreateNewProject
                    )
                }
            }
        }
    }
}
