package io.hhplus.tdd.point

import io.hhplus.tdd.database.PointHistoryTable
import io.hhplus.tdd.database.UserPointTable
import org.springframework.stereotype.Service

@Service
class PointService(
    private val userPointTable: UserPointTable,
    private val pointHistoryTable: PointHistoryTable
) {
    fun getUserPoint(userId: Long): UserPoint {
        return userPointTable.selectById(userId)
    }

    fun getUserPointHistory(userId: Long): List<PointHistory> {
        return pointHistoryTable.selectAllByUserId(userId)
    }
}