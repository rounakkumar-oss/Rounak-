package androidx.compose.material.icons.filled

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

private fun simpleIcon(name: String, block: androidx.compose.ui.graphics.vector.PathBuilder.() -> Unit): ImageVector {
    return materialIcon(name = name) {
        materialPath(pathBuilder = block)
    }
}

val Icons.Filled.AccessTime: ImageVector by lazy {
    simpleIcon("Filled.AccessTime") {
        moveTo(11.99f, 2.0f)
        curveTo(6.47f, 2.0f, 2.0f, 6.48f, 2.0f, 12.0f)
        reflectiveCurveToRelative(4.47f, 10.0f, 9.99f, 10.0f)
        curveTo(17.52f, 22.0f, 22.0f, 17.52f, 22.0f, 12.0f)
        reflectiveCurveTo(17.52f, 2.0f, 11.99f, 2.0f)
        close()
        moveTo(12.5f, 7.0f)
        verticalLineToRelative(5.25f)
        lineToRelative(4.5f, 2.67f)
        lineToRelative(-0.75f, 1.23f)
        lineTo(11.0f, 13.0f)
        verticalLineTo(7.0f)
        horizontalLineToRelative(1.5f)
        close()
    }
}

val Icons.Filled.Schedule: ImageVector get() = Icons.Filled.AccessTime

val Icons.Filled.AccountTree: ImageVector by lazy {
    simpleIcon("Filled.AccountTree") {
        moveTo(22.0f, 11.0f)
        verticalLineTo(3.0f)
        horizontalLineToRelative(-7.0f)
        verticalLineToRelative(3.0f)
        horizontalLineTo(9.0f)
        verticalLineTo(3.0f)
        horizontalLineTo(2.0f)
        verticalLineToRelative(8.0f)
        horizontalLineToRelative(7.0f)
        verticalLineTo(8.0f)
        horizontalLineToRelative(2.0f)
        verticalLineToRelative(10.0f)
        horizontalLineToRelative(4.0f)
        verticalLineToRelative(3.0f)
        horizontalLineToRelative(7.0f)
        verticalLineToRelative(-8.0f)
        horizontalLineToRelative(-7.0f)
        verticalLineToRelative(3.0f)
        horizontalLineToRelative(-2.0f)
        verticalLineTo(8.0f)
        horizontalLineToRelative(2.0f)
        verticalLineToRelative(3.0f)
        horizontalLineToRelative(7.0f)
        close()
    }
}

val Icons.Filled.AddBox: ImageVector by lazy {
    simpleIcon("Filled.AddBox") {
        moveTo(19.0f, 3.0f)
        horizontalLineTo(5.0f)
        curveToRelative(-1.11f, 0.0f, -2.0f, 0.9f, -2.0f, 2.0f)
        verticalLineToRelative(14.0f)
        curveToRelative(0.0f, 1.1f, 0.89f, 2.0f, 2.0f, 2.0f)
        horizontalLineToRelative(14.0f)
        curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
        verticalLineTo(5.0f)
        curveToRelative(0.0f, -1.1f, -0.9f, -2.0f, -2.0f, -2.0f)
        close()
        moveTo(17.0f, 13.0f)
        horizontalLineToRelative(-4.0f)
        verticalLineToRelative(4.0f)
        horizontalLineToRelative(-2.0f)
        verticalLineToRelative(-4.0f)
        horizontalLineTo(7.0f)
        verticalLineToRelative(-2.0f)
        horizontalLineToRelative(4.0f)
        verticalLineTo(7.0f)
        horizontalLineToRelative(2.0f)
        verticalLineToRelative(4.0f)
        horizontalLineToRelative(4.0f)
        verticalLineToRelative(2.0f)
        close()
    }
}

val Icons.Filled.AddCircleOutline: ImageVector get() = Icons.Filled.AddBox

