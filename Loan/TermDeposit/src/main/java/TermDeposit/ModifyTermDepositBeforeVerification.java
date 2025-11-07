package TermDeposit;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import Annotation.ExcelData;
import Base.BaseTest;
import Base.DriverManager;
import Utilities.Dataproviders;
import Utilities.LogOut;
import Utilities.RowData;
import Utilities.WindowHandle;

public class ModifyTermDepositBeforeVerification extends BaseTest {

	protected static WebDriver driver = DriverManager.getDriver();
    protected static WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

	   
//	    @BeforeClass
//	    public  void setup() throws IOException {
//	        driver = DriverManager.getDriver();
//	        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
//
//	        String userID = DriverManager.getProperty("userid");
//	        String password = DriverManager.getProperty("password");
//
//	        DriverManager.login(userID, password);
//	        System.out.println("✅ Logged in as: " + userID);
//	    }

    @Test(dataProvider = "testcase", dataProviderClass = Dataproviders.class)
    @ExcelData(sheetName = "Sheet1", rowIndex = {1,2})
    public void Verification(RowData fr ,RowData data) throws Exception 
    {
    	String mainWindowHandle = driver.getWindowHandle();
    	 driver.switchTo().defaultContent(); 
		  driver.switchTo().frame("loginFrame");
		 WindowHandle.slowDown(6);
		 
  WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
       
    	
    	
    	// WindowHandle.slowDown(4);
         WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),data.getByIndex(1));
         WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
         ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

             wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
             wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));
       

        WebElement acctID = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tempForacid")));
        String AcctID = fr.getByHeader("Created_AccountID");
        ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", acctID, AcctID);

        JavascriptExecutor js1 = (JavascriptExecutor) driver;
        js1.executeScript("document.getElementById('Accept').click();");

        
	/////////////////////////////// General Tab//////////////////////////////////////////

	try {
		
		WindowHandle.safeClick(driver, wait, By.id("Validate"));
	} catch (Exception e)
	{
			   System.out.println("Error: " + e.getMessage());
		    }	

			/////////////////////////////// interest Tab///////////////////////////////////////////////////
			
		 try {


			 WindowHandle.slowDown(2);
			 ((JavascriptExecutor) driver).executeScript("document.getElementById('tdint').click();");

				WindowHandle.safeClick(driver, wait, By.id("Validate"));
			} catch (Exception e) {
				System.out.println("Error " + e.getMessage());
			}

		/////////////////////////////// Scheme Tab ///////////////////////////////////////////////////
		             
					 
		try {
		
			 WindowHandle.slowDown(2);
			 ((JavascriptExecutor) driver).executeScript("document.getElementById('tdsch').click();");
			 
			 //WindowHandleJS wh = new WindowHandleJS(driver);
				
//			 WebElement depAmt = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("depAmt")));
//			 wh.setValue(depAmt, data.getByIndex(14));

			 WindowHandle.safeClick(driver, wait, By.id("Validate"));
			 
		} catch (Exception e) {
		System.out.println("Error: " + e.getMessage());
		}
		
		 /////////////////////////////// Related Party Tab///////////////////////////////////////////////////
        try {
        	WindowHandle.slowDown(2);
        	
			WebElement relatedPartyTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("relatedpartydetails")));
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", relatedPartyTab);
			
    	} catch (Exception e) {
			System.out.println("Relationship tab Error : " + e.getMessage());
		}
        
        
			/////////////////////////////// RenewalClosure Tab///////////////////////////////////////////////////
			        
			try {
			WindowHandle.slowDown(2);
			WebElement RenewalClosure = wait.until(ExpectedConditions.elementToBeClickable(By.id("tdren")));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", RenewalClosure);
			RenewalClosure.click();
			
	    	} catch (Exception e) {
				System.out.println("RenewalClosure tab Error : " + e.getMessage());
			}
		/////////////////////////////// Nomination Tab///////////////////////////////////////////////////
		            
		try {  
		WindowHandle.slowDown(2);
		WebElement NomineeTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("nominationdetails")));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", NomineeTab);
		NomineeTab.click();
		        
		    	
	} catch (Exception e) {
		System.out.println("RenewalClosure tab Error : " + e.getMessage());
	}
		/////////////////////////////// MIS Tab///////////////////////////////////////////////////
		
		try {
		WindowHandle.slowDown(2);
		WebElement Miscodesdetailstab = wait.until(ExpectedConditions.elementToBeClickable(By.id("miscodes")));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Miscodesdetailstab);
		Miscodesdetailstab.click();
		WindowHandle.slowDown(3);
		WindowHandle.safeClick(driver, wait, By.id("Validate"));
		} catch (Exception e) {
						System.out.println("Error " + e.getMessage());
					}
		            
			/////////////////////////////// Document Tab///////////////////////////////////////////////////
			try {
				WindowHandle.slowDown(2);
				WebElement documentDetailsTab = wait
						.until(ExpectedConditions.elementToBeClickable(By.id("documentdetails")));
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", documentDetailsTab);
				documentDetailsTab.click();
				WindowHandle.slowDown(2);
 
				// WindowHandle.selectDropdownWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("docScanFlg"))),data.getByIndex(53));
				try {
					WindowHandle.slowDown(2);
					WebElement otherdetails = wait
							.until(ExpectedConditions.elementToBeClickable(By.id("otherdetails")));
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", otherdetails);
					otherdetails.click();
					
					} catch (Exception e) {
						System.out.println("Error " + e.getMessage());
					}

				WebElement submit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Submit")));
		   		submit.click();

	} catch (Exception e) {
		System.out.println("Error " + e.getMessage());
	}
			   		WebElement submit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Submit")));
			   		submit.click();
			   		
			   		WindowHandle.handlePopupIfExists(driver);
			   		WebElement accept = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accept")));
			   		accept.click();
			   		driver.switchTo().window(mainWindowHandle);
			   		
					
					try{
				   		
					    driver.switchTo().defaultContent(); // or use driver.switchTo().frame(0); if no name
					    driver.switchTo().frame("loginFrame");
					    driver.switchTo().frame("CoreServer");
					    driver.switchTo().frame("FINW");
					    String  accountId=WindowHandle.getAccountId(driver);
				           if (accountId != null) {
				               System.out.println("✅ Extracted Account ID: " + accountId);
				           } else {
				               System.out.println("❌ Account ID not found!");
				           }
				        LogOut.performLogout(driver, wait);
				        WindowHandle.slowDown(2);		
				      
					}catch (Exception e) {
						System.out.println("Account id creartin Error " + e.getMessage());
						Assert.fail("Test Failed: Exception occurred -> " + e.getMessage());
					}
				 	
}
}
