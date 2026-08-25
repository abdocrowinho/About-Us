package org.aboutus.project.features.splash.presentation.components


import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.about_us.project.generated.resources.Res
import org.about_us.project.generated.resources.splash_tagline
import org.jetbrains.compose.resources.stringResource
import org.aboutus.project.core.presentation.components.AppMist
import org.aboutus.project.core.presentation.components.AboutUsBrand

@Composable
fun SplashBrand() {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(18.dp))
        AboutUsBrand(fontSize = 30)
        Text(stringResource(Res.string.splash_tagline), color = AppMist)
    }
}
