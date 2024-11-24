package rkrk.reservation.unit.entity

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import rkrk.reservation.warehouse.reservation.adapter.output.entity.ReservationJpaEntity
import rkrk.reservation.warehouse.reservation.adapter.output.entity.toTimeLine
import rkrk.reservation.warehouse.reservation.domain.ReservationStatus
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import rkrk.reservation.warehouse.reservation.domain.TimeLine
import rkrk.reservation.warehouse.warehouse.adapter.output.entity.WareHouseJpaEntity
import rkrk.reservation.warehouse.warehouse.domain.WareHouse
import java.math.BigDecimal
import java.time.LocalDateTime

class ReservationJpaEntityTest {
    @Test
    @DisplayName("예약엔티티를 타임라인으로 바꿀때는 상태가 pending과 confirmed인것만 바꾼다")
    fun toTimeLineFilteringPendingAndConfirmed() {
        val wareHouseEntity = WareHouseJpaEntity("aa", 10, 100, mutableListOf())

        val wareHouse = WareHouse("aa", 10, 100, TimeLine())
        wareHouse.createAndAddReservation(
            "abcd",
            ReservationTime(
                LocalDateTime.of(2024, 10, 24, 10, 0),
                LocalDateTime.of(2024, 10, 24, 11, 0),
            ),
        )
        wareHouse.createAndAddReservation(
            "abcd",
            ReservationTime(
                LocalDateTime.of(2024, 10, 24, 12, 0),
                LocalDateTime.of(2024, 10, 24, 13, 0),
            ),
        )
        val reservationJpaEntities = mutableListOf<ReservationJpaEntity>()
        reservationJpaEntities.add(
            ReservationJpaEntity(
                "testMember",
                LocalDateTime.of(2024, 10, 24, 12, 0),
                LocalDateTime.of(2024, 10, 24, 13, 0),
                BigDecimal("100"),
                ReservationStatus.PENDING,
                wareHouseEntity,
            ),
        )
        reservationJpaEntities.add(
            ReservationJpaEntity(
                "testMember",
                LocalDateTime.of(2024, 10, 25, 12, 0),
                LocalDateTime.of(2024, 10, 25, 13, 0),
                BigDecimal("100"),
                ReservationStatus.CONFIRMED,
                wareHouseEntity,
            ),
        )
        reservationJpaEntities.add(
            ReservationJpaEntity(
                "testMember",
                LocalDateTime.of(2024, 10, 26, 12, 0),
                LocalDateTime.of(2024, 10, 26, 13, 0),
                BigDecimal("100"),
                ReservationStatus.CANCELLED,
                wareHouseEntity,
            ),
        )
        reservationJpaEntities.add(
            ReservationJpaEntity(
                "testMember",
                LocalDateTime.of(2024, 10, 26, 12, 0),
                LocalDateTime.of(2024, 10, 26, 13, 0),
                BigDecimal("100"),
                ReservationStatus.REFUNDED,
                wareHouseEntity,
            ),
        )

        val timeLine = reservationJpaEntities.toTimeLine()

        Assertions.assertThat(timeLine.reservations.size).isEqualTo(2)

        Assertions
            .assertThat(
                timeLine.reservations
                    .filter {
                        it.state == ReservationStatus.PENDING ||
                            it.state == ReservationStatus.CONFIRMED
                    }.size,
            ).isEqualTo(2)
    }
}
