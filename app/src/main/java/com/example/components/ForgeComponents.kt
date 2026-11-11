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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Launch
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FolderOff
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.assets.ForgeAssetCatalog
import com.example.styles.ForgeStyles
import com.example.types.ForgeProject
import com.example.types.GameType
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
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

fun formatProjectTimestamp(timestamp: Timestamp?): String {
    if (timestamp == null) return "Just now"
    val date = timestamp.toDate()
    val formatter = SimpleDateFormat("MMM d, yyyy · HH:mm", Locale.US)
    return formatter.format(date)
}

@Composable
fun ForgeBrandHeaderBadge(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(ForgeStyles.PrimaryGradientBrush),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "WF",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "WORLD FORGE AI",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ForgeTextPrimary
                )
                Surface(
                    color = ForgeElectricBlue.copy(alpha = 0.16f),
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, ForgeElectricBlue.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "STEP 01",
                        style = MaterialTheme.typography.labelSmall,
                        color = ForgeBlueLight,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Text(
                text = "Open World Game Engine Foundation",
                style = MaterialTheme.typography.bodySmall,
                color = ForgeTextSecondary
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ForgeProjectCard(
    project: ForgeProject,
    onOpenProject: (ForgeProject) -> Unit,
    onRenameProject: (ForgeProject) -> Unit,
    onDeleteProject: (ForgeProject) -> Unit,
    onExportToGitHub: (ForgeProject) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val gameTypeEnum = GameType.fromLabel(project.gameType)
    val badgeColor = Color(gameTypeEnum.badgeColorHex)
    val lastModifiedStr = formatProjectTimestamp(project.updatedAt ?: project.createdAt)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("project_card_${project.id}")
            .clickable { onOpenProject(project) },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = ForgeSurface),
        border = BorderStroke(1.dp, ForgeCardBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(badgeColor.copy(alpha = 0.15f))
                            .border(1.dp, badgeColor.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = ForgeAssetCatalog.iconForGameType(project.gameType),
                            contentDescription = project.gameType,
                            tint = badgeColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = project.name,
                            style = MaterialTheme.typography.titleLarge,
                            color = ForgeTextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Surface(
                                color = badgeColor.copy(alpha = 0.16f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = project.gameType.uppercase(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = badgeColor,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Text(
                                text = "v${project.engineVersion}",
                                style = MaterialTheme.typography.labelSmall,
                                color = ForgeTextMuted
                            )
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    IconButton(
                        onClick = { onExportToGitHub(project) },
                        modifier = Modifier.testTag("export_github_button_${project.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Download or Export ${project.name} to GitHub",
                            tint = ForgeBlueLight,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(
                        onClick = { onRenameProject(project) },
                        modifier = Modifier.testTag("rename_project_button_${project.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Rename Project ${project.name}",
                            tint = ForgeTextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(
                        onClick = { onDeleteProject(project) },
                        modifier = Modifier.testTag("delete_project_button_${project.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete Project ${project.name}",
                            tint = ForgeDangerRed.copy(alpha = 0.85f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            if (project.description.isNotBlank()) {
                Text(
                    text = project.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ForgeTextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            } else {
                Text(
                    text = "No project description provided.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ForgeTextMuted
                )
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                MetadataPill(
                    icon = Icons.Default.Devices,
                    label = "Target: ${project.targetPlatform}"
                )
                MetadataPill(
                    icon = Icons.Default.HighQuality,
                    label = "Preset: ${project.graphicsPreset}"
                )
                MetadataPill(
                    icon = Icons.Default.ScreenRotation,
                    label = project.orientation
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = ForgeTextMuted,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Modified: $lastModifiedStr",
                        style = MaterialTheme.typography.labelSmall,
                        color = ForgeTextMuted
                    )
                }

                Button(
                    onClick = { onOpenProject(project) },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ForgeElectricBlue,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.testTag("open_project_button_${project.id}")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Launch,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Open Project", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Composable
fun MetadataPill(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = ForgeSurfaceElevated,
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, ForgeCardBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = ForgeBlueLight,
                modifier = Modifier.size(13.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = ForgeTextSecondary
            )
        }
    }
}

@Composable
fun EmptyProjectsStateCard(
    onCreateNewProject: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("empty_projects_state_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ForgeSurface),
        border = BorderStroke(1.dp, ForgeCardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(ForgeElectricBlue.copy(alpha = 0.12f))
                    .border(1.dp, ForgeElectricBlue.copy(alpha = 0.35f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.FolderOff,
                    contentDescription = "No projects yet",
                    tint = ForgeBlueLight,
                    modifier = Modifier.size(30.dp)
                )
            }
            Text(
                text = "No Game Projects Yet",
                style = MaterialTheme.typography.headlineMedium,
                color = ForgeTextPrimary
            )
            Text(
                text = "Initialize your first WORLD FORGE AI project. Configure your game genre, target platform (Web, Android, or Both), rendering preset, and viewport orientation.",
                style = MaterialTheme.typography.bodyMedium,
                color = ForgeTextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Button(
                onClick = onCreateNewProject,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ForgeElectricBlue,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("empty_state_create_project_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Create New Project", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
fun QuickAccessToolCard(
    title: String,
    subtitle: String,
    badgeText: String,
    isReadyInStep01: Boolean,
    icon: ImageVector,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    val accentColor = if (isReadyInStep01) ForgeSuccessGreen else ForgeVioletAccent
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag(testTag)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ForgeSurface),
        border = BorderStroke(1.dp, ForgeCardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(accentColor.copy(alpha = 0.14f))
                    .border(1.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = ForgeTextPrimary
                    )
                    Surface(
                        color = accentColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = badgeText,
                            style = MaterialTheme.typography.labelSmall,
                            color = accentColor,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = ForgeTextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
