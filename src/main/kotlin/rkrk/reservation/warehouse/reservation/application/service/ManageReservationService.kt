package rkrk.reservation.warehouse.reservation.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import rkrk.reservation.warehouse.reservation.application.port.input.ManageReservationUseCase
import rkrk.reservation.warehouse.reservation.application.port.input.dto.CreatePendingReservationDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.UpdateCancelReservationDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.UpdateConfirmReservationDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.UpdateRefundReservationDto
import rkrk.reservation.warehouse.reservation.application.port.output.FindReservationPort
import rkrk.reservation.warehouse.reservation.application.port.output.UpdateReservationPort
import rkrk.reservation.warehouse.reservation.domain.ReservationStatus
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import rkrk.reservation.warehouse.reservation.domain.TimeOverlapStatus
import rkrk.reservation.warehouse.warehouse.application.port.output.FindWareHousePort
import rkrk.reservation.warehouse.warehouse.application.port.output.UpdateWareHousePort

@Service
@Transactional(readOnly = true)
class ManageReservationService(
    private val findWareHousePort: FindWareHousePort,
    private val updateWareHousePort: UpdateWareHousePort,
    private val findReservationPort: FindReservationPort,
    private val updateReservationPort: UpdateReservationPort,
) : ManageReservationUseCase {
    @Transactional
    override fun createPendingReservation(dto: CreatePendingReservationDto) {
        val reservationTime = ReservationTime(dto.startDateTime, dto.endDateTime)
        val wareHouse =
            findWareHousePort.findAndLockWarehouseForReservationTime(
                dto.warehouseName,
                reservationTime,
            )

        if (wareHouse.checkOverlapReservation(reservationTime) == TimeOverlapStatus.NON_OVERLAPPING) {
            wareHouse.createAndAddReservation(dto.memberName, reservationTime)
        } else {
            throw RuntimeException() // 여기 커스텀예외로 수정
        }
        // wareHouse저장루틴(엔티티받아서 업데이트로직을 어댑터에 추가)
        // 어댑터에 wareHouse던지면 어댑터에서 알아서 저장
        updateWareHousePort.update(wareHouse)
        // 트랜잭션종료
    }

    @Transactional
    override fun updateConfirmReservation(dto: UpdateConfirmReservationDto) {
        val dtoReservationTime = ReservationTime(dto.startDateTime, dto.endDateTime)
        val reservation =
            findReservationPort.find(dto.warehouseName, dto.memberName, dtoReservationTime)
                ?: throw RuntimeException() // 커스텀예외변환
        // 결제확인로직
        // 결제확인로직끝

        reservation.updateState(ReservationStatus.CONFIRMED)

        updateReservationPort.updateReservation(reservation)
    }

    @Transactional
    override fun updateCancelReservation(dto: UpdateCancelReservationDto) {
        val dtoReservationTime = ReservationTime(dto.startDateTime, dto.endDateTime)
        val reservation =
            findReservationPort.find(dto.warehouseName, dto.memberName, dtoReservationTime)
                ?: throw RuntimeException() // 커스텀예외변환

        reservation.updateState(ReservationStatus.CANCELLED)

        updateReservationPort.updateReservation(reservation)
    }

    @Transactional
    override fun updateRefundReservation(dto: UpdateRefundReservationDto) {
        // 예약id와 멤버확인하고 환불완료되고나서 업데이트
        // 여기서 실제로 결제들어가면 외부연동결과에따라 보상트랜잭션필요할듯
        val dtoReservationTime = ReservationTime(dto.startDateTime, dto.endDateTime)
        val reservation =
            findReservationPort.find(dto.warehouseName, dto.memberName, dtoReservationTime)
                ?: throw RuntimeException() // 커스텀예외변환
        // 환불로직
        // 환불로직끝

        reservation.updateState(ReservationStatus.CONFIRMED)

        updateReservationPort.updateReservation(reservation)
    }
}
