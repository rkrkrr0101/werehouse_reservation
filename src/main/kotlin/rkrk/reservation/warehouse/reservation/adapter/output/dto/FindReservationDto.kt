package rkrk.reservation.warehouse.reservation.adapter.output.dto

import rkrk.reservation.warehouse.reservation.domain.Reservation
import rkrk.reservation.warehouse.reservation.domain.ReservationStatus
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import rkrk.reservation.warehouse.warehouse.adapter.output.entity.WareHouseJpaEntity
import java.math.BigDecimal
import java.time.LocalDateTime

class FindReservationDto(
    val memberName: String,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val totalFare: BigDecimal,
    val state: ReservationStatus,
    val wereHouse: WareHouseJpaEntity,
    val id: Long,
) {
    fun toDomain(): Reservation = Reservation(memberName, ReservationTime(startDateTime, endDateTime), totalFare, state)
}
