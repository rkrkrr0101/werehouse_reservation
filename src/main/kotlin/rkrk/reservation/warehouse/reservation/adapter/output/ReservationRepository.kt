package rkrk.reservation.warehouse.reservation.adapter.output

import org.springframework.stereotype.Repository
import rkrk.reservation.warehouse.reservation.adapter.output.entity.ReservationJpaEntity
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import rkrk.reservation.warehouse.share.exception.NotFoundEntityException

@Repository
class ReservationRepository(
    private val repository: ReservationJpaRepository,
) {
    fun find(
        wareHouseId: Long,
        memberName: String,
        reservationTime: ReservationTime,
    ): ReservationJpaEntity =
        repository.find(wareHouseId, memberName, reservationTime.startDateTime, reservationTime.endDateTime)
            ?: throw NotFoundEntityException("Reservation을 찾지못했습니다")

    fun findAndLockWarehouseByReservationTime(
        wareHouseId: Long,
        reservationTime: ReservationTime,
    ): List<ReservationJpaEntity> =
        repository.findAndLockByReservationTime(
            wareHouseId,
            reservationTime.startDateTime,
            reservationTime.endDateTime,
        )
}
