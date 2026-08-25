package org.aboutus.project.features.earth.wave.presentation

import androidx.compose.animation.animateColorAsState
import kotlin.math.PI
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.isActive
import kotlin.math.sin

@Composable
fun VerticalWaveComponent(
    state: WaveState,
    modifier: Modifier = Modifier
) {
    val animatedSpeed by animateFloatAsState(
        targetValue = state.speedFactor,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "SpeedDecay"
    )

    val animatedAmplitude by animateFloatAsState(
        targetValue = state.amplitudeFactor,
        animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing),
        label = "AmplitudeDecay"
    )

    val animatedColor by animateColorAsState(
        targetValue = state.waveCase,
        animationSpec = tween(durationMillis = 800),
        label = "ColorSmooth"
    )

    var phase by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        var lastTime = withFrameNanos { it }
        while (isActive) {
            withFrameNanos { currentTime ->
                val deltaSeconds = (currentTime - lastTime) / 1_000_000_000f
                lastTime = currentTime
                phase = (phase + deltaSeconds * animatedSpeed * 2.5f) % (2f * PI.toFloat())
            }
        }
    }

    Canvas(
        modifier = modifier
            .width(90.dp)
            .height(260.dp)
    ) {
        val axisX = 24f
        val height = size.height

        // رسم المقياس والـ Ticks
        drawLine(
            color = animatedColor.copy(alpha = 0.3f),
            start = Offset(axisX, 0f),
            end = Offset(axisX, height),
            strokeWidth = 2f
        )

        val tickSpacing = height / 12f
        for (i in 0..12) {
            val y = i * tickSpacing
            drawLine(
                color = animatedColor.copy(alpha = 0.4f),
                start = Offset(axisX - 8f, y),
                end = Offset(axisX, y),
                strokeWidth = 2f
            )
        }

        val wavePath = Path()
        val stepY = 3f
        var first = true

        var currentY = 0f
        while (currentY <= height) {
            val normalizedY = currentY / height
            val edgeDamping = sin(normalizedY * PI).toFloat()

            val wave1 = sin((normalizedY * 10f) + phase)
            val wave2 = sin((normalizedY * 20f) - (phase * 1.2f)) * 0.35f

            val calculatedX = axisX + (wave1 + wave2) * animatedAmplitude * edgeDamping

            if (first) {
                wavePath.moveTo(calculatedX, currentY)
                first = false
            } else {
                wavePath.lineTo(calculatedX, currentY)
            }
            currentY += stepY
        }

        drawPath(
            path = wavePath,
            color = animatedColor,
            style = Stroke(width = 3.5f)
        )
    }
}