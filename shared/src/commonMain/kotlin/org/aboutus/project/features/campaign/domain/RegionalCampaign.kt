package org.aboutus.project.features.campaign.domain

data class RegionalCampaign(
    val id: String,
    val countryCode: String,
    val triggerState: String,
    val minimumShare: Float,
    val minimumVotes: Long,
    val durationHours: Int,
    val brand: String
)

/** A small, feature-owned representation of a state count used for eligibility. */
data class CampaignSignal(
    val countryCode: String,
    val state: String,
    val count: Long
)

object CampaignEligibility {
    fun activeCampaign(
        campaign: RegionalCampaign,
        viewerCountryCode: String?,
        signals: List<CampaignSignal>
    ): RegionalCampaign? {
        if (!campaign.countryCode.equals(viewerCountryCode, ignoreCase = true)) return null

        val countrySignals = signals.filter { signal ->
            signal.state != "ALL" && signal.countryCode.equals(campaign.countryCode, ignoreCase = true)
        }
        val totalVotes = countrySignals.sumOf { it.count }
        val stateVotes = countrySignals
            .filter { it.state.equals(campaign.triggerState, ignoreCase = true) }
            .sumOf { it.count }
        val share = if (totalVotes == 0L) 0f else stateVotes.toFloat() / totalVotes

        return campaign.takeIf {
            totalVotes >= it.minimumVotes && share >= it.minimumShare
        }
    }
}
