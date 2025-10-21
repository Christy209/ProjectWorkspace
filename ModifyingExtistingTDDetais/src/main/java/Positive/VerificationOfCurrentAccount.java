package Positive;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.UnhandledAlertException;
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

public class VerificationOfCurrentAccount extends BaseTest {
	
	 private WebDriver driver;
	 private WebDriverWait wait;
	 
	 String excelpath = System.getProperty("user.dir") + "/resources/TestData.xlsx";
	    int row = 2;
	    String sheetname = "Sheet2"; 
	    
		@BeforeClass
		public void setup() throws IOException {
			driver = DriverManager.getDriver();
			wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			String userid = DriverManager.getProperty("userid1");
			String password = DriverManager.getProperty("password1");
			DriverManager.login(userid, password);
				}
		 @Test(dataProvider = "testcase", dataProviderClass = Dataproviders.class)
		    @ExcelData(sheetName ="Sheet2",rowIndex= {1,2})
		 
	    public void CreateLoan(RowData inputData,RowData verifyData) throws Exception {
		    	@SuppressWarnings("unused")
				String mainWindowHandle = driver.getWindowHandle();
		    	WindowHandle.slowDown(4);
		    	
		    	WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),verifyData.getByIndex(1));

		    	WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
		    	((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);


		        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
		        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));
		        WindowHandle.slowDown(1);
		        WebElement funCode = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("verifyCancel")));
		        funCode.sendKeys(verifyData.getByIndex(2));

		        WebElement acctID = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tempForacid")));
		        String AcctID = inputData.getByHeader("Created_AccountID");
	            ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", acctID ,AcctID);
	             
		    	   WebElement go = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='Accept']")));
		    	   ((JavascriptExecutor) driver).executeScript("arguments[0].click();", go);
		    	   
		    	   Map<String, String> appData = getApplicationData(driver,wait); // Fetch application value
		    	   Validation.validateData(inputData.getHeaderMap(),appData);

		    	   	WindowHandle.ValidationFrame(driver);
		    	   	WebElement accountElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='AcctNum']")));
		            String actualText = "Verified A/c. ID: " + accountElement.getText().trim();
		            String expectedMessage = "Verified A/c. ID: " + AcctID;
		            Assert.assertEquals(actualText, expectedMessage, "Test Failed: Account verification message is incorrect!");
		            

}
	   
	   private static Map<String, String> getApplicationData(WebDriver driver ,WebDriverWait wait) throws Exception {
		   
		    Map<String, String> appData = new HashMap<>();
		    String mainWindowHandle = driver.getWindowHandle();
		    //////////////////////////////////////General tab/////////////////////////
		    appData.put("A/c. Name", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='acctName']","xpath"));
		    appData.put("A/c. Short Name", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='acctShortName']","xpath"));
		    appData.put("A/c. Opening Date", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='acctOpenDate_ui']","xpath"));
		    appData.put("A/c. Manager ID", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='acctMgrAtAcct']","xpath"));
		    //appData.put("Acct_Statement", ExcelUtils.getTextOrValue(driver, wait, "pbPsFlg","id"));
		    appData.put("FreqType", ExcelUtils.getTextOrValue(driver, wait, "pbPsFreqType","id"));
		    appData.put("FreqWeek", ExcelUtils.getTextOrValue(driver, wait, "pbPsFreqWeek","id"));
		    appData.put("FreqDay", ExcelUtils.getTextOrValue(driver, wait, "pbPsFreqDay","id"));
		    appData.put("FreqStartDD", ExcelUtils.getTextOrValue(driver, wait, "pbPsFreqStartDD","id"));
		    appData.put("FreqHldyStat", ExcelUtils.getTextOrValue(driver, wait, "pbPsFreqHldyStat","id"));
		    appData.put("DispatchMode", ExcelUtils.getTextOrValue(driver, wait, "despatchMode","id"));
		    appData.put("Modeofoperation", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='modeOfOperCode']","xpath"));
		    
		    WebElement generalDetailsTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("generaldetails2")));
        	((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", generalDetailsTab);
        	generalDetailsTab.click();
        	
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
        //	appData.put("MinIntPcntCr", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='minIntPcntCr']", "xpath"));
        	//appData.put("MaxIntPcntCr", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='maxIntPcntCr']","xpath"));
        	//appData.put("CustPrefIntCr", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='custPrefIntCr']","xpath"));
        	//appData.put("AcctPrefIntCr", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='acctPrefIntCr']","xpath"));
        	//appData.put("ChanPrefIntCr", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='chanPrefIntCr']","xpath"));
        	appData.put("IntCrAcctFlg", ExcelUtils.getTextOrValue(driver, wait, "intCrAcctFlg","id"));
        	//appData.put("NextIntCrCalcDt", ExcelUtils.getTextOrValue(driver, wait, "nextIntCrCalcDt_ui","id"));
        	appData.put("Interest Rate Code", ExcelUtils.getTextOrValue(driver, wait, "intRateCode","id"));
        	appData.put("Tax Category", ExcelUtils.getTextOrValue(driver, wait, "wtaxFlg","id"));
        	appData.put("Withholding Tax Level", ExcelUtils.getTextOrValue(driver, wait, "wtaxLevelFlg","id"));
        	
        	 /////////////////////////////////////////////////schm tab///////////////////////////////////////////////////////
    		WebElement Schmdetailstab = wait.until(ExpectedConditions.elementToBeClickable(By.id("sbschemedetails")));
    		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Schmdetailstab);
    		Schmdetailstab.click();

    		String Nomineeflg = "Not Selected"; 

    		// Locate the radio buttons
    		WebElement NomineeflgYes = driver.findElement(By.xpath("//input[@id='availNomFlg' and @value='Y']"));
    		WebElement NomineeflgNo = driver.findElement(By.xpath("//input[@id='availNomFlg' and @value='N']"));

    		// Check which radio button is selected
    		if (NomineeflgYes.isSelected()) {
    			payInterestValue = "Yes";
    		} else if (NomineeflgNo.isSelected()) {
    			payInterestValue = "No";
    		}	        		   
		
    			appData.put("Pay Interest", Nomineeflg);
    			appData.put("A/c. Min. Balance", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='acctMinBal']", "xpath"));
    			
///////////////////////////////////Nominee tab//////////////////////////////////////////
		WebElement NomineeTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("nominationdetails")));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", NomineeTab);
		NomineeTab.click();
		        		   
	  appData.put("Regnum", ExcelUtils.getTextOrValue(driver, wait, "regValue","id"));
	   appData.put("NomineeCifId", ExcelUtils.getTextOrValue(driver, wait, "nominationdetails.cifId","name"));
	   appData.put("Nominee Name", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='nomName']","xpath"));
	   appData.put("Address Line 1", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='nomAddrLine1']","xpath"));
	   appData.put("Relation", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='relation']","xpath"));
	   appData.put("State", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='nomStateCode']","xpath"));
	   appData.put("Postal Code", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='nomPostalCode']","xpath"));
	   appData.put("City", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='nomCityCode']","xpath"));
	   appData.put("Country ", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='nomCntryCode']","xpath"));
	   appData.put("Birth Date", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='dtOfBirth_ui']","xpath"));
	   appData.put("NomineePcnt", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='nomPcnt']","xpath")); 
	   
