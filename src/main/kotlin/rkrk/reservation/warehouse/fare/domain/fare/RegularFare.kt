package rkrk.reservation.warehouse.fare.domain.fare

import java.math.BigDecimal

class RegularFare : Fare {
    private val surchargeRate = 1.0

    override fun calFare(
        minute: Long,
        minuteRate: Long,
    ): BigDecimal = BigDecimal.valueOf(minute * minuteRate * surchargeRate)
}
