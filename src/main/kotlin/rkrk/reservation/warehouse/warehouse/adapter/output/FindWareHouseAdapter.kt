package rkrk.reservation.warehouse.warehouse.adapter.output

import org.springframework.stereotype.Repository
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import rkrk.reservation.warehouse.warehouse.application.port.output.FindWareHousePort
import rkrk.reservation.warehouse.warehouse.domain.WareHouse

@Repository
class FindWareHouseAdapter(
    private val repository: WareHouseRepository,
) : FindWareHousePort {
    override fun findAndLockWarehouseForReservationTime(
        warehouseName: String,
        reservationTime: ReservationTime,
    ): WareHouse {
        TODO("Not yet implemented")
    }

    override fun findWarehouseByName(warehouseName: String): WareHouse {
        TODO("Not yet implemented")
    }
}
