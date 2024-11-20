package rkrk.reservation.warehouse.share

fun <T : Throwable> checkCustomException(
    value: Boolean,
    exceptionSupplier: () -> T,
) {
    if (!value) {
        throw exceptionSupplier()
    }
}
