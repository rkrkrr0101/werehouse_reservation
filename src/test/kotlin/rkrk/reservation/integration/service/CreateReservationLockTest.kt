package rkrk.reservation.integration.service

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.Rollback
import rkrk.reservation.helper.InitHelper
import rkrk.reservation.helper.SpringTestContainerTest
import rkrk.reservation.warehouse.reservation.adapter.output.ReservationJpaRepository
import rkrk.reservation.warehouse.reservation.application.port.input.ManageReservationUseCase
import rkrk.reservation.warehouse.reservation.application.port.input.dto.request.RequestCreatePendingReservationDto
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import rkrk.reservation.warehouse.warehouse.adapter.output.WareHouseJpaRepository
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringTestContainerTest
class CreateReservationLockTest(
    @Autowired val wareHouseJpaRepository: WareHouseJpaRepository,
    @Autowired val reservationJpaRepository: ReservationJpaRepository,
    @Autowired val manageReservationUseCase: ManageReservationUseCase,
) {
    private val initHelper = InitHelper()

    @AfterEach
    @Rollback(false)
    fun clear() {
        initHelper.basicClear(wareHouseJpaRepository, reservationJpaRepository)
    }

    @Test
    @DisplayName("겹치는 시간으로 동시에 예약을 추가하면 하나만 들어갈수있다")
    fun createReservationLock() {
        initHelper.basicInit(wareHouseJpaRepository)
        val threadCount = 100
        val executor = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)
        val reservationTime1 =
            ReservationTime(
                LocalDateTime.of(2024, 10, 25, 14, 30),
                LocalDateTime.of(2024, 10, 25, 15, 30),
            )
        val reservationTime2 =
            ReservationTime(
                LocalDateTime.of(2024, 10, 25, 10, 30),
                LocalDateTime.of(2024, 10, 25, 17, 30),
            )

        for (i in 0..threadCount) {
            executor.submit {
                try {
                    val randomTime = if (i % 2 == 1) reservationTime1 else reservationTime2
                    manageReservationUseCase.createPendingReservation(
                        createDto(
                            initHelper.getMemberName(),
                            initHelper.getWareHouseName(),
                            randomTime,
                        ),
                    )
                } finally {
                    latch.countDown()
                }
            }
        }
        latch.await()

        val reservations = reservationJpaRepository.findAll()
        Assertions.assertThat(reservations).hasSize(3)
    }

    @Test
    @DisplayName("겹치지 않는 시간으로 동시에 예약을 추가하면 전부 들어갈수있다")
    fun createReservationNonOverlap() {
        initHelper.basicInit(wareHouseJpaRepository)
        val threadCount = 3
        val executor = Executors.newFixedThreadPool(2)
        val latch = CountDownLatch(threadCount)
        val reservationTime1 =
            ReservationTime(
                LocalDateTime.of(2024, 10, 25, 14, 30),
                LocalDateTime.of(2024, 10, 25, 15, 30),
            )
        val reservationTime2 =
            ReservationTime(
                LocalDateTime.of(2024, 10, 25, 16, 30),
                LocalDateTime.of(2024, 10, 25, 17, 30),
            )
        val reservationTime3 =
            ReservationTime(
                LocalDateTime.of(2024, 10, 25, 17, 30),
                LocalDateTime.of(2024, 10, 25, 18, 30),
            )
        for (i in 1..threadCount) {
            executor.submit {
                try {
                    val randomTime =
                        when (i) {
                            1 -> reservationTime1
                            2 -> reservationTime2
                            3 -> reservationTime3
                            else -> throw RuntimeException()
                        }
                    manageReservationUseCase.createPendingReservation(
                        createDto(
                            initHelper.getMemberName(),
                            initHelper.getWareHouseName(),
                            randomTime,
                        ),
                    )
                } catch (e: Exception) {
                    throw e
                } finally {
                    latch.countDown()
                }
            }
        }
        latch.await()

        val reservations = reservationJpaRepository.findAll()
        Assertions.assertThat(reservations).hasSize(5)
    }

    private fun createDto(
        memberName: String,
        wareHouseName: String,
        reservationTime: ReservationTime,
    ): RequestCreatePendingReservationDto =
        RequestCreatePendingReservationDto(
            memberName,
            wareHouseName,
            reservationTime.startDateTime,
            reservationTime.endDateTime,
        )
}
