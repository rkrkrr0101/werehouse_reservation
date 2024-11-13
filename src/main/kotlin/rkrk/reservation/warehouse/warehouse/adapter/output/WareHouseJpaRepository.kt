package rkrk.reservation.warehouse.warehouse.adapter.output

import org.springframework.data.jpa.repository.JpaRepository
import rkrk.reservation.warehouse.warehouse.adapter.output.entity.WareHouseJpaEntity

interface WareHouseJpaRepository : JpaRepository<WareHouseJpaEntity, Long> {
    fun findByName(name: String): WareHouseJpaEntity?
}
