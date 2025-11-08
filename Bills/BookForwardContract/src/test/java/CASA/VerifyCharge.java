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

import Utilities.RowData;
import Utilities.WindowHandle;

public class VerifyCharge {
	
	  private WebDriver driver;
	    private WebDriverWait wait;

	    public VerifyCharge(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
	    }
	    public void execute(RowData accountData, List<String> vr) throws Exception {
	    		String mainWindow = driver.getWindowHandle();
	            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuSelect")));
	            searchBox.click();
	            searchBox.sendKeys(vr.get(1));
	            driver.findElement(By.id("menuSearcherGo")).click();

	            WindowHandle.ValidationFrame(driver);
	        	try {
			        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("funcCode"))).sendKeys("V - Verify");

			        String id = accountData.getByHeader("Created_AccountID");
			        WebElement acctField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("acctId")));
			        acctField.sendKeys(id);
			        //acctField.sendKeys("003000500000125");
//					
			        WebElement feeDrAcctId = driver.findElement(By.id("drAcctId"));
			        JavascriptExecutor js = (JavascriptExecutor) driver;
			        js.executeScript("arguments[0].value='003000500000125';", feeDrAcctId);
		
			        WebElement goButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("Go")));
			        js.executeScript("arguments[0].click();", goButton);
			       
				} catch (Exception e) {
					
				}
	        	wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Submit"))).click();
	        	
	        	WindowHandle.handlePopupIfExists(driver);
	        	wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accept"))).click();
	        	driver.switchTo().window(mainWindow);
	        	
	        	WindowHandle.ValidationFrame(driver);
	        	String AcctID = accountData.getByHeader("Created_AccountID");
	        	WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("label#compField")));
	        	String labelText = label.getText().trim();
	        	Assert.assertFalse(label == null ||labelText.isEmpty(),"Test Failed: Closure Charges message is empty!");
	        	Assert.assertTrue(labelText.contains(AcctID),"Test Failed: Closure Charges message does not contain account IDs!");
	        	String keyPhrase = "Verification of A/c. Closure Charges";
	        	String closingConfirmation = "Done successfully";
	        	Assert.assertTrue(labelText.contains(keyPhrase) && labelText.contains(closingConfirmation),
	        			"Test Failed: Closure Charges message does not reflect successful closure!");       
	    }


}
