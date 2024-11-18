package rkrk.reservation.unit.domain

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import rkrk.reservation.warehouse.reservation.domain.Reservation
import rkrk.reservation.warehouse.reservation.domain.ReservationStatus
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import rkrk.reservation.warehouse.reservation.domain.TimeLine
import rkrk.reservation.warehouse.share.exception.OverlapException
import java.math.BigDecimal
import java.time.LocalDateTime

class TimeLineTest {
    @Test
    @DisplayName("빈타임라인에 예약을 더할수있다")
    fun addReservationTimeToBlankTimeLine() {
        val timeLine = TimeLine()
        val targetTime =
            ReservationTime(
                LocalDateTime.of(2024, 10, 24, 10, 30, 0),
                LocalDateTime.of(2024, 10, 25, 9, 30, 0),
            )
        val reservation = Reservation("aa", targetTime, BigDecimal("10000"))

        timeLine.addReservation(reservation)

        Assertions.assertThat(timeLine.reservations.size).isEqualTo(1)
        Assertions.assertThat(timeLine.reservations[0].reservationTime).isEqualTo(targetTime)
    }

    @Test
    @DisplayName("타임라인에 겹치지않는 예약을 더할수있다")
    fun addReservationTime() {
        val timeLine = addBasicReservation()
        val targetTime =
            ReservationTime(
                LocalDateTime.of(2024, 10, 24, 10, 30, 0),
                LocalDateTime.of(2024, 10, 25, 9, 30, 0),
            )
        val reservation = Reservation("aa", targetTime, BigDecimal("10000"))

        timeLine.addReservation(reservation)

        Assertions.assertThat(timeLine.reservations.size).isEqualTo(5)
        Assertions.assertThat(timeLine.reservations[1].reservationTime).isEqualTo(targetTime)
    }

    @Test
    @DisplayName("타임라인의 제일 왼쪽에 예약을 더할수있다")
    fun leftAddReservationTime() {
        val timeLine = addBasicReservation()
        val targetTime =
            ReservationTime(
                LocalDateTime.of(2024, 10, 10, 10, 30, 0),
                LocalDateTime.of(2024, 10, 11, 9, 30, 0),
            )
        val reservation = Reservation("aa", targetTime, BigDecimal("10000"))

        timeLine.addReservation(reservation)

        Assertions.assertThat(timeLine.reservations.size).isEqualTo(5)
        Assertions.assertThat(timeLine.reservations[0].reservationTime).isEqualTo(targetTime)
    }

    @Test
    @DisplayName("타임라인의 제일 오른쪽에 예약을 더할수있다")
    fun rightAddReservationTime() {
        val timeLine = addBasicReservation()
        val targetTime =
            ReservationTime(
                LocalDateTime.of(2025, 11, 10, 10, 30, 0),
                LocalDateTime.of(2025, 11, 11, 9, 30, 0),
            )
        val reservation = Reservation("aa", targetTime, BigDecimal("10000"))

        timeLine.addReservation(reservation)

        Assertions.assertThat(timeLine.reservations.size).isEqualTo(5)
        Assertions.assertThat(timeLine.reservations[4].reservationTime).isEqualTo(targetTime)
    }

    @Test
    @DisplayName("타임라인에 시작시간과 종료시간의 연도가 다른 예약도 더할수있다")
    fun otherYearAddReservationTime() {
        val timeLine = addBasicReservation()
        val targetTime =
            ReservationTime(
                LocalDateTime.of(2023, 12, 28, 10, 30, 0),
                LocalDateTime.of(2024, 1, 20, 9, 30, 0),
            )
        val reservation = Reservation("aa", targetTime, BigDecimal("10000"))

        timeLine.addReservation(reservation)

        Assertions.assertThat(timeLine.reservations.size).isEqualTo(5)
        Assertions.assertThat(timeLine.reservations[0].reservationTime).isEqualTo(targetTime)
    }

    @Test
    @DisplayName("이전 예약의 종료시간과 새로 추가하는 예약의 시작시간이 일치하는 예약도 추가할수있다")
    fun stickReservation() {
        val timeLine = addBasicReservation()
        val targetTime =
            ReservationTime(
                LocalDateTime.of(2024, 10, 23, 13, 30, 0),
                LocalDateTime.of(2024, 10, 23, 15, 30, 0),
            )
        val reservation = Reservation("aa", targetTime, BigDecimal("10000"))

        timeLine.addReservation(reservation)

        Assertions.assertThat(timeLine.reservations.size).isEqualTo(5)
        Assertions.assertThat(timeLine.reservations[1].reservationTime).isEqualTo(targetTime)
    }

