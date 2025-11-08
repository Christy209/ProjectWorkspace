package CASA;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Utilities.RowData;
import Utilities.WindowHandle;

public class InwardDocumentaryCreditsMaintenance {
	private WebDriver driver;
	private WebDriverWait wait;

	    public InwardDocumentaryCreditsMaintenance(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
	    }
	    	    
	    public String execute(RowData inputData , String sheetname,int row,String excelpath) throws Exception {
	    	String mainWindowHandle = driver.getWindowHandle();
	    	WindowHandle.slowDown(4);
	    	
	    	WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),inputData.getByIndex(1));

	    	WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
	    	((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

	    	try {
		    	  wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
		          wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));
		          
		          WindowHandle.selectDropdownIfValuePresent(driver,wait,(By.id("funcCode")),inputData.getByIndex(2));
		          
		          WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("solId"))),inputData.getByIndex(3));
		          
		          WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("idcmNum"))),inputData.getByIndex(4));
		          
		          WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("idcmType"))),inputData.getByIndex(5));
		          
		          WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cifId"))),inputData.getByIndex(6));
		          
		          WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("dcCurrency"))),inputData.getByIndex(7));
		          
		          WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("messageId"))),inputData.getByIndex(8));
		          
		  		WebElement Go = driver.findElement(By.id("Go"));
				Go.click();
		          
	    	}catch(Exception e) {}
	    	
			return mainWindowHandle;
	    }
}

