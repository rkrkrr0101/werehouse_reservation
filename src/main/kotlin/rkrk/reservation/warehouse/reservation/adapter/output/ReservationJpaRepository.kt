package rkrk.reservation.warehouse.reservation.adapter.output

import jakarta.persistence.LockModeType
import jakarta.persistence.QueryHint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.QueryHints
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

    @Query(
        " select r from ReservationJpaEntity r join fetch r.wareHouse " +
            " where r.wareHouse.name=:wareHouseName " +
            " and (r.endDateTime>=:startDateTime and r.startDateTime< :endDateTime ) " +
            " and (r.state= 'PENDING' or r.state= 'CONFIRMED')  ",
    )
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(value = [QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")])
    fun findAndLockByReservationTime(
        wareHouseName: String,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
    ): List<ReservationJpaEntity>

    @Query(
        " select r from ReservationJpaEntity r where r.memberName=:memberName ",
    )
    fun findMember(memberName: String): List<ReservationJpaEntity>
}
