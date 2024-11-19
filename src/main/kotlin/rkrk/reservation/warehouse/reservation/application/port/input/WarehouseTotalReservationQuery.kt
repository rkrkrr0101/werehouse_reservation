package rkrk.reservation.warehouse.reservation.application.port.input

import rkrk.reservation.warehouse.reservation.application.port.input.dto.request.RequestFindTotalReservationByWarehouseDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.response.ResponseFindTotalReservationByWareHouseDto

interface WarehouseTotalReservationQuery {
    fun findWarehouseTotalReservation(dto: RequestFindTotalReservationByWarehouseDto): List<ResponseFindTotalReservationByWareHouseDto>
}
