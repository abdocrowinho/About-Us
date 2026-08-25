package org.aboutus.project.features.welcome.domain

interface AuthRepository {
    suspend fun signInAnonymously(): Result<Unit>
}
