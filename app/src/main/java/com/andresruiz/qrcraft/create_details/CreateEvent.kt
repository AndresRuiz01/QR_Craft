package com.andresruiz.qrcraft.create_details

interface CreateEvent {
    data class OnQRCreated(val id: Long): CreateEvent
}