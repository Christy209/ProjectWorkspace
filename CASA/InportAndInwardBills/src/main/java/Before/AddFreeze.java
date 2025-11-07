package Before;

	import java.io.IOException;
    import java.time.Duration;
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
		    String sheetname = "Sheet1"; 
		    
	@BeforeClass
	public void setup() throws IOException {
		driver = DriverManager.getDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		String userid = DriverManager.getProperty("userid");
		String password = DriverManager.getProperty("password");
		DriverManager.login(userid, password);
			}
	 @Test(dataProvider = "testcase", dataProviderClass = Dataproviders.class)
	    @ExcelData(sheetName ="Sheet1",rowIndex= {1,8})
	    public void Addfreeze(RowData inputData,RowData id) throws Exception {
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
		           //WindowHandle.selectByVisibleText(funCode,"F - Freeze");

		            // ✅ Enter Details
		            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("acctId"))).sendKeys(inputData.getByHeader("Created_AccountID"));
		            //wait.until(ExpectedConditions.presenceOfElementLocated(By.id("acctId"))).sendKeys("003000500000146");

		            // ✅ Select Radio Button
		            String FunCode =id.getByIndex(2);
		            
		            if("F - Freeze".equalsIgnoreCase(FunCode)) {
		            String freezeType = id.getByIndex(3);
		            
		            try {
		                WebElement freezeRadio = wait.until(ExpectedConditions.elementToBeClickable(
		                    By.xpath("//input[@id='freezeCode' and @value='" + freezeType + "']")));
		                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", freezeRadio);
		                
		                // ✅ Enter Freeze Reason
			            wait.until(ExpectedConditions.presenceOfElementLocated(By.name("afsm.freezeReason"))).sendKeys(id.getByIndex(4));

		            } catch (Exception e) {
		                System.out.println("❌ Failed to select freeze type [" + freezeType + "]: " + e.getMessage());
		            }
		            } else {
		                System.out.println("ℹ️ Skipping freeze radio selection since FunCode != 'Freeze' (actual: " + FunCode + ")");
		            }
		            
		            WebElement go = wait.until(ExpectedConditions.elementToBeClickable(
		                    By.id("Accept")));
		                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", go);
		                
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
		    	    String FunCode = id.getByIndex(2); // Some code for function

		    	    // Switch to validation frame
		    	    WindowHandle.ValidationFrame(driver);

		    	    // Wait and get the message label
		    	    WebElement msgLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='compField']")));
		    	    String actualMessage = msgLabel.getText().trim();

		    	    // Check if message is empty
		    	    Assert.assertFalse(actualMessage == null || actualMessage.isEmpty(), "Test Failed: Account Number label text is empty!");
		    	    System.out.println("SB Account Created Successfully..! AcctNo: " + actualMessage);

		    	    // Update Excel with the actual message
		    	    String excelfilePath = excelpath; // Path to your Excel file
		    	    String sheetName = sheetname; // Sheet name
		    	    int rowNum = row;  // Row number
		    	    String columnName = "Created_AccountID"; // Column where message should be stored
		    	    ExcelUtils.updateExcel(excelfilePath, sheetName, rowNum, columnName, actualMessage);

		    	    // Validation of freeze message
		    	    String expectedMessage = "Total Freeze on Selected A/cs placed successfully.";
		    	    if("F - Freeze".equalsIgnoreCase(FunCode)) {
		    	        Assert.assertEquals(actualMessage, expectedMessage, "❌ Freeze message validation failed!");
		    	        System.out.println("✅ Message validated: " + actualMessage);
		    	    } else {
		    	        Assert.assertTrue(
		    	            actualMessage.contains("Total Freeze on Selected A/cs Removed successfully."),
		    	            "❌ Expected operation success message was not found! Actual: " + actualMessage
		    	        );
		    	    }

		    	} catch (Exception e) {
		    	    System.out.println("Result msg Error: " + e.getMessage());
		    	}
		   	driver.switchTo().defaultContent();
			LogOut.performLogout(driver, wait);
		              
	}
		    }
