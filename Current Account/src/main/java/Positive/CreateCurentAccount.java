package Positive;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.time.Duration;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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
import Utilities.ExcelUtils2;
import Utilities.LogOut;
import Utilities.RowData;
import Utilities.WindowHandle;

public class CreateCurentAccount extends BaseTest {
	
	 private WebDriver driver;
	 private WebDriverWait wait;
	 
	 String excelpath = System.getProperty("user.dir") + "/resources/TestData.xlsx";
	    int row = 1;
	    String sheetname = "Sheet2"; 
	    
		@BeforeClass
		public void setup() throws IOException {
			driver = DriverManager.getDriver();
			wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			String userid = DriverManager.getProperty("userid");
			String password = DriverManager.getProperty("password");
			DriverManager.login(userid, password);
				}
		 @Test(dataProvider = "testcase", dataProviderClass = Dataproviders.class)
		    @ExcelData(sheetName ="Sheet2",rowIndex=1)
	    public void CreateLoan(RowData inputData) throws Exception {
		    	@SuppressWarnings("unused")
				String mainWindowHandle = driver.getWindowHandle();
		    	WindowHandle.slowDown(4);
		    	
		    	WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),inputData.getByIndex(1));

		    	WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
		    	((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

		    	try {
			    	  wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
			          wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));
			          
			          WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cifId"))),inputData.getByIndex(3));

			          WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("schmCode"))),inputData.getByIndex(4));
			          
			          WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("templateFunction"))),inputData.getByIndex(33));
			         
			          WebElement acceptButton = driver.findElement(By.id("Accept"));
			          ((JavascriptExecutor) driver).executeScript("arguments[0].click();", acceptButton);
			          
		    }catch(Exception e) {
		    	System.out.println("Error"+ e);
		    }
	       try {

	         //  WindowHandle.selectDropdownIfValuePresent(driver, wait, By.xpath("//select[@id='pbPsFlg']"), inputData.getByIndex(6));
 
	           WebElement calBase = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prefCalBase")));
	           calBase.sendKeys(inputData.getByIndex(7));
	           WindowHandle.slowDown(2);
	          
	           WebElement Type = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("generaldetails.pbPsFreqType")));
	           Type.sendKeys(inputData.getByIndex(8));
	           
	           WebElement week = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pbPsFreqWeek")));
	           week.sendKeys(inputData.getByIndex(9));
	          
	           WebElement day = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pbPsFreqDay")));
	           day.sendKeys(inputData.getByIndex(10));
	          
	           WebElement date = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pbPsFreqStartDD")));
	           date.sendKeys(inputData.getByIndex(11));
	          
	           WebElement hlday = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pbPsFreqHldyStat")));
	           hlday.sendKeys(inputData.getByIndex(12));
	          
	           WebElement Dispatch_Mode = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("despatchMode")));
	           Dispatch_Mode.sendKeys(inputData.getByIndex(13));
	           
	           WebElement ModeOfOperation = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='modeOfOperCode']")));
	           ModeOfOperation.sendKeys(inputData.getByIndex(14));
	           
	           WebElement Enblefrqforrelatedparty = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tbody/tr[24]/td[2]/input[1]")));
	           Enblefrqforrelatedparty.click();

	       } catch (Exception e) {
	 
	       	System.out.println("Genearl tab " + e.getMessage());
	 
	       }
	       ///////////////////////////////interest tab///////////////////////////////////////////////////////////////////////
	       try {
	    	   WindowHandle.slowDown(2);
	            ((JavascriptExecutor) driver).executeScript("document.getElementById('generaldetails2').click();");
	           
//	           WebElement CrIntPrtngmin = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='minIntPcntCr']")));
//	           CrIntPrtngmin.clear();
//	           CrIntPrtngmin.sendKeys(inputData.getByIndex(15));
//	           
//	           WebElement CrIntPrtngmax = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='maxIntPcntCr']")));
//	           CrIntPrtngmax.clear();
//	           CrIntPrtngmax.sendKeys(inputData.getByIndex(16));
//	           
//	           WebElement custPrefIntCr = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='custPrefIntCr']")));
//	           custPrefIntCr.clear();
//	           custPrefIntCr.sendKeys(inputData.getByIndex(17));
//	           
//	           WebElement acctPrefIntCr = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='acctPrefIntCr']")));
//	           acctPrefIntCr.clear();
//	           acctPrefIntCr.sendKeys(inputData.getByIndex(18));
//	           
//	           WebElement chanPrefIntCr = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='chanPrefIntCr']")));
//	           chanPrefIntCr.clear();
//	           chanPrefIntCr.sendKeys(inputData.getByIndex(19));

	           WindowHandle.selectDropdownIfValuePresent(driver, wait, By.id("intCrAcctFlg"), inputData.getByIndex(20));
	           WindowHandle.selectDropdownIfValuePresent(driver, wait, By.id("intDrAcctFlg"), inputData.getByIndex(20));
	           
//	           if (!inputData.getByIndex(21).equals("S - Original A/c.")) {
//	           
//	           WebElement intCrtACId = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='intDrAcctNum']")));
//	           intCrtACId.sendKeys(inputData.getByIndex(21));
//	           
//	           }
//	           WebElement Tax = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@id='wtaxFlg']")));
//	           WindowHandle.selectByVisibleText(Tax, inputData.getByIndex(22));
//	           
//	           WebElement tdsOpAcct = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='tdsOpAcct']")));
//	           tdsOpAcct.sendKeys(inputData.getByIndex(23));
//	           
//	           } catch (Exception e) {
//	       	System.out.println("Interest Tab Error" + e.getMessage());
//	       }
//	           	String inputDate = inputData.getByIndex(24); 
//	           	String formattedDate = ExcelUtils.formatDate(inputDate, "dd-MMM-yyyy", "dd-MM-yyyy");
//		        WebElement dateField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nextIntCrCalcDt_ui")));
//		        dateField.sendKeys(formattedDate);
	       }catch(Exception e) {
	    	   System.out.println("Error Message"+e);
	       }
	    	   ////////////////////////////////scheme details//////////////////////////////////
	       try {
	           WebElement Schmdetailstab = wait.until(ExpectedConditions.elementToBeClickable(By.id("sbschemedetails")));
	           ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Schmdetailstab);
	           Schmdetailstab.click();
	           
