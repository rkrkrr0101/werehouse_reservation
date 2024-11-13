package rkrk.reservation.warehouse.share.exception

class NotFoundEntityException(
    val msg: String,
) : IllegalArgumentException(msg)
