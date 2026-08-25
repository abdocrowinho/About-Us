package org.aboutus.project.core

import androidx.compose.ui.graphics.Color
import org.aboutus.project.features.earth.words.data.MessageState
import org.aboutus.project.features.earth.words.data.MessageState.*

fun messageColorHandler(state: MessageState): Color{
   return when(state){
        ANGER -> Color(0xFFE05252)
        FEAR -> Color(0xFF8667C9)
        DISGUST -> Color(0xFF8BAA4F)
        HOPE -> Color(0xFFFFC857)
        NONSENSE -> Color(0xFF8A94A6)
        FUN -> Color(0xFFFF8A65)
        HUNGER -> Color(0xFFC98B54)
        SHAME -> Color(0xFF65748E)
        LOVE -> Color(0xFFE85D93)
        DISTRACTION -> Color(0xFF3DA9C7)
        SAD -> Color(0xFF3B82F6)
    }
}
fun messageColorHandler(state: Int): Color {
    return when (state) {
        1 -> messageColorHandler(ANGER)
        2 -> messageColorHandler(FEAR)
        3 -> messageColorHandler(DISGUST)
        4 -> messageColorHandler(HOPE)
        5 -> messageColorHandler(NONSENSE)
        6 -> messageColorHandler(FUN)
        7 -> messageColorHandler(HUNGER)
        8 -> messageColorHandler(SHAME)
        9 -> messageColorHandler(LOVE)
        10 -> messageColorHandler(DISTRACTION)
        11 -> messageColorHandler(SAD)
        else -> Color.White
    }
}



 fun mapStateToWaveCase(stateStr: String): Color {
    return when (stateStr.uppercase()) {
        "ANGER", "1" -> messageColorHandler(ANGER)
        "FEAR", "2" -> messageColorHandler(FEAR)
        "DISGUST", "3" -> messageColorHandler(DISGUST)
        "HOPE", "4" -> messageColorHandler(HOPE)
        "NONSENSE", "5" -> messageColorHandler(NONSENSE)
        "FUN", "6" -> messageColorHandler(FUN)
        "HUNGER", "7" -> messageColorHandler(HUNGER)
        "SHAME", "8" -> messageColorHandler(SHAME)
        "LOVE", "9" -> messageColorHandler(LOVE)
        "DISTRACTION", "10" -> messageColorHandler(DISTRACTION)
        "SAD", "11" -> messageColorHandler(SAD)
        else -> Color.White
    }
}







