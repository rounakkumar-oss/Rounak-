package com.example.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.SettingsSuggest
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.types.ForgeProject
import com.example.types.GameType
import com.example.types.GraphicsPreset
import com.example.types.ProjectDraft
import com.example.types.TargetPlatform
import com.example.types.ViewportOrientation
import com.example.ui.theme.ForgeBlueLight
import com.example.ui.theme.ForgeCardBorder
import com.example.ui.theme.ForgeCardBorderActive
import com.example.ui.theme.ForgeDangerRed
import com.example.ui.theme.ForgeElectricBlue
import com.example.ui.theme.ForgeSuccessGreen
import com.example.ui.theme.ForgeSurface
import com.example.ui.theme.ForgeSurfaceElevated
import com.example.ui.theme.ForgeTextMuted
import com.example.ui.theme.ForgeTextPrimary
import com.example.ui.theme.ForgeTextSecondary
import com.example.ui.theme.ForgeVioletAccent
import com.example.ui.theme.ForgeWarningAmber

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateNewProjectDialog(
    isSubmitting: Boolean,
    onDismiss: () -> Unit,
    onCreateProject: (ProjectDraft) -> Unit
) {
    var projectName by rememberSaveable { mutableStateOf("") }
    var projectDescription by rememberSaveable { mutableStateOf("") }
    var selectedGameType by rememberSaveable { mutableStateOf(GameType.OPEN_WORLD) }
    var selectedPlatform by rememberSaveable { mutableStateOf(TargetPlatform.BOTH) }
    var selectedPreset by rememberSaveable { mutableStateOf(GraphicsPreset.HIGH) }
    var selectedOrientation by rememberSaveable { mutableStateOf(ViewportOrientation.LANDSCAPE) }
    var nameError by rememberSaveable { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = { if (!isSubmitting) onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .widthIn(max = 600.dp)
                .clip(RoundedCornerShape(18.dp))
                .border(1.dp, ForgeCardBorder, RoundedCornerShape(18.dp))
                .testTag("create_project_dialog"),
            color = ForgeSurface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircleOutline,
                            contentDescription = null,
                            tint = ForgeElectricBlue,
                            modifier = Modifier.size(26.dp)
                        )
                        Column {
                            Text(
                                text = "Create New Game Project",
                                style = MaterialTheme.typography.headlineMedium,
                                color = ForgeTextPrimary
                            )
                            Text(
                                text = "Configure WORLD FORGE AI project parameters & build targets",
                                style = MaterialTheme.typography.bodySmall,
                                color = ForgeTextSecondary
                            )
                        }
                    }
                    IconButton(
                        onClick = { if (!isSubmitting) onDismiss() },
                        modifier = Modifier.testTag("create_dialog_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cancel",
                            tint = ForgeTextSecondary
                        )
                    }
                }

                HorizontalDivider(color = ForgeCardBorder)

                // 1. Project Name
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Project Name *",
                        style = MaterialTheme.typography.labelLarge,
                        color = ForgeTextPrimary
                    )
                    OutlinedTextField(
                        value = projectName,
                        onValueChange = {
                            projectName = it
                            if (nameError != null && it.trim().isNotEmpty()) {
                                nameError = null
                            }
                        },
                        placeholder = {
                            Text("e.g., Aethelgard Open World RPG", color = ForgeTextMuted)
                        },
                        singleLine = true,
                        isError = nameError != null,
                        supportingText = {
                            if (nameError != null) {
                                Text(text = nameError!!, color = ForgeDangerRed)
                            } else {
                                Text(
                                    text = "Required · Max 100 characters",
                                    color = ForgeTextMuted
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ForgeElectricBlue,
                            unfocusedBorderColor = ForgeCardBorder,
                            focusedTextColor = ForgeTextPrimary,
                            unfocusedTextColor = ForgeTextPrimary,
                            focusedContainerColor = ForgeSurfaceElevated,
                            unfocusedContainerColor = ForgeSurfaceElevated
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("project_name_input")
                    )
                }

                // 2. Project Description
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Project Description",
                        style = MaterialTheme.typography.labelLarge,
                        color = ForgeTextPrimary
                    )
                    OutlinedTextField(
                        value = projectDescription,
                        onValueChange = { projectDescription = it.take(500) },
                        placeholder = {
                            Text(
                                "Describe world scale, biomes, vehicle/character systems, or gameplay goals...",
                                color = ForgeTextMuted
                            )
                        },
                        minLines = 2,
                        maxLines = 4,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ForgeElectricBlue,
                            unfocusedBorderColor = ForgeCardBorder,
                            focusedTextColor = ForgeTextPrimary,
                            unfocusedTextColor = ForgeTextPrimary,
                            focusedContainerColor = ForgeSurfaceElevated,
                            unfocusedContainerColor = ForgeSurfaceElevated
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("project_description_input")
                    )
                }

                // 3. Game Type
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Game Type",
                        style = MaterialTheme.typography.labelLarge,
                        color = ForgeTextPrimary
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        GameType.entries.forEach { type ->
                            SelectableOptionChip(
                                label = type.label,
                                isSelected = selectedGameType == type,
                                onClick = { selectedGameType = type },
                                testTag = "game_type_${type.name.lowercase()}"
                            )
                        }
                    }
                }

                // 4. Target Platform
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Target Platform",
                        style = MaterialTheme.typography.labelLarge,
                        color = ForgeTextPrimary
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TargetPlatform.entries.forEach { platform ->
                            SelectableOptionChip(
                                label = platform.label,
                                isSelected = selectedPlatform == platform,
                                onClick = { selectedPlatform = platform },
                                testTag = "target_platform_${platform.name.lowercase()}"
                            )
                        }
                    }
                }

                // 5. Graphics Preset
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Graphics Preset",
                        style = MaterialTheme.typography.labelLarge,
                        color = ForgeTextPrimary
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        GraphicsPreset.entries.forEach { preset ->
                            SelectableOptionChip(
                                label = preset.label,
                                isSelected = selectedPreset == preset,
                                onClick = { selectedPreset = preset },
                                testTag = "graphics_preset_${preset.name.lowercase()}"
                            )
                        }
                    }
                    Text(
                        text = selectedPreset.detail,
                        style = MaterialTheme.typography.labelSmall,
                        color = ForgeBlueLight
                    )
                }

                // 6. Orientation
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Orientation",
                        style = MaterialTheme.typography.labelLarge,
                        color = ForgeTextPrimary
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ViewportOrientation.entries.forEach { orientation ->
                            SelectableOptionChip(
                                label = orientation.label,
                                isSelected = selectedOrientation == orientation,
                                onClick = { selectedOrientation = orientation },
                                testTag = "orientation_${orientation.name.lowercase()}"
                            )
                        }
                    }
                }

                HorizontalDivider(color = ForgeCardBorder)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        enabled = !isSubmitting,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, ForgeCardBorder),
                        modifier = Modifier.testTag("cancel_create_project_button")
                    ) {
                        Text("Cancel", color = ForgeTextSecondary)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            val trimmed = projectName.trim()
                            if (trimmed.isEmpty()) {
                                nameError = "Project Name is required to initialize a project."
                            } else if (trimmed.length > 100) {
                                nameError = "Project Name must be 100 characters or fewer."
                            } else {
                                onCreateProject(
                                    ProjectDraft(
                                        name = trimmed,
                                        description = projectDescription.trim(),
                                        gameType = selectedGameType,
                                        targetPlatform = selectedPlatform,
                                        graphicsPreset = selectedPreset,
                                        orientation = selectedOrientation
                                    )
                                )
                            }
                        },
                        enabled = !isSubmitting,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ForgeElectricBlue,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.testTag("submit_create_project_button")
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text("Create Project", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectableOptionChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val bgColor = if (isSelected) ForgeElectricBlue.copy(alpha = 0.22f) else ForgeSurfaceElevated
    val borderColor = if (isSelected) ForgeCardBorderActive else ForgeCardBorder
    val textColor = if (isSelected) Color.White else ForgeTextSecondary

    Surface(
        modifier = Modifier
            .testTag(testTag)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        color = bgColor,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = textColor,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp)
        )
    }
}

