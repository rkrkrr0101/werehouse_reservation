package rkrk.reservation.warehouse.reservation.application.port.input

import rkrk.reservation.warehouse.reservation.application.port.input.dto.FindMemberReservationDto
import rkrk.reservation.warehouse.reservation.domain.Reservation

interface MemberReservationQuery {
    fun findMemberReservation(dto: FindMemberReservationDto): List<Reservation>
}
