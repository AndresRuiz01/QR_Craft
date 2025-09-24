package com.andresruiz.qrcraft.preview

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andresruiz.qrcraft.core.domain.QRExt.Companion.createScaledQrBitmap
import com.andresruiz.qrcraft.core.domain.QRExt.Companion.generateQRCodeData
import com.andresruiz.qrcraft.core.domain.models.QR
import com.andresruiz.qrcraft.core.domain.repositories.IQRRepository
import com.andresruiz.qrcraft.core.presentation.ui.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface QRPreviewAction {
    data class ToggleFavorite(val qr: QR): QRPreviewAction
    data class UpdateTitle(val title: String): QRPreviewAction
    data object DownloadQR: QRPreviewAction

}

class QRPreviewViewModel(
    private val qrRepository: IQRRepository,
    private val application: Application,
    private val applicationScope: CoroutineScope,
    val id: Long,
): ViewModel() {

    private val _uiState = MutableStateFlow<UiState<QRPreviewState>>(UiState.Loading)
    val uiState: StateFlow<UiState<QRPreviewState>> = _uiState
        .asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )

    init {
        observeQR()
    }

    fun onAction(action: QRPreviewAction) {
        when(action) {
            is QRPreviewAction.ToggleFavorite -> {
                viewModelScope.launch(Dispatchers.Default) {
                    qrRepository.upsertQR(action.qr.copy(isFavorite = !action.qr.isFavorite))
                }
            }

            is QRPreviewAction.UpdateTitle -> {
                val state = _uiState.value
                if (state is UiState.Success) {
                    // update local state
                    _uiState.update {
                        state.copy(
                            state = state.state.copy(title = action.title)
                        )
                    }

                    // Update DB as well
                    viewModelScope.launch {
                        qrRepository.upsertQR(state.state.qr.copy(title = action.title))
                    }
                }
            }

            is QRPreviewAction.DownloadQR -> {
                // run it on app scope in case the user leaves the screen
                applicationScope.launch {
                    // generate QR data
                    val state = _uiState.value
                    if (state is UiState.Success) {
                        state.state.qr.generateQRCodeData()?.let { qrData ->
                            // create scaled bitmap
                            createScaledQrBitmap(qrData)?.let { bitmap ->
                                // save to downloads
                                saveBitmapToDownloads(
                                    context = application,
                                    bitmap = bitmap,
                                    displayName = "qrcraft_${state.state.qr.title}_${System.currentTimeMillis()}"
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun observeQR() {
        viewModelScope.launch(Dispatchers.Default) {
            qrRepository.observeQR(id).collect { qr ->
                if (qr == null) {
                    _uiState.value = UiState.Error
                } else {
                    _uiState.value = UiState.Success(
                        QRPreviewState(
                            qr = qr,
                            title = qr.title
                        )
                    )
                }
            }
        }
    }

}