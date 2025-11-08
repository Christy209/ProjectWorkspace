	package CRM;
	
	import java.io.IOException;
	import java.time.Duration;
	import org.openqa.selenium.By;
	import org.openqa.selenium.JavascriptExecutor;
	import org.openqa.selenium.WebDriver;
	import org.openqa.selenium.WebElement;
	import org.openqa.selenium.interactions.Actions;
	import org.openqa.selenium.support.ui.ExpectedConditions;
	import org.openqa.selenium.support.ui.Select;
	import org.openqa.selenium.support.ui.WebDriverWait;
	import org.testng.annotations.BeforeClass;
	import org.testng.annotations.Test;
	import Base.BaseTest;
	import Base.DriverManager;
	import Utilities.Inputs;
	import Utilities.WindowHandle;
	
	public class CreateRetailCIFID extends BaseTest {
		protected WebDriver driver;
	    protected WebDriverWait wait;
	    
	    @BeforeClass
			 public void setup() throws IOException { 
			        driver = DriverManager.getDriver();
			        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			        String userID = DriverManager.getProperty("userid");
		            String password = DriverManager.getProperty("password");
	
		            // Login with first user
		            DriverManager.login(userID, password);
		            System.out.println("Executing TC01: Savings Account Creation for User: " + userID);
			    }
		   @Test(priority = 1)
			//@Test(priority = 1, groups = "createAccount")
		   	public void createCustomerID() throws Exception {
			   
			   	Inputs cifInput = new Inputs(1, "Sheet2");
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
		        WindowHandle.slowDown(1);
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
	
	            	WebElement Newentity = wait.until(ExpectedConditions.elementToBeClickable(By.name("spanFor4")));
	            	Newentity.click();
	            	
	            	WebElement Customer = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@id='subviewspanFor41']")));
	            	Customer.click();
	
		}catch(Exception e) {
	        System.out.println("Frame not found: " + e.getMessage());
		}
	            
	            driver.switchTo().defaultContent();
	            
	            try{
	            	WindowHandle.goToMainFrame(wait);
	
	            	WebElement gender = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@name='AccountModBO.Gender']")));
	            	WindowHandle.selectByVisibleText(gender, cifInput.getGender());
	
	            	WebElement titleCodeBox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//input[@name='AccountModBO.Salutation_code'])[1]")));
	            	titleCodeBox.sendKeys(cifInput.getTitle()); 
	            	WebElement body = driver.findElement(By.tagName("body"));
	                Actions actions = new Actions(driver);
            	actions.moveToElement(body, 0, 0).click().perform(); 
	                
	                
	                WebElement Firstname = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='AccountBO.Cust_First_Name']")));
	                Firstname.sendKeys(cifInput.getFirstName());

	            	WindowHandle.slowDown(2);
	            	WebElement Middlename = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='AccountBO.Cust_Middle_Name']")));
	            	Middlename.sendKeys(cifInput.getMiddleName());

	            	WindowHandle.slowDown(2);
	            	WebElement Lastname = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='AccountBO.Cust_Last_Name']")));
	            	Lastname.sendKeys(cifInput.getLastName());
	            	
	            	WindowHandle.slowDown(2);
	            	WebElement Shortname = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='AccountBO.short_name']")));
	            	Shortname.sendKeys(cifInput.getShortName());
	
	            	WindowHandle.slowDown(2);
	            	WebElement Preferredname = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='AccountBO.PreferredName']")));
	            	Preferredname.sendKeys(cifInput.getPreferredName());
	            	
	            	WindowHandle.slowDown(2);
	            	WebElement DOB = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='3_AccountBO.Cust_DOB']")));
	            	DOB.sendKeys(cifInput.getDateofBirth());
	            	
	            	WindowHandle.slowDown(2);
	            	WebElement NonResInd = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@name='AccountModBO.CustomerNREFlg']")));
	            	WindowHandle.selectByVisibleText(NonResInd, cifInput.getNonresidentIndicator());
	
	            	WebElement CustType = wait.until(ExpectedConditions.elementToBeClickable(By.name("Cat_AccountBO.Cust_type_code")));
	            	CustType.sendKeys(cifInput.getCustomerType());
	            	
	            	WindowHandle.slowDown(2);
	            	WebElement seg = wait.until(ExpectedConditions.elementToBeClickable(By.name("AccountModBO.Segmentation_Class")));
	            	seg.sendKeys(cifInput.getSegment()); 
	                actions.moveToElement(body, 0, 0).click().perform(); 
	                
	                WindowHandle.slowDown(2);
	            	WebElement PsolId = wait.until(ExpectedConditions.elementToBeClickable(By.name("AccountBO.Primary_sol_id")));
	            	PsolId.sendKeys(cifInput.getPrimarySOLID()); 
	            	actions.moveToElement(body, 0, 0).click().perform();
	                
	            	WindowHandle.slowDown(2);
	            	WebElement SubSeg = driver.findElement(By.xpath("//select[@name='AccountModBO.SubSegment']"));
	            	WindowHandle.selectByVisibleText(SubSeg, cifInput.getSubsegment());
	                
	            	WindowHandle.slowDown(2);
	            	WebElement CrefCode = wait.until(ExpectedConditions.elementToBeClickable(By.name("AccountModBO.Constitution_ref_code")));
	            	CrefCode.sendKeys(cifInput.getConstitutionCode()); 
	            	actions.moveToElement(body, 0, 0).click().perform();
	                
	            	WindowHandle.slowDown(2);
	            	WebElement TDSC = wait.until(ExpectedConditions.elementToBeClickable(By.name("AccountModBO.Tds_tbl")));
	            	TDSC.sendKeys(cifInput.getTaxDeducatSouTable()); 
	            	actions.moveToElement(body, 0, 0).click().perform();
	                
	            	WindowHandle.slowDown(2);
	            	WebElement Crating = wait.until(ExpectedConditions.elementToBeClickable(By.name("AccountModBO.Rating")));
	            	Crating.sendKeys(cifInput.getCustomerRating()); 
	            	actions.moveToElement(body, 0, 0).click().perform();
	                
	            	WindowHandle.slowDown(2);
	            	WebElement Region = wait.until(ExpectedConditions.elementToBeClickable(By.name("AccountModBO.region")));
	            	Region.sendKeys(cifInput.getRegion()); 
	            	actions.moveToElement(body, 0, 0).click().perform();

	            	WindowHandle.slowDown(2);
	            	WebElement Ealert = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@name='AccountModBO.Enable_Alerts']")));
	            	WindowHandle.selectByVisibleText(Ealert, cifInput.getEnableCRMAlerts());
	
	            	WindowHandle.slowDown(2);
	             	WebElement Manager = wait.until(ExpectedConditions.elementToBeClickable(By.name("Acc_manager")));
	               	Manager.sendKeys(cifInput.getPrimyRelshpMangID());
	               	
	               	WindowHandle.slowDown(2);
	            	WebElement Custlan = wait.until(ExpectedConditions.elementToBeClickable(By.name("Cat_AccountModBO.Cust_Language")));
	            	Custlan.sendKeys(cifInput.getPrfredNtieLnguge()); 
	                actions.moveToElement(body, 0, 0).click().perform(); // C
	                
	                WindowHandle.slowDown(2);
	                WebElement NLCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@name='AccountModBO.NativeLangCode']")));
	                WindowHandle.selectByVisibleText(NLCode, cifInput.getNativeLngugeCde());
	            	
		}catch(Exception e) {
	        System.out.println("Frame not found: " + e.getMessage());  
		} 
	    		
	    	 WebElement plang = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("btnone_PsychographicBO.Preferred_Locale")));
	     	 plang.click();
	     	
	     	WindowHandle.handlePopupIfExists(driver);
	    	        try {
	
	    	        	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("SearchOptions")));
	    	        	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("tabContentFrm")));
	    	        	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("userArea")));
	    	        	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("IFrmtab0")));
	
	    	         WebElement submitButton1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Submit")));
	    	         submitButton1.click();   
	    	         
	    	         driver.switchTo().defaultContent();
	 	        	 wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("SearchResult")));
	
	    	         WebElement en_US = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//font[@class='fntColData']")));
	    	         Actions actions = new Actions(driver);
	    	         actions.doubleClick(en_US).perform();
	    	         
	
	    	        }catch(Exception e) {
	                    System.out.println("Searcher box: " + e.getMessage());
	            	}

	////////////////////////////////////////////address details//////////////////////////////////////////////////////////////////////////////
	                switchToWindowAndFrame(Windowhandle, wait);
	
	            	WebElement secondtab =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fnttpageCont3")));
	            	secondtab.click();
	            	
