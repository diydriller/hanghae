package io.hhplus.tdd.controller.dto

import jakarta.validation.constraints.Min

data class UsePointRequest(
    @field:Min(value = 0, message = "amount must be greater than or equal to zero")
    val amount: Long = 0
)
