package rkrk.reservation.warehouse.reservation.domain

class WareHouseReservation(
    private val id: Long,
    private val member: String,
    private val timeSlot: TimeSlot, // 엔티티에선 LocalDateTime2개로 변경
    private val totalPrice: Long,
    private val state: Long, // enum
)
