package com.example.types

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

enum class GameType(val label: String, val badgeColorHex: Long) {
    OPEN_WORLD("Open World", 0xFF3B82F6),
    RACING("Racing", 0xFFF59E0B),
    SIMULATION("Simulation", 0xFF10B981),
    ADVENTURE("Adventure", 0xFF8B5CF6),
    SURVIVAL("Survival", 0xFFEF4444),
    CUSTOM_GAME("Custom Game", 0xFF06B6D4);

    companion object {
        fun fromLabel(label: String): GameType =
            entries.find { it.label == label } ?: OPEN_WORLD
    }
}

enum class TargetPlatform(val label: String) {
    WEB("Web"),
    ANDROID("Android"),
    BOTH("Both");

    companion object {
        fun fromLabel(label: String): TargetPlatform =
            entries.find { it.label == label } ?: BOTH
    }
}

enum class GraphicsPreset(val label: String, val detail: String) {
    VERY_LOW("Very Low", "Mobile / Integrated GPU — 30 FPS target"),
    LOW("Low", "Entry Mobile — Basic shadows, baked lighting"),
    MEDIUM("Medium", "Balanced Mobile/Web — Dynamic cascades"),
    HIGH("High", "High-End Android & Desktop — PBR & SSR"),
    ULTRA("Ultra", "Flagship / Desktop — Volumetric GI & LOD0");

    companion object {
        fun fromLabel(label: String): GraphicsPreset =
            entries.find { it.label == label } ?: HIGH
    }
}

enum class ViewportOrientation(val label: String) {
    LANDSCAPE("Landscape"),
    PORTRAIT("Portrait");

    companion object {
        fun fromLabel(label: String): ViewportOrientation =
            entries.find { it.label == label } ?: LANDSCAPE
    }
}

enum class ForgeNavDestination(
    val route: String,
    val title: String,
    val subtitle: String,
    val isCoreImplemented: Boolean
) {
    DASHBOARD("dashboard", "Dashboard", "Engine Command Center & Overview", true),
    APK_DOWNLOAD_INSTALL("apk_download_install", "APK Download & Install", "Direct Engine APK Download & Installer Page", true),
    MY_PROJECTS("my_projects", "My Projects", "Manage & Inspect Game Projects", true),
    CREATE_NEW_GAME("create_new_game", "Create New Game", "Initialize New World Forge Project", true),
    AI_WORLD_GENERATOR("ai_world_generator", "AI World Generator", "Procedural Biome & Terrain Synthesis", false),
    IMPORT_3D_WORLD("import_3d_world", "Import 3D World", "GLTF / FBX / OBJ Scene Ingestion & AI Scan", false),
    AI_GAME_DEVELOPER("ai_game_developer", "AI Game Developer", "Visual Scripting & AI Code Co-Pilot", false),
    ASSET_LIBRARY("asset_library", "Asset Library", "PBR Materials, Characters, NPCs & Vehicles", false),
    ENGINE_SETTINGS("engine_settings", "Engine Settings", "Render Pipeline, Physics & APK/AAB Export", true)
}

/**
 * Modular Project data model persisted to Cloud Firestore (`/users/{userId}/projects/{projectId}`).
 * Designed to scale seamlessly in future stages with scenes, assets, scripts, and build targets.
 */
@IgnoreExtraProperties
data class ForgeProject(
    @DocumentId val id: String = "",
    val userId: String = "",
    val name: String = "",
    val description: String = "",
    val gameType: String = GameType.OPEN_WORLD.label,
    val targetPlatform: String = TargetPlatform.BOTH.label,
    val graphicsPreset: String = GraphicsPreset.HIGH.label,
    val orientation: String = ViewportOrientation.LANDSCAPE.label,
    val engineVersion: String = "0.1.0-step01",
    val sceneCount: Long = 1L,
    val assetCount: Long = 0L,
    val scriptCount: Long = 0L,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

data class ProjectDraft(
    val name: String = "",
    val description: String = "",
    val gameType: GameType = GameType.OPEN_WORLD,
    val targetPlatform: TargetPlatform = TargetPlatform.BOTH,
    val graphicsPreset: GraphicsPreset = GraphicsPreset.HIGH,
    val orientation: ViewportOrientation = ViewportOrientation.LANDSCAPE
)
