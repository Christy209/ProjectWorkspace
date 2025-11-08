package CASA;

import java.time.Duration;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import Utilities.ExcelUtils;
import Utilities.RowData;
import Utilities.SharedContext;

public class TransactionInquire {

	  private WebDriver driver;
	    private WebDriverWait wait;

	    public TransactionInquire(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
	    }
	  public void execute ( RowData id) throws Exception {
		  	JavascriptExecutor js = (JavascriptExecutor) driver;
		  		
	  try {
		 	driver.switchTo().defaultContent();
	        driver.switchTo().frame("loginFrame");
	        
	        WebElement searchbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuSelect")));
	    	searchbox.click();
	    	searchbox.sendKeys("HACLI");
	    	
	    	WebElement element4 = driver.findElement(By.id("menuSearcherGo"));
	    	element4.click();
	    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
	    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));
	    	        

	           WebElement AccountId = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("account_No")));
	           AccountId.sendKeys(id.getByHeader("Created_AccountID"));
	                    
	           WebElement FromDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("date_from_ui")));
	           FromDate.sendKeys("");
	           
	           WebElement ToDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("date_to_ui")));
	           ToDate.sendKeys("");
	           
	           WebElement GoButton = driver.findElement(By.xpath("//input[@type='button' and @id='Submit']"));
	           js.executeScript("arguments[0].click();", GoButton);
	           
	           try {
	        	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        	    wait.until(ExpectedConditions.alertIsPresent());

	        	    Alert alert = driver.switchTo().alert();
	        	    String alertText = alert.getText();
	        	    System.out.println("✅ Alert received: " + alertText);

	        	    // Store it for later use in the main test case
	        	    SharedContext.alertMessage = alertText;

	        	    alert.accept(); // Accept the alert
	        	} catch (NoAlertPresentException e) {
	        	    System.err.println("❌ No alert was present.");
	        	    SharedContext.alertMessage = null;
	        	}

	           
	           WebElement AvailableAmt = driver.findElement(By.id("avail_amt10"));
	           String balanceText = AvailableAmt.getText();
	           System.out.println(balanceText);
	           
	           String excelFilePath = System.getProperty("user.dir") + "/Resource/TestData.xlsx";
	  	        String sheetName = "Sheet1";
	  	        int targetRow = 4;        
	  	        int targetColumn = 74; 

	  	        // Write Account ID to Excel
	  	        try {
	  	        	ExcelUtils.setCellData(excelFilePath, sheetName, targetRow, targetColumn, balanceText);


			   	   } catch (Exception e) {
			   	       System.out.println("❌ Popup handling failed: " + e.getMessage());
			   	   }
	  	        
	           
	           WebElement OkButton = driver.findElement(By.xpath("//input[@type='button' and @id='Ok']"));
	           js.executeScript("arguments[0].click();", OkButton);
	           
	           
	   	   } catch (Exception e) {
	   	       System.out.println("❌ Popup handling failed: " + e.getMessage());
	   	   }
		  
		  
	}
}
