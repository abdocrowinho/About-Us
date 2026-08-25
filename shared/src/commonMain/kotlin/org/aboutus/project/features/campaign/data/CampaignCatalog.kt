package org.aboutus.project.features.campaign.data

import org.aboutus.project.features.campaign.domain.RegionalCampaign

object CampaignCatalog {
    // Mock configuration. Production campaigns will come from a server-side source.
    val pepsiEgypt = RegionalCampaign(
        id = "pepsi-egypt-care-v1",
        countryCode = "EG",
        triggerState = "ANGER",
        minimumShare = .50f,
        minimumVotes = 100L,
        durationHours = 1,
        brand = "Pepsi"
    )
}
