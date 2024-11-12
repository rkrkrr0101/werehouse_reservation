package rkrk.reservation.warehouse.reservation.application.port.input.dto

import java.time.LocalDateTime

data class UpdateRefundReservationDto(
    val memberName: String,
    val warehouseName: String,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    // val accountNumber:String,
)
