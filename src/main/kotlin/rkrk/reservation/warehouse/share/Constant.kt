package rkrk.reservation.warehouse.share

import java.time.LocalTime

class Constant {
    companion object {
        val START_NIGHT_TIME: LocalTime = LocalTime.of(22, 0) // 전역값?
        val END_NIGHT_TIME: LocalTime = LocalTime.of(4, 0) // 전역값?
    }
}
