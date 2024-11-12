package rkrk.reservation.unit

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import rkrk.reservation.warehouse.reservation.domain.TimeLine
import rkrk.reservation.warehouse.reservation.domain.TimeOverlapStatus
import rkrk.reservation.warehouse.warehouse.domain.WareHouse
import java.math.BigDecimal
import java.time.LocalDateTime

class WareHouseTest {
    @Test
    @DisplayName("예약을 생성하고 추가할수있다")
    fun createAndAddReservation() {
        val timeLine = TimeLine()
        val wareHouse = WareHouse("ware1", 100, 1000, timeLine)
        val startDateTime = LocalDateTime.of(2024, 10, 25, 10, 30, 0)
        val endDateTime = LocalDateTime.of(2024, 10, 25, 13, 30, 0)
        val reservationTime =
            ReservationTime(
                startDateTime,
                endDateTime,
            )

        val reservation = wareHouse.createAndAddReservation("aa", reservationTime)

        Assertions.assertThat(reservation.totalFare).isEqualTo(BigDecimal("18000.0"))
        Assertions
            .assertThat(wareHouse.checkOverlapReservation(reservationTime))
            .isEqualTo(TimeOverlapStatus.OVERLAPPING)
    }

    @Test
    @DisplayName("빈 타임라인에 오버랩체크를 하면 NON_OVERLAPPING를 리턴한다")
    fun emptyTimeLineOverlapCheck() {
        val timeLine = TimeLine()
        val wareHouse = WareHouse("ware1", 100, 1000, timeLine)
        val startDateTime = LocalDateTime.of(2024, 10, 25, 10, 30, 0)
        val endDateTime = LocalDateTime.of(2024, 10, 25, 13, 30, 0)
        val reservationTime =
            ReservationTime(
                startDateTime,
                endDateTime,
            )

        Assertions
            .assertThat(wareHouse.checkOverlapReservation(reservationTime))
            .isEqualTo(TimeOverlapStatus.NON_OVERLAPPING)
    }
}
