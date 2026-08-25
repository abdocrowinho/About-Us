package org.aboutus.project.features.earth.words.data

import kotlinx.serialization.Serializable
import org.about_us.project.generated.resources.Res
import org.about_us.project.generated.resources.state_anger
import org.about_us.project.generated.resources.state_disgust
import org.about_us.project.generated.resources.state_distraction
import org.about_us.project.generated.resources.state_fear
import org.about_us.project.generated.resources.state_fun
import org.about_us.project.generated.resources.state_hunger
import org.about_us.project.generated.resources.state_hope
import org.about_us.project.generated.resources.state_love
import org.about_us.project.generated.resources.state_nonsense
import org.about_us.project.generated.resources.state_sad
import org.about_us.project.generated.resources.state_shame
import org.jetbrains.compose.resources.StringResource

@Serializable
data class WordPayload(
    val id: String,
    val text: String,
    val state: MessageState,
    val fullSentence: String,
    val countryCode: String,
    val lat: Double,
    val lng: Double,
    val timestamp: Long
)


@Serializable
enum class MessageState(
    val value: String,
    val number: Int,
    val stringResId: StringResource
) {
    ANGER("ANGER", 1, Res.string.state_anger),
    FEAR("FEAR", 2, Res.string.state_fear),
    DISGUST("DISGUST", 3, Res.string.state_disgust),
    HOPE("HOPE", 4, Res.string.state_hope),
    NONSENSE("NONSENSE", 5, Res.string.state_nonsense),
    FUN("FUN", 6, Res.string.state_fun),
    HUNGER("HUNGER", 7, Res.string.state_hunger),
    SHAME("SHAME", 8, Res.string.state_shame),
    LOVE("LOVE", 9, Res.string.state_love),
    DISTRACTION("DISTRACTION", 10, Res.string.state_distraction),
    SAD("SAD", 11, Res.string.state_sad);

    companion object {
        fun fromValue(value: String): MessageState {
            return entries.find { it.value.equals(value, ignoreCase = true) }
                ?: when (value.uppercase()) {
                    "ONE", "1" -> ANGER
                    "TWO", "2" -> FEAR
                    "THREE", "3" -> DISGUST
                    "FOUR", "4" -> HOPE
                    "FIVE", "5" -> NONSENSE
                    "SIX", "6" -> FUN
                    "SEVEN", "7" -> HUNGER
                    "EIGHT", "8" -> SHAME
                    "NINE", "9" -> LOVE
                    "TEN", "10" -> DISTRACTION
                    "ELEVEN", "11" -> SAD
                    else -> ANGER
                }
        }
    }
}
