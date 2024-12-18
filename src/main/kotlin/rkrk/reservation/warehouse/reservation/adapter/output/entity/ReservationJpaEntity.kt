package rkrk.reservation.warehouse.reservation.adapter.output.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
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
@Table(
    name = "reservations",
    indexes = [
        Index(name = "idx_member_warehouse", columnList = "member_name,warehouse_id"),
        Index(
            name = "idx_warehouse_reservation_timeline",
            columnList = "warehouse_id,startDateTime,endDateTime",
        ),
    ],
)
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
    @Suppress("unused")
    var id: Long = 0,
) : BaseEntity() {
    fun toDomain(): Reservation =
        Reservation(
            memberName,
            ReservationTime(startDateTime, endDateTime),
            totalFare,
            state,
        )

    fun updateState(reservationStatus: ReservationStatus) {
        this.state = reservationStatus
    }
}

fun List<ReservationJpaEntity>.toTimeLine(): TimeLine {
    val timeLine = TimeLine()
    for (entity in this) {
        if (entity.state in setOf(ReservationStatus.PENDING, ReservationStatus.CONFIRMED)) {
            timeLine.addReservation(entity.toDomain())
        }
    }
    return timeLine
}
