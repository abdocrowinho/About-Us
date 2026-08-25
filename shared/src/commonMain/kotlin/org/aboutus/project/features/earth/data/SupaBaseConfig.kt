package org.aboutus.project.features.earth.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.serialization.json.Json

object SupabaseConfig {
    private const val SUPABASE_URL = "https://gzreuqkiywwemfbyyuok.supabase.co"
    private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imd6cmV1cWtpeXd3ZW1mYnl5dW9rIiwicm9sZSI6ImFub24iLCJpYXQiOjE3ODcyMTg2NzUsImV4cCI6MjEwMjc5NDY3NX0.zeYuLbHdgKqHkC6kYRIgIrgNkmZjzOiKZHp6-oqETPg"

    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = SUPABASE_URL,
            supabaseKey = SUPABASE_KEY
        ) {
            install(Auth)
            install(Realtime)
            install(Postgrest)
            defaultSerializer = KotlinXSerializer(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }
}
