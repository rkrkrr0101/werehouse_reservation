package rkrk.reservation.warehouse.warehouse.adapter.output

import org.springframework.stereotype.Repository
import rkrk.reservation.warehouse.warehouse.application.port.output.UpdateWareHousePort
import rkrk.reservation.warehouse.warehouse.domain.WareHouse

@Repository
class UpdateWareHouseAdapter(
    private val repository: WareHouseRepository,
) : UpdateWareHousePort {
    override fun update(wareHouse: WareHouse) {
        val wareHouseEntity = repository.findByName(wareHouse.retrieveName())
        wareHouseEntity.update(wareHouse)
    }
}
