package com.andresruiz.qrcraft.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andresruiz.qrcraft.core.domain.models.QR
import com.andresruiz.qrcraft.core.domain.repositories.IQRRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface HistoryAction {
    data class QRLongPress(val id: Long) : HistoryAction
    data object DeleteQR : HistoryAction
    data class ToggleFavorite(val qr: QR) : HistoryAction
    data object DismissModal : HistoryAction
}

data class HistoryState(
    val scannedQRs: List<QR> = emptyList(),
    val generatedQRs: List<QR> = emptyList(),
    val currentTab: Int = 0,
    val currentQR: Long? = null,
)


class HistoryViewModel(
    private val qrRepository: IQRRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state = _state
        .asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HistoryState()
        )

    init {
        observeScannedQRs()
        observeGeneratedQRs()
    }

    fun onAction(action: HistoryAction) {
        when (action) {
            is HistoryAction.QRLongPress -> {
                _state.update {
                    it.copy(
                        currentQR = action.id
                    )
                }
            }

            is HistoryAction.DeleteQR -> {
                _state.value.currentQR?.let { qrId ->
                    viewModelScope.launch {
                        qrRepository.deleteQR(qrId)
                        _state.update {
                            it.copy(
                                currentQR = null
                            )
                        }
                    }
                }
            }

            is HistoryAction.ToggleFavorite -> {
                viewModelScope.launch {
                    qrRepository.upsertQR(
                        action.qr.copy(
                            isFavorite = !action.qr.isFavorite
                        )
                    )
                }
            }

            HistoryAction.DismissModal -> {
                _state.update {
                    it.copy(
                        currentQR = null
                    )
                }
            }
        }
    }

    private fun observeScannedQRs() {
        viewModelScope.launch {
            qrRepository.observeScannedQRs().collect { scannedQRs ->
                _state.update {
                    it.copy(
                        scannedQRs = scannedQRs
                    )
                }
            }
        }
    }

    private fun observeGeneratedQRs() {
        viewModelScope.launch {
            qrRepository.observeGeneratedQRs().collect { generatedQRs ->
                _state.update {
                    it.copy(
                        generatedQRs = generatedQRs
                    )
                }
            }
        }
    }
}