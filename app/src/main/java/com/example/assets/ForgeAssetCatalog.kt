package com.example.assets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.R
import com.example.types.ForgeNavDestination
import com.example.types.GameType

object ForgeAssetCatalog {
    val HeroBannerRes: Int = R.drawable.img_hero_forge_banner_1794368206502
    val AppIconFgRes: Int = R.drawable.img_app_icon_fg_1794368203773

    fun iconForDestination(destination: ForgeNavDestination): ImageVector = when (destination) {
        ForgeNavDestination.DASHBOARD -> Icons.Default.Dashboard
        ForgeNavDestination.APK_DOWNLOAD_INSTALL -> Icons.Default.Android
        ForgeNavDestination.MY_PROJECTS -> Icons.Default.FolderOpen
        ForgeNavDestination.CREATE_NEW_GAME -> Icons.Default.AddBox
        ForgeNavDestination.AI_WORLD_GENERATOR -> Icons.Default.Public
        ForgeNavDestination.IMPORT_3D_WORLD -> Icons.Default.ViewInAr
        ForgeNavDestination.AI_GAME_DEVELOPER -> Icons.Default.Code
        ForgeNavDestination.ASSET_LIBRARY -> Icons.Default.AccountTree
        ForgeNavDestination.ENGINE_SETTINGS -> Icons.Default.Settings
    }

    fun iconForGameType(gameTypeLabel: String): ImageVector = when (GameType.fromLabel(gameTypeLabel)) {
        GameType.OPEN_WORLD -> Icons.Default.Public
        GameType.RACING -> Icons.Default.Speed
        GameType.SIMULATION -> Icons.Default.Build
        GameType.ADVENTURE -> Icons.Default.Terrain
        GameType.SURVIVAL -> Icons.Default.AutoAwesome
        GameType.CUSTOM_GAME -> Icons.Default.SportsEsports
    }
}
