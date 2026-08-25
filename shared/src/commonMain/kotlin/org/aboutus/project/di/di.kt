package org.aboutus.project.di

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.aboutus.project.core.data.SupaServices
import org.aboutus.project.core.domain.SupaServicesImpl
import org.aboutus.project.features.earth.data.SupabaseConfig
import org.aboutus.project.features.earth.peresntation.Map3DViewModel
import org.aboutus.project.features.earth.wave.presentation.WaveViewModel
import org.aboutus.project.features.earth.words.EarthWordsViewModel
import org.aboutus.project.features.welcome.data.SettingsWelcomePreferences
import org.aboutus.project.features.welcome.data.SupabaseAuthRepository
import org.aboutus.project.features.welcome.domain.AuthRepository
import org.aboutus.project.features.welcome.domain.WelcomePreferences
import org.aboutus.project.features.location.domain.UserLocationPreferences
import org.aboutus.project.features.location.data.SettingsUserLocationPreferences
import org.aboutus.project.features.location.presentation.LocationViewModel
import org.aboutus.project.features.splash.presentation.SplashViewModel
import org.aboutus.project.features.onboarding.presentation.OnboardingViewModel
import org.aboutus.project.features.auth.presentation.AuthViewModel
import org.aboutus.project.features.welcome.presentation.WelcomeCoordinatorViewModel

val dataModule = module {
    single<SupabaseClient> {
        SupabaseConfig.client
    }

    single<Postgrest> {
        get<SupabaseClient>().postgrest
    }

    single <SupaServices>{ SupaServicesImpl(get()) }
    single<AuthRepository> { SupabaseAuthRepository(get()) }
    single<WelcomePreferences> { SettingsWelcomePreferences() }
    single<UserLocationPreferences> { SettingsUserLocationPreferences() }
}
val presentation = module {
    viewModel { WaveViewModel(get()) }
    viewModel { EarthWordsViewModel(get()) }
    viewModel { Map3DViewModel(get()) }
    viewModel { SplashViewModel() }
    viewModel { OnboardingViewModel(get()) }
    viewModel { AuthViewModel(get(), get()) }
    viewModel { WelcomeCoordinatorViewModel(get()) }
    viewModel { LocationViewModel(get()) }
}
