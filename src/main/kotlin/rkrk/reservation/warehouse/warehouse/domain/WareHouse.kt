package rkrk.reservation.warehouse.warehouse.domain

import rkrk.reservation.warehouse.reservation.domain.Reservation
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import rkrk.reservation.warehouse.reservation.domain.TimeLine
import rkrk.reservation.warehouse.reservation.domain.TimeOverlapStatus

class WareHouse(
    private val name: String,
    private val minutePrice: Long,
    private val capacity: Long,
    private val timeLine: TimeLine,
) {
    fun retrieveName() = name

    fun retrieveMinutePrice() = minutePrice

    fun retrieveCapacity() = capacity

    fun retrieveTotalReservations() = timeLine

    fun addReservation(reservation: Reservation) {
        timeLine.addReservation(reservation)
    }

    fun checkOverlapReservation(reservationTime: ReservationTime): TimeOverlapStatus = timeLine.overlapCheck(reservationTime)
}
