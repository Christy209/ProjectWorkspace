package OD;


import java.io.IOException;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
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

public class ModifyODVerification extends BaseTest {
	protected static WebDriver driver;
	protected static WebDriverWait wait;

	@BeforeClass
	public void setup() throws IOException {
		driver = DriverManager.getDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(40));
		String userID = DriverManager.getProperty("userid1");
		String password = DriverManager.getProperty("password1");
		DriverManager.login(userID, password);
		System.out.println("✅ Logged in as: " + userID);
	}

	@Test(dataProvider = "testcase", dataProviderClass = Dataproviders.class)
	@ExcelData(sheetName = "Sheet3", rowIndex = { 1, 4 })
	public static void CreateLoan(RowData rowData, RowData modifyData) throws Exception {

		String mainwindow = driver.getWindowHandle();
		System.out.println("MainWindow :" + mainwindow);
		try {
			WindowHandle.slowDown(3);
			  WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		        
		       WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),modifyData.getByIndex(1));
		       WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
		       ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);
			   wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
			   wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));

			   WindowHandle.slowDown(1);
			   WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("mode"))),modifyData.getByIndex(2));

			   WebElement acctID = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("acctNo")));
			   String AcctID = rowData.getByHeader("Created_AccountID");
			   ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", acctID, AcctID);



			   WebElement acceptButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Accept")));
			   ((JavascriptExecutor) driver).executeScript("arguments[0].click();", acceptButton);
			   System.out.println("✅ Accept button clicked successfully");
			   WindowHandle.captureErrors(driver, 5);
					
			   WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(10));
			   WebElement submitBtn = wait1.until(ExpectedConditions.visibilityOfElementLocated(By.id("Submit")));
			   ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitBtn);
			  
	               
		try {
			 WindowHandle.ValidationFrame(driver);
		        String  accountId=WindowHandle.getAccountId(driver);
		        if (accountId != null) {
		            System.out.println("✅ Extracted Account ID: " + accountId);
		        } else {
		            System.out.println("❌ Account ID not found!");
		        }

		}catch(Exception e)
		{
			System.out.println("Error Message"+e);
		}
		driver.switchTo().defaultContent();
		driver.switchTo().window(mainwindow);
		LogOut.performLogout(driver, wait);
		}catch(Exception e) {
			System.out.println("Error Message"+e);		}
}
}