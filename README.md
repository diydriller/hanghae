## 동시성 문제
* 포인트를 충전하거나 사용하는 기능에서 동시성 문제가 발생할 수 있다.
  포인트를 충전하거나 사용하는 기능은 다음과 같은 로직을 가진다.
  ```
  1. 사용자는 일정 금액을 충전하거나 사용하는 요청을 보낸다.
  2. 해당 사용자의 포인트를 조회한다
  3. 포인트를 갱신한다
  4. 갱신된 포인트를 저장한다.
  ```
  문제는 동시 요청이 많아져서 2개 이상의 스레드가 같은 사용자의 포인트를
  조회한다면 이후 포인트를 갱신하는 로직에서 데이터 일관성이 깨지는 문제가 발생할 수 있다.
* 동시성 문제를 해결하기 위해서 하나의 스레드가 사용자의 포인트를 갱신하는 작업을 수행하는 동안 
  다른 스레드가 동일한 사용자의 포인트에 접근하지 못하도록 해야 한다.
* 일반적으로 동시성 문제를 해결하는 방법은 아래와 같이 있다.
  * ReentranceLock
  * Synchronized
---
## ReentrantLock이란?
* java의 _java.util.concurrent.locks_ 패키지에서 동기화를 위해 
  제공하는 lock의 구현체이다. 이름에서 알 수 있듯이 동일한 스레드가
  동일한 lock을 여러번 획득할 수 있다는 것이 특징이다. lock을 획득한
  개수는 내부적으로 **holdCount**라는 변수로 관리되고 **holdCount**가 0이
  될때 lock을 반환할 수 있다.
  ```kotlin
    // lock을 2번 획득했으므로 holdCount가 2가 출력이 된다.
    fun accessResource(){
        reentrantLock.lock()
        reentrantLock.lock()
        try {
            println("획득한 lock 갯수: " + reentrantLock.holdCount)
        } finally {
            reentrantLock.unlock()
            reentrantLock.unlock()
        }
    }
  ```
* 하나의 스레드가 lock을 획득한 상태에서 다른 스레드가 lock을 획득하려고 하면
  해당 스레드는 대기 상태로 전환되며 대기열에 등록이 된다. 

## Fairness란?
* ReentrantLock 객체를 생성할 때 매개변수로 boolean 값을 줄 수 있는데
  이를 통해 **Fairness Policy**를 설정할 수 있다. 
* 아래는 10개의 스레드가 lock에 접근하는 테스트 코드이다. 
  생성자의 매개변수에 true를 주면 fair rule을 적용해서 lock에 접근하는 스레드를 출력하고 
  false를 주면 unfair rule을 적용해서 lock에 접근하는 스레드를 출력한다.  
  ```kotlin
    private val reentrantLock = ReentrantLock(true)

    @Test
    fun testLockFairness(){
        val threadList = mutableListOf<Thread>()
        for(i: Int in 1..10) {
            val thread = Thread {
                accessResource()
                println("thread $i end")
            }
            println("thread $i start")
            threadList.add(thread)
        }

        threadList.forEach{
            it.start()
        }

        threadList.forEach{
            it.join()
        }
    }

    private fun accessResource(){
        reentrantLock.lock()
        try {
            Thread.sleep(1000)
        } finally {
            reentrantLock.unlock()
        }
    }
  ```
* fair rule을 따른다면 대기열에 먼저 등록된 스레드부터 우선적으로
  lock을 획득한다.
  ```shell
  thread 1 start
  thread 2 start
  thread 3 start
  thread 4 start
  thread 5 start
  thread 6 start
  thread 7 start
  thread 8 start
  thread 9 start
  thread 10 start
  thread 1 end
  thread 2 end
  thread 3 end
  thread 4 end
  thread 5 end
  thread 6 end
  thread 7 end
  thread 8 end
  thread 9 end
  thread 10 end
  ```
