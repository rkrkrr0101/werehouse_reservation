package rkrk.reservation.warehouse.warehouse.adapter.output.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import rkrk.reservation.warehouse.reservation.adapter.output.entity.ReservationJpaEntity
import rkrk.reservation.warehouse.reservation.adapter.output.entity.toTimeLine
import rkrk.reservation.warehouse.reservation.domain.Reservation
import rkrk.reservation.warehouse.share.BaseEntity
import rkrk.reservation.warehouse.share.exception.exception.IllegalDomainException
import rkrk.reservation.warehouse.warehouse.domain.WareHouse

@Entity
@Table(
    name = "warehouse",
    indexes = [Index(name = "idx_warehouse_name", columnList = "name")],
)
class WareHouseJpaEntity(
    @Column(unique = true, nullable = false)
    var name: String,
    var capacity: Long,
    @Column(nullable = false)
    var minutePrice: Long,
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "wareHouse", cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    var reservationJpaEntities: MutableList<ReservationJpaEntity>,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Suppress("unused")
    var id: Long = 0,
) : BaseEntity() {
    private fun addReservation(reservation: ReservationJpaEntity) {
        reservationJpaEntities.add(reservation)
        reservation.wareHouse = this
    }

    private fun addReservation(reservation: Reservation) {
        val newReservationJpaEntity =
            ReservationJpaEntity(
                reservation.memberName,
                reservation.reservationTime.startDateTime,
                reservation.reservationTime.endDateTime,
                reservation.totalFare,
                reservation.state,
                this,
            )
        addReservation(newReservationJpaEntity)
    }

    fun toDomain(): WareHouse {
        val timeLine = reservationJpaEntities.toTimeLine()
        return WareHouse(name, minutePrice, capacity, timeLine)
    }

    fun update(wereHouse: WareHouse) {
        if (wereHouse.retrieveName() != name)throw IllegalDomainException("같은창고가 아닙니다")
        capacity = wereHouse.retrieveCapacity()
        minutePrice = wereHouse.retrieveMinutePrice()
        val inputReservations = wereHouse.retrieveTotalReservations().reservations
        // 예약 추가
        for (reservation in inputReservations) {
            if (!reservationJpaEntities.any {
                    it.memberName == reservation.memberName &&
                        it.startDateTime == reservation.reservationTime.startDateTime &&
                        it.endDateTime == reservation.reservationTime.endDateTime
                }
            ) {
                addReservation(reservation)
            }
        }
        // 없어진예약삭제
        val iterator = reservationJpaEntities.iterator()
        while (iterator.hasNext()) {
            val reservationEntity = iterator.next()
            if (!inputReservations.any {
                    it.memberName == reservationEntity.memberName &&
                        it.reservationTime.startDateTime == reservationEntity.startDateTime &&
                        it.reservationTime.endDateTime == reservationEntity.endDateTime
                }
            ) {
                iterator.remove()
                // reservationEntity.wareHouse=null //캐스캐이드로 어짜피사라짐
            }
        }
    }
}

fun WareHouse.toNewEntity(): WareHouseJpaEntity {
    val wareHouseJpaEntity =
        WareHouseJpaEntity(
            this.retrieveName(),
            this.retrieveCapacity(),
            this.retrieveMinutePrice(),
            mutableListOf(),
        )
    wareHouseJpaEntity.update(this)
    return wareHouseJpaEntity
}
