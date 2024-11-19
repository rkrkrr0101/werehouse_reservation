package rkrk.reservation.warehouse.reservation.application.port.output

import rkrk.reservation.warehouse.reservation.application.port.output.dto.FindMemberReservationDto
import rkrk.reservation.warehouse.reservation.domain.Reservation
import rkrk.reservation.warehouse.reservation.domain.ReservationTime

interface FindReservationPort {
    fun find(
        wareHouseName: String,
        memberName: String,
        reservationTime: ReservationTime,
    ): Reservation

    fun findMember(memberName: String): List<FindMemberReservationDto>
}
