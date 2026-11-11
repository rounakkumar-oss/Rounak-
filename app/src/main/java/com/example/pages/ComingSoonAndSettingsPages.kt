package com.example.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Architecture
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.assets.ForgeAssetCatalog
import com.example.engine.EngineArchitectureRegistry
import com.example.types.ForgeNavDestination
import com.example.types.GraphicsPreset
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

/**
 * Transparent Coming-Soon Module Specification Page for future engine stages:
 * - AI World Generator
 * - Import 3D World
 * - AI Game Developer
 * - Asset Library
 *
 * Strictly obeys Rule 4, 9, and 11: No fake progress bars, no fake 3D editors, no non-functional buttons.
 */
@Composable
fun ComingSoonModulePage(
    destination: ForgeNavDestination,
    onReturnToDashboard: () -> Unit,
    onCreateNewProject: () -> Unit,
    modifier: Modifier = Modifier
) {
    val moduleSpecs = when (destination) {
        ForgeNavDestination.AI_WORLD_GENERATOR -> ModuleSpecDetails(
            stageBadge = "PLANNED FOR STEP 03 · AI WORLD GENERATION",
            headline = "AI Open World & Biome Generator",
            overview = "This module will synthesize playable 3D open-world terrains, multi-biome ecosystems, hydrologically eroded valleys, road splines, and foliage scatter layers directly from structured world parameters.",
            plannedCapabilities = listOf(
                "Procedural Heightfield & Hydraulic Erosion Pipeline (1km² to 64km² streaming tiles)",
                "Biome Mask Synthesis: Alpine, Desert Canyon, Temperate Forest, Cyber-Metropolis, Archipelago",
                "Automated Spline Road & River Network Carving with Terrain Flattening",
                "Octree-Culled GPU Instanced Foliage & Rock Scattering",
                "Direct Binding to Active WORLD FORGE AI Project Scene Graph"
            ),
            interfaceContract = "com.example.engine.worldgen.WorldGeneratorService (Reserved Module Boundary)"
        )
        ForgeNavDestination.IMPORT_3D_WORLD -> ModuleSpecDetails(
            stageBadge = "PLANNED FOR STEP 03 · 3D ASSET & WORLD INGESTION",
            headline = "Import 3D World & Automatic AI World Scanner",
            overview = "This module will ingest existing 3D worlds (.GLTF, .GLB, .FBX, .OBJ) and execute an automatic AI spatial pass to classify static geometry, generate optimized physics colliders, and configure navigation meshes.",
            plannedCapabilities = listOf(
                "GLTF 2.0 / GLB / Draco & KTX2 Compressed Mesh Ingestion Pipeline",
                "Automatic AI World Scanning: Semantic classification of roads, buildings, terrain, and props",
                "Auto-Collider Generation: Convex Hull, Box/Capsule primitives, and static BVH triangle meshes",
                "Automated NavMesh Walkable Surface Baking for Character & NPC Agents",
                "LOD Chain Verification & Draw-Call Batching Diagnostics"
            ),
            interfaceContract = "com.example.engine.importer.WorldImportScannerService (Reserved Module Boundary)"
        )
        ForgeNavDestination.AI_GAME_DEVELOPER -> ModuleSpecDetails(
            stageBadge = "PLANNED FOR STEP 04–05 · VISUAL SCRIPTING & AI CO-PILOT",
            headline = "AI Game Developer, Visual Scripting & Error Detection",
            overview = "This module will provide no-code node-graph visual scripting alongside an AI Coding Assistant, Character/NPC/Vehicle behavior controllers, and real-time game testing & error detection.",
            plannedCapabilities = listOf(
                "No-Code Node Graph Visual Scripting for Triggers, Quests, UI, and Physics Events",
                "Character, NPC Crowd AI, and Raycast Vehicle Controller Configuration",
                "AI Coding Assistant for custom gameplay behaviours and shader parameters",
                "Integrated Game Testing Sandbox with Live Runtime Error & Frame-Time Detection",
                "One-Click Android APK / AAB & WebGL Export Pipeline Integration"
            ),
            interfaceContract = "com.example.engine.scripting.AiGameDeveloperService (Reserved Module Boundary)"
        )
        ForgeNavDestination.ASSET_LIBRARY -> ModuleSpecDetails(
            stageBadge = "PLANNED FOR STEP 04 · MODULAR ENGINE ASSETS",
            headline = "WORLD FORGE AI Asset Library",
            overview = "This module will manage reusable 3D characters, rigged NPCs, drivable vehicles, PBR material libraries, modular buildings, and audio banks linked to your projects.",
            plannedCapabilities = listOf(
                "Rigged Character & NPC Archetypes with Locomotion State Machines",
                "Drivable Vehicle Prefabs with Tunable Suspension, Torque Curves & Tire Friction",
                "PBR Material Library (Albedo, Normal, Roughness, Metallic, Ambient Occlusion)",
                "Modular Open-World Kitbash Props, Foliage, and Lighting Rigs",
                "Per-Project Asset Dependency Tracking & Memory Footprint Estimator"
            ),
            interfaceContract = "com.example.assets.AssetRepositoryService (Reserved Module Boundary)"
        )
        else -> ModuleSpecDetails(
            stageBadge = "UPCOMING ENGINE MODULE",
            headline = destination.title,
            overview = destination.subtitle,
            plannedCapabilities = emptyList(),
            interfaceContract = "com.example.engine"
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("coming_soon_page_${destination.route}"),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = ForgeSurface),
            border = BorderStroke(1.dp, ForgeCardBorder)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(ForgeVioletAccent.copy(alpha = 0.16f))
                            .border(1.dp, ForgeVioletAccent.copy(alpha = 0.45f), RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = ForgeAssetCatalog.iconForDestination(destination),
                            contentDescription = destination.title,
                            tint = ForgeVioletLight,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Column {
                        Surface(
                            color = ForgeVioletAccent.copy(alpha = 0.18f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = moduleSpecs.stageBadge,
                                style = MaterialTheme.typography.labelSmall,
                                color = ForgeVioletLight,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = moduleSpecs.headline,
                            style = MaterialTheme.typography.headlineLarge,
                            color = ForgeTextPrimary
                        )
                    }
                }

                Text(
                    text = moduleSpecs.overview,
                    style = MaterialTheme.typography.bodyLarge,
                    color = ForgeTextSecondary
                )

                Surface(
                    color = ForgeSurfaceElevated,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, ForgeCardBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Construction,
                            contentDescription = null,
                            tint = ForgeBlueLight,
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text(
                                text = "Step 01 Foundation Status: Scheduled for Upcoming Stage",
                                style = MaterialTheme.typography.titleSmall,
                                color = ForgeTextPrimary
                            )
                            Text(
                                text = "Per WORLD FORGE AI Step 01 engineering rules, no fake 3D editors or simulated AI progress bars are rendered here. Your projects created in Step 01 already store the schema fields required by this module.",
                                style = MaterialTheme.typography.bodySmall,
                                color = ForgeTextSecondary
                            )
                        }
                    }
                }

                HorizontalDivider(color = ForgeCardBorder)

                Text(
                    text = "Planned Module Architecture & Capabilities",
                    style = MaterialTheme.typography.titleMedium,
                    color = ForgeTextPrimary
                )

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    moduleSpecs.plannedCapabilities.forEach { capability ->
                        Row(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = ForgeBlueLight,
                                modifier = Modifier
                                    .padding(top = 2.dp)
                                    .size(16.dp)
                            )
                            Text(
                                text = capability,
                                style = MaterialTheme.typography.bodyMedium,
                                color = ForgeTextSecondary
                            )
                        }
                    }
                }

                Surface(
                    color = ForgeSurfaceElevated,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, ForgeCardBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Architecture,
                            contentDescription = null,
                            tint = ForgeVioletLight,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Module Contract: ${moduleSpecs.interfaceContract}",
                            style = MaterialTheme.typography.labelMedium,
                            color = ForgeTextMuted
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onReturnToDashboard,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, ForgeCardBorder),
                        modifier = Modifier.testTag("coming_soon_back_dashboard_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = ForgeTextPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Back to Dashboard", color = ForgeTextPrimary)
                    }

                    Button(
                        onClick = onCreateNewProject,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ForgeElectricBlue,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.testTag("coming_soon_create_project_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Create Project Now", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

private data class ModuleSpecDetails(
    val stageBadge: String,
    val headline: String,
    val overview: String,
    val plannedCapabilities: List<String>,
    val interfaceContract: String
)

/**
 * Engine Settings Page (Navigation item 8) — Displays real Step 01 Engine Architecture,
 * Storage Layer status, Graphics Preset specifications, and Android/Web build target matrix.
 */
@Composable
fun EngineSettingsPage(
    currentUserId: String,
    projectCount: Int,
    onOpenGitHubExportHub: () -> Unit = {},
    onReturnToDashboard: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("engine_settings_page"),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Engine Settings & Architecture",
                    style = MaterialTheme.typography.displayMedium,
                    color = ForgeTextPrimary
                )
                Text(
                    text = "WORLD FORGE AI Core Configuration, Persistence Diagnostics & Graphics Preset Matrix",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ForgeTextSecondary
                )
            }
            OutlinedButton(
                onClick = onReturnToDashboard,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, ForgeCardBorder)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = ForgeTextPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Dashboard", color = ForgeTextPrimary)
            }
        }

        // 1. Core Engine Runtime Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = ForgeSurface),
            border = BorderStroke(1.dp, ForgeCardBorder)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Storage,
                        contentDescription = null,
                        tint = ForgeSuccessGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Workspace & Persistence Engine Status",
                        style = MaterialTheme.typography.titleLarge,
                        color = ForgeTextPrimary
                    )
                }

                SettingRowItem("Platform Build", EngineArchitectureRegistry.ENGINE_VERSION)
                SettingRowItem("Project Data Schema", EngineArchitectureRegistry.SCHEMA_VERSION)
                SettingRowItem("Persistence Layer", "Cloud Firestore Enterprise + Offline Cache")
                SettingRowItem("Authenticated Developer UID", currentUserId.take(16) + "...")
                SettingRowItem("Active Workspace Projects", "$projectCount project(s)")

                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = onOpenGitHubExportHub,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ForgeElectricBlue,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("settings_open_github_hub_button")
                ) {
                    Text(
                        text = "Open GitHub Export & Direct Download Hub",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // 2. Graphics Presets Specification Matrix
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = ForgeSurface),
            border = BorderStroke(1.dp, ForgeCardBorder)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Memory,
                        contentDescription = null,
                        tint = ForgeBlueLight,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Graphics Presets Specification Matrix",
                        style = MaterialTheme.typography.titleLarge,
                        color = ForgeTextPrimary
                    )
                }

                GraphicsPreset.entries.forEach { preset ->
                    Surface(
                        color = ForgeSurfaceElevated,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, ForgeCardBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = preset.label,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = ForgeTextPrimary
                                )
                                Text(
                                    text = preset.detail,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ForgeTextSecondary
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.CheckCircleOutline,
                                contentDescription = null,
                                tint = ForgeBlueLight,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // 3. Modular Directory Architecture Overview
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = ForgeSurface),
            border = BorderStroke(1.dp, ForgeCardBorder)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Layers,
                        contentDescription = null,
                        tint = ForgeVioletLight,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Modular Package Architecture (Step 01 Foundation)",
                        style = MaterialTheme.typography.titleLarge,
                        color = ForgeTextPrimary
                    )
                }
                Text(
                    text = "Decoupled module layout ready for future 3D rendering, physics, AI services, and APK/AAB build pipelines:",
                    style = MaterialTheme.typography.bodySmall,
                    color = ForgeTextSecondary
                )
                SettingRowItem("components/", "Reusable UI cards, dialogs, metadata badges")
                SettingRowItem("pages/", "Dashboard, My Projects, Module Specs, Engine Settings")
                SettingRowItem("layouts/", "Adaptive responsive shell (Drawer + Rail + TopBar)")
                SettingRowItem("projectmanager/", "Reactive ViewModel & workspace state orchestration")
                SettingRowItem("engine/", "Subsystem registry & future 3D/Physics boundaries")
                SettingRowItem("services/", "Credential Manager Google Auth & session services")
                SettingRowItem("storage/", "Firestore repository & structured JSON error handler")
                SettingRowItem("types/", "Project models, enums, and navigation routes")
                SettingRowItem("assets/ & styles/", "Visual asset catalog, gradients & CAD grid shaders")
            }
        }
    }
}

@Composable
private fun SettingRowItem(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = ForgeTextSecondary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelMedium,
            color = ForgeTextPrimary
        )
    }
}
