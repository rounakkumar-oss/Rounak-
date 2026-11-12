package androidx.compose.material.icons.automirrored.filled

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

private var _launch: ImageVector? = null
val Icons.AutoMirrored.Filled.Launch: ImageVector
    get() = _launch ?: materialIcon(name = "AutoMirrored.Filled.Launch", autoMirror = true) {
        materialPath {
            moveTo(19.0f, 19.0f)
            horizontalLineTo(5.0f)
            verticalLineTo(5.0f)
            horizontalLineToRelative(7.0f)
            verticalLineTo(3.0f)
            horizontalLineTo(5.0f)
            curveToRelative(-1.11f, 0.0f, -2.0f, 0.9f, -2.0f, 2.0f)
            verticalLineToRelative(14.0f)
            curveToRelative(0.0f, 1.1f, 0.89f, 2.0f, 2.0f, 2.0f)
            horizontalLineToRelative(14.0f)
            curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
            verticalLineToRelative(-7.0f)
            horizontalLineToRelative(-2.0f)
            verticalLineToRelative(7.0f)
            close()
            moveTo(14.0f, 3.0f)
            verticalLineToRelative(2.0f)
            horizontalLineToRelative(3.59f)
            lineToRelative(-9.83f, 9.83f)
            lineToRelative(1.41f, 1.41f)
            lineTo(19.0f, 6.41f)
            verticalLineTo(10.0f)
            horizontalLineToRelative(2.0f)
            verticalLineTo(3.0f)
            horizontalLineToRelative(-7.0f)
            close()
        }
    }.also { _launch = it }

private var _openInNew: ImageVector? = null
val Icons.AutoMirrored.Filled.OpenInNew: ImageVector
    get() = _openInNew ?: Launch.also { _openInNew = it }

private var _login: ImageVector? = null
val Icons.AutoMirrored.Filled.Login: ImageVector
    get() = _login ?: materialIcon(name = "AutoMirrored.Filled.Login", autoMirror = true) {
        materialPath {
            moveTo(11.0f, 7.0f)
            lineTo(9.6f, 8.4f)
            lineToRelative(2.6f, 2.6f)
            horizontalLineTo(2.0f)
            verticalLineToRelative(2.0f)
            horizontalLineToRelative(10.2f)
            lineToRelative(-2.6f, 2.6f)
            lineTo(11.0f, 17.0f)
            lineToRelative(5.0f, -5.0f)
            lineToRelative(-5.0f, -5.0f)
            close()
            moveTo(20.0f, 19.0f)
            horizontalLineToRelative(-8.0f)
            verticalLineToRelative(2.0f)
            horizontalLineToRelative(8.0f)
            curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
            verticalLineTo(5.0f)
            curveToRelative(0.0f, -1.1f, -0.9f, -2.0f, -2.0f, -2.0f)
            horizontalLineToRelative(-8.0f)
            verticalLineToRelative(2.0f)
            horizontalLineToRelative(8.0f)
            verticalLineToRelative(14.0f)
            close()
        }
    }.also { _login = it }

private var _logout: ImageVector? = null
val Icons.AutoMirrored.Filled.Logout: ImageVector
    get() = _logout ?: materialIcon(name = "AutoMirrored.Filled.Logout", autoMirror = true) {
        materialPath {
            moveTo(17.0f, 7.0f)
            lineToRelative(-1.41f, 1.41f)
            lineTo(18.17f, 11.0f)
            horizontalLineTo(8.0f)
            verticalLineToRelative(2.0f)
            horizontalLineToRelative(10.17f)
            lineToRelative(-2.58f, 2.58f)
            lineTo(17.0f, 17.0f)
            lineToRelative(5.0f, -5.0f)
            close()
            moveTo(4.0f, 5.0f)
            horizontalLineToRelative(8.0f)
            verticalLineTo(3.0f)
            horizontalLineTo(4.0f)
            curveToRelative(-1.1f, 0.0f, -2.0f, 0.9f, -2.0f, 2.0f)
            verticalLineToRelative(14.0f)
            curveToRelative(0.0f, 1.1f, 0.9f, 2.0f, 2.0f, 2.0f)
            horizontalLineToRelative(8.0f)
            verticalLineToRelative(-2.0f)
            horizontalLineTo(4.0f)
            verticalLineTo(5.0f)
            close()
        }
    }.also { _logout = it }
