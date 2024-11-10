package rkrk.reservation.warehouse.reservation.domain

// 한 창고에 대한 총 예약시간
// 생성자로 받은 시간을 가지고 타임슬롯리스트를 생성할수있음
// 타임슬롯리스트는 end시간으로 정렬되어있어야함(바이너리서치)
// 오버랩되지않는지 확인하고 추가해야함(해당위치로 가서,왼쪽의 end와 대상의 start를 비교,오른쪽의 start와 대상의 end를 비교

class TimeLine {
    private val privateReservationTimes = mutableListOf<ReservationTime>()
    val reservationTimes: List<ReservationTime> get() = privateReservationTimes.toList()

    fun addTime(addTime: ReservationTime) {
        check(overlapCheck(addTime) == TimeOverlapStatus.NON_OVERLAPPING) {
            "Time OverLapping"
        }
        privateReservationTimes.add(addTime)
        privateReservationTimes.sortBy { it.endDateTime }
    }

    private fun overlapCheck(otherTime: ReservationTime): TimeOverlapStatus {
        val position = findAddPosition(otherTime)

        val leftOverlap = leftOverlapCheck(position, otherTime)
        val rightOverlap = rightOverlapCheck(position, otherTime)

        if (leftOverlap == TimeOverlapStatus.NON_OVERLAPPING &&
            rightOverlap == TimeOverlapStatus.NON_OVERLAPPING
        ) {
            return TimeOverlapStatus.NON_OVERLAPPING
        }

        return TimeOverlapStatus.OVERLAPPING
    }

    private fun findAddPosition(addTime: ReservationTime): Int =
        privateReservationTimes
            .binarySearch {
                it.endDateTime.compareTo(addTime.endDateTime)
            }.let { if (it < 0) -(it + 1) else it } // 삼항연산풀기?

    private fun leftOverlapCheck(
        position: Int,
        otherTime: ReservationTime,
    ): TimeOverlapStatus {
        if (position <= 0) return TimeOverlapStatus.NON_OVERLAPPING

        val leftTime = this.privateReservationTimes[position - 1]
        val overlap =
            leftTime.endTimeSlot().endTime < otherTime.startTimeSlot().startTime

        return toOverlapStatus(overlap)
    }

    private fun rightOverlapCheck(
        position: Int,
        otherTime: ReservationTime,
    ): TimeOverlapStatus {
        if (position >= this.privateReservationTimes.size) return TimeOverlapStatus.NON_OVERLAPPING

        val rightTime = this.privateReservationTimes[position]
        val overlap =
            otherTime.endTimeSlot().endTime > rightTime.startTimeSlot().startTime

        return toOverlapStatus(overlap)
    }

    private fun toOverlapStatus(overlap: Boolean) =
        when {
            overlap -> TimeOverlapStatus.OVERLAPPING
            else -> TimeOverlapStatus.NON_OVERLAPPING
        }
}
