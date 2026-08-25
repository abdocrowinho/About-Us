package org.aboutus.project.features.earth.words

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import org.aboutus.project.core.messageColorHandler
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.hypot

@Composable
fun ScatteredWordsOverlay(
    words: List<FloatingWord>,
    onWordClick: (FloatingWord) -> Unit,
    onDrag: (Float, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val latestWords by rememberUpdatedState(words)

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown()
                    val downOffset = down.position
                    var totalDrag = Offset.Zero
                    var isDragging = false
                    while (true) {
                        val event = awaitPointerEvent()
                        val change = event.changes.firstOrNull { it.id == down.id } ?: break
                        val dragAmount = change.positionChange()
                        totalDrag += dragAmount
                        if (hypot(totalDrag.x, totalDrag.y) > 14f) isDragging = true
                        if (isDragging && dragAmount != Offset.Zero) {
                            change.consume()
                            onDrag(dragAmount.x, dragAmount.y)
                        }
                        if (change.pressed) continue
                        if (isDragging) break
                        val centerX = size.width / 2f
                        val centerY = size.height / 2f

                        latestWords.asReversed().forEach { word ->
                            val x = centerX + word.currentDistance * cos(word.angleRad)
                            val y = centerY + word.currentDistance * sin(word.angleRad)

                            val textLayout = textMeasurer.measure(
                                text = word.word,
                                style = TextStyle(fontSize = 13.sp)
                            )

                            val padding = 18f
                            val rectLeft = x - (textLayout.size.width / 2f) - padding
                            val rectTop = y - (textLayout.size.height / 2f) - padding
                            val rectRight = rectLeft + textLayout.size.width + padding * 2
                            val rectBottom = rectTop + textLayout.size.height + padding * 2

                            if (downOffset.x in rectLeft..rectRight && downOffset.y in rectTop..rectBottom) {
                                onWordClick(word)
                                return@forEach
                            }
                        }
                        break
                    }
                }
            }
    ) {
        val centerX = size.width / 2f
        val centerY = size.height / 2f

        words.forEach { word ->
            val x = centerX + word.currentDistance * cos(word.angleRad)
            val y = centerY + word.currentDistance * sin(word.angleRad)

            val textLayoutResult = textMeasurer.measure(
                text = word.word,
                style = TextStyle(
                    color = messageColorHandler(word.state.toInt()).copy(alpha = word.alpha),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )

            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(
                    x = x - (textLayoutResult.size.width / 2f),
                    y = y - (textLayoutResult.size.height / 2f)
                )
            )
        }
    }
}
