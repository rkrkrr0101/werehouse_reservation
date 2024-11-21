package rkrk.reservation.warehouse.reservation.adapter.output

import org.springframework.stereotype.Repository
import rkrk.reservation.warehouse.reservation.application.port.output.FindReservationPort
import rkrk.reservation.warehouse.reservation.application.port.output.dto.FindMemberReservationDto
import rkrk.reservation.warehouse.reservation.domain.Reservation
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import rkrk.reservation.warehouse.warehouse.adapter.output.WareHouseRepository

@Repository
class FindReservationAdapter(
    private val reservationRepository: ReservationRepository,
    private val wareHouseRepository: WareHouseRepository,
) : FindReservationPort {
    override fun find(
        wareHouseName: String,
        memberName: String,
        reservationTime: ReservationTime,
    ): Reservation {
        val wareHouseEntity = wareHouseRepository.findByNameAndValidReservation(wareHouseName)
        val reservationEntity = reservationRepository.find(wareHouseEntity.id, memberName, reservationTime)

        return reservationEntity.toDomain()
    }

    override fun findMember(memberName: String): List<FindMemberReservationDto> {
        val resList = mutableListOf<FindMemberReservationDto>()
        reservationRepository.findMember(memberName).forEach {
            resList.add(
                FindMemberReservationDto(
                    it.wareHouse.name,
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
