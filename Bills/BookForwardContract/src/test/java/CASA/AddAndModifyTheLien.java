package CASA;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import Utilities.DateUtils;
import Utilities.RowData;
import Utilities.WindowHandle;

public class AddAndModifyTheLien {

	 private WebDriver driver;
	 private WebDriverWait wait;

	    public AddAndModifyTheLien(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
	    }
	        
	        public void execute(RowData id ,List<String> li) throws Exception {
	        	JavascriptExecutor js = (JavascriptExecutor) driver;
	        	
			 	driver.switchTo().defaultContent();
		        driver.switchTo().frame("loginFrame");
		        
		    	WebElement searchbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuSelect")));
		        searchbox.click();
		        searchbox.sendKeys(li.get(1));

		       WebElement element4 = driver.findElement(By.id("menuSearcherGo"));
		       element4.click();
		       
		       WindowHandle.handleAlertIfPresent(driver);
		       try {
		           // Switch to required frames
		           wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
		           wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));
		           
		           WebElement Function  = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("funcCode")));
		           Function.sendKeys(li.get(2));
		                    
		           WebElement AcctId = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("acctId")));
		           AcctId.sendKeys(id.getByHeader("Created_AccountID"));
		          // AcctId.sendKeys("003000500000340");
		           
	               WebElement GoButton = driver.findElement(By.xpath("//input[@type='button' and @id='Accept']"));
		           js.executeScript("arguments[0].click();", GoButton);
		           
		           WebElement NewLienAmount = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("newLienAmt")));
		           NewLienAmount.clear();
		           NewLienAmount.sendKeys(li.get(3));

		           WebElement LienReason = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("lienReasonCode")));
		           LienReason.clear();
		           LienReason.sendKeys(li.get(4));
		           
		           WebElement frdate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("effFromDate_ui")));
		           @SuppressWarnings("deprecation")
		           String existingValue = frdate.getAttribute("value").trim();

		           if (!existingValue.isEmpty()) {
		               // If the field already has a value, skip typing
		               System.out.println("From Date already has value: " + existingValue + ". Skipping input.");
		           } else {
		               // Otherwise, send the new value
		               frdate.clear();  // optional: clear any placeholder
		               frdate.sendKeys(li.get(5));
		               System.out.println("From Date was empty. Entered: " + li.get(5));
		           }
		         
		           String date = li.get(6);
		           String formatedate = DateUtils.toDDMMYYYY(date);
		        
		           WebElement exdate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("expDate_ui")));
		           exdate.sendKeys(formatedate);
		           
		           WebElement contanum = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("contactNum")));
		           contanum.clear();
		           contanum.sendKeys(li.get(7));
		           
		           WebElement SubmitButton = driver.findElement(By.id("Submit"));
		           js.executeScript("arguments[0].click();", SubmitButton);
		           
		           WindowHandle.ValidationFrame(driver);
		           
		           WebElement msgElem = wait.until(ExpectedConditions.visibilityOfElementLocated(
		        		    By.xpath("//font[@face='Arial, Helvetica, sans-serif']")
		        		));

		        		String actualMessage = msgElem.getText().trim();
		        		String mode = li.get(2);
		        		String expectedMessage;
		        		if ("M - Modify".equalsIgnoreCase(mode)) {
		        		    expectedMessage = "Record modified successfully.";
		        		} else if ("A - Add".equalsIgnoreCase(mode)) {
		        		    expectedMessage = "Record added successfully.";
		        		} else {
		        		    throw new IllegalArgumentException("Unknown operation mode: " + mode);
		        		}
		        		Assert.assertEquals(
		        		    actualMessage,
		        		    expectedMessage,
		        		    "Unexpected message displayed: expected '" + expectedMessage + "', but found '" + actualMessage + "'"
		        		);

		           
		       }catch(Exception e) {
		    	   System.out.println("Lien Failed:"+e);
		       }
		       
	    }
}
