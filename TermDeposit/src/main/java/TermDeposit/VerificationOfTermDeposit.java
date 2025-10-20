package TermDeposit;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Annotation.ExcelData;
import Base.BaseTest; 
import Base.DriverManager;
import Utilities.Dataproviders;
import Utilities.ExcelUtils;
import Utilities.LogOut;
import Utilities.RowData;
import Utilities.Validation;
import Utilities.WindowHandle;


public class VerificationOfTermDeposit extends BaseTest{

    protected static WebDriver driver;
    protected static WebDriverWait wait;
   
    @BeforeClass
    public  void setup() throws IOException {
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        String userID = DriverManager.getProperty("userid1");
        String password = DriverManager.getProperty("password1");

        DriverManager.login(userID, password);
        System.out.println("✅ Logged in as: " + userID);
    }
     @Test(dataProvider = "testcase", dataProviderClass = Dataproviders.class)
     @ExcelData(sheetName = "Sheet1", rowIndex = {1, 3})
     
     
     public static void VerifyTDAccount(RowData createData,RowData verifyData) throws Exception {
    	 String mainwindow = driver.getWindowHandle();
	
        try {
        	
       	 WindowHandle.slowDown(2);
         WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),verifyData.getByIndex(1));
         WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
         ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

        
        } catch (Exception e) {
        	System.out.println("Error: " + e.getMessage());
        }
        
        
      try {
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));
            
            WebElement funCode1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("verifyCancel")));
            funCode1.sendKeys(verifyData.getByIndex(2));
            WindowHandle.slowDown(2);
       
              WebElement acctID = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tempForacid")));
	          String AcctID = createData.getByHeader("Created_AccountID");
              ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", acctID ,AcctID);
             
	    	   WebElement go = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='Accept']")));
	    	   ((JavascriptExecutor) driver).executeScript("arguments[0].click();", go);
      }catch(Exception e) {
    	  WindowHandle.checkForApplicationErrors(driver);
      }
	    	   Map<String, String> appData = getApplicationData(driver, wait);
	           int mismatchCount = Validation.validateData(createData.getHeaderMap(), appData);

	          if (mismatchCount == 0) {
	              System.out.println("✅ No mismatches found. Proceeding to handle popup...");
	              try {
	             	 Thread.sleep(2000); // allow popup to appear

	          	    String mainWindow = driver.getWindowHandle();
	          	    Set<String> allWindows = driver.getWindowHandles();

	          	    if (allWindows.size() > 1) {
	          	        System.out.println("ℹ️ Popup detected after submit.");

	          	        for (String handle : allWindows) {
	          	            if (!handle.equals(mainWindow)) {
	          	                driver.switchTo().window(handle);
	          	                break;
	          	            }
	          	        }

	          	        try {
	          	            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
	          	            WebElement accept = shortWait.until(ExpectedConditions.elementToBeClickable(By.id("accept")));
	          	            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", accept);
	          	            System.out.println("✅ Accept clicked successfully (Selenium/JS).");
	          	        } catch (Exception inner) {
	          	            System.out.println("⚠ Selenium could not click Accept, trying Robot...");
	          	            Robot robot = new Robot();
	          	            robot.setAutoDelay(500);
	          	            robot.keyPress(KeyEvent.VK_TAB);
	          	            robot.keyRelease(KeyEvent.VK_TAB);
	          	            robot.keyPress(KeyEvent.VK_ENTER);
	          	            robot.keyRelease(KeyEvent.VK_ENTER);
	          	            System.out.println("✅ Accept clicked via Robot.");
	          	        }

	          	        // Optional: close popup if still open
	          	        try {
	          	            driver.close();
	          	        } catch (Exception ignored) {}

	          	    } else {
	          	        System.out.println("ℹ️ No popup detected, continuing...");
	          	    }

	          	    // Switch back to main window
	          	    driver.switchTo().window(mainWindow);
	          	    System.out.println("✅ Back to main window.");

	          	} catch (Exception e) {
	          	    System.out.println("❌ Error during Submit: " + e.getMessage());
	          	}
	              WindowHandle.ValidationFrame(driver);
	              String successLog = WindowHandle.checkForSuccessElements(driver);
	              if(successLog != null){
	                  System.out.println("✅ Test Success");
	              }
	       //       String  accountId=WindowHandle.getAccountId(driver);
//	              if (accountId != null) {
//	                  System.out.println("✅ Extracted Account ID: " + accountId);
//	              } else {
//	                  System.out.println("❌ Account ID not found!");
//	              }
//	      			 // Optionally close popup and return driver.close();
	      			 driver.switchTo().window(mainwindow);
	      			 driver.switchTo().defaultContent();
	      				LogOut.performLogout(driver, wait);
	      			 
	              }

	          }


	      

	      private static Map<String, String> getApplicationData(WebDriver driver, WebDriverWait wait2) throws Exception {
	          Map<String, String> appData = new HashMap<>();
	          String mainWindowHandle = driver.getWindowHandle();
	          System.out.println("Validation main window :" + mainWindowHandle);
	    		    
	        //////////////////////////////////////General tab/////////////////////////
	    		    
	        WebElement generalDetailsTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("tdgen")));
		    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", generalDetailsTab);
		    generalDetailsTab.click();
	         
		    
	
		    appData.put("modeOfOperCode", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='modeOfOperCode']","xpath"));
		    appData.put("locationCode", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='locationCode']","xpath"));
            /////////////////////////////Interest Details///////////////////////////////////
	        
	        WebElement interestTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("tdint")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", interestTab);
            interestTab.click();
            
            // Initialize variable with a default value
        	String payInterestValue = "Not Selected"; 

        	// Locate the radio buttons
        	WebElement payInterestYes = driver.findElement(By.xpath("//input[@id='payIntFlg' and @value='Y']"));
        	WebElement payInterestNo = driver.findElement(By.xpath("//input[@id='payIntFlg' and @value='N']"));

        	// Check which radio button is selected
        	if (payInterestYes.isSelected()) {
        		payInterestValue = "Yes";
        	} else if (payInterestNo.isSelected()) {
        		payInterestValue = "No";
        	}
        	
        	appData.put("Pay Interest", payInterestValue); 
            
            appData.put("custPrefIntCr", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='custPrefIntCr']","xpath"));
		    appData.put("acctPrefIntCr", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='acctPrefIntCr']","xpath"));
		    appData.put("chanPrefIntCr", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='chanPrefIntCr']","xpath"));
		    appData.put("pegFreqMnths", ExcelUtils.getTextOrValue(driver, wait, "pegFreqMnths","id"));
		    appData.put("pegFreqDay", ExcelUtils.getTextOrValue(driver, wait, "pegFreqDay","id"));
		    
		   
            
            /////////////////////////////Scheme Details///////////////////////////////////
		    

            WebElement Schmdetailstab = wait.until(ExpectedConditions.elementToBeClickable(By.id("tdsch")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Schmdetailstab);
            Schmdetailstab.click();
            WindowHandle.slowDown(2);
           
           
            appData.put("depAmt", ExcelUtils.getTextOrValue(driver, wait, "depAmt","id"));
            appData.put("DepositPeriod", ExcelUtils.getTextOrValue(driver, wait, "depPerdMths","id"));
		    appData.put("repayAcct", ExcelUtils.getTextOrValue(driver, wait, "repayAcct","id"));
		    appData.put("operAcct", ExcelUtils.getTextOrValue(driver, wait, "operAcct","id"));
		    
		    String Nomineeflg = "Not Selected"; 

    		// Locate the radio buttons
    		WebElement NomineeflgYes = driver.findElement(By.xpath("//input[@id='nomineeFlg' and @value='Y']"));
    		WebElement NomineeflgNo = driver.findElement(By.xpath("//input[@id='nomineeFlg' and @value='N']"));

    		// Check which radio button is selected
    		if (NomineeflgYes.isSelected()) {
    			payInterestValue = "Yes";
    		} else if (NomineeflgNo.isSelected()) {
    			payInterestValue = "No";
    		}	        		   
		
    			appData.put("Nominee Flag", Nomineeflg);
    			
    			 /////////////////////////////////Renewal&Closure///////////////////////////////////		  
    		    
    		    WebElement RenewalClosure = wait.until(ExpectedConditions.elementToBeClickable(By.id("tdren")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", RenewalClosure);
                RenewalClosure.click();
               
                
    		    appData.put("autoClosureFlg", ExcelUtils.getTextOrValue(driver, wait, "autoClosureFlg","id"));
    		   // appData.put("maxRenew", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='maxRenew']","xpath"));
    		    appData.put("renewMths", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='renewMths']","xpath")); 
    		    appData.put("renewSchm", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='renewSchm']","xpath")); 
    		    appData.put("renewGLSubHead", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='renewGLSubHead']","xpath")); 
    		    appData.put("renewCrncy", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='renewCrncy']","xpath"));
    		    
    			
    			  ///////////////////////////////////Nominee tab//////////////////////////////////////////
    		    
    		    WebElement NomineeTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("nominationdetails")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", NomineeTab);
                NomineeTab.click();
        
                
    		    appData.put("nomName", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='nomName']","xpath"));
    		    appData.put(" relation", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='relation']","xpath"));
    		    appData.put("nomAddrLine1", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='nomAddrLine1']","xpath"));
    		    appData.put(" nomCityCode ", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='nomCityCode']","xpath"));
    		    appData.put("nomStateCode", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='nomStateCode']","xpath"));
    		    appData.put("nomCntryCode", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='nomCntryCode']","xpath"));
    		    appData.put("nomPostalCode", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='nomPostalCode']","xpath"));
    		    appData.put("nomPcnt", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='nomPcnt']","xpath"));
    		   
        
          
		    

            ///////////////////////////////////////////RelatedParty Details///////////////////////////////////////////////////////
        
    		   
    		       WebElement RelatedPartydetailstab = wait.until(ExpectedConditions.elementToBeClickable(By.id("relatedpartydetails")));
    		       ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", RelatedPartydetailstab);
    		       ((JavascriptExecutor) driver).executeScript("arguments[0].click();", RelatedPartydetailstab);
    		       appData.put("custTitle", ExcelUtils.getTextOrValue(driver, wait, "custTitle", "id"));
    		        appData.put("custName", ExcelUtils.getTextOrValue(driver, wait, "custName", "id"));
    		        appData.put("custAddrLine1", ExcelUtils.getTextOrValue(driver, wait, "custAddrLine1", "id"));

    		       
           
           ///////////////////////////////////////////////MIS Code////////////////////////////////////////////
            
            
            WebElement Miscodesdetailstab = wait.until(ExpectedConditions.elementToBeClickable(By.id("miscodes")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Miscodesdetailstab);
            Miscodesdetailstab.click();
            
            appData.put("sectCode", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='sectCode']","xpath"));
		    appData.put("subSectCode", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='subSectCode']","xpath"));
		    appData.put("occCode", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='occCode']","xpath"));
		    //appData.put("Purpose of Advance", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='purpAdv']","xpath"));
		    appData.put("modeAdv", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='modeAdv']","xpath"));
		    //appData.put("Advance Type", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='typeAdv']","xpath"));
		    appData.put("natAdv", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='natAdv']","xpath"));
	   

       
         //////////////////////////////////////////////Document details/////////////////////////////////////////////////////
        
		 WebElement documentDetailsTab = driver.findElement(By.xpath("//a[@id='documentdetails']"));
	     ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", documentDetailsTab);
	     ((JavascriptExecutor) driver).executeScript("arguments[0].click();", documentDetailsTab);
	     WindowHandle.slowDown(2);
	         
		   appData.put("docCode", ExcelUtils.getTextOrValue(driver, wait, "docCode","id"));
		   appData.put("docScanFlg", ExcelUtils.getTextOrValue(driver, wait, "docScanFlg","id"));
//		   WebElement submit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Submit")));
//	   		submit.click();
	
			////////////////////////////Tranaction tab////////////////////
			
	   		try {
				WindowHandle.slowDown(2);
				WebElement otherdetails = wait
						.until(ExpectedConditions.elementToBeClickable(By.id("otherdetails")));
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", otherdetails);
				otherdetails.click();
				WebElement submit1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Submit")));
		   		submit1.click();
				} catch (Exception e) {
					 WindowHandle.checkForApplicationErrors(driver);
				}
//		WindowHandle.slowDown(2);
//				WebElement tdtran = wait
//						.until(ExpectedConditions.elementToBeClickable(By.id("tdtran")));
//				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", tdtran);
//				tdtran.click();
//				WebElement submit1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Submit")));
//		   		submit1.click();
//				WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
//				Alert alert = shortWait.until(ExpectedConditions.alertIsPresent());
//				System.out.println("⚠️ Alert found: " + alert.getText());
//				alert.accept();  // or alert.dismiss();

//		 try{
//		   		WebElement submit11 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Submit")));
//		   		submit11.click();
//				}catch (Exception e) {
//					System.out.println("Error " + e.getMessage());
//				}	
					
//	       WindowHandle.handlePopupIfExists(driver);
//	       WindowHandle.handleAlertIfPresent(driver);
//	       WebElement acceptButton = driver.findElement(By.id("Accept"));
//	       acceptButton.click();
//	       driver.switchTo().window(mainWindowHandle);
	    

				return appData;
		   
	
}
}
