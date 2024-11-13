package rkrk.reservation.warehouse.share.exception

class IllegalDomainException(
    val msg: String,
) : IllegalArgumentException(msg)
