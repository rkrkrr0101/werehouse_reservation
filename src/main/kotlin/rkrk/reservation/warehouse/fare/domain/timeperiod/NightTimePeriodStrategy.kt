package rkrk.reservation.warehouse.fare.domain.timeperiod

import rkrk.reservation.warehouse.reservation.domain.TimeSlot
import rkrk.reservation.warehouse.share.Constant

// 시작시간 종료시간 야간시작 야간종료
// 시간이 야간
// 이게 reservationTime이 아닌 TimeSlot으로 처리해야 다른거랑 같이불일수있을거같은데
// 안그러면 주말할증같은걸 처리할수있나?

class NightTimePeriodStrategy : TimePeriodStrategy() {
    override fun calTotalTime(timeSlot: TimeSlot): Long = calNightMinute(timeSlot)

    private fun calNightMinute(timeSlot: TimeSlot): Long {
        var resTime = 0L
        resTime += calPositiveMinutes(timeSlot.startTime, minOf(timeSlot.endTime, Constant.END_NIGHT_TIME))
        resTime += calPositiveMinutes(maxOf(Constant.START_NIGHT_TIME, timeSlot.startTime), timeSlot.endTime)
        return resTime
    }
}
