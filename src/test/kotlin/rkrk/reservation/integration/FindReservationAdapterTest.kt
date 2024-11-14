package rkrk.reservation.integration

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import rkrk.reservation.helper.SpringTestContainerTest
import rkrk.reservation.warehouse.reservation.adapter.output.FindReservationAdapter
import rkrk.reservation.warehouse.reservation.domain.Reservation
import rkrk.reservation.warehouse.reservation.domain.ReservationStatus
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import rkrk.reservation.warehouse.share.exception.NotFoundEntityException
import rkrk.reservation.warehouse.warehouse.adapter.output.WareHouseJpaRepository
import rkrk.reservation.warehouse.warehouse.adapter.output.entity.WareHouseJpaEntity
import java.time.LocalDateTime

@SpringTestContainerTest
class FindReservationAdapterTest(
    @Autowired val wareHouseRepository: WareHouseJpaRepository,
    @Autowired val findReservationAdapter: FindReservationAdapter,
) {
    @Test
    @DisplayName("정상적으로 예약을 조회할수있다")
    fun findReservation() {
        basicInit()

        val reservationTime =
            ReservationTime(
                LocalDateTime.of(2024, 10, 24, 10, 30),
                LocalDateTime.of(2024, 10, 24, 12, 30),
            )
        val reservation =
            findReservationAdapter.find(
                "testWareHouse",
                "testMember",
                reservationTime,
            )

        Assertions.assertThat(reservation.reservationTime.startDateTime).isEqualTo(reservationTime.startDateTime)
        Assertions.assertThat(reservation.reservationTime.endDateTime).isEqualTo(reservationTime.endDateTime)
        Assertions.assertThat(reservation.state).isEqualTo(ReservationStatus.PENDING)
        Assertions.assertThat(reservation.memberName).isEqualTo("testMember")
    }

    @Test
    @DisplayName("없는예약을 조회하면 예외를 리턴한다")
    fun notFindReservation() {
        basicInit()

        val reservationTime =
            ReservationTime(
                LocalDateTime.of(1998, 10, 24, 10, 30),
                LocalDateTime.of(1998, 10, 24, 12, 30),
            )
        Assertions
            .assertThatThrownBy {
                findReservationAdapter.find(
                    "testWareHouse",
                    "testMember",
                    reservationTime,
                )
            }.isInstanceOf(NotFoundEntityException::class.java)
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
