package rkrk.reservation.unit

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import rkrk.reservation.warehouse.reservation.domain.TimeLine
import java.time.LocalDateTime

// 앞쪽으로 겹치는
// 빈타임라인에 추가
// 포개지는
// 연도
class TimeLineTest {
    @Test
    @DisplayName("타임라인에 겹치지않는 예약을 더할수있다")
    fun addReservationTime() {
        val timeLine = createTimeLine()
        val targetTime =
            ReservationTime(
                LocalDateTime.of(2024, 10, 24, 10, 30, 0),
                LocalDateTime.of(2024, 10, 25, 9, 30, 0),
            )

        timeLine.addTime(targetTime)

        Assertions.assertThat(timeLine.reservationTimes.size).isEqualTo(4)
        Assertions.assertThat(timeLine.reservationTimes[1]).isEqualTo(targetTime)
    }

    @Test
    @DisplayName("타임라인에 뒤쪽으로 겹치는 예약을 추가하려고 하면 예외가 발생한다")
    fun addReservationTimeThrowException() {
        val timeLine = createTimeLine()
        val targetTime =
            ReservationTime(
                LocalDateTime.of(2024, 10, 24, 10, 30, 0),
                LocalDateTime.of(2024, 10, 25, 11, 30, 0),
            )

        Assertions
            .assertThatThrownBy { timeLine.addTime(targetTime) }
            .isInstanceOf(IllegalStateException::class.java)
    }

    private fun createTimeLine(): TimeLine {
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
        timeLine.reservationTimes.add(reservationTime1)
        timeLine.reservationTimes.add(reservationTime2)
        timeLine.reservationTimes.add(reservationTime3)

        return timeLine
    }
}