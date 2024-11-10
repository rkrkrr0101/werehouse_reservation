package rkrk.reservation.unit

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import rkrk.reservation.warehouse.fare.domain.timeperiod.NightTimePeriodStrategy
import rkrk.reservation.warehouse.reservation.domain.TimeSlot
import java.time.LocalDate
import java.time.LocalTime

class NightTimePeriodStrategyTest {
    @Test
    @DisplayName("자정 이후 야간할증시간에서 시작해서 야간할증시간 종료 후 종료되는 예약의 총 야간할증 시간을 구할수있다")
    fun laterMidnightStartNightTimeEndRegularTime() {
        val timeSlot =
            TimeSlot(
                LocalDate.of(2024, 10, 25),
                LocalTime.of(2, 30, 0),
                LocalTime.of(4, 30, 0),
            )
        val time = NightTimePeriodStrategy().calTotalTime(timeSlot)
        Assertions.assertThat(time).isEqualTo(90)
    }

    @Test
    @DisplayName("야간할증시간에서 시작해서 야간할증시간에서 종료되는 예약의 총 야간할증 시간을 구할수있다")
    fun startNightTimeEndNightTime() {
        val timeSlot =
            TimeSlot(
                LocalDate.of(2024, 10, 25),
                LocalTime.of(1, 30, 0),
                LocalTime.of(3, 30, 0),
            )
        val time = NightTimePeriodStrategy().calTotalTime(timeSlot)
        Assertions.assertThat(time).isEqualTo(120)
    }

    @Test
    @DisplayName("일반시간에서 시작해서 일반시간에서 종료되는 예약의 총 야간할증 시간은 0이다")
    fun startRegularTimeEndRegularTime() {
        val timeSlot =
            TimeSlot(
                LocalDate.of(2024, 10, 25),
                LocalTime.of(10, 30, 0),
                LocalTime.of(12, 30, 0),
            )
        val time = NightTimePeriodStrategy().calTotalTime(timeSlot)
        Assertions.assertThat(time).isEqualTo(0)
    }

    @Test
    @DisplayName("일반시간에서 시작해서 야간시간에서 종료되는 예약의 총 야간할증 시간을 구할수있다")
    fun startRegularTimeEndNightTime() {
        val timeSlot =
            TimeSlot(
                LocalDate.of(2024, 10, 25),
                LocalTime.of(15, 30, 0),
                LocalTime.of(22, 30, 0),
            )

        val time = NightTimePeriodStrategy().calTotalTime(timeSlot)

        Assertions.assertThat(time).isEqualTo(30)
    }

    @Test
    @DisplayName("자정이전 야간시간에서 시작해서 야간시간에서 종료되는 예약의 총 야간할증 시간을 구할수있다")
    fun earlyMidnightStartNightTimeEndNightTime() {
        val timeSlot =
            TimeSlot(
                LocalDate.of(2024, 10, 25),
                LocalTime.of(22, 30, 0),
                LocalTime.of(23, 50, 0),
            )

        val time = NightTimePeriodStrategy().calTotalTime(timeSlot)
        Assertions.assertThat(time).isEqualTo(80)
    }

//    @Test
//    @DisplayName("이틀에 걸친 예약의 총 야간할증 시간을 구할수있다")
//    fun twoDayTime() {
//        val startDateTime = LocalDateTime.of(2024, 10, 25, 15, 30, 0)
//        val endDateTime = LocalDateTime.of(2024, 10, 26, 5, 30, 0)
//        val reservationTime =
//            ReservationTime(
//                startDateTime,
//                endDateTime,
//            )
//        val time = NightTimePeriodStrategy().totalTime(reservationTime)
//
//        Assertions.assertThat(time).isEqualTo(360)
//    }
//
//    @Test
//    @DisplayName("5일에 걸친 예약의 총 야간할증 시간을 구할수있다")
//    fun fiveDayTime() {
//        val startDateTime = LocalDateTime.of(2024, 10, 22, 15, 30, 0)
//        val endDateTime = LocalDateTime.of(2024, 10, 26, 2, 30, 0)
//        val reservationTime =
//            ReservationTime(
//                startDateTime,
//                endDateTime,
//            )
//        val time = NightTimePeriodStrategy().totalTime(reservationTime)
//
//        Assertions.assertThat(time).isEqualTo(1350L)
//    }
}
