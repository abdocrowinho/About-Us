package org.aboutus.project.features.earth.peresntation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import org.aboutus.project.core.messageColorHandler
import org.aboutus.project.features.earth.words.data.CountryStatDto
import org.aboutus.project.features.earth.words.data.MessageState
import org.aboutus.project.features.earth.domain.EarthError
import org.about_us.project.generated.resources.Res
import org.about_us.project.generated.resources.stats_countries
import org.about_us.project.generated.resources.stats_empty
import org.about_us.project.generated.resources.stats_votes

@Composable
fun TopStatsBar(
    stats: List<CountryStatDto>,
    totalVotes: Long,
    participatingCountries: Int,
    error: EarthError?,
    modifier: Modifier = Modifier
) {
    val stateTotals = stats
        .groupBy { it.state }
        .map { (state, entries) -> state to entries.sumOf { it.count } }
        .sortedByDescending { it.second }
    val total = stateTotals.sumOf { it.second }

    if (error != null) {
        return
    }

    if (total == 0L) {
        Surface(
            modifier = modifier,
            color = Color(0xDD101827),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = stringResource(Res.string.stats_empty),
                color = Color(0xFFB8C2D3),
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }
        return
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(stringResource(Res.string.stats_votes, totalVotes), color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text("•", color = Color(0xFF718096))
            Text(stringResource(Res.string.stats_countries, participatingCountries), color = Color(0xFFB8C2D3), fontSize = 13.sp)
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(stateTotals, key = { it.first }) { (stateValue, count) ->
            val state = MessageState.fromValue(stateValue)
            val color = messageColorHandler(state)
            val percentage = (count * 100f / total).toInt()

            Column(
                modifier = Modifier
                    .width(104.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xDD101827))
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(state.stringResId),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                    Text(
                        text = "$percentage%",
                        color = color,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(99.dp))
                        .background(Color.White.copy(alpha = .12f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(percentage / 100f)
                            .background(color)
                            .padding(vertical = 2.dp)
                    )
                }
            }
            }
        }
    }
}
