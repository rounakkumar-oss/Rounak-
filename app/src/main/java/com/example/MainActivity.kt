package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.layouts.WorldForgeMainLayout
import com.example.pages.AuthScreen
import com.example.projectmanager.ProjectManagerViewModel
import com.example.services.AuthService
import com.example.storage.ProjectStorageRepository
import com.example.ui.theme.MyApplicationTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                WorldForgeAppRoot()
            }
        }
    }
}

@Composable
fun WorldForgeAppRoot() {
    val auth = Firebase.auth
    val currentUser by AuthService.observeAuthState(auth)
        .collectAsStateWithLifecycle(initialValue = auth.currentUser)

    val user = currentUser
    if (user == null) {
        AuthScreen(
            onAuthSuccess = {
                // AuthStateListener automatically emits the signed-in user
            }
        )
    } else {
        val uid = user.uid
        key(uid) {
            val viewModel: ProjectManagerViewModel = viewModel(
                key = uid,
                factory = viewModelFactory {
                    initializer {
                        val app = checkNotNull(this[APPLICATION_KEY]) {
                            "APPLICATION_KEY missing from CreationExtras"
                        }
                        val databaseId = app.getString(R.string.firestore_database_id)
                        val db = FirebaseFirestore.getInstance(databaseId)
                        ProjectManagerViewModel(
                            repository = ProjectStorageRepository(db),
                            currentUserId = uid
                        )
                    }
                }
            )
            WorldForgeMainLayout(
                viewModel = viewModel,
                userEmail = user.email
            )
        }
    }
}
