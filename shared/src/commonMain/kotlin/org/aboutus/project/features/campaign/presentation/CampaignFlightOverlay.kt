package org.aboutus.project.features.campaign.presentation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.about_us.project.generated.resources.Res
import org.about_us.project.generated.resources.campaign_care_message
import org.about_us.project.generated.resources.campaign_hours_left
import org.about_us.project.generated.resources.campaign_spacecraft
import org.about_us.project.generated.resources.pepsi_campaign_mock
import org.aboutus.project.features.campaign.domain.RegionalCampaign
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CampaignFlightOverlay(
    campaign: RegionalCampaign,
    onCouponClick: (RegionalCampaign) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val progress by rememberInfiniteTransition(label = "campaignFlight").animateFloat(
            initialValue = -1.12f,
            targetValue = 1.12f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 15_000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "campaignFlightOffset"
        )
        val offset = maxWidth * progress

        Column(
            modifier = Modifier
                .width(174.dp)
                .graphicsLayer { translationX = offset.toPx() },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(Res.drawable.campaign_spacecraft),
                contentDescription = null,
                modifier = Modifier.size(width = 60.dp, height = 44.dp),
                contentScale = ContentScale.Fit
            )
            Canvas(Modifier.height(13.dp).width(1.dp)) {
                drawLine(
                    color = Color.White.copy(alpha = .62f),
                    start = androidx.compose.ui.geometry.Offset(size.width / 2f, 0f),
                    end = androidx.compose.ui.geometry.Offset(size.width / 2f, size.height),
                    strokeWidth = 1.5f
                )
            }
            CampaignCouponBanner(campaign = campaign, onClick = { onCouponClick(campaign) })
        }
    }
}

@Composable
private fun CampaignCouponBanner(campaign: RegionalCampaign, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(166.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xF20A1427))
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(Res.drawable.pepsi_campaign_mock),
            contentDescription = campaign.brand,
            modifier = Modifier.fillMaxWidth().height(46.dp),
            contentScale = ContentScale.Crop
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.campaign_care_message),
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )
                Spacer(Modifier.width(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = Color(0xFF77B7FF),
                    modifier = Modifier.size(12.dp)
                )
                Spacer(Modifier.width(2.dp))
                Text(
                    text = stringResource(Res.string.campaign_hours_left, campaign.durationHours),
                    color = Color(0xFFB8D7FF),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
