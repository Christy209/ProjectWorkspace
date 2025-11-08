package CASA;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import Utilities.RowData;
import Utilities.WindowHandle;

public class VerificationOfStopAndRevokePayment {
	private WebDriver driver;
	 private WebDriverWait wait;

	    public VerificationOfStopAndRevokePayment(WebDriver driver){
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
	    }
	        
	        public void execute(List<String> stop,RowData refno) throws Exception {

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
		            WebElement refNum = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("refNum")));
		            String RefNum = refno.getByHeader("Created_AccountID");
		            refNum.sendKeys(RefNum);
		            
		            WebElement ok = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Accept")));
		        	((JavascriptExecutor) driver).executeScript("arguments[0].click();", ok);
		        	
		        	WebElement payeeName = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payeeName")));
		        	String PayeeName = payeeName.getText().trim();
		        	
		        	WebElement chqAmt = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("chqAmt")));
		        	String ChqAmt = chqAmt.getText().trim();
		        	
		        	WebElement reasonCode = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("reasonCode")));
		        	String ReasonCode = reasonCode.getText().trim();
		        	
		        	 Map<String, String> AppData = new HashMap<>();
		        	 	AppData.put("RefNo", RefNum);
		        	    AppData.put("PayeeName", PayeeName);
		        	    AppData.put("ChqAmt", ChqAmt);
		        	    AppData.put("ReasonCode.", ReasonCode);

		        	    Map<String, String> ExceptedValue = new HashMap<>();
		        	    ExceptedValue.put("RefNo", refno.getByHeader("Created_AccountID"));
		        	    ExceptedValue.put("PayeeName", stop.get(5).toString().trim());
		        	    ExceptedValue.put("ChqAmt.", stop.get(6).toString().trim());
		        	    ExceptedValue.put("ReasonCode", stop.get(8).toString().trim());
		        	
		        	    int matchCount = 0, mismatchCount = 0;
		                StringBuilder mismatchDetails = new StringBuilder();

		                for (String key : ExceptedValue.keySet()) {
		                    String exp = ExceptedValue.get(key);
		                    String act = AppData.get(key);
		                    
		                    if (exp == null || exp.isEmpty() || act == null || act.isEmpty()) {
		                        System.out.printf("⚠️ Skipping '%s' – expected: [%s], actual: [%s]%n", key, exp, act);
		                        continue;
		                    }
		                    if (exp.equals(act)) {
		                        matchCount++;
		                    } else {
		                        mismatchCount++;
		                        mismatchDetails.append(
		                            String.format("Field '%s': expected [%s], actual [%s]%n", key, exp, act)
		                        );
		                    }
		                }
		                // Log summary
		                System.out.printf("✅ Matched: %d, ❌ Mismatches: %d%n", matchCount, mismatchCount);
		                if (mismatchCount > 0) {
		                    System.err.println("Mismatch Details:\n" + mismatchDetails);
		                }
		                // ✅ Only fail if any mismatch found
		                Assert.assertEquals(mismatchCount, 0, 
		                    "🔴 Found " + mismatchCount + " mismatched field(s).");
		                
//		 WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(By.id("Submit")));
//		((JavascriptExecutor) driver).executeScript("arguments[0].click();", submit);
		 WindowHandle.slowDown(1);
		                
		WindowHandle.ValidationFrame(driver);
		String expectedMessage = "";
		WebElement accountElement = wait.until( ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='compField']")
		));
		String actualText = accountElement.getText().trim();
		Assert.assertEquals(actualText, expectedMessage, "❌ Mismatch in Verified Account ID message!");
		            
		       }catch(Exception e) {
		    	   
		    	   }
		       }
}
