package Positive;

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
import Utilities.RowData;
import Utilities.WindowHandle;

public class Withoutfundingdotransactiontotheaccount extends BaseTest {
	
	 @BeforeClass
	    public void setup() throws IOException {
	        driver = DriverManager.getDriver();
	        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
	        String userid = DriverManager.getProperty("userid");
	        String password = DriverManager.getProperty("password");
	        DriverManager.login(userid, password);
	    }

	    @Test(dataProvider = "testcase", dataProviderClass = Dataproviders.class)
	    @ExcelData(sheetName = "Sheet4", rowIndex = {1, 7})
	    public void  FUNDING(RowData inputData, RowData id) throws Exception {
	        WindowHandle.handleAlertIfPresent(driver);
	        WindowHandle.slowDown(4);

	        // Navigate to menu
	        WebElement menuSelect = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect")));
	        WindowHandle.setValueWithJS(driver, menuSelect, id.getByIndex(1));

	        WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
	        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

	        try {
	            // Switch to required frames
	            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
	            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));

	            // Fill function code and transaction type
	            WebElement funCode = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("funcCode")));
	            funCode.sendKeys(id.getByIndex(2));

	            WebElement transactionType = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tranTypeSubType")));
	            transactionType.sendKeys(id.getByIndex(3));

	            // Click Go
	            driver.findElement(By.id("Go")).click();
	        } catch (Exception e) {
	        	WindowHandle.checkForApplicationErrors(driver);
	        }

	        try {
	            WindowHandle.slowDown(1);

	            WebElement acctID = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("acctId")));
	            String AcctID = inputData.getByHeader("Created_AccountID");
	            ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", acctID, AcctID);
	            
//	            String accountIdFromSheet1 = getCellDataByColumnName(excelpath, "Sheet1", 1, "Created_AccountID");
	//
//		           // Log for clarity
//		           System.out.println("🔹 Account ID fetched from Sheet1: " + accountIdFromSheet1);
	//
//		           // ✅ Enter into field
//		           wait.until(ExpectedConditions.presenceOfElementLocated(By.id("acctId"))).sendKeys(accountIdFromSheet1);

	            
	            WebElement currency = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("refCrncy")));
	            currency.sendKeys(id.getByIndex(4));

	            WindowHandle.slowDown(1);

	            WebElement amount = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("refAmt")));
	            amount.sendKeys(id.getByIndex(5));

	            WebElement tranParticularCode = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tranParticularsCode")));
	            tranParticularCode.sendKeys(id.getByIndex(6));

	            boolean isDenomRequired = Boolean.parseBoolean(id.getByIndex(7)); // Example: from Excel
	            if (isDenomRequired) {
	            WebElement denomButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("DENOMDTLS")));
	            denomButton.click();

	            WindowHandle.slowDown(1);

	            WebElement denomCount = wait.until(ExpectedConditions.elementToBeClickable(By.id("arrDenomCount")));
	            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", denomCount);
	            denomCount.clear();
	            denomCount.sendKeys(id.getByIndex(8));

	            WebElement okButton = driver.findElement(By.xpath("//input[@type='button' and @id='OK']"));
	            okButton.click();
	            }
	            WindowHandle.slowDown(1);

	            WebElement post = driver.findElement(By.id("Post"));
	            post.click();
	            
	            WindowHandle.handlePopupIfExists(driver);

	            // Handle Accept popup if exists
	            try {
	                WebElement acceptButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Accept")));
	                if (acceptButton.isDisplayed() && acceptButton.isEnabled()) {
	                    acceptButton.click();
	                    System.out.println("Clicked Accept button.");
	                }
	            } catch (Exception e) {
	                System.out.println("Accept button not present, may have been auto-handled.");
	            }

	            WindowHandle.ValidationFrame(driver);

	            String successLog = WindowHandle.checkForSuccessElements(driver);
	            if(successLog != null){
	                System.out.println("✅ Test Success");
	                
	            }

	      
	            }catch(Exception e) {
	            	WindowHandle.checkForApplicationErrors(driver);
	            	 System.out.println("error Message"+e);
	            	
	            }
	    }
	    
	    }


