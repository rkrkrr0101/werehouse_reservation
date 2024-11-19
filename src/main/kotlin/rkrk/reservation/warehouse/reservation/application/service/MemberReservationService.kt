package rkrk.reservation.warehouse.reservation.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import rkrk.reservation.warehouse.reservation.application.port.input.MemberReservationQuery
import rkrk.reservation.warehouse.reservation.application.port.input.dto.RequestReservationByMemberDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.ResultMemberReservationDto
import rkrk.reservation.warehouse.reservation.application.port.output.FindReservationPort

@Service
@Transactional(readOnly = true)
class MemberReservationService(
    private val findReservationPort: FindReservationPort,
) : MemberReservationQuery {
    override fun findMemberReservation(dto: RequestReservationByMemberDto): List<ResultMemberReservationDto> {
        val resList = mutableListOf<ResultMemberReservationDto>()
        findReservationPort.findMember(dto.memberName).forEach {
            resList.add(
                ResultMemberReservationDto(
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