val Icons.Filled.Android: ImageVector by lazy {
    simpleIcon("Filled.Android") {
        moveTo(6.0f, 18.0f)
        curveToRelative(0.0f, 0.55f, 0.45f, 1.0f, 1.0f, 1.0f)
        horizontalLineToRelative(1.0f)
        verticalLineToRelative(3.5f)
        curveToRelative(0.0f, 0.83f, 0.67f, 1.5f, 1.5f, 1.5f)
        reflectiveCurveToRelative(1.5f, -0.67f, 1.5f, -1.5f)
        verticalLineTo(19.0f)
        horizontalLineToRelative(2.0f)
        verticalLineToRelative(3.5f)
        curveToRelative(0.0f, 0.83f, 0.67f, 1.5f, 1.5f, 1.5f)
        reflectiveCurveToRelative(1.5f, -0.67f, 1.5f, -1.5f)
        verticalLineTo(19.0f)
        horizontalLineToRelative(1.0f)
        curveToRelative(0.55f, 0.0f, 1.0f, -0.45f, 1.0f, -1.0f)
        verticalLineTo(8.0f)
        horizontalLineTo(6.0f)
        verticalLineToRelative(10.0f)
        close()
        moveTo(3.5f, 8.0f)
        curveTo(2.67f, 8.0f, 2.0f, 8.67f, 2.0f, 9.5f)
        verticalLineToRelative(7.0f)
        curveToRelative(0.0f, 0.83f, 0.67f, 1.5f, 1.5f, 1.5f)
        reflectiveCurveTo(5.0f, 17.33f, 5.0f, 16.5f)
        verticalLineToRelative(-7.0f)
        curveTo(5.0f, 8.67f, 4.33f, 8.0f, 3.5f, 8.0f)
        close()
        moveTo(20.5f, 8.0f)
        curveToRelative(-0.83f, 0.0f, -1.5f, 0.67f, -1.5f, 1.5f)
        verticalLineToRelative(7.0f)
        curveToRelative(0.0f, 0.83f, 0.67f, 1.5f, 1.5f, 1.5f)
        reflectiveCurveToRelative(1.5f, -0.67f, 1.5f, -1.5f)
        verticalLineToRelative(-7.0f)
        curveToRelative(0.0f, -0.83f, -0.67f, -1.5f, -1.5f, -1.5f)
        close()
        moveTo(15.53f, 2.16f)
        lineToRelative(1.3f, -1.3f)
        curveToRelative(0.2f, -0.2f, 0.2f, -0.51f, 0.0f, -0.71f)
        curveToRelative(-0.2f, -0.2f, -0.51f, -0.2f, -0.71f, 0.0f)
        lineToRelative(-1.48f, 1.48f)
        curveTo(13.85f, 1.23f, 12.95f, 1.0f, 12.0f, 1.0f)
        curveToRelative(-0.96f, 0.0f, -1.86f, 0.23f, -2.66f, 0.63f)
        lineTo(7.85f, 0.15f)
        curveToRelative(-0.2f, -0.2f, -0.51f, -0.2f, -0.71f, 0.0f)
        curveToRelative(-0.2f, 0.2f, -0.2f, 0.51f, 0.0f, 0.71f)
        lineToRelative(1.31f, 1.31f)
        curveTo(6.97f, 3.26f, 6.0f, 5.01f, 6.0f, 7.0f)
        horizontalLineToRelative(12.0f)
        curveToRelative(0.0f, -1.99f, -0.97f, -3.75f, -2.47f, -4.84f)
        close()
    }
}

val Icons.Filled.Architecture: ImageVector by lazy {
    simpleIcon("Filled.Architecture") {
        moveTo(6.36f, 18.78f)
        lineTo(6.61f, 21.0f)
        lineToRelative(1.62f, -1.54f)
        lineToRelative(2.77f, -7.6f)
        lineToRelative(-1.88f, -0.68f)
        lineTo(6.36f, 18.78f)
        close()
        moveTo(14.77f, 10.88f)
        lineToRelative(-1.88f, 0.68f)
        lineToRelative(2.77f, 7.6f)
        lineTo(17.39f, 21.0f)
        lineToRelative(0.26f, -2.22f)
        lineToRelative(-2.88f, -7.9f)
        close()
        moveTo(15.0f, 8.0f)
        curveToRelative(0.0f, -1.3f, -0.84f, -2.4f, -2.0f, -2.82f)
        verticalLineTo(3.0f)
        horizontalLineToRelative(-2.0f)
        verticalLineToRelative(2.18f)
        curveTo(9.84f, 5.6f, 9.0f, 6.7f, 9.0f, 8.0f)
        curveToRelative(0.0f, 1.66f, 1.34f, 3.0f, 3.0f, 3.0f)
        reflectiveCurveTo(15.0f, 9.66f, 15.0f, 8.0f)
        close()
    }
}

