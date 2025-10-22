package Base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    @Parameters("browser")
    @BeforeMethod
    public void setUp(@Optional("chrome") String browser) throws Exception {
        // ✅ Initialize driver from DriverManager (Grid-based)
        DriverManager.initDriver(browser);

        // ✅ Store locally for easy access in child tests
        driver = DriverManager.getDriver();
        wait = DriverManager.getWait();
    }

    @AfterMethod
    public void tearDown() {
        // ✅ Quit driver and cleanup
        DriverManager.quitDriver();
    }
}
