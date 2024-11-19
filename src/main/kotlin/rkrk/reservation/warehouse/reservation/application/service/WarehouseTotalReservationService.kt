package rkrk.reservation.warehouse.reservation.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import rkrk.reservation.warehouse.reservation.application.port.input.WarehouseTotalReservationQuery
import rkrk.reservation.warehouse.reservation.application.port.input.dto.RequestTotalReservationByWarehouseDto

@Service
@Transactional(readOnly = true)
class WarehouseTotalReservationService : WarehouseTotalReservationQuery {
    // 리턴값 dto로 변경
    override fun findWarehouseTotalReservation(dto: RequestTotalReservationByWarehouseDto) {
        TODO("Not yet implemented")
    }
}
