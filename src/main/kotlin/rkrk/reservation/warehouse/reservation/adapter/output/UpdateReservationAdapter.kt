package rkrk.reservation.warehouse.reservation.adapter.output

import org.springframework.stereotype.Repository
import rkrk.reservation.warehouse.reservation.application.port.output.UpdateReservationPort
import rkrk.reservation.warehouse.reservation.domain.Reservation

@Repository
class UpdateReservationAdapter(
    private val reservationRepository: ReservationRepository,
) : UpdateReservationPort {
    override fun updateReservation(reservation: Reservation) {
        TODO("Not yet implemented")
    }
}
