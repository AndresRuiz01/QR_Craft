package com.andresruiz.qrcraft.create_details.forms

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.andresruiz.qrcraft.R
import com.andresruiz.qrcraft.core.presentation.design_system.components.QRCraftTextInput

@Composable
fun GeolocationColumn(
    lat: String,
    long: String,
    onLatChanged: (String) -> Unit,
    onLongChanged: (String) -> Unit
) {
    QRCraftTextInput(
        title = stringResource(R.string.geolocation_lat),
        text = lat,
        onTextChanged = onLatChanged
    )
    QRCraftTextInput(
        title = stringResource(R.string.geolocation_long),
        text = long,
        onTextChanged = onLongChanged
    )
}