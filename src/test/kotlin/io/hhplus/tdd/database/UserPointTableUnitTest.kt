package io.hhplus.tdd.database

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class UserPointTableUnitTest {
    private lateinit var userPointTable: UserPointTable

    @BeforeEach
    fun setUp() {
        userPointTable = UserPointTable()
    }

    @DisplayName("selectById method should return existing UserPoint")
    @Test
    fun returnExistingUserPoint() {
        // given
        val id = 1L
        val amount = 1000L
        userPointTable.insertOrUpdate(id, amount)

        // when
        val result = userPointTable.selectById(id)

        // then
        assertEquals(id, result.id)
        assertEquals(amount, result.point)
    }
}