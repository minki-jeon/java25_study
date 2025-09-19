import static java.lang.IO.println;

/**
 * <pre>
 * packageName    : feature.preview
 * fileName       : Person
 * author         : minki-jeon
 * date           : 2025-09-19 (금)
 * description    : 생성자에서 유연한 초기화 로직 작성
 * ===========================================================
 * DATE                     AUTHOR           NOTE
 * -----------------------------------------------------------
 * 2025-09-19 (금)        minki-jeon       최초 생성
 * </pre>
 */
class Person {
    private final String name;
    private final int age;
    private final String email;

    public Person(String name, int age, String rawEmail) {
        // 생성자 내부에서 validate(검증)과 같은 복잡한 로직 수행 가능
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        // 이메일 정규화
        String normalizedEmail = rawEmail.toLowerCase().trim();
        if (!normalizedEmail.contains("@")) {
            normalizedEmail = normalizedEmail + "@gmail.com";
        }

        // 필드 초기화
        this.name = name.trim();
        this.age = age;
        this.email = normalizedEmail;

        // 초기화 후 추가 검증
        validateAge();
    }

    private void validateAge() {
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Age must be between 0 and 150; Invalid age: " + age);
        }
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public int getAge() { return age; }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + ", email='" + email + "'}";
    }

}

void main() {
    try {
        Person person1 = new Person("  John Doe  ", 33, "John.Doe.mail");
        println("person 1 = " + person1);

        Person person2 = new Person("  Jane Smith  ", 25, "Jane.Smith.mail@github.com");
        println("person 2 = " + person2);

        // 예외 테스트 1 : 빈 이름
        try {
            Person person3 = new Person("  ", 20, "test@example.com");
        } catch (IllegalArgumentException e) {
            println("Expected error: " + e.getMessage());
        }

        // 예외 테스트 2 : 잘못된 나이
        try {
            Person person4 = new Person("Invalid Age", 200, "test2@exapmle.com");
        } catch (IllegalArgumentException e) {
            println("Expected error: " + e.getMessage());
        }
    } catch (Exception e) {
            println("Unexpected error: " + e.getMessage());
    }
}

