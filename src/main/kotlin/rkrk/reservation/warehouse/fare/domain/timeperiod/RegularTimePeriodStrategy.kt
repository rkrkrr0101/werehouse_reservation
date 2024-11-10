package rkrk.reservation.warehouse.fare.domain.timeperiod

import rkrk.reservation.warehouse.reservation.domain.TimeSlot

class RegularTimePeriodStrategy : TimePeriodStrategy() {
    override fun calTotalTime(timeSlot: TimeSlot): Long = calPositiveMinutes(timeSlot.startTime, timeSlot.endTime)
}
