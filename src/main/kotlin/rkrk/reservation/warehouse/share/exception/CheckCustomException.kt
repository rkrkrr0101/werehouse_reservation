package rkrk.reservation.warehouse.share.exception

fun <T : Throwable> checkCustomException(
    value: Boolean,
    exceptionSupplier: () -> T,
) {
    if (!value)
        {
            throw exceptionSupplier()
        }
}
