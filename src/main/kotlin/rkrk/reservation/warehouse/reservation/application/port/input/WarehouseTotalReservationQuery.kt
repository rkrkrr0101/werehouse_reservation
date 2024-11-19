package rkrk.reservation.warehouse.reservation.application.port.input

import rkrk.reservation.warehouse.reservation.application.port.input.dto.RequestTotalReservationByWarehouseDto

interface WarehouseTotalReservationQuery {
    fun findWarehouseTotalReservation(dto: RequestTotalReservationByWarehouseDto)
}
