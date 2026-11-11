package com.example.engine

/**
 * Architectural Registry for WORLD FORGE AI Engine Subsystems.
 * Keeps future Three.js WebGL/Vulkan rendering, Physics, AI World Generation, and Android APK/AAB
 * build pipelines cleanly decoupled from the Step 01 Main Dashboard UI.
 * No fake or simulated progress bars are used — each module reports its true Step 01 readiness state.
 */
enum class SubsystemStage(val badgeText: String, val isReadyInStep01: Boolean) {
    ACTIVE_STEP_01("ACTIVE · STEP 01", true),
    PLANNED_STEP_02("STEP 02 MODULE", false),
    PLANNED_STEP_03("STEP 03 MODULE", false),
    PLANNED_STEP_04("STEP 04 MODULE", false),
    PLANNED_STEP_05("STEP 05 MODULE", false)
}

data class EngineSubsystemSpec(
    val id: String,
    val name: String,
    val category: String,
    val stage: SubsystemStage,
    val summary: String,
    val technicalSpecs: List<String>
)

object EngineArchitectureRegistry {
    const val PLATFORM_NAME = "WORLD FORGE AI"
    const val ENGINE_VERSION = "0.1.0-step01 (Foundation Build)"
    const val SCHEMA_VERSION = "wf-project-schema-v1"

    val subsystems: List<EngineSubsystemSpec> = listOf(
        EngineSubsystemSpec(
            id = "core_dashboard_pm",
            name = "Foundation & Project Manager",
            category = "Core Platform",
            stage = SubsystemStage.ACTIVE_STEP_01,
            summary = "Cloud-synced project workspace, metadata schema, graphics presets, and multi-platform configuration.",
            technicalSpecs = listOf(
                "Persistent Cloud Firestore Project Store",
                "Real-time Snapshot Synchronization",
                "Scene / Asset / Script Counter Descriptors",
                "Responsive Mobile & Desktop Workspace Layout"
            )
        ),
        EngineSubsystemSpec(
            id = "three_scene_renderer",
            name = "3D Viewport & Scene Graph Engine",
            category = "Rendering Pipeline",
            stage = SubsystemStage.PLANNED_STEP_02,
            summary = "Hardware-accelerated 3D scene graph, PBR shader pipeline, dynamic LODs, and camera controller.",
            technicalSpecs = listOf(
                "Three.js / WebGL2 & Native Vulkan Bridge",
                "Cascaded Shadow Maps & Sky Atmosphere",
                "5-Tier Graphics Preset Scaler (Very Low to Ultra)",
                "Octree Spatial Culling for Open Worlds"
            )
        ),
        EngineSubsystemSpec(
            id = "ai_world_generator",
            name = "AI Open World & Biome Synthesizer",
            category = "AI Generation",
            stage = SubsystemStage.PLANNED_STEP_03,
            summary = "Procedural heightmap synthesis, erosion simulation, road networks, and biome foliage scattering.",
            technicalSpecs = listOf(
                "Multi-Biome Heightfield Synthesis",
                "Spline River & Road Network Carving",
                "GPU Instanced Vegetation & Rock Scattering",
                "Non-Destructive Layer Masks"
            )
        ),
        EngineSubsystemSpec(
            id = "world_scanner_importer",
            name = "3D World Importer & AI Auto-Scanner",
            category = "Asset Pipeline",
            stage = SubsystemStage.PLANNED_STEP_03,
            summary = "GLTF/GLB/FBX/OBJ world ingestion with automatic semantic mesh segmentation and collider fitting.",
            technicalSpecs = listOf(
                "GLTF 2.0 / Draco / KTX2 Texture Streaming",
                "Automatic AI Topology & Bounds Analysis",
                "Auto-Collider Generation (Convex Hull / BVH)",
                "Material Slot Remapping"
            )
        ),
        EngineSubsystemSpec(
            id = "physics_character_vehicle",
            name = "Automatic Physics, NPC & Vehicle Systems",
            category = "Gameplay Systems",
            stage = SubsystemStage.PLANNED_STEP_04,
            summary = "Rigid-body dynamics, raycast vehicle suspension, inverse kinematics, and NavMesh crowd AI.",
            technicalSpecs = listOf(
                "Deterministic Rigid-Body & Trigger Volumes",
                "4-Wheel Raycast & Pacejka Tire Vehicle Physics",
                "NavMesh Pathfinding & Behavior Trees",
                "Modular Character Controller State Machine"
            )
        ),
        EngineSubsystemSpec(
            id = "visual_scripting_apk_export",
            name = "Visual Scripting, AI Co-Pilot & APK/AAB Export",
            category = "Scripting & Build",
            stage = SubsystemStage.PLANNED_STEP_05,
            summary = "Node-based event graphs, AI script synthesis, live error diagnostics, and Android APK/AAB packager.",
            technicalSpecs = listOf(
                "Node Graph Event & State Machine Compiler",
                "AI Code Assistant & Runtime Error Profiler",
                "One-Click Web & Android APK/AAB Packaging",
                "Keystore Signing & Bundle Optimization"
            )
        )
    )
}
