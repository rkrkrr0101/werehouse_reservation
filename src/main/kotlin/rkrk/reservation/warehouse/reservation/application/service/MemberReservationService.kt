package rkrk.reservation.warehouse.reservation.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import rkrk.reservation.warehouse.reservation.application.port.input.MemberReservationQuery
import rkrk.reservation.warehouse.reservation.application.port.input.dto.FindMemberReservationDto
import rkrk.reservation.warehouse.reservation.domain.Reservation

@Service
@Transactional(readOnly = true)
class MemberReservationService : MemberReservationQuery {
    // 리턴값 dto로 변경
    override fun findMemberReservation(dto: FindMemberReservationDto): List<Reservation> {
        TODO("Not yet implemented")
    }
}