* unfair rule을 따른다면 대기열 순서대로 처리하지 않고 가장 먼저 lock을 요청한 스레드가 lock을 획득한다. 
  이 방법은 스레드의 starvation이 발생할 수 있다.
  ```shell
  thread 1 start
  thread 2 start
  thread 3 start
  thread 4 start
  thread 5 start
  thread 6 start
  thread 7 start
  thread 8 start
  thread 9 start
  thread 10 start
  thread 1 end
  thread 2 end
  thread 4 end
  thread 3 end
  thread 5 end
  thread 7 end
  thread 9 end
  thread 6 end
  thread 8 end
  thread 10 end
  ```
## trylock
* lock 메서드 대신 trylock 메서드를 사용하면 timeout을 지정해서 일정시간 내에 lock을 
  획득하지 못하면 실패처리를 하고 넘어간다. 이때 trylock을 쓴다면 fair rule을 적용해도 깨진다.
  ```kotlin
    if (lock.tryLock(TIMEOUT, TimeUnit.MILLISECONDS)) {
        try {
            joinPoint.proceed()
        } finally {
            lock.unlock()
            lockManager.releaseLock(key)
        }
    } else {
        throw IllegalStateException(ExceptionStatus.TIMEOUT_ERROR.value)
    }
  ```
----
## Synchronized
* **synchronized** 블록을 사용해서 동기화 문제를 해결할 수 있다.
  모든 객체는 하나의 **monitor**를 가지고 있는데 스레드가 **synchronized** 블록에 
  진입할 때 **monitor lock**을 획득한다. 하나의 스레드가 **monitor lock**을 
  점유하고 있는 동안 다른 스레드들은 접근할 수 없다.
  ```kotlin
    private fun accessResource(){
        synchronized(this){
            
        }
    }
  ```
* **synchronized**는 lock 획득 순서를 보장하지 않는다.
---
## ReentrantLock을 사용해서 동시성 문제 해결
* 포인트를 충전하는 메서드와 포인트를 사용하는 메서드는 다음과 같다. 
  동시성을 해결할기 위해 메서드에 접근하는 스레드들은 lock을 획득해서 잠금해야한다.
  ```kotlin
    // 포인트룰 충전하는 메서드
    fun chargeUserPoint(dto: ChargePointDto): UserPoint {
      val userPoint = userPointTable.selectById(dto.userId)
      val updatedUserPoint = userPoint.charge(dto.amount)
      return userPointTable.insertOrUpdate(updatedUserPoint.id, updatedUserPoint.point)
    }

    // 포인트를 사용하는 메서드
    fun useUserPoint(dto: UsePointDto): UserPoint {
        val userPoint = userPointTable.selectById(dto.userId)
        val updatedUserPoint = userPoint.use(dto.amount)
        return userPointTable.insertOrUpdate(updatedUserPoint.id, updatedUserPoint.point)
    }
  ```
* 동시성 문제를 해결하기 위해 다음과 같은 기준을 세웠다. 
  ```kotlin
    1. lock을 어디에 걸어야할까
    2. lock을 획득하는 순서를 어떻게 정할까
  ```
  lock은 포인트를 충전하거나 사용하는 사용자마다 거는 것이 좋다고 생각했고 
  대기열 순서대로 lock을 획득하는 것이 좋다고 생각해서 **synchronized** 대신 **ReentrantLock**을 사용했다. 
* 사용자 아이디를 key로 가지고 ReentrantLock을 value로 가지는 ConcurrentHashMap을 생성하고
  메서드가 실행될때마다 해당 사용자 아이디를 map에서 조회해서 lock을 획득하도록 했다.
  ```kotlin
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
  ```
  ```kotlin
    val key = synchronizeWithKey.key
    val lock = lockManager.getLock(key)
    lock.lock()
    return try {
        joinPoint.proceed()
    } finally {
        lock.unlock()
        lockManager.releaseLock(key)
    }
  ```
  ```kotlin
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
  ```