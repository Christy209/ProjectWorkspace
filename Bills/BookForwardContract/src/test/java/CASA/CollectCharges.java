package CASA;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import Utilities.RowData;
import Utilities.WindowHandle;

public class CollectCharges {
	  private WebDriver driver;
	    private WebDriverWait wait;

	    public CollectCharges(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
	    }
    public void execute(RowData accountData, List<String> charg) throws Exception {
        try {
		 	driver.switchTo().defaultContent();
	        driver.switchTo().frame("loginFrame");
	        
        	WindowHandle.handleAlertIfPresent(driver);
        	
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuSelect")));
            searchBox.click();
            searchBox.sendKeys(charg.get(1));
            
            driver.findElement(By.id("menuSearcherGo")).click();
            WindowHandle.handleAlertIfPresent(driver);
            
        } catch (Exception e) {
            throw new RuntimeException("Menu selection failed: " + e.getMessage(), e);
        }

        WindowHandle.ValidationFrame(driver);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("funcCode")))
            .sendKeys(charg.get(2));

        WebElement acctField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("acctId")));
        acctField.sendKeys(accountData.getByHeader("Created_AccountID"));
       //cctField.sendKeys("003000500000269");
		
		WebElement drAcctId = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("drAcctId")));
		drAcctId.sendKeys(accountData.getByHeader("Created_AccountID"));

        WebElement goButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("Go")));
        goButton.click();
		
		WebElement submit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Submit")));
 		submit.click();
 		
 		WindowHandle.ValidationFrame(driver);
 		WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("label#compField")));	    
 		Assert.assertFalse(label == null ||label.getText().trim().isEmpty(),"Test Failed: Closure Charges message is empty!");
 		Assert.assertTrue(label.getText().contains("Closure Charges Collected"),"Test Failed: Closure Charges message is not as expected!");	    

    }
}
