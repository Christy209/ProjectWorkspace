package TermDeposit;


import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Annotation.ExcelData;
import Base.BaseTest;
import Base.DriverManager;
import Utilities.Dataproviders;
import Utilities.ExcelUtils2;
import Utilities.LogOut;
import Utilities.RowData;
import Utilities.WindowHandle;
import Utilities.WindowHandleJS;

public class OpeningTermDepositAccount extends BaseTest {


	   protected static WebDriver driver;
	    protected static WebDriverWait wait;
	   
	    @BeforeClass
	    public  void setup() throws IOException {
	        driver = DriverManager.getDriver();
	        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

	        String userID = DriverManager.getProperty("userid");
	        String password = DriverManager.getProperty("password");

	        DriverManager.login(userID, password);
	        System.out.println("✅ Logged in as: " + userID);
	    }

	@Test(dataProvider = "testcase", dataProviderClass = Dataproviders.class)
	@ExcelData(sheetName = "Sheet1", rowIndex = 1)

	public void createTDAccount(RowData inputData) throws Exception {
		
		
		String mainWindowHandle = driver.getWindowHandle();
		
			 WindowHandle.slowDown(4);
            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),inputData.getByIndex(1));
            WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);
try {
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));
      
           //WindowHandle.selectDropdownWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("templateFunction"))),inputData.getByIndex(61));
			 	WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cifId"))),inputData.getByIndex(2));
	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("schmCode"))),inputData.getByIndex(3));
	            WebElement body = driver.findElement(By.tagName("body"));
	            Actions actions = new Actions(driver);
	           actions.moveToElement(body, 0, 1).click().perform();
	            //WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("glSubHeadCode"))),inputData.getByIndex(4));
   
	            WindowHandle.safeClick(driver, wait, By.id("Accept"));
