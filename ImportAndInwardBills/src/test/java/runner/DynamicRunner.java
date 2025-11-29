package runner;
import org.testng.TestNG;
import java.util.Collections;

public class DynamicRunner {

    public static void main(String[] args) {

        String suitePath = System.getProperty("suite");

        if (suitePath == null || suitePath.trim().isEmpty()) {
            System.out.println("❌ No TestNG suite specified! Use: -Dsuite=<xml>");
            return;
        }

        System.out.println("🔥 Running Dynamic TestNG Suite: " + suitePath);

        TestNG testng = new TestNG();
        testng.setTestSuites(Collections.singletonList(suitePath));
        testng.setPreserveOrder(true);
        testng.run();
    }
}
