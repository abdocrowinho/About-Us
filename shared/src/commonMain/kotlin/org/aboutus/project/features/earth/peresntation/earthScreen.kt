package org.aboutus.project.features.earth.peresntation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.geometry.Rect
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.aboutus.project.core.feelingsStateHandler
import org.aboutus.project.core.messageColorHandler
import org.aboutus.project.core.presentation.components.AboutUsTopBar
import org.aboutus.project.features.earth.domain.GeoUtils
import org.aboutus.project.features.earth.words.EarthWordsViewModel
import org.aboutus.project.features.earth.peresntation.composables.InputField
import org.aboutus.project.features.earth.peresntation.composables.Interactive3DMap
import org.aboutus.project.features.earth.peresntation.composables.TopStatsBar
import org.aboutus.project.features.earth.peresntation.composables.GlobeScopeToggle
import org.aboutus.project.features.earth.peresntation.composables.EarthAppTourOverlay
import org.aboutus.project.features.earth.peresntation.composables.EarthStatusBanner
import org.aboutus.project.features.earth.peresntation.composables.EarthTourTarget
import org.aboutus.project.features.about_us.peresntation.GlobeScope
import org.about_us.project.generated.resources.Res
import org.about_us.project.generated.resources.common_done
import org.about_us.project.generated.resources.earth_message_details_title
import org.about_us.project.generated.resources.earth_message_state_label
import org.about_us.project.generated.resources.region_mood_first
import org.about_us.project.generated.resources.region_mood_second
import org.about_us.project.generated.resources.earth_no_feelings_here
import org.about_us.project.generated.resources.world_mood_first
import org.about_us.project.generated.resources.world_mood_second

import org.aboutus.project.features.earth.wave.presentation.VerticalWaveComponent
import org.aboutus.project.features.earth.wave.presentation.WaveViewModel
import org.aboutus.project.features.earth.words.FloatingWord
import org.aboutus.project.features.earth.words.ScatteredWordsOverlay
import org.aboutus.project.features.earth.words.data.MessageState
import org.aboutus.project.features.campaign.data.CampaignCatalog
import org.aboutus.project.features.campaign.domain.CampaignEligibility
import org.aboutus.project.features.campaign.domain.CampaignSignal
import org.aboutus.project.features.campaign.presentation.CampaignFlightOverlay

