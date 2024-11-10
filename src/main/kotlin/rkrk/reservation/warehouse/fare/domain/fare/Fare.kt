package rkrk.reservation.warehouse.fare.domain.fare

import java.math.BigDecimal

interface Fare {
    fun calFare(
        minute: Long,
        minuteRate: Long,
    ): BigDecimal
}