//	           WebElement NomiFlg = wait.until(ExpectedConditions.elementToBeClickable(By.id("availNomFlg")));
//	           ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", NomiFlg);
//	           NomiFlg.click();
	           
	           WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	        // Locate the "No" radio button using value='N'
	        WebElement noRadioButton = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//input[@name='sbschemedetails.availNomFlg' and @value='N']")));

	        // Scroll it into view
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", noRadioButton);

	        // Click using JavaScript (most reliable for Finacle UIs)
	        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", noRadioButton);

	        System.out.println("✅ 'No' radio button clicked successfully.");
	        driver.findElement(By.id("Validate")).click();

	           
	       } catch (Exception e) {
	       	System.out.println("Error " + e.getMessage());
	       }

	       /////////////////////////////////////////////Nominee details/////////////////////////////////////
	     try {  
           WebElement NomineeTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("nominationdetails")));
           ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", NomineeTab);
           NomineeTab.click();
           
           WebElement Relnum = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("regValue")));
           Relnum.sendKeys(inputData.getByIndex(25));
           
           
           WebElement Cifid = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("nominationdetails.cifId")));
           Cifid.sendKeys(inputData.getByIndex(26));
           ((JavascriptExecutor) driver).executeScript("document.body.click();");
//           Actions actions = new Actions(driver);
//           actions.moveToElement(Cifid, 0, 0).click().perform();
           
           WebElement Relationtype = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='relation']")));
           String relationValue = inputData.getByIndex(27);
           WindowHandle.slowDown(1);
           ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", Relationtype, relationValue);

           WebElement NomPrct = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='nomPcnt']")));
           String Nomineeprct = inputData.getByIndex(28);
           ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", NomPrct, Nomineeprct);
           //////////////////////////////minor nominee ///////////////////////////////////////////////////////////
           if(inputData.getByIndex(29).equalsIgnoreCase("Y")) {
        	   
        	  try {
        	   WebElement grdnName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='grdnName']")));
        	   grdnName.sendKeys(inputData.getByIndex(30));
        	   
        	   WebElement grdnCode = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@id='grdnCode']"))); 
        	   ((JavascriptExecutor) driver).executeScript("arguments[0].click();", grdnCode);
        	   WindowHandle.selectByVisibleText(grdnCode, inputData.getByIndex(31));
        	   
        	   WebElement grdnAddrLine1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("grdnAddrLine1")));
        	   String GrdnAddrLine1 = inputData.getByIndex(32);
               ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", grdnAddrLine1, GrdnAddrLine1);
        	   
        	   WebElement grdnAddrLine2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='grdnAddrLine2']")));
        	   grdnAddrLine2.sendKeys(inputData.getByIndex(33));
        	   
        	   WebElement grdnCityCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='grdnCityCode']")));
        	   grdnCityCode.sendKeys(inputData.getByIndex(34));
        	   
        	   WebElement grdnStateCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='grdnStateCode']")));
        	   grdnStateCode.sendKeys(inputData.getByIndex(35));
        	   
        	   WebElement grdnCntryCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='grdnCntryCode']")));
        	   grdnCntryCode.sendKeys(inputData.getByIndex(36));
        	   
        	   WebElement grdnPostalCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='grdnPostalCode']")));
        	   grdnPostalCode.sendKeys(inputData.getByIndex(37));
        	   
           } catch (Exception e) {
               System.out.println("❌ Error in Minor nominee process: " + e.getMessage());
               e.printStackTrace();
           }
 
           	} else {
       	    System.out.println("Skipping!..Nominee is not minor");
           	}
           /////////////////////////////////////second nomineee///////////////////////////////
           try {  
        	    String secondNomineeFlag = inputData.getByIndex(38);
        	    System.out.println("🔍 Checking SecondNomineeflg: '" + secondNomineeFlag + "'");

        	    if (secondNomineeFlag != null && secondNomineeFlag.trim().equalsIgnoreCase("Y")) {
        	        System.out.println("✅ Condition Passed! Adding Second Nominee...");

        	        WebElement Nadd = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='nomDetail_AddNew']")));
        	        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", Nadd);
        	        System.out.println("✅ 'Add' button clicked as SecondNomineeFlg is Y.");

        	        WebElement S_Cifid = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("nominationdetails.cifId")));
        	        String S_cifID = inputData.getByIndex(39);
        	        ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", S_Cifid, S_cifID);
