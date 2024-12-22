package io.hhplus.tdd.lock

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class SynchronizeWithKey(val key: String = "")
