package CASA;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import Utilities.RowData;
import Utilities.WindowHandle;

public class AddFreeze {

	 private WebDriver driver;
	 private WebDriverWait wait;

	    public AddFreeze(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
	    }
	    public void execute(RowData id ,List<String> fr) throws Exception {
	    	
		 	driver.switchTo().defaultContent();
	        driver.switchTo().frame("loginFrame");
	        
	    	WebElement searchbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuSelect")));
	        searchbox.click();
	        searchbox.sendKeys(fr.get(1));

	       WebElement element4 = driver.findElement(By.id("menuSearcherGo"));
	       element4.click();
	       
	       WindowHandle.handleAlertIfPresent(driver);
	       try {
	           // Switch to required frames
	           wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
	           wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));

	           WebElement funCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("afsm.funcCode")));
	           WindowHandle.selectByVisibleText(funCode,fr.get(2));
	           //WindowHandle.selectByVisibleText(funCode,"F - Freeze");

	            // ✅ Enter Details
	            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("acctId"))).sendKeys(id.getByHeader("Created_AccountID"));
	            //wait.until(ExpectedConditions.presenceOfElementLocated(By.id("acctId"))).sendKeys("003000500000146");

	            // ✅ Select Radio Button
	            String FunCode = fr.get(2);
	            
	            if("F - Freeze".equalsIgnoreCase(FunCode)) {
	            String freezeType = fr.get(3);
	            
	            try {
	                WebElement freezeRadio = wait.until(ExpectedConditions.elementToBeClickable(
	                    By.xpath("//input[@id='freezeCode' and @value='" + freezeType + "']")));
	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", freezeRadio);
	                
	                // ✅ Enter Freeze Reason
		            wait.until(ExpectedConditions.presenceOfElementLocated(By.name("afsm.freezeReason"))).sendKeys(fr.get(4));

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
	    	   	String FunCode = fr.get(2);
	            WindowHandle.ValidationFrame(driver);
	            WebElement msgLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='compField']")));
	            String actualMessage = msgLabel.getText().trim();
	            String expectedMessage = "Total Freeze on Selected A/cs placed successfully.";
	            if("F - Freeze".equalsIgnoreCase(FunCode)) {
	            Assert.assertEquals(actualMessage, expectedMessage, "❌ Freeze message validation failed!");
	            System.out.println("✅ Message validated: " + actualMessage);
	            }else {
	            	Assert.assertTrue(
	            	        actualMessage.contains("Total Freeze on Selected A/cs Removed successfully."),
	            	        "❌ Expected operation success message was not found! Actual: " + actualMessage);
	            }
	            
	       	    } catch (Exception e) {
		       	System.out.println("Result msg Error: " + e.getMessage());
		       }
}}
