package CASA;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;

import Utilities.RowData;
import Utilities.WindowHandle;

public class ClosureOfTheAccount {
	  private WebDriver driver;
	    private WebDriverWait wait;

	    public ClosureOfTheAccount(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
	    }
		 public void execute(RowData id , List<String> cl,List<String> cas) throws IOException, Exception {
			 
			 JavascriptExecutor js = (JavascriptExecutor) driver;
        try {
        	
        	 // Switch back to default content before menu search
	        driver.switchTo().defaultContent();
	        driver.switchTo().frame("loginFrame");
	        
        	WebElement searchbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuSelect")));
	        searchbox.click();
	        searchbox.sendKeys(cl.get(1));
	        WebElement element4 = driver.findElement(By.id("menuSearcherGo"));
	        element4.click();
	        WindowHandle.handleAlertIfPresent(driver);
	        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
	        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));
	        
	        WebElement FunCode = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("funcCode")));
	        FunCode.sendKeys(cl.get(2));

            driver.findElement(By.id("acctId")).sendKeys(id.getByHeader("Created_AccountID"));
            
            String type = cl.get(3);
            WebElement checkBox = driver.findElement(By.id("chkbalTransferFlg"));
            js.executeScript("arguments[0].click();", checkBox);
            
            WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("caac.tranType")));
            WindowHandle.selectByVisibleText(dropdown, cl.get(3));
            
            if ("Transfer".equalsIgnoreCase(type)) {

            driver.findElement(By.name("caac.xferAcctId")).sendKeys(cl.get(4));

            }else if( ("Both".equalsIgnoreCase(type))) {
            	
            	driver.findElement(By.name("caac.cashAmt")).sendKeys(cas.get(77));
            	
            	driver.findElement(By.name("caac.xferAcctId")).sendKeys(cl.get(4));
            	
            }else {}
            
            driver.findElement(By.name("caac.rptCode")).sendKeys(cl.get(5));
            
            WebElement go = wait.until(ExpectedConditions.elementToBeClickable(By.id("Submit")));
            js.executeScript("arguments[0].click();", go);
            
            driver.findElement(By.xpath("//a[@id='caaccls']")).click();
            
            if (isLienMessagePresent()) {
                throw new SkipException("Skipped closure: User Liens Exist.");
            }
            
            driver.findElement(By.xpath("//input[@id='acctClsReasonCode']")).sendKeys(cl.get(6));
            
            wait.until(ExpectedConditions.elementToBeClickable(By.id("Submit"))).click();
            
            WindowHandle.ValidationFrame(driver);
            String AcctID = id.getByHeader("Created_AccountID");
            WebElement confirmationMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[contains(text(),'Closed Successfully')]")));       
            String confirmationText = confirmationMsg.getText().trim();
            String expectedMessage = "A/c. ID " + AcctID + " Closed Successfully.";
            Assert.assertTrue(confirmationText.contains(expectedMessage),"Confirmation message does not match!");

        } catch (Exception e) {
            System.out.println("Closure Error : " + e.getMessage());
        }
    }
		 private boolean isLienMessagePresent() {
			    return !driver.findElements(By.xpath("//td[@class='subhdr' and contains(text(),'User Liens Exist')]")).isEmpty();
			}
}