//	            WindowHandle.handlePopupIfExists(driver);
//		   		WebElement accept = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accept")));
//		   		accept.click();
}catch(Exception e) {
	 WindowHandle.checkForApplicationErrors(driver);
}
	             
	

			/////////////////////////////// General Tab //////////////////////////////////////////
			try {
			
			
			
			WindowHandle.slowDown(2);
			WindowHandleJS wh = new WindowHandleJS(driver);
			
			WebElement CustomerPreferInterest =
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("locationCode")));
			wh.setValue(CustomerPreferInterest, inputData.getByIndex(7));
			
			WebElement CreditPreferInterest =
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("acctOpenDate_ui")));
			wh.setValue(CreditPreferInterest, inputData.getByIndex(5));
			
			WebElement ChannelPreferInterest =
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("modeOfOperCode")));
			wh.setValue(ChannelPreferInterest, inputData.getByIndex(6));
			
			WindowHandle.safeClick(driver, wait, By.id("Validate"));
			
			} catch (Exception e) {
			System.out.println("❌ Error in General Tab: " + e.getMessage());
			}
			

				/////////////////////////////// interest Tab///////////////////////////////////////////////////
				
			 try {


				 WindowHandle.slowDown(2);
				 ((JavascriptExecutor) driver).executeScript("document.getElementById('tdint').click();");

				
				WindowHandleJS wh = new WindowHandleJS(driver);
				WebElement CustomerPreferInterest = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("custPrefIntCr")));
				wh.setValue(CustomerPreferInterest, inputData.getByIndex(8));
				WebElement CreditPreferInterest = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("acctPrefIntCr")));
				wh.setValue(CreditPreferInterest, inputData.getByIndex(9));
				WebElement ChannelPreferInterest = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("chanPrefIntCr")));
				wh.setValue(ChannelPreferInterest, inputData.getByIndex(10));
				WebElement PeggingFrequency1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pegFreqMnths")));
				wh.setValue(PeggingFrequency1, inputData.getByIndex(11));
				WebElement PeggingFrequencyDay1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pegFreqDay")));
				wh.setValue(PeggingFrequencyDay1, inputData.getByIndex(12));
				WindowHandle.safeClick(driver, wait, By.id("Validate"));
			} catch (Exception e) {
				System.out.println("Error " + e.getMessage());
			}

		/////////////////////////////// Scheme Tab ///////////////////////////////////////////////////
		             
					 
		try {
		
			 WindowHandle.slowDown(2);
			 ((JavascriptExecutor) driver).executeScript("document.getElementById('tdsch').click();");
			 
			 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			 LocalDate valueDate = LocalDate.parse(inputData.getByIndex(13), formatter);
			 int months = Integer.parseInt(inputData.getByIndex(15));
			 int days = Integer.parseInt(inputData.getByIndex(8));
			 LocalDate maturityDate = valueDate.plusMonths(months).plusDays(days);
			 WebElement maturityDateField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("maturityDate_ui")));
			 ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + maturityDate.format(formatter) + "';", maturityDateField);
			 ((JavascriptExecutor) driver).executeScript(
			     "if(document.createEvent){" +
			     " var evt=document.createEvent('HTMLEvents');" +
			     " evt.initEvent('change',true,true);" +
			     " arguments[0].dispatchEvent(evt);" +
			     "} else if(arguments[0].fireEvent){" +
			     " arguments[0].fireEvent('onchange');" +
			     "}", maturityDateField);
			 
			 if (maturityDate.isBefore(valueDate)) {
				    throw new RuntimeException("Invalid Maturity Date: " + maturityDate + " is before Value Date: " + valueDate);
				}

			 //WebElement transferMode = driver.findElement(By.cssSelector("input[name='tdsch.xferInd'][value='T']"));
			 //transferMode.click();
			 //((JavascriptExecutor) driver).executeScript("arguments[0].click();", transferMode);

			WindowHandleJS wh = new WindowHandleJS(driver);
			
			 WebElement depAmt = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("depAmt")));
			 wh.setValue(depAmt, inputData.getByIndex(14));
			 WebElement depPerdMths = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("depPerdMths")));
			 wh.setValue(depPerdMths, inputData.getByIndex(15));
			 WebElement depPerdDays = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("depPerdDays")));
			 wh.setValue(depPerdDays, inputData.getByIndex(8));
			 WebElement repayAcct = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("repayAcct")));
			 wh.setValue(repayAcct, inputData.getByIndex(17));
			
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
			

			WindowHandle.slowDown(2);
			WebElement relationCode = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='relnCode']")));
			JavascriptExecutor js1 = (JavascriptExecutor) driver;
			js1.executeScript("arguments[0].value='" + inputData.getByIndex(43) + "';", relationCode);

			
			////////////////////////////////// Second Related Party Tab////////////////////////////////////
			try {

				if (inputData.getByIndex(44).equalsIgnoreCase("Y")) {

					WebElement add = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='relParty_AddNew']")));
					((JavascriptExecutor) driver).executeScript("arguments[0].click();", add);
					WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
					WebElement dropdownElem = wait.until(ExpectedConditions.elementToBeClickable(By.id("relnType")));

					// Use Select to pick by index
					Select dropdown = new Select(dropdownElem);
					dropdown.selectByIndex(45); // selects the 46th option (0-based index)

 
					
					WebElement RelnCode2 = wait.until(ExpectedConditions.elementToBeClickable(By.id("relnCode")));
					String relnCode2 = inputData.getByIndex(46);
					((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", RelnCode2,relnCode2);

					WindowHandleJS wh = new WindowHandleJS(driver);
					 WebElement cifId = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='cifId']")));
					 wh.setValue(cifId, inputData.getByIndex(47));
						Actions actions1 = new Actions(driver);
						actions1.moveToElement(dropdownElem).perform();
	 
//					  WebElement body1 = driver.findElement(By.tagName("body"));
//			            Actions actions1 = new Actions(driver);
//			         
					 //actions1.moveToElement(body1, 0, 1).click().perform();
//					 Actions act = new Actions(driver);
//					 act.moveToElement(element).click().perform();

//					WebElement Title = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='custTitle']")));
//					Title.sendKeys(inputData.getByIndex(48));


					WebElement addressLine = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='custAddrLine1']")));
					addressLine.sendKeys(inputData.getByIndex(50));

				} else {
					System.out.println("⏩ Skipping 'Add' button click as SecondRelatedFlg is not Y.");
				}
			} catch (Exception e) {
				System.out.println("Error in second Related process:: " + e.getMessage());
			}

		} catch (Exception e) {
			System.out.println("Relationship tab Error : " + e.getMessage());
		}
        
        
			/////////////////////////////// RenewalClosure Tab///////////////////////////////////////////////////
			        
			try {
			WindowHandle.slowDown(2);
			WebElement RenewalClosure = wait.until(ExpectedConditions.elementToBeClickable(By.id("tdren")));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", RenewalClosure);
			RenewalClosure.click();
			
			
			if (inputData.getByIndex(19).equalsIgnoreCase("Yes")) {
			
			WebElement autoClosureFlg = driver.findElement(By.xpath("//input[@type='radio' and @value='Y']"));
			autoClosureFlg.click();
			
			} else {
			WebElement autoClosureFlg = driver.findElement(By.xpath("//input[@type='radio' and @value='N']"));
			autoClosureFlg.click();
			}  
			WebElement maxRenew = driver.findElement(By.id("maxRenew"));
			((JavascriptExecutor) driver).executeScript("arguments[0].value='2';", maxRenew);

			@SuppressWarnings("unused")
			WebElement RenewalPeriod1 = driver.findElement(By.id("renewMths"));
			((JavascriptExecutor) driver).executeScript("arguments[0].value='22';", maxRenew);

//			WebElement RenewalPeriod1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("renewMths")));
//			RenewalPeriod1.clear();
//			RenewalPeriod1.sendKeys(inputData.getByIndex(22));
//			
			WebElement AutoRenewalScheme1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("renewSchm")));
			AutoRenewalScheme1.sendKeys(inputData.getByIndex(23));
			
			WebElement AutorenewalGeneralLedgerSubHead1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("renewGLSubHead")));
			AutorenewalGeneralLedgerSubHead1.sendKeys(inputData.getByIndex(24));
			
			WebElement RenewalCCY1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("renewCrncy")));
			RenewalCCY1.sendKeys(inputData.getByIndex(25));
			
			if (inputData.getByIndex(19).equalsIgnoreCase("No")) {
			
			WebElement autoRenewFlg = driver.findElement(By.xpath("//input[@type='radio' and @value='N']"));
			autoRenewFlg.click();
			
			} else {
			WebElement autoRenewFlg = driver.findElement(By.xpath("//input[@type='radio' and @value='U']"));
			autoRenewFlg.click();
			} 
			
			 WindowHandle.safeClick(driver, wait, By.id("Validate"));
			
			} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			
			
			}
			/////////////////////////////// Nomination Tab///////////////////////////////////////////////////
			            
			try {  
				WindowHandle.slowDown(2);
			WebElement NomineeTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("nominationdetails")));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", NomineeTab);
			NomineeTab.click();
			
			WebElement Relnum = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("regValue")));
			Relnum.sendKeys(inputData.getByIndex(26));
			
			
			WindowHandleJS wh = new WindowHandleJS(driver);
			 WebElement cifId = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("nominationdetails.cifId")));
			 wh.setValue(cifId, inputData.getByIndex(27));
			 
		