val Icons.Filled.AutoAwesome: ImageVector by lazy {
    simpleIcon("Filled.AutoAwesome") {
        moveTo(19.0f, 9.0f)
        lineToRelative(1.25f, -2.75f)
        lineTo(23.0f, 5.0f)
        lineToRelative(-2.75f, -1.25f)
        lineTo(19.0f, 1.0f)
        lineToRelative(-1.25f, 2.75f)
        lineTo(15.0f, 5.0f)
        lineToRelative(2.75f, 1.25f)
        lineTo(19.0f, 9.0f)
        close()
        moveTo(11.5f, 9.5f)
        lineTo(9.0f, 4.0f)
        lineTo(6.5f, 9.5f)
        lineTo(1.0f, 12.0f)
        lineToRelative(5.5f, 2.5f)
        lineTo(9.0f, 20.0f)
        lineToRelative(2.5f, -5.5f)
        lineTo(17.0f, 12.0f)
        lineToRelative(-5.5f, -2.5f)
        close()
    }
}

val Icons.Filled.CheckCircleOutline: ImageVector get() = Icons.Filled.CheckCircle

val Icons.Filled.CloudUpload: ImageVector by lazy {
    simpleIcon("Filled.CloudUpload") {
        moveTo(19.35f, 10.04f)
        curveTo(18.67f, 6.59f, 15.64f, 4.0f, 12.0f, 4.0f)
        curveTo(9.11f, 4.0f, 6.6f, 5.64f, 5.35f, 8.04f)
        curveTo(2.34f, 8.36f, 0.0f, 10.91f, 0.0f, 14.0f)
        curveToRelative(0.0f, 3.31f, 2.69f, 6.0f, 6.0f, 6.0f)
        horizontalLineToRelative(13.0f)
        curveToRelative(2.76f, 0.0f, 5.0f, -2.24f, 5.0f, -5.0f)
        curveToRelative(0.0f, -2.64f, -2.05f, -4.78f, -4.65f, -4.96f)
        close()
        moveTo(14.0f, 13.0f)
        verticalLineToRelative(4.0f)
        horizontalLineToRelative(-4.0f)
        verticalLineToRelative(-4.0f)
        horizontalLineTo(7.0f)
        lineToRelative(5.0f, -5.0f)
        lineToRelative(5.0f, 5.0f)
        horizontalLineToRelative(-3.0f)
        close()
    }
}

val Icons.Filled.CloudDone: ImageVector get() = Icons.Filled.CloudUpload

val Icons.Filled.Code: ImageVector by lazy {
    simpleIcon("Filled.Code") {
        moveTo(9.4f, 16.6f)
        lineTo(4.8f, 12.0f)
        lineToRelative(4.6f, -4.6f)
        lineTo(8.0f, 6.0f)
        lineToRelative(-6.0f, 6.0f)
        lineToRelative(6.0f, 6.0f)
        lineToRelative(1.4f, -1.4f)
        close()
        moveTo(14.6f, 16.6f)
        lineToRelative(4.6f, -4.6f)
        lineToRelative(-4.6f, -4.6f)
        lineTo(16.0f, 6.0f)
        lineToRelative(6.0f, 6.0f)
        lineToRelative(-6.0f, 6.0f)
        lineToRelative(-1.4f, -1.4f)
        close()
    }
}

val Icons.Filled.Construction: ImageVector get() = Icons.Filled.Build

