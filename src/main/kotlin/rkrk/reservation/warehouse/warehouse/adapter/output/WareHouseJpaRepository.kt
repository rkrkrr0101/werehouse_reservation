package rkrk.reservation.warehouse.warehouse.adapter.output

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import rkrk.reservation.warehouse.warehouse.adapter.output.entity.WareHouseJpaEntity

interface WareHouseJpaRepository : JpaRepository<WareHouseJpaEntity, Long> {
    fun findByName(name: String): WareHouseJpaEntity?

    @Query(
        " select w from WareHouseJpaEntity w join w.reservationJpaEntities r" +
            " where w.name=:name " +
            " and (r.state= 'PENDING' or r.state= 'CONFIRMED')  ",
    )
    fun findByNameAndValidReservation(name: String): WareHouseJpaEntity?
}
