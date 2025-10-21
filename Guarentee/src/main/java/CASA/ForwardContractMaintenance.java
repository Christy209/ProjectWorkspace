package CASA;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import Utilities.DateUtils;
import Utilities.ExcelUtils;
import Utilities.RowData;
import Utilities.WindowHandle;

public class ForwardContractMaintenance {
	private WebDriver driver;
	private WebDriverWait wait;
	String excelPath = System.getProperty("user.dir") + "/Resource/FBTCDATA.xlsx";

	    public ForwardContractMaintenance(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
	    }
	    	    
	    public String execute(RowData inputData , String sheetname,int row,String excelpath) throws Exception {
	    	ExcelUtils.loadExcel(excelPath);
	        String mainWindowHandle = driver.getWindowHandle();
	        WindowHandle.slowDown(4);

	        try {
	            // ---- navigate and enter values ----
	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),inputData.getByIndex(1));
	            WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
	            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

	            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
	            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));

	            WindowHandle.selectDropdownIfValuePresent(driver,wait,(By.id("funcCode")),inputData.getByIndex(2));
	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("solId"))),inputData.getByIndex(3));
	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FwdCntrctNo"))),inputData.getByIndex(4));
	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FwdCntrctType"))),inputData.getByIndex(5));
	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cifId"))),inputData.getByIndex(6));
	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ccy"))),inputData.getByIndex(7));

	            WebElement Go = driver.findElement(By.id("Accept"));
	            Go.click();

	            WindowHandle.slowDown(10); // small wait for page update
//	            String accountID = inputData.getByIndex(8);
//	            WebElement acctIdField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("acctId")));
//	            WindowHandle.setValueWithJS(driver, acctIdField, accountID);

//	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FwdCntrctAmt"))),
//	                    inputData.getByIndex(9));
//
//	            // dates
//	            String validfrmDate = DateUtils.toDDMMYYYY(inputData.getByIndex(10));
//	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("validfrmDate_ui"))), validfrmDate);
//
//	            String validtoDate = DateUtils.toDDMMYYYY(inputData.getByIndex(11));
//	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("validtoDate_ui"))), validtoDate);
//
//	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Toccy"))), inputData.getByIndex(12));
//	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("rateCode"))), inputData.getByIndex(13));

	            // Tabs
	            WebElement Limittab = wait.until(ExpectedConditions.elementToBeClickable(By.id("tfclimit")));
	            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Limittab);
	            Limittab.click();

	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("limitIdPrefix"))), inputData.getByIndex(14));
	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("rateCode"))), inputData.getByIndex(15));

	            WebElement Chargetab = wait.until(ExpectedConditions.elementToBeClickable(By.id("tfccharge")));
	            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Chargetab);
	            Chargetab.click();

	            WebElement submit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Submit")));
	            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submit);

	            if (WindowHandle.HandlePopupIfExists(driver)){
	                WebElement accept = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accept")));
	                accept.click();
	                driver.switchTo().window(mainWindowHandle);
	            }

	        } catch (Exception e) {
	            Assert.fail("❌ Forward Contract Menu Error: " + e.getMessage(), e);
	        }

	        return "❌ No success message found";  // fallback
	    }
}

