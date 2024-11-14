package rkrk.reservation.unit.domain

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import rkrk.reservation.warehouse.fare.domain.timeperiod.WeekendTimePeriodStrategy
import rkrk.reservation.warehouse.reservation.domain.TimeSlot
import java.time.LocalDate
import java.time.LocalTime

class WeekendTimePeriodStrategyTest {
    private val weekdayDate: LocalDate = LocalDate.of(2024, 10, 24)
    private val weekendDate: LocalDate = LocalDate.of(2024, 10, 27)

    @Test
    @DisplayName("주말의 총 시간을 계산하면 전체시간이 리턴된다")
    fun weekendDateTotalTime() {
        val timeSlot =
            TimeSlot(
                weekendDate,
                LocalTime.of(13, 30, 0),
                LocalTime.of(15, 30, 0),
            )
        val totalTime = WeekendTimePeriodStrategy().calTotalTime(timeSlot)

        Assertions.assertThat(totalTime).isEqualTo(120)
    }

    @Test
    @DisplayName("주중의 총 시간을 계산하면 0이 리턴된다")
    fun weekdayDateTotalTimeEqualZero() {
        val timeSlot =
            TimeSlot(
                weekdayDate,
                LocalTime.of(13, 30, 0),
                LocalTime.of(15, 30, 0),
            )
        val totalTime = WeekendTimePeriodStrategy().calTotalTime(timeSlot)

        Assertions.assertThat(totalTime).isEqualTo(0)
    }
}
