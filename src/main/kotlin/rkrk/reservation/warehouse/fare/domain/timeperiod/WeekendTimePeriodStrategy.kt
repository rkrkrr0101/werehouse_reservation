package rkrk.reservation.warehouse.fare.domain.timeperiod

import rkrk.reservation.warehouse.reservation.domain.TimeSlot
import java.time.DayOfWeek

class WeekendTimePeriodStrategy : TimePeriodStrategy() {
    override fun calTotalTime(timeSlot: TimeSlot): Long {
        if (timeSlot.date.dayOfWeek == DayOfWeek.SUNDAY) {
            return calPositiveMinutes(timeSlot.startTime, timeSlot.endTime)
        }
        return 0
    }
}
