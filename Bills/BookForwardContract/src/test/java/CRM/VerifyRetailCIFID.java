package CRM;

import java.io.IOException;
import java.time.Duration;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Annotation.ExcelData;
import Base.DriverManager;
import Utilities.Dataproviders;
import Utilities.WindowHandle;

public class VerifyRetailCIFID {

	protected WebDriver driver;
    protected WebDriverWait wait;
    
	 @BeforeClass
	 public void setup() throws IOException { 
	
	        driver = DriverManager.getDriver();
	        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
	        String userID = DriverManager.getProperty("userid2");
            String password = DriverManager.getProperty("password2");

            // Login with first user
            DriverManager.login(userID, password);
            System.out.println("Executing TC01: Verify the RetailCIFID: " + userID);
	    }

//	 @DataProvider(name="RetailCif")
//	    public Object[][] getInputData() throws IOException {
//	        return Dataproviders.getInputData(3); // Row 4 of Sheet2
//	    }
//	    @Test(dataProvider = "RetailCif")
	  @Test(dataProvider = "Inputs", dataProviderClass = Dataproviders.class)
	  @ExcelData(sheetName = "Sheet2", rowIndex = 3)
		   	public void ModifyRetailCIFID(String[] getData) throws Exception {
		        String Windowhandle = driver.getWindowHandle();
		        System.out.println("Main window :" +Windowhandle);

		        try {
		        	
		        	WindowHandle.slowDown(2);
		            
		            WebElement appSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("appSelect")));
		            Select dropdown = new Select(appSelect);
		            dropdown.selectByValue("CRMServer");
		            WindowHandle.handleAlertIfPresent(driver);

		        } catch (Exception e) {
		            System.out.println("No iframe or error during switching: " + e.getMessage());
		        }
		        WindowHandle.handlePopupIfExists(driver);
	 
				WebElement submitButton = wait .until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='submitBtn']")));
	            JavascriptExecutor js = (JavascriptExecutor) driver;
	            js.executeScript("arguments[0].click();", submitButton);

	        	driver.switchTo().window(Windowhandle);
	        	System.out.println("Switched back to parent window : " + driver.getCurrentUrl());
	        	
	            
	            try{
	            	driver.switchTo().defaultContent();
	            	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("loginFrame")));
	            	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CRMServer")));
	            	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("ScreensTOCFrm")));
	            	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("Functionmain")));
	            	
	            	WebElement cifRetail = wait.until(ExpectedConditions.elementToBeClickable(By.id("spanFor1")));
	            	cifRetail.click();
	            	
	            	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("ifrmFor1")));
	            	
	            	
	            	WebElement EntityQue = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@id='spanFor3']")));
	            	EntityQue.click();
	            	
	            	driver.switchTo().defaultContent();
	            	PageObject.Frame(wait);
	            	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("process")));
	            	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("userArea")));
	            	
	            	WindowHandle.selectDropdownIfValuePresent(driver, wait, By.xpath("//select[@name='TypeFilter']"), "Business Center Group");
	            	WindowHandle.selectDropdownIfValuePresent(driver, wait, By.name("GpFilter"), "General Banking");
	            	
	            	WebElement EditCIFID = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='cifID']")));
	            	EditCIFID.sendKeys(getData[1]);
	            	
	            	
	            	
	            	driver.switchTo().defaultContent();
	            	PageObject.Frame(wait);
	            	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("process")));
	            	
	            	WebElement get = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='Get']")));
	            	get.click();
	            	
	            	WindowHandle.handleAlertIfPresent(driver);
	            	
	            	driver.switchTo().defaultContent();
	            	PageObject.Frame(wait);
	            	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("servletFrm")));
	                
	                WebElement checkbox = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//font[normalize-space()='A']")));
	                checkbox.click();
	    	         
	    	         
	    	         driver.switchTo().defaultContent();
		             PageObject.iframe(wait);
		             wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("tabViewFrm")));
		             
		             wait.until(ExpectedConditions.elementToBeClickable(By.id("fnttab1"))).click();
		             	
		             driver.switchTo().defaultContent();
		             PageObject.iframe(wait);
		             wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("tabContentFrm")));
		             wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("userArea")));
		             wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("IFrmtab1")));

		             
		             WebElement link = wait.until(ExpectedConditions.elementToBeClickable(
		                     By.xpath("//a[normalize-space()='Approval :- Process Time : 1 Days']")));
		                 link.click();
		             
		             WindowHandle.handlePopupIfExists(driver);
		             driver.switchTo().defaultContent();
		             PageObject.Tbs(wait);
		             
		             WindowHandle.selectDropdownIfValuePresent(driver, wait, By.xpath("//select[@name='Decision']"), "Approve");

		             driver.switchTo().defaultContent();
		             PageObject.button(wait);
		             
		             wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='saveBut']"))).click();
		             
		             //WindowHandle.handleAlertIfPresent(driver);
		             WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(2)); // Short wait
		             Alert alert = alertWait.until(ExpectedConditions.alertIsPresent());
		             String alertText = alert.getText();
		             alert.accept();
		             
		             
		             if (alertText.contains("There was an error in saving")) {
		            	 
		             WindowHandle.selectDropdownIfValuePresent(driver, wait, By.xpath("//select[@name='Decision']"), "Reject");
		             wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//textarea[@name='Remarks']"))).sendKeys("Retail Cif ID");
		            
		             }	
	            	
	            } catch (Exception e) {
	                e.printStackTrace(); // Log the exception
	                // Handle specific exceptions or perform recovery actions as needed
	            }
	    }
}
