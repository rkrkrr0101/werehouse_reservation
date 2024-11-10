package rkrk.reservation.warehouse.reservation.domain

import rkrk.reservation.warehouse.warehouse.domain.WareHouse

class Reservation(
    private val memberName: String,
    private val wereHouse: WareHouse,
    private val reservationTime: ReservationTime, // 엔티티에선 LocalDateTime2개로 변경
    private val price: Long,
    private val state: ReservationStatus, // enum
)
