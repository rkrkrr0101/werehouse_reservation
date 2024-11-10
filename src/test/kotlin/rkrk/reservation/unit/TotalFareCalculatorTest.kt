package rkrk.reservation.unit

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import rkrk.reservation.warehouse.fare.domain.service.TotalFareCalculator
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import java.math.BigDecimal
import java.time.LocalDateTime

class TotalFareCalculatorTest {
    @Test
    fun calculate() {
        val startDateTime = LocalDateTime.of(2024, 10, 25, 10, 30, 0)
        val endDateTime = LocalDateTime.of(2024, 10, 25, 13, 30, 0)
        val reservationTime =
            ReservationTime(
                startDateTime,
                endDateTime,
            )

        val totalPrice = TotalFareCalculator().calTotalFare(reservationTime, 100)

        Assertions.assertThat(totalPrice).isEqualTo(BigDecimal("18000.0"))
    }
}
