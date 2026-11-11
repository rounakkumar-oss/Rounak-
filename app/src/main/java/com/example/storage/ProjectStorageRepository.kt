package com.example.storage

import android.content.Context
import com.example.R
import com.example.types.ForgeProject
import com.example.types.ProjectDraft
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.snapshots
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

/**
 * Persistent storage repository for World Forge AI projects backed by Cloud Firestore.
 * Always uses the provisioned named database ID from R.string.firestore_database_id.
 */
class ProjectStorageRepository(private val db: FirebaseFirestore) {

    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth = Firebase.auth

    private fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw FirebaseFirestoreException(
                "User must be signed in with Google before accessing Firestore.",
                FirebaseFirestoreException.Code.PERMISSION_DENIED
            )
    }

    fun observeProjects(): Flow<List<ForgeProject>> = flow {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            val unauthPath = "users/unauthenticated/projects"
            emitAll(
                db.collection("users").document("unauthenticated").collection("projects")
                    .snapshots()
                    .map { snapshot ->
                        snapshot.toObjects(
                            ForgeProject::class.java,
                            DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                        )
                    }
                    .catch { error ->
                        val firestoreError = generateSequence(error) { it.cause }
                            .filterIsInstance<FirebaseFirestoreException>()
                            .firstOrNull()
                        val exceptionToLog = firestoreError ?: (error as? Exception)
                        if (exceptionToLog != null) {
                            handleFirestoreError(exceptionToLog, OperationType.LIST, unauthPath)
                        }
                        throw error
                    }
            )
            return@flow
        }

        val path = "users/$uid/projects"
        emitAll(
            db.collection("users").document(uid).collection("projects")
                .snapshots()
                .map { snapshot ->
                    snapshot.toObjects(
                        ForgeProject::class.java,
                        DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                    ).sortedByDescending {
                        (it.updatedAt ?: it.createdAt)?.seconds ?: 0L
                    }
                }
                .catch { error ->
                    val firestoreError = generateSequence(error) { it.cause }
                        .filterIsInstance<FirebaseFirestoreException>()
                        .firstOrNull()
                    if (firestoreError == null && error is CancellationException) throw error
                    val exceptionToLog = firestoreError ?: (error as? Exception)
                    if (exceptionToLog != null && auth.currentUser?.uid == uid) {
                        handleFirestoreError(exceptionToLog, OperationType.LIST, path)
                    }
                    throw error
                }
        )
    }

    suspend fun getUserProjects(): Result<List<ForgeProject>> {
        val uid = requireUserId()
        val path = "users/$uid/projects"
        return try {
            val snapshot = db.collection("users").document(uid).collection("projects")
                .get()
                .await()
            val list = snapshot.toObjects(
                ForgeProject::class.java,
                DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
            ).sortedByDescending {
                (it.updatedAt ?: it.createdAt)?.seconds ?: 0L
            }
            Result.success(list)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.LIST, path)
            Result.failure(e)
        }
    }

    suspend fun getProjectById(ownerUid: String, projectId: String): Result<ForgeProject> {
        val docRef = db.collection("users").document(ownerUid)
            .collection("projects").document(projectId)
        return try {
            val snapshot = docRef.get().await()
            val project = snapshot.toObject(
                ForgeProject::class.java,
                DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
            ) ?: throw IllegalStateException("Project not found: $projectId")
            Result.success(project)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.GET, docRef.path)
            Result.failure(e)
        }
    }

    suspend fun createProject(draft: ProjectDraft): Result<String> {
        val uid = requireUserId()
        val trimmedName = draft.name.trim()
        require(trimmedName.isNotEmpty() && trimmedName.length <= 100) {
            "Project name must be between 1 and 100 characters."
        }
        val trimmedDesc = draft.description.trim().take(500)
        val docRef = db.collection("users").document(uid).collection("projects").document()
        val payload = mapOf(
            "userId" to uid,
            "name" to trimmedName,
            "description" to trimmedDesc,
            "gameType" to draft.gameType.label,
            "targetPlatform" to draft.targetPlatform.label,
            "graphicsPreset" to draft.graphicsPreset.label,
            "orientation" to draft.orientation.label,
            "engineVersion" to "0.1.0-step01",
            "sceneCount" to 1L,
            "assetCount" to 0L,
            "scriptCount" to 0L,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        return try {
            docRef.set(payload).await()
            Result.success(docRef.id)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            Result.failure(e)
        }
    }

    suspend fun renameProject(projectId: String, newName: String): Result<Unit> {
        val uid = requireUserId()
        val trimmedName = newName.trim()
        require(projectId.isNotBlank()) { "Project ID must not be blank" }
        require(trimmedName.isNotEmpty() && trimmedName.length <= 100) {
            "Project name must be between 1 and 100 characters"
        }
        val docRef = db.collection("users").document(uid)
            .collection("projects").document(projectId)
        return try {
            docRef.update(
                mapOf(
                    "name" to trimmedName,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            Result.failure(e)
        }
    }

    suspend fun touchProjectOpened(projectId: String): Result<Unit> {
        val uid = requireUserId()
        require(projectId.isNotBlank()) { "Project ID must not be blank" }
        val docRef = db.collection("users").document(uid)
            .collection("projects").document(projectId)
        return try {
            docRef.update(
                mapOf(
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            Result.failure(e)
        }
    }

    suspend fun deleteProject(projectId: String): Result<Unit> {
        val uid = requireUserId()
        require(projectId.isNotBlank()) { "Project ID must not be blank" }
        val docRef = db.collection("users").document(uid)
            .collection("projects").document(projectId)
        return try {
            docRef.delete().await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, docRef.path)
            Result.failure(e)
        }
    }
}
