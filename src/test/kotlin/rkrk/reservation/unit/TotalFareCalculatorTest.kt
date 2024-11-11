package rkrk.reservation.unit

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import rkrk.reservation.warehouse.fare.domain.service.TotalFareCalculator
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import java.math.BigDecimal
import java.time.LocalDateTime

class TotalFareCalculatorTest {
    @Test
    @DisplayName("평일 당일의 예약에 대해 정상적으로 총 금액을 계산할수있다")
    fun weekdayRegularCalculate() {
        val startDateTime = LocalDateTime.of(2024, 10, 24, 10, 30, 0)
        val endDateTime = LocalDateTime.of(2024, 10, 24, 13, 30, 0)
        val reservationTime =
            ReservationTime(
                startDateTime,
                endDateTime,
            )

        val totalPrice = TotalFareCalculator().calTotalFare(reservationTime, 100)

        Assertions.assertThat(totalPrice).isEqualTo(BigDecimal("18000.0"))
    }

    @Test
    @DisplayName("평일 당일의 할증이 붙은 예약에 대해 정상적으로 총 금액을 계산할수있다")
    fun nightSurchargeCalculate() {
        val startDateTime = LocalDateTime.of(2024, 10, 24, 20, 30, 0)
        val endDateTime = LocalDateTime.of(2024, 10, 24, 23, 30, 0)
        val reservationTime =
            ReservationTime(
                startDateTime,
                endDateTime,
            )

        val totalPrice = TotalFareCalculator().calTotalFare(reservationTime, 100)

        // (100원*180분)+(100원*0.2할증률*90분)=19800
        // (100*60*3)+(100*0.2*60*1.5)
        Assertions.assertThat(totalPrice).isEqualTo(BigDecimal("19800.0"))
    }

    @Test
    @DisplayName("평일 당일의 12시00분까지의 예약에 대해 정상적으로 총 금액을 계산할수있다")
    fun nightSurchargeMidnightCalculate() {
        val startDateTime = LocalDateTime.of(2024, 10, 24, 23, 30, 0)
        val endDateTime = LocalDateTime.of(2024, 10, 25, 0, 0, 0)
        val reservationTime =
            ReservationTime(
                startDateTime,
                endDateTime,
            )

        val totalPrice = TotalFareCalculator().calTotalFare(reservationTime, 100)

        // (100원*180분)+(100원*0.2할증률*90분)=19800
        // (100*60*3)+(100*0.2*60*1.5)
        Assertions.assertThat(totalPrice).isEqualTo(BigDecimal("3600.0"))
    }

    @Test
    @DisplayName("주말 당일의 예약에 대해 정상적으로 총 금액을 계산할수있다")
    fun weekendRegularCalculate() {
        val startDateTime = LocalDateTime.of(2024, 10, 27, 10, 30, 0)
        val endDateTime = LocalDateTime.of(2024, 10, 27, 13, 30, 0)
        val reservationTime =
            ReservationTime(
                startDateTime,
                endDateTime,
            )

        val totalPrice = TotalFareCalculator().calTotalFare(reservationTime, 100)

        Assertions.assertThat(totalPrice).isEqualTo(BigDecimal("27000.0"))
    }

    @Test
    @DisplayName("주말 당일의 할증이 붙은 예약에 대해 정상적으로 총 금액을 계산할수있다")
    fun weekendNightSurchargeCalculate() {
        val startDateTime = LocalDateTime.of(2024, 10, 27, 20, 30, 0)
        val endDateTime = LocalDateTime.of(2024, 10, 27, 23, 30, 0)
        val reservationTime =
            ReservationTime(
                startDateTime,
                endDateTime,
            )

        val totalPrice = TotalFareCalculator().calTotalFare(reservationTime, 100)
        // (100*60*3)+(100*0.2*60*1.5)+(100*0.5*60*3)
        Assertions.assertThat(totalPrice).isEqualTo(BigDecimal("28800.0"))
    }

    @Test
    @DisplayName("4박5일의 예약에 대해 정상적으로 총 금액을 계산할수있다")
    fun fiveDayCalculate() {
        val startDateTime = LocalDateTime.of(2024, 10, 24, 10, 30, 0)
        val endDateTime = LocalDateTime.of(2024, 10, 28, 10, 30, 0)
        val reservationTime =
            ReservationTime(
                startDateTime,
                endDateTime,
            )
        val totalPrice = TotalFareCalculator().calTotalFare(reservationTime, 100)
        // 평일24*4일 +야간할증 총 24시간+주말하루 24시간
        // (100*60*24*4)+(100*0.2*60*24)+(100*0.5*60*24*1)
        Assertions.assertThat(totalPrice).isEqualTo(BigDecimal("676800.0"))
    }
}
