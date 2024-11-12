package rkrk.reservation.warehouse.reservation.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import rkrk.reservation.warehouse.reservation.application.port.input.ManageReservationUseCase
import rkrk.reservation.warehouse.reservation.application.port.input.dto.CreatePendingReservationDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.UpdateCancelReservationDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.UpdateConfirmReservationDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.UpdateRefundReservationDto
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import rkrk.reservation.warehouse.reservation.domain.TimeOverlapStatus
import rkrk.reservation.warehouse.warehouse.application.port.output.FindWareHousePort

@Service
@Transactional(readOnly = true)
class ManageReservationService(
    private val findWareHousePort: FindWareHousePort,
) : ManageReservationUseCase {
    @Transactional
    override fun createPendingReservation(dto: CreatePendingReservationDto): Long {
        // 비관적락 걸기(+타임라인 db에서 가져오기)
        val reservationTime = ReservationTime(dto.startDateTime, dto.endDateTime)
        val wareHouse =
            findWareHousePort.findAndLockWarehouseForReservationTime(
                dto.warehouseName,
                reservationTime,
            )

        // 들어가도되는지 확인(타임라인에 add)후
        if (wareHouse.checkOverlapReservation(reservationTime) == TimeOverlapStatus.NON_OVERLAPPING) {
            // 인서트
            //reservationTime이 아닌,Reservation이 저장되어야할거같기도
            wareHouse.addReservation(reservationTime)
        } else {
            throw RuntimeException() // 여기 커스텀예외로 수정
        }
        // wareHouse저장루틴(엔티티받아서 업데이트로직을 어댑터에 추가)
        //어댑터에 wareHouse던지면 어댑터에서 알아서 저장
        // 트랜잭션종료
    }

    @Transactional
    override fun updateConfirmReservation(dto: UpdateConfirmReservationDto): Long {
        TODO("Not yet implemented")
        // 예약id와 멤버확인하고 결제확인하고 업데이트
    }

    @Transactional
    override fun updateCancelReservation(dto: UpdateCancelReservationDto): Long {
        TODO("Not yet implemented")
        // 예약id와 멤버확인하고 업데이트
    }

    @Transactional
    override fun updateRefundReservation(dto: UpdateRefundReservationDto): Long {
        TODO("Not yet implemented")
        // 예약id와 멤버확인하고 환불완료되고나서 업데이트
        // 여기서 실제로 결제들어가면 외부연동결과에따라 보상트랜잭션필요할듯
    }
}
