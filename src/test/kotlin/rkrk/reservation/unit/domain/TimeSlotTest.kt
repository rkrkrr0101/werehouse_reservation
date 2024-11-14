package rkrk.reservation.unit.domain

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import rkrk.reservation.warehouse.reservation.domain.TimeOverlapStatus
import rkrk.reservation.warehouse.reservation.domain.TimeSlot
import java.time.LocalDate
import java.time.LocalTime

class TimeSlotTest {
    @Test
    @DisplayName("두시간이 겹치면 OVERLAPPING을 리턴한다")
    fun overlapping() {
        val firstSlot =
            TimeSlot(
                LocalDate.of(2024, 11, 7),
                LocalTime.of(11, 0),
                LocalTime.of(12, 0),
            )
        val secondSlot =
            TimeSlot(
                LocalDate.of(2024, 11, 7),
                LocalTime.of(10, 30),
                LocalTime.of(11, 30),
            )
        Assertions
            .assertThat(firstSlot.overlapsWith(secondSlot))
            .isEqualTo(TimeOverlapStatus.OVERLAPPING)
        Assertions
            .assertThat(secondSlot.overlapsWith(firstSlot))
            .isEqualTo(TimeOverlapStatus.OVERLAPPING)
    }

    @Test
    @DisplayName("두시간이 겹쳐도 날짜가 다르면 NON_OVERLAPPING을 리턴한다")
    fun differentDate() {
        val firstSlot =
            TimeSlot(
                LocalDate.of(2024, 11, 7),
                LocalTime.of(11, 0),
                LocalTime.of(12, 0),
            )
        val secondSlot =
            TimeSlot(
                LocalDate.of(2024, 11, 8),
                LocalTime.of(10, 30),
                LocalTime.of(11, 30),
            )
        Assertions
            .assertThat(firstSlot.overlapsWith(secondSlot))
            .isEqualTo(TimeOverlapStatus.NON_OVERLAPPING)
        Assertions
            .assertThat(secondSlot.overlapsWith(firstSlot))
            .isEqualTo(TimeOverlapStatus.NON_OVERLAPPING)
    }

    @Test
    @DisplayName("한시간이 다른시간을 포함하고 있으면 OVERLAPPING을 리턴한다")
    fun include_overlapping() {
        val firstSlot =
            TimeSlot(
                LocalDate.of(2024, 11, 7),
                LocalTime.of(11, 0),
                LocalTime.of(12, 0),
            )
        val secondSlot =
            TimeSlot(
                LocalDate.of(2024, 11, 7),
                LocalTime.of(11, 20),
                LocalTime.of(11, 40),
            )
        Assertions
            .assertThat(firstSlot.overlapsWith(secondSlot))
            .isEqualTo(TimeOverlapStatus.OVERLAPPING)
        Assertions
            .assertThat(secondSlot.overlapsWith(firstSlot))
            .isEqualTo(TimeOverlapStatus.OVERLAPPING)
    }

    @Test
    @DisplayName("한시간이 다른시간보다 앞에있으면 NON_OVERLAPPING을 리턴한다")
    fun earlyNonOverlapping() {
        val firstSlot =
            TimeSlot(
                LocalDate.of(2024, 11, 7),
                LocalTime.of(11, 0),
                LocalTime.of(12, 0),
            )
        val secondSlot =
            TimeSlot(
                LocalDate.of(2024, 11, 7),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
            )
        Assertions
            .assertThat(firstSlot.overlapsWith(secondSlot))
            .isEqualTo(TimeOverlapStatus.NON_OVERLAPPING)
        Assertions
            .assertThat(secondSlot.overlapsWith(firstSlot))
            .isEqualTo(TimeOverlapStatus.NON_OVERLAPPING)
    }
}
