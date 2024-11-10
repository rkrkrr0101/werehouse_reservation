package rkrk.reservation.warehouse.fare.domain.fare

import java.math.BigDecimal

class NightSurcharge : Fare {
    private val surchargeRate = 0.2

    override fun calFare(
        minute: Long,
        minuteRate: Long,
    ): BigDecimal = BigDecimal.valueOf(minute * minuteRate * surchargeRate)
}
