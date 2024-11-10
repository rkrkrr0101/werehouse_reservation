package rkrk.reservation.warehouse.fare.domain.timeperiod

import rkrk.reservation.warehouse.reservation.domain.TimeSlot
import java.time.LocalTime

abstract class TimePeriodStrategy {
    abstract fun calTotalTime(timeSlot: TimeSlot): Long

    protected fun calPositiveMinutes(
        startTime: LocalTime,
        endTime: LocalTime,
    ): Long {
        val startTimeToMinute = Math.round(startTime.toSecondOfDay() / 60.0)
        val endTimeToMinute = Math.round(endTime.toSecondOfDay() / 60.0)
        val betweenTime = endTimeToMinute - startTimeToMinute
        if (betweenTime <= 0) {
            return 0
        }
        return betweenTime
    }
}
