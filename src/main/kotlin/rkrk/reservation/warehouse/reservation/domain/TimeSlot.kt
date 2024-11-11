package rkrk.reservation.warehouse.reservation.domain

import java.time.LocalDate
import java.time.LocalTime

data class TimeSlot(
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
) {
    init {
        require(startTime <= endTime) { "시작시간이 종료시간보다 더 큽니다" }
    }

    val isFullTime: Boolean = (this.startTime == LocalTime.MIN) && (this.endTime == LocalTime.MAX)

    fun overlapsWith(other: TimeSlot): TimeOverlapStatus =
        when {
            this.date != other.date -> TimeOverlapStatus.NON_OVERLAPPING
            (this.startTime < other.endTime) && (this.endTime > other.startTime) // 함수화?
            -> TimeOverlapStatus.OVERLAPPING

            else -> TimeOverlapStatus.NON_OVERLAPPING
        }

    companion object {
        fun createFullTime(date: LocalDate): TimeSlot = TimeSlot(date, LocalTime.MIN, LocalTime.MAX)
    }
}
