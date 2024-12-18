package rkrk.reservation.unit.entity

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import rkrk.reservation.warehouse.reservation.domain.TimeLine
import rkrk.reservation.warehouse.warehouse.adapter.output.entity.WareHouseJpaEntity
import rkrk.reservation.warehouse.warehouse.domain.WareHouse
import java.time.LocalDateTime

class WareHouseJpaEntityTest {
    @Test
    @DisplayName("창고에 예약을 추가하고 성공적으로 업데이트할수있다")
    fun addReservation() {
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

        wareHouseEntity.update(wareHouse)

        Assertions.assertThat(wareHouseEntity.reservationJpaEntities.size).isEqualTo(2)
    }

    @Test
    @DisplayName("창고엔티티에 예약을 추가하면,예약엔티티에서도 창고엔티티가 추가된다")
    fun addWareHouseToReservationUpdateReservationToWareHouse() {
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

        wareHouseEntity.update(wareHouse)

        Assertions.assertThat(wareHouseEntity.reservationJpaEntities.size).isEqualTo(2)
        Assertions.assertThat(wareHouseEntity.reservationJpaEntities[0].wareHouse).isEqualTo(wareHouseEntity)
        Assertions.assertThat(wareHouseEntity.reservationJpaEntities[1].wareHouse).isEqualTo(wareHouseEntity)
    }
}