//			  WebElement body1 = driver.findElement(By.tagName("body"));
//	            Actions actions1 = new Actions(driver);
//	           actions1.moveToElement(body1, 0, 0).click().perform();
			
//			WebElement CifId = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("nominationdetails.cifId")));
//			CifId.sendKeys(inputData.getByIndex(27));
//			Actions actions = new Actions(driver);
//			actions.moveToElement(CifId, 0, 0).click().perform();
			
			//WebElement nomName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='nomName']")));
			//String nomNameValue = inputData.getByIndex(28);
			//((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", nomName, nomNameValue);
			//
			WebElement Relationtype = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='relation']")));
			String relationValue = inputData.getByIndex(28);
			((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", Relationtype, relationValue);
			
			WebElement nomAddrLine1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='nomAddrLine1']")));
			String nomAddrLine1Value = inputData.getByIndex(33);
			((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", nomAddrLine1, nomAddrLine1Value);
			
			//WebElement nomCityCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='nomCityCode']")));
			//String nomCityCodeValue = inputData.getByIndex(31);
			//((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", nomCityCode, nomCityCodeValue);
			//
			//WebElement nomStateCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='nomStateCode']")));
			//String nomStateCodeValue = inputData.getByIndex(32);
			//((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", nomStateCode, nomStateCodeValue);
			//
			//WebElement nomCntryCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='nomCntryCode']")));
			//String nomCntryCodeValue = inputData.getByIndex(33);
			//((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", nomCntryCode, nomCntryCodeValue);
			//
			//WebElement nomPostalCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='nomPostalCode']")));
			//String nomPostalCodeValue = inputData.getByIndex(34);
			//((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", nomPostalCode, nomPostalCodeValue);
			
			WebElement NomPrct = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='nomPcnt']")));
			String Nomineeprct = inputData.getByIndex(29);
			((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", NomPrct, Nomineeprct);
			((JavascriptExecutor) driver).executeScript("document.body.click();");
			
			//////////////////////////////minor nominee ///////////////////////////////////////////////////////////
			
			if(inputData.getByIndex(30).equalsIgnoreCase("Y")) {
			  
			 try {
			  WebElement GuardianName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='grdnName']")));
			 GuardianName.sendKeys(inputData.getByIndex(37));
			  
			  WebElement grdnCode = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@id='grdnCode']"))); 
			  ((JavascriptExecutor) driver).executeScript("arguments[0].click();", grdnCode);
			  WindowHandle.selectByVisibleText(grdnCode, inputData.getByIndex(38));
			  
			  WebElement grdnAddrLine1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("grdnAddrLine1")));
			  String GrdnAddrLine1 = inputData.getByIndex(39);
			  ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", grdnAddrLine1, GrdnAddrLine1);
			  
			  WebElement grdnAddrLine2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='grdnAddrLine2']")));
			  grdnAddrLine2.sendKeys(inputData.getByIndex(40));
			  
			  WebElement grdnCityCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='grdnCityCode']")));
			  grdnCityCode.sendKeys(inputData.getByIndex(41));
			  
			  WebElement grdnStateCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='grdnStateCode']")));
			  grdnStateCode.sendKeys(inputData.getByIndex(42));
			  
			  WebElement grdnCntryCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='grdnCntryCode']")));
			  grdnCntryCode.sendKeys(inputData.getByIndex(43));
			  
			  WebElement grdnPostalCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='grdnPostalCode']")));
			  grdnPostalCode.sendKeys(inputData.getByIndex(44));
			  
			} catch (Exception e) {
			  System.out.println("❌ Error in Minor nominee process: " + e.getMessage());
			  e.printStackTrace();
			}
			
				} else {
			  System.out.println("Skipping!..Nominee is not minor");
				}
			
			
			/////////////////////////////////////Second Nomineee///////////////////////////////
			try {  
			   String SecondNomineeFlag = inputData.getByIndex(45);
			   System.out.println("🔍 Checking SecondNomineeflg: '" + SecondNomineeFlag + "'");
			
			   if (SecondNomineeFlag != null && SecondNomineeFlag.trim().equalsIgnoreCase("Y")) {
			       System.out.println("✅ Condition Passed! Adding Second Nominee...");
			
			       WebElement Nadd = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='nomDetail_AddNew']")));
			       ((JavascriptExecutor) driver).executeScript("arguments[0].click();", Nadd);
			       System.out.println("✅ 'Add' button clicked as SecondNomineeFlg is Y.");
			
			       WebElement S_Cifid = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("nominationdetails.cifId")));
			       String S_cifID = inputData.getByIndex(46);
			       ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", S_Cifid, S_cifID);
			   //    actions1.moveToElement(S_Cifid, 0, 0).click().perform();
			       ((JavascriptExecutor) driver).executeScript("document.body.click();");
			
			       WebElement S_Relationtype = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("nominationdetails.relation")));
			       String S_relationValue = inputData.getByIndex(47);
			       ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", S_Relationtype, S_relationValue);
			       //WindowHandle.slowDown(2);
			
			       WebElement S_NomPrct = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='nomPcnt']")));
			       String S_Nomineeprct = inputData.getByIndex(48);
			       ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", S_NomPrct, S_Nomineeprct);
			
			
			   } else {
			       System.out.println("⏩ Skipping 'Add' button click as SecondNomineeFlg is not Y.");
			   }
			} catch (Exception e) {
			   System.out.println("❌ Error in second nominee process: " + e.getMessage());
			   e.printStackTrace();
			}
			
			
			} catch (Exception e) {
			 System.out.println("Nominee tab error : " + e.getMessage());
			}
			    

			/////////////////////////////// MIS Tab///////////////////////////////////////////////////

			try {
				WindowHandle.slowDown(2);
				WebElement Miscodesdetailstab = wait.until(ExpectedConditions.elementToBeClickable(By.id("miscodes")));
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Miscodesdetailstab);
				Miscodesdetailstab.click();
				WindowHandle.slowDown(3);

				WebElement seccode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sectCode")));
				seccode.sendKeys(inputData.getByIndex(54));

				WebElement subseccode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("subSectCode")));
				subseccode.sendKeys(inputData.getByIndex(55));

				WebElement Occupation1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("occCode")));
				Occupation1.sendKeys(inputData.getByIndex(56));

