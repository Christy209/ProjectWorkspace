package CRM;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Base.DriverManager;
import Utilities.Dataproviders;
import Utilities.WindowHandle;

public class VerifyCorporateCIFID {
	
	protected static WebDriver driver;
    protected static WebDriverWait wait;
    
  @BeforeClass
    public static void setup() throws IOException { 
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        String userID = DriverManager.getProperty("userid2");
        String password = DriverManager.getProperty("password2");

        // Login with second user
        DriverManager.login(userID, password);
        System.out.println("Executing TC04: Savings Account Verification for User: " + userID);

    }
  @Test(dataProvider = "CorporateData", dataProviderClass = Dataproviders.class)
  
  public static void VerifyCorporatecIFID(String[] getData) throws Exception  {
	  
	  String Windowhandle = driver.getWindowHandle();
      System.out.println("First window :" +Windowhandle);

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
      	
      	WebElement CifCorporate = wait.until(ExpectedConditions.elementToBeClickable(By.id("spanFor2")));
      	CifCorporate.click();
      	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("ifrmFor2")));
      	WebElement Customer = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@id='spanFor3']")));
      	Customer.click();

     	driver.switchTo().defaultContent();
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("loginFrame")));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CRMServer")));
    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("DataAreaFrm")));
    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("topStyledFrame")));
    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("userArea")));
      	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("process")));
      	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("userArea")));
      	
      	//PageObject.waitAndFill(wait, "xpath", "//input[@id='cifID']//input[@id='cifID']", getData[108]);
    	WebElement cifid = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cifID")));
    	cifid.sendKeys(getData[108]);
    	
      	driver.switchTo().defaultContent();

      	
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("loginFrame")));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CRMServer")));
    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("DataAreaFrm")));
    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("topStyledFrame")));
    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("userArea")));
      	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("process")));
      	
      	WebElement get = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='Get']")));
      	get.click();
      	
      	WindowHandle.handleAlertIfPresent(driver);
      	driver.switchTo().defaultContent();

     wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("loginFrame")));
     wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CRMServer")));
     wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("DataAreaFrm")));
     wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("topStyledFrame")));
     wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("userArea")));
     wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("servletFrm")));

     WebElement checkbox = wait.until(ExpectedConditions.presenceOfElementLocated( By.xpath("//font[normalize-space()='A']")));
     checkbox.click();

      	driver.switchTo().defaultContent();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("loginFrame"));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("CRMServer"));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("DataAreaFrm"));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("dynamicTabFrm"));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("tabViewFrm"));
        WebElement targetElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("fnttab1")));
        targetElement.click();

      	driver.switchTo().defaultContent();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("loginFrame"));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("CRMServer"));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("DataAreaFrm"));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("dynamicTabFrm"));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("tabContentFrm"));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("userArea"));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("IFrmtab1"));
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[normalize-space()='CorpApprove :- Process Time : 1 Days']")));
        link.click();

      	WindowHandle.handlePopupIfExists(driver);
      	driver.switchTo().defaultContent();

      	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("tempFrm")));
      	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("tabContentFrm")));
      	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("userArea")));
      	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("IFrmtab0")));
      	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("formDispFrame")));
      	WindowHandle.selectDropdownIfValuePresent(driver, wait, By.xpath("//select[@name='Decision']"),getData[109]);
      	
      	driver.switchTo().defaultContent();
      	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("tempFrm")));
      	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("buttonFrm")));
      	
      	WebElement save = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='saveBut']")));
      	save.click();
      	
      	WindowHandle.handleAlertIfPresent(driver);
      }
  catch (Exception e) {
     System.out.println("Verification: " + e.getMessage());
 }
	  
	  
  }
}
