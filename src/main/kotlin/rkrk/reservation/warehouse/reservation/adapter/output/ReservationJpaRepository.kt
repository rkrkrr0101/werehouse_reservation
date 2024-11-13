package rkrk.reservation.warehouse.reservation.adapter.output

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import rkrk.reservation.warehouse.reservation.adapter.output.entity.ReservationJpaEntity
import java.time.LocalDateTime

interface ReservationJpaRepository : JpaRepository<ReservationJpaEntity, Long> {
    @Query(
        " select r from ReservationJpaEntity r " +
            " where r.wareHouse.id=:wareHouseId " +
            " and r.memberName=:memberName " +
            " and r.startDateTime=:startDateTime " +
            " and r.endDateTime=:endDateTime ",
    )
    fun find(
        wareHouseId: Long,
        memberName: String,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
    ): ReservationJpaEntity?
}
