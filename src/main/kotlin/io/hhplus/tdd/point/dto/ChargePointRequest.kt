package io.hhplus.tdd.point.dto

import jakarta.validation.constraints.Min

data class ChargePointRequest(
    @field:Min(value = 0, message = "amount must be greater than or equal to zero")
    val amount: Long = 0
)