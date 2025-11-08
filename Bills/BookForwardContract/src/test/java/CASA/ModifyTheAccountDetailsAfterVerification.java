package CASA;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Base.BaseTest;
import Base.DriverManager;
import Utilities.ExcelUtils;
import Utilities.Inputs;
import Utilities.LogOut;
public class ModifyTheAccountDetailsAfterVerification extends BaseTest{
	

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
        System.out.println("Executing TC01: Savings Account Creation for User: " + userID);
    }
	   @Test(priority = 1)
	   public void createSBAccount() throws Exception {
		   System.out.println("🚀 Creating SB Account...");
		   Inputs account = new Inputs(6,"Sheet1");
            	  
        WebElement searchbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuSelect")));
        searchbox.click();
        searchbox.sendKeys(account.getMenu());

       WebElement element4 = driver.findElement(By.id("menuSearcherGo"));
       element4.click();

       try {
           // Switch to required frames
           wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
           wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));
           
           Thread.sleep(1000);
           WebElement cifIdField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cifId")));
           cifIdField.sendKeys(account.getCifid());
           
           Thread.sleep(1000);
           WebElement schmcode = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("schmCode")));
           schmcode.sendKeys(account.getSchmccd());
           
           WebElement accountId = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='permForacid']")));
           accountId.sendKeys("");

           driver.findElement(By.id("Accept")).click();

       } catch (Exception e) {
       	System.out.println("Error: " + e.getMessage());
       }
       try {

           WebElement Acct_Statement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@id='pbPsFlg']")));
           try {
        	    Select acctStatementSelect = new Select(Acct_Statement);
        	    acctStatementSelect.selectByVisibleText(account.getAcct_Statement());
        	} catch (Exception e) {
        	    System.out.println("Select failed, trying JavaScript...");
        	    ((JavascriptExecutor) driver).executeScript("arguments[0].value = account.getAcct_Statement();", Acct_Statement);
        	}

           WebElement Statement_Freq = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prefCalBase")));
           Statement_Freq.sendKeys(account.getCalBase());
           Thread.sleep(100);
          
           WebElement Type = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("generaldetails.pbPsFreqType")));
           Type.sendKeys(account.getFreqType());
           Thread.sleep(100);
           
           WebElement week = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pbPsFreqWeek")));
           week.sendKeys(account.getFreqWeek());
           Thread.sleep(100);
          
           WebElement day = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pbPsFreqDay")));
           day.sendKeys(account.getFreqDay());
           Thread.sleep(100);
          
           WebElement date = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pbPsFreqStartDD")));
           date.sendKeys(account.getFreqStartDD());
           Thread.sleep(100);
          
           WebElement hlday = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pbPsFreqHldyStat")));
           hlday.sendKeys(account.getFreqHldyStat());
           Thread.sleep(100);
          
           WebElement Dispatch_Mode = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("despatchMode")));
           Dispatch_Mode.sendKeys(account.getDespatchMode());
           Thread.sleep(100);
           
           WebElement ModeOfOperation = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='modeOfOperCode']")));
           ModeOfOperation.sendKeys(account.getModeofoperation());
           
           WebElement Enblefrqforrelatedparty = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tbody/tr[24]/td[2]/input[1]")));
           Enblefrqforrelatedparty.click();

       } catch (Exception e) {
 
       	System.out.println("Genearl tab " + e.getMessage());
 
       }
       ///////////////////////////////interest tab///////////////////////////////////////////////////////////////////////
       try {
           WebElement generalDetailsTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("generaldetails2")));
           ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", generalDetailsTab);
           generalDetailsTab.click();
           
           WebElement CrIntPrtngmin = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='minIntPcntCr']")));
           CrIntPrtngmin.clear();
           CrIntPrtngmin.sendKeys(account.getminIntPcntCr());
           
           WebElement CrIntPrtngmax = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='maxIntPcntCr']")));
           CrIntPrtngmax.clear();
           CrIntPrtngmax.sendKeys(account.getmaxIntPcntCr());
           
           WebElement custPrefIntCr = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='custPrefIntCr']")));
           custPrefIntCr.clear();
           custPrefIntCr.sendKeys(account.getcustPrefIntCr());
           
           WebElement acctPrefIntCr = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='acctPrefIntCr']")));
           acctPrefIntCr.clear();
           acctPrefIntCr.sendKeys(account.getacctPrefIntCr());
           
           WebElement chanPrefIntCr = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='chanPrefIntCr']")));
           chanPrefIntCr.clear();
           chanPrefIntCr.sendKeys(account.getchanPrefIntCr());

           WebElement intCrtAC = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("intCrAcctFlg")));
           Select intCrt = new Select(intCrtAC);
           List<WebElement> options = intCrt.getOptions();
           for (@SuppressWarnings("unused") WebElement option : options) {
           }
           boolean selected = false;
           for (WebElement option : options) {
               if (option.getText().contains(account.getintCrActFlg())) {
                   intCrt.selectByVisibleText(option.getText());
                   selected = true;
                   break;
               }
           }

           if (!selected) {
               System.out.println("Error: No matching value found in dropdown.");
           }
           
           WebElement intCrtACId = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='intDrAcctNum']")));
           intCrtACId.sendKeys("");
           
           WebElement Tax = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@id='wtaxFlg']")));
           Select Taxc = new Select(Tax);
           Taxc.selectByVisibleText("");
           
           WebElement tdsOpAcct = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='tdsOpAcct']")));
           tdsOpAcct.sendKeys("");
           
           } catch (Exception e) {
       	System.out.println("Interest Tab Error" + e.getMessage());
       }
               
       try {
           WebElement dateField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nextIntCrCalcDt_ui")));

           if (dateField.isDisplayed() && dateField.isEnabled()) {
               dateField.click();
               dateField.clear();
               dateField.sendKeys(account.getnextIntCrCalcDt());
           } else {
               System.out.println("Element not interactable, using JavaScript...");
               
               JavascriptExecutor js = (JavascriptExecutor) driver;
               js.executeScript("document.getElementById('nextIntCrCalcDt_ui').removeAttribute('readonly');");
               js.executeScript("document.getElementById('nextIntCrCalcDt_ui').value='account.getnextIntCrCalcDt()';");
           }
       } catch (Exception e) {
           System.out.println("Error entering date: " + e.getMessage());
       }
       ////////////////////////////////schme details//////////////////////////////////
       try {
           WebElement Schmdetailstab = wait.until(ExpectedConditions.elementToBeClickable(By.id("sbschemedetails")));
           ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Schmdetailstab);
           Schmdetailstab.click();
           
           WebElement NomiFlg = wait.until(ExpectedConditions.elementToBeClickable(By.id("availNomFlg")));
           ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", NomiFlg);
           NomiFlg.click();
           
       } catch (Exception e) {
       	System.out.println("Error " + e.getMessage());
       }

       /////////////////////////////////////////////Nominee details/////////////////////////////////////
     try {  
       WebElement NomineeTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("nominationdetails")));
       ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", NomineeTab);
       NomineeTab.click();
       
       WebElement Relnum = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("regValue")));
       Relnum.sendKeys(account.getRegnum());
       
       
       WebElement Cifid = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("nominationdetails.cifId")));
       Cifid.sendKeys(account.getNomineeCifId());
       Actions actions = new Actions(driver);
       actions.moveToElement(Cifid, 0, 0).click().perform();
       
       WebElement Relationtype = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='relation']")));
       String relationValue = account.getrelation();
       ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", Relationtype, relationValue);

       WebElement NomPrct = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='nomPcnt']")));
       String Nomineeprct = account.getNomineePcnt();
       System.out.println("Setting nomineeprct : " + Nomineeprct);
       ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", NomPrct, Nomineeprct);
       
       if(account.getNomineeMinor().equalsIgnoreCase("Y")) {
    	   
    	  try {
    	   WebElement grdnName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='grdnName']")));
    	   grdnName.sendKeys(account.getGuardianName());
    	   
    	   WebElement grdnCode = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@id='grdnCode']"))); 
    	   ((JavascriptExecutor) driver).executeScript("arguments[0].click();", grdnCode);
    	   Select GrdnCode = new Select(grdnCode);
    	   GrdnCode.selectByVisibleText(account.getGuardianCode());
    	   
    	   WebElement grdnAddrLine1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("grdnAddrLine1")));
    	   String GrdnAddrLine1 = account.getAddressLine1();
           ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", grdnAddrLine1, GrdnAddrLine1);
    	   
    	   WebElement grdnAddrLine2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='grdnAddrLine2']")));
    	   grdnAddrLine2.sendKeys(account.getAddressLine2());
    	   
    	   WebElement grdnCityCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='grdnCityCode']")));
    	   grdnCityCode.sendKeys(account.getCity());
    	   
    	   WebElement grdnStateCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='grdnStateCode']")));
    	   grdnStateCode.sendKeys(account.getState());
    	   
    	   WebElement grdnCntryCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='grdnCntryCode']")));
    	   grdnCntryCode.sendKeys(account.getCountry());
    	   
    	   WebElement grdnPostalCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='grdnPostalCode']")));
    	   grdnPostalCode.sendKeys(account.getPostalCode());
    	   
       } catch (Exception e) {
           System.out.println("❌ Error in Minor nominee process: " + e.getMessage());
           e.printStackTrace();
       }

       	} else {
   	    System.out.println("Skipping!..Nominee is not minor");
       	}
       
       try {  
    	    String secondNomineeFlag = account.getSecondNomineeflg();
    	    System.out.println("🔍 Checking SecondNomineeflg: '" + secondNomineeFlag + "'");

    	    if (secondNomineeFlag != null && secondNomineeFlag.trim().equalsIgnoreCase("Y")) {
    	        System.out.println("✅ Condition Passed! Adding Second Nominee...");

    	        WebElement Nadd = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='nomDetail_AddNew']")));
    	        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", Nadd);
    	        System.out.println("✅ 'Add' button clicked as SecondNomineeFlg is Y.");

    	        // ✅ Enter CIF ID using JavaScript to prevent clearing
    	        WebElement S_Cifid = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("nominationdetails.cifId")));
    	        String S_cifID = account.getSecond_NomineeCifId();
    	        ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", S_Cifid, S_cifID);
    	        actions.moveToElement(S_Cifid, 0, 0).click().perform();
    	        System.out.println("✅ Second Nominee CIF ID entered via JavaScript.");

    	        // ✅ Wait for CIF ID to stabilize & verify its presence
    	        Thread.sleep(2000);

    	        // ✅ Re-locate relation field after CIF ID is processed
    	        WebElement S_Relationtype = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("nominationdetails.relation")));
    	        String S_relationValue = account.getSecond_Relation();
    	        ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", S_Relationtype, S_relationValue);
    	        System.out.println("✅ Relationship code entered: " + S_relationValue);

    	        // ✅ Wait before moving to the next field
    	        Thread.sleep(2000);

    	        // ✅ Re-locate & enter Nominee Percentage
    	        WebElement S_NomPrct = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='nomPcnt']")));
    	        String S_Nomineeprct = account.getSecond_NomineePcnt();
    	        ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", S_NomPrct, S_Nomineeprct);
    	        System.out.println("✅ Setting nominee percentage: " + S_Nomineeprct);

    	        //Thread.sleep(1000);

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
     
     Thread.sleep(500);
