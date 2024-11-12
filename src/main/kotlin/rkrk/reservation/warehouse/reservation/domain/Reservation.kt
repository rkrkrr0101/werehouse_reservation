package rkrk.reservation.warehouse.reservation.domain

import java.math.BigDecimal

class Reservation(
    val memberName: String,
    val reservationTime: ReservationTime, // 엔티티에선 LocalDateTime2개로 변경
    val price: BigDecimal,
    val state: ReservationStatus = ReservationStatus.PENDING,
)
