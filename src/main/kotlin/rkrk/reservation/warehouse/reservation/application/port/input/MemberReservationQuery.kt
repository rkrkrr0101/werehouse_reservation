package rkrk.reservation.warehouse.reservation.application.port.input

import rkrk.reservation.warehouse.reservation.application.port.input.dto.RequestReservationByMemberDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.ResultMemberReservationDto

interface MemberReservationQuery {
    fun findMemberReservation(dto: RequestReservationByMemberDto): List<ResultMemberReservationDto>
}
