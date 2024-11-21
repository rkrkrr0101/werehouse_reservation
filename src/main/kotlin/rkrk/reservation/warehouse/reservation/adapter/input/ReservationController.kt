package rkrk.reservation.warehouse.reservation.adapter.input

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import rkrk.reservation.warehouse.reservation.application.port.input.ManageReservationUseCase
import rkrk.reservation.warehouse.reservation.application.port.input.MemberReservationQuery
import rkrk.reservation.warehouse.reservation.application.port.input.WarehouseTotalReservationQuery
import rkrk.reservation.warehouse.reservation.application.port.input.dto.request.RequestCreatePendingReservationDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.request.RequestFindReservationByMemberDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.request.RequestFindTotalReservationByWarehouseDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.request.RequestUpdateCancelReservationDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.request.RequestUpdateConfirmReservationDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.request.RequestUpdateRefundReservationDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.response.ResponseFindMemberReservationDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.response.ResponseFindTotalReservationByWareHouseDto
import rkrk.reservation.warehouse.share.Result

@RestController
@RequestMapping("/api/reservations")
class ReservationController(
    private val warehouseTotalReservationQuery: WarehouseTotalReservationQuery,
    private val memberReservationQuery: MemberReservationQuery,
    private val manageReservationUseCase: ManageReservationUseCase,
) {
    @GetMapping("/warehouse")
    fun findTotalReservationByWareHouse(
        dto: RequestFindTotalReservationByWarehouseDto,
    ): Result<List<ResponseFindTotalReservationByWareHouseDto>> {
        val reservationDtos = warehouseTotalReservationQuery.findWarehouseTotalReservation(dto)
        return Result(reservationDtos)
    }

    @GetMapping("/member")
    fun findTotalReservationByMember(dto: RequestFindReservationByMemberDto): Result<List<ResponseFindMemberReservationDto>> {
        val reservationDtos = memberReservationQuery.findMemberReservation(dto)
        return Result(reservationDtos)
    }

    @PostMapping
    fun createReservation(
        @RequestBody dto: RequestCreatePendingReservationDto,
    ) {
        manageReservationUseCase.createPendingReservation(dto)
    }

    @PatchMapping("/cancel")
    fun cancelReservation(
        @RequestBody dto: RequestUpdateCancelReservationDto,
    ) {
        manageReservationUseCase.updateCancelReservation(dto)
    }

    @PatchMapping("/confirm")
    fun confirmReservation(
        @RequestBody dto: RequestUpdateConfirmReservationDto,
    ) {
        manageReservationUseCase.updateConfirmReservation(dto)
    }

    @PatchMapping("/refund")
    fun refundReservation(
        @RequestBody dto: RequestUpdateRefundReservationDto,
    ) {
        manageReservationUseCase.updateRefundReservation(dto)
    }
}
