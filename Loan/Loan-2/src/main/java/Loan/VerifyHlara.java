package NegativeTestCase;

import java.io.IOException;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Annotation.ExcelData;
import Base.BaseTest;
import Base.DriverManager;
import Utilities.Dataproviders;
import Utilities.LogOut;
import Utilities.RowData;
import Utilities.WindowHandle;

public class VerifyHlara extends BaseTest {
	

	@BeforeClass
    public  void setup() throws IOException {
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        String userID = DriverManager.getProperty("userid1");
        String password = DriverManager.getProperty("password1");
        DriverManager.login(userID, password);
    }

	@Test(dataProvider = "testcase", dataProviderClass = Dataproviders.class)
    @ExcelData(sheetName = "Sheet2", rowIndex = {10})
    public void CreateLoan(RowData rowData) throws Exception {

            @SuppressWarnings("unused")
			String mainWindowHandle = driver.getWindowHandle();
            WindowHandle.slowDown(4);

            // Menu selection
            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),rowData.getByIndex(1));
            WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));
            
            WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("funcCode"))),rowData.getByIndex(2));
            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("acctNum"))),rowData.getByIndex(4));
            WindowHandle.safeClick(driver, wait, By.id("Accept"));

            //////////////Schedule History//////////////////
        	WindowHandle.slowDown(2);
			WebElement larehist = wait.until(ExpectedConditions.elementToBeClickable(By.id("larehist")));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", larehist);
			larehist.click();
			

		    WindowHandle.slowDown(2);
		    WebElement laprepay = wait.until(ExpectedConditions.elementToBeClickable(By.id("laprepay")));
		    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", laprepay);
		    laprepay.click();
		    WindowHandle.safeClick(driver, wait, By.id("Submit"));


		    WebElement row = driver.findElement(By.cssSelector("tr.textfielddisplaylabel1"));
            System.out.println("Row text: " + row.getText());
            driver.switchTo().defaultContent();
		    LogOut.performLogout(driver, wait);
}
}