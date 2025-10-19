package CASA;

import java.time.Duration;
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
import Base.BaseTest;
import Utilities.DateUtils;
import Utilities.ExcelUtils2;
import Utilities.RowData;
import Utilities.WindowHandle;

public class SBA extends BaseTest {
	private WebDriver driver;
	 private WebDriverWait wait;

	    public SBA(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
	    }
	    	    
	    public String execute(RowData inputData , String sheetname,int row,String excelpath) throws Exception {
	    	String mainWindowHandle = driver.getWindowHandle();
	    	WindowHandle.slowDown(4);
	    	
	    	WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),inputData.getByIndex(1));

	    	WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
	    	((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

	    	try {
		    	  wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
		          wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));
		          
		          WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cifId"))),inputData.getByIndex(2));

		          WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("schmCode"))),inputData.getByIndex(3));
		          
		          WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("permForacid"))),inputData.getByIndex(4));
		         
		          WebElement acceptButton = driver.findElement(By.id("Accept"));
		          ((JavascriptExecutor) driver).executeScript("arguments[0].click();", acceptButton);
		          
	    }catch(Exception e) {
	    	System.out.println("Error"+ e);
	    }

		//////////////////////////// General Details/////////////////////////

	try {
			
		WindowHandle.selectDropdownIfValuePresent(driver, wait, By.xpath("//select[@id='pbPsFlg']"), inputData.getByIndex(5));
		 
		if (!inputData.getByIndex(5).equalsIgnoreCase("N - None") && !inputData.getByIndex(5).equalsIgnoreCase("P - Passbook")) {
		WindowHandle.selectDropdownIfValuePresent(driver,wait, By.id("prefCalBase"),inputData.getByIndex(6));

        WindowHandle.selectDropdownIfValuePresent(driver,wait,(By.name("generaldetails.pbPsFreqType")),inputData.getByIndex(7));
        
        WindowHandle.selectDropdownIfValuePresent(driver,wait,(By.id("pbPsFreqWeek")),inputData.getByIndex(8));
        
        WindowHandle.selectDropdownIfValuePresent(driver,wait,(By.id("pbPsFreqStartDD")),inputData.getByIndex(9));
        
        WindowHandle.selectDropdownIfValuePresent(driver,wait,(By.id("pbPsFreqHldyStat")),inputData.getByIndex(10));
        
        WindowHandle.selectDropdownIfValuePresent(driver,wait,(By.id("pbPsFreqCalBase")),inputData.getByIndex(11));
        
        WindowHandle.selectDropdownIfValuePresent(driver,wait,(By.id("despatchMode")),inputData.getByIndex(12));
        
		}

		WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("modeOfOperCode"))),inputData.getByIndex(13));
		
		String psAtRelPartyFlg = inputData.getByIndex(14);
		
				if(psAtRelPartyFlg.equalsIgnoreCase("Y")) {
				WebElement collectChargesYes = driver.findElement(By.xpath("//input[@name='generaldetails.psAtRelPartyFlg' and @value='Y']"));
				collectChargesYes.click();
				
				}else {
					
				WebElement collectChargesNo = driver.findElement(By.xpath("//input[@name='generaldetails.psAtRelPartyFlg' and @value='N']"));
				collectChargesNo.click();
				
				}
		
		String collectcharge = inputData.getByIndex(15);
		
					if(collectcharge.equalsIgnoreCase("Y")) {
					WebElement collectChargesYes = driver.findElement(By.xpath("//input[@name='generaldetails.collectCharges' and @value='Y']"));
					collectChargesYes.click();
					
					}else {
						
					WebElement collectChargesNo = driver.findElement(By.xpath("//input[@name='generaldetails.collectCharges' and @value='N']"));
					collectChargesNo.click();
					}
					
		}catch(Exception e) {
					System.out.println("General tab Error :"+e);
		}

	    ////////////////////////Interest Details/////////////////////////
	    
	try {
		
	    WebElement generalDetailsTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("generaldetails2")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", generalDetailsTab);
	    generalDetailsTab.click();

	    // minIntPcntCr
		    WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("minIntPcntCr"))),inputData.getByIndex(16));
	
		    // maxIntPcntCr
		    WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("maxIntPcntCr"))),inputData.getByIndex(17));
	
		    // custPrefIntCr
		    WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("custPrefIntCr"))),inputData.getByIndex(18));
		    
		    // acctPrefIntCr
		    WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("acctPrefIntCr"))),inputData.getByIndex(19));
	
		    // chanPrefIntCr
		    WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("chanPrefIntCr"))),inputData.getByIndex(20));
	
		    // intCrAcctFlg dropdown
		    WindowHandle.selectDropdownIfValuePresent(driver,wait,(By.id("intCrAcctFlg")),inputData.getByIndex(21));

	    // intDrAcctNum input (only if not "S - Original A/c.")
	    if (!inputData.getByIndex(17).equals("S - Original A/c.")) {
	    	
	    	 WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("intDrAcctNum"))),inputData.getByIndex(22));

	    }

	    String inputDate = inputData.getByIndex(25);
	    String formattedDate = DateUtils.toDDMMYYYY(inputDate);
	    WindowHandle.setValueWithJS(
	        driver,
	        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nextIntCrCalcDt_ui"))),
	        formattedDate
	    );

	    // Tax dropdown
	    WindowHandle.selectDropdownIfValuePresent(driver,wait,(By.id("wtaxFlg")),inputData.getByIndex(23));

	    // tdsOpAcct input
	    WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tdsOpAcct"))),inputData.getByIndex(24));

	} catch (Exception e) {
	    System.out.println("Interest Tab Error: " + e);
	}

    ////////////////////////////////scheme details//////////////////////////////////
    try {
        WebElement Schmdetailstab = wait.until(ExpectedConditions.elementToBeClickable(By.id("sbschemedetails")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Schmdetailstab);
        Schmdetailstab.click();
        
		String ChqAlwdFlg = inputData.getByIndex(26);
		
		if(ChqAlwdFlg.equalsIgnoreCase("Y"))
		{
		WebElement chqAlwdFlg = driver.findElement(By.xpath("//input[@name='sbschemedetails.chqAlwdFlg' and @value='Y']"));
		chqAlwdFlg.click();
		
		}else {
			
		WebElement chqAlwdFlg = driver.findElement(By.xpath("//input[@name='sbschemedetails.chqAlwdFlg' and @value='N']"));
		chqAlwdFlg.click();
		}
		
		String availNomFlg = inputData.getByIndex(27);
		
		if(availNomFlg.equalsIgnoreCase("Y"))
		{
			WebElement chqAlwdFlg = driver.findElement(By.xpath("//input[@name='sbschemedetails.availNomFlg' and @value='Y']"));
			chqAlwdFlg.click();
		
		    ////////////////////////////////NOINEE details//////////////////////////////////
			try { 
				
		    	WebElement NomineeTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("nominationdetails")));
		        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", NomineeTab);
		        NomineeTab.click();
		
		        WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("regValue"))),inputData.getByIndex(28));
		
		        // Set CIF ID
		        WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(
		        		By.name("nominationdetails.cifId"))),inputData.getByIndex(29));
		        
		        // Set Relation type
		        WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("relation"))),inputData.getByIndex(30));
		
		        // Set Nominee percentage
		        WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nomPcnt"))),inputData.getByIndex(31));

			} catch (Exception e) {	
			    System.out.println("Error " + e.getMessage());
			}	        
	    
				// Minor nominee block
				if (inputData.getByIndex(32).equalsIgnoreCase("Y")) {
					try {
			            WebElement grdnName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='grdnName']")));
			            ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", grdnName, inputData.getByIndex(33));
		
			            WebElement grdnCode = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@id='grdnCode']")));
			            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", grdnCode);
			            WindowHandle.selectByVisibleText(grdnCode, inputData.getByIndex(34));
		
			            WebElement grdnAddrLine1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("grdnAddrLine1")));
			            ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", grdnAddrLine1, inputData.getByIndex(35));
		
			            WebElement grdnAddrLine2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='grdnAddrLine2']")));
			            ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", grdnAddrLine2, inputData.getByIndex(36));
		
			            WebElement grdnCityCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='grdnCityCode']")));
			            ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", grdnCityCode, inputData.getByIndex(37));
		
			            WebElement grdnStateCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='grdnStateCode']")));
			            ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", grdnStateCode, inputData.getByIndex(38));
		
			            WebElement grdnCntryCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='grdnCntryCode']")));
			            ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", grdnCntryCode, inputData.getByIndex(39));
		
			            WebElement grdnPostalCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='grdnPostalCode']")));
			            ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", grdnPostalCode, inputData.getByIndex(40));
		
			            // Trigger change event for all guardian fields using IE11-safe method
			            String ieChangeEvent = 
			                "var evt = document.createEvent('HTMLEvents');" +
			                "evt.initEvent('change', true, false);" +
			                "arguments[0].dispatchEvent(evt);";
			            ((JavascriptExecutor) driver).executeScript(ieChangeEvent, grdnName);
			            ((JavascriptExecutor) driver).executeScript(ieChangeEvent, grdnAddrLine1);
			            ((JavascriptExecutor) driver).executeScript(ieChangeEvent, grdnAddrLine2);
			            ((JavascriptExecutor) driver).executeScript(ieChangeEvent, grdnCityCode);
			            ((JavascriptExecutor) driver).executeScript(ieChangeEvent, grdnStateCode);
			            ((JavascriptExecutor) driver).executeScript(ieChangeEvent, grdnCntryCode);
			            ((JavascriptExecutor) driver).executeScript(ieChangeEvent, grdnPostalCode);

		        } catch (Exception e) {
		            System.out.println("❌ Error in Minor nominee process: " + e.getMessage());
		            e.printStackTrace();
		        }
		    } else {
		        System.out.println("Skipping!..Nominee is not minor");
		    }
			    
				/////////////////////////////////// SECOND NOMINEE ///////////////////////////////
				try {  
					String secondNomineeFlag = inputData.getByIndex(41);
					System.out.println("🔍 Checking SecondNomineeflg: '" + secondNomineeFlag + "'");
			
					if (secondNomineeFlag != null && secondNomineeFlag.trim().equalsIgnoreCase("Y")) {
						
						System.out.println("✅ Condition Passed! Adding Second Nominee...");
						
						WebElement Nadd = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='nomDetail_AddNew']")));
						((JavascriptExecutor) driver).executeScript("arguments[0].click();", Nadd);
						System.out.println("✅ 'Add' button clicked as SecondNomineeFlg is Y.");
						
						WebElement S_Cifid = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("nominationdetails.cifId")));
						String S_cifID = inputData.getByIndex(42);
						((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", S_Cifid, S_cifID);
						WindowHandle.slowDown(2);
						
						WebElement S_Relationtype = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("nominationdetails.relation")));
						String S_relationValue = inputData.getByIndex(43);
						((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", S_Relationtype, S_relationValue);
						WindowHandle.slowDown(2);
						
						WebElement S_NomPrct = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='nomPcnt']")));
						String S_Nomineeprct = inputData.getByIndex(44);
						((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", S_NomPrct, S_Nomineeprct);
			
					} else {
					System.out.println("⏩ Skipping 'Add' button click as SecondNomineeFlg is not Y.");
					}
			} catch (Exception e) {
			System.out.println("❌ Error in second nominee process: " + e.getMessage());
			e.printStackTrace();
			}

		}else {
			
		WebElement chqAlwdFlg = driver.findElement(By.xpath("//input[@name='sbschemedetails.availNomFlg' and @value='N']"));
		chqAlwdFlg.click();
		}

    } catch (Exception e) {
    	System.out.println("Error " + e.getMessage());
    }
    

		 WindowHandle.slowDown(2);
	//////////////////////////////////////////////////////////////relationship details//////////////////////////////////////////////
		         
		         
		  try {
		       WebElement RelatedPartydetailstab = wait.until(ExpectedConditions.elementToBeClickable(By.id("relatedpartydetails")));
		       ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", RelatedPartydetailstab);
		       ((JavascriptExecutor) driver).executeScript("arguments[0].click();", RelatedPartydetailstab);
		          
		       WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("relnCode"))),inputData.getByIndex(45));
			   WindowHandle.slowDown(2);  
			//////////////////////////////////Second Related Party ////////////////////////////////////
			try {
				if (inputData.getByIndex(46).equalsIgnoreCase("Y")) {

				WebElement add = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("relParty_AddNew")));
				 ((JavascriptExecutor) driver).executeScript("var el = arguments[0];" + "if (document.createEvent) {" +
				"  var evt = document.createEvent('MouseEvents');" + "  evt.initEvent('click', true, true);" + "  el.dispatchEvent(evt);" +
				"} else if (el.click) { el.click(); }", add);
				 System.out.println("Second Related Party 'Add New' clicked successfully!");
				 WindowHandle.slowDown(1);

			WindowHandle.selectDropdownIfValuePresent(driver, wait, By.id("relnType"), inputData.getByIndex(47));
			
			WebElement RelnCode2 = wait.until(ExpectedConditions.elementToBeClickable(By.id("relnCode")));
			String relnCode2 = inputData.getByIndex(48);
			((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", RelnCode2, relnCode2);

			((JavascriptExecutor) driver).executeScript(
			"document.getElementsByName('relatedpartydetails.pbPsFreqType')[0].value=arguments[0];", 
		    inputData.getByIndex(49));
		
			((JavascriptExecutor) driver).executeScript(
			"document.getElementsByName('relatedpartydetails.pbPsFreqWeek')[0].value=arguments[0];", 
			inputData.getByIndex(50));
				
			((JavascriptExecutor) driver).executeScript(
			"document.getElementsByName('relatedpartydetails.pbPsFreqDay')[0].value=arguments[0];", 
			inputData.getByIndex(51));
				
			((JavascriptExecutor) driver).executeScript(
			"document.getElementsByName('relatedpartydetails.pbPsFreqStartDD')[0].value=arguments[0];", 
			inputData.getByIndex(52));
				
			((JavascriptExecutor) driver).executeScript(
			"document.getElementsByName('relatedpartydetails.pbPsFreqHldyStat')[0].value=arguments[0];", 
			inputData.getByIndex(53));
				
			((JavascriptExecutor) driver).executeScript(
			"document.getElementsByName('relatedpartydetails.despMode')[0].value=arguments[0];", 
			inputData.getByIndex(54));

			WebElement custtitle = wait.until(ExpectedConditions.elementToBeClickable(By.id("custTitle")));
			((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", custtitle, inputData.getByIndex(55));
				
			WebElement name = wait.until(ExpectedConditions.elementToBeClickable(By.id("custName")));
			((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", name, inputData.getByIndex(56));
				
			WebElement address1 = wait.until(ExpectedConditions.elementToBeClickable(By.id("custAddrLine1")));
			((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", address1, inputData.getByIndex(57));

				} else {
				System.out.println("Skipping Second Related Party (Flag not Y)");
				}
			} catch (Exception e) {
			System.out.println("❌ Error while clicking Add button for Second Related Party: " + e.getMessage());
			}
      
		  } catch (Exception e) {
			System.out.println("❌ Error in second nominee process: " + e.getMessage());
			e.printStackTrace();
		  }
		      
		//////////////////////////Document tab //////////////////////////
		try {
		WebElement documentDetailsTab = driver.findElement(By.xpath("//a[@id='documentdetails']"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", documentDetailsTab);
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", documentDetailsTab);

		WebElement DocCode = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("docCode")));
		((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", DocCode, inputData.getByIndex(58));

		WebElement docScanFlg = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("docScanFlg")));
		Select dropdown = new Select(docScanFlg);
		dropdown.selectByVisibleText(inputData.getByIndex(59));   
		
		} catch (Exception e) {
		System.out.println("Error in Document Tab: " + e.getMessage());
		}
		 /////////////////////////////////////////////FFD tab/////////////////////////////////////////////
	     if (inputData.getByIndex(60).equalsIgnoreCase("Y")) {
		     try { WebElement FFDtab = wait.until(ExpectedConditions.elementToBeClickable(By.id("sbffdparameters")));
		      ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", FFDtab);
		      FFDtab.click();
	     
	          WebElement ffdschmcode = wait.until(ExpectedConditions.elementToBeClickable(By.name("sbffdparameters.schmCode")));
	          ffdschmcode.sendKeys(inputData.getByIndex(61));
	          Actions actions = new Actions(driver);
	          actions.moveToElement(ffdschmcode, 0, 0).click().perform();

	          WebElement autosweep = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='autSwpFlg']")));

	          if (autosweep.isSelected()) {
	              System.out.println("Auto Sweep is Yes, proceeding with remaining fields.");

	              WebElement swpDepFreqMnths = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='swpDepFreqMnths']")));
	              ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value',inputData[63]);", swpDepFreqMnths);
	              swpDepFreqMnths.sendKeys(Keys.TAB);

	              WindowHandle.selectDropdownIfValuePresent(driver, wait, By.xpath("//select[@id='swpFreqType']"), inputData.getByIndex(64));
	             
	              WindowHandle.selectDropdownIfValuePresent(driver, wait, By.xpath("//select[@id='swpHldyStat']"), inputData.getByIndex(65));

	              WebElement sweepoutinmultiples = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='minBalOperAcct']")));
	              sweepoutinmultiples.sendKeys(inputData.getByIndex(66));

	              WebElement repayInstr = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='repayInstr']")));
	              repayInstr.sendKeys(inputData.getByIndex(67));

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
		    
		    String EXDate = inputData.getByIndex(68);
		    String formattedDate = DateUtils.toDDMMYYYY(EXDate);
		    WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("expiryDate_ui"))),
		    formattedDate );

		    String DocDate = inputData.getByIndex(69);
		    String formattedDocDate = DateUtils.toDDMMYYYY(DocDate);
		    WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("documentDate_ui"))),
		    formattedDocDate);
		     
	      WindowHandle.slowDown(1);
	      WindowHandle.selectDropdownIfValuePresent(driver, wait, By.xpath("//select[@id='drawingPowerInd']"), inputData.getByIndex(70));
	         
		} catch (Exception e) {
		System.out.println("Error in Account Limit Tab: " + e.getMessage());
		}
	      
	      WindowHandle.slowDown(1);
	      /////////////////////////////////////////////miscode tab/////////////////////////////////////////
	   try {
	      WebElement Miscodesdetailstab = wait.until(ExpectedConditions.elementToBeClickable(By.id("miscodes")));
	      ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Miscodesdetailstab);
	      Miscodesdetailstab.click();

	      WebElement seccode = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sectCode")));
	      ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", seccode, inputData.getByIndex(71));

	      WebElement subseccode = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("subSectCode")));
	      ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", subseccode, inputData.getByIndex(72));
	      WindowHandle.slowDown(1);

	      WebElement purpadv = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("purpAdv")));
	      ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", purpadv, inputData.getByIndex(73));
	      WindowHandle.slowDown(1);

	      WebElement modeadv = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("modeAdv")));
	      ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", modeadv, inputData.getByIndex(74));
	      WindowHandle.slowDown(1);
	      
	      WebElement typeAdv = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("typeAdv")));
	      ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", typeAdv, inputData.getByIndex(75));
	      WindowHandle.slowDown(1);
	      
	      WebElement natAdv = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("natAdv")));
	      ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", natAdv, inputData.getByIndex(76));
	      WindowHandle.slowDown(1);

	   } catch (Exception e) {
		System.out.println("MIS Code Error: " + e.getMessage());
		}
	   try {
		    WebElement submit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Submit")));
		    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submit);
		    
		    if (WindowHandle.HandlePopupIfExists(driver)){
	   		WebElement accept = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accept")));
	   		accept.click();
	   		driver.switchTo().window(mainWindowHandle);
		    }
		} catch (Exception e) {
		    System.out.println("❌ Error: " + e.getMessage());
		}
		    try{
		    	  WindowHandle.ValidationFrame(driver);
		    	      WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='AcctNum']")));
		    	      String labelText = label.getText(); // Get the text of the label

		    	       Assert.assertFalse(labelText == null || labelText.isEmpty(), "Test Failed: Account Number label text is empty!");
		    	       System.out.println("SB Account Created Successfully..! AcctNo: " + labelText);
//		    	   
//		    	       String excelfilePath = excelpath; // Path to your Excel file
//		    	       String sheetName = sheetname; // Update with your sheet name
//		    	       int rowNum = row;  // Row where you want to store
//		    	       //int colNum = 0;  // Column number where Account Number should be stored
//		    	       String columnName = "Created_AccountID";
		    	       ExcelUtils2.updateExcel(excelpath, sheetname,row, "Created_AccountID", labelText);
		    	       
		    	       return labelText;
		    	       
		    	}catch (Exception e) {
		    	System.out.println("Account id creartin Error " + e.getMessage());
		    	Assert.fail("Test Failed: Exception occurred -> " + e.getMessage());
	   
		    	}
			return mainWindowHandle;
			
	    }
}
