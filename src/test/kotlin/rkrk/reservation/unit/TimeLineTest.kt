package rkrk.reservation.unit

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import rkrk.reservation.warehouse.reservation.domain.TimeLine
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

        timeLine.addTime(targetTime)

        Assertions.assertThat(timeLine.reservationTimes.size).isEqualTo(1)
        Assertions.assertThat(timeLine.reservationTimes[0]).isEqualTo(targetTime)
    }

    @Test
    @DisplayName("타임라인에 겹치지않는 예약을 더할수있다")
    fun addReservationTime() {
        val timeLine = createBasicTimeLine()
        val targetTime =
            ReservationTime(
                LocalDateTime.of(2024, 10, 24, 10, 30, 0),
                LocalDateTime.of(2024, 10, 25, 9, 30, 0),
            )

        timeLine.addTime(targetTime)

        Assertions.assertThat(timeLine.reservationTimes.size).isEqualTo(5)
        Assertions.assertThat(timeLine.reservationTimes[1]).isEqualTo(targetTime)
    }

    @Test
    @DisplayName("타임라인의 제일 왼쪽에 예약을 더할수있다")
    fun leftAddReservationTime() {
        val timeLine = createBasicTimeLine()
        val targetTime =
            ReservationTime(
                LocalDateTime.of(2024, 10, 10, 10, 30, 0),
                LocalDateTime.of(2024, 10, 11, 9, 30, 0),
            )

        timeLine.addTime(targetTime)

        Assertions.assertThat(timeLine.reservationTimes.size).isEqualTo(5)
        Assertions.assertThat(timeLine.reservationTimes[0]).isEqualTo(targetTime)
    }

    @Test
    @DisplayName("타임라인의 제일 오른쪽에 예약을 더할수있다")
    fun rightAddReservationTime() {
        val timeLine = createBasicTimeLine()
        val targetTime =
            ReservationTime(
                LocalDateTime.of(2025, 11, 10, 10, 30, 0),
                LocalDateTime.of(2025, 11, 11, 9, 30, 0),
            )

        timeLine.addTime(targetTime)

        Assertions.assertThat(timeLine.reservationTimes.size).isEqualTo(5)
        Assertions.assertThat(timeLine.reservationTimes[4]).isEqualTo(targetTime)
    }

    @Test
    @DisplayName("타임라인에 시작시간과 종료시간의 연도가 다른 예약도 더할수있다")
    fun otherYearAddReservationTime() {
        val timeLine = createBasicTimeLine()
        val targetTime =
            ReservationTime(
                LocalDateTime.of(2023, 12, 28, 10, 30, 0),
                LocalDateTime.of(2024, 1, 20, 9, 30, 0),
            )

        timeLine.addTime(targetTime)

        Assertions.assertThat(timeLine.reservationTimes.size).isEqualTo(5)
        Assertions.assertThat(timeLine.reservationTimes[0]).isEqualTo(targetTime)
    }

    @Test
    @DisplayName("타임라인에 뒤쪽으로 겹치는 예약을 추가하려고 하면 예외가 발생한다")
    fun rearOverlapReservationTimeThrowException() {
        val timeLine = createBasicTimeLine()
        val targetTime =
            ReservationTime(
                LocalDateTime.of(2024, 10, 24, 10, 30, 0),
                LocalDateTime.of(2024, 10, 25, 11, 30, 0),
            )

        Assertions
            .assertThatThrownBy { timeLine.addTime(targetTime) }
            .isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    @DisplayName("타임라인에 앞쪽으로 겹치는 예약을 추가하려고 하면 예외가 발생한다")
    fun frontOverlapReservationTimeThrowException() {
        val timeLine = createBasicTimeLine()
        val targetTime =
            ReservationTime(
                LocalDateTime.of(2024, 10, 27, 10, 30, 0),
                LocalDateTime.of(2024, 10, 29, 11, 30, 0),
            )

        Assertions
            .assertThatThrownBy { timeLine.addTime(targetTime) }
            .isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    @DisplayName("타임라인에 완전히 겹치는 예약을 추가하려고 하면 예외가 발생한다")
    fun equalOverlapReservationTimeThrowException() {
        val timeLine = createBasicTimeLine()
        val targetTime =
            ReservationTime(
                LocalDateTime.of(2024, 10, 25, 10, 30, 0),
                LocalDateTime.of(2024, 10, 27, 13, 30, 0),
            )

        Assertions
            .assertThatThrownBy { timeLine.addTime(targetTime) }
            .isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    @DisplayName("타임라인에 있는 예약에 포개지는 예약을 추가하려고 하면 예외가 발생한다")
    fun includeOverlapReservationTimeThrowException() {
        val timeLine = createBasicTimeLine()
        val targetTime =
            ReservationTime(
                LocalDateTime.of(2024, 10, 26, 10, 30, 0),
                LocalDateTime.of(2024, 10, 26, 13, 30, 0),
            )

        Assertions
            .assertThatThrownBy { timeLine.addTime(targetTime) }
            .isInstanceOf(IllegalStateException::class.java)
    }

    private fun createBasicTimeLine(): TimeLine {
        val timeLine = TimeLine()

        val reservationTime1 =
            ReservationTime(
                LocalDateTime.of(2024, 10, 22, 10, 30, 0),
                LocalDateTime.of(2024, 10, 23, 13, 30, 0),
            )
        val reservationTime2 =
            ReservationTime(
                LocalDateTime.of(2024, 10, 25, 10, 30, 0),
                LocalDateTime.of(2024, 10, 27, 13, 30, 0),
            )
        val reservationTime3 =
            ReservationTime(
                LocalDateTime.of(2024, 10, 28, 10, 30, 0),
                LocalDateTime.of(2024, 10, 29, 13, 30, 0),
            )
        val reservationTime4 =
            ReservationTime(
                LocalDateTime.of(2024, 12, 28, 10, 30, 0),
                LocalDateTime.of(2025, 1, 10, 13, 30, 0),
            )
        timeLine.addTime(reservationTime1)
        timeLine.addTime(reservationTime2)
        timeLine.addTime(reservationTime3)
        timeLine.addTime(reservationTime4)

        return timeLine
    }
}