@Composable
fun MapScreen(
    viewModel: Map3DViewModel = koinViewModel(),
    waveViewModel: WaveViewModel = koinViewModel(),
    wordsViewModel: EarthWordsViewModel = koinViewModel(),
    showAppTour: Boolean = false,
    onAppTourComplete: () -> Unit = {}
) {
    var selectedWordForDialog by remember { mutableStateOf<FloatingWord?>(null) }
    var appTourVisible by remember(showAppTour) { mutableStateOf(showAppTour) }
    val state by viewModel.state.collectAsState()
    val waveState by waveViewModel.state.collectAsState()

    val words by wordsViewModel.wordsState.collectAsState()

    var selectedState by remember { mutableStateOf<MessageState?>(null) }
    var inputText by remember { mutableStateOf("") }
    var tourTargets by remember { mutableStateOf<Map<EarthTourTarget, Rect>>(emptyMap()) }
    val captureTourTarget: (EarthTourTarget) -> Modifier = { target ->
        Modifier.onGloballyPositioned { coordinates ->
            tourTargets = tourTargets + (target to coordinates.boundsInRoot())
        }
    }
    val scopeCountryCode = if (state.scope == GlobeScope.SELECTED_COUNTRY) {
        state.selectedCountryCode
    } else {
        null
    }
    val activeCampaign = remember(scopeCountryCode, waveState.globalStats) {
        CampaignEligibility.activeCampaign(
            campaign = CampaignCatalog.pepsiEgypt,
            viewerCountryCode = scopeCountryCode,
            signals = waveState.globalStats.map { stat ->
                CampaignSignal(
                    countryCode = stat.country_code,
                    state = stat.state,
                    count = stat.count
                )
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050811))
    ) {
        Interactive3DMap(
            state = state,
            countryStats = waveState.globalStats
        )

        ScatteredWordsOverlay(
            words = words,
            onWordClick = { clickedWord ->
                selectedWordForDialog = clickedWord
            },
            modifier = Modifier.fillMaxSize(),
            onDrag = viewModel::onDrag
        )

        selectedWordForDialog?.let { word ->
                AlertDialog(
                    onDismissRequest = { selectedWordForDialog = null },
                    confirmButton = {
                        TextButton(onClick = { selectedWordForDialog = null }) {
                            Text(
                                stringResource(Res.string.common_done),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF73B4FF)
                            )
                        }
                    },
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(Res.string.earth_message_details_title),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )

                            Surface(
                                color = Color(0xFF1E293B),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = " ${word.countryCode}",
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    },
                    text = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = word.fullSentence,
                                fontSize = 16.sp,
                                color = Color(0xFFF4F8FF),
                                lineHeight = 22.sp
                            )

                            HorizontalDivider(color = Color(0xFF73B4FF).copy(alpha = 0.22f))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = stringResource(Res.string.earth_message_state_label),
                                    fontSize = 13.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = stringResource(feelingsStateHandler(word.state)),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = messageColorHandler(word.state.toInt())
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(20.dp),
                    containerColor = Color(0xFF071323),
                    titleContentColor = Color.White,
                    textContentColor = Color(0xFFF4F8FF)
                )
        }
        LaunchedEffect(scopeCountryCode) {
            wordsViewModel.setScope(scopeCountryCode)
            waveViewModel.fetchWaveStatsForCountry(scopeCountryCode)
        }
        AboutUsTopBar(
            modifier = Modifier.align(Alignment.TopCenter)
        )
        TopStatsBar(
            stats = waveState.stats,
            totalVotes = waveState.totalVotes,
            participatingCountries = waveState.participatingCountries,
            error = waveState.statsError,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 58.dp)
                .then(captureTourTarget(EarthTourTarget.STATS))
        )

        activeCampaign?.let { campaign ->
            CampaignFlightOverlay(
                campaign = campaign,
                onCouponClick = { /* Coupon claiming is added with the server-side campaign flow. */ },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 150.dp)
            )
        }

        Column(
            modifier = Modifier
                .align(AbsoluteAlignment.CenterLeft)
                .padding(start = 8.dp)
                .then(captureTourTarget(EarthTourTarget.WAVE)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (waveState.totalVotes == 0L) {
                Text(
                    text = stringResource(Res.string.earth_no_feelings_here),
                    color = Color(0xFF93A4BE),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            } else waveState.dominantState?.let { stateValue ->
                val dominant = MessageState.fromValue(stateValue)
                Text(
                    text = stringResource(
                        if (scopeCountryCode == null) Res.string.world_mood_first else Res.string.region_mood_first
                    ),
                    color = waveState.waveCase,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = stringResource(
                        if (scopeCountryCode == null) Res.string.world_mood_second else Res.string.region_mood_second,
                        stringResource(dominant.stringResId)
                    ),
                    color = waveState.waveCase,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            if (waveState.totalVotes > 0L) {
                VerticalWaveComponent(state = waveState)
            }
        }

        GlobeScopeToggle(
            scope = state.scope,
            countryName = state.selectedCountryName,
            onScopeChange = viewModel::setScope,
            modifier = Modifier
                .align(AbsoluteAlignment.TopRight)
                .padding(top = 154.dp, end = 16.dp)
                .then(captureTourTarget(EarthTourTarget.SCOPE))
        )

        Canvas(
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.Center)
                .then(captureTourTarget(EarthTourTarget.TRUTH))
        ) {
            val center = Offset(size.width / 2f, size.height / 2f)

            drawCircle(
                color = Color(0xFFFF4D4D).copy(alpha = 0.3f),
                radius = size.width / 2f,
                center = center
            )

            drawCircle(
                color = Color(0xFFFF4D4D),
                radius = 5f,
                center = center
            )
        }

        state.selectedCountryName?.let { countryName ->
            Card(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 218.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xCC000000)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = countryName,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                )
            }
        }

        InputField(
            modifier = Modifier.align(Alignment.BottomCenter),
            value = inputText ,
            onValueChange = {value->
                inputText = value
            },
            onSend =  {
                val latlng = GeoUtils.getCenterLatLng(
                    rotationX = state.rotationX ,
                    rotationY = state.rotationY
                )
                if (inputText.isNotBlank()) {
                    wordsViewModel.sendSentenceStream(
                        fullText = inputText,
                        state = selectedState ?: return@InputField,
                        countryCode = scopeCountryCode ?: "GLOBAL",
                        lat = latlng.latitude.toDouble(),
                        lng = latlng.longitude.toDouble()
                    )
                    inputText = ""

                }
            },
            selectedState = selectedState,
            onSelectedStateChange = {state ->
                selectedState = state
            },
            statesModifier = captureTourTarget(EarthTourTarget.STATES)
        )

        EarthStatusBanner(
            isLoading = waveState.isLoading,
            error = waveState.statsError,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        if (appTourVisible) {
            EarthAppTourOverlay(
                targetBounds = tourTargets,
                onComplete = {
                    appTourVisible = false
                    onAppTourComplete()
                }
            )
        }


    }
}
