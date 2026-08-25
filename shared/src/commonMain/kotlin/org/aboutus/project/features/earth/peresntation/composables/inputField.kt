package org.aboutus.project.features.earth.peresntation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import org.aboutus.project.core.messageColorHandler
import org.aboutus.project.features.earth.words.data.MessageState
import org.about_us.project.generated.resources.Res
import org.about_us.project.generated.resources.earth_message_placeholder

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    onSend : ()-> Unit,
    value : String ,
    onValueChange:(String)-> Unit,
    selectedState: MessageState?,
    onSelectedStateChange:(MessageState)-> Unit,
    statesModifier: Modifier = Modifier
){



    Column(
        modifier = modifier
            .fillMaxWidth()
            .imePadding()
            .navigationBarsPadding()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 10.dp)
    ) {
        LazyRow(
            modifier = statesModifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(MessageState.entries.toTypedArray()) { state ->
                val isSelected = selectedState == state
                val statColor = messageColorHandler(state)
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectedStateChange(state) },
                    label = {
                        Text(
                            text = stringResource(state.stringResId),
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = statColor.copy(.1f),
                        labelColor = statColor,
                        selectedContainerColor = statColor.copy(.1f),
                        selectedLabelColor = statColor
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = Color.Transparent,
                        selectedBorderColor = statColor
                    )
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val inputTextColor = selectedState?.let(::messageColorHandler) ?: Color(0xFF93A4BE)


            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    Text(
                        stringResource(Res.string.earth_message_placeholder),
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = inputTextColor.copy(.1f),
                    unfocusedContainerColor = Color(0xDD0D1322),
                    focusedBorderColor = inputTextColor,
                    unfocusedBorderColor = Color(0x444CC9FE),
                    focusedTextColor = inputTextColor,
                    unfocusedTextColor = inputTextColor
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = { if (selectedState != null) onSend() },
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = inputTextColor.copy(.1f)
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = inputTextColor
                )
            }
        }
    }
}
