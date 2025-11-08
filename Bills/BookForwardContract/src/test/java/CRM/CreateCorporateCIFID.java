package CRM;

import java.io.IOException;
import java.time.Duration;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Base.DriverManager;
import Utilities.Dataproviders;
import Utilities.ExcelUtils;
import Utilities.WindowHandle;

public class CreateCorporateCIFID {
	
	protected static WebDriver driver;
    protected WebDriverWait wait;
    
    @BeforeClass
	 public void setup() throws IOException { 
    	
	        driver = DriverManager.getDriver();
	        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
	        String userID = DriverManager.getProperty("userid");
           String password = DriverManager.getProperty("password");

           // Login with first user
           DriverManager.login(userID, password);
           System.out.println("Executing TC01: Savings Account Creation for User: " + userID);
	    }

	   @Test(dataProvider = "CorporateData", dataProviderClass = Dataproviders.class)
	   	public void createCustomerID(String[] getData) throws Exception {
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
            	
            	WebElement cifRetail = wait.until(ExpectedConditions.elementToBeClickable(By.id("spanFor2")));
            	cifRetail.click();
            	
            	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("ifrmFor2")));
            	
            	WebElement Newentity = wait.until(ExpectedConditions.elementToBeClickable(By.name("spanFor6")));
            	Newentity.click();
            	
            	WebElement Customer = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@id='subviewspanFor60']")));
            	Customer.click();
            }
        catch (Exception e) {
           System.out.println("No iframe or error during switching: " + e.getMessage());
       }
            driver.switchTo().defaultContent();
            
            try{
            	WindowHandle.goToMainFrame(wait);

            	WebElement Name = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='CorporateBO.corporate_Name']")));
            	Name.sendKeys(getData[1]);

            	WebElement short_Name = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='CorporateBO.short_Name']")));
            	short_Name.sendKeys(getData[2]); 
            	
            	WebElement body = driver.findElement(By.tagName("body"));
                Actions actions = new Actions(driver);
                actions.moveToElement(body, 0, 0).click().perform(); 
                
            	WebElement priority = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@name='CorporateModBO.priority']")));
            	WindowHandle.selectByVisibleText(priority, getData[3]);

            	WebElement legalEntity_Type = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@name='CorporateModBO.legalEntity_Type']")));
            	WindowHandle.selectByVisibleText(legalEntity_Type, getData[4]);

                WebElement keyContact_PersonName = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='CorporateBO.keyContact_PersonName']")));
                keyContact_PersonName.sendKeys(getData[5]);

            	WebElement Corpsegment = wait.until(ExpectedConditions.elementToBeClickable(By.name("CorporateModBO.segment")));
            	Corpsegment.sendKeys(getData[6]);
            	actions.moveToElement(body, 0, 0).click().perform();
            	
            	WebElement CorpSubsegment = wait.until(ExpectedConditions.elementToBeClickable(By.name("CorporateModBO.subSegment")));
            	CorpSubsegment.sendKeys(getData[7]);

            	WebElement Phoncntrycode = wait.until(ExpectedConditions.elementToBeClickable(By.name("CorporateBO.phone.cntrycode")));
            	Phoncntrycode.sendKeys(getData[8]);

            	WebElement Phonareacode = wait.until(ExpectedConditions.elementToBeClickable(By.name("CorporateBO.phone.areacode")));
            	Phonareacode.sendKeys(getData[9]);

            	WebElement Phonlocalcode = wait.until(ExpectedConditions.elementToBeClickable(By.name("CorporateBO.phone.localcode")));
            	Phonlocalcode.sendKeys(getData[10]);
            	
            	WebElement business_Type = wait.until(ExpectedConditions.elementToBeClickable(By.name("Cat_CorporateModBO.business_Type")));
            	business_Type.sendKeys(getData[11]);

            	WebElement PlaceOperation = wait.until(ExpectedConditions.elementToBeClickable(By.name("CorporateBO.principle_PlaceOperation")));
            	PlaceOperation.sendKeys(getData[12]);
            	actions.moveToElement(body, 0, 0).click().perform();

            	WebElement notes = wait.until(ExpectedConditions.elementToBeClickable(By.name("CorporateBO.notes")));
            	notes.sendKeys(getData[13]); 

            	WebElement date = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='3_CorporateBO.date_Of_Incorporation']")));
            	date.sendKeys(getData[14]); 

            	WebElement region = wait.until(ExpectedConditions.elementToBeClickable(By.name("CorporateModBO.region")));
            	region.sendKeys(getData[15]); 
            	actions.moveToElement(body, 0, 0).click().perform();
            	
            	WebElement RelType = driver.findElement(By.name("CorporateModBO.relationship_Type"));
            	WindowHandle.selectByVisibleText(RelType, getData[16]);

            	WebElement sector = wait.until(ExpectedConditions.elementToBeClickable(By.name("CorporateBO.sector")));
            	sector.sendKeys(getData[17]); 
            	actions.moveToElement(body, 0, 0).click().perform();

            	WebElement taxID = wait.until(ExpectedConditions.elementToBeClickable(By.name("CorporateBO.taxID")));
            	taxID.sendKeys(getData[18]); 

            	WebElement entityClass = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("CorporateModBO.entityClass")));
            	WindowHandle.selectByVisibleText(entityClass, getData[19]);

            	WebElement RegNo = wait.until(ExpectedConditions.elementToBeClickable(By.name("CorporateBO.registration_Number")));
            	RegNo.sendKeys(getData[20]); 

             	WebElement PriSolId = wait.until(ExpectedConditions.elementToBeClickable(By.name("CorporateBO.primary_Service_Center")));
             	PriSolId.sendKeys(getData[21]);
             	actions.moveToElement(body, 0, 0).click().perform();

            	WebElement subSector = wait.until(ExpectedConditions.elementToBeClickable(By.name("CorporateModBO.subSector")));
            	subSector.sendKeys(getData[22]); 
                actions.moveToElement(body, 0, 0).click().perform(); // C

                WebElement FundSource = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("CorporateBO.source_Of_Funds")));
                FundSource.sendKeys(getData[23]);
                
                WebElement AnnualIncome = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("3_CorporateBO.average_AnnualIncome")));
                AnnualIncome.sendKeys(getData[24]);
                actions.moveToElement(body, 0, 0).click().perform(); // C

            	WebElement NativeLangCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("CorporateBO.NativeLangCode")));
            	WindowHandle.selectByVisibleText(NativeLangCode, getData[25]);

                WebElement PrimaryRMLogin_ID = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("CorporateBO.PrimaryRMLogin_ID")));
                PrimaryRMLogin_ID.sendKeys(getData[26]);
                
	}catch(Exception e) {
        System.out.println("Frame not found: " + e.getMessage());  
	} 
            
            ///////////////////////document details//////////////////////////////////////
            try {
            WebElement plang = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//font[@id='fnttpageCont1']")));
	     	 plang.click();
	     	 
	     	documentdetails(driver, wait, true,getData);
           	
           	if ("Y".equalsIgnoreCase(getData[34])) {
           		switchToWindowAndFrame(Windowhandle, wait);
	    	    
	    	    documentdetails(driver, wait, false, getData);
           
           	}
            if ("Y".equalsIgnoreCase(getData[42])) {
            	
	    	    String addressToDelete = getData[43]; // from Excel
	    	    
	    	    switchToWindowAndFrame(Windowhandle, wait);
	    	    
	    	    String xpath = "//table[@id='table0']//tr[contains(.,'" + addressToDelete + "')]//font[contains(text(),'" + addressToDelete + "')]";
	    	    WebElement addressCell = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
	    	    js.executeScript("arguments[0].scrollIntoView(true);", addressCell); // Ensure it's in view
	    	    addressCell.click();

	    	    WebElement deleteBtn = wait.until(ExpectedConditions.elementToBeClickable(By.name("RemoveIdentificationDetails")));
	    	    deleteBtn.click();
	    	    WindowHandle.handleAlertIfPresent(driver);

	    	} else {
	    	    System.out.println("No delete operation needed.");
	    	}
            } catch (Exception e) {
                System.out.println("Document Tab Error: " + e.getMessage());
            }
		////////////////////////////////////////////address details//////////////////////////////////////////////////////////////////////////////
       switchToWindowAndFrame(Windowhandle, wait);
		
		WebElement Address =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fnttpageCont2")));
		Address.click();
		
		WebElement preferredAddress =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("CorporateBO.Address.preferredAddress")));
		WindowHandle.selectByVisibleText(preferredAddress, getData[44]);
		//WindowHandle.selectDropdownIfValuePresent(driver, wait, By.name("CorporateBO.Address.preferredAddress"),getData[44]);

		WebElement AddressMail =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='Add Address']")));
		AddressMail.click();
		
		///////////////////////////First Address/////////////////////////////////////////////////////////////
		try {
		WindowHandle.handlePopupIfExists(driver);
		
		fillAddressDetails(driver, wait,false,getData);
		
		WebElement save = wait.until(ExpectedConditions.elementToBeClickable(By.name("Save")));
		save.click();
		WindowHandle.handleAlertIfPresent(driver);
		
		///////////////////////////Second Address/////////////////////////////////////////////////////////////
		
		if ("Y".equalsIgnoreCase(getData[57])) {
		
			switchToWindowAndFrame(Windowhandle, wait);
		
		WebElement preferredAddress2 =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("CorporateBO.Address.preferredAddress")));
		WindowHandle.selectByVisibleText(preferredAddress2, getData[58]);
		
		WebElement AddressMail2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='Add Address']")));
		AddressMail2.click();
		
		WindowHandle.handlePopupIfExists(driver);
		
		fillAddressDetails(driver, wait, true,getData);
		
		WebElement saveSecondary = wait.until(ExpectedConditions.elementToBeClickable(By.name("Save")));
		saveSecondary.click();
		WindowHandle.handleAlertIfPresent(driver);
		
		driver.switchTo().window(Windowhandle);
		System.out.println("Switched back to previous window: " + driver.getCurrentUrl());
		
		} else {
		System.out.println("Second address not required.");
		}
		
		///////////////////////////////delete///////////////////////////////////////
		if ("Y".equalsIgnoreCase(getData[71])) {
		String addressToDelete = getData[72]; // from Excel
		switchToWindowAndFrame(Windowhandle, wait);
		
		String xpath = "//table[@id='table0']//tr[contains(.,'" + addressToDelete + "')]//font[contains(text(),'" + addressToDelete + "')]";
		WebElement addressCell = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
		js.executeScript("arguments[0].scrollIntoView(true);", addressCell); // Ensure it's in view
		addressCell.click();
		
		WebElement deleteBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='Remove Address']")));
		deleteBtn.click();
		WindowHandle.handleAlertIfPresent(driver);
		
		} else {
		System.out.println("No delete operation needed.");
		}
		
		} catch (Exception e) {
		System.out.println("SecondAddressDetails tab error " + e.getMessage());
		}
		
		
		/////////////////////////////////////////////////////////phone details/////////////////////////////////////////////////////
		try {  

			switchToWindowAndFrame(Windowhandle, wait);
		
		WebElement PhoneDTab =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fnttpagePhone")));
		PhoneDTab.click();
		
		fillContactDetails(driver, wait, false,getData); 
		
		/////////////////////////Second phone and email details///////////////////////////////
		
		if ("Y".equalsIgnoreCase(getData[83])) {
			switchToWindowAndFrame(Windowhandle, wait);
		
		fillContactDetails(driver, wait, true, getData);
		
		}else {
		System.out.println("SKipping there is no SecordDetails flg as Y");
		}
		///////////////////delete///////////////////////////////////////////////////
		
		if ("Y".equalsIgnoreCase(getData[94])) {
		
		String addressToDelete = getData[95]; // from Excel
		
		switchToWindowAndFrame(Windowhandle, wait);
		
		String xpath = "//table[@id='table0']//tr[contains(.,'" + addressToDelete + "')]//font[contains(text(),'" + addressToDelete + "')]";
		WebElement addressCell = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
		js.executeScript("arguments[0].scrollIntoView(true);", addressCell); // Ensure it's in view
		addressCell.click();
		
		WebElement deleteBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='DELETE_PHONE']")));
		deleteBtn.click();
		WindowHandle.handleAlertIfPresent(driver);
		
		} else {
		System.out.println("No delete operation needed.");
		}
		
		
		} catch (Exception e) {
		System.out.println("Phone and email error: " + e.getMessage());
		}
		
		 ///////////////////////////////////////////////////////////////core Interface tab//////////////////////////////////////////////
  	  
		switchToWindowAndFrame(Windowhandle, wait);
 	  try {
 		    
          	WebElement CoreInterfacetab =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//font[@id='fnttpageCont5']")));
          	CoreInterfacetab.click();
          	
        	    WindowHandle.selectDropdownIfValuePresent(driver, wait,By.xpath("//select[@name='CoreInterfaceModBO.FreeCode6Desc']"),
        	    getData[96]);

        	    WindowHandle.selectDropdownIfValuePresent(driver, wait,By.xpath("//select[@name='CoreInterfaceModBO.FreeCode7Desc']"),
        	    getData[97]);
             
             WebElement Freezecode8 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Cat_CoreInterfaceBO.FreeCode8Desc")));
             Freezecode8.sendKeys(getData[98]);
          	
 	  }catch (Exception e) {
           System.out.println("Coreinterface tab error: " + e.getMessage());
       }
 	  ////////////////////////////////////////////////currency////////////////////////////////////////////////////////////
 	  switchToWindowAndFrame(Windowhandle, wait);
	  try {
		    
         	WebElement CurrencyTab =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//font[@id='fnttpageCurr']")));
         	CurrencyTab.click();
         	
         	WebElement addcurrency =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='Add Currency']")));
         	addcurrency.click();
         	
         	WindowHandle.handlePopupIfExists(driver);
         	
         	WebElement save =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='SAVE']")));
         	save.click();

         	
	  }catch (Exception e) {
          System.out.println("Currency tab error: " + e.getMessage());
      }
	  if (!"New".equalsIgnoreCase(getData[16])) {
	  ///////////////////////relationship details//////////////////////////////////
	  driver.switchTo().window(Windowhandle);
 	  driver.switchTo().defaultContent();
	  wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("loginFrame")));
	  wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CRMServer")));
	  wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("DataAreaFrm")));
	  wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("tempFrm")));
	  wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("tabViewFrm")));
	 
	  WebElement Relation =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//font[@id='fnttab1']")));
	  Relation.click();
		  
		  driver.switchTo().defaultContent();
		  wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("loginFrame")));
	    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CRMServer")));
	    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("DataAreaFrm")));
	    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("tempFrm")));
	    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("tabContentFrm")));
	    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("userArea")));
	    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("IFrmtab1")));
	    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("formDispFrame")));
	    	
	    	Relationshipdetails(driver, wait, false, getData);
	  }
	    	driver.switchTo().window(Windowhandle);
	    	driver.switchTo().defaultContent();
	    	
			  wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("loginFrame")));
		    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CRMServer")));
		    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("DataAreaFrm")));
		    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("tempFrm")));
		    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("buttonFrm")));
		    	
		
		  	  WebElement submit =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='submitBut']")));
		  	  submit.click();

		  	 // WindowHandle.handleAlertIfPresent(driver);
			  	
		  	try {
		  	    WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(2)); // Short wait
		  	    Alert alert = alertWait.until(ExpectedConditions.alertIsPresent());
		  	    String alertText = alert.getText();
		  	    alert.accept(); // Dismiss the alert

		  	    // Extract CIF ID using regex
		  	    Pattern pattern = Pattern.compile("CIF ID: (\\d+)");
		  	    Matcher matcher = pattern.matcher(alertText);
		  	    String cifId = null;
		  	    if (matcher.find()) {
		  	        cifId = matcher.group(1); // Extracted CIF ID
		  	    }

		  	    // Validate CIF ID
		  	    Assert.assertFalse(cifId == null || cifId.isEmpty(), "Test Failed: CIF ID is empty!");
		  	    System.out.println("CIF ID Created Successfully..! CIF ID: " + cifId);

		  	    // Write CIF ID to Excel
		  	    String excelFilePath = System.getProperty("user.dir") + "/Resource/SBData.xlsx"; // Path to your Excel file
		  	    String sheetName = "Sheet2"; // Update with your sheet name
		  	    int rowNum = 2;  // Row where you want to store
		  	    String columnName = "CIFID"; 
		  	    ExcelUtils.updateExcel(excelFilePath, sheetName, rowNum, columnName, cifId);

		  	} catch (TimeoutException ignored) {
		  	    // No alert means we can continue
		  	}
		  	  WindowHandle.handlePopupIfExists(driver);
		  	  
		  	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("tempFrm")));
	    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("tabContentFrm")));
	    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("userArea")));
	    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("IFrmtab0")));
	    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("formDispFrame")));
	    	
		    WebElement Processurl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@name='ProcessBO.url_']")));
		    WindowHandle.selectByVisibleText(Processurl,getData[107]);
		    
		    driver.switchTo().defaultContent();
		    wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("tempFrm")));
	    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("buttonFrm")));
	    	
	    	WebElement save = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='saveBut']")));
	    	save.click();
		  	  
	    	WindowHandle.handleAlertIfPresent(driver);

}
	public static void documentdetails(WebDriver driver, WebDriverWait wait, boolean isSecondAddress ,String[] getData) throws Exception {
       	    
           	WebElement AddIden =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("AddIdentificationDetails")));
           	AddIden.click();
           	
           	WindowHandle.handlePopupIfExists(driver);
            WebElement body = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
       	    // Select Doc Type Code
       	    WindowHandle.selectDropdownIfValuePresent(driver, wait,
       	        By.xpath("//select[@name='EntityDocumentBO.DocTypeCode']"),
       	        isSecondAddress ? getData[27] : getData[35]);
       	    // Select Doc Code
       	    WindowHandle.selectDropdownIfValuePresent(driver, wait,
       	        By.xpath("//select[@name='EntityDocumentBO.DocCode']"),
       	        isSecondAddress ? getData[28] : getData[36]);

       	    PageObject.waitAndFill(wait, "name", "EntityDocumentBO.ReferenceNumber", isSecondAddress? getData[31]:getData[39]);
       	    PageObject.waitAndFill(wait, "name", "EntityDocumentBO.CountryOfIssue", isSecondAddress? getData[29]:getData[37]);
       	    body.click();
       	    PageObject.waitAndFill(wait, "name", "EntityDocumentBO.PlaceOfIssue", isSecondAddress? getData[30]:getData[38]);
       	    body.click();
       	    PageObject.waitAndFill(wait, "name", "3_EntityDocumentBO.DocIssueDate", isSecondAddress? getData[32]:getData[40]);
       	    PageObject.waitAndFill(wait, "name", "3_EntityDocumentBO.DocExpiryDate", isSecondAddress? getData[33]:getData[41]);
       	    WebElement save = wait.until(ExpectedConditions.elementToBeClickable(By.name("SAVE")));
       	    save.click();
       	}
       	public static void fillAddressDetails(WebDriver driver, WebDriverWait wait, boolean isSecondAddress ,String[] getData) throws Exception {
		    if (isSecondAddress) {
		        System.out.println("=== Filling Secondary Address Details ===");
		    } else {
		        System.out.println("=== Filling Primary Address Details ===");
		    }

		    String addressType = isSecondAddress ? getData[59] : getData[45];

		    if ("Structured".equalsIgnoreCase(addressType)) {
		        System.out.println("==> Structured address detected");
		        fillStructuredAddress(driver, wait, isSecondAddress,getData);
		    } else if ("Free Text".equalsIgnoreCase(addressType)) {
		        System.out.println("==> FreeText address detected");
		        fillFreeTextAddress(driver, wait, isSecondAddress,getData);
		    } else {
		        System.out.println("Unknown AddressType: " + addressType);
		    }
		}

		private static void fillStructuredAddress(WebDriver driver, WebDriverWait wait, boolean isSecondAddress ,String[] getData) throws Exception {
		    Actions actions = new Actions(driver);
		    WebElement body = driver.findElement(By.tagName("body"));

		    if (isSecondAddress) {
		        System.out.println("==> Filling Secondary Structured Address");

		        selectAddressTypeIfPresent(wait,true,getData); // picks secondary address type

		        PageObject.waitAndFill(wait, "name", "CorporateBO.Address.house_no",getData[61]);
		        PageObject.waitAndFill(wait, "name", "CorporateBO.Address.street_no", getData[62]);
		        PageObject.waitAndFill(wait, "name", "CorporateBO.Address.street_name", getData[63]);
		        PageObject.waitAndFill(wait, "name", "Cat_CorporateBO.Address.city", getData[64]);
		        PageObject.waitAndFill(wait, "name", "Cat_CorporateBO.Address.state", getData[65]);
		        PageObject.waitAndFill(wait, "name", "CorporateBO.Address.zip", getData[67]);
		        PageObject.waitAndFill(wait, "name", "CorporateBO.Address.country",getData[66]);
		    } else {
		        System.out.println("==> Filling Primary Structured Address");

		        selectAddressTypeIfPresent(wait,false,getData);// picks primary address type

		        PageObject.waitAndFill(wait, "name", "CorporateBO.Address.house_no", getData[47]);
		        PageObject.waitAndFill(wait, "name", "CorporateBO.Address.street_no", getData[48]);
		        PageObject.waitAndFill(wait, "name", "CorporateBO.Address.street_name", getData[49]);
		        PageObject.waitAndFill(wait, "name", "Cat_CorporateBO.Address.city", getData[50]);
		        PageObject.waitAndFill(wait, "name", "Cat_CorporateBO.Address.state", getData[51]);
		        PageObject.waitAndFill(wait, "name", "CorporateBO.Address.zip", getData[53]);
		        PageObject. waitAndFill(wait, "name", "CorporateBO.Address.country", getData[52]);
		    }

		    actions.moveToElement(body, 0, 0).click().perform(); // shift focus
		}

		private static void fillFreeTextAddress(WebDriver driver, WebDriverWait wait, boolean isSecondAddress,String[] getData) {
		    WebElement PreferredFormat = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("CorporateBO.Address.PreferredFormat")));
		    WindowHandle.selectByVisibleText(PreferredFormat,isSecondAddress?getData[59]:getData[45]);
		    System.out.println(getData[59]);
		    WindowHandle.handleAlertIfPresent(driver);

		    if (isSecondAddress) {
		    	selectAddressTypeIfPresent(wait, true,getData);
		        WindowHandle.slowDown(2);
		        PageObject.waitAndFill(wait, "name", "CorporateBO.Address.FreeTextLabel", getData[69]);
		        PageObject.waitAndFill(wait, "name", "CorporateBO.Address.Address_Line1", getData[70]);
		        PageObject.waitAndFill(wait, "name", "Cat_CorporateBO.Address.city",getData[64]);
		        PageObject.waitAndFill(wait, "name", "Cat_CorporateBO.Address.state",getData[65]);
		        PageObject.waitAndFill(wait, "name", "CorporateBO.Address.country",getData[66]);
		        PageObject.waitAndFill(wait, "name", "CorporateBO.Address.zip", getData[67]);

		        System.out.println("Filled Secondary Free Text Address.");
		    } else {
		    	selectAddressTypeIfPresent(wait,false , getData);
		    	PageObject.waitAndFill(wait, "name", "CorporateBO.Address.FreeTextLabel", getData[55]);
		    	PageObject.waitAndFill(wait, "name", "CorporateBO.Address.Address_Line1", getData[56]);
		    	PageObject.waitAndFill(wait, "name", "Cat_CorporateBO.Address.city", getData[50]);
		    	PageObject.waitAndFill(wait, "name", "Cat_CorporateBO.Address.state",getData[51]);
		    	PageObject.waitAndFill(wait, "name", "CorporateBO.Address.country", getData[52]);
		    	PageObject.waitAndFill(wait, "name", "CorporateBO.Address.zip", getData[53]);

		        System.out.println("Filled Primary Free Text Address.");
		    }
		}
		
		public static void selectAddressTypeIfPresent(WebDriverWait wait, boolean isSecondRecord ,String[] getData) {
		    try {
		        String preferredFormat = isSecondRecord ? getData[60]: getData[46];

		        // Only proceed if it's NOT null, NOT empty, and NOT "Mailing"
		        if (preferredFormat != null && !preferredFormat.trim().isEmpty()
		                && !preferredFormat.trim().equalsIgnoreCase("Registered")) {

		            WebElement formatDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(
		                    By.name("CorporateBO.Address.addressCategory")));
		            Select select = new Select(formatDropdown);
		            select.selectByVisibleText(preferredFormat.trim());

		            System.out.println((isSecondRecord ? "Second" : "Primary") +
		                    " Preferred Format set to: " + preferredFormat);
		        } else {
		            System.out.println((isSecondRecord ? "Second" : "Primary") +
		                    " Preferred Format skipped (default 'Registered' or empty in Excel)");
		        }
		    } catch (Exception e) {
		        System.out.println("Error setting " + (isSecondRecord ? "Second" : "Primary") +
		                " Preferred Format: " + e.getMessage());
		    }
		}
		private static void fillContactDetails	(WebDriver driver, WebDriverWait wait, boolean isSecondAddress ,String[] getData) throws Exception {

			WindowHandle.selectDropdownIfValuePresent(driver, wait, By.name("CorporateBO.PhoneEmail.PhoneEmailType"),
			isSecondAddress ? getData[84] : getData[73]);
					
			WindowHandle.selectDropdownIfValuePresent(driver,wait, By.name("CorporateBO.PhoneEmail.PhoneEmailType1"),
			isSecondAddress ? getData[86] : getData[75]);

		    WebElement Add = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='ADD_PHONE_EMAIL']")));
		    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", Add);

		    WindowHandle.handlePopupIfExists(driver);
		    try {
		    	String selectedContact = isSecondAddress ? getData[87] : getData[76];
		    	if (selectedContact != null) {
		    	    // If the record is "Phone" and it's the Primary (first) record
		    	    if (selectedContact.trim().equalsIgnoreCase("Phone") && !isSecondAddress) {
		    	        System.out.println("📞 [INFO] Entering Primary (Phone) Block for " + getData[76]);
		    	        
		    	        // Handle Primary Phone
		    	        selectPhoneNEmailIfPresent(wait,false , getData ); // Primary (Phone)
		    	        
		    	        // Fill in Phone fields for Primary record
		    	        WebElement PhoneNEmailType = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("CorporateBO.PhoneEmail.PhoneEmailType")));
			            WindowHandle.selectByVisibleText(PhoneNEmailType,getData[77]);
			            PageObject.waitAndFill(wait, "name", "CorporateBO.PhoneEmail.PhoneNo.cntrycode", getData[78]);
			            PageObject.waitAndFill(wait, "name", "CorporateBO.PhoneEmail.PhoneNo.areacode", getData[79]);
			            PageObject.waitAndFill(wait, "name", "CorporateBO.PhoneEmail.PhoneNo.localcode", getData[80]);
		    	        
		    	    } 
		    	    // If the record is "Phone" and it's the Secondary (second) record
		    	    else if (selectedContact.trim().equalsIgnoreCase("Phone") && isSecondAddress) {
		    	        System.out.println("📞 [INFO] Entering Secondary (Phone) Block for " + getData[87]);
		    	        
		    	        // Handle Secondary Phone
		    	        selectPhoneNEmailIfPresent(wait,true,getData); // Secondary (Phone)
		    	        
		    	        // Fill in Phone fields for Secondary record
		    	        WebElement PhoneType = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("CorporateBO.PhoneEmail.PhoneEmailType")));
			            WindowHandle.selectByVisibleText(PhoneType,getData[88]);
			            PageObject.waitAndFill(wait, "name", "CorporateBO.PhoneEmail.PhoneNo.cntrycode", getData[89]);
			            PageObject.waitAndFill(wait, "name", "CorporateBO.PhoneEmail.PhoneNo.areacode", getData[90]);
			            PageObject.waitAndFill(wait, "name", "CorporateBO.PhoneEmail.PhoneNo.localcode", getData[91]);
		    	        
		    	    }
		    	    // If the record is "E-mail" and it's the Primary (first) record
		    	    else if (selectedContact.trim().equalsIgnoreCase("E-mail") && !isSecondAddress) {
		    	        System.out.println("✉️ [INFO] Entering Primary (E-mail) Block for " + getData[76]);
		    	        
		    	        // Handle Primary Email
		    	        selectPhoneNEmailIfPresent(wait,false,getData); // Primary (E-mail)
		    	        
		    	        // Fill in Email fields for Primary record
		    	        PageObject.waitAndFill(wait, "name", "CorporateBO.PhoneEmail.Email", getData[81]);
		    	        WebElement EmailType = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("CorporateBO.PhoneEmail.PhoneEmailType1")));
			            WindowHandle.selectByVisibleText(EmailType,getData[82]);
		    	        
		    	    } 
		    	    // If the record is "E-mail" and it's the Secondary (second) record
		    	    else if (selectedContact.trim().equalsIgnoreCase("E-mail") && isSecondAddress) {
		    	        System.out.println("✉️ [INFO] Entering Secondary (E-mail) Block for " + getData[76]);
		    	        
		    	        // Handle Secondary Email
		    	        selectPhoneNEmailIfPresent(wait,true ,getData); // Secondary (E-mail)
		    	        
		    	        // Fill in Email fields for Secondary record
		    	        PageObject.waitAndFill(wait, "name", "CorporateBO.PhoneEmail.Email",getData[92]);
		    	        WebElement EmailType = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("CorporateBO.PhoneEmail.PhoneEmailType1")));
			            WindowHandle.selectByVisibleText(EmailType,getData[93]);
		    	        
		    	    } 
		    	    // If the record type is neither Phone nor E-mail
		    	    else {
		    	        System.out.println("⚠️ No valid contact type found. Value was: '" + selectedContact + "'");
		    	    }
		    	}
	        
            WebElement save = wait.until(ExpectedConditions.elementToBeClickable(By.name("Save")));
            save.click();
		    } catch (Exception e) {
		    	System.out.println("Error setting FirstAddress Formate: " + e.getMessage());
		    }
		}

		    
		public static void selectPhoneNEmailIfPresent(WebDriverWait wait, boolean isSecondRecord ,String[] getData) {
		    try {
		        String format = isSecondRecord ? getData[87] : getData[76];
		        if (format != null && !format.trim().isEmpty() && !format.equalsIgnoreCase("Phone")) {
		            Select select = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(
		                By.name("CorporateBO.PhoneEmail.PhoneOrEmail"))));
		            select.selectByVisibleText(format.trim());
		            System.out.println((isSecondRecord ? "Second" : "Primary") + " Preferred Format set to: " + format);
		        } else {
		            System.out.println((isSecondRecord ? "Second" : "Primary") + " Preferred Format skipped.");
		        }
		    } catch (Exception e) {
		        System.out.println("Error setting Preferred Format: " + e.getMessage());
		    }
		}

