package rkrk.reservation.warehouse.warehouse.domain

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

    fun addReservation(reservationTime: ReservationTime)  {
        timeLine.addTime(reservationTime)
    }

    fun checkOverlapReservation(reservationTime: ReservationTime): TimeOverlapStatus = timeLine.overlapCheck(reservationTime)
}
