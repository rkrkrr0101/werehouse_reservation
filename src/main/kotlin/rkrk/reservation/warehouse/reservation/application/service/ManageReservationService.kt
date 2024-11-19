package rkrk.reservation.warehouse.reservation.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import rkrk.reservation.warehouse.reservation.application.port.input.ManageReservationUseCase
import rkrk.reservation.warehouse.reservation.application.port.input.dto.request.RequestCreatePendingReservationDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.request.RequestUpdateCancelReservationDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.request.RequestUpdateConfirmReservationDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.request.RequestUpdateRefundReservationDto
import rkrk.reservation.warehouse.reservation.application.port.output.FindReservationPort
import rkrk.reservation.warehouse.reservation.application.port.output.UpdateReservationPort
import rkrk.reservation.warehouse.reservation.domain.ReservationStatus
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import rkrk.reservation.warehouse.reservation.domain.TimeOverlapStatus
import rkrk.reservation.warehouse.share.exception.OverlapException
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
    override fun createPendingReservation(dto: RequestCreatePendingReservationDto) {
        val reservationTime = ReservationTime(dto.startDateTime, dto.endDateTime)
        val wareHouse =
            findWareHousePort.findAndLockWarehouseByReservationTime(
                dto.warehouseName,
                reservationTime,
            )

        if (wareHouse.checkOverlapReservation(reservationTime) == TimeOverlapStatus.NON_OVERLAPPING) {
            wareHouse.createAndAddReservation(dto.memberName, reservationTime)
        } else {
            throw OverlapException("오버랩이 발생했습니다")
        }
        updateWareHousePort.update(wareHouse)
    }

    @Transactional
    override fun updateConfirmReservation(dto: RequestUpdateConfirmReservationDto) {
        val dtoReservationTime = ReservationTime(dto.startDateTime, dto.endDateTime)
        val reservation =
            findReservationPort.find(dto.warehouseName, dto.memberName, dtoReservationTime)
        val wareHouse = findWareHousePort.findWarehouseByName(dto.warehouseName)
        // 결제확인로직
        // 결제확인로직끝

        reservation.updateState(ReservationStatus.CONFIRMED)

        updateReservationPort.updateReservation(wareHouse, reservation)
    }

    @Transactional
    override fun updateCancelReservation(dto: RequestUpdateCancelReservationDto) {
        val dtoReservationTime = ReservationTime(dto.startDateTime, dto.endDateTime)
        val reservation =
            findReservationPort.find(dto.warehouseName, dto.memberName, dtoReservationTime)
        val wareHouse = findWareHousePort.findWarehouseByName(dto.warehouseName)

        reservation.updateState(ReservationStatus.CANCELLED)

        updateReservationPort.updateReservation(wareHouse, reservation)
    }

    @Transactional
    override fun updateRefundReservation(dto: RequestUpdateRefundReservationDto) {
        // 예약id와 멤버확인하고 환불완료되고나서 업데이트
        // 여기서 실제로 결제들어가면 외부연동결과에따라 보상트랜잭션필요할듯
        val dtoReservationTime = ReservationTime(dto.startDateTime, dto.endDateTime)
        val reservation =
            findReservationPort.find(dto.warehouseName, dto.memberName, dtoReservationTime)
                ?: throw RuntimeException() // 커스텀예외변환
        val wareHouse = findWareHousePort.findWarehouseByName(dto.warehouseName)
        // 환불로직
        // 환불로직끝

        reservation.updateState(ReservationStatus.REFUNDED)

        updateReservationPort.updateReservation(wareHouse, reservation)
    }
}