//		private static void Relationshipdetails	(WebDriver driver, WebDriverWait wait, boolean isSecondAddress ,String[] getData) throws Exception {
//
//			
//			WindowHandle.selectDropdownIfValuePresent(driver, wait, By.name("CorporateBO.CorpMiscellaneousInfo.str3"),
//			isSecondAddress ? getData[84] : getData[101]);
//		    	
//		  	WebElement addRel =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='Add Relationship']")));
//		  	addRel.click();
//
//		    WindowHandle.handlePopupIfExists(driver);
//		   try {
//			   WindowHandle.selectDropdownIfValuePresent(driver, wait, By.name("BeneficialOwnerBO.cifType"),
//			   isSecondAddress ? getData[84] : getData[102]);
//			   WindowHandle.selectDropdownIfValuePresent(driver, wait, By.name("BeneficialOwnerBO.entity_Type"),
//			   isSecondAddress ? getData[84] : getData[103]);
//			   waitAndFill(wait, "name", "3_BeneficialOwnerBO.percentageBenefited",isSecondAddress ? getData[84] : getData[104]);
//			   
//			   WebElement lastname =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("btnone_BeneficialOwnerBO.last_Name")));
//			   lastname.click();
//			   
//			   Set<String> allWindows = driver.getWindowHandles();
//			   for (String handle : allWindows) {
//			       driver.switchTo().window(handle);
//			   }
//			   driver.switchTo().defaultContent(); // Always reset before switching frames
//
//			   
//			   try {
//		            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("SearchOptions")));
//		            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("tabContentFrm")));
//		            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("userArea")));
//		            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("IFrmtab0")));
//
//					   waitAndFill(wait, "name", "FilterParam1",isSecondAddress ? getData[84] : getData[105]);
//					   WebElement submit =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Submit")));
//					   submit.click();
//
//		        } catch (Exception e) {
//		        	System.out.println("Lastname Error: " + e.getMessage());
//		        }
//
//			   
//			// 2. Switch to the 'SearchResult' frame
//			   driver.switchTo().defaultContent(); // Always reset before switching frames
//			   driver.switchTo().frame("SearchResult");
//			   String xpath = "//tr[td[contains(text(),'" + itemToClick + "')]]";
//
//			   try {
//			       WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
//
//			       // Scroll into view
//			       ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", row);
//
//			       // Try Actions double-click
//			       Actions actions = new Actions(driver);
//			       actions.moveToElement(row).doubleClick().perform();
//
//			   } catch (Exception e) {
//			       System.out.println("Actions.doubleClick failed. Trying JavaScript fallback...");
//
//			       try {
//			           WebElement row = driver.findElement(By.xpath(xpath)); // Re-fetch in case stale
//			           ((JavascriptExecutor) driver).executeScript(
//			               "var evt = new MouseEvent('dblclick', {bubbles:true, cancelable:true}); arguments[0].dispatchEvent(evt);",
//			               row
//			           );
//			       } catch (Exception jsEx) {
//			           System.out.println("JavaScript fallback failed: " + jsEx.getMessage());
//			       }
//			   }
//
////		       
//				String Windowhandle = driver.getWindowHandle();
//				driver.switchTo().window(Windowhandle);
//	       	    driver.switchTo().defaultContent();
//	       	    WindowHandle.goToMainFrame(wait); 
//				
//			   
////			   waitAndFill(wait, "name", "BeneficialOwnerBO.cifType",isSecondAddress ? getData[84] : getData[97]);
////			   waitAndFill(wait, "name", "BeneficialOwnerBO.cifType",isSecondAddress ? getData[84] : getData[97]);
////			   waitAndFill(wait, "name", "BeneficialOwnerBO.cifType",isSecondAddress ? getData[84] : getData[97]);
////			   
////            WebElement save = wait.until(ExpectedConditions.elementToBeClickable(By.name("Save")));
////            save.click();
//            
//		    } catch (Exception e) {
//		    	System.out.println("Error setting FirstAddress Formate: " + e.getMessage());
//		    }
//		}
		
		private static void Relationshipdetails(WebDriver driver, WebDriverWait wait, boolean isSecondAddress, String[] getData) throws Exception {

		    // Store main window before clicking "Add Relationship"
		    String parentWindow = driver.getWindowHandle();

		    // Step 1: Select from dropdown
		    WindowHandle.selectDropdownIfValuePresent(driver, wait,
		        By.name("CorporateBO.CorpMiscellaneousInfo.str3"),
		        isSecondAddress ? getData[84] : getData[101]);

		    // Step 2: Click Add Relationship (opens second window)
		    WebElement addRel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='Add Relationship']")));
		    addRel.click();

		    // Step 3: Wait and get second window handle
		    WindowHandle.handlePopupIfExists(driver);
		    Set<String> handlesAfterAddRel = driver.getWindowHandles();
		    String secondWindowHandle = handlesAfterAddRel.stream()
		        .filter(handle -> !handle.equals(parentWindow))
		        .findFirst().orElseThrow(() -> new RuntimeException("Second window not found"));

		    driver.switchTo().window(secondWindowHandle);
		    driver.switchTo().defaultContent();

		    // Continue with dropdowns
		    WindowHandle.selectDropdownIfValuePresent(driver, wait, By.name("FinancialBO.FinancialDetails.cifType"),
		        isSecondAddress ? getData[84] : getData[102]);
		    WindowHandle.selectDropdownIfValuePresent(driver, wait, By.name("FinancialBO.FinancialDetails.entityType"),
		        isSecondAddress ? getData[84] : getData[103]);
		    WindowHandle.selectDropdownIfValuePresent(driver, wait, By.name("FinancialBO.FinancialDetails.str3"),
		        isSecondAddress ? getData[84] : getData[104]);

		    // Open third window
		    WebElement lastname = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("btnone_shareholderName")));
		    lastname.click();

		    // Capture third window handle
		    Set<String> handlesAfterLastName = driver.getWindowHandles();
		    String thirdWindowHandle = handlesAfterLastName.stream()
		        .filter(handle -> !handle.equals(parentWindow) && !handle.equals(secondWindowHandle))
		        .findFirst().orElseThrow(() -> new RuntimeException("Third window not found"));

		    // Switch to third window and perform actions
		    driver.switchTo().window(thirdWindowHandle);
		    driver.switchTo().defaultContent();

		    try {
		        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("SearchOptions")));
		        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("tabContentFrm")));
		        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("userArea")));
		        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("IFrmtab0")));

		        PageObject.waitAndFill(wait, "name", "FilterParam1", isSecondAddress ? getData[84] : getData[105]);
		        WebElement submit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Submit")));
		        submit.click();

		    } catch (Exception e) {
		        System.out.println("Lastname Error: " + e.getMessage());
		    }

		    // Handle result table
		    driver.switchTo().defaultContent();
		    wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("SearchResult")));

		    WebElement en_US = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//font[@class='fntColData']")));
		    new Actions(driver).doubleClick(en_US).perform();

		    // Close third window and switch back to second
		    WindowHandle.slowDown(2);
		    driver.switchTo().window(secondWindowHandle);
		    driver.switchTo().defaultContent();
		    WindowHandle.goToMainFrame(wait);
		    System.out.println("Switched back to second window: " + driver.getWindowHandle());

		    // Now click Save
		    WebElement save = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='Save']")));
		    save.click();
		}

	   	public static void switchToWindowAndFrame(String windowHandle, WebDriverWait wait) {
	   		
			driver.switchTo().window(windowHandle);
	   	    driver.switchTo().defaultContent();
	   	    WindowHandle.goToMainFrame(wait); 
	   	}

	   }
