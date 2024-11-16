package rkrk.reservation.helper

import rkrk.reservation.warehouse.reservation.domain.Reservation
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import rkrk.reservation.warehouse.reservation.domain.TimeLine
import rkrk.reservation.warehouse.warehouse.adapter.output.WareHouseJpaRepository
import rkrk.reservation.warehouse.warehouse.adapter.output.entity.WareHouseJpaEntity
import rkrk.reservation.warehouse.warehouse.domain.WareHouse
import java.time.LocalDateTime

class InitHelper {
    fun basicInit(wareHouseJpaRepository: WareHouseJpaRepository) {
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

        wareHouseJpaRepository.save(wareHouseJpaEntity)
    }
}
