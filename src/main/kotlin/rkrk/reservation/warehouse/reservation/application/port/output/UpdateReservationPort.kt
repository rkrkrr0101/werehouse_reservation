package rkrk.reservation.warehouse.reservation.application.port.output

import rkrk.reservation.warehouse.reservation.domain.Reservation
import rkrk.reservation.warehouse.warehouse.domain.WareHouse

interface UpdateReservationPort {
    fun updateReservation(
        wareHouse: WareHouse,
        reservation: Reservation,
    )
}
