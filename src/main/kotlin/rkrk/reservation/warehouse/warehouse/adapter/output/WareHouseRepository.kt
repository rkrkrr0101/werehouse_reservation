package rkrk.reservation.warehouse.warehouse.adapter.output

import org.springframework.stereotype.Repository
import rkrk.reservation.warehouse.share.exception.NotFoundEntityException
import rkrk.reservation.warehouse.warehouse.adapter.output.entity.WareHouseJpaEntity

@Repository
class WareHouseRepository(
    private val repository: WareHouseJpaRepository,
) {
    fun findByName(name: String): WareHouseJpaEntity = repository.findByName(name) ?: throw NotFoundEntityException("WareHouse를 찾지못했습니다")
}
