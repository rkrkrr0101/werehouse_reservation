package rkrk.reservation.integration

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import rkrk.reservation.helper.InitHelper
import rkrk.reservation.helper.SpringTestContainerTest
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import rkrk.reservation.warehouse.warehouse.adapter.output.UpdateWareHouseAdapter
import rkrk.reservation.warehouse.warehouse.adapter.output.WareHouseJpaRepository
import java.time.LocalDateTime

@SpringTestContainerTest
class UpdateWareHouseAdapterTest(
    @Autowired val wareHouseJpaRepository: WareHouseJpaRepository,
    @Autowired val updateWareHouseAdapter: UpdateWareHouseAdapter,
) {
    private val initHelper = InitHelper()

    @Test
    @DisplayName("정상적으로 창고의 예약을 추가후 업데이트할수있다")
    fun updateWareHouseToAddReservation() {
        initHelper.basicInit(wareHouseJpaRepository)
        val wareHouseJpaEntity =
            wareHouseJpaRepository.findByName("testWareHouse")
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
            wareHouseJpaRepository.findByName("testWareHouse")
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
}