val Icons.Filled.ContentCopy: ImageVector by lazy {
    simpleIcon("Filled.ContentCopy") {
        moveTo(16.0f, 1.0f)
        horizontalLineTo(4.0f)
        curveToRelative(-1.1f, 0.0f, -2.0f, 0.9f, -2.0f, 2.0f)
        verticalLineToRelative(14.0f)
        horizontalLineToRelative(2.0f)
        verticalLineTo(3.0f)
        horizontalLineToRelative(12.0f)
        verticalLineTo(1.0f)
        close()
        moveTo(19.0f, 5.0f)
        horizontalLineTo(8.0f)
        curveToRelative(-1.1f, 0.0f, -2.0f, 0.9f, -2.0f, 2.0f)
        verticalLineToRelative(14.0f)
        curveToRelative(0.0f, 1.1f, 0.9f, 2.0f, 2.0f, 2.0f)
        horizontalLineToRelative(11.0f)
        curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
        verticalLineTo(7.0f)
        curveToRelative(0.0f, -1.1f, -0.9f, -2.0f, -2.0f, -2.0f)
        close()
        moveTo(19.0f, 21.0f)
        horizontalLineTo(8.0f)
        verticalLineTo(7.0f)
        horizontalLineToRelative(11.0f)
        verticalLineToRelative(14.0f)
        close()
    }
}

val Icons.Filled.Dashboard: ImageVector by lazy {
    simpleIcon("Filled.Dashboard") {
        moveTo(3.0f, 13.0f)
        horizontalLineToRelative(8.0f)
        verticalLineTo(3.0f)
        horizontalLineTo(3.0f)
        verticalLineToRelative(10.0f)
        close()
        moveTo(3.0f, 21.0f)
        horizontalLineToRelative(8.0f)
        verticalLineToRelative(-6.0f)
        horizontalLineTo(3.0f)
        verticalLineToRelative(6.0f)
        close()
        moveTo(13.0f, 21.0f)
        horizontalLineToRelative(8.0f)
        verticalLineTo(11.0f)
        horizontalLineToRelative(-8.0f)
        verticalLineToRelative(10.0f)
        close()
        moveTo(13.0f, 3.0f)
        verticalLineToRelative(6.0f)
        horizontalLineToRelative(8.0f)
        verticalLineTo(3.0f)
        horizontalLineToRelative(-8.0f)
        close()
    }
}

val Icons.Filled.DeleteForever: ImageVector get() = Icons.Filled.Delete
val Icons.Filled.DeleteOutline: ImageVector get() = Icons.Filled.Delete

val Icons.Filled.Description: ImageVector by lazy {
    simpleIcon("Filled.Description") {
        moveTo(14.0f, 2.0f)
        horizontalLineTo(6.0f)
        curveToRelative(-1.1f, 0.0f, -1.99f, 0.9f, -1.99f, 2.0f)
        lineTo(4.0f, 20.0f)
        curveToRelative(0.0f, 1.1f, 0.89f, 2.0f, 1.99f, 2.0f)
        horizontalLineTo(18.0f)
        curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
        verticalLineTo(8.0f)
        lineToRelative(-6.0f, -6.0f)
        close()
        moveTo(16.0f, 18.0f)
        horizontalLineTo(8.0f)
        verticalLineToRelative(-2.0f)
        horizontalLineToRelative(8.0f)
        verticalLineToRelative(2.0f)
        close()
        moveTo(16.0f, 14.0f)
        horizontalLineTo(8.0f)
        verticalLineToRelative(-2.0f)
        horizontalLineToRelative(8.0f)
        verticalLineToRelative(2.0f)
        close()
        moveTo(13.0f, 9.0f)
        verticalLineTo(3.5f)
        lineTo(18.5f, 9.0f)
        horizontalLineTo(13.0f)
        close()
    }
}

val Icons.Filled.Devices: ImageVector by lazy {
    simpleIcon("Filled.Devices") {
        moveTo(4.0f, 6.0f)
        horizontalLineToRelative(18.0f)
        verticalLineTo(4.0f)
        horizontalLineTo(4.0f)
        curveToRelative(-1.1f, 0.0f, -2.0f, 0.9f, -2.0f, 2.0f)
        verticalLineToRelative(11.0f)
        horizontalLineTo(0.0f)
        verticalLineToRelative(3.0f)
        horizontalLineToRelative(14.0f)
        verticalLineToRelative(-3.0f)
        horizontalLineTo(4.0f)
        verticalLineTo(6.0f)
        close()
        moveTo(23.0f, 8.0f)
        horizontalLineToRelative(-6.0f)
        curveToRelative(-0.55f, 0.0f, -1.0f, 0.45f, -1.0f, 1.0f)
        verticalLineToRelative(10.0f)
        curveToRelative(0.0f, 0.55f, 0.45f, 1.0f, 1.0f, 1.0f)
        horizontalLineToRelative(6.0f)
        curveToRelative(0.55f, 0.0f, 1.0f, -0.45f, 1.0f, -1.0f)
        verticalLineTo(9.0f)
        curveToRelative(0.0f, -0.55f, -0.45f, -1.0f, -1.0f, -1.0f)
        close()
    }
}

