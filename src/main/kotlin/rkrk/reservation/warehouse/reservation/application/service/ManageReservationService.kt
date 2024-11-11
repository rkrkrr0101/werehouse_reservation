package rkrk.reservation.warehouse.reservation.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import rkrk.reservation.warehouse.reservation.application.port.input.ManageReservationUseCase
import rkrk.reservation.warehouse.reservation.application.port.input.dto.CreatePendingReservationDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.UpdateCancelReservationDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.UpdateConfirmReservationDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.UpdateRefundReservationDto

@Service
@Transactional(readOnly = true)
class ManageReservationService : ManageReservationUseCase {
    @Transactional
    override fun createPendingReservation(dto: CreatePendingReservationDto): Long {
        TODO("Not yet implemented")
        // 비관적락 걸기
        // 들어가도되는지 확인(타임라인에 add)
        // 인서트
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
