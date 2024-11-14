package rkrk.reservation.unit.domain

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import rkrk.reservation.warehouse.reservation.domain.ReservationTime
import java.time.LocalDateTime
import java.time.LocalTime

class ReservationTimeTest {
    @Test
    @DisplayName("하루에 끝나는 예약을 타임슬롯리스트로 만들수 있다")
    fun oneDayReservationToTimeSlots() {
        val startDateTime = LocalDateTime.of(2024, 10, 25, 10, 30, 0)
        val endDateTime = LocalDateTime.of(2024, 10, 25, 13, 30, 0)
        val reservationTime =
            ReservationTime(
                startDateTime,
                endDateTime,
            )

        val toTimeSlots = reservationTime.toTimeSlots()

        Assertions.assertThat(toTimeSlots.size).isEqualTo(1)

        Assertions.assertThat(toTimeSlots[0].date).isEqualTo(startDateTime.toLocalDate())
        Assertions.assertThat(toTimeSlots[0].startTime).isEqualTo(startDateTime.toLocalTime())
        Assertions.assertThat(toTimeSlots[0].date).isEqualTo(endDateTime.toLocalDate())
        Assertions.assertThat(toTimeSlots[0].endTime).isEqualTo(endDateTime.toLocalTime())
    }

    @Test
    @DisplayName("이틀에 걸친 예약을 타임슬롯리스트로 만들수 있다")
    fun twoDayReservationToTimeSlots() {
        val startDateTime = LocalDateTime.of(2024, 10, 25, 10, 30, 0)
        val endDateTime = LocalDateTime.of(2024, 10, 26, 13, 30, 0)
        val reservationTime =
            ReservationTime(
                startDateTime,
                endDateTime,
            )

        val toTimeSlots = reservationTime.toTimeSlots()

        Assertions.assertThat(toTimeSlots.size).isEqualTo(2)

        Assertions.assertThat(toTimeSlots[0].date).isEqualTo(startDateTime.toLocalDate())
        Assertions.assertThat(toTimeSlots[0].startTime).isEqualTo(startDateTime.toLocalTime())
        Assertions.assertThat(toTimeSlots[0].endTime).isEqualTo(LocalTime.MAX)

        Assertions.assertThat(toTimeSlots[1].date).isEqualTo(endDateTime.toLocalDate())
        Assertions.assertThat(toTimeSlots[1].startTime).isEqualTo(LocalTime.MIN)
        Assertions.assertThat(toTimeSlots[1].endTime).isEqualTo(endDateTime.toLocalTime())
    }

    @Test
    @DisplayName("5일에 걸친 예약을 타임슬롯리스트로 만들수 있다")
    fun fiveDayReservationToTimeSlots() {
        val startDateTime = LocalDateTime.of(2024, 10, 25, 10, 30, 0)
        val endDateTime = LocalDateTime.of(2024, 10, 29, 13, 30, 0)
        val reservationTime =
            ReservationTime(
                startDateTime,
                endDateTime,
            )

        val toTimeSlots = reservationTime.toTimeSlots()

        Assertions.assertThat(toTimeSlots.size).isEqualTo(5)

        Assertions.assertThat(toTimeSlots[0].date).isEqualTo(startDateTime.toLocalDate())
        Assertions.assertThat(toTimeSlots[0].startTime).isEqualTo(startDateTime.toLocalTime())
        Assertions.assertThat(toTimeSlots[0].endTime).isEqualTo(LocalTime.MAX)

        Assertions.assertThat(toTimeSlots[1].date).isEqualTo(startDateTime.toLocalDate().plusDays(1))
        Assertions.assertThat(toTimeSlots[1].startTime).isEqualTo(LocalTime.MIN)
        Assertions.assertThat(toTimeSlots[1].endTime).isEqualTo(LocalTime.MAX)

        Assertions.assertThat(toTimeSlots[2].date).isEqualTo(startDateTime.toLocalDate().plusDays(2))
        Assertions.assertThat(toTimeSlots[2].startTime).isEqualTo(LocalTime.MIN)
        Assertions.assertThat(toTimeSlots[2].endTime).isEqualTo(LocalTime.MAX)

        Assertions.assertThat(toTimeSlots[4].date).isEqualTo(endDateTime.toLocalDate())
        Assertions.assertThat(toTimeSlots[4].startTime).isEqualTo(LocalTime.MIN)
        Assertions.assertThat(toTimeSlots[4].endTime).isEqualTo(endDateTime.toLocalTime())
    }

    @Test
    @DisplayName("5일에 걸친 예약의 시작하는 타임슬롯만 받아올수있다")
    fun fiveDayReservationToStartTimeSlot() {
        val startDateTime = LocalDateTime.of(2024, 10, 25, 10, 30, 0)
        val endDateTime = LocalDateTime.of(2024, 10, 29, 13, 30, 0)
        val reservationTime =
            ReservationTime(
                startDateTime,
                endDateTime,
            )

        val startTimeSlot = reservationTime.startTimeSlot()

        Assertions.assertThat(startTimeSlot.date).isEqualTo(startDateTime.toLocalDate())
        Assertions.assertThat(startTimeSlot.startTime).isEqualTo(startDateTime.toLocalTime())
        Assertions.assertThat(startTimeSlot.endTime).isEqualTo(LocalTime.MAX)
    }

    @Test
    @DisplayName("5일에 걸친 예약의 종료하는 타임슬롯만 받아올수있다")
    fun fiveDayReservationToEndTimeSlot() {
        val startDateTime = LocalDateTime.of(2024, 10, 25, 10, 30, 0)
        val endDateTime = LocalDateTime.of(2024, 10, 29, 13, 30, 0)
        val reservationTime =
            ReservationTime(
                startDateTime,
                endDateTime,
            )

        val startTimeSlot = reservationTime.endTimeSlot()

        Assertions.assertThat(startTimeSlot.date).isEqualTo(endDateTime.toLocalDate())
        Assertions.assertThat(startTimeSlot.startTime).isEqualTo(LocalTime.MIN)
        Assertions.assertThat(startTimeSlot.endTime).isEqualTo(endDateTime.toLocalTime())
    }
}
