package io.hhplus.tdd.facade

import io.hhplus.tdd.point.TransactionType
import io.hhplus.tdd.point.UserPoint
import io.hhplus.tdd.service.PointHistoryManagementService
import io.hhplus.tdd.service.PointManagementService
import io.hhplus.tdd.service.dto.ChargePointDto
import io.hhplus.tdd.service.dto.RecordHistoryDto
import io.hhplus.tdd.service.dto.UsePointDto
import org.springframework.stereotype.Component

@Component
class PointFacade(
    private val pointManagementService: PointManagementService,
    private val pointHistoryManagementService: PointHistoryManagementService
) {
    fun chargeUserPoint(dto: ChargePointDto): UserPoint {
        val userPoint = pointManagementService.chargeUserPoint(dto)
        val recordHistoryDto = RecordHistoryDto(dto.userId, dto.amount, TransactionType.CHARGE)
        pointHistoryManagementService.recordUserPointHistory(recordHistoryDto)
        return userPoint
    }

    fun useUserPoint(dto: UsePointDto): UserPoint {
        val userPoint = pointManagementService.useUserPoint(dto)
        val recordHistoryDto = RecordHistoryDto(dto.userId, dto.amount, TransactionType.USE)
        pointHistoryManagementService.recordUserPointHistory(recordHistoryDto)
        return userPoint
    }
}