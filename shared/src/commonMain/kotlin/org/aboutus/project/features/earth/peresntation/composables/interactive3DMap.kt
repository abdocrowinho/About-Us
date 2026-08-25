package org.aboutus.project.features.earth.peresntation.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import org.aboutus.project.features.earth.domain.Projection3D
import org.aboutus.project.features.about_us.peresntation.Map3DState
import org.aboutus.project.features.earth.words.data.CountryStatDto
import org.aboutus.project.features.earth.words.data.MessageState
import org.aboutus.project.core.messageColorHandler
import org.aboutus.project.features.earth.domain.LatLng

@Composable
fun Interactive3DMap(
    state: Map3DState,
    countryStats: List<CountryStatDto>,
    modifier: Modifier = Modifier
) {
    val lightPulse by rememberInfiniteTransition(label = "countryLights").animateFloat(
        initialValue = .78f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(tween(1100), RepeatMode.Reverse),
        label = "countryLightPulse"
    )
    val statsByCountry = countryStats
        .filter { it.state != "ALL" }
        .groupBy { it.country_code.uppercase() }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF050811))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2f
            val centerY = size.height / 2f
            val centerOffset = Offset(centerX, centerY)

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF4CC9FE).copy(alpha = 0.35f),
                        Color(0xFF4CC9FE).copy(alpha = 0.05f),
                        Color.Transparent
                    ),
                    center = centerOffset,
                    radius = state.globeRadius * 1.35f
                ),
                radius = state.globeRadius * 1.35f,
                center = centerOffset
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF1B4965),
                        Color(0xFF0B2545),
                        Color(0xFF031024)
                    ),
                    center = Offset(
                        centerX - state.globeRadius * 0.3f,
                        centerY - state.globeRadius * 0.3f
                    ),
                    radius = state.globeRadius * 1.4f
                ),
                radius = state.globeRadius,
                center = centerOffset
            )

            state.countries.forEach { country ->
                var firstPointWritten = false
                val path = Path()

                country.points3D.forEach { raw3D ->
                    val rotated3D =
                        Projection3D.rotatePoint(raw3D, state.rotationX, state.rotationY)

                    if (rotated3D.z > 0f) {
                        val screenPoint = Projection3D.projectToScreen(
                            point = rotated3D,
                            centerX = centerX,
                            centerY = centerY
                        )

                        if (!firstPointWritten) {
                            path.moveTo(screenPoint.x, screenPoint.y)
                            firstPointWritten = true
                        } else {
                            path.lineTo(screenPoint.x, screenPoint.y)
                        }
                    } else {
                        firstPointWritten = false
                    }
                }

                drawPath(
                    path = path,
                    color = Color(0xFF52B788),
                    style = Stroke(width = 1.8f)
                )

                val countryId = country.id.substringBefore("_").uppercase()
                val countryStates = statsByCountry[countryId].orEmpty()
                if (countryStates.isNotEmpty()) {
                    val dominant = countryStates.maxByOrNull { it.count } ?: return@forEach
                    val total = countryStates.sumOf { it.count }
                    val center = country.boundary.averageLatLng()
                    val rotated = Projection3D.rotatePoint(
                        Projection3D.latLngToVector3D(center, state.globeRadius),
                        state.rotationX,
                        state.rotationY
                    )
                    if (rotated.z > 0f) {
                        val point = Projection3D.projectToScreen(rotated, centerX, centerY)
                        val color = messageColorHandler(MessageState.fromValue(dominant.state))
                        val coreRadius = (3f + kotlin.math.ln((total + 1).toDouble()).toFloat() * 1.7f) * lightPulse
                        val glowRadius = coreRadius * 5.5f
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(color.copy(alpha = .38f), color.copy(alpha = .10f), Color.Transparent),
                                center = point,
                                radius = glowRadius
                            ),
                            radius = glowRadius,
                            center = point
                        )
                        drawCircle(color = color, radius = coreRadius, center = point)
                        drawCircle(color = Color.White.copy(alpha = .85f), radius = coreRadius * .3f, center = point)
                    }
                }
            }
        }
    }
}

private fun List<LatLng>.averageLatLng(): LatLng {
    if (isEmpty()) return LatLng(0f, 0f)
    val latitude = sumOf { it.latitude.toDouble() } / size
    val longitude = sumOf { it.longitude.toDouble() } / size
    return LatLng(latitude.toFloat(), longitude.toFloat())
}
