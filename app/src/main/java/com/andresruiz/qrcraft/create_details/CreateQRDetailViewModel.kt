package com.andresruiz.qrcraft.create_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andresruiz.qrcraft.core.domain.models.QR
import com.andresruiz.qrcraft.core.domain.models.QRType
import com.andresruiz.qrcraft.core.domain.repositories.IQRRepository
import com.andresruiz.qrcraft.create_details.domain.QRForm
import com.andresruiz.qrcraft.create_details.domain.QRFormMapper
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class CreateQRDetailViewModel(
    val qrType: QRType,
    val qrRepository: IQRRepository,
) : ViewModel() {

    private val eventChannel = Channel<CreateEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _state = MutableStateFlow(CreateQRDetailState(qrForm = getDefaultFormFromQRType(qrType)))
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CreateQRDetailState(qrForm = getDefaultFormFromQRType(qrType))
        )

    fun onAction(action: CreateQRDetailAction) {
        when(action) {
            is CreateQRDetailAction.OnQRTextChanged -> {
                val state = _state.value
                if(state.qrForm is QRForm.Text) {
                    _state.update {
                        state.copy(
                            qrForm = state.qrForm.copy(
                                text = action.text
                            )
                        )
                    }
                }
            }

            is CreateQRDetailAction.OnQRLinkChanged -> {
                val state = _state.value
                if(state.qrForm is QRForm.Link) {
                    _state.update {
                        state.copy(
                            qrForm = state.qrForm.copy(
                                url = action.url
                            )
                        )
                    }
                }
            }

            is CreateQRDetailAction.OnQRContactEmailChanged -> {
                val state = _state.value
                if(state.qrForm is QRForm.Contact) {
                    _state.update {
                        state.copy(
                            qrForm = state.qrForm.copy(
                                email = action.email
                            )
                        )
                    }
                }
            }

            is CreateQRDetailAction.OnQRContactNameChanged -> {
                val state = _state.value
                if(state.qrForm is QRForm.Contact) {
                    _state.update {
                        state.copy(
                            qrForm = state.qrForm.copy(
                                name = action.name
                            )
                        )
                    }
                }
            }

            is CreateQRDetailAction.OnQRContactPhoneChanged -> {
                val state = _state.value
                if(state.qrForm is QRForm.Contact) {
                    _state.update {
                        state.copy(
                            qrForm = state.qrForm.copy(
                                phoneNumber = action.phone
                            )
                        )
                    }
                }
            }

            is CreateQRDetailAction.OnQRGeoLatChanged -> {
                val state = _state.value
                if(state.qrForm is QRForm.Geolocation) {
                    _state.update {
                        state.copy(
                            qrForm = state.qrForm.copy(
                                latitude = action.lat
                            )
                        )
                    }
                }
            }
            is CreateQRDetailAction.OnQRGeoLongChanged -> {
                val state = _state.value
                if(state.qrForm is QRForm.Geolocation) {
                    _state.update {
                        state.copy(
                            qrForm = state.qrForm.copy(
                                longitude = action.long
                            )
                        )
                    }
                }
            }
            is CreateQRDetailAction.OnQRPhoneNumberChanged -> {
                val state = _state.value
                if(state.qrForm is QRForm.PhoneNumber) {
                    _state.update {
                        state.copy(
                            qrForm = state.qrForm.copy(
                                number = action.number
                            )
                        )
                    }
                }
            }
            is CreateQRDetailAction.OnQRWifiEncryptionChanged -> {
                val state = _state.value
                if(state.qrForm is QRForm.Wifi) {
                    _state.update {
                        state.copy(
                            qrForm = state.qrForm.copy(
                                encryption = action.encryption
                            )
                        )
                    }
                }
            }
            is CreateQRDetailAction.OnQRWifiNameChanged -> {
                val state = _state.value
                if(state.qrForm is QRForm.Wifi) {
                    _state.update {
                        state.copy(
                            qrForm = state.qrForm.copy(
                                ssid = action.name
                            )
                        )
                    }
                }
            }
            is CreateQRDetailAction.OnQRWifiPasswordChanged -> {
                val state = _state.value
                if(state.qrForm is QRForm.Wifi) {
                    _state.update {
                        state.copy(
                            qrForm = state.qrForm.copy(
                                psk = action.password
                            )
                        )
                    }
                }
            }

            CreateQRDetailAction.OnQRCreate -> {
                viewModelScope.launch {
                    val qrString = QRFormMapper.qrFormToString(_state.value.qrForm)
                    val id = qrRepository.upsertQR(
                        qr = QR(
                            id = 0,
                            title = "",
                            qrString = qrString,
                            isGenerated = true,
                            isFavorite = false,
                            qrType = qrType
                        )
                    )
                    eventChannel.send(CreateEvent.OnQRCreated(id))
                }
            }
        }
    }


    private fun getDefaultFormFromQRType(qrType: QRType): QRForm {
        return when(qrType) {
            QRType.TEXT -> QRForm.Text("")
            QRType.LINK -> QRForm.Link("")
            QRType.CONTACT -> QRForm.Contact("", "", "")
            QRType.PHONENUMBER -> QRForm.PhoneNumber("")
            QRType.GEOLOCATION -> QRForm.Geolocation("", "")
            QRType.WIFI -> QRForm.Wifi("", "", "")
        }
    }

}