package rkrk.reservation.warehouse.warehouse.application.port.output

import rkrk.reservation.warehouse.warehouse.domain.WareHouse

interface SaveWareHousePort {
    fun save(wareHouse: WareHouse)
}
