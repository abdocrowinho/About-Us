package org.aboutus.project.features.earth.wave.domain


data class WaveParams(
    val speedFactor: Float,
    val amplitudeFactor: Float,
)
object WaveCalculator {
    fun calculateParams(intensity: Float): WaveParams {

        val speedFactor = when {
            intensity < 0.3f -> 0.5f
            intensity < 0.7f -> 1.5f
            else -> 4.0f
        }

        val amplitudeFactor = when {
            intensity < 0.3f -> 8f
            intensity < 0.7f -> 22f
            else -> 42f
        }

        return WaveParams(speedFactor = speedFactor, amplitudeFactor = amplitudeFactor)
    }
}

