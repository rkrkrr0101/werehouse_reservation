package rkrk.reservation.warehouse.reservation.domain

import java.time.LocalDate
import java.time.LocalTime

// 2일이 아닌 3일이상 겹칠때?
// 분할의 책임을 타임슬롯 바깥으로 넘김,3일이상이든 뭐든 첫날+중간+마지막날으로 분리할책임은 서비스에있음
// 분할용 함수를 컴패니언오브젝트로 생성?
data class TimeSlot(
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
) {
    init {
        require(startTime != endTime) { "시작시간과 종료시간이 같음" }
    }

    val isFullTime: Boolean = (this.startTime == LocalTime.MIN) && (this.endTime == LocalTime.MAX)

    fun overlapsWith(other: TimeSlot): TimeOverlapStatus =
        when {
            this.date != other.date -> TimeOverlapStatus.NON_OVERLAPPING
            (this.startTime < other.endTime) && (this.endTime > other.startTime) // 함수화?
            -> TimeOverlapStatus.OVERLAPPING

            else -> TimeOverlapStatus.NON_OVERLAPPING
        }

//    override operator fun compareTo(other: TimeSlot): Int {
//        val thisDateTime = LocalDateTime.of(this.date, this.endTime)
//        val otherDateTime = LocalDateTime.of(other.date, other.endTime)
//        return when {
//            otherDateTime > thisDateTime -> -1
//            otherDateTime < thisDateTime -> 1
//            else -> 0
//        }
//    }

    companion object {
        fun createFullTime(date: LocalDate): TimeSlot = TimeSlot(date, LocalTime.MIN, LocalTime.MAX)
    }
}
