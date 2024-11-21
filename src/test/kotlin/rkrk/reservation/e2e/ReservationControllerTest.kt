package rkrk.reservation.e2e

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import rkrk.reservation.helper.InitHelper
import rkrk.reservation.helper.SpringTestContainerTestWithTransactional
import rkrk.reservation.warehouse.reservation.domain.ReservationStatus
import rkrk.reservation.warehouse.warehouse.adapter.output.WareHouseJpaRepository
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime

@SpringTestContainerTestWithTransactional
@AutoConfigureMockMvc
class ReservationControllerTest(
    @Autowired private val mvc: MockMvc,
    @Autowired private val wareHouseJpaRepository: WareHouseJpaRepository,
) {
    private val initHelper = InitHelper()
    val om = ObjectMapper()

    @Test
    @DisplayName("특정창고의 모든 예약을 조회할수있다")
    fun findTotalReservationByWareHouse() {
        initHelper.basicInit(wareHouseJpaRepository)
        val mvcResult =
            mvc
                .perform(
                    MockMvcRequestBuilders
                        .get("/api/reservations/warehouse?warehouseName=${initHelper.getWareHouseName()}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8),
                ).andExpect(status().isOk)
                .andReturn()
        val body = toListMap(mvcResult, om)

        Assertions.assertThat(body.size).isEqualTo(2)
        Assertions
            .assertThat(body.filter { it["memberName"] == initHelper.getMemberName() }.size)
            .isEqualTo(2)
    }

    @Test
    @DisplayName("창고의 전체 예약 조회시 창고가 없다면 400에러가 발생한다")
    fun findNotExistWareHouse() {
        initHelper.basicInit(wareHouseJpaRepository)
        mvc
            .perform(
                MockMvcRequestBuilders
                    .get("/api/reservations/warehouse?warehouseName=NotExistsWareHouse")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8),
            ).andExpect(status().is4xxClientError)
    }

    @Test
    @DisplayName("특정멤버의 모든 예약을 조회할수있다")
    fun findTotalReservationByMember() {
        initHelper.basicInit(wareHouseJpaRepository)
        val mvcResult =
            mvc
                .perform(
                    MockMvcRequestBuilders
                        .get("/api/reservations/member?memberName=${initHelper.getMemberName()}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8),
                ).andExpect(status().isOk)
                .andReturn()
        val body = toListMap(mvcResult, om)

        Assertions.assertThat(body.size).isEqualTo(2)
        Assertions
            .assertThat(body.filter { it["warehouseName"] == initHelper.getWareHouseName() }.size)
            .isEqualTo(2)
    }

    @Test
    @DisplayName("창고에 예약을 추가할수 있다")
    fun createReservation() {
        initHelper.basicInit(wareHouseJpaRepository)
        val jsonMap = hashMapOf<String, String>()
        jsonMap["warehouseName"] = initHelper.getWareHouseName()
        jsonMap["memberName"] = initHelper.getMemberName()
        jsonMap["startDateTime"] = "2024-10-26T11:30:00"
        jsonMap["endDateTime"] = "2024-10-26T13:30:00"
        val reqBodyJson = om.writeValueAsString(jsonMap)

        mvc
            .perform(
                MockMvcRequestBuilders
                    .post("/api/reservations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(reqBodyJson),
            ).andExpect(status().isOk)

        val wareHouseJpaEntity =
            wareHouseJpaRepository.findByName(initHelper.getWareHouseName())
                ?: throw RuntimeException()
        Assertions.assertThat(wareHouseJpaEntity.reservationJpaEntities).hasSize(3)
    }

    @Test
    @DisplayName("창고에 겹치는 예약이 존재하면 400에러가 발생한다")
    fun overlapReservation() {
        initHelper.basicInit(wareHouseJpaRepository)
        val jsonMap = hashMapOf<String, String>()
        jsonMap["warehouseName"] = initHelper.getWareHouseName()
        jsonMap["memberName"] = initHelper.getMemberName()
        jsonMap["startDateTime"] = "2024-10-24T11:30:00"
        jsonMap["endDateTime"] = "2024-10-26T13:30:00"
        val reqBodyJson = om.writeValueAsString(jsonMap)

        mvc
            .perform(
                MockMvcRequestBuilders
                    .post("/api/reservations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(reqBodyJson),
            ).andExpect(status().is4xxClientError)
    }

    @Test
    @DisplayName("예약의 상태를 확정으로 변경할수 있다")
    fun confirmReservation() {
        initHelper.basicInit(wareHouseJpaRepository)
        val jsonMap = hashMapOf<String, String>()
        jsonMap["warehouseName"] = initHelper.getWareHouseName()
        jsonMap["memberName"] = initHelper.getMemberName()
        jsonMap["startDateTime"] = "2024-10-24T10:30:00"
        jsonMap["endDateTime"] = "2024-10-24T12:30:00"
        val reqBodyJson = om.writeValueAsString(jsonMap)

        mvc
            .perform(
                MockMvcRequestBuilders
                    .patch("/api/reservations/confirm")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(reqBodyJson),
            ).andExpect(status().isOk)

        val wareHouseJpaEntity =
            wareHouseJpaRepository.findByName(initHelper.getWareHouseName())
                ?: throw RuntimeException()
        Assertions
            .assertThat(
                wareHouseJpaEntity.reservationJpaEntities
                    .filter {
                        it.startDateTime == LocalDateTime.of(2024, 10, 24, 10, 30) &&
                            it.endDateTime == LocalDateTime.of(2024, 10, 24, 12, 30)
                    }[0]
                    .state,
            ).isEqualTo(ReservationStatus.CONFIRMED)
    }

    @Test
    @DisplayName("예약의 상태를 확정으로 변경시 해당하는 창고가 없으면 400에러를 리턴한다")
    fun confirmReservationNotExistsWareHouse4xxError() {
        initHelper.basicInit(wareHouseJpaRepository)
        val jsonMap = hashMapOf<String, String>()
        jsonMap["warehouseName"] = "NotExistsWareHouse"
        jsonMap["memberName"] = initHelper.getMemberName()
        jsonMap["startDateTime"] = "2024-10-24T10:30:00"
        jsonMap["endDateTime"] = "2024-10-24T12:30:00"
        val reqBodyJson = om.writeValueAsString(jsonMap)

        mvc
            .perform(
                MockMvcRequestBuilders
                    .patch("/api/reservations/confirm")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(reqBodyJson),
            ).andExpect(status().is4xxClientError)
    }

    @Test
    @DisplayName("예약의 상태를 확정으로 변경시 해당하는 멤버가 없으면 400에러를 리턴한다")
    fun confirmReservationNotExistsMember4xxError() {
        initHelper.basicInit(wareHouseJpaRepository)
        val jsonMap = hashMapOf<String, String>()
        jsonMap["warehouseName"] = initHelper.getWareHouseName()
        jsonMap["memberName"] = "NotExistsMember"
        jsonMap["startDateTime"] = "2024-10-24T10:30:00"
        jsonMap["endDateTime"] = "2024-10-24T12:30:00"
        val reqBodyJson = om.writeValueAsString(jsonMap)

        mvc
            .perform(
                MockMvcRequestBuilders
                    .patch("/api/reservations/confirm")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(reqBodyJson),
            ).andExpect(status().is4xxClientError)
    }

    @Test
    @DisplayName("예약의 상태를 확정으로 변경시 해당하는 예약이 없으면 400에러를 리턴한다")
    fun confirmReservationNotExistsReservation4xxError() {
        initHelper.basicInit(wareHouseJpaRepository)
        val jsonMap = hashMapOf<String, String>()
        jsonMap["warehouseName"] = initHelper.getWareHouseName()
        jsonMap["memberName"] = initHelper.getMemberName()
        jsonMap["startDateTime"] = "2000-07-11T10:30:00"
        jsonMap["endDateTime"] = "2000-07-12T12:30:00"
        val reqBodyJson = om.writeValueAsString(jsonMap)

        mvc
            .perform(
                MockMvcRequestBuilders
                    .patch("/api/reservations/confirm")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(reqBodyJson),
            ).andExpect(status().is4xxClientError)
    }

    @Test
    @DisplayName("예약의 상태를 취소로 변경할수 있다")
    fun cancelReservation() {
        initHelper.basicInit(wareHouseJpaRepository)
        val jsonMap = hashMapOf<String, String>()
        jsonMap["warehouseName"] = initHelper.getWareHouseName()
        jsonMap["memberName"] = initHelper.getMemberName()
        jsonMap["startDateTime"] = "2024-10-24T10:30:00"
        jsonMap["endDateTime"] = "2024-10-24T12:30:00"
        val reqBodyJson = om.writeValueAsString(jsonMap)

        mvc
            .perform(
                MockMvcRequestBuilders
                    .patch("/api/reservations/cancel")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(reqBodyJson),
            ).andExpect(status().isOk)

        val wareHouseJpaEntity =
            wareHouseJpaRepository.findByName(initHelper.getWareHouseName())
                ?: throw RuntimeException()
        Assertions
            .assertThat(
                wareHouseJpaEntity.reservationJpaEntities
                    .filter {
                        it.startDateTime == LocalDateTime.of(2024, 10, 24, 10, 30) &&
                            it.endDateTime == LocalDateTime.of(2024, 10, 24, 12, 30)
                    }[0]
                    .state,
            ).isEqualTo(ReservationStatus.CANCELLED)
    }

    @Test
    @DisplayName("예약의 상태를 취소로 변경시 해당하는 창고가 없으면 400에러를 리턴한다")
    fun cancelReservationNotExistsWareHouse4xxError() {
        initHelper.basicInit(wareHouseJpaRepository)
        val jsonMap = hashMapOf<String, String>()
        jsonMap["warehouseName"] = "NotExistsWareHouse"
        jsonMap["memberName"] = initHelper.getMemberName()
        jsonMap["startDateTime"] = "2024-10-24T10:30:00"
        jsonMap["endDateTime"] = "2024-10-24T12:30:00"
        val reqBodyJson = om.writeValueAsString(jsonMap)

        mvc
            .perform(
                MockMvcRequestBuilders
                    .patch("/api/reservations/cancel")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(reqBodyJson),
            ).andExpect(status().is4xxClientError)
    }

    @Test
    @DisplayName("예약의 상태를 취소로 변경시 해당하는 멤버가 없으면 400에러를 리턴한다")
    fun cancelReservationNotExistsMember4xxError() {
        initHelper.basicInit(wareHouseJpaRepository)
        val jsonMap = hashMapOf<String, String>()
        jsonMap["warehouseName"] = initHelper.getWareHouseName()
        jsonMap["memberName"] = "NotExistsMember"
        jsonMap["startDateTime"] = "2024-10-24T10:30:00"
        jsonMap["endDateTime"] = "2024-10-24T12:30:00"
        val reqBodyJson = om.writeValueAsString(jsonMap)

        mvc
            .perform(
                MockMvcRequestBuilders
                    .patch("/api/reservations/cancel")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(reqBodyJson),
            ).andExpect(status().is4xxClientError)
    }

    @Test
    @DisplayName("예약의 상태를 취소로 변경시 해당하는 예약이 없으면 400에러를 리턴한다")
    fun cancelReservationNotExistsReservation4xxError() {
        initHelper.basicInit(wareHouseJpaRepository)
        val jsonMap = hashMapOf<String, String>()
        jsonMap["warehouseName"] = initHelper.getWareHouseName()
        jsonMap["memberName"] = initHelper.getMemberName()
        jsonMap["startDateTime"] = "2000-07-11T10:30:00"
        jsonMap["endDateTime"] = "2000-07-12T12:30:00"
        val reqBodyJson = om.writeValueAsString(jsonMap)

        mvc
            .perform(
                MockMvcRequestBuilders
                    .patch("/api/reservations/cancel")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(reqBodyJson),
            ).andExpect(status().is4xxClientError)
    }

    @Test
    @DisplayName("예약의 상태를 환불로 변경할수 있다")
    fun refundReservation() {
        initHelper.basicInit(wareHouseJpaRepository)
        val jsonMap = hashMapOf<String, String>()
        jsonMap["warehouseName"] = initHelper.getWareHouseName()
        jsonMap["memberName"] = initHelper.getMemberName()
        jsonMap["startDateTime"] = "2024-10-24T10:30:00"
        jsonMap["endDateTime"] = "2024-10-24T12:30:00"
        val reqBodyJson = om.writeValueAsString(jsonMap)

        mvc
            .perform(
                MockMvcRequestBuilders
                    .patch("/api/reservations/refund")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(reqBodyJson),
            ).andExpect(status().isOk)

        val wareHouseJpaEntity =
            wareHouseJpaRepository.findByName(initHelper.getWareHouseName())
                ?: throw RuntimeException()
        Assertions
            .assertThat(
                wareHouseJpaEntity.reservationJpaEntities
                    .filter {
                        it.startDateTime == LocalDateTime.of(2024, 10, 24, 10, 30) &&
                            it.endDateTime == LocalDateTime.of(2024, 10, 24, 12, 30)
                    }[0]
                    .state,
            ).isEqualTo(ReservationStatus.REFUNDED)
    }

    @Test
    @DisplayName("예약의 상태를 환불로 변경시 해당하는 창고가 없으면 400에러를 리턴한다")
    fun refundReservationNotExistsWareHouse4xxError() {
        initHelper.basicInit(wareHouseJpaRepository)
        val jsonMap = hashMapOf<String, String>()
        jsonMap["warehouseName"] = "NotExistsWareHouse"
        jsonMap["memberName"] = initHelper.getMemberName()
        jsonMap["startDateTime"] = "2024-10-24T10:30:00"
        jsonMap["endDateTime"] = "2024-10-24T12:30:00"
        val reqBodyJson = om.writeValueAsString(jsonMap)

        mvc
            .perform(
                MockMvcRequestBuilders
                    .patch("/api/reservations/refund")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(reqBodyJson),
            ).andExpect(status().is4xxClientError)
    }

    @Test
    @DisplayName("예약의 상태를 환불로 변경시 해당하는 멤버가 없으면 400에러를 리턴한다")
    fun refundReservationNotExistsMember4xxError() {
        initHelper.basicInit(wareHouseJpaRepository)
        val jsonMap = hashMapOf<String, String>()
        jsonMap["warehouseName"] = initHelper.getWareHouseName()
        jsonMap["memberName"] = "NotExistsMember"
        jsonMap["startDateTime"] = "2024-10-24T10:30:00"
        jsonMap["endDateTime"] = "2024-10-24T12:30:00"
        val reqBodyJson = om.writeValueAsString(jsonMap)

        mvc
            .perform(
                MockMvcRequestBuilders
                    .patch("/api/reservations/cancel")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(reqBodyJson),
            ).andExpect(status().is4xxClientError)
    }

    @Test
    @DisplayName("예약의 상태를 환불로 변경시 해당하는 예약이 없으면 400에러를 리턴한다")
    fun refundReservationNotExistsReservation4xxError() {
        initHelper.basicInit(wareHouseJpaRepository)
        val jsonMap = hashMapOf<String, String>()
        jsonMap["warehouseName"] = initHelper.getWareHouseName()
        jsonMap["memberName"] = initHelper.getMemberName()
        jsonMap["startDateTime"] = "2000-07-11T10:30:00"
        jsonMap["endDateTime"] = "2000-07-12T12:30:00"
        val reqBodyJson = om.writeValueAsString(jsonMap)

        mvc
            .perform(
                MockMvcRequestBuilders
                    .patch("/api/reservations/cancel")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(reqBodyJson),
            ).andExpect(status().is4xxClientError)
    }

    private fun toListMap(
        mvcResult: MvcResult,
        om: ObjectMapper,
    ): List<Map<String, Any>> {
        val content = mvcResult.response.contentAsString
        val resMap = om.readValue(content, Map::class.java)
        val body = (resMap["data"] as List<*>).filterIsInstance<Map<String, Any>>()
        return body
    }
}
