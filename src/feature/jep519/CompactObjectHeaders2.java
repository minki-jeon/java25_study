
import java.lang.management.ManagementFactory;

import static java.lang.IO.println;

/**
 * <pre>
 * packageName    : feature.jep519
 * fileName       : CompactObjectHeaders2
 * author         : minki-jeon
 * date           : 2025-09-19 (금)
 * description    : 메모리 사용량 개선
 * ===========================================================
 * DATE                     AUTHOR           NOTE
 * -----------------------------------------------------------
 * 2025-09-19 (금)        minki-jeon       최초 생성
 * </pre>
 */

/**
 === Compact Object Headers 상세 분석 ===
 JVM Arguments: [--enable-preview, -javaagent:C:\dev_env\IntelliJ IDEA 2025.1.1.1\lib\idea_rt.jar=4858, -Dfile.encoding=UTF-8, -Dsun.stdout.encoding=UTF-8, -Dsun.stderr.encoding=UTF-8]
 Empty Object: 1000000개, 총 15 MB, 객체당 16.0 bytes
 첫 객체 클래스: TinyObject
 1-byte Object: 1000000개, 총 19 MB, 객체당 20.3 bytes
 첫 객체 클래스: SmallObject
 8-byte Object: 1000000개, 총 27 MB, 객체당 28.9 bytes
 첫 객체 클래스: MediumObject

 === Compact Object Headers 상세 분석 ===
 JVM Arguments: [-XX:-UseCompactObjectHeaders, --enable-preview, -javaagent:C:\dev_env\IntelliJ IDEA 2025.1.1.1\lib\idea_rt.jar=4848, -Dfile.encoding=UTF-8, -Dsun.stdout.encoding=UTF-8, -Dsun.stderr.encoding=UTF-8]
 Empty Object: 1000000개, 총 11 MB, 객체당 12.6 bytes
 첫 객체 클래스: TinyObject
 1-byte Object: 1000000개, 총 19 MB, 객체당 20.5 bytes
 첫 객체 클래스: SmallObject
 8-byte Object: 1000000개, 총 26 MB, 객체당 28.3 bytes
 첫 객체 클래스: MediumObject

 === Compact Object Headers 상세 분석 ===
 JVM Arguments: [-XX:+UseCompactObjectHeaders, --enable-preview, -javaagent:C:\dev_env\IntelliJ IDEA 2025.1.1.1\lib\idea_rt.jar=1741, -Dfile.encoding=UTF-8, -Dsun.stdout.encoding=UTF-8, -Dsun.stderr.encoding=UTF-8]
 Empty Object: 1000000개, 총 8 MB, 객체당 8.8 bytes
 첫 객체 클래스: TinyObject
 1-byte Object: 1000000개, 총 19 MB, 객체당 20.4 bytes
 첫 객체 클래스: SmallObject
 8-byte Object: 1000000개, 총 19 MB, 객체당 20.9 bytes
 첫 객체 클래스: MediumObject
 */

private class TinyObject {
    // 필드 없음 - 순수 헤더 오버헤드
}

private class SmallObject {
    byte value;     // 1 byte
}

private class MediumObject {
    long value;     // 8 byte
}
void main() {
    println("=== Compact Object Headers 상세 분석 ===");
//    println("JVM 옵션: " + System.getProperty("java.vm.options", "정보 없음"));
    println("JVM Arguments: " + ManagementFactory.getRuntimeMXBean().getInputArguments());
    /*
    Properties props = System.getProperties();
    Enumeration<Object> enumerator = props.keys();
    while (enumerator.hasMoreElements()) {
        Object ele = enumerator.nextElement();
        String key = ele.toString();
        System.out.println(key + ": " + System.getProperty(key));
    }
    */
    // 다양한 크기의 객체로 테스트
    testObjectSize("Empty Object", TinyObject.class, TinyObject::new);
    testObjectSize("1-byte Object", SmallObject.class, SmallObject::new);
    testObjectSize("8-byte Object", MediumObject.class, MediumObject::new);
}

interface ObjectFactory<T> {
    T create();
}

private <T> void testObjectSize(String name, Class<T> clazz, ObjectFactory<T> factory) {
    Runtime runtime = Runtime.getRuntime();
    System.gc();
    try {
        Thread.sleep(100);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
    long before = runtime.totalMemory() - runtime.freeMemory();

    int count = 1_000_000;
    List<T> objects = new ArrayList<>(count);
    for (int i = 0; i < count; i++) {
        objects.add(factory.create());
    }

    System.gc();
    try{
        Thread.sleep(100);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
    long after = runtime.totalMemory() - runtime.freeMemory();

    long used = after - before;

    println(String.format("%s: %d개, 총 %d MB, 객체당 %.1f bytes",
            name, count,
            used / 1024 / 1024,
            (double) used / count));

    // 참조 유지
    println("  첫 객체 클래스: " + objects.get(0).getClass().getSimpleName());
}

void measureActualMemory() {
    println("\n=== 실제 메모리 할당 패턴 ===");

    Runtime runtime = Runtime.getRuntime();
    System.gc();

    long baseline = runtime.totalMemory() - runtime.freeMemory();
    println("기준선 메모리: " + baseline / 1024 / 1024 + " MB");

    // 단계별로 메모리 증가 측정
    List<Object> objects = new ArrayList<>();

    for (int step = 1; step <= 10; step++) {
        int batchSize = 500_000;

        for (int i = 0; i < batchSize; i++) {
            objects.add(new TinyObject());
        }

        System.gc();
        long current = runtime.totalMemory() - runtime.freeMemory();
        long used = current - baseline;

        println(String.format("Step %d: %d개 객체, %d MB 사용, 객체당 %.1f bytes",
                step,
                step * batchSize,
                used / 1024 / 1024,
                (double) used / (step * batchSize)));
    }

    // 마지막 객체 참조 유지
    println("총 객체 수: " + objects.size());
}