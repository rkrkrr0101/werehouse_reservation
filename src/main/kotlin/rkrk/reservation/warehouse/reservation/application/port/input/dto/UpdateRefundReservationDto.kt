package rkrk.reservation.warehouse.reservation.application.port.input.dto

data class UpdateRefundReservationDto(
    val reservationId: Long,
    val memberName: String,
    // val accountNumber:String,
)
