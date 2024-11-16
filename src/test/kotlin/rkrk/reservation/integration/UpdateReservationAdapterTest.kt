package rkrk.reservation.integration

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import rkrk.reservation.helper.SpringTestContainerTest
import rkrk.reservation.warehouse.reservation.adapter.output.ReservationJpaRepository
import rkrk.reservation.warehouse.reservation.adapter.output.UpdateReservationAdapter
import rkrk.reservation.warehouse.reservation.domain.Reservation
import rkrk.reservation.warehouse.reservation.domain.ReservationStatus
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import rkrk.reservation.warehouse.reservation.domain.TimeLine
import rkrk.reservation.warehouse.warehouse.adapter.output.WareHouseJpaRepository
import rkrk.reservation.warehouse.warehouse.adapter.output.entity.WareHouseJpaEntity
import rkrk.reservation.warehouse.warehouse.domain.WareHouse
import java.time.LocalDateTime

@SpringTestContainerTest
@Transactional
class UpdateReservationAdapterTest(
    @Autowired val wareHouseRepository: WareHouseJpaRepository,
    @Autowired val reservationRepository: ReservationJpaRepository,
    @Autowired val updateReservationAdapter: UpdateReservationAdapter,
) {
    @Test
    @DisplayName("예약의 상태를 변경할수 있다")
    fun updateReservationState() {
        basicInit()
        val wareHouseJpaEntity =
            wareHouseRepository.findByName("testWareHouse")
                ?: throw RuntimeException("warehouse 엔티티를 찾을수없음")
        val wareHouse = wareHouseJpaEntity.toDomain()
        val reservation = wareHouse.retrieveTotalReservations().reservations[0]

        reservation.updateState(ReservationStatus.CONFIRMED)
        updateReservationAdapter.updateReservation(wareHouse, reservation)

        val reservationJpaEntity =
            reservationRepository.find(
                wareHouseJpaEntity.id,
                "testMember",
                reservation.reservationTime.startDateTime,
                reservation.reservationTime.endDateTime,
            ) ?: throw RuntimeException("reservation 엔티티를 찾을수없음")

        Assertions.assertThat(reservationJpaEntity.state).isEqualTo(ReservationStatus.CONFIRMED)
    }

    private fun basicInit() {
        val wareHouse =
            WareHouse(
                "testWareHouse",
                1000,
                100,
                TimeLine(),
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
                wareHouse,
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
                wareHouse,
            )
        wareHouse.addReservation(reservation1)
        wareHouse.addReservation(reservation2)

        val wareHouseJpaEntity =
            WareHouseJpaEntity(
                "testWareHouse",
                1000,
                100,
                mutableListOf(),
            )

        wareHouseJpaEntity.update(wareHouse)

        wareHouseRepository.save(wareHouseJpaEntity)
    }
}
