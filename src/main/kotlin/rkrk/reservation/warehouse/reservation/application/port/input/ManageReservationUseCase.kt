package rkrk.reservation.warehouse.reservation.application.port.input

import rkrk.reservation.warehouse.reservation.application.port.input.dto.CreatePendingReservationDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.UpdateCancelReservationDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.UpdateConfirmReservationDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.UpdateRefundReservationDto

interface ManageReservationUseCase {
    fun createPendingReservation(dto: CreatePendingReservationDto): Long

    fun updateConfirmReservation(dto: UpdateConfirmReservationDto): Long

    fun updateCancelReservation(dto: UpdateCancelReservationDto): Long

    fun updateRefundReservation(dto: UpdateRefundReservationDto): Long
}
