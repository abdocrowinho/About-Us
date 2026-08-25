package org.aboutus.project.features.earth.peresntation.composables

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max
import kotlin.math.min
import org.about_us.project.generated.resources.*
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

enum class EarthTourTarget { WAVE, STATES, SCOPE, STATS, TRUTH }
private data class TourHint(
    val target: EarthTourTarget,
    val title: StringResource,
    val body: StringResource
)

@Composable
fun EarthAppTourOverlay(
    targetBounds: Map<EarthTourTarget, Rect>,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var step by remember { mutableIntStateOf(0) }
    val hints = listOf(
        TourHint(EarthTourTarget.WAVE, Res.string.tour_wave_title, Res.string.tour_wave_body),
        TourHint(EarthTourTarget.STATES, Res.string.tour_states_title, Res.string.tour_states_body),
        TourHint(EarthTourTarget.SCOPE, Res.string.tour_scope_title, Res.string.tour_scope_body),
        TourHint(EarthTourTarget.STATS, Res.string.tour_stats_title, Res.string.tour_stats_body),
        TourHint(EarthTourTarget.TRUTH, Res.string.tour_truth_title, Res.string.tour_truth_final)
    )
    val hint = hints[step]
    val rect = targetBounds[hint.target] ?: return
    val pulse by rememberInfiniteTransition(label = "tourPulse").animateFloat(
        12f,
        20f,
        infiniteRepeatable(tween(850), RepeatMode.Reverse),
        label = "tourPulseValue"
    )
    val density = LocalDensity.current
    BoxWithConstraints(modifier.fillMaxSize()) {
        val maxWidth = constraints.maxWidth;
        val maxHeight = constraints.maxHeight
        Canvas(
            Modifier.fillMaxSize()
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }) {
            drawRect(Color.Black.copy(alpha = .73f))
            drawRoundRect(
                Color.Transparent,
                topLeft = androidx.compose.ui.geometry.Offset(rect.left - pulse, rect.top - pulse),
                size = androidx.compose.ui.geometry.Size(
                    rect.width + pulse * 2,
                    rect.height + pulse * 2
                ),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(22.dp.toPx()),
                blendMode = BlendMode.Clear
            )
        }
        val tipWidth = min(with(density) { 280.dp.toPx() }, maxWidth * .82f).toInt()
        val tipX = max(12f, min(rect.center.x - tipWidth / 2f, maxWidth - tipWidth - 12f)).toInt()
        val tipY = max(
            16f,
            min(
                if (rect.center.y < maxHeight / 2f) rect.bottom + 26f else rect.top - 190f,
                maxHeight - 190f
            )
        ).toInt()
        val targetIsAboveTip = rect.center.y < tipY
        Canvas(Modifier.fillMaxSize()) {
            val start =
                Offset(tipX + tipWidth / 2f, if (targetIsAboveTip) tipY.toFloat() else tipY + 180f)
            val end = rect.center
            drawLine(
                Color(0xFF83BDFF),
                start,
                end,
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawCircle(Color(0xFF83BDFF), radius = 6.dp.toPx(), center = end)
            val angle = kotlin.math.atan2(end.y - start.y, end.x - start.x)
            val arrowSize = 12.dp.toPx()
            val arrow = Path().apply {
                moveTo(end.x, end.y)
                lineTo(
                    end.x - arrowSize * kotlin.math.cos((angle - .55f).toDouble()).toFloat(),
                    end.y - arrowSize * kotlin.math.sin((angle - .55f).toDouble()).toFloat()
                )
                lineTo(
                    end.x - arrowSize * kotlin.math.cos((angle + .55f).toDouble()).toFloat(),
                    end.y - arrowSize * kotlin.math.sin((angle + .55f).toDouble()).toFloat()
                )
                close()
            }
            drawPath(arrow, Color(0xFF83BDFF))
        }
        Surface(
            modifier = Modifier.offset { IntOffset(tipX, tipY) }.widthIn(max = 280.dp),
            color = Color(0xFF081426),
            shape = RoundedCornerShape(22.dp),
            shadowElevation = 18.dp
        ) {
            Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    stringResource(hint.title),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(9.dp)); Text(
                stringResource(hint.body),
                color = Color(0xFFC5D6EA),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
                Spacer(Modifier.height(16.dp)); Button(
                onClick = { if (step == hints.lastIndex) onComplete() else step++ },
                colors = ButtonDefaults.buttonColors(Color(0xFF3A8DFF), Color(0xFF02050D)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    stringResource(if (step == hints.lastIndex) Res.string.tour_done else Res.string.tour_got_it),
                    fontWeight = FontWeight.Bold
                )
            }
            }
        }
    }
}
