package rkrk.reservation.warehouse.share

import java.time.LocalDateTime

class CustomDateTimeImpl : CustomDateTime {
    override fun getNow(): LocalDateTime = LocalDateTime.now()

    override fun toString(): String = LocalDateTime.now().toString()
}