val Icons.Filled.Download: ImageVector by lazy {
    simpleIcon("Filled.Download") {
        moveTo(5.0f, 20.0f)
        horizontalLineToRelative(14.0f)
        verticalLineToRelative(-2.0f)
        horizontalLineTo(5.0f)
        verticalLineToRelative(2.0f)
        close()
        moveTo(19.0f, 9.0f)
        horizontalLineToRelative(-4.0f)
        verticalLineTo(3.0f)
        horizontalLineTo(9.0f)
        verticalLineToRelative(6.0f)
        horizontalLineTo(5.0f)
        lineToRelative(7.0f, 7.0f)
        lineToRelative(7.0f, -7.0f)
        close()
    }
}

val Icons.Filled.FolderOpen: ImageVector by lazy {
    simpleIcon("Filled.FolderOpen") {
        moveTo(20.0f, 6.0f)
        horizontalLineToRelative(-8.0f)
        lineToRelative(-2.0f, -2.0f)
        horizontalLineTo(4.0f)
        curveToRelative(-1.1f, 0.0f, -1.99f, 0.9f, -1.99f, 2.0f)
        lineTo(2.0f, 18.0f)
        curveToRelative(0.0f, 1.1f, 0.9f, 2.0f, 2.0f, 2.0f)
        horizontalLineToRelative(16.0f)
        curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
        verticalLineTo(8.0f)
        curveToRelative(0.0f, -1.1f, -0.9f, -2.0f, -2.0f, -2.0f)
        close()
        moveTo(20.0f, 18.0f)
        horizontalLineTo(4.0f)
        verticalLineTo(8.0f)
        horizontalLineToRelative(16.0f)
        verticalLineToRelative(10.0f)
        close()
    }
}

val Icons.Filled.FolderOff: ImageVector get() = Icons.Filled.FolderOpen
val Icons.Filled.FolderZip: ImageVector get() = Icons.Filled.FolderOpen

val Icons.Filled.HighQuality: ImageVector by lazy {
    simpleIcon("Filled.HighQuality") {
        moveTo(19.0f, 4.0f)
        horizontalLineTo(5.0f)
        curveToRelative(-1.11f, 0.0f, -2.0f, 0.9f, -2.0f, 2.0f)
        verticalLineToRelative(12.0f)
        curveToRelative(0.0f, 1.1f, 0.89f, 2.0f, 2.0f, 2.0f)
        horizontalLineToRelative(14.0f)
        curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
        verticalLineTo(6.0f)
        curveToRelative(0.0f, -1.1f, -0.9f, -2.0f, -2.0f, -2.0f)
        close()
        moveTo(11.0f, 15.0f)
        horizontalLineTo(9.5f)
        verticalLineToRelative(-2.0f)
        horizontalLineToRelative(-2.0f)
        verticalLineToRelative(2.0f)
        horizontalLineTo(6.0f)
        verticalLineTo(9.0f)
        horizontalLineToRelative(1.5f)
        verticalLineToRelative(2.5f)
        horizontalLineToRelative(2.0f)
        verticalLineTo(9.0f)
        horizontalLineTo(11.0f)
        verticalLineToRelative(6.0f)
        close()
    }
}

val Icons.Filled.InstallMobile: ImageVector get() = Icons.Filled.Download

