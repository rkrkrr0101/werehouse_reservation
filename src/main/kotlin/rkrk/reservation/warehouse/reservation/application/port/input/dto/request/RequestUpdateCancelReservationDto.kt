package rkrk.reservation.warehouse.reservation.application.port.input.dto.request

import java.time.LocalDateTime

data class RequestUpdateCancelReservationDto(
    val memberName: String,
    val warehouseName: String,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
)