package CashCreditAccount;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
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
import Utilities.ExcelUtils;
import Utilities.LogOut;
import Utilities.RowData;
import Utilities.Validation;
import Utilities.WindowHandle;

public class VerifyCashCreditAccount extends BaseTest {

	String excelpath = System.getProperty("user.dir") + "/resources/LoanLoan.xlsx";
    int row = 1;
    String sheetname = "Sheet4"; 


	   protected static WebDriver driver;
	    protected static WebDriverWait wait;
	   
	    @BeforeClass
	    public  void setup() throws IOException {
	        driver = DriverManager.getDriver();
	        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

	        String userID = DriverManager.getProperty("userid1");
	        String password = DriverManager.getProperty("password1");

	        DriverManager.login(userID, password);
	        System.out.println("✅ Logged in as: " + userID);
	    }

	@Test(dataProvider = "testcase", dataProviderClass = Dataproviders.class)
	@ExcelData(sheetName = "Sheet4", rowIndex = {1,3})

	public void createTDAccount(RowData verifyData,RowData inputData) throws Exception {
		
		 String mainwindow = driver.getWindowHandle();
		
			 WindowHandle.slowDown(4);
         WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),inputData.getByIndex(1));
         WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
         ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);
 try {
         wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
         wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));
    	
      
        WebElement funCode1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("verifyCancel")));
        funCode1.sendKeys(inputData.getByIndex(2));
        WindowHandle.slowDown(2);
   
          WebElement acctID = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tempForacid")));
          String AcctID = verifyData.getByHeader("Created_AccountID");
          ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", acctID ,AcctID);
         
    	   WebElement go = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='Accept']")));
    	   ((JavascriptExecutor) driver).executeScript("arguments[0].click();", go);
 }catch(Exception e) {
   	 WindowHandle.checkForApplicationErrors(driver);
   }
    	   Map<String, String> appData = getApplicationData(driver, wait);
           int mismatchCount = Validation.validateData(verifyData.getHeaderMap(), appData);

           if (mismatchCount == 0) {
              // System.out.println("✅ No mismatches found. Proceeding to handle popup...");

        	   try {
              	 Thread.sleep(2000); // allow popup to appear

           	    String mainWindow = driver.getWindowHandle();
           	    Set<String> allWindows = driver.getWindowHandles();

           	    if (allWindows.size() > 1) {
           	        System.out.println("ℹ️ Popup detected after submit.");

           	        for (String handle : allWindows) {
           	            if (!handle.equals(mainWindow)) {
           	                driver.switchTo().window(handle);
           	                break;
           	            }
           	        }

           	        try {
           	            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
           	            WebElement accept = shortWait.until(ExpectedConditions.elementToBeClickable(By.id("accept")));
           	            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", accept);
           	            System.out.println("✅ Accept clicked successfully (Selenium/JS).");
           	        } catch (Exception inner) {
           	            System.out.println("⚠ Selenium could not click Accept, trying Robot...");
           	            Robot robot = new Robot();
           	            robot.setAutoDelay(500);
           	            robot.keyPress(KeyEvent.VK_TAB);
           	            robot.keyRelease(KeyEvent.VK_TAB);
           	            robot.keyPress(KeyEvent.VK_ENTER);
           	            robot.keyRelease(KeyEvent.VK_ENTER);
           	            System.out.println("✅ Accept clicked via Robot.");
           	        }

           	        // Optional: close popup if still open
           	        try {
           	            driver.close();
           	        } catch (Exception ignored) {}

           	    } else {
           	        System.out.println("ℹ️ No popup detected, continuing...");
           	    }

           	    // Switch back to main window
           	    driver.switchTo().window(mainWindow);
           	    System.out.println("✅ Back to main window.");

           	} catch (Exception e) {
           	    System.out.println("❌ Error during Submit: " + e.getMessage());
           	}


          WindowHandle.ValidationFrame(driver);
          String successLog = WindowHandle.checkForSuccessElements(driver);
          if(successLog != null){
              System.out.println("✅ Test Success");
          }
   			 driver.switchTo().window(mainwindow);
   			 driver.switchTo().defaultContent();
   				LogOut.performLogout(driver, wait);
   			 
           }     

       }

      private static Map<String, String> getApplicationData(WebDriver driver, WebDriverWait wait2) throws Exception {
          Map<String, String> appData = new HashMap<>();
	    @SuppressWarnings("unused")
		String mainWindowHandle = driver.getWindowHandle();
    		    
        //////////////////////////////////////General tab/////////////////////////
    		    
        
	    appData.put("modeOfOperCode", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='modeOfOperCode']","xpath"));
	    appData.put("locationCode", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='locationCode']","xpath"));
       
	    WindowHandle.slowDown(2);
        ((JavascriptExecutor) driver).executeScript("document.getElementById('miscodes').click();");
        appData.put("sectCode", ExcelUtils.getTextOrValue(driver, wait, "sectCode", "id"));
        appData.put("subSectCode", ExcelUtils.getTextOrValue(driver, wait, "subSectCode", "id"));
        appData.put("occCode", ExcelUtils.getTextOrValue(driver, wait, "occCode", "id"));
        appData.put("modeAdv", ExcelUtils.getTextOrValue(driver, wait, "modeAdv", "id"));
        
        WindowHandle.slowDown(2);
        ((JavascriptExecutor) driver).executeScript("document.getElementById('documentdetails').click();");
      
        appData.put("docScanFlg", ExcelUtils.getTextOrValue(driver, wait, "docScanFlg", "id"));
        
        WindowHandle.slowDown(2);
        ((JavascriptExecutor) driver).executeScript("document.getElementById('relatedpartydetails').click();");   
        appData.put("custTitle", ExcelUtils.getTextOrValue(driver, wait, "custTitle", "id"));
        appData.put("custName", ExcelUtils.getTextOrValue(driver, wait, "custName", "id"));
        appData.put("custAddrLine1", ExcelUtils.getTextOrValue(driver, wait, "custAddrLine1", "id"));
        
        WindowHandle.slowDown(2);
        ((JavascriptExecutor) driver).executeScript("document.getElementById('acctlmt').click();");
        appData.put("expiryDate_ui", ExcelUtils.getTextOrValue(driver, wait, "expiryDate_ui", "id"));
        appData.put("documentDate_ui", ExcelUtils.getTextOrValue(driver, wait, "documentDate_ui", "id"));
        appData.put("drawingPowerInd", ExcelUtils.getTextOrValue(driver, wait, "drawingPowerInd", "id"));


        WindowHandle.slowDown(2);
         ((JavascriptExecutor) driver).executeScript("document.getElementById('generaldetails2').click();");
         
                 WindowHandle.slowDown(2);
		 WebElement tdsch = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sbschemedetails")));
		 ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tdsch);
		 appData.put("acctHlthCode", ExcelUtils.getTextOrValue(driver, wait, "acctHlthCode", "id"));
		 try {
	        	
	        	
			  WebElement submit1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Submit")));
               ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submit1);
               
               WindowHandle.popupop(driver, wait, By.id("accept"));
               String  accountId=WindowHandle.getAccountId(driver);
               System.out.println("Account id"+accountId);
               
	        }catch(Exception e) {
              	 WindowHandle.checkForApplicationErrors(driver);
              }
	        
		
		return appData;

      }
}
        