//	   ///////////////////////////////////////////RelatedParty Details///////////////////////////////////////////////////////
//		
//	   List<Map<String, String>> allRecords = new ArrayList<>();
//       WebElement RelatedPartydetailstab = wait.until(ExpectedConditions.elementToBeClickable(By.id("relatedpartydetails")));
//       ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", RelatedPartydetailstab);
//       ((JavascriptExecutor) driver).executeScript("arguments[0].click();", RelatedPartydetailstab);
//       
//       @SuppressWarnings("unused")
//		boolean hasRecords = false;
//   
//       do {
//           try {
//
//        	   appData.put("Relation Code", ExcelUtils.getTextOrValue(driver, wait, "relnCode", "id"));
//        	   appData.put("Relation Type", ExcelUtils.getTextOrValue(driver, wait, "relnType", "id"));
//               appData.put("RelatedFreqType", ExcelUtils.getTextOrValue(driver, wait, "relatedpartydetails.pbPsFreqType", "name"));
//               appData.put("RelatedFreqWeek", ExcelUtils.getTextOrValue(driver, wait, "relatedpartydetails.pbPsFreqWeek", "name"));
//               appData.put("RelatedFreqDay", ExcelUtils.getTextOrValue(driver, wait, "relatedpartydetails.pbPsFreqDay", "name"));
//               appData.put("RelatedFreqStartDD", ExcelUtils.getTextOrValue(driver, wait, "relatedpartydetails.pbPsFreqStartDD", "name"));
//               appData.put("RelatedFreqHldyStat", ExcelUtils.getTextOrValue(driver, wait, "relatedpartydetails.pbPsFreqHldyStat", "name"));
//               appData.put("RelateddispMode", ExcelUtils.getTextOrValue(driver, wait, "relatedpartydetails.despMode", "name"));
//               appData.put("Related-CustTitle", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='custTitle']", "xpath"));
//               appData.put("Related - CustName", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='custName']", "xpath"));
//               appData.put("Related -CustAddrLine1", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='custAddrLine1']", "xpath"));
//
//               if (appData.get("Relation Code") != null && !appData.get("Relation Code").isEmpty()) {
//                   allRecords.add(new HashMap<>(appData));  // Store a copy to prevent overwriting
//                   hasRecords = true;
//                   System.out.println("✅ Record " + allRecords.size() + ": " + appData);
//               } else {
//                   System.out.println("❌ No valid related party records found. Exiting.");
//                   break;
//               }
//               WindowHandle.handleAlertIfPresent(driver);
//
//               List<WebElement> nextButtonList = driver.findElements(By.id("relParty_NextRec"));
//               if (!nextButtonList.isEmpty() && nextButtonList.get(0).isEnabled()) {
//                   WebElement nextButton = nextButtonList.get(0);
//                   nextButton.click();
//                   wait.until(ExpectedConditions.stalenessOf(nextButton)); // Ensure the page refreshes
//               } else {
//                   System.out.println("🔴 No more records found. Moving to the next tab.");
//                   break;
//               }
//           } catch (NoSuchElementException e) {
//               System.out.println("🚫 Element not found. Exiting.");
//               break;
//           } catch (UnhandledAlertException e) {
//        	   WindowHandle.handleAlertIfPresent(driver);
//               break;
//           }
//       } while (true);
		///////////////////////////////////////////// Related Party Details - UI Extraction ///////////////////////////////////////
		
		List<Map<String, String>> allUIRecords = new ArrayList<>();
		WebElement relatedPartydetailstab = wait.until(ExpectedConditions.elementToBeClickable(By.id("relatedpartydetails")));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", relatedPartydetailstab);
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", relatedPartydetailstab);
		
		int recordIndex = 1;
		do {
		try {
		Map<String, String> uiData = new HashMap<>();
		
		uiData.put("Relation Code", ExcelUtils.getTextOrValue(driver, wait, "relnCode", "id"));
		uiData.put("Relation Type", ExcelUtils.getTextOrValue(driver, wait, "relnType", "id"));
		uiData.put("RelatedFreqType", ExcelUtils.getTextOrValue(driver, wait, "relatedpartydetails.pbPsFreqType", "name"));
		uiData.put("RelatedFreqWeek", ExcelUtils.getTextOrValue(driver, wait, "relatedpartydetails.pbPsFreqWeek", "name"));
		uiData.put("RelatedFreqDay", ExcelUtils.getTextOrValue(driver, wait, "relatedpartydetails.pbPsFreqDay", "name"));
		uiData.put("RelatedFreqStartDD", ExcelUtils.getTextOrValue(driver, wait, "relatedpartydetails.pbPsFreqStartDD", "name"));
		uiData.put("RelatedFreqHldyStat", ExcelUtils.getTextOrValue(driver, wait, "relatedpartydetails.pbPsFreqHldyStat", "name"));
		uiData.put("RelateddispMode", ExcelUtils.getTextOrValue(driver, wait, "relatedpartydetails.despMode", "name"));
		uiData.put("Related-CustTitle", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='custTitle']", "xpath"));
		uiData.put("Related - CustName", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='custName']", "xpath"));
		uiData.put("Related -CustAddrLine1", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='custAddrLine1']", "xpath"));
		
		if (uiData.get("Relation Code") != null && !uiData.get("Relation Code").isEmpty()) {
		System.out.println("🟦 UI Related Party Record #" + recordIndex + ": " + uiData);
		allUIRecords.add(uiData);
		recordIndex++;
		} else {
		System.out.println("❌ No valid related party record found. Exiting loop.");
		break;
		}
		
		WindowHandle.handleAlertIfPresent(driver);
		
		List<WebElement> nextButtonList = driver.findElements(By.id("relParty_NextRec"));
		if (!nextButtonList.isEmpty() && nextButtonList.get(0).isEnabled()) {
		WebElement nextButton = nextButtonList.get(0);
		nextButton.click();
		wait.until(ExpectedConditions.stalenessOf(nextButton)); // Wait for UI to refresh
		} else {
		break;
		}
		
		} catch (NoSuchElementException e) {
		break;
		} catch (UnhandledAlertException e) {
		WindowHandle.handleAlertIfPresent(driver);
		break;
		}
		
		} while (true);

		//✅ Step 2: Read only the expected record from Excel (RelatedPartyFlg = Y)
		Map<String, String> expectedRecordFromExcel = ExcelUtils.getRelatedPartyRecordWhereFlagIsY("TC01");  // Implement this method
		
		if (expectedRecordFromExcel != null) {
		String expectedRelationCode = expectedRecordFromExcel.get("Relation Code");
		
		Optional<Map<String, String>> matchedUIRecordOpt = allUIRecords.stream()
		.filter(r -> expectedRelationCode.equalsIgnoreCase(r.get("Relation Code")))
		.findFirst();
		
		if (matchedUIRecordOpt.isPresent()) {
		Map<String, String> uiRecord = matchedUIRecordOpt.get();
		
		System.out.println("🔍 Validating Related Party with Relation Code: " + expectedRelationCode);
		for (String key : expectedRecordFromExcel.keySet()) {
		String expected = expectedRecordFromExcel.getOrDefault(key, "").trim();
		String actual = uiRecord.getOrDefault(key, "").trim();
		
			if (!expected.equalsIgnoreCase(actual)) {
			System.out.println("❌ Mismatch at key '" + key + "': Expected='" + expected + "', Actual='" + actual + "'");
			} else {
			System.out.println("✅ Matched key '" + key + "': Value='" + expected + "'");
			}
		}
			} else {
			System.out.println("⚠️ Related Party with Relation Code '" + expectedRelationCode + "' not found in UI records.");
			}
		} else {
		System.out.println("⚠️ No Related Party in Excel marked with RelatedPartyFlg = Y");
		}

       
