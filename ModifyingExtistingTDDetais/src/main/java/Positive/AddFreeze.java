package Positive;

	import java.io.FileInputStream;
import java.io.IOException;
    import java.time.Duration;

    import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
	import org.openqa.selenium.JavascriptExecutor;
	import org.openqa.selenium.Keys;
	import org.openqa.selenium.WebDriver;
	import org.openqa.selenium.WebElement;
	import org.openqa.selenium.interactions.Actions;
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
	import Utilities.WindowHandle;
	
	public class AddFreeze extends BaseTest {

		 private WebDriver driver;
		 private WebDriverWait wait;
		 
		 String excelpath = System.getProperty("user.dir") + "/resources/TestData.xlsx";
		    int row = 1;
		    String sheetname = "Sheet5"; 
		    
	@BeforeClass
	public void setup() throws IOException {
		driver = DriverManager.getDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		String userid = DriverManager.getProperty("userid");
		String password = DriverManager.getProperty("password");
		DriverManager.login(userid, password);
			}
	 @Test(dataProvider = "testcase", dataProviderClass = Dataproviders.class)
	    @ExcelData(sheetName ="Sheet5",rowIndex= {1})
	    public void Addfreeze(RowData id) throws Exception {
		       WindowHandle.handleAlertIfPresent(driver);
		       
		   	WindowHandle.slowDown(4);
	    	
	    	WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),id.getByIndex(1));

	    	WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
	    	((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

		       try {
		           // Switch to required frames
		           wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
		           wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));

		           WebElement funCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("afsm.funcCode")));
		           WindowHandle.selectByVisibleText(funCode,id.getByIndex(2));
		         
		          
		         
		           WebElement acctID = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("acctId")));
		           String AcctID = id.getByIndex(3);
		           ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", acctID, AcctID);

		            // ✅ Select Radio Button
		            //String FunCode =id.getByIndex(2);
		            
//		            if("F - Freeze".equalsIgnoreCase(FunCode)) {
//		            String freezeType = id.getByIndex(3);
		            
//		            try {
//		                WebElement freezeRadio = wait.until(ExpectedConditions.elementToBeClickable(
//		                    By.xpath("//input[@id='freezeCode' and @value='" + freezeType + "']")));
//		                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", freezeRadio);
//		                
//		                // ✅ Enter Freeze Reason
//			            wait.until(ExpectedConditions.presenceOfElementLocated(By.name("afsm.freezeReason"))).sendKeys(id.getByIndex(4));
//
//		            } catch (Exception e) {
//		                System.out.println("❌ Failed to select freeze type [" + freezeType + "]: " + e.getMessage());
//		            }
//		            } else {
//		                System.out.println("ℹ️ Skipping freeze radio selection since FunCode != 'Freeze' (actual: " + FunCode + ")");

		            
		            WebElement freezeCode = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("freezeCode")));

		            JavascriptExecutor js = (JavascriptExecutor) driver;

		            // Enable the radio button if it's disabled
		            js.executeScript("arguments[0].removeAttribute('disabled');", freezeCode);

		            // Click it using JavaScript
		            js.executeScript("arguments[0].click();", freezeCode);

		            System.out.println("✅ 'freezeCode' radio button clicked successfully via JS.");


			           WebElement freezeReason = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("freezeReason")));
			           String freezeReason1 = id.getByIndex(4);
			           ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", freezeReason, freezeReason1);

		            WebElement go = wait.until(ExpectedConditions.elementToBeClickable(
		                    By.id("Accept")));
		                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", go);
		                WindowHandle.handleAlertIfPresent(driver);
		                
		           // driver.findElement(By.id("Accept")).click();
		            // ✅ Checkbox & Submit
		            WebElement checkBox = driver.findElement(By.xpath("//input[@type='checkbox' and @value='00001']"));
		            checkBox.click();

		            Actions actions = new Actions(driver);
		            actions.sendKeys(Keys.PAGE_DOWN).perform();

		            WindowHandle.slowDown(1);
		            WebElement Submit = driver.findElement(By.xpath("//input[@type='button' and @value='Submit']"));
		            Submit.click();

		    } catch (Exception e) {
		       	System.out.println("Freeze Error: " + e.getMessage());
		       }
		       
		       try {


		    	    // Switch to validation frame
		    	    WindowHandle.ValidationFrame(driver);
		    	    String successLog = WindowHandle.checkForSuccessElements(driver);
	                if(successLog != null){
	                    System.out.println("✅ Test Success");
	                }

		    	} catch (Exception e) {
		    	    System.out.println("Result msg Error: " + e.getMessage());
		    	}
		   	driver.switchTo().defaultContent();
			LogOut.performLogout(driver, wait);
		              
	}
	}