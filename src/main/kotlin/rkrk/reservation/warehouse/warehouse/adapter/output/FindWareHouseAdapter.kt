package rkrk.reservation.warehouse.warehouse.adapter.output

import org.springframework.stereotype.Repository
import rkrk.reservation.warehouse.reservation.adapter.output.ReservationRepository
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import rkrk.reservation.warehouse.warehouse.application.port.output.FindWareHousePort
import rkrk.reservation.warehouse.warehouse.domain.WareHouse

@Repository
class FindWareHouseAdapter(
    private val wareHouseRepository: WareHouseRepository,
    private val reservationRepository: ReservationRepository,
) : FindWareHousePort {
    override fun findAndLockWarehouseByReservationTime(
        warehouseName: String,
        reservationTime: ReservationTime,
    ): WareHouse {
        val wareHouseJpaEntity = wareHouseRepository.findByName(warehouseName)
        reservationRepository
            .findAndLockWarehouseByReservationTime(wareHouseJpaEntity.id, reservationTime)
        return wareHouseJpaEntity.toDomain()
    }

    override fun findWarehouseByName(warehouseName: String): WareHouse =
        wareHouseRepository
            .findByName(warehouseName)
            .toDomain()
}
