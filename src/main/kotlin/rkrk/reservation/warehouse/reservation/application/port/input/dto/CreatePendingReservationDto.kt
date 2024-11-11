package rkrk.reservation.warehouse.reservation.application.port.input.dto

import java.time.LocalDateTime

// 멤버이름,창고번호,예약기간
data class CreatePendingReservationDto(
    val memberName: String,
    val warehouseName: String,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
)
