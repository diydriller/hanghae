package io.hhplus.tdd.lock

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

@Aspect
@Component
class SynchronizeWithKeyAspect(
    private val lockManager: LockManager
) {
    @Around("@annotation(synchronizeWithKey)")
    fun synchronizeWithKeyMethod(joinPoint: ProceedingJoinPoint, synchronizeWithKey: SynchronizeWithKey): Any? {
        val key = synchronizeWithKey.key
        val lock = lockManager.getLock(key)
        lock.lock()
        return try {
            joinPoint.proceed()
        } finally {
            lock.unlock()
            lockManager.releaseLock(key)
        }
    }
}