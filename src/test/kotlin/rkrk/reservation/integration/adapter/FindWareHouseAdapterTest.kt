package rkrk.reservation.integration.adapter

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import rkrk.reservation.helper.InitHelper
import rkrk.reservation.helper.SpringTestContainerTestWithTransactional
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import rkrk.reservation.warehouse.warehouse.adapter.output.FindWareHouseAdapter
import rkrk.reservation.warehouse.warehouse.adapter.output.WareHouseJpaRepository
import java.time.LocalDateTime

@SpringTestContainerTestWithTransactional
class FindWareHouseAdapterTest(
    @Autowired private val wareHouseJpaRepository: WareHouseJpaRepository,
    @Autowired private val findWareHouseAdapter: FindWareHouseAdapter,
) {
    private val initHelper = InitHelper()

    @Test
    @DisplayName("창고를 창고이름으로 조회할수있다")
    fun findWareHouse() {
        initHelper.basicInit(wareHouseJpaRepository)

        val wareHouse = findWareHouseAdapter.findWarehouseByName(initHelper.getWareHouseName())

        Assertions.assertThat(wareHouse.retrieveName()).isEqualTo(initHelper.getWareHouseName())
        Assertions
            .assertThat(wareHouse.retrieveTotalReservations().reservations.size)
            .isEqualTo(2)
    }

    @Test
    @DisplayName("정상적으로 락을걸며 창고를 조회할수있다")
    fun findAndLockWareHouse() {
        initHelper.basicInit(wareHouseJpaRepository)

        val reservationTime =
            ReservationTime(
                LocalDateTime.of(2024, 10, 24, 16, 0),
                LocalDateTime.of(2024, 10, 24, 17, 0),
            )

        val wareHouse =
            findWareHouseAdapter.findAndLockWarehouseByReservationTime(
                initHelper.getWareHouseName(),
                reservationTime,
            )

        Assertions.assertThat(wareHouse.retrieveName()).isEqualTo(initHelper.getWareHouseName())
    }
}
