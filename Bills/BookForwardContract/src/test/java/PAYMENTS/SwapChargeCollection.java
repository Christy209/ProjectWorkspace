package PAYMENTS;

import java.time.Duration;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import Base.DriverManager;
import Utilities.DateUtils;
import Utilities.ErrorCapture;
import Utilities.RowData;
import Utilities.WindowHandle;

public class SwapChargeCollection {

		private WebDriver driver;
		private WebDriverWait wait;

		    public SwapChargeCollection(WebDriver driver) {
		        this.driver = driver;
		        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
		    }
		    	    
		    public String execute(RowData inputData , String sheetname,int row,String excelpath) throws Exception {
		    	String mainWindowHandle = driver.getWindowHandle();
		    	WindowHandle.slowDown(10);
		    	WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),inputData.getByIndex(1));
		    	WindowHandle.slowDown(10);
		    	WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
		    	((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);
		    	
		    	 	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		    	    wait.until(ExpectedConditions.alertIsPresent());
		    	    Alert alert = driver.switchTo().alert();
		    	    System.out.println("Alert text: " + alert.getText());
		    	    alert.accept();
		    	    System.out.println("Alert accepted.");
		    	    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("usertxt")));
			    	
			        String userID = DriverManager.getProperty("userid1");
			        String password = DriverManager.getProperty("password1");
		    	    driver.findElement(By.id("usertxt")).sendKeys(userID);
		    	    driver.findElement(By.id("passtxt")).sendKeys(password);
		    	    driver.findElement(By.id("Submit")).click();
	
		    	try {
			    	  wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
			          wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));
			          
			          WindowHandle.selectDropdownIfValuePresent(driver,wait,(By.id("funcCode")),inputData.getByIndex(2));
			          
			          WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("solId"))),inputData.getByIndex(3));
			          
			          WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("entityId"))),inputData.getByIndex(4));
			          
			          WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cifId"))),inputData.getByIndex(5));

			          String fromDate = inputData.getByIndex(6);
			  	      String formattedDate = DateUtils.toDDMMYYYY(fromDate);
			  	      WindowHandle.setValueWithJS(driver,
			  	      wait.until(ExpectedConditions.presenceOfElementLocated(By.id("fromTranDt_ui"))),formattedDate);
			  	    
			  	      String todate = inputData.getByIndex(7);
			  	      String formattedtodate = DateUtils.toDDMMYYYY(todate);
			  	      WindowHandle.setValueWithJS( driver,
				      wait.until(ExpectedConditions.presenceOfElementLocated(By.id("toTranDt_ui"))),formattedtodate);

			  		WebElement Go = driver.findElement(By.id("Accept"));
			  		((JavascriptExecutor) driver).executeScript("arguments[0].click();", Go);
			  		
			  		
			  		
			  		//ErrorCapture.checkForError(driver);
			  		ErrorCapture.checkForApplicationErrors(driver);
			  		
		    	}catch(Exception e) {}
		    	
				return mainWindowHandle;
		    }
 }
