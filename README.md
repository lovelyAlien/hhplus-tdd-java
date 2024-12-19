## 동시성 제어
동시성 제어는 다수의 프로세스나 스레드가 동일한 리소스(예: 데이터베이스, 파일, 메모리 등)에 동시에 접근하거나 작업을 수행할 때, 데이터 무결성과 시스템 안정성을 보장하기 위해 이를 관리하는 기술과 기법을 의미한다.

## 문제 상황 예시
![image](https://github.com/user-attachments/assets/7d7dfc14-7cf5-4bf1-85fe-9f8fc8b2e367)

위와 같이 2개의 트랜잭션에서 동일한 데이터를 변경할 경우 마지막으로 커밋된 내용만이 인정되고, 먼저 커밋된 내용이 분실되는 문제를 두번의 갱신 분실 문제 (second lost updates problem) 라고 부른다. 
이와 같이 두번의 갱신 분실 문제와 같은 경우 트랜잭션으로 처리할 수 있는 범위를 넘어선다. 따라서 별도의 방법이 필요하다.

참고로, **두 번의 갱신 분실 문제**라는 이름은 문제의 발생 과정에서 동일한 데이터에 대해 두 번의 갱신(update)이 일어나지만, 그 중 하나의 갱신이 손실된다는 점을 강조한 것이다.

## ReentrantLock
ReentrantLock은 자바의 `java.util.concurrent.locks` 패키지에 포함된 동기화 메커니즘 중 하나로, 명시적인 락을 제공하여 동시성을 제어해준다.  

### 주요 특징
- 재진입 가능(Reentrant)
  - 동일한 스레드가 이미 획득한 락을 다시 획득할 수 있다. -> 데드락 방지
 ```java
    ReentrantLock lock = new ReentrantLock();
    lock.lock();
    try {
        // 공유 자원에 대한 접근 코드
    } finally {
        lock.unlock();
    }

```
- 공정성(Fairness)
  - ReentrantLock은 생성 시 공정 모드를 설정할 수 있다.
 ```java
ReentrantLock lock = new ReentrantLock(true); // 공정 모드
```
### 공정성이란?
- 여러 스레드가 동일한 리소스에 접근하려고 할 때, 요청한 순서대로 접근 권한을 부여하는 것을 의미한다.
- 공정 모드에서는 락을 대기하는 스레드가 FIFO 순서로 락을 획득한다.

### synchronized와 비교
ReentrantLock는 기본적으로 공정성을 보장하지만 synchronized는 보장하지 않는다.
- JVM 및 운영 체제 스케줄러에 의해 락 획득 순서가 결정 된다.
- 락을 요청한 순서를 고려하지 않아 특정 스레드가 자주 락을 획득하거나 기아 상태에 빠질 수 있다.
```java
// synchronized 버전
public class SynchronizedExample {
    private final Object lockObject = new Object();

    public void doWork() {
        synchronized(lockObject) {
            // 임계 영역 코드
        }
    }
}

// ReentrantLock 버전
public class ReentrantLockExample {
    private final ReentrantLock lock = new ReentrantLock();

    public void doWork() {
        lock.lock();
        try {
            // 임계 영역 코드
        } finally {
            lock.unlock();
        }
    }
}
```