//        	        actions.moveToElement(S_Cifid, 0, 0).click().perform();
        	        ((JavascriptExecutor) driver).executeScript("document.body.click();");
        	        WindowHandle.slowDown(2);

        	        WebElement S_Relationtype = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("nominationdetails.relation")));
        	        String S_relationValue = inputData.getByIndex(40);
        	        ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", S_Relationtype, S_relationValue);
        	        WindowHandle.slowDown(2);

        	        WebElement S_NomPrct = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='nomPcnt']")));
        	        String S_Nomineeprct = inputData.getByIndex(41);
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
	     //////////////////////////Document tab//////////////////////////////////////
		      try {

		       	WebElement documentDetailsTab = driver.findElement(By.xpath("//a[@id='documentdetails']"));

		       	((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", documentDetailsTab);

		       	((JavascriptExecutor) driver).executeScript("arguments[0].click();", documentDetailsTab);
		           
		           WebElement DocCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("docCode")));
		           DocCode.sendKeys(inputData.getByIndex(55));

		           WindowHandle.selectDropdownIfValuePresent(driver, wait, By.id("docScanFlg"), inputData.getByIndex(56));


		       } catch (Exception e) {
		       	System.out.println("Error " + e.getMessage());
		       }
		      
		       /////////////////////////////////////////////FFD tab/////////////////////////////////////////////
		      if (inputData.getByIndex(57).equalsIgnoreCase("Y")) {
		      try { WebElement FFDtab = wait.until(ExpectedConditions.elementToBeClickable(By.id("sbffdparameters")));
		       ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", FFDtab);
		       FFDtab.click();
		       
	          WebElement ffdschmcode = wait.until(ExpectedConditions.elementToBeClickable(By.name("sbffdparameters.schmCode")));
	          ffdschmcode.sendKeys(inputData.getByIndex(58));
	          Actions actions = new Actions(driver);
	          actions.moveToElement(ffdschmcode, 0, 0).click().perform();

	          WebElement autosweep = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='autSwpFlg']")));

	          if (autosweep.isSelected()) {
	              System.out.println("Auto Sweep is Yes, proceeding with remaining fields.");

	              WebElement swpDepFreqMnths = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='swpDepFreqMnths']")));
	              ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value',inputData[59]);", swpDepFreqMnths);
	              swpDepFreqMnths.sendKeys(Keys.TAB);

	              WindowHandle.selectDropdownIfValuePresent(driver, wait, By.xpath("//select[@id='swpFreqType']"), inputData.getByIndex(61));
	              
	              WindowHandle.selectDropdownIfValuePresent(driver, wait, By.xpath("//select[@id='swpHldyStat']"), inputData.getByIndex(62));

	              WebElement sweepoutinmultiples = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='minBalOperAcct']")));
	              sweepoutinmultiples.sendKeys(inputData.getByIndex(63));

	              WebElement repayInstr = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='repayInstr']")));
	              repayInstr.sendKeys(inputData.getByIndex(64));

	              System.out.println("All required fields filled successfully.");
	          } else {
	              System.out.println("Auto Sweep is No, skipping the remaining fields.");
	          }

		      }catch (Exception e) {
		  	   	System.out.println("FFD Tab Error " + e.getMessage());
			   }
		      }
		      /////////////////////////////////////////Account limit////////////////////////////////////////////////////////
		       try {
		    	   WebElement accountLimitsTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("acctlmt")));
		    	   ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", accountLimitsTab);
		    	   ((JavascriptExecutor) driver).executeScript("arguments[0].click();", accountLimitsTab);

		    	   String exdate = inputData.getByIndex(65); 
		           String Expdate = ExcelUtils.formatDate(exdate, "dd-MMM-yyyy", "dd-MM-yyyy");
		           WebElement Exdate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("expiryDate_ui")));
		           Exdate.sendKeys(Expdate);

		           String DocDate = inputData.getByIndex(66); 
		           String DocuDate = ExcelUtils.formatDate(DocDate, "dd-MMM-yyyy", "dd-MM-yyyy");
		           WebElement DocumentDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("documentDate_ui")));
		           DocumentDate.sendKeys(DocuDate);

	               WindowHandle.selectDropdownIfValuePresent(driver, wait, By.id("drawingPowerInd"), inputData.getByIndex(67));

		       } catch (Exception e) {
		       	System.out.println("Account Limit Tab Error  " + e.getMessage());
		       }
		       
		       WindowHandle.slowDown(2);
		       /////////////////////////////////////////////miscode tab/////////////////////////////////////////
		    try {
		       WebElement Miscodesdetailstab = wait.until(ExpectedConditions.elementToBeClickable(By.id("miscodes")));
		       ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Miscodesdetailstab);
		       Miscodesdetailstab.click();
