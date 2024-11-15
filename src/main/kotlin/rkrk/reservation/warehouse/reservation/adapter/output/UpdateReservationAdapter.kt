package rkrk.reservation.warehouse.reservation.adapter.output

import org.springframework.stereotype.Repository
import rkrk.reservation.warehouse.reservation.application.port.output.UpdateReservationPort
import rkrk.reservation.warehouse.reservation.domain.Reservation
import rkrk.reservation.warehouse.warehouse.adapter.output.WareHouseRepository
import rkrk.reservation.warehouse.warehouse.domain.WareHouse

@Repository
class UpdateReservationAdapter(
    private val reservationRepository: ReservationRepository,
    private val wareHouseRepository: WareHouseRepository,
) : UpdateReservationPort {
    override fun updateReservation(
        wareHouse: WareHouse,
        reservation: Reservation,
    ) {
        val wareHouseJpaEntity = wareHouseRepository.findByName(wareHouse.retrieveName())
        val reservationJpaEntity =
            reservationRepository.find(
                wareHouseJpaEntity.id,
                reservation.memberName,
                reservation.reservationTime,
            )
        reservationJpaEntity.updateState(reservation.state)
    }
}
