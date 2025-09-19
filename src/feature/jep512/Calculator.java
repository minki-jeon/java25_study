import static java.lang.IO.println;

/**
 * <pre>
 * packageName    : feature.jep512
 * fileName       : Calculator
 * author         : minki-jeon
 * date           : 2025-09-19 (금)
 * description    :
 * ===========================================================
 * DATE                     AUTHOR           NOTE
 * -----------------------------------------------------------
 * 2025-09-19 (금)        minki-jeon       최초 생성
 * </pre>
 */

    private String version = "1.0";

    void main() {
        println("Calculator version: " + version);
        int result = add(10, 5);
        System.out.println("10 + 5 = " + result);
    }

    private int add(int a, int b) {
        return a + b;
    }

