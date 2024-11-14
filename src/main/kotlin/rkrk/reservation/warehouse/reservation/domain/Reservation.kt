package rkrk.reservation.warehouse.reservation.domain

import rkrk.reservation.warehouse.fare.domain.service.TotalFareCalculator
import rkrk.reservation.warehouse.warehouse.domain.WareHouse
import java.math.BigDecimal

class Reservation(
    val memberName: String,
    val reservationTime: ReservationTime, // 엔티티에선 LocalDateTime2개로 변경
    val totalFare: BigDecimal,
    private var _state: ReservationStatus = ReservationStatus.PENDING,
) {
    constructor(
        memberName: String,
        reservationTime: ReservationTime,
        warehouse: WareHouse,
        state: ReservationStatus = ReservationStatus.PENDING,
    ) : this(
        memberName,
        reservationTime,
        TotalFareCalculator().calTotalFare(reservationTime, warehouse.retrieveMinutePrice()),
        state,
    )

    val state: ReservationStatus
        get() = _state

    fun updateState(newState: ReservationStatus) {
        this._state = newState
    }
}
