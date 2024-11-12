package rkrk.reservation.warehouse.reservation.application.port.output

import rkrk.reservation.warehouse.reservation.domain.Reservation

interface UpdateReservationPort {
    fun updateReservation(reservation: Reservation)
}