//	              	WebElement preferredAddress =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@name='AccountBO.Address.preferredAddress']")));
//	              	WindowHandle.selectByVisibleText(preferredAddress, cifInput.getpreferredAddress());
	            	WindowHandle.selectDropdownIfValuePresent(driver, wait, By.xpath("//select[@name='AccountBO.Address.preferredAddress']"),
	            			cifInput.getpreferredAddress());
	              	
	            	
	            	WebElement AddressMail =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='Add Address Details']")));
	            	AddressMail.click();
	            	
	            	///////////////////////////First Address/////////////////////////////////////////////////////////////
	            	try {
	            	    WindowHandle.handlePopupIfExists(driver);
	            	    fillAddressDetails(driver, wait, cifInput, false);

	            	    WebElement save = wait.until(ExpectedConditions.elementToBeClickable(By.name("Save")));
	            	    save.click();
	            	    WindowHandle.handleAlertIfPresent(driver);

	            	    	if ("Y".equalsIgnoreCase(cifInput.getSecondAddressFlg())) {

            	    	    switchToWindowAndFrame(Windowhandle, wait);

            	    	    WebElement preferredAddress2 =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@name='AccountBO.Address.preferredAddress']")));
        	              	WindowHandle.selectByVisibleText(preferredAddress2, cifInput.getS_preferredAddress());
        	              	
	            	        WebElement AddressMail2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Add Address Details")));
	            	        AddressMail2.click();

	            	        WindowHandle.handlePopupIfExists(driver);

	            	        fillAddressDetails(driver, wait, cifInput, true);

	            	        WebElement saveSecondary = wait.until(ExpectedConditions.elementToBeClickable(By.name("Save")));
	            	        saveSecondary.click();
	            	        WindowHandle.handleAlertIfPresent(driver);

	            	        driver.switchTo().window(Windowhandle);
	            	        System.out.println("Switched back to previous window: " + driver.getCurrentUrl());

	            	    } else {
	            	        System.out.println("Second address not required.");
	            	    }
	            	    	///////////////////////////////delete///////////////////////////////////////
	            	    	if ("Y".equalsIgnoreCase(cifInput.getDelect())) {
	            	    	    String addressToDelete = cifInput.getAddressToDelete(); // from Excel
	            	    	    switchToWindowAndFrame(Windowhandle, wait);
	            	    	    
	            	    	    String xpath = "//table[@id='table0']//tr[contains(.,'" + addressToDelete + "')]//font[contains(text(),'" + addressToDelete + "')]";
	            	    	    WebElement addressCell = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
	            	    	    js.executeScript("arguments[0].scrollIntoView(true);", addressCell); // Ensure it's in view
	            	    	    addressCell.click();

	            	    	    WebElement deleteBtn = wait.until(ExpectedConditions.elementToBeClickable(By.name("Delete Address Details")));
	            	    	    deleteBtn.click();
	            	    	    WindowHandle.handleAlertIfPresent(driver);
	
	            	    	} else {
	            	    	    System.out.println("No delete operation needed.");
	            	    	}

	            	} catch (Exception e) {
	            	    System.out.println("SecondAddressDetails tab error " + e.getMessage());
	            	}


	/////////////////////////////////////////////////////////phone details////////////////////////////////////////////////////////////////////////////
	            driver.switchTo().defaultContent(); 
	            
	           try {  
	                
	            	WindowHandle.goToMainFrame(wait);
	
	            	WebElement PhoneDTab =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fnttpagePhone")));
	            	PhoneDTab.click();
	                
	            	fillContactDetails(driver, wait, cifInput, false); 

	                if ("Y".equalsIgnoreCase(cifInput.getSecondPhoneNEnamiFlg())) {
	                	switchToWindowAndFrame(Windowhandle, wait);

        	    	    fillContactDetails(driver, wait, cifInput, true);

	                }else {
	                	System.out.println("SKipping there is no SecordDetails flg as Y");
	                }
	                if ("Y".equalsIgnoreCase(cifInput.getPNEDelect())) {

        	    	    String addressToDelete = cifInput.getPNEDelectToBe(); // from Excel
        	    	    
        	    	    switchToWindowAndFrame(Windowhandle, wait);
        	    	    String xpath = "//table[@id='table0']//tr[contains(.,'" + addressToDelete + "')]//font[contains(text(),'" + addressToDelete + "')]";
        	    	    WebElement addressCell = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        	    	    js.executeScript("arguments[0].scrollIntoView(true);", addressCell); // Ensure it's in view
        	    	    addressCell.click();

        	    	    WebElement deleteBtn = wait.until(ExpectedConditions.elementToBeClickable(By.name("Delete Phone and E-mail")));
        	    	    deleteBtn.click();
        	    	    WindowHandle.handleAlertIfPresent(driver);

        	    	} else {
        	    	    System.out.println("No delete operation needed.");
        	    	}

	                
	            } catch (Exception e) {
	                System.out.println("Phone and email error: " + e.getMessage());
	            }
	            /////////////////////////////////document details///////////////////////////////////////////
	           switchToWindowAndFrame(Windowhandle, wait);
	           try { 
	 
	           	WebElement DocumentTab =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fnttpageCont5")));
	           	DocumentTab.click();
	           	
	           	documentdetails(driver, wait, false, cifInput);
	           	
	           	if ("Y".equalsIgnoreCase(cifInput.getSecdocflg())) {
	           		switchToWindowAndFrame(Windowhandle, wait);
    	    	    
    	    	    documentdetails(driver, wait, true, cifInput);
	           
	           	}
	            if ("Y".equalsIgnoreCase(cifInput.getDelectIdenty())) {
	            	
    	    	    String addressToDelete = cifInput.getDelectIdentyToBe(); // from Excel
    	    	    
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

	    	  ///////////////////////////////////////////////////////////////core Interface tab//////////////////////////////////////////////
	    	  
	           switchToWindowAndFrame(Windowhandle, wait);
	    	  try {
	    		    
	             	WebElement CoreInterfacetab =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//font[@id='fnttpageCore']")));
	             	CoreInterfacetab.click();
	             	
	           	    WindowHandle.selectDropdownIfValuePresent(driver, wait,By.xpath("//select[@name='CoreInterfaceModBO.FreeCode6Desc']"),
	               	cifInput.getFreCode6());

	           	    WindowHandle.selectDropdownIfValuePresent(driver, wait,By.xpath("//select[@name='CoreInterfaceModBO.FreeCode7Desc']"),
	 	            cifInput.getFreCode7());
	                
	                WebElement Freezecode8 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Cat_CoreInterfaceBO.FreeCode8Desc")));
	                Freezecode8.sendKeys(cifInput.getFreCode8());
	             	
	    	  }catch (Exception e) {
	              System.out.println("Coreinterface tab error: " + e.getMessage());
	          }
	    	  
	    	  driver.switchTo().defaultContent();
	    	  wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("loginFrame")));
	    	  wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CRMServer")));
	    	  wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("DataAreaFrm")));
	    	  wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("tempFrm")));
	    	  wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("tabViewFrm")));
	    	 
	    	  WebElement demograpic =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@id='tab1']")));
	    		  demograpic.click();
	    		  
	    		  driver.switchTo().defaultContent();
	    		  
	    		  	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("loginFrame")));
	    	    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CRMServer")));
	    	    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("DataAreaFrm")));
	    	    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("tempFrm")));
	    	    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("tabContentFrm")));
	    	    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("userArea")));
	    	    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("IFrmtab1")));
	    	    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("formDispFrame")));
	    		  
	    			  
	    		  try{
	    		  WebElement Nationality =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr[2]/td[2]/input[2]")));
	    		  Nationality.sendKeys(cifInput.getNationality());

	    		  WebElement body = driver.findElement(By.tagName("body"));
	    		  body.click();

	    		  WindowHandle.selectDropdownIfValuePresent(driver, wait,By.xpath("//select[@name='DemographicModBO.Marital_Status']"),
	  	          cifInput.getMaritalStatus());
	    		  
	    	  }catch (Exception e) {
	              System.out.println("Demograpic tab error: " + e.getMessage());
	          }
	  ///////////////////////////////////////demograpic Employee details//////////////////////////////////////////////////////////
	    		  
	    		  try {
	    	    	  WebElement demoEmDetails =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//font[@id='fnttpageEDet']")));
	    	    	  demoEmDetails.click();
	    	    	  
	    	    	  WebElement occuption =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='Cat_DemographicModBO.MiscellaneousInfo.StrText2_code']")));
	    	    	  occuption.sendKeys(cifInput.getoccuption());
	    	    	  
	    	    	  WebElement demoIntax =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@id='tab_tpageIExp']")));
	    	    	  demoIntax.click();
	    	    	  
	    	    	  WebElement crossincome =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("3_DemographicBO.Annual_Salary_Income")));
	    	    	  crossincome.sendKeys(cifInput.getcrossincome());
	    	    	  WebElement body = driver.findElement(By.tagName("body"));
	                  body.click();
	    			  
	    		  }catch (Exception e) {
	                  System.out.println("Demograpic tab error: " + e.getMessage());
	              }
	    		  
	    		  driver.switchTo().defaultContent();
	    		  
	    		  wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("loginFrame")));
	    		  wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CRMServer")));
	    		  wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("DataAreaFrm")));
	    		  wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("tempFrm")));
	    		  wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("buttonFrm")));
	
		    	  try{
		    		  WebElement Submitbutton =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='submitBut']")));
		    		  Submitbutton.click();
		    		  WindowHandle.slowDown(2);
		    		  WindowHandle.handleAlertIfPresent(driver);
		    		  WindowHandle.handlePopupIfExists(driver);
	  
		    	  }catch (Exception e) {
	                  System.out.println("FinalSubmit error: " + e.getMessage());
	              }
	
		}  

		public static void fillAddressDetails(WebDriver driver, WebDriverWait wait, Inputs cifInput, boolean isSecondAddress) throws Exception {
		    if (isSecondAddress) {
		        System.out.println("=== Filling Secondary Address Details ===");
		    } else {
		        System.out.println("=== Filling Primary Address Details ===");
		    }

		    String addressType = isSecondAddress ? cifInput.getS_AddressFormat() : cifInput.getAddressFormat();

		    if ("Structured".equalsIgnoreCase(addressType)) {
		        System.out.println("==> Structured address detected");
		        fillStructuredAddress(driver, wait, cifInput, isSecondAddress);
		    } else if ("Free Text".equalsIgnoreCase(addressType)) {
		        System.out.println("==> FreeText address detected");
		        fillFreeTextAddress(driver, wait, cifInput, isSecondAddress);
		    } else {
		        System.out.println("Unknown AddressType: " + addressType);
		    }
		}

		private static void fillStructuredAddress(WebDriver driver, WebDriverWait wait, Inputs cifInput, boolean isSecondAddress) throws Exception {
		    Actions actions = new Actions(driver);
		    WebElement body = driver.findElement(By.tagName("body"));

		    if (isSecondAddress) {
		        System.out.println("==> Filling Secondary Structured Address");

		        selectAddressTypeIfPresent(wait, cifInput, true); // picks secondary address type

		        waitAndFill(wait, "name", "AccountBO.Address.house_no", cifInput.getS_HouseNo());
		        waitAndFill(wait, "name", "AccountBO.Address.street_no", cifInput.getS_StreetNo());
		        waitAndFill(wait, "name", "AccountBO.Address.street_name", cifInput.getS_StreetName());
		        waitAndFill(wait, "name", "Cat_AccountBO.Address.city", cifInput.getS_City());
		        waitAndFill(wait, "name", "Cat_AccountBO.Address.state", cifInput.getS_State());
		        waitAndFill(wait, "name", "AccountBO.Address.zip", cifInput.getS_PostalCode());
		        waitAndFill(wait, "name", "AccountBO.Address.country", cifInput.getS_Country());
		    } else {
		        System.out.println("==> Filling Primary Structured Address");

		        selectAddressTypeIfPresent(wait, cifInput, false);// picks primary address type

		        waitAndFill(wait, "name", "AccountBO.Address.house_no", cifInput.getHouseNo());
		        waitAndFill(wait, "name", "AccountBO.Address.street_no", cifInput.getStreetNo());
		        waitAndFill(wait, "name", "AccountBO.Address.street_name", cifInput.getStreetName());
		        waitAndFill(wait, "name", "Cat_AccountBO.Address.city", cifInput.getCity1());
		        waitAndFill(wait, "name", "Cat_AccountBO.Address.state", cifInput.getState1());
		        waitAndFill(wait, "name", "AccountBO.Address.zip", cifInput.getPostalCode1());
		        waitAndFill(wait, "name", "AccountBO.Address.country", cifInput.getCountry1());
		    }

		    actions.moveToElement(body, 0, 0).click().perform(); // shift focus
		}

		private static void fillFreeTextAddress(WebDriver driver, WebDriverWait wait, Inputs cifInput, boolean isSecondAddress) {
		    WebElement PreferredFormat = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("AccountBO.Address.PreferredFormat")));
		    WindowHandle.selectByVisibleText(PreferredFormat, cifInput.getS_AddressFormat());
		    WindowHandle.handleAlertIfPresent(driver);

		    if (isSecondAddress) {
		    	selectAddressTypeIfPresent(wait, cifInput, true);
		        WindowHandle.slowDown(2);
		        waitAndFill(wait, "name", "AccountBO.Address.FreeTextLabel", cifInput.getS_AddressLabel());
		        waitAndFill(wait, "name", "AccountBO.Address.address_Line1", cifInput.getS_AddressLine1());
		        waitAndFill(wait, "name", "Cat_AccountBO.Address.city", cifInput.getS_City());
		        waitAndFill(wait, "name", "Cat_AccountBO.Address.state", cifInput.getS_State());
		        waitAndFill(wait, "name", "AccountBO.Address.country", cifInput.getS_Country());
		        waitAndFill(wait, "name", "AccountBO.Address.zip", cifInput.getS_PostalCode());

		        System.out.println("Filled Secondary Free Text Address.");
		    } else {
		    	selectAddressTypeIfPresent(wait, cifInput, false);
		        waitAndFill(wait, "name", "AccountBO.Address.FreeTextLabel", cifInput.getAddressLabel());
		        waitAndFill(wait, "name", "AccountBO.Address.address_Line1", cifInput.getAddressLine11());
		        waitAndFill(wait, "name", "Cat_AccountBO.Address.city", cifInput.getCity1());
		        waitAndFill(wait, "name", "Cat_AccountBO.Address.state", cifInput.getState1());
		        waitAndFill(wait, "name", "AccountBO.Address.country", cifInput.getCountry1());
		        waitAndFill(wait, "name", "AccountBO.Address.zip", cifInput.getPostalCode1());

		        System.out.println("Filled Primary Free Text Address.");
		    }
		}
		public static void waitAndFill(WebDriverWait wait, String locatorType, String locatorValue, String input) {
		    By by;

		    switch (locatorType.toLowerCase()) {
		        case "id":
		            by = By.id(locatorValue);
		            break;
		        case "name":
		            by = By.name(locatorValue);
		            break;
		        case "xpath":
		            by = By.xpath(locatorValue);
		            break;
		        case "css":
		            by = By.cssSelector(locatorValue);
		            break;
		        default:
		            throw new IllegalArgumentException("Unsupported locator type: " + locatorType);
		    }

		        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		        element.clear();
		        element.sendKeys(input);
		}
		public static void selectAddressTypeIfPresent(WebDriverWait wait, Inputs cifInput, boolean isSecondRecord) {
		    try {
		        String preferredFormat = isSecondRecord ? cifInput.getS_AddressType() : cifInput.getAddressType();

		        // Only proceed if it's NOT null, NOT empty, and NOT "Mailing"
		        if (preferredFormat != null && !preferredFormat.trim().isEmpty()
		                && !preferredFormat.trim().equalsIgnoreCase("Mailing")) {

		            WebElement formatDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(
		                    By.name("AccountBO.Address.addressCategory")));
		            Select select = new Select(formatDropdown);
		            select.selectByVisibleText(preferredFormat.trim());

		            System.out.println((isSecondRecord ? "Second" : "Primary") +
		                    " Preferred Format set to: " + preferredFormat);
		        } else {
		            System.out.println((isSecondRecord ? "Second" : "Primary") +
		                    " Preferred Format skipped (default 'Mailing' or empty in Excel)");
		        }
		    } catch (Exception e) {
		        System.out.println("Error setting " + (isSecondRecord ? "Second" : "Primary") +
		                " Preferred Format: " + e.getMessage());
		    }
		}
		private static void fillContactDetails(WebDriver driver, WebDriverWait wait, Inputs cifInput ,boolean isSecondAddress) throws Exception {

			WindowHandle.selectDropdownIfValuePresent(driver, wait, By.name("AccountBO.PhoneEmail.PhoneEmailType"),
			isSecondAddress ? cifInput.getS_PContactType() : cifInput.getP_ContactType());
				

			WindowHandle.selectDropdownIfValuePresent(driver,wait, By.name("AccountBO.Preferred_Mobile_Alert_Type"),
			isSecondAddress ? cifInput.getS_PMobileAltType() : cifInput.getP_MobileAltType());
					
			WindowHandle.selectDropdownIfValuePresent(driver,wait, By.name("AccountBO.PhoneEmail.PhoneEmailType1"),
			isSecondAddress ? cifInput.getS_PEIDType() : cifInput.getP_EIDType());

		    WebElement Add = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='Add Phone and E-mail']")));
		    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", Add);

		    WindowHandle.handlePopupIfExists(driver);
		    try {
		    	String selectedContact = isSecondAddress ? cifInput.getS_PhoneOrEmail() : cifInput.getPhoneOrEmail();
		    	if (selectedContact != null) {
		    	    // If the record is "Phone" and it's the Primary (first) record
		    	    if (selectedContact.trim().equalsIgnoreCase("Phone") && !isSecondAddress) {
		    	        System.out.println("📞 [INFO] Entering Primary (Phone) Block for " + cifInput.getPhoneOrEmail());
		    	        
		    	        // Handle Primary Phone
		    	        selectPhoneNEmailIfPresent(wait, cifInput, false); // Primary (Phone)
		    	        
		    	        // Fill in Phone fields for Primary record
		    	        WebElement PhoneNEmailType = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("AccountBO.PhoneEmail.PhoneEmailType")));
			            WindowHandle.selectByVisibleText(PhoneNEmailType,cifInput.getPhoneEmailType());
		    	        waitAndFill(wait, "name", "AccountBO.PhoneEmail.PhoneNo.cntrycode", cifInput.getCntrycode());
		    	        waitAndFill(wait, "name", "AccountBO.PhoneEmail.PhoneNo.areacode", cifInput.getAreacode());
		    	        waitAndFill(wait, "name", "AccountBO.PhoneEmail.PhoneNo.localcode", cifInput.getlocalcode());
		    	        
		    	    } 
		    	    // If the record is "Phone" and it's the Secondary (second) record
		    	    else if (selectedContact.trim().equalsIgnoreCase("Phone") && isSecondAddress) {
		    	        System.out.println("📞 [INFO] Entering Secondary (Phone) Block for " + cifInput.getPhoneOrEmail());
		    	        
		    	        // Handle Secondary Phone
		    	        selectPhoneNEmailIfPresent(wait, cifInput, true); // Secondary (Phone)
		    	        
		    	        // Fill in Phone fields for Secondary record
		    	        WebElement PhoneType = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("AccountBO.PhoneEmail.PhoneEmailType")));
			            WindowHandle.selectByVisibleText(PhoneType,cifInput.getS_PhoneEmailType());
		    	        waitAndFill(wait, "name", "AccountBO.PhoneEmail.PhoneNo.cntrycode", cifInput.getS_Cntrycode());
		    	        waitAndFill(wait, "name", "AccountBO.PhoneEmail.PhoneNo.areacode", cifInput.getS_Areacode());
		    	        waitAndFill(wait, "name", "AccountBO.PhoneEmail.PhoneNo.localcode", cifInput.getS_localcode());
		    	        
		    	    }
		    	    // If the record is "E-mail" and it's the Primary (first) record
		    	    else if (selectedContact.trim().equalsIgnoreCase("E-mail") && !isSecondAddress) {
		    	        System.out.println("✉️ [INFO] Entering Primary (E-mail) Block for " + cifInput.getPhoneOrEmail());
		    	        
		    	        // Handle Primary Email
		    	        selectPhoneNEmailIfPresent(wait, cifInput, false); // Primary (E-mail)
		    	        
		    	        // Fill in Email fields for Primary record
		    	        waitAndFill(wait, "name", "AccountBO.PhoneEmail.Email", cifInput.getEmailId());
		    	        WebElement EmailType = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("AccountBO.PhoneEmail.PhoneEmailType1")));
			            WindowHandle.selectByVisibleText(EmailType,cifInput.getEmailType());
		    	        
		    	    } 
		    	    // If the record is "E-mail" and it's the Secondary (second) record
		    	    else if (selectedContact.trim().equalsIgnoreCase("E-mail") && isSecondAddress) {
		    	        System.out.println("✉️ [INFO] Entering Secondary (E-mail) Block for " + cifInput.getPhoneOrEmail());
		    	        
		    	        // Handle Secondary Email
		    	        selectPhoneNEmailIfPresent(wait, cifInput, true); // Secondary (E-mail)
		    	        
		    	        // Fill in Email fields for Secondary record
		    	        waitAndFill(wait, "name", "AccountBO.PhoneEmail.Email", cifInput.getS_EmailId());
		    	        WebElement EmailType = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("AccountBO.PhoneEmail.PhoneEmailType1")));
			            WindowHandle.selectByVisibleText(EmailType,cifInput.getS_EmailType());
		    	        
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

		    
		public static void selectPhoneNEmailIfPresent(WebDriverWait wait, Inputs cifInput, boolean isSecondRecord) {
		    try {
		        String format = isSecondRecord ? cifInput.getS_PhoneOrEmail() : cifInput.getPhoneOrEmail();
		        if (format != null && !format.trim().isEmpty() && !format.equalsIgnoreCase("Phone")) {
		            Select select = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(
		                By.name("AccountBO.PhoneEmail.PhoneOrEmail"))));
		            select.selectByVisibleText(format.trim());
		            System.out.println((isSecondRecord ? "Second" : "Primary") + " Preferred Format set to: " + format);
		        } else {
		            System.out.println((isSecondRecord ? "Second" : "Primary") + " Preferred Format skipped.");
		        }
		    } catch (Exception e) {
		        System.out.println("Error setting Preferred Format: " + e.getMessage());
		    }
		}
        
       	public static void documentdetails(WebDriver driver, WebDriverWait wait, boolean isSecondAddress, Inputs cifInput) throws Exception {
       	    
           	WebElement AddIden =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(" //input[@name='AddIdentificationDetails']")));
           	AddIden.click();
           	
           	WindowHandle.handlePopupIfExists(driver);
            WebElement body = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
       	    // Select Doc Type Code
       	    WindowHandle.selectDropdownIfValuePresent(driver, wait,
       	        By.xpath("//select[@name='EntityDocumentBO.DocTypeCode']"),
       	        isSecondAddress ? cifInput.getS_DocType() : cifInput.getDocType());

       	    // Select Doc Code
       	    WindowHandle.selectDropdownIfValuePresent(driver, wait,
       	        By.xpath("//select[@name='EntityDocumentBO.DocCode']"),
       	        isSecondAddress ? cifInput.getS_DocCode() : cifInput.getDocCode());
       	    waitAndFill(wait, "name", "EntityDocumentBO.ReferenceNumber", isSecondAddress? cifInput.getS_UID():cifInput.getUID());
       	    waitAndFill(wait, "name", "EntityDocumentBO.CountryOfIssue", isSecondAddress? cifInput.getS_Countryofissue():cifInput.getCountryofissue());
       	    body.click();
       	    waitAndFill(wait, "name", "EntityDocumentBO.PlaceOfIssue", isSecondAddress? cifInput.getS_placeofissue():cifInput.getplaceofissue());
       	    body.click();
       	    waitAndFill(wait, "name", "3_EntityDocumentBO.DocIssueDate", isSecondAddress? cifInput.getS_Issdate():cifInput.getIssdate());
       	    waitAndFill(wait, "name", "3_EntityDocumentBO.DocExpiryDate", isSecondAddress? cifInput.getS_ExDate():cifInput.getExDate());
       	    WebElement save = wait.until(ExpectedConditions.elementToBeClickable(By.name("SAVE")));
       	    save.click();
       	}

       	public void switchToWindowAndFrame(String windowHandle, WebDriverWait wait) {
       	    driver.switchTo().window(windowHandle);
       	    driver.switchTo().defaultContent();
       	    WindowHandle.goToMainFrame(wait); 
       	}


	}
