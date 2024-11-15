package rkrk.reservation.warehouse.warehouse.application.port.output

import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import rkrk.reservation.warehouse.warehouse.domain.WareHouse

interface FindWareHousePort {
    fun findAndLockWarehouseByReservationTime(
        warehouseName: String,
        reservationTime: ReservationTime,
    ): WareHouse

    fun findWarehouseByName(warehouseName: String): WareHouse
}
