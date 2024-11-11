package rkrk.reservation.warehouse.reservation.application.port.input

import rkrk.reservation.warehouse.reservation.application.port.input.dto.FindWarehouseTotalReservationDto

interface WarehouseTotalReservationQuery {
    fun findWarehouseTotalReservation(dto: FindWarehouseTotalReservationDto)
}
