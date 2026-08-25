package org.aboutus.project.core

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
import org.about_us.project.generated.resources.state_shame
import org.about_us.project.generated.resources.state_sad
import org.jetbrains.compose.resources.StringResource

fun feelingsStateHandler(state:String): StringResource {
    val stateToInt = state.toInt()
    return when (stateToInt) {
        1 -> Res.string.state_anger
        2 -> Res.string.state_fear
        3 -> Res.string.state_disgust
        4 -> Res.string.state_hope
        5 -> Res.string.state_nonsense
        6 -> Res.string.state_fun
        7 -> Res.string.state_hunger
        8 -> Res.string.state_shame
        9 -> Res.string.state_love
        10 -> Res.string.state_distraction
        11 -> Res.string.state_sad
        else -> Res.string.state_anger
    }
}
