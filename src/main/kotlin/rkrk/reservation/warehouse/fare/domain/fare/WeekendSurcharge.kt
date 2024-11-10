package rkrk.reservation.warehouse.fare.domain.fare

import java.math.BigDecimal

class WeekendSurcharge : Fare {
    private val surchargeRate = 0.5

    override fun calFare(
        minute: Long,
        minuteRate: Long,
    ): BigDecimal = BigDecimal.valueOf(minute * minuteRate * surchargeRate)
}