//				WebElement BorrowerCategory1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("borrCtgry")));
//				BorrowerCategory1.sendKeys(inputData.getByIndex(53));

//				WebElement PurposeOfAdvance1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("purpAdv")));
//				PurposeOfAdvance1.sendKeys(inputData.getByIndex(54));

				WebElement ModeOfAdvance1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modeAdv")));
				ModeOfAdvance1.sendKeys(inputData.getByIndex(57));

//				WebElement AdvanceType1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("typeAdv")));
//				AdvanceType1.sendKeys(inputData.getByIndex(58));

				WebElement NatureOfAdvance = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("natAdv")));
				NatureOfAdvance.sendKeys(inputData.getByIndex(59));
				
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
		
						WebElement Document = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("docCode")));
						JavascriptExecutor js = (JavascriptExecutor) driver;
						js.executeScript("arguments[0].value = arguments[1];", Document, inputData.getByIndex(51));
		
		//				WebElement DueDate1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("dueDate_ui")));
		//				JavascriptExecutor js1 = (JavascriptExecutor) driver;
		//				js1.executeScript("arguments[0].value = arguments[1];", DueDate1, inputData.getByIndex(52));
		
		
		//WebElement ExpiryDate1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("documentdetails.expDate_ui")));
		//ExpiryDate1.sendKeys(inputData.getByIndex(47));

				WebElement ScanDoc = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("docScanFlg")));
				ScanDoc.sendKeys(inputData.getByIndex(53));   

				 WindowHandle.safeClick(driver, wait, By.id("Validate"));

			} catch (Exception e) {
				System.out.println("Error " + e.getMessage());
			}
					try {
					WindowHandle.slowDown(2);
					WebElement otherdetails = wait
							.until(ExpectedConditions.elementToBeClickable(By.id("otherdetails")));
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", otherdetails);
					otherdetails.click();
					
					} catch (Exception e) {
						System.out.println("Error " + e.getMessage());
					}
