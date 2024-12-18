package rkrk.reservation.integration.service

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import rkrk.reservation.helper.InitHelper
import rkrk.reservation.helper.SpringTestContainerTestWithTransactional
import rkrk.reservation.warehouse.reservation.adapter.output.ReservationJpaRepository
import rkrk.reservation.warehouse.reservation.application.port.input.ManageReservationUseCase
import rkrk.reservation.warehouse.reservation.application.port.input.dto.request.RequestUpdateCancelReservationDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.request.RequestUpdateConfirmReservationDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.request.RequestUpdateRefundReservationDto
import rkrk.reservation.warehouse.reservation.application.port.output.FindReservationPort
import rkrk.reservation.warehouse.reservation.domain.ReservationStatus
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import rkrk.reservation.warehouse.share.exception.exception.NotFoundEntityException
import rkrk.reservation.warehouse.warehouse.adapter.output.WareHouseJpaRepository
import java.time.LocalDateTime

@SpringTestContainerTestWithTransactional
class UpdateReservationTest(
    @Autowired val manageReservationUseCase: ManageReservationUseCase,
    @Autowired val wareHouseJpaRepository: WareHouseJpaRepository,
    @Autowired val reservationJpaRepository: ReservationJpaRepository,
    @Autowired val findReservationPort: FindReservationPort,
) {
    private val initHelper = InitHelper()

    @Test
    @DisplayName("예약을 취소시킬수 있다")
    fun cancelReservation() {
        initHelper.basicInit(wareHouseJpaRepository)
        val dto =
            RequestUpdateCancelReservationDto(
                initHelper.getMemberName(),
                initHelper.getWareHouseName(),
                LocalDateTime.of(2024, 10, 24, 10, 30),
                LocalDateTime.of(2024, 10, 24, 12, 30),
            )
        val reservationTime = ReservationTime(dto.startDateTime, dto.endDateTime)

        manageReservationUseCase.updateCancelReservation(dto)

        Assertions
            .assertThatThrownBy { findReservationPort.find(dto.warehouseName, dto.memberName, reservationTime) }
            .isInstanceOf(NotFoundEntityException::class.java)
        val reservations = reservationJpaRepository.findAll()
        Assertions.assertThat(reservations.size).isEqualTo(2)
    }

    @Test
    @DisplayName("예약을 환불시킬수 있다")
    fun refundReservation() {
        initHelper.basicInit(wareHouseJpaRepository)
        val dto =
            RequestUpdateRefundReservationDto(
                initHelper.getMemberName(),
                initHelper.getWareHouseName(),
                LocalDateTime.of(2024, 10, 24, 10, 30),
                LocalDateTime.of(2024, 10, 24, 12, 30),
            )
        val reservationTime = ReservationTime(dto.startDateTime, dto.endDateTime)

        manageReservationUseCase.updateRefundReservation(dto)

        Assertions
            .assertThatThrownBy { findReservationPort.find(dto.warehouseName, dto.memberName, reservationTime) }
            .isInstanceOf(NotFoundEntityException::class.java)
        val reservations = reservationJpaRepository.findAll()
        Assertions.assertThat(reservations.size).isEqualTo(2)
    }

    @Test
    @DisplayName("예약을 확정시킬수 있다")
    fun confirmReservation() {
        initHelper.basicInit(wareHouseJpaRepository)
        val dto =
            RequestUpdateConfirmReservationDto(
                initHelper.getMemberName(),
                initHelper.getWareHouseName(),
                LocalDateTime.of(2024, 10, 24, 10, 30),
                LocalDateTime.of(2024, 10, 24, 12, 30),
            )
        val reservationTime = ReservationTime(dto.startDateTime, dto.endDateTime)

        manageReservationUseCase.updateConfirmReservation(dto)

        val findReservation = findReservationPort.find(dto.warehouseName, dto.memberName, reservationTime)
        Assertions.assertThat(findReservation.state).isEqualTo(ReservationStatus.CONFIRMED)
    }
}
