package com.andresruiz.qrcraft.create_details.forms

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.andresruiz.qrcraft.R
import com.andresruiz.qrcraft.core.presentation.design_system.components.QRCraftTextInput

@Composable
fun TextColumn(
    text: String,
    onTextChanged: (String) -> Unit
) {
    QRCraftTextInput(
        title = stringResource(R.string.text),
        text = text,
        onTextChanged = onTextChanged
    )
}