package rkrk.reservation.warehouse.reservation.domain

// 한 창고에 대한 총 예약시간
// 생성자로 받은 시간을 가지고 타임슬롯리스트를 생성할수있음
// 타임슬롯리스트는 end시간으로 정렬되어있어야함(바이너리서치)
// 오버랩되지않는지 확인하고 추가해야함(해당위치로 가서,왼쪽의 end와 대상의 start를 비교,오른쪽의 start와 대상의 end를 비교

class TimeLine {
    private val privateReservations = mutableListOf<Reservation>()
    val reservations: List<Reservation> get() = privateReservations.toList()

    fun addReservation(reservation: Reservation) {
        check(overlapCheck(reservation.reservationTime) == TimeOverlapStatus.NON_OVERLAPPING) {
            "Time OverLapping"
        }

        privateReservations.add(reservation)
        privateReservations.sortBy { it.reservationTime.endDateTime }
    }

    fun overlapCheck(otherTime: ReservationTime): TimeOverlapStatus {
        val rawIndex = findAddIndex(otherTime)
        if (rawIndex >= 0) return TimeOverlapStatus.OVERLAPPING
        val position = convertToInsertPosition(rawIndex) // 바이너리서치값 정상화

        val leftOverlap = leftOverlapCheck(position, otherTime)
        val rightOverlap = rightOverlapCheck(position, otherTime)
        val fullOverlap = fullOverlapCheck(position, otherTime)

        if (leftOverlap == TimeOverlapStatus.NON_OVERLAPPING &&
            rightOverlap == TimeOverlapStatus.NON_OVERLAPPING &&
            fullOverlap == TimeOverlapStatus.NON_OVERLAPPING
        ) {
            return TimeOverlapStatus.NON_OVERLAPPING
        }

        return TimeOverlapStatus.OVERLAPPING
    }

    private fun convertToInsertPosition(rawIndex: Int) = -(rawIndex + 1)

    private fun findAddIndex(addTime: ReservationTime): Int =
        privateReservations
            .binarySearch {
                it.reservationTime.endDateTime.compareTo(addTime.endDateTime)
            }

    private fun leftOverlapCheck(
        position: Int,
        otherTime: ReservationTime,
    ): TimeOverlapStatus {
        if (position <= 0) return TimeOverlapStatus.NON_OVERLAPPING
        val leftTime = this.privateReservations[position - 1]
        val leftEndTimeSlot = leftTime.reservationTime.endTimeSlot()
        val otherStartTimeSlot = otherTime.startTimeSlot()
        if (leftEndTimeSlot.date != otherStartTimeSlot.date) return TimeOverlapStatus.NON_OVERLAPPING

        val overlap =
            leftEndTimeSlot.endTime > otherStartTimeSlot.startTime

        return toOverlapStatus(overlap)
    }

    private fun rightOverlapCheck(
        position: Int,
        otherTime: ReservationTime,
    ): TimeOverlapStatus {
        if (position >= this.privateReservations.size) return TimeOverlapStatus.NON_OVERLAPPING

        val rightTime = this.privateReservations[position]
        val rightStartTimeSlot = rightTime.reservationTime.startTimeSlot()
        val otherEndTimeSlot = otherTime.endTimeSlot()

        if (rightStartTimeSlot.date != otherEndTimeSlot.date) return TimeOverlapStatus.NON_OVERLAPPING

        val overlap =
            otherEndTimeSlot.endTime > rightStartTimeSlot.startTime

        return toOverlapStatus(overlap)
    }

    private fun fullOverlapCheck(
        position: Int,
        otherTime: ReservationTime,
    ): TimeOverlapStatus {
        if (position >= this.privateReservations.size) return TimeOverlapStatus.NON_OVERLAPPING

        val rightTime = this.privateReservations[position]
        val rightStartTime = rightTime.reservationTime.startDateTime
        val rightEndTime = rightTime.reservationTime.endDateTime
        val otherStartTime = otherTime.startDateTime
        val otherEndTime = otherTime.endDateTime

        val overlap = ((rightStartTime < otherStartTime) && (rightEndTime > otherEndTime))

        return toOverlapStatus(overlap)
    }

    private fun toOverlapStatus(overlap: Boolean) =
        when {
            overlap -> TimeOverlapStatus.OVERLAPPING
            else -> TimeOverlapStatus.NON_OVERLAPPING
        }
}