//
//		       WebElement seccode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sectCode")));
//		       seccode.sendKeys(inputData.getByIndex(68));
		       WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sectCode"))),inputData.getByIndex(68));
		       WebElement subseccode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("subSectCode")));
		       subseccode.sendKeys(inputData.getByIndex(69));
		       WebElement purpadv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("purpAdv")));
		       purpadv.sendKeys(inputData.getByIndex(70));
		       WebElement modeadv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modeAdv")));
		       modeadv.sendKeys(inputData.getByIndex(71));
		       WebElement typeadv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("typeAdv")));
		       typeadv.sendKeys(inputData.getByIndex(72));
		       WebElement natadv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("natAdv")));
		       natadv.sendKeys(inputData.getByIndex(73));
		       
		       WebElement submit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Submit")));
		   		submit.click();

	           } catch (Exception e) {
	               System.out.println("Relationship tab Error : " + e.getMessage());
	           }
	     
	   	     WindowHandle.slowDown(2);
	   	   /////////////////////////////////////////////relationship details//////////////////////////////////
	   	         
	   	         
	   		       try {
	   		           WebElement RelatedPartydetailstab = wait.until(ExpectedConditions.elementToBeClickable(By.id("relatedpartydetails")));
	   		           ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", RelatedPartydetailstab);
	   		           ((JavascriptExecutor) driver).executeScript("arguments[0].click();", RelatedPartydetailstab);

	   		           
	   		          
	   		               WebElement RelnCode = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("relnCode")));
	   		               String relnCode1 = inputData.getByIndex(42);
	   		               ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", RelnCode ,relnCode1);

	   		               WindowHandle.slowDown(2);
	   		               //////////////////////////////////second rel////////////////////////////////////
	   		               try {
	   		            	   
	   		               if (inputData.getByIndex(43).equalsIgnoreCase("Y")) {
	   		            	   
	   		               WebElement add = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='relParty_AddNew']")));
	   		               ((JavascriptExecutor) driver).executeScript("arguments[0].click();", add);
	   		               
	   		               WindowHandle.slowDown(1);
	   		               WindowHandle.selectDropdownIfValuePresent(driver, wait, By.id("relnType"), inputData.getByIndex(44));
	   		               
	   		               WebElement RelnCode2 = wait.until(ExpectedConditions.elementToBeClickable(By.id("relnCode")));
	   		               String relnCode2 = inputData.getByIndex(45);
	   		               ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", RelnCode2 ,relnCode2);

	   			           WebElement RType = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("relatedpartydetails.pbPsFreqType")));
	   			           RType.sendKeys(inputData.getByIndex(46));
	   			           
	   			           WebElement Rweek = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("relatedpartydetails.pbPsFreqWeek")));
	   			           Rweek.sendKeys(inputData.getByIndex(47));
	   			          
	   			           WebElement Rday = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("relatedpartydetails.pbPsFreqDay")));
	   			           Rday.sendKeys(inputData.getByIndex(48));
	   			          
	   			           WebElement dRate = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("relatedpartydetails.pbPsFreqStartDD")));
	   			           dRate.sendKeys(inputData.getByIndex(49));
	   			          
	   			           WebElement Rhlday = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("relatedpartydetails.pbPsFreqHldyStat")));
	   			           Rhlday.sendKeys(inputData.getByIndex(50));
	   			          
	   			           WebElement Dispatch_Mode = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("relatedpartydetails.despMode")));
	   			           Dispatch_Mode.sendKeys(inputData.getByIndex(51));

	   		               WebElement custtitle = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='custTitle']")));
	   		               custtitle.sendKeys(inputData.getByIndex(52));
	   		               
	   		               WebElement name = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='custName']")));
	   		               name.sendKeys(inputData.getByIndex(53));
	   		               
	   		               WebElement address1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='custAddrLine1']")));
	   		               address1.sendKeys(inputData.getByIndex(54));
	   		               

	   		               } else {
	   		            	   System.out.println("⏩ Skipping 'Add' button click as SecondRelatedFlg is not Y.");
	   		               }
	   	            	   } catch (Exception e) {
	   	            		   System.out.println("Error in second Related process:: " + e.getMessage());
	   	            	   }
	   	   
	   		           } catch (Exception e) {
	   		               System.out.println("Relationship tab Error : " + e.getMessage());
	   		           }
	   		           	   		
	   		   
	   	try{
	   		WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(By.id("Submit")));
    	    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submit);
    	    System.out.println("✅ Submit clicked successfully.");

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

        try {
            WindowHandle.ValidationFrame(driver);

            String successLog = WindowHandle.checkForSuccessElements(driver);
            if(successLog != null){
                System.out.println("✅ Test Success");
                
            WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='AcctNum']")));
    	      String labelText = label.getText(); // Get the text of the label

    	       Assert.assertFalse(labelText == null || labelText.isEmpty(), "Test Failed: Account Number label text is empty!");
    	      
            String excelfilePath = excelpath;
            String sheetName = sheetname;
            int rowNum = row;
            String columnName = "Created_AccountID";

            ExcelUtils2.updateExcel(excelfilePath, sheetName, rowNum, columnName, labelText);
            }
        } catch (Exception e) {
            System.out.println("❌ Account id creation Error: " + e.getMessage());
            Assert.fail("Test Failed: Exception occurred -> " + e.getMessage());
        }
		 driver.switchTo().defaultContent();
			LogOut.performLogout(driver, wait);
		 
 
		 }

		 }
