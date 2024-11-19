package rkrk.reservation.integration.service

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import rkrk.reservation.helper.InitHelper
import rkrk.reservation.helper.SpringTestContainerTestWithTransactional
import rkrk.reservation.warehouse.reservation.application.port.output.FindReservationPort
import rkrk.reservation.warehouse.reservation.domain.Reservation
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import rkrk.reservation.warehouse.reservation.domain.TimeLine
import rkrk.reservation.warehouse.warehouse.adapter.output.WareHouseJpaRepository
import rkrk.reservation.warehouse.warehouse.adapter.output.entity.toNewEntity
import rkrk.reservation.warehouse.warehouse.domain.WareHouse
import java.time.LocalDateTime

@SpringTestContainerTestWithTransactional
class FindReservationByMemberTest(
    @Autowired val findReservationPort: FindReservationPort,
    @Autowired val wareHouseJpaRepository: WareHouseJpaRepository,
) {
    val initHelper = InitHelper()
    val otherWareHouseName = "otherWareHouse"
    val otherMemberName = "otherMemberName"

    @Test
    @DisplayName("멤버의 예약기록을 조회할수있다")
    fun findReservationByMember() {
        memberInit(wareHouseJpaRepository)
        val dtos = findReservationPort.findMember(initHelper.getMemberName())

        Assertions.assertThat(dtos).hasSize(3)
        Assertions.assertThat(dtos.filter { it.warehouseName == initHelper.getWareHouseName() }).hasSize(2)
        Assertions.assertThat(dtos.filter { it.warehouseName == otherWareHouseName }).hasSize(1)
    }

    private fun memberInit(wareHouseJpaRepository: WareHouseJpaRepository) {
        initHelper.basicInit(wareHouseJpaRepository)

        val otherWareHouse =
            WareHouse(
                otherWareHouseName,
                1000,
                100,
                TimeLine(),
            )
        val otherMemberReservationTime =
            ReservationTime(
                LocalDateTime.of(2024, 10, 25, 10, 30),
                LocalDateTime.of(2024, 10, 25, 12, 30),
            )
        val otherMemberReservation =
            Reservation(
                otherMemberName,
                otherMemberReservationTime,
                otherWareHouse,
            )

        val reservationTime1 =
            ReservationTime(
                LocalDateTime.of(2024, 10, 26, 10, 30),
                LocalDateTime.of(2024, 10, 26, 12, 30),
            )
        val reservation1 =
            Reservation(
                initHelper.getMemberName(),
                reservationTime1,
                otherWareHouse,
            )
        val reservationTime2 =
            ReservationTime(
                LocalDateTime.of(2024, 10, 26, 13, 30),
                LocalDateTime.of(2024, 10, 26, 15, 30),
            )
        val reservation2 =
            Reservation(
                otherMemberName,
                reservationTime2,
                otherWareHouse,
            )
        otherWareHouse.addReservation(otherMemberReservation)
        otherWareHouse.addReservation(reservation1)
        otherWareHouse.addReservation(reservation2)

        val wareHouseJpaEntity = otherWareHouse.toNewEntity()

        wareHouseJpaRepository.save(wareHouseJpaEntity)
    }
}
