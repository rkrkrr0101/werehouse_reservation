package rkrk.reservation.warehouse.reservation.adapter.output.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import rkrk.reservation.warehouse.reservation.domain.Reservation
import rkrk.reservation.warehouse.reservation.domain.ReservationStatus
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import rkrk.reservation.warehouse.reservation.domain.TimeLine
import rkrk.reservation.warehouse.share.BaseEntity
import rkrk.reservation.warehouse.warehouse.adapter.output.entity.WareHouseJpaEntity
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "reservations")
class ReservationJpaEntity(
    @Column(nullable = false)
    var memberName: String,
    @Column(nullable = false)
    var startDateTime: LocalDateTime,
    @Column(nullable = false)
    var endDateTime: LocalDateTime,
    var totalFare: BigDecimal,
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var state: ReservationStatus,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    var wareHouse: WareHouseJpaEntity,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
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
