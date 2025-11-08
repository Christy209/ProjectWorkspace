package CASA;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import Utilities.RowData;
import Utilities.WindowHandle;

public class InterestCalculation {
	  private WebDriver driver;
	    private WebDriverWait wait;

	    public InterestCalculation(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
	    }
		 public void execute(RowData id ,List<String> intcal) throws IOException, Exception {
		 				   		
			 try {
				 	driver.switchTo().defaultContent();
			        driver.switchTo().frame("loginFrame");
					 WebElement searchbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuSelect")));
				     searchbox.click();
				     searchbox.sendKeys(intcal.get(1));
			
				     WebElement element4 = driver.findElement(By.id("menuSearcherGo"));
				     element4.click();
				     WindowHandle.handleAlertIfPresent(driver); 
			 
			 } catch (Exception e) {
			    	System.out.println("Error: " + e.getMessage());
			 }
			 
			 		driver.switchTo().defaultContent();
			 	
			 	 driver.switchTo().frame("loginFrame");
			 	 driver.switchTo().frame("CoreServer");
			 	 driver.switchTo().frame("FINW");
			 	 
		 	try{
		 		
		 		
		 		WebElement reportto = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("report_to")));
		 		reportto.sendKeys(intcal.get(2));
		 		
		 		WebElement schmtype = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("schm_type")));
		 		schmtype.sendKeys(intcal.get(3));
		 		
		 		WebElement fromacctnum = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("from_acct_num")));
		 		fromacctnum.sendKeys(id.getByHeader("Created_AccountID"));
		 		//fromacctnum.sendKeys("003000500000055");
		 		
		 		WebElement fromdate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("acint.from_date_ui")));
		 		fromdate.sendKeys(intcal.get(4));
		 		
		 		WebElement todate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("to_date_ui")));
		 		todate.sendKeys(intcal.get(5)); 	
		 	
		 		WebElement radioButton = driver.findElement(By.xpath("//input[@type='radio' and @value='Y']"));
		        radioButton.click(); 		
		 		 		
			    WebElement Submit = driver.findElement(By.xpath("//input[@type='button' and @value='Submit']"));
		        Submit.click();
		
		 	 } catch (Exception e) {
		     	System.out.println("Error: " + e.getMessage());
		     }
    }
}
