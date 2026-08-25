package org.aboutus.project.features.auth.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.about_us.project.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.aboutus.project.core.presentation.components.*
import org.aboutus.project.features.auth.presentation.AuthUiState

@Composable
fun AnonymousAuthContent(
    state: AuthUiState, onEnter: () -> Unit, onRestore:
        () -> Unit
) = Column(
    Modifier.padding(28.dp),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Icon(
        Icons.Default.AutoAwesome,
        null, tint = AppBlue,
        modifier = Modifier.size(52.dp)
    )
    Spacer(Modifier.height(22.dp));
    Text(
        stringResource(Res.string.auth_title),
        color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.Bold
    )
    Spacer(Modifier.height(14.dp));
    Text(
        stringResource(Res.string.auth_body),
        color = AppMist, textAlign = TextAlign.Center
    )
    Spacer(Modifier.height(32.dp))
    Button(
        onEnter, Modifier.fillMaxWidth().height(54.dp),
        enabled = !state.loading, shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(AppBlue, AppInk)
    )
    {
        Text(
            stringResource(
                if (state.loading)
                    Res.string.auth_loading else Res.string.auth_enter
            ),
            fontWeight = FontWeight.Bold
        )
    }
    TextButton(onRestore) {
        Text(
            stringResource(Res.string.auth_existing),
            color = AppMist
        )
    }; if (state.error) Text(
    stringResource(Res.string.auth_error),
    color = Color(0xFFFFB4AB)
)
}
