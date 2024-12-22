package io.hhplus.tdd.lock

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

@Component
class LockManager {
    private val lockMap = ConcurrentHashMap<String, ReentrantLock>()

    fun getLock(key: String): Lock {
        return lockMap.computeIfAbsent(key) { ReentrantLock(true) }
    }

    fun releaseLock(key: String) {
        val lock = lockMap[key]
        if (lock != null && lock.isHeldByCurrentThread) {
            try {
                lock.unlock()
            } finally {
                lockMap.remove(key)
            }
        }
    }
}