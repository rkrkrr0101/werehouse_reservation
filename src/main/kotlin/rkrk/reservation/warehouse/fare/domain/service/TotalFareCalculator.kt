package rkrk.reservation.warehouse.fare.domain.service

import rkrk.reservation.warehouse.fare.domain.fare.NightSurcharge
import rkrk.reservation.warehouse.fare.domain.fare.RegularFare
import rkrk.reservation.warehouse.fare.domain.fare.WeekendSurcharge
import rkrk.reservation.warehouse.fare.domain.timeperiod.NightTimePeriodStrategy
import rkrk.reservation.warehouse.fare.domain.timeperiod.RegularTimePeriodStrategy
import rkrk.reservation.warehouse.fare.domain.timeperiod.WeekendTimePeriodStrategy
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import java.math.BigDecimal

// ReservationTime을 받아서,해당 클래스의 날짜 금액을 계산해주는 도메인서비스
// 날짜기반은 얘로 처리한다고 치고,쿠폰같은건 다른클래스에서 처리

// 시작전 일요일체크

// 엔드타임이 저녁이전이면 시작~엔드 +일반요금
// 시작타임이 저녁이전이고 엔드타임이 저녁 이후면 시작~저녁(일반요금) + 저녁~엔드(저녁요금)
// 시작타임이 저녁이후면 시작~엔드+ 저녁요금
// TODO
// fare와 TimePeriodStrategy를 둘다 di받고,그걸가지고 계산해서 리턴
// 각 계산단위는 TimeSlot
// night와 regular을 각각 계산
// weekend도 그냥 주말이면 다시 0.2만큼을 추가로 더하고,아니면 0을 더함

class TotalFareCalculator {
    fun calTotalFare(
        reservationTime: ReservationTime,
        price: Long,
    ): BigDecimal {
        // 계산
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
}
