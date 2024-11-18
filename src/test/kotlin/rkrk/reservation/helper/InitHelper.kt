package rkrk.reservation.helper

import rkrk.reservation.warehouse.reservation.adapter.output.ReservationJpaRepository
import rkrk.reservation.warehouse.reservation.domain.Reservation
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import rkrk.reservation.warehouse.reservation.domain.TimeLine
import rkrk.reservation.warehouse.warehouse.adapter.output.WareHouseJpaRepository
import rkrk.reservation.warehouse.warehouse.adapter.output.entity.toNewEntity
import rkrk.reservation.warehouse.warehouse.domain.WareHouse
import java.time.LocalDateTime

class InitHelper {
    fun getWareHouseName(): String = "testWareHouse"

    fun getMemberName(): String = "testMember"

    fun basicInit(wareHouseJpaRepository: WareHouseJpaRepository) {
        val wareHouse =
            WareHouse(
                getWareHouseName(),
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
                getMemberName(),
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
                getMemberName(),
                reservationTime2,
                wareHouse,
            )
        wareHouse.addReservation(reservation1)
        wareHouse.addReservation(reservation2)

        val wareHouseJpaEntity = wareHouse.toNewEntity()

        wareHouseJpaRepository.save(wareHouseJpaEntity)
    }

    fun basicClear(
        wareHouseJpaRepository: WareHouseJpaRepository,
        reservationJpaRepository: ReservationJpaRepository,
    ) {
        reservationJpaRepository.deleteAll()
        wareHouseJpaRepository.deleteAll()
    }
}
