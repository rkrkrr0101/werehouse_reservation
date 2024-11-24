package rkrk.reservation.warehouse.warehouse.adapter.output

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import rkrk.reservation.warehouse.warehouse.adapter.output.entity.WareHouseJpaEntity

interface WareHouseJpaRepository : JpaRepository<WareHouseJpaEntity, Long> {
    @Query(
        " select w from WareHouseJpaEntity w join fetch w.reservationJpaEntities r " +
            " where w.name=:name ",
    )
    fun findByName(name: String): WareHouseJpaEntity?
}
