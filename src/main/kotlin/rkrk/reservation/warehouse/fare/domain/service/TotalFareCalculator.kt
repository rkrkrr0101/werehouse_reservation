package rkrk.reservation.warehouse.fare.domain.service

import rkrk.reservation.warehouse.fare.domain.fare.Fare
import rkrk.reservation.warehouse.fare.domain.fare.NightSurcharge
import rkrk.reservation.warehouse.fare.domain.fare.RegularFare
import rkrk.reservation.warehouse.fare.domain.fare.WeekendSurcharge
import rkrk.reservation.warehouse.fare.domain.timeperiod.NightTimePeriodStrategy
import rkrk.reservation.warehouse.fare.domain.timeperiod.RegularTimePeriodStrategy
import rkrk.reservation.warehouse.fare.domain.timeperiod.TimePeriodStrategy
import rkrk.reservation.warehouse.fare.domain.timeperiod.WeekendTimePeriodStrategy
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import rkrk.reservation.warehouse.reservation.domain.TimeSlot
import java.math.BigDecimal

// ReservationTime을 받아서,해당 클래스의 날짜 금액을 계산해주는 도메인서비스
// 날짜기반은 얘로 처리하고,쿠폰같은건 여기서 계산값을 받아간후에 다른클래스에서 처리

class TotalFareCalculator {
    fun calTotalFare(
        reservationTime: ReservationTime,
        price: Long,
    ): BigDecimal {
        var totalPrice = BigDecimal(0)
        val timeSlots = reservationTime.toTimeSlots()
        val fareCalculators = createFareCalculators()

        for (timeSlot in timeSlots) {
            for (fareCalculator in fareCalculators) {
                totalPrice += fareCalculator.calculate(timeSlot, price)
            }
        }
        return totalPrice
    }

    private fun createFareCalculators(): List<FareCalculator> {
        val fareCalculators = mutableListOf<FareCalculator>()

        fareCalculators.add(FareCalculator(RegularFare(), RegularTimePeriodStrategy()))
        fareCalculators.add(FareCalculator(NightSurcharge(), NightTimePeriodStrategy()))
        fareCalculators.add(FareCalculator(WeekendSurcharge(), WeekendTimePeriodStrategy()))

        return fareCalculators
    }

    private class FareCalculator(
        private val fare: Fare,
        private val timePeriodStrategy: TimePeriodStrategy,
    ) {
        fun calculate(
            timeSlot: TimeSlot,
            baseMinuteRate: Long,
        ): BigDecimal = fare.calFare(timePeriodStrategy.calTotalTime(timeSlot), baseMinuteRate)
    }
}
