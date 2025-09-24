package com.andresruiz.qrcraft.preview

import android.content.ClipData
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andresruiz.qrcraft.R
import com.andresruiz.qrcraft.core.domain.QRExt.Companion.generateQRCodeData
import com.andresruiz.qrcraft.core.domain.QRExt.Companion.getQRCodeBitmap
import com.andresruiz.qrcraft.core.domain.QRTypeExt.Companion.title
import com.andresruiz.qrcraft.core.domain.models.QRType
import com.andresruiz.qrcraft.core.presentation.design_system.onOverlay
import com.andresruiz.qrcraft.core.presentation.design_system.qrLinkBg
import com.andresruiz.qrcraft.core.presentation.design_system.surfaceHigher
import com.andresruiz.qrcraft.core.presentation.ui.ChangeStatusBarColors
import com.andresruiz.qrcraft.core.presentation.ui.UiState
import com.andresruiz.qrcraft.core.presentation.ui.UiText
import com.andresruiz.qrcraft.preview.components.ActionButton
import com.andresruiz.qrcraft.preview.components.SmallActionButton
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf


@Composable
fun QRPreviewScreenRoot(
    title: UiText,
    id: Long,
    onNavigateBack: () -> Unit,
    viewModel: QRPreviewViewModel = koinViewModel<QRPreviewViewModel>(
        parameters = {
            parametersOf(id)
        },
        key = "QRPreviewViewModel_$id",
    )
) {

    ChangeStatusBarColors(false)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when(uiState) {
        is UiState.Success -> {
            QRPreviewScreen(
                title = title,
                state = (uiState as UiState.Success).state,
                onAction = viewModel::onAction,
                onNavigateBack = onNavigateBack
            )
        }
        is UiState.Error -> {}
        is UiState.Loading -> {}
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRPreviewScreen(
    title: UiText,
    state: QRPreviewState,
    onAction: (QRPreviewAction) -> Unit,
    onNavigateBack: () -> Unit,
) {

    val context = LocalContext.current
    val clipboard = LocalClipboard.current
    val coroutineScope = rememberCoroutineScope()

    state.qr.generateQRCodeData()?.let {
        state.qr.getQRCodeBitmap(it)
    }

    val qrCodeBitmap: ImageBitmap? = remember(state.qr) {
        val qrData = state.qr.generateQRCodeData()
        if (qrData != null) {
            state.qr.getQRCodeBitmap(qrData)?.asImageBitmap()
        } else {
            null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title.asString(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onOverlay
                        ),
                        textAlign = TextAlign.Center,
                         modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onOverlay,
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = null
                        ) {
                            onNavigateBack()
                        }
                    )
                },
                actions = {
                    Icon(
                        painter = painterResource(if (state.qr.isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star),
                        contentDescription = null,
                        tint = if(state.qr.isFavorite) MaterialTheme.colorScheme.surfaceHigher else Color.Unspecified,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                indication = null,
                                interactionSource = null
                            ) {
                                onAction(QRPreviewAction.ToggleFavorite(state.qr))
                            }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        },
        containerColor = MaterialTheme.colorScheme.onSurface
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 124.dp)
                .padding(horizontal = 16.dp)
        ) {
            // Title / Content / Action Buttons
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                val scrollState = rememberScrollState()
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .padding(top = 80.dp)
                        .padding(top = 20.dp)
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                        .verticalScroll(scrollState)
                ) {
                    var isFocused by remember { mutableStateOf(false) }
                    BasicTextField(
                        value = state.title,
                        onValueChange = {
                            onAction(QRPreviewAction.UpdateTitle(it))
                        },
                        textStyle = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        ),
                        singleLine = true,
                        decorationBox = {
                            if (state.title.isEmpty() && !isFocused) {
                                Text(
                                    text = state.qr.qrType.title.asString(),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurface
                                    ),
                                    modifier = Modifier
                                )
                            } else {
                                it()
                            }
                        },
                        modifier = Modifier
                            .onFocusChanged { focusState ->
                                isFocused = focusState.isFocused
                            }
                    )

                    Text(
                        text = state.qr.displayString,
                        textAlign = if (state.qr.qrType == QRType.TEXT) TextAlign.Start else TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            background = if (state.qr.qrType == QRType.LINK) MaterialTheme.colorScheme.qrLinkBg else MaterialTheme.typography.bodyLarge.background
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(state.qr.qrType == QRType.LINK) {
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    data = state.qr.displayString.toUri()
                                }
                                context.startActivity(intent)
                            }
                    )

                    Spacer(modifier = Modifier.height(0.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SmallActionButton(
                            icon = R.drawable.ic_share,
                            onClick = {
                                // Create an Intent with the ACTION_SEND action
                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, state.qr.displayString)
                                }

                                // Create a chooser for the user to select an app
                                val shareIntent = Intent.createChooser(sendIntent, null)

                                // Launch the intent
                                context.startActivity(shareIntent)
                            },
                            modifier = Modifier
                                .size(44.dp)
                        )
                        SmallActionButton(
                            icon = R.drawable.ic_copy,
                            onClick = {
                                coroutineScope.launch {
                                    clipboard.setClipEntry(
                                        ClipEntry(
                                            ClipData.newPlainText(
                                                "label",
                                                state.qr.displayString
                                            )
                                        )
                                    )
                                }
                            },
                            modifier = Modifier
                                .size(44.dp)
                        )
                        ActionButton(
                            leadingIcon = R.drawable.ic_save,
                            text = stringResource(R.string.save_to_downloads),
                            onClick = {
                                onAction(QRPreviewAction.DownloadQR)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                        )
                    }
                }
            }

            // QR Code
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(160.dp)
                    .offset(y = (-80).dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceHigher)
            ) {
                if (qrCodeBitmap != null) {
                    Image(
                        bitmap = qrCodeBitmap,
                        contentDescription = null,
                        filterQuality = FilterQuality.None,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                    )
                }
            }
        }
    }
}