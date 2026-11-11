package com.example.projectmanager

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storage.ProjectStorageRepository
import com.example.types.ForgeNavDestination
import com.example.types.ForgeProject
import com.example.types.ProjectDraft
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface ProjectsUiState {
    data object Loading : ProjectsUiState
    data class Success(val projects: List<ForgeProject>) : ProjectsUiState
    data class Error(val message: String) : ProjectsUiState
}

/**
 * ViewModel managing WORLD FORGE AI projects, navigation state, and active workspace dialogs.
 */
class ProjectManagerViewModel(
    private val repository: ProjectStorageRepository,
    val currentUserId: String
) : ViewModel() {

    val projectsState: StateFlow<ProjectsUiState> = repository.observeProjects()
        .map<List<ForgeProject>, ProjectsUiState> { ProjectsUiState.Success(it) }
        .catch { error ->
            val firestoreError = generateSequence(error) { it.cause }
                .filterIsInstance<FirebaseFirestoreException>()
                .firstOrNull()
            if (firestoreError == null && error is CancellationException) throw error
            val rootError = firestoreError ?: error
            Log.w(TAG, "Error observing projects", rootError)
            emit(ProjectsUiState.Error(rootError.message ?: "Failed to load projects"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ProjectsUiState.Loading
        )

    private val _currentDestination = MutableStateFlow(ForgeNavDestination.DASHBOARD)
    val currentDestination: StateFlow<ForgeNavDestination> = _currentDestination.asStateFlow()

    private val _activeOpenedProjectId = MutableStateFlow<String?>(null)
    val activeOpenedProjectId: StateFlow<String?> = _activeOpenedProjectId.asStateFlow()

    private val _showCreateDialog = MutableStateFlow(false)
    val showCreateDialog: StateFlow<Boolean> = _showCreateDialog.asStateFlow()

    private val _projectToRename = MutableStateFlow<ForgeProject?>(null)
    val projectToRename: StateFlow<ForgeProject?> = _projectToRename.asStateFlow()

    private val _projectToDelete = MutableStateFlow<ForgeProject?>(null)
    val projectToDelete: StateFlow<ForgeProject?> = _projectToDelete.asStateFlow()

    private val _showGitHubExportModal = MutableStateFlow(false)
    val showGitHubExportModal: StateFlow<Boolean> = _showGitHubExportModal.asStateFlow()

    private val _projectForGitHubExport = MutableStateFlow<ForgeProject?>(null)
    val projectForGitHubExport: StateFlow<ForgeProject?> = _projectForGitHubExport.asStateFlow()

    private val _statusBannerMessage = MutableStateFlow<String?>(null)
    val statusBannerMessage: StateFlow<String?> = _statusBannerMessage.asStateFlow()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    fun navigateTo(destination: ForgeNavDestination) {
        if (destination == ForgeNavDestination.CREATE_NEW_GAME) {
            _showCreateDialog.value = true
            _currentDestination.value = ForgeNavDestination.CREATE_NEW_GAME
        } else {
            _currentDestination.value = destination
        }
    }

    fun openCreateProjectDialog() {
        _showCreateDialog.value = true
    }

    fun closeCreateProjectDialog() {
        _showCreateDialog.value = false
        if (_currentDestination.value == ForgeNavDestination.CREATE_NEW_GAME) {
            _currentDestination.value = ForgeNavDestination.DASHBOARD
        }
    }

    fun openProject(project: ForgeProject) {
        _activeOpenedProjectId.value = project.id
        viewModelScope.launch {
            repository.touchProjectOpened(project.id)
        }
    }

    fun closeOpenedProjectInspector() {
        _activeOpenedProjectId.value = null
    }

    fun requestRenameProject(project: ForgeProject) {
        _projectToRename.value = project
    }

    fun dismissRenameDialog() {
        _projectToRename.value = null
    }

    fun requestDeleteProject(project: ForgeProject) {
        _projectToDelete.value = project
    }

    fun dismissDeleteDialog() {
        _projectToDelete.value = null
    }

    fun openGitHubExportModal(project: ForgeProject? = null) {
        _projectForGitHubExport.value = project
        _showGitHubExportModal.value = true
    }

    fun closeGitHubExportModal() {
        _showGitHubExportModal.value = false
        _projectForGitHubExport.value = null
    }

    fun postStatusMessage(message: String) {
        _statusBannerMessage.value = message
    }

    fun clearStatusBanner() {
        _statusBannerMessage.value = null
    }

    fun createProject(draft: ProjectDraft, onCreated: (String) -> Unit = {}) {
        val trimmed = draft.name.trim()
        if (trimmed.isEmpty()) {
            _statusBannerMessage.value = "Project Name is required."
            return
        }
        _isSubmitting.value = true
        viewModelScope.launch {
            val result = repository.createProject(draft)
            _isSubmitting.value = false
            result.onSuccess { newId ->
                _showCreateDialog.value = false
                _activeOpenedProjectId.value = newId
                if (_currentDestination.value == ForgeNavDestination.CREATE_NEW_GAME) {
                    _currentDestination.value = ForgeNavDestination.DASHBOARD
                }
                _statusBannerMessage.value = "Project \"${trimmed}\" created and saved to workspace."
                onCreated(newId)
            }.onFailure { err ->
                _statusBannerMessage.value = err.localizedMessage ?: "Failed to create project."
            }
        }
    }

    fun confirmRenameProject(newName: String) {
        val target = _projectToRename.value ?: return
        val trimmed = newName.trim()
        if (trimmed.isEmpty()) {
            _statusBannerMessage.value = "Project Name cannot be empty."
            return
        }
        _isSubmitting.value = true
        viewModelScope.launch {
            val result = repository.renameProject(target.id, trimmed)
            _isSubmitting.value = false
            result.onSuccess {
                _projectToRename.value = null
                _statusBannerMessage.value = "Renamed project to \"${trimmed}\"."
            }.onFailure { err ->
                _statusBannerMessage.value = err.localizedMessage ?: "Failed to rename project."
            }
        }
    }

    fun confirmDeleteProject() {
        val target = _projectToDelete.value ?: return
        _isSubmitting.value = true
        viewModelScope.launch {
            val result = repository.deleteProject(target.id)
            _isSubmitting.value = false
            result.onSuccess {
                if (_activeOpenedProjectId.value == target.id) {
                    _activeOpenedProjectId.value = null
                }
                _projectToDelete.value = null
                _statusBannerMessage.value = "Project \"${target.name}\" deleted."
            }.onFailure { err ->
                _statusBannerMessage.value = err.localizedMessage ?: "Failed to delete project."
            }
        }
    }

    private companion object {
        const val TAG = "ProjectManagerVM"
    }
}
