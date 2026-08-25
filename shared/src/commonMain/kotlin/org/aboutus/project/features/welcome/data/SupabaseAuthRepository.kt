package org.aboutus.project.features.welcome.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import org.aboutus.project.features.welcome.domain.AuthRepository

class SupabaseAuthRepository(private val supabase: SupabaseClient) : AuthRepository {
    override suspend fun signInAnonymously(): Result<Unit> = runCatching {
        supabase.auth.signInAnonymously()
    }
}
