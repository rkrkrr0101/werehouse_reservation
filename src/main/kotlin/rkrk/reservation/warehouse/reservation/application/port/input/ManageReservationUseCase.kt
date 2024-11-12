package rkrk.reservation.warehouse.reservation.application.port.input

import rkrk.reservation.warehouse.reservation.application.port.input.dto.CreatePendingReservationDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.UpdateCancelReservationDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.UpdateConfirmReservationDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.UpdateRefundReservationDto

interface ManageReservationUseCase {
    fun createPendingReservation(dto: CreatePendingReservationDto)

    fun updateConfirmReservation(dto: UpdateConfirmReservationDto)

    fun updateCancelReservation(dto: UpdateCancelReservationDto)

    fun updateRefundReservation(dto: UpdateRefundReservationDto)
}
