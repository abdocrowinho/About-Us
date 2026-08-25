package org.aboutus.project.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.aboutus.project.core.messageColorHandler
import org.aboutus.project.features.earth.words.data.MessageState

private val brandColors = listOf(
    messageColorHandler(MessageState.DISTRACTION),
    messageColorHandler(MessageState.HOPE),
    messageColorHandler(MessageState.LOVE),
    messageColorHandler(MessageState.FUN),
    messageColorHandler(MessageState.FEAR),
    messageColorHandler(MessageState.DISGUST),
    messageColorHandler(MessageState.ANGER),
    messageColorHandler(MessageState.HUNGER)
)

@Composable
fun AboutUsBrand(modifier: Modifier = Modifier, fontSize: Int = 24) {
    Text(
        text = aboutUsColoredText(),
        modifier = modifier,
        fontSize = fontSize.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = (-.6).sp
    )
}

@Composable
fun AboutUsTopBar(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, top = 14.dp, bottom = 8.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            AboutUsBrand(fontSize = 16)
        }
        HorizontalDivider(color = Color.White.copy(alpha = .07f), thickness = .5.dp)
    }
}

private fun aboutUsColoredText(): AnnotatedString = buildAnnotatedString {
    "About Us".forEachIndexed { index, letter ->
        pushStyle(SpanStyle(color = brandColors[index % brandColors.size]))
        append(letter)
        pop()
    }
}
