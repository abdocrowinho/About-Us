package org.aboutus.project.features.earth.words

import androidx.compose.ui.geometry.Rect
import org.aboutus.project.features.earth.domain.LatLng

data class FloatingWord(
    val id: String,
    val word: String,
    val sentence: String,
    val location: LatLng? = null,
    val fullSentence: String,
    val countryCode: String = "",
    val state: String = "NORMAL",
    val angleRad: Float,
    val currentDistance: Float,
    val maxDistance: Float,
    val speed: Float,
    val alpha: Float = 1.0f,
    val boundsOnScreen: Rect = Rect.Zero
)