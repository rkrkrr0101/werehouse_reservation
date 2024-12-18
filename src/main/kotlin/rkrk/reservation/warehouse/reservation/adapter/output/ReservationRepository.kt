package rkrk.reservation.warehouse.reservation.adapter.output

import org.springframework.stereotype.Repository
import rkrk.reservation.warehouse.reservation.adapter.output.entity.ReservationJpaEntity
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import rkrk.reservation.warehouse.share.exception.exception.NotFoundEntityException

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
        wareHouseName: String,
        reservationTime: ReservationTime,
    ): List<ReservationJpaEntity> =
        repository.findAndLockByReservationTime(
            wareHouseName,
            reservationTime.startDateTime,
            reservationTime.endDateTime,
        )

    fun findMember(memberName: String): List<ReservationJpaEntity> = repository.findMember(memberName)
}
