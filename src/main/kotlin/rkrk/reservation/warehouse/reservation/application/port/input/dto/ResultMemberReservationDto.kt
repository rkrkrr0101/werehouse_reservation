package rkrk.reservation.warehouse.reservation.application.port.input.dto

import rkrk.reservation.warehouse.reservation.domain.ReservationStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class ResultMemberReservationDto(
    val warehouseName: String,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val totalFare: BigDecimal,
    val state: ReservationStatus,
)
