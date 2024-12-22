package io.hhplus.tdd.service.dto

import io.hhplus.tdd.point.TransactionType

data class RecordHistoryDto(
    val userId: Long,
    val amount: Long,
    val type: TransactionType
)
