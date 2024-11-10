package rkrk.reservation.warehouse.warehouse.domain

import rkrk.reservation.warehouse.reservation.domain.TimeLine

class WareHouse(
    private val name: String,
    private val minutePrice: Long,
    private val capacity: Long,
    private val timeLine: TimeLine,
)
