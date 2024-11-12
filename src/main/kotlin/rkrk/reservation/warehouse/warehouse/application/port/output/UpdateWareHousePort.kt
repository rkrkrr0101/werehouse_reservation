package rkrk.reservation.warehouse.warehouse.application.port.output

import rkrk.reservation.warehouse.warehouse.domain.WareHouse

interface UpdateWareHousePort {
    fun update(wareHouse: WareHouse)
}
