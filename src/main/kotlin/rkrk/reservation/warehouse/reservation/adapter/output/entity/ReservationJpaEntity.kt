package rkrk.reservation.warehouse.reservation.adapter.output.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import rkrk.reservation.warehouse.reservation.domain.ReservationStatus
import rkrk.reservation.warehouse.warehouse.adapter.output.WareHouseJpaEntity
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
    var wereHouse: WareHouseJpaEntity,
    @Id var id: Long = 0,
)
