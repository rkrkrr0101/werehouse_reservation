package rkrk.reservation.integration.adapter

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import rkrk.reservation.helper.InitHelper
import rkrk.reservation.helper.SpringTestContainerTestWithTransactional
import rkrk.reservation.warehouse.reservation.adapter.output.ReservationJpaRepository
import rkrk.reservation.warehouse.reservation.adapter.output.UpdateReservationAdapter
import rkrk.reservation.warehouse.reservation.domain.ReservationStatus
import rkrk.reservation.warehouse.warehouse.adapter.output.WareHouseJpaRepository

@SpringTestContainerTestWithTransactional
class UpdateReservationAdapterTest(
    @Autowired val wareHouseJpaRepository: WareHouseJpaRepository,
    @Autowired val reservationJpaRepository: ReservationJpaRepository,
    @Autowired val updateReservationAdapter: UpdateReservationAdapter,
) {
    private val initHelper = InitHelper()

    @Test
    @DisplayName("예약의 상태를 변경할수 있다")
    fun updateReservationState() {
        initHelper.basicInit(wareHouseJpaRepository)
        val wareHouseJpaEntity =
            wareHouseJpaRepository.findByName("testWareHouse")
                ?: throw RuntimeException("warehouse 엔티티를 찾을수없음")
        val wareHouse = wareHouseJpaEntity.toDomain()
        val reservation = wareHouse.retrieveTotalReservations().reservations[0]

        reservation.updateState(ReservationStatus.CONFIRMED)
        updateReservationAdapter.updateReservation(wareHouse, reservation)

        val reservationJpaEntity =
            reservationJpaRepository.find(
                wareHouseJpaEntity.id,
                "testMember",
                reservation.reservationTime.startDateTime,
                reservation.reservationTime.endDateTime,
            ) ?: throw RuntimeException("reservation 엔티티를 찾을수없음")

        Assertions.assertThat(reservationJpaEntity.state).isEqualTo(ReservationStatus.CONFIRMED)
    }
}
