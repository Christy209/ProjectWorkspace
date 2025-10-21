package Positive;

import java.io.IOException;
import java.time.Duration;

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
import Utilities.ExcelUtils2;
import Utilities.LogOut;
import Utilities.RowData;
import Utilities.WindowHandle;

public class ModifyHIMC extends BaseTest {
	protected static WebDriver driver = DriverManager.getDriver();
    protected static WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

//		 String excelpath = System.getProperty("user.dir") + "/resources/LoanLoan.xlsx";
//		    int row = 1;
//		    String sheetname = "Sheet12"; 
//
//	@BeforeClass
//	public void setup() throws IOException {
//		driver = DriverManager.getDriver();
//		wait = new WebDriverWait(driver, Duration.ofSeconds(30));
//		String userid = DriverManager.getProperty("userid");
//		String password = DriverManager.getProperty("password");
//		DriverManager.login(userid, password);
//			}
	@Test(dataProvider = "testcase", dataProviderClass = Dataproviders.class)
	   @ExcelData(sheetName ="Sheet12",rowIndex= {1,5})
	   public void Addfreeze(RowData input,RowData id ) throws Exception {
		
		 driver.switchTo().defaultContent(); 
		  driver.switchTo().frame("loginFrame");
		 WindowHandle.slowDown(6);
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	   	
	   	WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),id.getByIndex(1));

	   	WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
	   	((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

		       try {
		           // Switch to required frames
		           wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
		           wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));

		           WebElement funCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("funcCode")));
		           WindowHandle.selectByVisibleText(funCode,id.getByIndex(2));
		           
		           driver.findElement(By.id("Go")).click();
	}catch(Exception e) {
		  WindowHandle.checkForApplicationErrors(driver);
	}
		       try {
//		    	   WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("invtLocnClassFrom"))),id.getByIndex(3));
//		           WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("invtLocnCodeFrom"))),id.getByIndex(4));
//		           WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("invtLocnClassTo"))),id.getByIndex(5));
//		           WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("invtLocnCodeTo"))),id.getByIndex(6));
	           driver.findElement(By.id("Accept")).click();
		       }catch(Exception e) {
		    		  WindowHandle.checkForApplicationErrors(driver);
		       }
		       
		       try {
		    	   WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("invtClass"))),id.getByIndex(7));
		           WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("invtType"))),id.getByIndex(8));
		           WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("invtSrlAlpha"))),id.getByIndex(9));
		           WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("invtBeginSrlNum"))),id.getByIndex(10));
		           WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("invtEndSrlNum"))),id.getByIndex(11));
		           WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("invtQty"))),id.getByIndex(12));
	           driver.findElement(By.id("Submit")).click();
		       }catch(Exception e) {
		    		  WindowHandle.checkForApplicationErrors(driver);
		       }
		       try {WindowHandle.ValidationFrame(driver);

	           String successLog = WindowHandle.checkForSuccessElements(driver);
	           if(successLog != null){
	               System.out.println("✅ Test Success");
	           }
//	           WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='compField']")));
//	 	      String labelText = label.getText(); // Get the text of the label
//
//	 	       Assert.assertFalse(labelText == null || labelText.isEmpty(), "Test Failed: Account Number label text is empty!");
//	 	      
//	         String excelfilePath = excelpath;
//	         String sheetName = sheetname;
//	         int rowNum = row;
//	         String columnName = "Tran_Id";
//
//	         ExcelUtils2.updateExcel(excelfilePath, sheetName, rowNum, columnName, labelText);

	      }catch(Exception e) {
	   	   System.out.println("error message"+e);
	      }
	 	 driver.switchTo().defaultContent();
		 LogOut.performLogout(driver, wait);
	}

	}
