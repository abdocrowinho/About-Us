# Cursor / Architecture Conventions — About Us

## Stack

- **Kotlin Multiplatform** + **Compose Multiplatform**
- **MVI**: `State`, `Event`, `Effect` per feature; ViewModels extend `BaseViewModel`
- **Dialogs**: independent `BaseDialogViewModel` (see `ReflectionDialogViewModel`)
- **DI**: Koin (`di/AppModule.kt`, `KoinApplication` in `App.kt`)
- **UiResult**: `core/ui/UiResult.kt` for async UI flows (use in repositories later)
- **Images**: Coil 3 (`coil-compose`, `coil-network-ktor`)

## Package layout

```
org.aboutus.project/
  core/           # MVI base, theme, shared UI
  di/             # Koin modules
  navigation/     # Main app tabs
  feature/
    auth/         # Onboarding, login, register, forgot password
    mosque/       # Prayer status, dhikr walkie-talkie
    group/        # Quran progress + reflections dialog
    hadith/       # Spiritual feed
```

## Per-feature docs

Each feature folder contains `FEATURE.md` explaining MVI flow and UI.

## Running

- Desktop: `./gradlew :desktopApp:run`
- Android: `./gradlew :androidApp:assembleDebug`
