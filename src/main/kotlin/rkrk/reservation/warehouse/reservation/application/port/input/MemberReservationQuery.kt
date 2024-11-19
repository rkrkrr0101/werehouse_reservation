package rkrk.reservation.warehouse.reservation.application.port.input

import rkrk.reservation.warehouse.reservation.application.port.input.dto.request.RequestFindReservationByMemberDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.response.ResponseFindMemberReservationDto

interface MemberReservationQuery {
    fun findMemberReservation(dto: RequestFindReservationByMemberDto): List<ResponseFindMemberReservationDto>
}
