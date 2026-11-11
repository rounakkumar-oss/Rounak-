package com.example.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import com.example.assets.ForgeAssetCatalog
import com.example.components.DirectApkAndGitHubBannerCard
import com.example.components.ForgeBrandHeaderBadge
import com.example.components.GitHubExportDialog
import com.example.services.AuthService
import com.example.styles.ForgeStyles
import com.example.styles.engineGridBackground
import com.example.types.ForgeProject
import com.example.ui.theme.ForgeBlueLight
import com.example.ui.theme.ForgeCardBorder
import com.example.ui.theme.ForgeCharcoalBg
import com.example.ui.theme.ForgeDangerRed
import com.example.ui.theme.ForgeElectricBlue
import com.example.ui.theme.ForgeSuccessGreen
import com.example.ui.theme.ForgeSurface
import com.example.ui.theme.ForgeSurfaceElevated
import com.example.ui.theme.ForgeTextPrimary
import com.example.ui.theme.ForgeTextSecondary
import com.example.ui.theme.ForgeVioletLight

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember(context) { CredentialManager.create(context) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var statusNotice by remember { mutableStateOf<String?>(null) }
    var showGitHubApkModal by remember { mutableStateOf(false) }
    var showApkInstallPage by remember { mutableStateOf(false) }

    val defaultStarterProject = remember {
        ForgeProject(
            id = "world-forge-ai-starter",
            name = "World Forge AI Open World",
            description = "WORLD FORGE AI — Step 01 Foundation & Open World Game Engine Platform"
        )
    }

    LaunchedEffect(Unit) {
        AuthService.attemptAutoSignIn(
            context = context,
            credentialManager = credentialManager,
            onAuthSuccess = onAuthSuccess,
            onUnauthenticated = { /* Stay on interactive sign-in screen */ },
            scope = scope
        )
    }

    if (showGitHubApkModal) {
        GitHubExportDialog(
            project = defaultStarterProject,
            allProjects = listOf(defaultStarterProject),
            onDismiss = { showGitHubApkModal = false },
            onExportStatusMessage = { msg ->
                statusNotice = msg
            }
        )
    }

    if (showApkInstallPage) {
        androidx.activity.compose.BackHandler {
            showApkInstallPage = false
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ForgeCharcoalBg)
                .engineGridBackground()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 720.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (statusNotice != null) {
                    Surface(
                        color = ForgeSuccessGreen.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, ForgeSuccessGreen.copy(alpha = 0.6f))
                    ) {
                        Text(
                            text = statusNotice!!,
                            style = MaterialTheme.typography.bodySmall,
                            color = ForgeSuccessGreen,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
                ApkInstallDownloadPage(
                    activeProject = defaultStarterProject,
                    allProjects = listOf(defaultStarterProject),
                    onPostStatusMessage = { statusNotice = it },
                    onReturnToDashboard = { showApkInstallPage = false }
                )
            }
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ForgeCharcoalBg)
            .engineGridBackground()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 580.dp)
                .verticalScroll(rememberScrollState()),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = ForgeSurface),
            border = BorderStroke(1.dp, ForgeCardBorder)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    Image(
                        painter = painterResource(id = ForgeAssetCatalog.HeroBannerRes),
                        contentDescription = "World Forge AI Engine Viewport",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(ForgeStyles.HeroOverlayBrush)
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(20.dp)
                    ) {
                        ForgeBrandHeaderBadge()
                    }
                }

                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (statusNotice != null) {
                        Surface(
                            color = ForgeSuccessGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, ForgeSuccessGreen.copy(alpha = 0.6f))
                        ) {
                            Text(
                                text = statusNotice!!,
                                style = MaterialTheme.typography.bodySmall,
                                color = ForgeSuccessGreen,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }

                    // Prominent Direct APK & GitHub Download Center right on the first screen!
                    DirectApkAndGitHubBannerCard(
                        activeProject = defaultStarterProject,
                        allProjects = listOf(defaultStarterProject),
                        onOpenApkInstallPage = { showApkInstallPage = true },
                        onOpenGitHubApkHub = { showGitHubApkModal = true },
                        onStatusMessage = { statusNotice = it }
                    )

                    Text(
                        text = "AI-Powered Open World Game Development Platform",
                        style = MaterialTheme.typography.headlineMedium,
                        color = ForgeTextPrimary
                    )
                    Text(
                        text = "Sign in with your Google Developer account to access your persistent Cloud Firestore project workspace, create/rename/delete game projects, and configure target platforms.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ForgeTextSecondary
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FeaturePill(Icons.Default.Public, "AI Open World Engine")
                        FeaturePill(Icons.Default.ViewInAr, "3D World Scanner")
                        FeaturePill(Icons.Default.Code, "Visual & AI Scripting")
                        FeaturePill(Icons.Default.CloudDone, "Persistent Workspace")
                    }

                    if (errorMessage != null) {
                        Surface(
                            color = ForgeDangerRed.copy(alpha = 0.14f),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, ForgeDangerRed.copy(alpha = 0.45f))
                        ) {
                            Text(
                                text = errorMessage!!,
                                style = MaterialTheme.typography.bodySmall,
                                color = ForgeDangerRed,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }

                    Button(
                        onClick = {
                            isLoading = true
                            errorMessage = null
                            AuthService.signInWithGoogleInteractive(
                                context = context,
                                credentialManager = credentialManager,
                                onAuthSuccess = {
                                    isLoading = false
                                    onAuthSuccess()
                                },
                                onAuthError = { msg ->
                                    isLoading = false
                                    errorMessage = msg
                                },
                                scope = scope,
                                onAuthCancelled = {
                                    isLoading = false
                                }
                            )
                        },
                        enabled = !isLoading,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ForgeElectricBlue,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("google_sign_in_button")
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Authenticating Studio Session...", style = MaterialTheme.typography.labelLarge)
                        } else {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Login,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Sign in with Google (Open Dashboard)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = ForgeBlueLight,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Zero-trust multi-user isolation · Projects synced via Cloud Firestore Enterprise",
                            style = MaterialTheme.typography.labelSmall,
                            color = ForgeTextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FeaturePill(
    icon: ImageVector,
    label: String
) {
    Surface(
        color = ForgeSurfaceElevated,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, ForgeCardBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = ForgeVioletLight,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = ForgeTextPrimary
            )
        }
    }
}
