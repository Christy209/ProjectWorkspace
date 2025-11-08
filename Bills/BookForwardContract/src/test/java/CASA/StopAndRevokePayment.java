package CASA;

import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import Utilities.ExcelUtils;
import Utilities.RowData;
import Utilities.WindowHandle;

public class StopAndRevokePayment {
	private WebDriver driver;
	 private WebDriverWait wait;

	    public StopAndRevokePayment(WebDriver driver){
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
	    }
	        
	        public void execute(RowData id ,List<String> stop,String excelpath ,String sheetname , int row) throws Exception {

	        	driver.switchTo().defaultContent();
		        driver.switchTo().frame("loginFrame");
		        
		    	WebElement searchbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuSelect")));
		        searchbox.sendKeys(stop.get(1));
		        
		       WebElement element4 = driver.findElement(By.id("menuSearcherGo"));
		       element4.click();
		       
		       WindowHandle.handleAlertIfPresent(driver);
		       try {
		           // Switch to required frames
		           wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
		           wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));
		           
		            // Select Function Code
		        	WebElement FunCode = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("funcCode")));
			        FunCode.sendKeys(stop.get(2));

		            // Enter Account Number
		            WebElement account = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("acctId")));
		            String foracid = id.getByHeader("Created_AccountID");
		            account.clear();
		            account.sendKeys(foracid);
		            
		        	WebElement beginChqNum = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("beginChqNum")));
		        	beginChqNum.sendKeys(stop.get(3));
			        
		        	WebElement noOfLvs = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("noOfLvs")));
		        	noOfLvs.sendKeys(stop.get(4));
			        
//		        	WebElement fromAcceptDate = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("fromAcceptDate_ui")));
//		        	fromAcceptDate.sendKeys(stop.get(5));
//			        
//		        	WebElement toAcceptDate = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("toAcceptDate_ui")));
//		        	toAcceptDate.sendKeys(stop.get(6));
		        	
		        	WebElement ok = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Accept")));
		        	((JavascriptExecutor) driver).executeScript("arguments[0].click();", ok);
		        	
		        	WebElement payeeName = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payeeName")));
		        	payeeName.sendKeys(stop.get(7));
		        	
		        	WebElement Submit = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Submit")));
		        	Submit.click();
		        	
		  WindowHandle.ValidationFrame(driver);
		            
		  WebElement label = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[@id='compField']")));
		  String actualMessage = label.getText().trim();
		  String expectedMessage = " The stop payment is marked successfully. Stop Payment"
		  		+ " Ref. No : 387 Cheque No. : " + stop.get(3) + " No. of Leaves : " + stop.get(4);
		  
		  Pattern refPattern = Pattern.compile("Ref\\. No\\s*:?\\s*(\\d+)");
		  Matcher refMatcher = refPattern.matcher(actualMessage);

		  String refNo = "";
		  if (refMatcher.find()) {
		      refNo = refMatcher.group(1);
		      System.out.println("✅ Extracted Ref No: " + refNo);
		  } else {
		      System.err.println("⚠️ Unable to extract Ref No from message: " + actualMessage);
		  }

		  Assert.assertEquals(actualMessage, expectedMessage,"Failed: message was not found! Actual:" + actualMessage);
		  
		  String excelfilePath = excelpath; // Path to your Excel file
	      String sheetName = sheetname; // Update with your sheet name
	      int rowNum = row;  // Row where you want to store
	      String columnName = "Created_AccountID";   
	      ExcelUtils.updateExcel(excelfilePath, sheetName,rowNum, columnName, refNo);

		       }catch(Exception e){
		    	   
		    	   System.out.println("Error In StopAndRevoke Payment :" + e);
		    	   }
		       }
}
