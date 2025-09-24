package com.andresruiz.qrcraft.create_details.forms

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.andresruiz.qrcraft.R
import com.andresruiz.qrcraft.core.presentation.design_system.components.QRCraftTextInput

@Composable
fun WifiColumn(
    name: String,
    password: String,
    encryption: String,
    onNameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onEncryptionChanged: (String) -> Unit
) {
    QRCraftTextInput(
        title = stringResource(R.string.wifi_name),
        text = name,
        onTextChanged = onNameChanged
    )
    QRCraftTextInput(
        title = stringResource(R.string.wifi_password),
        text = password,
        onTextChanged = onPasswordChanged
    )
    QRCraftTextInput(
        title = stringResource(R.string.wifi_encryption),
        text = encryption,
        onTextChanged = onEncryptionChanged
    )
}