package io.hhplus.tdd.database

import io.hhplus.tdd.point.TransactionType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class PointHistoryTableUnitTest {
    private lateinit var pointHistoryTable: PointHistoryTable

    @BeforeEach
    fun setUp() {
        pointHistoryTable = PointHistoryTable()
    }

    @DisplayName("selectAllByUserId method should return existing PointHistory")
    @Test
    fun returnExistingPointHistory() {
        // given
        val userId = 1L
        pointHistoryTable.insert(userId, 100L, TransactionType.USE, System.currentTimeMillis())
        pointHistoryTable.insert(userId, 50L, TransactionType.CHARGE, System.currentTimeMillis())

        // when
        val result = pointHistoryTable.selectAllByUserId(userId)

        // then
        assertEquals(2, result.size)
        assertTrue(result.all { it.userId == userId })
    }
}