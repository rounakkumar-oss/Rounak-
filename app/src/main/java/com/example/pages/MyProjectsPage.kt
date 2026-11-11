package com.example.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.components.EmptyProjectsStateCard
import com.example.components.ForgeProjectCard
import com.example.components.OpenedProjectWorkspaceBanner
import com.example.projectmanager.ProjectsUiState
import com.example.types.ForgeProject
import com.example.types.GameType
import com.example.ui.theme.ForgeCardBorder
import com.example.ui.theme.ForgeCardBorderActive
import com.example.ui.theme.ForgeElectricBlue
import com.example.ui.theme.ForgeSurface
import com.example.ui.theme.ForgeSurfaceElevated
import com.example.ui.theme.ForgeTextMuted
import com.example.ui.theme.ForgeTextPrimary
import com.example.ui.theme.ForgeTextSecondary
import com.example.ui.theme.ForgeWarningAmber

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MyProjectsPage(
    projectsState: ProjectsUiState,
    activeOpenedProject: ForgeProject?,
    onCreateNewProject: () -> Unit,
    onOpenProject: (ForgeProject) -> Unit,
    onRenameProject: (ForgeProject) -> Unit,
    onDeleteProject: (ForgeProject) -> Unit,
    onExportToGitHub: (ForgeProject?) -> Unit = {},
    onCloseOpenedProject: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedGenreFilter by rememberSaveable { mutableStateOf("All") }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "My Projects",
                    style = MaterialTheme.typography.displayMedium,
                    color = ForgeTextPrimary
                )
                Text(
                    text = "Manage, open, rename, export to GitHub, or delete your persistent WORLD FORGE AI game projects",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ForgeTextSecondary
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Button(
                onClick = onCreateNewProject,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ForgeElectricBlue,
                    contentColor = Color.White
                ),
                modifier = Modifier.testTag("my_projects_create_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("New Project", fontWeight = FontWeight.SemiBold)
            }
        }

        if (activeOpenedProject != null) {
            OpenedProjectWorkspaceBanner(
                project = activeOpenedProject,
                onRename = onRenameProject,
                onDelete = onDeleteProject,
                onExportToGitHub = { onExportToGitHub(it) },
                onCloseInspector = onCloseOpenedProject
            )
        }

        // Search & Genre Filter Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = {
                Text("Search projects by name, description, or platform...", color = ForgeTextMuted)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = ForgeTextSecondary
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear search",
                            tint = ForgeTextSecondary
                        )
                    }
                }
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ForgeElectricBlue,
                unfocusedBorderColor = ForgeCardBorder,
                focusedTextColor = ForgeTextPrimary,
                unfocusedTextColor = ForgeTextPrimary,
                focusedContainerColor = ForgeSurface,
                unfocusedContainerColor = ForgeSurface
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("my_projects_search_input")
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val filters = listOf("All") + GameType.entries.map { it.label }
            filters.forEach { genre ->
                val isSelected = selectedGenreFilter == genre
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { selectedGenreFilter = genre }
                        .testTag("filter_genre_${genre.lowercase().replace(' ', '_')}"),
                    color = if (isSelected) ForgeElectricBlue.copy(alpha = 0.22f) else ForgeSurface,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) ForgeCardBorderActive else ForgeCardBorder
                    )
                ) {
                    Text(
                        text = genre,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (isSelected) Color.White else ForgeTextSecondary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                    )
                }
            }
        }

        when (projectsState) {
            is ProjectsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
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
                    val filtered = projectsState.projects.filter { project ->
                        val matchesGenre = selectedGenreFilter == "All" || project.gameType == selectedGenreFilter
                        val q = searchQuery.trim().lowercase()
                        val matchesQuery = q.isEmpty() ||
                            project.name.lowercase().contains(q) ||
                            project.description.lowercase().contains(q) ||
                            project.targetPlatform.lowercase().contains(q)
                        matchesGenre && matchesQuery
                    }

                    Text(
                        text = "Showing ${filtered.size} of ${projectsState.projects.size} project(s)",
                        style = MaterialTheme.typography.labelMedium,
                        color = ForgeTextMuted
                    )

                    if (filtered.isEmpty()) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = ForgeSurface,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, ForgeCardBorder)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "No matching projects found",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = ForgeTextPrimary
                                )
                                Text(
                                    text = "Try clearing your search query or switching the Game Type filter.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ForgeTextSecondary
                                )
                            }
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            filtered.forEach { project ->
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
    }
}
