package Loan;

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

public class LoanClose extends BaseTest{
	protected static WebDriver driver;
    protected static WebDriverWait wait;

    @BeforeClass
    public void setup() throws IOException {
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        String userID = DriverManager.getProperty("userid1");
        String password = DriverManager.getProperty("password1");

        DriverManager.login(userID, password);
        System.out.println("✅ Logged in as: " + userID);
    }
        @Test(dataProvider = "testcase", dataProviderClass = Dataproviders.class)
        @ExcelData(sheetName = "Sheet1", rowIndex = {1,8})
        public void close(RowData data, RowData closeData) {
        	String mainwindow = driver.getWindowHandle();
            System.out.println("MainWindow :" + mainwindow);
        	
        	try {
                WindowHandle.slowDown(3);
                
                WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),closeData.getByIndex(1));
                WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

                wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
                wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));
              
                WebElement   funcCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("funcCode")));
                funcCode.sendKeys(closeData.getByIndex(2));

                WebElement acctID = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("acctId")));
                String AcctID = data.getByHeader("Created_AccountID");
                ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", acctID, AcctID);
              
                WindowHandle.captureErrors(driver, 5);
                
                WebElement goBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("Submit")));
                goBtn.click();
                
                
                ((JavascriptExecutor) driver).executeScript("document.getElementById('Submit').click();");

                WindowHandle.captureErrors(driver, 5);
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
           	driver.switchTo().window(mainwindow);
			 driver.switchTo().defaultContent();
			 LogOut.performLogout(driver, wait);



}catch(Exception e)
            {
	System.out.println("Error Message"+e);
            }
}
}
