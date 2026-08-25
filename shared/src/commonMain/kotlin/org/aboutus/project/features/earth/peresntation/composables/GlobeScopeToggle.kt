package org.aboutus.project.features.earth.peresntation.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Public
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.aboutus.project.features.about_us.peresntation.GlobeScope

@Composable
fun GlobeScopeToggle(
    scope: GlobeScope,
    countryName: String?,
    onScopeChange: (GlobeScope) -> Unit,
    modifier: Modifier = Modifier
) {
    val isGlobal = scope == GlobeScope.GLOBAL
    FilterChip(
        modifier = modifier,
        selected = isGlobal,
        enabled = isGlobal || countryName != null,
        onClick = {
            onScopeChange(if (isGlobal) GlobeScope.SELECTED_COUNTRY else GlobeScope.GLOBAL)
        },
        label = {
            if (isGlobal) {
                Icon(
                    imageVector = Icons.Default.Public,
                    contentDescription = "العالم",
                    modifier = Modifier.padding(horizontal = 2.dp)
                )
            } else {
                Text(countryName ?: "العالم", fontSize = 12.sp)
            }
        },
        shape = RoundedCornerShape(12.dp),
        colors = scopeColors()
    )
}

@Composable
private fun scopeColors() = FilterChipDefaults.filterChipColors(
    containerColor = Color.Transparent,
    labelColor = Color(0xFFBAC5D6),
    selectedContainerColor = Color(0xFF243752),
    selectedLabelColor = Color.White
)
