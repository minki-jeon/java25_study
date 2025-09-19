import static java.lang.IO.println;

/**
 * <pre>
 * packageName    : feature.jep512
 * fileName       : FileProcessor
 * author         : minki-jeon
 * date           : 2025-09-19 (금)
 * description    : 자동 import와 IO helper 활용
 * ===========================================================
 * DATE                     AUTHOR           NOTE
 * -----------------------------------------------------------
 * 2025-09-19 (금)        minki-jeon       최초 생성
 * </pre>
 */

void main() throws IOException {
    Path inputFile = Path.of("input.txt");
    Files.writeString(inputFile, "Hello, " + IO.readln("What is your name? "));

    // IO helper 클래스 자동 사용 가능
    var content = Files.readString(inputFile);
    println("File content: " + content);

    // 자동으로 필요한 패키지들이 import됨
    var list = List.of("apple", "banana", "cherry");
    list.forEach(item -> println(item));

    Files.deleteIfExists(inputFile);
}