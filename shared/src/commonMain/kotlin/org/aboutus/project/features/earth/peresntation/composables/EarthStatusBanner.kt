package org.aboutus.project.features.earth.peresntation.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.about_us.project.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.aboutus.project.features.earth.domain.EarthError

@Composable
fun EarthStatusBanner(isLoading: Boolean, error: EarthError?, modifier: Modifier = Modifier) {
    val text = when (error) {
        EarthError.NoInternet -> stringResource(Res.string.earth_no_internet)
        EarthError.ServiceUnavailable, EarthError.Unknown -> stringResource(Res.string.earth_update_failed)
        null -> if (isLoading) stringResource(Res.string.earth_loading) else null
    }
    AnimatedVisibility(
        visible = text != null,
        enter = slideInVertically { -it },
        exit = slideOutVertically { -it },
        modifier = modifier
    ) {
        Box(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(bottomStart = 14.dp, bottomEnd = 14.dp))
                .background(if (error == EarthError.NoInternet) Color(0xFF172235) else Color(0xFF10233C))
                .padding(vertical = 10.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) { Text(text.orEmpty(), color = Color(0xFFD9E8FF), fontSize = 13.sp, fontWeight = FontWeight.Medium) }
    }
}
