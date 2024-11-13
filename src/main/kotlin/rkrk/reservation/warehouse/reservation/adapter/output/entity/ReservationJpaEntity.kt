package rkrk.reservation.warehouse.reservation.adapter.output.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import rkrk.reservation.warehouse.reservation.domain.Reservation
import rkrk.reservation.warehouse.reservation.domain.ReservationStatus
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import rkrk.reservation.warehouse.reservation.domain.TimeLine
import rkrk.reservation.warehouse.share.BaseEntity
import rkrk.reservation.warehouse.warehouse.adapter.output.entity.WareHouseJpaEntity
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
class ReservationJpaEntity(
    var memberName: String,
    var startDateTime: LocalDateTime,
    var endDateTime: LocalDateTime,
    var totalFare: BigDecimal,
    var state: ReservationStatus,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "werehouse_id")
    var wareHouse: WareHouseJpaEntity,
    @Id var id: Long = 0,
) : BaseEntity() {
    fun toDomain(): Reservation = Reservation(memberName, ReservationTime(startDateTime, endDateTime), totalFare, state)
}

fun List<ReservationJpaEntity>.toTimeLine(): TimeLine {
    val timeLine = TimeLine()
    for (entity in this) {
        timeLine.addReservation(entity.toDomain())
    }
    return timeLine
}

fun List<ReservationJpaEntity>.toDomain(): List<Reservation> {
    val resList = mutableListOf<Reservation>()
    for (entity in this) {
        resList.add(entity.toDomain())
    }
    return resList
}

fun ReservationJpaEntity.toDomain(): Reservation =
    Reservation(
        this.memberName,
        ReservationTime(this.startDateTime, this.endDateTime),
        this.totalFare,
        this.state,
    )
