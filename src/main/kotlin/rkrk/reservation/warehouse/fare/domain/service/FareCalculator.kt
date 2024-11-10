package rkrk.reservation.warehouse.fare.domain.service

import rkrk.reservation.warehouse.fare.domain.fare.Fare
import rkrk.reservation.warehouse.fare.domain.timeperiod.TimePeriodStrategy
import rkrk.reservation.warehouse.reservation.domain.TimeSlot
import java.math.BigDecimal

class FareCalculator(
    private val fare: Fare,
    private val timePeriodStrategy: TimePeriodStrategy,
) {
    fun calculate(
        timeSlot: TimeSlot,
        baseMinuteRate: Long,
    ): BigDecimal = fare.calFare(timePeriodStrategy.calTotalTime(timeSlot), baseMinuteRate)
}
