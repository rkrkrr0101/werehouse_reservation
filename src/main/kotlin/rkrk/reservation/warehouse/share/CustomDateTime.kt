package rkrk.reservation.warehouse.share

import java.time.LocalDateTime

interface CustomDateTime {
    fun getNow(): LocalDateTime
}
