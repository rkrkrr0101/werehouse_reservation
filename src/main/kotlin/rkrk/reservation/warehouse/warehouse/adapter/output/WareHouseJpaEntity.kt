package rkrk.reservation.warehouse.warehouse.adapter.output

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import rkrk.reservation.warehouse.reservation.adapter.output.entity.ReservationJpaEntity
import rkrk.reservation.warehouse.share.BaseEntity

@Entity
class WareHouseJpaEntity(
    var name: String,
    var category: Long,
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "wereHouse")
    var reservations: MutableList<ReservationJpaEntity>,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    // onetomany reservation
) : BaseEntity() {
    fun addReservation(reservation: ReservationJpaEntity) {
        reservations.add(reservation)
        reservation.wereHouse = this
    }
}
