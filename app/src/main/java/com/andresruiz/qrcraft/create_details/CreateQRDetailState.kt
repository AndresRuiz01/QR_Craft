package com.andresruiz.qrcraft.create_details

import com.andresruiz.qrcraft.create_details.domain.QRForm
import com.andresruiz.qrcraft.create_details.domain.isValid

data class CreateQRDetailState(
    val test: Boolean = false,
    val qrForm: QRForm = QRForm.Text("")
) {
    val isFormValid: Boolean
        get() = qrForm.isValid()
}