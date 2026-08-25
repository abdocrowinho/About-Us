package org.aboutus.project.core.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/** A quiet, non-interactive space layer reused by welcome screens. */
@Composable
fun AmbientFeelingLights(modifier: Modifier = Modifier) {
    val time by rememberInfiniteTransition(label = "ambientFeelingLights").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 18_000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ambientFeelingLightsTime"
    )

    Canvas(modifier.fillMaxSize()) {
        ambientLights.forEachIndexed { index, light ->
            drawAmbientLight(light, index, time)
        }
    }
}

private fun DrawScope.drawAmbientLight(light: AmbientLight, index: Int, time: Float) {
    val phase = time * 2f * PI.toFloat() + index * 0.94f
    val x = size.width * light.x + sin(phase * light.speed) * 26.dp.toPx()
    val y = size.height * light.y + cos(phase * (light.speed * .72f)) * 18.dp.toPx()
    val point = Offset(x, y)
    val trailDirection = Offset(
        x - cos(phase) * light.trail.dp.toPx(),
        y - sin(phase) * light.trail.dp.toPx()
    )

    // A diffuse smoke trail stays behind each light without becoming visually heavy.
    repeat(4) { smokeIndex ->
        val progress = (smokeIndex + 1) / 4f
        val smokePoint = Offset(
            x + (trailDirection.x - x) * progress,
            y + (trailDirection.y - y) * progress
        )
        drawCircle(
            color = Color(0xFF9AB7D7).copy(alpha = .045f * (1f - progress)),
            radius = (5f + smokeIndex * 4f).dp.toPx(),
            center = smokePoint
        )
    }
    drawLine(
        color = light.color.copy(alpha = .16f),
        start = trailDirection,
        end = point,
        strokeWidth = 1.dp.toPx(),
        cap = Stroke.DefaultCap
    )
    drawCircle(light.color.copy(alpha = .10f), radius = 13.dp.toPx(), center = point)
    drawCircle(light.color.copy(alpha = .28f), radius = 6.dp.toPx(), center = point)
    drawCircle(light.color, radius = 2.2.dp.toPx(), center = point)
}

private data class AmbientLight(
    val x: Float,
    val y: Float,
    val color: Color,
    val speed: Float,
    val trail: Float
)

private val ambientLights = listOf(
    AmbientLight(.12f, .18f, Color(0xFFFF5A5F), .76f, 24f),
    AmbientLight(.83f, .12f, Color(0xFF8B68DB), .62f, 18f),
    AmbientLight(.74f, .32f, Color(0xFFFFC84A), .84f, 26f),
    AmbientLight(.18f, .57f, Color(0xFF63D7AE), .57f, 20f),
    AmbientLight(.89f, .61f, Color(0xFF58B8FF), .68f, 22f),
    AmbientLight(.37f, .82f, Color(0xFFFF7E5F), .71f, 24f),
    AmbientLight(.61f, .76f, Color(0xFFEF5595), .53f, 18f),
    AmbientLight(.42f, .38f, Color(0xFF9BE75B), .66f, 22f)
)