    @Test
    @DisplayName("타임라인에 뒤쪽으로 겹치는 예약을 추가하려고 하면 예외가 발생한다")
    fun rearOverlapReservationTimeThrowException() {
        val timeLine = addBasicReservation()
        val targetTime =
            ReservationTime(
                LocalDateTime.of(2024, 10, 24, 10, 30, 0),
                LocalDateTime.of(2024, 10, 25, 11, 30, 0),
            )
        val reservation = Reservation("aa", targetTime, BigDecimal("10000"))

        Assertions
            .assertThatThrownBy { timeLine.addReservation(reservation) }
            .isInstanceOf(OverlapException::class.java)
    }

    @Test
    @DisplayName("타임라인에 앞쪽으로 겹치는 예약을 추가하려고 하면 예외가 발생한다")
    fun frontOverlapReservationTimeThrowException() {
        val timeLine = addBasicReservation()
        val targetTime =
            ReservationTime(
                LocalDateTime.of(2024, 10, 27, 10, 30, 0),
                LocalDateTime.of(2024, 10, 29, 11, 30, 0),
            )
        val reservation = Reservation("aa", targetTime, BigDecimal("10000"))

        Assertions
            .assertThatThrownBy { timeLine.addReservation(reservation) }
            .isInstanceOf(OverlapException::class.java)
    }

    @Test
    @DisplayName("타임라인에 완전히 겹치는 예약을 추가하려고 하면 예외가 발생한다")
    fun equalOverlapReservationTimeThrowException() {
        val timeLine = addBasicReservation()
        val targetTime =
            ReservationTime(
                LocalDateTime.of(2024, 10, 25, 10, 30, 0),
                LocalDateTime.of(2024, 10, 27, 13, 30, 0),
            )
        val reservation = Reservation("aa", targetTime, BigDecimal("10000"))

        Assertions
            .assertThatThrownBy { timeLine.addReservation(reservation) }
            .isInstanceOf(OverlapException::class.java)
    }

    @Test
    @DisplayName("타임라인에 있는 예약에 포개지는 예약을 추가하려고 하면 예외가 발생한다")
    fun includeOverlapReservationTimeThrowException() {
        val timeLine = addBasicReservation()
        val targetTime =
            ReservationTime(
                LocalDateTime.of(2024, 10, 26, 10, 30, 0),
                LocalDateTime.of(2024, 10, 26, 13, 30, 0),
            )
        val reservation = Reservation("aa", targetTime, BigDecimal("10000"))

        Assertions
            .assertThatThrownBy { timeLine.addReservation(reservation) }
            .isInstanceOf(OverlapException::class.java)
    }

    private fun addBasicReservation(): TimeLine {
        val timeLine = TimeLine()
        val reservationTime1 =
            ReservationTime(
                LocalDateTime.of(2024, 10, 22, 10, 30, 0),
                LocalDateTime.of(2024, 10, 23, 13, 30, 0),
            )
        val reservation1 =
            Reservation(
                "aa",
                reservationTime1,
                BigDecimal("10000"),
                ReservationStatus.CONFIRMED,
            )
        val reservationTime2 =
            ReservationTime(
                LocalDateTime.of(2024, 10, 25, 10, 30, 0),
                LocalDateTime.of(2024, 10, 27, 13, 30, 0),
            )
        val reservation2 =
            Reservation(
                "aa",
                reservationTime2,
                BigDecimal("10000"),
                ReservationStatus.CONFIRMED,
            )
        val reservationTime3 =
            ReservationTime(
                LocalDateTime.of(2024, 10, 28, 10, 30, 0),
                LocalDateTime.of(2024, 10, 29, 13, 30, 0),
            )
        val reservation3 =
            Reservation(
                "aa",
                reservationTime3,
                BigDecimal("10000"),
                ReservationStatus.CONFIRMED,
            )
        val reservationTime4 =
            ReservationTime(
                LocalDateTime.of(2024, 12, 28, 10, 30, 0),
                LocalDateTime.of(2025, 1, 10, 13, 30, 0),
            )
        val reservation4 =
            Reservation(
                "aa",
                reservationTime4,
                BigDecimal("10000"),
                ReservationStatus.CONFIRMED,
            )
        timeLine.addReservation(reservation1)
        timeLine.addReservation(reservation2)
        timeLine.addReservation(reservation3)
        timeLine.addReservation(reservation4)

        return timeLine
    }
}
