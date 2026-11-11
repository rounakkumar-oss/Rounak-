package com.example.storage

import com.example.base.FirestoreEmulatorTestBase
import com.example.types.GameType
import com.example.types.GraphicsPreset
import com.example.types.ProjectDraft
import com.example.types.TargetPlatform
import com.example.types.ViewportOrientation
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ProjectStorageRepositoryRuleTest : FirestoreEmulatorTestBase() {

    @Test
    fun createProject_validPayload_createsAndReturnsId() = runBlocking {
        signInTestUser(ALICE_EMAIL)
        val repository = ProjectStorageRepository(firestore)
        val draft = ProjectDraft(
            name = "Solaris Open World",
            description = "Sci-fi open world planet exploration",
            gameType = GameType.OPEN_WORLD,
            targetPlatform = TargetPlatform.BOTH,
            graphicsPreset = GraphicsPreset.ULTRA,
            orientation = ViewportOrientation.LANDSCAPE
        )

        val createResult = withTimeout(DEFAULT_TIMEOUT_MS) { repository.createProject(draft) }
        assertTrue(createResult.isSuccess)
        val projectId = createResult.getOrThrow()
        assertTrue(projectId.isNotEmpty())
    }

    @Test
    fun getUserProjects_andRename_andDelete_succeedForOwner() = runBlocking {
        signInTestUser(ALICE_EMAIL)
        val repository = ProjectStorageRepository(firestore)
        val draft = ProjectDraft(
            name = "Apex Drift Racing",
            description = "Arcade street racing simulator",
            gameType = GameType.RACING,
            targetPlatform = TargetPlatform.ANDROID,
            graphicsPreset = GraphicsPreset.HIGH,
            orientation = ViewportOrientation.LANDSCAPE
        )

        val projectId = withTimeout(DEFAULT_TIMEOUT_MS) { repository.createProject(draft).getOrThrow() }

        val listResult = withTimeout(DEFAULT_TIMEOUT_MS) { repository.getUserProjects() }
        assertTrue(listResult.isSuccess)
        assertTrue(listResult.getOrThrow().any { it.id == projectId })

        val renameResult = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.renameProject(projectId, "Apex Drift Racing Pro")
        }
        assertTrue(renameResult.isSuccess)

        val updatedList = withTimeout(DEFAULT_TIMEOUT_MS) { repository.getUserProjects().getOrThrow() }
        val renamed = updatedList.first { it.id == projectId }
        assertEquals("Apex Drift Racing Pro", renamed.name)

        val deleteResult = withTimeout(DEFAULT_TIMEOUT_MS) { repository.deleteProject(projectId) }
        assertTrue(deleteResult.isSuccess)
    }

    @Test
    fun observeProjects_authenticatedOwner_emitsRealtimeUpdates() = runBlocking {
        signInTestUser(ALICE_EMAIL)
        val repository = ProjectStorageRepository(firestore)
        val draft = ProjectDraft(name = "Live Sync World", description = "Realtime test")
        val projectId = withTimeout(DEFAULT_TIMEOUT_MS) { repository.createProject(draft).getOrThrow() }

        val emitted = withTimeout(FLOW_TIMEOUT_MS) {
            repository.observeProjects().first { list -> list.any { it.id == projectId } }
        }
        assertTrue(emitted.any { it.id == projectId })
    }

    @Test
    fun getProjectById_crossUserAccess_failsWithPermissionDenied() = runBlocking {
        val aliceUid = signInTestUser(ALICE_EMAIL)
        val aliceRepo = ProjectStorageRepository(firestore)
        val projectId = withTimeout(DEFAULT_TIMEOUT_MS) {
            aliceRepo.createProject(ProjectDraft(name = "Alice Secret Game")).getOrThrow()
        }

        signInTestUser(BOB_EMAIL)
        val bobRepo = ProjectStorageRepository(firestore)
        val result = withTimeout(DEFAULT_TIMEOUT_MS) {
            bobRepo.getProjectById(aliceUid, projectId)
        }
        assertTrue(result.isFailure)
        val firestoreException = result.exceptionOrNull()?.unwrapFirestoreException()
        assertNotNull(firestoreException)
        assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, firestoreException?.code)
    }

    @Test
    fun observeProjects_unauthenticatedUser_failsWithPermissionDenied() = runBlocking {
        auth.signOut()
        val repository = ProjectStorageRepository(firestore)

        var caughtException: Throwable? = null
        try {
            withTimeout(FLOW_TIMEOUT_MS) {
                repository.observeProjects().first()
            }
        } catch (e: Throwable) {
            caughtException = e
        }
        assertNotNull("Expected exception for unauthenticated user", caughtException)
        val firestoreException = caughtException?.unwrapFirestoreException()
        assertNotNull("Expected nested FirebaseFirestoreException, got $caughtException", firestoreException)
        assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, firestoreException?.code)
    }

    private companion object {
        const val ALICE_EMAIL = "alice@worldforge.test"
        const val BOB_EMAIL = "bob@worldforge.test"
        const val DEFAULT_TIMEOUT_MS = 5000L
        const val FLOW_TIMEOUT_MS = 3000L
    }
}
