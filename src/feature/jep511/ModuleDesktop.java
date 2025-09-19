//import javax.swing.*;

import static java.lang.IO.println;
import module java.desktop;

/**
 * <pre>
 * packageName    : feature.jep511
 * fileName       : ModuleDesktop
 * author         : minki-jeon
 * date           : 2025-09-19 (금)
 * description    : java.awt / java.swing 모듈의 모든 패키지 import
 * ===========================================================
 * DATE                     AUTHOR           NOTE
 * -----------------------------------------------------------
 * 2025-09-19 (금)        minki-jeon       최초 생성
 * </pre>
 */

void main() {

    SwingUtilities.invokeLater(() -> {
        JFrame frame = new JFrame("Java 25 Module Import Demo");
        JLabel label = new JLabel("\t Hello from Java 25 !");

        frame.add(label);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        println("Swing application started");
    });

}