//					////////////////////////////Tranaction tab////////////////////
//					
//					try {
//						WindowHandle.slowDown(2);
//						WebElement tdtran1 = wait
//								.until(ExpectedConditions.elementToBeClickable(By.id("tdtran")));
//						((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", tdtran1);
//						tdtran1.click();
//						 
//						WindowHandleJS wh = new WindowHandleJS(driver);
//						
//						 WebElement depAmt = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("rptCodeDr")));
//						 wh.setValue(depAmt, inputData.getByIndex(18));
////						WebElement drAcctId = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("drAcctId")));
//										JavascriptExecutor js1 = (JavascriptExecutor) driver;
//										js1.executeScript("arguments[0].value = arguments[1];", drAcctId, inputData.getByIndex(17));
//
//						
//					}catch(Exception e) {
//						System.out.println("Error Message"+e);
//					}
						try{
					   		WebElement submit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Submit")));
					   		submit.click();
					   		
					   		WindowHandle.handlePopupIfExists(driver);
					   		WebElement accept1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accept")));
					   		accept1.click();
					   		driver.switchTo().window(mainWindowHandle);
					   		
							}catch (Exception e) {
								 WindowHandle.checkForApplicationErrors(driver);
							
							}

						try{
					   		
						    driver.switchTo().defaultContent(); // or use driver.switchTo().frame(0); if no name
						    driver.switchTo().frame("loginFrame");
						    driver.switchTo().frame("CoreServer");
						    driver.switchTo().frame("FINW");
						    String successLog = WindowHandle.checkForSuccessElements(driver);
			                if(successLog != null){
			                    System.out.println("✅ Test Success");
			                }
					       	WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='acctNum']")));
					       	String labelText = label.getText(); // Get the text of the label

					        Assert.assertFalse(labelText == null || labelText.isEmpty(), "Test Failed: Account Number label text is empty!");
					        System.out.println("TD Account Created Successfully..! AcctNo: " + labelText);
					    
					        String excelfilePath = System.getProperty("user.dir") + "/resources/TD Testcase 3.xlsx"; // Path to your Excel file
					        String sheetName = "Sheet1"; 
					        int rowNum = 1;  
					       
					        String columnName = "Created_AccountID"; 
					        ExcelUtils2.updateExcel(excelfilePath, sheetName,rowNum, columnName, labelText);
					        
					        LogOut.performLogout(driver, wait);
					        WindowHandle.slowDown(2);		
					      
						}catch (Exception e) {
							System.out.println("Account id creartin Error " + e.getMessage());
							Assert.fail("Test Failed: Exception occurred -> " + e.getMessage());
						}
					 	
	}
}
