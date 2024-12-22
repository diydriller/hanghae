package io.hhplus.tdd.service

import io.hhplus.tdd.database.UserPointTable
import io.hhplus.tdd.lock.SynchronizeWithKey
import io.hhplus.tdd.point.UserPoint
import io.hhplus.tdd.service.dto.ChargePointDto
import io.hhplus.tdd.service.dto.UsePointDto
import org.springframework.stereotype.Service

@Service
class PointManagementService(
    private val userPointTable: UserPointTable
) {
    fun getUserPoint(userId: Long): UserPoint {
        return userPointTable.selectById(userId)
    }

    @SynchronizeWithKey(key = "user-#{#dto.userId}-transaction")
    fun chargeUserPoint(dto: ChargePointDto): UserPoint {
        val userPoint = userPointTable.selectById(dto.userId)
        val updatedUserPoint = userPoint.charge(dto.amount)
        return userPointTable.insertOrUpdate(updatedUserPoint.id, updatedUserPoint.point)
    }

    @SynchronizeWithKey(key = "user-#{#dto.userId}-transaction")
    fun useUserPoint(dto: UsePointDto): UserPoint {
        val userPoint = userPointTable.selectById(dto.userId)
        val updatedUserPoint = userPoint.use(dto.amount)
        return userPointTable.insertOrUpdate(updatedUserPoint.id, updatedUserPoint.point)
    }
}