//////////////////////////////////////////////Document details/////////////////////////////////////////////////////
       WebElement documentDetailsTab = driver.findElement(By.xpath("//a[@id='documentdetails']"));
       ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", documentDetailsTab);
       ((JavascriptExecutor) driver).executeScript("arguments[0].click();", documentDetailsTab);

	        		   
       appData.put("DocCode", ExcelUtils.getTextOrValue(driver, wait, "docCode","id"));
	  // appData.put("DocScanFlg", ExcelUtils.getTextOrValue(driver, wait, "docScanFlg","id"));
	   
	   ////////////////////////////////FFD tab//////////////////////////////////////////////////////////////////////
       
       WebElement FFDtab = wait.until(ExpectedConditions.elementToBeClickable(By.id("sbffdparameters")));
       ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", FFDtab);
       FFDtab.click();
       	        		   
       appData.put("FFD-Schmcode", ExcelUtils.getTextOrValue(driver, wait, "sbffdparameters.schmCode","name"));
       
////////////////////////////////////////////Account limit///////////////////////////////////////////////
       
       WebElement AccountLimitsTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("acctlmt")));
       ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", AccountLimitsTab);
       AccountLimitsTab.click();
	        		   
       appData.put("ExpiryDate", ExcelUtils.getTextOrValue(driver, wait, "expiryDate_ui","id"));
       appData.put("DocumentDate", ExcelUtils.getTextOrValue(driver, wait, "documentDate_ui","id"));
       appData.put("DrawingPowerInd", ExcelUtils.getTextOrValue(driver, wait, "drawingPowerInd","id"));
       
