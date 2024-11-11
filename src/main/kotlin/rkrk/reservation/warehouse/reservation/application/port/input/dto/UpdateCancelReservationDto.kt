package rkrk.reservation.warehouse.reservation.application.port.input.dto

data class UpdateCancelReservationDto(
    val reservationId: Long,
    val memberName: String,
)
