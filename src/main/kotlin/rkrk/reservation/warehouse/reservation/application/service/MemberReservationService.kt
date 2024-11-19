package rkrk.reservation.warehouse.reservation.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import rkrk.reservation.warehouse.reservation.application.port.input.MemberReservationQuery
import rkrk.reservation.warehouse.reservation.application.port.input.dto.request.RequestFindReservationByMemberDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.response.ResponseFindMemberReservationDto
import rkrk.reservation.warehouse.reservation.application.port.output.FindReservationPort

@Service
@Transactional(readOnly = true)
class MemberReservationService(
    private val findReservationPort: FindReservationPort,
) : MemberReservationQuery {
    override fun findMemberReservation(dto: RequestFindReservationByMemberDto): List<ResponseFindMemberReservationDto> {
        val resList = mutableListOf<ResponseFindMemberReservationDto>()
        findReservationPort.findMember(dto.memberName).forEach {
            resList.add(
                ResponseFindMemberReservationDto(
                    it.warehouseName,
                    it.startDateTime,
                    it.endDateTime,
                    it.totalFare,
                    it.state,
                ),
            )
        }
        return resList
    }
}