////////////////////////////////////////////MIS code details////////////////////////////////////////////////////////
       WebElement Miscodesdetailstab = wait.until(ExpectedConditions.elementToBeClickable(By.id("miscodes")));
       ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Miscodesdetailstab);
       Miscodesdetailstab.click();
		        		   
       appData.put("SectCode", ExcelUtils.getTextOrValue(driver, wait, "sectCode","id"));
	   appData.put("SubSectCode", ExcelUtils.getTextOrValue(driver, wait, "subSectCode","id"));
	   appData.put("PurpAdv", ExcelUtils.getTextOrValue(driver, wait, "purpAdv","id"));
	   appData.put("ModeAdv", ExcelUtils.getTextOrValue(driver, wait, "modeAdv","id"));
	   appData.put("TypeAdv", ExcelUtils.getTextOrValue(driver, wait, "typeAdv","id"));
	   appData.put("NatAdv", ExcelUtils.getTextOrValue(driver, wait, "natAdv","id"));
	   
       try{
	   		WebElement submit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Submit")));
	   		submit.click();
			}catch (Exception e) {
				System.out.println("Error " + e.getMessage());
			}
       
       WindowHandle.handlePopupIfExists(driver);
       WebElement acceptButton = driver.findElement(By.id("accept"));
       acceptButton.click();
       driver.switchTo().window(mainWindowHandle);
       WindowHandle.ValidationFrame(driver);
       String successLog = WindowHandle.checkForSuccessElements(driver);
       if(successLog != null){
           System.out.println("✅ Test Success");
       }
		 driver.switchTo().defaultContent();
			LogOut.performLogout(driver, wait);
		 
  		
			return appData;
	   }
	}
