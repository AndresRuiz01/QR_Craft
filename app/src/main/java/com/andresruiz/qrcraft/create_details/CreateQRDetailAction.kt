package com.andresruiz.qrcraft.create_details

sealed interface CreateQRDetailAction {
    data class OnQRTextChanged(val text: String): CreateQRDetailAction
    data class OnQRLinkChanged(val url: String): CreateQRDetailAction
    data class OnQRContactNameChanged(val name: String): CreateQRDetailAction
    data class OnQRContactEmailChanged(val email: String): CreateQRDetailAction
    data class OnQRContactPhoneChanged(val phone: String): CreateQRDetailAction
    data class OnQRGeoLatChanged(val lat: String): CreateQRDetailAction
    data class OnQRGeoLongChanged(val long: String): CreateQRDetailAction
    data class OnQRWifiNameChanged(val name: String): CreateQRDetailAction
    data class OnQRWifiPasswordChanged(val password: String): CreateQRDetailAction
    data class OnQRWifiEncryptionChanged(val encryption: String): CreateQRDetailAction
    data class OnQRPhoneNumberChanged(val number: String): CreateQRDetailAction
    data object OnQRCreate: CreateQRDetailAction
}