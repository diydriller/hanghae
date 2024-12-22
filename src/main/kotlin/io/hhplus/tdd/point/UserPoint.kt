package io.hhplus.tdd.point

import io.hhplus.tdd.exception.ExceptionStatus

data class UserPoint(
    val id: Long,
    val point: Long,
    val updateMillis: Long,
) {
    fun charge(amount: Long): UserPoint {
        return UserPoint(id, point + amount, updateMillis)
    }

    fun use(amount: Long): UserPoint {
        if (point < amount) {
            throw RuntimeException(ExceptionStatus.NOT_ENOUGH_BALANCE.value)
        }
        return UserPoint(id, point - amount, updateMillis)
    }
}
