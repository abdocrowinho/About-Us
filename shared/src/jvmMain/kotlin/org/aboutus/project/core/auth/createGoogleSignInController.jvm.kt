package org.aboutus.project.core.auth

class JvmGoogleSignInController : GoogleSignInController {
    override suspend fun signIn(): GoogleAccountInfo? = null

    override suspend fun signOut() = Unit
}

actual fun createGoogleSignInController(): GoogleSignInController = JvmGoogleSignInController()
