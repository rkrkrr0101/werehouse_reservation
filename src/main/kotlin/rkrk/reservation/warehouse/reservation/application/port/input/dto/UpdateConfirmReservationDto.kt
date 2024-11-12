package rkrk.reservation.warehouse.reservation.application.port.input.dto

import java.time.LocalDateTime

data class UpdateConfirmReservationDto(
    val memberName: String,
    val warehouseName: String,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
)
