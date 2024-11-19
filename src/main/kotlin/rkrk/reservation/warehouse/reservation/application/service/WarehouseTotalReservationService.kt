package rkrk.reservation.warehouse.reservation.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import rkrk.reservation.warehouse.reservation.application.port.input.WarehouseTotalReservationQuery
import rkrk.reservation.warehouse.reservation.application.port.input.dto.request.RequestFindTotalReservationByWarehouseDto
import rkrk.reservation.warehouse.reservation.application.port.input.dto.response.ResponseFindTotalReservationByWareHouseDto
import rkrk.reservation.warehouse.warehouse.application.port.output.FindWareHousePort

@Service
@Transactional(readOnly = true)
class WarehouseTotalReservationService(
    private val findWareHousePort: FindWareHousePort,
) : WarehouseTotalReservationQuery {
    override fun findWarehouseTotalReservation(
        dto: RequestFindTotalReservationByWarehouseDto,
    ): List<ResponseFindTotalReservationByWareHouseDto> {
        val wareHouse = findWareHousePort.findWarehouseByName(dto.warehouseName)
        val resList = mutableListOf<ResponseFindTotalReservationByWareHouseDto>()
        wareHouse.retrieveTotalReservations().reservations.forEach {
            resList.add(
                ResponseFindTotalReservationByWareHouseDto(
                    it.memberName,
                    it.reservationTime.startDateTime,
                    it.reservationTime.endDateTime,
                    it.totalFare,
                    it.state,
                ),
            )
        }
        return resList
    }
}