@Composable
fun RenameProjectDialog(
    project: ForgeProject,
    isSubmitting: Boolean,
    onDismiss: () -> Unit,
    onConfirmRename: (String) -> Unit
) {
    var updatedName by rememberSaveable(project.id) { mutableStateOf(project.name) }
    var errorMsg by rememberSaveable { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = { if (!isSubmitting) onDismiss() },
        containerColor = ForgeSurface,
        titleContentColor = ForgeTextPrimary,
        textContentColor = ForgeTextSecondary,
        icon = {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = ForgeElectricBlue
            )
        },
        title = {
            Text("Rename Project", style = MaterialTheme.typography.headlineMedium)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Enter a new name for \"${project.name}\":",
                    style = MaterialTheme.typography.bodyMedium
                )
                OutlinedTextField(
                    value = updatedName,
                    onValueChange = {
                        updatedName = it
                        if (errorMsg != null && it.trim().isNotEmpty()) errorMsg = null
                    },
                    singleLine = true,
                    isError = errorMsg != null,
                    supportingText = {
                        if (errorMsg != null) {
                            Text(errorMsg!!, color = ForgeDangerRed)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ForgeElectricBlue,
                        unfocusedBorderColor = ForgeCardBorder,
                        focusedTextColor = ForgeTextPrimary,
                        unfocusedTextColor = ForgeTextPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("rename_project_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val trimmed = updatedName.trim()
                    if (trimmed.isEmpty()) {
                        errorMsg = "Project Name cannot be empty."
                    } else if (trimmed.length > 100) {
                        errorMsg = "Max 100 characters allowed."
                    } else {
                        onConfirmRename(trimmed)
                    }
                },
                enabled = !isSubmitting,
                colors = ButtonDefaults.buttonColors(containerColor = ForgeElectricBlue),
                modifier = Modifier.testTag("confirm_rename_project_button")
            ) {
                Text("Save Name", color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                enabled = !isSubmitting,
                border = BorderStroke(1.dp, ForgeCardBorder),
                modifier = Modifier.testTag("cancel_rename_project_button")
            ) {
                Text("Cancel", color = ForgeTextSecondary)
            }
        }
    )
}

@Composable
fun DeleteProjectConfirmDialog(
    project: ForgeProject,
    isSubmitting: Boolean,
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { if (!isSubmitting) onDismiss() },
        containerColor = ForgeSurface,
        titleContentColor = ForgeTextPrimary,
        textContentColor = ForgeTextSecondary,
        icon = {
            Icon(
                imageVector = Icons.Default.WarningAmber,
                contentDescription = null,
                tint = ForgeDangerRed
            )
        },
        title = {
            Text("Delete Project?", style = MaterialTheme.typography.headlineMedium)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Are you sure you want to permanently delete \"${project.name}\"?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = ForgeTextPrimary
                )
                Text(
                    text = "This action removes the project configuration, scene descriptors, and build metadata from your workspace and cannot be undone.",
                    style = MaterialTheme.typography.bodySmall,
                    color = ForgeTextSecondary
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirmDelete,
                enabled = !isSubmitting,
                colors = ButtonDefaults.buttonColors(containerColor = ForgeDangerRed),
                modifier = Modifier.testTag("confirm_delete_project_button")
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteForever,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Delete Permanently", color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                enabled = !isSubmitting,
                border = BorderStroke(1.dp, ForgeCardBorder),
                modifier = Modifier.testTag("cancel_delete_project_button")
            ) {
                Text("Cancel", color = ForgeTextSecondary)
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OpenedProjectWorkspaceBanner(
    project: ForgeProject,
    onRename: (ForgeProject) -> Unit,
    onDelete: (ForgeProject) -> Unit,
    onExportToGitHub: (ForgeProject) -> Unit = {},
    onCloseInspector: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("opened_project_workspace_panel"),
        shape = RoundedCornerShape(16.dp),
        color = ForgeSurfaceElevated,
        border = BorderStroke(1.5.dp, ForgeElectricBlue)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
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
                            .size(38.dp)
                            .clip(RoundedCornerShape(9.dp))
                            .background(ForgeElectricBlue.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FolderOpen,
                            contentDescription = null,
                            tint = ForgeBlueLight
                        )
                    }
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                color = ForgeSuccessGreen.copy(alpha = 0.18f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "ACTIVE WORKSPACE PROJECT",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ForgeSuccessGreen,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Text(
                                text = "ID: ${project.id.take(10)}",
                                style = MaterialTheme.typography.labelSmall,
                                color = ForgeTextMuted
                            )
                        }
                        Text(
                            text = project.name,
                            style = MaterialTheme.typography.headlineMedium,
                            color = ForgeTextPrimary
                        )
                    }
                }
                IconButton(
                    onClick = onCloseInspector,
                    modifier = Modifier.testTag("close_opened_project_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close project inspector",
                        tint = ForgeTextSecondary
                    )
                }
            }

            if (project.description.isNotBlank()) {
                Text(
                    text = project.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ForgeTextSecondary
                )
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetadataPill(icon = Icons.Default.Layers, label = "Genre: ${project.gameType}")
                MetadataPill(icon = Icons.Default.SettingsSuggest, label = "Platform: ${project.targetPlatform}")
                MetadataPill(icon = Icons.Default.Memory, label = "Graphics: ${project.graphicsPreset}")
                MetadataPill(icon = Icons.Default.Layers, label = "Orientation: ${project.orientation}")
                MetadataPill(icon = Icons.Default.Layers, label = "Scenes: ${project.sceneCount} · Assets: ${project.assetCount} · Scripts: ${project.scriptCount}")
            }

            Surface(
                color = ForgeSurface,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, ForgeCardBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Step 01 Foundation Project Descriptor Ready",
                            style = MaterialTheme.typography.titleSmall,
                            color = ForgeBlueLight
                        )
                        Text(
                            text = "This project schema is persisted and ready to export to GitHub or mount into the Step 02 3D Scene Editor, AI World Generator, and Android APK/AAB Build Pipeline.",
                            style = MaterialTheme.typography.bodySmall,
                            color = ForgeTextSecondary
                        )
                    }
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onExportToGitHub(project) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ForgeElectricBlue,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("opened_project_github_export_button")
                        ) {
                            Text("GitHub / Download", style = MaterialTheme.typography.labelMedium, color = Color.White)
                        }
                        OutlinedButton(
                            onClick = { onRename(project) },
                            border = BorderStroke(1.dp, ForgeCardBorder),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Rename", style = MaterialTheme.typography.labelMedium, color = ForgeTextPrimary)
                        }
                        OutlinedButton(
                            onClick = { onDelete(project) },
                            border = BorderStroke(1.dp, ForgeDangerRed.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Delete", style = MaterialTheme.typography.labelMedium, color = ForgeDangerRed)
                        }
                    }
                }
            }
        }
    }
}
