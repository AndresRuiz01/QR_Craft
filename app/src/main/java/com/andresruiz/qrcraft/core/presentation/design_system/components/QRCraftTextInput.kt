package com.andresruiz.qrcraft.core.presentation.design_system.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andresruiz.qrcraft.core.presentation.design_system.QRCraftTheme
import com.andresruiz.qrcraft.core.presentation.design_system.onSurfaceAlt

@Composable
fun QRCraftTextInput(
    title: String,
    text: String,
    onTextChanged: (String) -> Unit,
) {
    var isFocused by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 44.dp, max = 200.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(MaterialTheme.colorScheme.surface)
    ) {
        BasicTextField(
            value = text,
            textStyle = MaterialTheme.typography.bodyLarge,
            onValueChange = onTextChanged,
            decorationBox = { content ->
                if(text.isEmpty() && !isFocused) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurfaceAlt
                        )
                    )
                } else {
                    content()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                }
        )
    }
}


@Preview
@Composable
private fun QRCraftTextInputPreview() {
    QRCraftTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            QRCraftTextInput(
                title = "Text",
                text = "",
                onTextChanged = {},
            )
            QRCraftTextInput(
                title = "Text",
                text = "hello world",
                onTextChanged = {},
            )
        }
    }
}