/////////////////////////////////////////////relationship details//////////////////////////////////
     
     
       try {
           WebElement RelatedPartydetailstab = wait.until(ExpectedConditions.elementToBeClickable(By.id("relatedpartydetails")));
           ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", RelatedPartydetailstab);
           ((JavascriptExecutor) driver).executeScript("arguments[0].click();", RelatedPartydetailstab);

           
          
               WebElement RelnCode = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("relnCode")));
               String relnCode1 = account.getrelnCode();
               ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", RelnCode ,relnCode1);

               Thread.sleep(100);
               try {
            	   
               if (account.getSecond_RelatedPartyflg().equalsIgnoreCase("Y")) {
            	   
               WebElement add = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='relParty_AddNew']")));
               ((JavascriptExecutor) driver).executeScript("arguments[0].click();", add);
               
               WebElement relationtype = wait.until(ExpectedConditions.elementToBeClickable(By.id("relnType")));  
               Select relationDropdown = new Select(relationtype);
               relationDropdown.selectByVisibleText(account.getrelnType());
               
               WebElement RelnCode2 = wait.until(ExpectedConditions.elementToBeClickable(By.id("relnCode")));
               String relnCode2 = account.getRelcode();
               ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", RelnCode2 ,relnCode2);

	           WebElement RType = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("relatedpartydetails.pbPsFreqType")));
	           RType.sendKeys(account.getRelatedFreqType());
	           
	           WebElement Rweek = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("relatedpartydetails.pbPsFreqWeek")));
	           Rweek.sendKeys(account.getRelatedFreqWeek());
	          
	           WebElement Rday = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("relatedpartydetails.pbPsFreqDay")));
	           Rday.sendKeys(account.getRelatedFreqDay());
	          
	           WebElement dRate = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("relatedpartydetails.pbPsFreqStartDD")));
	           dRate.sendKeys(account.getRelatedFreqStartDD());
	          
	           WebElement Rhlday = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("relatedpartydetails.pbPsFreqHldyStat")));
	           Rhlday.sendKeys(account.gethlyday());
	          
	           WebElement Dispatch_Mode = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("relatedpartydetails.despMode")));
	           Dispatch_Mode.sendKeys(account.getRelateddespMode());

               WebElement custtitle = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='custTitle']")));
               custtitle.sendKeys(account.getRelatedCustTitle());
               
               WebElement name = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='custName']")));
               name.sendKeys(account.getRelatedCustName());
               
               WebElement address1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='custAddrLine1']")));
               address1.sendKeys(account.getRelatedCustAddrLine());
               

               } else {
            	   System.out.println("⏩ Skipping 'Add' button click as SecondRelatedFlg is not Y.");
               }
        	   } catch (Exception e) {
        		   System.out.println("Error in second Related process:: " + e.getMessage());
        	   }

           } catch (Exception e) {
               System.out.println("Relationship tab Error : " + e.getMessage());
           }
           

       //////////////////////////Document tab//////////////////////////////////////
      try {

       	WebElement documentDetailsTab = driver.findElement(By.xpath("//a[@id='documentdetails']"));

       	((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", documentDetailsTab);

       	((JavascriptExecutor) driver).executeScript("arguments[0].click();", documentDetailsTab);
           
           WebElement DocCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("docCode")));
           DocCode.sendKeys(account.getdocCode());
           
           WebElement DocScanFlg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("docScanFlg")));
           Select DocScnFlg = new Select(DocScanFlg);
           DocScnFlg.selectByVisibleText(account.getdocScanFlg());

       } catch (Exception e) {
       	System.out.println("Error " + e.getMessage());
       }
      
       /////////////////////////////////////////////FFD tab/////////////////////////////////////////////
      try { WebElement FFDtab = wait.until(ExpectedConditions.elementToBeClickable(By.id("sbffdparameters")));
       ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", FFDtab);
       FFDtab.click();
       
      WebElement ffdschmcode = wait.until(ExpectedConditions.elementToBeClickable(By.name("sbffdparameters.schmCode")));
      ffdschmcode.sendKeys(account.getFFDSchmcode());

      WebElement autosweep = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='autSwpFlg']")));

      if (autosweep.isSelected()) {
          System.out.println("Auto Sweep is Yes, proceeding with remaining fields.");

          WebElement swpDepFreqMnths = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='swpDepFreqMnths']")));
          ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value',account.getSwpDepFreqMnths());", swpDepFreqMnths);
          swpDepFreqMnths.sendKeys(Keys.TAB);
          
          WebElement swpFreqType = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@id='swpFreqType']")));
          Select swpfreqtype = new Select(swpFreqType);
          swpfreqtype.selectByVisibleText(account.getSwpFreqType());

          WebElement swpHldyStat = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@id='swpHldyStat']")));
          Select swphldystat = new Select(swpHldyStat);
          swphldystat.selectByVisibleText(account.getSwpHldyStat());

          WebElement sweepoutinmultiples = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='minBalOperAcct']")));
          sweepoutinmultiples.sendKeys(account.getMinBalOperAcct());

          WebElement repayInstr = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='repayInstr']")));
          repayInstr.sendKeys(account.getRepayInstr());

          System.out.println("All required fields filled successfully.");
      } else {
          System.out.println("Auto Sweep is No, skipping the remaining fields.");
      }

      }catch (Exception e) {
  	   	System.out.println("FFD Tab Error " + e.getMessage());
	   }
      /////////////////////////////////////////Account limit////////////////////////////////////////////////////////
       try {
    	   WebElement accountLimitsTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("acctlmt")));
    	   ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", accountLimitsTab);
    	   ((JavascriptExecutor) driver).executeScript("arguments[0].click();", accountLimitsTab);

    	   Thread.sleep(1000);
           WebElement Exdate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("expiryDate_ui")));
           String exdate = account.getexpiryDate();
           ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", Exdate ,exdate);
           Thread.sleep(1000);

           WebElement DocumentDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("documentDate_ui")));
           String docdate = account.getdocumentDate();
           ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", DocumentDate ,docdate);
           Thread.sleep(1000);

           WebElement DrawingPowerInd = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("drawingPowerInd")));
           Select DrawPowerInd = new Select(DrawingPowerInd);
           DrawPowerInd.selectByVisibleText(account.getdrawingPowerInd());
           Thread.sleep(1000);

       } catch (Exception e) {
       	System.out.println("Account Limit Tab Error  " + e.getMessage());
       }
       
       Thread.sleep(1000);
       /////////////////////////////////////////////miscode tab/////////////////////////////////////////
    try {
       WebElement Miscodesdetailstab = wait.until(ExpectedConditions.elementToBeClickable(By.id("miscodes")));
       ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Miscodesdetailstab);
       Miscodesdetailstab.click();

       WebElement seccode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sectCode")));
       seccode.sendKeys(account.getsectCode());
       WebElement subseccode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("subSectCode")));
       subseccode.sendKeys(account.getsubSectCode());
       WebElement purpadv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("purpAdv")));
       purpadv.sendKeys(account.getpurpAdv());
       WebElement modeadv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modeAdv")));
       modeadv.sendKeys(account.getmodeAdv());
       WebElement typeadv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("typeAdv")));
       typeadv.sendKeys(account.gettypeAdv());
       WebElement natadv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("natAdv")));
       natadv.sendKeys(account.getnatAdv());
       
   } catch (Exception e) {
   	System.out.println("Error " + e.getMessage());
   }
   	try{
   		WebElement submit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Submit")));
   		submit.click();
		}catch (Exception e) {
			System.out.println("Error " + e.getMessage());
		
		}
   	try{
       	WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='AcctNum']")));
       	String labelText = label.getText(); // Get the text of the label

        Assert.assertFalse(labelText == null || labelText.isEmpty(), "Test Failed: Account Number label text is empty!");
        System.out.println("SB Account Created Successfully..! AcctNo: " + labelText);
    
        String filePath = "C:\\Users\\priya\\Finacle\\Core\\CASA\\Resource\\SBData.xlsx"; // Path to your Excel file
        String sheetName = "Sheet1"; // Update with your sheet name
        int rowNum = 1;  // Row where you want to store
        //int colNum = 0;  // Column number where Account Number should be stored
        String columnName = "Created_AccountID"; 
        ExcelUtils.updateExcel(filePath, sheetName,rowNum, columnName, labelText);

	}catch (Exception e) {
		System.out.println("Account id creartin Error " + e.getMessage());
		Assert.fail("Test Failed: Exception occurred -> " + e.getMessage());
	}
   			
    List<WebElement> errorElements = driver.findElements(By.xpath("//td[@class='alert']"));

    if (!errorElements.isEmpty()) {
        String errorMessage = errorElements.get(0).getText();
        System.out.println("Captured Error: " + errorMessage);

    } else {
        System.out.println("No error message found.");
    }
              driver.switchTo().defaultContent();
              LogOut.performLogout(driver, wait);

}}
