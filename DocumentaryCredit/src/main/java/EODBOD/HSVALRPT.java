package EODBOD;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Utilities.WindowHandle;

public class HSVALRPT {
	
	 private WebDriver driver;
	 private WebDriverWait wait;

	    public HSVALRPT(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
	    }
	    public void execute(List<String> data,String excelpath,String sheetname,int row) throws Exception {
	    	
		 	driver.switchTo().defaultContent();
	        driver.switchTo().frame("loginFrame");

	        WindowHandle.slowDown(1);
	    	WebElement searchbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuSelect")));
	        searchbox.click();
	        searchbox.sendKeys("HSVALRPT");

	       WebElement element4 = driver.findElement(By.id("menuSearcherGo"));
	       element4.click();
	       
	       WebElement exeLevel = driver.findElement(By.xpath("//select[@id='exec_level']"));
	       WindowHandle.selectByVisibleText(exeLevel,data.get(1));
	       
	       WebElement submit = driver.findElement(By.id("Submit"));
	       submit.click();
	    }

}
