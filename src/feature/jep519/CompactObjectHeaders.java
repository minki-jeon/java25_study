import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

import static java.lang.IO.println;

/**
 * <pre>
 * packageName    : feature.jep450
 * fileName       : CompactObjectHeaders
 * author         : minki-jeon
 * date           : 2025-09-19 (금)
 * description    : 메모리 사용량 개선
 * ===========================================================
 * DATE                     AUTHOR           NOTE
 * -----------------------------------------------------------
 * 2025-09-19 (금)        minki-jeon       최초 생성
 * </pre>
 */

// JVM 실행 시 -XX:+UseCompactObjectHeaders 옵션 사용하여 메모리 사용량 비교
// [result (option 미추가)]
/**
 == Compact Object Headers 메모리 테스트 ===
 JVM Arguments: [--enable-preview, -javaagent:C:\dev_env\IntelliJ IDEA 2025.1.1.1\lib\idea_rt.jar=13552, -Dfile.encoding=UTF-8, -Dsun.stdout.encoding=UTF-8, -Dsun.stderr.encoding=UTF-8]

 == 최종 결과 ==
총 객체 수: 10000000
사용된 메모리: 190MB
객체당 평균 메모리: 19 bytes

=== 이론적 예상값 ===
전통적 헤더 예상: 123 MB
압축 헤더 예상: 85 MB
예상 절약량: 38 MB

=== 분석 ===
Compact Object Headers 적용 추정: NO
첫 번째 객체 값: 0
마지막 객체 값: 127
**/
// [result (option 추가)]
/**
== Compact Object Headers 메모리 테스트 ===
JVM Arguments: [-XX:+UseCompactObjectHeaders, --enable-preview, -javaagent:C:\dev_env\IntelliJ IDEA 2025.1.1.1\lib\idea_rt.jar=13552, -Dfile.encoding=UTF-8, -Dsun.stdout.encoding=UTF-8, -Dsun.stderr.encoding=UTF-8]

== 최종 결과 ==
총 객체 수: 10000000
사용된 메모리: 190MB
객체당 평균 메모리: 19 bytes

=== 이론적 예상값 ===
전통적 헤더 예상: 123 MB
압축 헤더 예상: 85 MB
예상 절약량: 38 MB

=== 분석 ===
Compact Object Headers 적용 추정: NO
첫 번째 객체 값: 0
마지막 객체 값: 127
**/

void main() throws InterruptedException {
    MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    Runtime runtime = Runtime.getRuntime();

    println("== Compact Object Headers 메모리 테스트 ===");
    println("JVM Arguments: " + ManagementFactory.getRuntimeMXBean().getInputArguments());

    // 강제 GC로 시작 상태 정리
    System.gc();
    Thread.sleep(100);

    long beforeMemory = runtime.totalMemory() - runtime.freeMemory();
    println("시작 전 메모리 사용량: " + (beforeMemory / 1024 / 1024) + "MB");

    // 1000만 개의 작은 객체 생성 (헤더 오버헤드가 상대적으로 큰 객체)
    int objectCount = 10_000_000;
    SmallObject[] objects = new SmallObject[objectCount];

    println("객체 생성 중 (" + objectCount + " 개)");

    for (int i = 0; i < objectCount; i++) {
        objects[i] = new SmallObject((byte) (i % 128));

        // 100만개의 객체 생성할 때마다 메모리 상태 출력
        if (i > 0 && i % 1_000_000 == 0) {
            long currentMemory = runtime.totalMemory() - runtime.freeMemory();
            println(i + " 개 생성 후: " + (currentMemory / 1024 / 1024) + "MB");
        }
    }

    // 최종 메모리 측정
    System.gc();
    Thread.sleep(200);

    long afterMemory = runtime.totalMemory() - runtime.freeMemory();
    long usedMemory = afterMemory - beforeMemory;

    println("\n== 최종 결과 ==");
    println("총 객체 수: " + objectCount);
    println("사용된 메모리: " + (usedMemory / 1024 / 1024) + "MB");
    println("객체당 평균 메모리: " + (usedMemory / objectCount) + " bytes");


    // 이론적인 계산
    int fieldSize = 1; // byte 필드 1개
    int arrayOverhead = 16; // 배열 자체의 오버헤드
    int traditionalHeaderSize = 12; // 전통적인 객체 헤더 (Mark Word + Class Pointer)
    int compactHeaderSize = 8;      // 압축된 헤더 크기

    long traditionalExpected = (long)objectCount * (traditionalHeaderSize + fieldSize) + arrayOverhead;
    long compactExpected = (long)objectCount * (compactHeaderSize + fieldSize) + arrayOverhead;

    println("\n=== 이론적 예상값 ===");
    println("전통적 헤더 예상: " + (traditionalExpected / 1024 / 1024) + " MB");
    println("압축 헤더 예상: " + (compactExpected / 1024 / 1024) + " MB");
    println("예상 절약량: " + ((traditionalExpected - compactExpected) / 1024 / 1024) + " MB");

    // 메모리 효율성 분석
    println("\n=== 분석 ===");
    boolean isCompactLikely = usedMemory < traditionalExpected * 0.9;
    println("Compact Object Headers 적용 추정: " + (isCompactLikely ? "YES" : "NO"));

    // 객체 참조 유지 (GC 방지)
    println("첫 번째 객체 값: " + objects[0].getValue());
    println("마지막 객체 값: " + objects[objectCount-1].getValue());


}

static class SmallObject {
    private final byte value;

    SmallObject(byte value) {
        this.value = value;
    }

    byte getValue() {return value;}

}
