package com.andresruiz.qrcraft.create_details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andresruiz.qrcraft.core.domain.QRTypeExt.Companion.title
import com.andresruiz.qrcraft.core.domain.models.QRType
import com.andresruiz.qrcraft.core.presentation.design_system.components.QRCraftFormButton
import com.andresruiz.qrcraft.core.presentation.design_system.surfaceHigher
import com.andresruiz.qrcraft.core.presentation.ui.ChangeStatusBarColors
import com.andresruiz.qrcraft.core.presentation.ui.ObserveAsEvents
import com.andresruiz.qrcraft.create_details.domain.QRForm
import com.andresruiz.qrcraft.create_details.forms.ContactColumn
import com.andresruiz.qrcraft.create_details.forms.GeolocationColumn
import com.andresruiz.qrcraft.create_details.forms.LinkColumn
import com.andresruiz.qrcraft.create_details.forms.PhoneNumberColumn
import com.andresruiz.qrcraft.create_details.forms.TextColumn
import com.andresruiz.qrcraft.create_details.forms.WifiColumn
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf


@Composable
fun CreateQRDetailsScreenRoot(
    qrType: QRType,
    onNavigateBack: () -> Unit,
    onCreateQR: (Long) -> Unit,
    viewModel: CreateQRDetailViewModel = koinViewModel<CreateQRDetailViewModel>(
        parameters = {
            parametersOf(qrType)
        },
        key = qrType.name
    )
) {

    ChangeStatusBarColors(true)

    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when(event) {
            is CreateEvent.OnQRCreated -> {
                onCreateQR(event.id)
            }
        }
    }

    CreateQRDetailsScreen(
        qrType = qrType,
        state = state,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack,
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQRDetailsScreen(
    qrType: QRType,
    state: CreateQRDetailState,
    onAction: (CreateQRDetailAction) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = qrType.title.asString(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = null
                        ) {
                            onNavigateBack()
                        }
                    )
                },
                modifier = Modifier.padding(start = 16.dp, end = 32.dp)
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceHigher)
                .padding(16.dp)
        ) {
            when(state.qrForm) {
                is QRForm.Text -> {
                    TextColumn(
                        text = state.qrForm.text,
                        onTextChanged = { onAction(CreateQRDetailAction.OnQRTextChanged(it)) }
                    )
                }
                is QRForm.Link -> {
                    LinkColumn(
                        link = state.qrForm.url,
                        onLinkChanged = { onAction(CreateQRDetailAction.OnQRLinkChanged(it)) }
                    )
                }
                is QRForm.Contact -> {
                    ContactColumn(
                        name = state.qrForm.name,
                        email = state.qrForm.email,
                        phone = state.qrForm.phoneNumber,
                        onNameChanged = { onAction(CreateQRDetailAction.OnQRContactNameChanged(it)) },
                        onEmailChanged = { onAction(CreateQRDetailAction.OnQRContactEmailChanged(it)) },
                        onPhoneChanged = { onAction(CreateQRDetailAction.OnQRContactPhoneChanged(it)) }
                    )
                }
                is QRForm.Geolocation -> {
                    GeolocationColumn(
                        lat = state.qrForm.latitude,
                        long = state.qrForm.longitude,
                        onLatChanged = { onAction(CreateQRDetailAction.OnQRGeoLatChanged(it)) },
                        onLongChanged = { onAction(CreateQRDetailAction.OnQRGeoLongChanged(it)) }
                    )
                }
                is QRForm.PhoneNumber -> {
                    PhoneNumberColumn(
                        number = state.qrForm.number,
                        onNumberChanged = { onAction(CreateQRDetailAction.OnQRPhoneNumberChanged(it)) }
                    )
                }
                is QRForm.Wifi -> {
                    WifiColumn(
                        name = state.qrForm.ssid,
                        password = state.qrForm.psk,
                        encryption = state.qrForm.encryption,
                        onNameChanged = { onAction(CreateQRDetailAction.OnQRWifiNameChanged(it)) },
                        onPasswordChanged = { onAction(CreateQRDetailAction.OnQRWifiPasswordChanged(it)) },
                        onEncryptionChanged = { onAction(CreateQRDetailAction.OnQRWifiEncryptionChanged(it)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(0.dp))

            QRCraftFormButton(
                onClick = { onAction(CreateQRDetailAction.OnQRCreate) },
                isEnabled = state.isFormValid,
            )
        }
    }
}