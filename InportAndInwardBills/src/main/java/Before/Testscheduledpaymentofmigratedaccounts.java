package Before;

import java.io.IOException;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Annotation.ExcelData;
import Base.BaseTest;
import Base.DriverManager;
import Utilities.Dataproviders;
import Utilities.ExcelUtils2;
import Utilities.LogOut;
import Utilities.RowData;
import Utilities.WindowHandle;

public class Testscheduledpaymentofmigratedaccounts extends BaseTest {
	
	 String excelpath = System.getProperty("user.dir") + "/resources/LoanLoan.xlsx";
	    int row = 12;
	    String sheetname = "Sheet2"; 
	    
	@BeforeClass
    public  void setup() throws IOException {
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        String userID = DriverManager.getProperty("userid");
        String password = DriverManager.getProperty("password");
        DriverManager.login(userID, password);
    }

	@Test(dataProvider = "testcase", dataProviderClass = Dataproviders.class)
    @ExcelData(sheetName = "Sheet2", rowIndex = {12})
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
            try {
            WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pymtType"))),rowData.getByIndex(2));
            WindowHandle.safeClick(driver, wait, By.id("Accept"));
            }catch(Exception e) {           
            WindowHandle.checkForApplicationErrors(driver);
            }
            try {
            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("loanAcctId"))),rowData.getByIndex(3));
            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("crAmt"))),rowData.getByIndex(4));            
            WindowHandle.safeClick(driver, wait, By.id("Accept"));
            }catch(Exception e) {
            WindowHandle.checkForApplicationErrors(driver);
            }
            WindowHandle.safeClick(driver, wait, By.id("Submit"));
            
//            WebElement innerTable = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.innertable")));
//            String tableText = innerTable.getText().trim();
//            System.out.println(tableText);
            String successLog = WindowHandle.checkForSuccessElements(driver);
            if(successLog != null){
                System.out.println("✅ Test Success");
            }
            try {
            WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='tranId']")));
  	      String labelText = label.getText(); // Get the text of the label

  	       Assert.assertFalse(labelText == null || labelText.isEmpty(), "Test Failed:  label text is empty!");
  	      
          String excelfilePath = excelpath;
          String sheetName = sheetname;
          int rowNum = row;
          String columnName = "Tran_Id";

          ExcelUtils2.updateExcel(excelfilePath, sheetName, rowNum, columnName, labelText);
            }catch(Exception e) {
            	System.out.println("Unable to find Tranid:"+e);
            }
            
            driver.switchTo().defaultContent();
			 LogOut.performLogout(driver, wait);



            
	}
}
