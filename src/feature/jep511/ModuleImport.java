//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.xpath.XPath;
//import javax.xml.xpath.XPathFactory;

import static java.lang.IO.println;
import module java.xml;

/**
 * <pre>
 * packageName    : feature.jep511
 * fileName       : ModuleImport
 * author         : minki-jeon
 * date           : 2025-09-19 (금)
 * description    : java.xml 모듈의 모든 패키지 import
 * ===========================================================
 * DATE                     AUTHOR           NOTE
 * -----------------------------------------------------------
 * 2025-09-19 (금)        minki-jeon       최초 생성
 * </pre>
 */

void main() throws ParserConfigurationException {
    XPathFactory xPathFactory = XPathFactory.newInstance();
    XPath xPath = xPathFactory.newXPath();

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();

    println("XPath factory created: " + xPathFactory.getClass().getName());
    println("Document builder created: " + builder.getClass().getName());

    var names = java.util.List.of("Alice", "Bob", "Charlie");
    println("names: " + names);
}