val Icons.Filled.Key: ImageVector by lazy {
    simpleIcon("Filled.Key") {
        moveTo(21.0f, 10.0f)
        horizontalLineToRelative(-8.35f)
        curveTo(11.83f, 7.67f, 9.61f, 6.0f, 7.0f, 6.0f)
        curveToRelative(-3.31f, 0.0f, -6.0f, 2.69f, -6.0f, 6.0f)
        reflectiveCurveToRelative(2.69f, 6.0f, 6.0f, 6.0f)
        curveToRelative(2.61f, 0.0f, 4.83f, -1.67f, 5.65f, -4.0f)
        horizontalLineTo(13.0f)
        lineToRelative(2.0f, 2.0f)
        lineToRelative(2.0f, -2.0f)
        lineToRelative(2.0f, 2.0f)
        lineToRelative(4.0f, -4.04f)
        lineTo(21.0f, 10.0f)
        close()
    }
}

val Icons.Filled.Layers: ImageVector by lazy {
    simpleIcon("Filled.Layers") {
        moveTo(11.99f, 18.54f)
        lineToRelative(-7.37f, -5.73f)
        lineTo(3.0f, 14.07f)
        lineToRelative(9.0f, 7.0f)
        lineToRelative(9.0f, -7.0f)
        lineToRelative(-1.63f, -1.27f)
        lineToRelative(-7.38f, 5.74f)
        close()
        moveTo(12.0f, 16.0f)
        lineToRelative(7.36f, -5.73f)
        lineTo(21.0f, 9.0f)
        lineToRelative(-9.0f, -7.0f)
        lineToRelative(-9.0f, 7.0f)
        lineToRelative(1.63f, 1.27f)
        lineTo(12.0f, 16.0f)
        close()
    }
}

val Icons.Filled.LockOpen: ImageVector get() = Icons.Filled.Lock
val Icons.Filled.Memory: ImageVector get() = Icons.Filled.Settings
val Icons.Filled.Public: ImageVector by lazy {
    simpleIcon("Filled.Public") {
        moveTo(12.0f, 2.0f)
        curveTo(6.48f, 2.0f, 2.0f, 6.48f, 2.0f, 12.0f)
        reflectiveCurveToRelative(4.48f, 10.0f, 10.0f, 10.0f)
        reflectiveCurveToRelative(10.0f, -4.48f, 10.0f, -10.0f)
        reflectiveCurveTo(17.52f, 2.0f, 12.0f, 2.0f)
        close()
    }
}

val Icons.Filled.ScreenRotation: ImageVector get() = Icons.Filled.Devices
val Icons.Filled.Security: ImageVector get() = Icons.Filled.Lock
val Icons.Filled.SettingsSuggest: ImageVector get() = Icons.Filled.Settings
val Icons.Filled.Speed: ImageVector get() = Icons.Filled.AccessTime

val Icons.Filled.SportsEsports: ImageVector by lazy {
    simpleIcon("Filled.SportsEsports") {
        moveTo(21.58f, 16.09f)
        lineToRelative(-1.09f, -7.66f)
        curveTo(20.21f, 6.46f, 18.52f, 5.0f, 16.53f, 5.0f)
        horizontalLineTo(7.47f)
        curveTo(5.48f, 5.0f, 3.79f, 6.46f, 3.51f, 8.43f)
        lineToRelative(-1.09f, 7.66f)
        curveTo(2.2f, 17.63f, 3.39f, 19.0f, 4.94f, 19.0f)
        horizontalLineToRelative(0.0f)
        curveToRelative(0.68f, 0.0f, 1.32f, -0.27f, 1.8f, -0.75f)
        lineTo(9.0f, 16.0f)
        horizontalLineToRelative(6.0f)
        lineToRelative(2.25f, 2.25f)
        curveToRelative(0.48f, 0.48f, 1.13f, 0.75f, 1.8f, 0.75f)
        horizontalLineToRelative(0.0f)
        curveTo(20.61f, 19.0f, 21.8f, 17.63f, 21.58f, 16.09f)
        close()
    }
}

val Icons.Filled.Storage: ImageVector get() = Icons.Filled.Layers
val Icons.Filled.Terrain: ImageVector get() = Icons.Filled.Public
val Icons.Filled.Verified: ImageVector get() = Icons.Filled.CheckCircle
val Icons.Filled.ViewInAr: ImageVector get() = Icons.Filled.Layers
val Icons.Filled.WarningAmber: ImageVector get() = Icons.Filled.Warning
