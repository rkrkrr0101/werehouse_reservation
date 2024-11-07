package rkrk.reservation.warehouse.reservation.domain

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

// 한 예약에 대한 예약시간
// 생성자로 받은 시간을 가지고 타임슬롯리스트를 생성할수있음
// 타임슬롯리스트는 정렬되어있어야함(바이너리서치)

// TODO 테스트생성
class ReservationSlots(
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
) {
    private val startDate = startDateTime.toLocalDate()
    private val startTime = startDateTime.toLocalTime()
    private val endDate = endDateTime.toLocalDate()
    private val endTime = endDateTime.toLocalTime()
    private val isSameDay = startDate == endDate

    fun toTimeSlots(): List<TimeSlot> {
        val resList = mutableListOf<TimeSlot>()

        startDate.datesUntil(endDate).forEach {
            val timeSlot =
                createTimeSlotForDate(it)
            resList.add(timeSlot)
        }
        return resList
    }

    private fun createTimeSlotForDate(date: LocalDate) =
        when {
            isSameDay -> createSameDaySlot()
            date == startDate -> createStartTimeSlot()
            date == endDate -> createEndTimeSlot()
            else -> TimeSlot.createFullTime(date)
        }

    fun startTimeSlot(): TimeSlot {
        val timeSlot =
            when {
                isSameDay -> createSameDaySlot()
                else -> createStartTimeSlot()
            }
        return timeSlot
    }

    fun endTimeSlot(): TimeSlot {
        val timeSlot =
            when {
                isSameDay -> createSameDaySlot()
                else -> createEndTimeSlot()
            }
        return timeSlot
    }

    private fun createSameDaySlot() = TimeSlot(startDate, startTime, endTime)

    private fun createStartTimeSlot() = TimeSlot(startDate, startTime, LocalTime.MAX)

    private fun createEndTimeSlot() = TimeSlot(endDate, LocalTime.MIN, endTime)
}
