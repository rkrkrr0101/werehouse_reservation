package rkrk.reservation.warehouse.reservation.application.port.input

import rkrk.reservation.warehouse.reservation.application.port.input.dto.request.RequestCreatePendingReservationDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.request.RequestUpdateCancelReservationDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.request.RequestUpdateConfirmReservationDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.request.RequestUpdateRefundReservationDto

interface ManageReservationUseCase {
    fun createPendingReservation(dto: RequestCreatePendingReservationDto)

    fun updateConfirmReservation(dto: RequestUpdateConfirmReservationDto)

    fun updateCancelReservation(dto: RequestUpdateCancelReservationDto)

    fun updateRefundReservation(dto: RequestUpdateRefundReservationDto)
}
