package rkrk.reservation.integration

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import rkrk.reservation.helper.SpringTestContainerTest
import rkrk.reservation.warehouse.reservation.domain.Reservation
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import rkrk.reservation.warehouse.warehouse.adapter.output.UpdateWareHouseAdapter
import rkrk.reservation.warehouse.warehouse.adapter.output.WareHouseJpaRepository
import rkrk.reservation.warehouse.warehouse.adapter.output.entity.WareHouseJpaEntity
import java.time.LocalDateTime

@SpringTestContainerTest
class UpdateWareHouseAdapterTest(
    @Autowired val wareHouseRepository: WareHouseJpaRepository,
    @Autowired val updateWareHouseAdapter: UpdateWareHouseAdapter,
) {
    @Test
    @DisplayName("정상적으로 창고의 예약을 추가후 업데이트할수있다")
    fun updateWareHouseToAddReservation() {
        basicInit()
        val wareHouseJpaEntity =
            wareHouseRepository.findByName("testWareHouse")
                ?: throw RuntimeException()
        val wareHouse = wareHouseJpaEntity.toDomain()
        val startDateTime = LocalDateTime.of(2024, 10, 24, 16, 30)
        val endDateTime = LocalDateTime.of(2024, 10, 24, 18, 30)
        val reservationTime =
            ReservationTime(
                startDateTime,
                endDateTime,
            )
        wareHouse.createAndAddReservation(
            "testMember",
            reservationTime,
        )

        updateWareHouseAdapter.update(wareHouse)

        val resEntity =
            wareHouseRepository.findByName("testWareHouse")
                ?: throw RuntimeException()
        Assertions.assertThat(resEntity.reservationJpaEntities.size).isEqualTo(3)
        Assertions
            .assertThat(
                resEntity.reservationJpaEntities.find {
                    it.memberName == "testMember" &&
                        it.startDateTime == startDateTime &&
                        it.endDateTime == endDateTime &&
                        it.wareHouse == wareHouseJpaEntity
                },
            ).isNotNull
    }

    private fun basicInit() {
        val wareHouseJpaEntity =
            WareHouseJpaEntity(
                "testWareHouse",
                1000,
                100,
                mutableListOf(),
            )

        val reservationTime1 =
            ReservationTime(
                LocalDateTime.of(2024, 10, 24, 10, 30),
                LocalDateTime.of(2024, 10, 24, 12, 30),
            )
        val reservation1 =
            Reservation(
                "testMember",
                reservationTime1,
                wareHouseJpaEntity.toDomain(),
            )
        val reservationTime2 =
            ReservationTime(
                LocalDateTime.of(2024, 10, 24, 13, 30),
                LocalDateTime.of(2024, 10, 24, 15, 30),
            )
        val reservation2 =
            Reservation(
                "testMember",
                reservationTime2,
                wareHouseJpaEntity.toDomain(),
            )
        wareHouseJpaEntity.addReservation(reservation1)
        wareHouseJpaEntity.addReservation(reservation2)

        wareHouseRepository.save(wareHouseJpaEntity)
    }
}
