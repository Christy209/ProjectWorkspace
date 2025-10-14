package CRM;

import java.io.IOException;
import java.time.Duration;
import java.util.Set;
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
import Annotation.ExcelData;
import Base.DriverManager;
import Utilities.Dataproviders;
import Utilities.WindowHandle;

public class ModifyRetailCIFID {

	protected WebDriver driver;
    protected WebDriverWait wait;
    
    @BeforeClass
		 public void setup() throws IOException { 
    	
		        driver = DriverManager.getDriver();
		        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		        String userID = DriverManager.getProperty("userid");
	            String password = DriverManager.getProperty("password");

	            // Login with first user
	            DriverManager.login(userID, password);
	            System.out.println("Executing TC01: Modify the RetailCIFID: " + userID);
		    }
    
//    @DataProvider(name="RetailCif")
//    public Object[][] getInputData() throws IOException {
//        return Dataproviders.getInputData(3); // Row 4 of Sheet2
//    }
	  @Test(dataProvider = "Inputs", dataProviderClass = Dataproviders.class)
	  @ExcelData(sheetName = "Sheet2", rowIndex = 3)
	   	public void ModifyRetailcIFID(String[] getData) throws Exception {
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
            	
            	WebElement EditEntity = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@id='spanFor2']")));
            	EditEntity.click();
            	
