package rkrk.reservation.warehouse.reservation.application.port.input.dto

data class UpdateConfirmReservationDto(
    val reservationId: Long,
    val memberName: String,
)
