package io.hhplus.tdd.point

import io.hhplus.tdd.exception.ExceptionStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class UserPointUnitTest {

    @Test
    @DisplayName("charging point method should increase user point")
    fun chargePointSuccessTest() {
        // given
        val userPoint = UserPoint(1, 1000, System.currentTimeMillis())
        // when
        val updatedUserPoint = userPoint.charge(1000)
        // then
        assertEquals(updatedUserPoint.point, 2000)
    }

    @Test
    @DisplayName("using point method should decrease user point")
    fun usePointSuccessTest() {
        // given
        val userPoint = UserPoint(1, 1000, System.currentTimeMillis())
        // when
        val updatedUserPoint = userPoint.use(500)
        // then
        assertEquals(updatedUserPoint.point, 500)
    }

    @Test
    @DisplayName("using point more than balance should throw exception")
    fun usePointFailTest() {
        // given
        val userPoint = UserPoint(1, 1000, System.currentTimeMillis())
        // when & then
        val exception = assertThrows(RuntimeException::class.java) {
            userPoint.use(2000)
        }
        assertEquals(ExceptionStatus.NOT_ENOUGH_BALANCE.value, exception.message)
    }
}