            	driver.switchTo().defaultContent();
            	PageObject.Frames(wait);
            	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("userArea")));
            	
            	WebElement EditCIFID = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='FilterParam1']")));
            	EditCIFID.sendKeys(getData[1]);
            	
            	driver.switchTo().defaultContent();
            	PageObject.Frames(wait);
            	
            	WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='submitBut']")));
            	submit.click();

            	driver.switchTo().defaultContent();
              	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("loginFrame")));
            	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CRMServer")));
            	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("DataAreaFrm")));
            	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath("//html//frameset//frame")));
            	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("tempFrm")));
            	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("tabContentFrm")));
            	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("userArea")));
            	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("IFrmtab0")));
            	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("primaryUserArea")));
            	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("userArea")));
                // Right-click on CIF ID
            	String cif = getData[1];
            	WebElement cifIdLink = driver.findElement(By.xpath("//a[normalize-space()='" + cif + "']")); // Adjust text as needed
                Actions actions = new Actions(driver);
                actions.contextClick(cifIdLink).perform();

               if("Edit".equalsIgnoreCase(getData[3])) {
                	
	                WebElement edit = driver.findElement(By.xpath("//div[@id='ie5menu3']"));///only edit
	                actions.moveToElement(edit).perform();
	                
	                WebElement GeneralDetails = driver.findElement(By.xpath("//div[@id='suboptions5']"));
	                actions.moveToElement(GeneralDetails).click().perform();
	                
	                String secondWindowHandle = PageObject.switchToSecondWindow(driver, Windowhandle, Windowhandle);
	                
	                driver.switchTo().defaultContent();
	                PageObject.Tbs(wait);
	                
	                WebElement Documentdetails = driver.findElement(By.xpath("//font[@id='fnttpageCont5']"));///only edit
	                Documentdetails.click();
                
	                if("Add".equalsIgnoreCase(getData[28])) {
	                	
	                WebElement add = driver.findElement(By.xpath("//input[@name='AddIdentificationDetails']"));///only edit
	                add.click();
	                
	               PageObject.switchToThirdWindow(driver, Windowhandle, secondWindowHandle);

	                // Select values from dropdowns
	                WindowHandle.selectDropdownIfValuePresent(driver, wait, By.name("EntityDocumentBO.DocTypeCode"), "KYCRP");
	                WindowHandle.selectDropdownIfValuePresent(driver, wait, By.name("EntityDocumentBO.DocCode"), "PSPT");

	                // Fill in text fields
	                wait.until(ExpectedConditions.elementToBeClickable(By.name("EntityDocumentBO.ReferenceNumber"))).sendKeys("XYZPZ5678L");
	                wait.until(ExpectedConditions.elementToBeClickable(By.name("EntityDocumentBO.PlaceOfIssue"))).sendKeys("001");
	                wait.until(ExpectedConditions.elementToBeClickable(By.name("EntityDocumentBO.CountryOfIssue"))).sendKeys("AD");
	                wait.until(ExpectedConditions.elementToBeClickable(By.name("3_EntityDocumentBO.DocIssueDate"))).sendKeys("16/05/2025");
	                
	                }
                
	                if("Update".equalsIgnoreCase(getData[28])) {
	                	
	                	WebElement en_US = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//font[@class='fntColData']")));
	                    actions.doubleClick(en_US).perform();
		    	         
		    	         WebElement box = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='AddIdentificationDetails']")));
		    	         box.click();
		    	         
		    	         fillEntityDocumentDetails(driver, wait, secondWindowHandle, secondWindowHandle);
		    	         
	                }
                
                wait.until(ExpectedConditions.elementToBeClickable(By.name("SAVE"))).click();

                WindowHandle.handleAlertIfPresent(driver);
                driver.switchTo().window(secondWindowHandle);
                
                }
                if("QuickEdit".equalsIgnoreCase(getData[3])) {
                
	                WebElement QuickEdit = driver.findElement(By.xpath("//div[@id='ie5menu4']"));///only edit
	                actions.moveToElement(QuickEdit).perform();

                
                if ("A".equalsIgnoreCase(getData[4])) {
                    try {
                        // Locate the "generalDetails" element and perform the action
                        WebElement AddressDetails = driver.findElement(By.xpath("//div[@id='suboptions14']"));
                        actions.moveToElement(AddressDetails).click().perform();

                        String secondWindowHandle = PageObject.switchToSecondWindow(driver, Windowhandle, Windowhandle);
                        PageObject.Tbs(wait);

                        // Select the "preferredAddress" dropdown value
                        WindowHandle.selectDropdownIfValuePresent(driver, wait, By.xpath("//select[@name='Address.preferredAddress']"), getData[5]);

                        // Double-click on the 'en_US' element
                        WebElement en_US = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//font[@class='fntColData']")));
                        actions.doubleClick(en_US).perform();

                        // Click on the "tab" element
                        WebElement tab = driver.findElement(By.xpath("//tbody/tr[3]/td[7]/input[1]"));
                        tab.click();

                        PageObject.switchToThirdWindow(driver, Windowhandle, Windowhandle);

                        // Fill out the form in the third window
                        WindowHandle.selectDropdownIfValuePresent(driver, wait, By.name("AccountBO.Address.PreferredFormat"), getData[6]);
                        WindowHandle.handleAlertIfPresent(driver);
                        WindowHandle.selectDropdownIfValuePresent(driver, wait, By.name("AccountBO.Address.addressCategory"), getData[7]);
                        
                        // Fill in address details
                        PageObject.waitAndFill(wait, "name", "AccountBO.Address.house_no", getData[8]);
                        PageObject.waitAndFill(wait, "name", "AccountBO.Address.street_no", getData[9]);
                        PageObject.waitAndFill(wait, "name", "AccountBO.Address.street_name", getData[10]);
                        PageObject.waitAndFill(wait, "name", "Cat_AccountBO.Address.city", getData[11]);
                        PageObject.waitAndFill(wait, "name", "Cat_AccountBO.Address.state", getData[12]);
                        PageObject.waitAndFill(wait, "name", "AccountBO.Address.zip", getData[13]);
                        PageObject.waitAndFill(wait, "name", "AccountBO.Address.country", getData[14]);
                        PageObject.waitAndFill(wait, "name", "3_AccountBO.Address.Start_Date", getData[15]);

                        // Click the "Save" button
                        WebElement Save = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Save")));
                        Save.click();

                        // Handle potential alert
                WindowHandle.handleAlertIfPresent(driver);
                driver.switchTo().window(secondWindowHandle);
                 
            } catch (Exception e) {
                e.printStackTrace(); // Log the exception
                // Handle specific exceptions or perform recovery actions as needed
            }
                }
                
                if ("Phone&Email".equalsIgnoreCase(getData[16])) {
                    try {
                    	
                    	 WebElement generalDetails = driver.findElement(By.xpath("//div[@id='suboptions15']"));
                         actions.moveToElement(generalDetails).click().perform();

                         String secondWindowHandle = PageObject.switchToSecondWindow(driver, Windowhandle, Windowhandle);
                         PageObject.Tbs(wait);

                         WindowHandle.selectDropdownIfValuePresent(driver, wait, By.name("PhoneEmail.preferredPhone"),
     	            	 getData[18]);
                         
                         WindowHandle.selectDropdownIfValuePresent(driver, wait, By.xpath("//select[@name='PhoneEmail.preferredEmail']"),
     	            	 getData[19]);
                         
                         if("Update".equalsIgnoreCase(getData[17])) {
                        	 
                        	 WebElement en_US = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//font[@class='fntColData']")));
                             actions.doubleClick(en_US).perform();
        	    	         
        	    	         WebElement box = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='...']")));
        	    	         box.click();
        	    	         
        	    	         PageObject.switchToThirdWindow(driver, Windowhandle, Windowhandle);
        	    	         
        	    	         WindowHandle.selectDropdownIfValuePresent(driver, wait, By.name("AccountBO.PhoneEmail.PhoneEmailType"), getData[21]);
        	    	         WebElement cntrycode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("AccountBO.PhoneEmail.PhoneNo.cntrycode")));
        	    	         cntrycode.clear();
        	    	         cntrycode.sendKeys(getData[22]);
        	    	         WebElement areacode =wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("AccountBO.PhoneEmail.PhoneNo.areacode")));
        	    	         areacode.clear();
        	    	         areacode.sendKeys(getData[23]);
        	    	         WebElement localcode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("AccountBO.PhoneEmail.PhoneNo.localcode")));
        	    	         localcode.clear();
        	    	        localcode.sendKeys(getData[24]);

                         } else {
                        	 
                        	 System.out.println("Skipping updation");
                         }

                    if (!"Update".equalsIgnoreCase(getData[17])) {
                        try {
                        		 
                         wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='ADD_PHONEEMAIL']"))).click();
                         PageObject.switchToThirdWindow(driver, Windowhandle, Windowhandle);
                         
                         String option = getData[17].trim();
                         WindowHandle.selectDropdownIfValuePresent(driver, wait, By.name("AccountBO.PhoneEmail.PhoneOrEmail"), getData[20]);
                         
                         if ("Phone".equalsIgnoreCase(option)) {
                         
                         WindowHandle.selectDropdownIfValuePresent(driver, wait, By.name("AccountBO.PhoneEmail.PhoneEmailType"), getData[21]);
                         wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("AccountBO.PhoneEmail.PhoneNo.cntrycode"))).sendKeys(getData[22]);
                         wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("AccountBO.PhoneEmail.PhoneNo.areacode"))).sendKeys(getData[23]);
                         wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("AccountBO.PhoneEmail.PhoneNo.localcode"))).sendKeys(getData[24]);
                         }
                         else if ("E-mail".equalsIgnoreCase(option)) {
                        	 
                        	 WindowHandle.selectDropdownIfValuePresent(driver, wait, By.name("AccountBO.PhoneEmail.PhoneEmailType1"), getData[25]);
                             wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("AccountBO.PhoneEmail.Email"))).sendKeys(getData[26]);
                         }
                         else {
                        	    throw new IllegalArgumentException("Unsupported option: " + option);
                        	}

                    } catch (Exception e) {
                        e.printStackTrace(); // Log the exception
                        // Handle specific exceptions or perform recovery actions as needed
                    }
                    } 
                    
                    
                    wait.until(ExpectedConditions.elementToBeClickable(By.name("Save"))).click();

                    WindowHandle.handleAlertIfPresent(driver);
                    driver.switchTo().window(secondWindowHandle);

                    } catch (Exception e) {
                        e.printStackTrace(); // Log the exception
                        // Handle specific exceptions or perform recovery actions as needed
                    }

                }
                }
                    
    		    driver.switchTo().defaultContent();
    		    PageObject.button(wait);
    		    WebElement save = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='submitBut']")));
    		    save.click();
    		    
    		    WindowHandle.handleAlertIfPresent(driver);
    		    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    		    wait.until(driver -> driver.getWindowHandles().size() > 1);

    		    Set<String> allWindowHandles = driver.getWindowHandles();
    		    String newWindowHandle = null;

    		    for (String handle : allWindowHandles) {
    		        if (!handle.equals(Windowhandle)) {
    		            newWindowHandle = handle;
    		            driver.switchTo().window(newWindowHandle);
    		            System.out.println("Switched to new window: " + newWindowHandle);
    		            break;
    		        }
    		    }
    		    
                driver.switchTo().defaultContent();
    		  	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("tempFrm")));
    	    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("tabContentFrm")));
    	    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("userArea")));
    	    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("IFrmtab0")));
    	    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("formDispFrame")));
    	    	
    		    WebElement Processurl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@name='ProcessBO.url_']")));
    		    WindowHandle.selectByVisibleText(Processurl,getData[2]);
    		    
    		    driver.switchTo().defaultContent();
    		    wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("tempFrm")));
    	    	wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("buttonFrm")));
    	    	
    	    	WebElement savePro = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='saveBut']")));
    	    	savePro.click();
    		  	  
    	    	WindowHandle.handleAlertIfPresent(driver);
    	    	

			    } catch (Exception e) {
			        System.out.println("Modification Error: " + e.getMessage());
			    }
            

}
    public void fillEntityDocumentDetails(WebDriver driver, WebDriverWait wait, String windowHandle1, String windowHandle2) {
        // Switch to the third window
        PageObject.switchToThirdWindow(driver, windowHandle1, windowHandle2);

        // Select values from dropdowns
        WindowHandle.selectDropdownIfValuePresent(driver, wait, By.name("EntityDocumentBO.DocTypeCode"), "KYCRP");
        WindowHandle.selectDropdownIfValuePresent(driver, wait, By.name("EntityDocumentBO.DocCode"), "PSPT");

        // Fill in text fields
        wait.until(ExpectedConditions.elementToBeClickable(By.name("EntityDocumentBO.ReferenceNumber"))).sendKeys("XYZPZ5678L");
        wait.until(ExpectedConditions.elementToBeClickable(By.name("EntityDocumentBO.PlaceOfIssue"))).sendKeys("001");
        wait.until(ExpectedConditions.elementToBeClickable(By.name("EntityDocumentBO.CountryOfIssue"))).sendKeys("AD");
        wait.until(ExpectedConditions.elementToBeClickable(By.name("3_EntityDocumentBO.DocIssueDate"))).sendKeys("16/05/2025");
    }
    }
