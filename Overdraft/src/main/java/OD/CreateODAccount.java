package OD;

import java.io.IOException;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
import Utilities.ExcelUtils2;
import Utilities.LogOut;
import Utilities.RowData;
import Utilities.WindowHandle;
import Utilities.WindowHandleJS;

public class CreateODAccount extends BaseTest {
	
	String excelpath = System.getProperty("user.dir") + "/resources/LoanLoan.xlsx";
    int row = 1;
    String sheetname = "Sheet3"; 


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
	@ExcelData(sheetName = "Sheet3", rowIndex = 1)

	public void createTDAccount(RowData inputData) throws Exception {
		
		
		
			 WindowHandle.slowDown(4);
         WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),inputData.getByIndex(1));
         WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
         ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);
try {
         wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
         wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));
   
			 	WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cifId"))),inputData.getByIndex(2));
	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("schmCode"))),inputData.getByIndex(3));
	            WebElement body = driver.findElement(By.tagName("body"));
	            Actions actions = new Actions(driver);
	            actions.moveToElement(body, 0, 1).click().perform();
	            
	            WindowHandle.safeClick(driver, wait, By.id("Accept"));
}catch(Exception e) {
	 WindowHandle.checkForApplicationErrors(driver);
}
	            
	 			////////////////// General Details ///////////////////
	            
	            WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pbPsFlg")));
	            WindowHandle.selectDropdownWithJS(driver, dropdown, inputData.getByIndex(5));
	            WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pbPsFreqType"))),inputData.getByIndex(6));
	            WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pbPsFreqStartDD"))),inputData.getByIndex(7));
	            WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pbPsFreqHldyStat"))),inputData.getByIndex(8));
	            WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pbPsFreqCalBase"))),inputData.getByIndex(9));
	            WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("despatchMode"))),inputData.getByIndex(10));
	            
	            
	            //////////////////////MIS Codes///////////////////////////////////////////////////
	            
	            WindowHandle.slowDown(2);
	            ((JavascriptExecutor) driver).executeScript("document.getElementById('miscodes').click();");
	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sectCode"))),inputData.getByIndex(21));
	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("subSectCode"))),inputData.getByIndex(22));
	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("purpAdv"))),inputData.getByIndex(23));
	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modeAdv"))),inputData.getByIndex(24));

	 	    			 
	 	    			 ////////////////////////////////////Document Details/////////////////////////////////////////
	 	    	            
	 	    	            WindowHandle.slowDown(2);
	 	    	            ((JavascriptExecutor) driver).executeScript("document.getElementById('documentdetails').click();");
	 	    	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("docCode"))),inputData.getByIndex(11));
	 	    	            WindowHandle.selectDropdownWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("docScanFlg"))),inputData.getByIndex(12));

	 	    	            /////////////////////////////// Related Party Tab///////////////////////////////////////////////////
	 	    	            try {
	 	    	            	WindowHandle.slowDown(2);
	 	    	            	
	 	    	    			WebElement relatedPartyTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("relatedpartydetails")));
	 	    	    			JavascriptExecutor js = (JavascriptExecutor) driver;
	 	    	    			js.executeScript("arguments[0].click();", relatedPartyTab);
	 	    	    			

	 	    	    			WindowHandle.slowDown(2);
	 	    	    			WebElement relationCode = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='relnCode']")));
	 	    	    			JavascriptExecutor js1 = (JavascriptExecutor) driver;
	 	    	    			js1.executeScript("arguments[0].value='" + inputData.getByIndex(13) + "';", relationCode);

	 	    	    			
	 	    	    			////////////////////////////////// Second Related Party Tab////////////////////////////////////
	 	    	    			try {

	 	    	    				if (inputData.getByIndex(14).equalsIgnoreCase("Y")) {

	 	    	    					WebElement add = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='relParty_AddNew']")));
	 	    	    					((JavascriptExecutor) driver).executeScript("arguments[0].click();", add);
	 	    	    					
	 	    	    					WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
	 	    	    		            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("relnCode"))),inputData.getByIndex(16));
	 	    	    		            
	 	    	    		           WindowHandle.selectDropdownWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("relnType"))),inputData.getByIndex(15));
	 	    	    		           Thread.sleep(500);
	 	    	    					WindowHandleJS wh = new WindowHandleJS(driver);
	 	    	    					 WebElement cifId = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='cifId']")));
	 	    	    					 wh.setValue(cifId, inputData.getByIndex(17));
	 	    	    					((JavascriptExecutor) driver).executeScript("arguments[0].click();", cifId);

	 	    	    					 
//	 	    	    					 act.moveToElement(element).click().perform();

//	 	    	    					WebElement Title = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='custTitle']")));
//	 	    	    					Title.sendKeys(inputData.getByIndex(48));


	 	    	    					 Thread.sleep(100);
	 	    	    					WebElement addressLine = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='custAddrLine1']")));
	 	    	    					addressLine.sendKeys(inputData.getByIndex(19));

	 	    	    				} else {
	 	    	    					System.out.println("⏩ Skipping 'Add' button click as SecondRelatedFlg is not Y.");
	 	    	    				}
	 	    	    			} catch (Exception e) {
	 	    	    				System.out.println("Error in second Related process:: " + e.getMessage());
	 	    	    			}

	 	    	    		} catch (Exception e) {
	 	    	    			System.out.println("Relationship tab Error : " + e.getMessage());
	 	    	    		}

	///////////////////////////Interest Details//////////////////////////////////
	 	    	            
	 	    	            WindowHandle.slowDown(2);
	 	    	            ((JavascriptExecutor) driver).executeScript("document.getElementById('generaldetails2').click();");
	 	    		
	 	    		  	/////////////////////////////// Nomination Tab///////////////////////////////////////////////////
				            
	 
//	 	    					WindowHandle.slowDown(2);
//	 	    				WebElement NomineeTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("nominationdetails")));
//	 	    				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", NomineeTab);
//	 	    				NomineeTab.click();
//	 	    				
//	 	    				WebElement Relnum = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("regValue")));
//	 	    				Relnum.sendKeys(inputData.getByIndex(31));
//	 	    				
//	 	    				
//	 	    				WindowHandleJS wh = new WindowHandleJS(driver);
//	 	    				 WebElement cifId = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("nominationdetails.cifId")));
//	 	    				 wh.setValue(cifId, inputData.getByIndex(32));
//	 	    			
//	 	    				  WebElement body1 = driver.findElement(By.tagName("body"));
//	 	    		            Actions actions1 = new Actions(driver);
//	 	    		           actions1.moveToElement(body1, 0, 0).click().perform();
//	 	    				
////	 	    				WebElement CifId = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("nominationdetails.cifId")));
////	 	    				CifId.sendKeys(inputData.getByIndex(27));
////	 	    				Actions actions = new Actions(driver);
////	 	    				actions.moveToElement(CifId, 0, 0).click().perform();
//	 	    				
//	 	    				//WebElement nomName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='nomName']")));
//	 	    				//String nomNameValue = inputData.getByIndex(28);
//	 	    				//((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", nomName, nomNameValue);
//	 	    				//
//	 	    				WebElement Relationtype = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='relation']")));
//	 	    				String relationValue = inputData.getByIndex(34);
//	 	    				((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", Relationtype, relationValue);
//	 	    				
//	 	    				//WebElement nomAddrLine1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='nomAddrLine1']")));
//	 	    				//String nomAddrLine1Value = inputData.getByIndex(30);
//	 	    				//((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", nomAddrLine1, nomAddrLine1Value);
//	 	    				//
//	 	    				//WebElement nomCityCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='nomCityCode']")));
//	 	    				//String nomCityCodeValue = inputData.getByIndex(31);
//	 	    				//((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", nomCityCode, nomCityCodeValue);
//	 	    				//
//	 	    				//WebElement nomStateCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='nomStateCode']")));
//	 	    				//String nomStateCodeValue = inputData.getByIndex(32);
//	 	    				//((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", nomStateCode, nomStateCodeValue);
//	 	    				//
//	 	    				//WebElement nomCntryCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='nomCntryCode']")));
//	 	    				//String nomCntryCodeValue = inputData.getByIndex(33);
//	 	    				//((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", nomCntryCode, nomCntryCodeValue);
//	 	    				//
//	 	    				//WebElement nomPostalCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='nomPostalCode']")));
//	 	    				//String nomPostalCodeValue = inputData.getByIndex(34);
//	 	    				//((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", nomPostalCode, nomPostalCodeValue);
//	 	    				
//	 	    				WebElement NomPrct = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='nomPcnt']")));
//	 	    				String Nomineeprct = inputData.getByIndex(35);
//	 	    				((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", NomPrct, Nomineeprct);
//	 	    				
//	 	    				//////////////////////////////minor nominee ///////////////////////////////////////////////////////////
//	 	    				
//	 	    				if(inputData.getByIndex(35).equalsIgnoreCase("Y")) {
//	 	    				  
//	 	    				 try {
//	 	    				  WebElement GuardianName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='grdnName']")));
//	 	    				 GuardianName.sendKeys(inputData.getByIndex(37));
//	 	    				  
//	 	    				  WebElement grdnCode = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@id='grdnCode']"))); 
//	 	    				  ((JavascriptExecutor) driver).executeScript("arguments[0].click();", grdnCode);
//	 	    				  WindowHandle.selectByVisibleText(grdnCode, inputData.getByIndex(38));
//	 	    				  
//	 	    				  WebElement grdnAddrLine1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("grdnAddrLine1")));
//	 	    				  String GrdnAddrLine1 = inputData.getByIndex(39);
//	 	    				  ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", grdnAddrLine1, GrdnAddrLine1);
//	 	    				  
//	 	    				  WebElement grdnAddrLine2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='grdnAddrLine2']")));
//	 	    				  grdnAddrLine2.sendKeys(inputData.getByIndex(40));
//	 	    				  
//	 	    				  WebElement grdnCityCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='grdnCityCode']")));
//	 	    				  grdnCityCode.sendKeys(inputData.getByIndex(41));
//	 	    				  
//	 	    				  WebElement grdnStateCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='grdnStateCode']")));
//	 	    				  grdnStateCode.sendKeys(inputData.getByIndex(42));
//	 	    				  
//	 	    				  WebElement grdnCntryCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='grdnCntryCode']")));
//	 	    				  grdnCntryCode.sendKeys(inputData.getByIndex(43));
//	 	    				  
//	 	    				  WebElement grdnPostalCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='grdnPostalCode']")));
//	 	    				  grdnPostalCode.sendKeys(inputData.getByIndex(44));
//	 	    				  
//	 	    				} catch (Exception e) {
//	 	    				  System.out.println("❌ Error in Minor nominee process: " + e.getMessage());
//	 	    				  e.printStackTrace();
//	 	    				}
//	 	    				
//	 	    					} else {
//	 	    				  System.out.println("Skipping!..Nominee is not minor");
//	 	    					}
//	 	    				
//	 	    				
//	 	    				/////////////////////////////////////Second Nomineee///////////////////////////////
//	 	    				try {  
//	 	    				   String SecondNomineeFlag = inputData.getByIndex(45);
//	 	    				   System.out.println("🔍 Checking SecondNomineeflg: '" + SecondNomineeFlag + "'");
//	 	    				
//	 	    				   if (SecondNomineeFlag != null && SecondNomineeFlag.trim().equalsIgnoreCase("Y")) {
//	 	    				       System.out.println("✅ Condition Passed! Adding Second Nominee...");
//	 	    				
//	 	    				       WebElement Nadd = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='nomDetail_AddNew']")));
//	 	    				       ((JavascriptExecutor) driver).executeScript("arguments[0].click();", Nadd);
//	 	    				       System.out.println("✅ 'Add' button clicked as SecondNomineeFlg is Y.");
//	 	    				
//	 	    				       WebElement S_Cifid = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("nominationdetails.cifId")));
//	 	    				       String S_cifID = inputData.getByIndex(46);
//	 	    				       ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", S_Cifid, S_cifID);
//	 	    				       actions1.moveToElement(S_Cifid, 0, 0).click().perform();
//	 	    				     
//	 	    				
//	 	    				       WebElement S_Relationtype = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("nominationdetails.relation")));
//	 	    				       String S_relationValue = inputData.getByIndex(47);
//	 	    				       ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", S_Relationtype, S_relationValue);
//	 	    				       //WindowHandle.slowDown(2);
//	 	    				
//	 	    				       WebElement S_NomPrct = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='nomPcnt']")));
//	 	    				       String S_Nomineeprct = inputData.getByIndex(48);
//	 	    				       ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", S_NomPrct, S_Nomineeprct);
//	 	    				
//	 	    				
//	 	    				   } else {
//	 	    				       System.out.println("⏩ Skipping 'Add' button click as SecondNomineeFlg is not Y.");
//	 	    				   }
//	 	    				} catch (Exception e) {
//	 	    				   System.out.println("❌ Error in second nominee process: " + e.getMessage());
//	 	    				   e.printStackTrace();
//	 	    				}
//	 	    				
//	 	    				
//	 	    				} catch (Exception e) {
//	 	    				 System.out.println("Nominee tab error : " + e.getMessage());
//	 	    				}
/////////////////////////////// Scheme Tab ///////////////////////////////////////////////////
	    		             
	    			 WindowHandle.slowDown(2);
	    			 WebElement tdsch = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sbschemedetails")));
	    			 ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tdsch);

	    			 WindowHandle.safeClick(driver, wait, By.id("Validate"));
	    			 
	    			 WindowHandle.slowDown(2);
	    	            driver.findElement(By.id("linttmacct")).click();
	    	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tenorOfSlabInMnths"))),inputData.getByIndex(48));
	    	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tenorOfSlabInDays"))),inputData.getByIndex(49));
	    			 
	    			    ////////////////////////////////Account Limits//////////////////////////////////
	 	            
	 	            WindowHandle.slowDown(2);
	 	            ((JavascriptExecutor) driver).executeScript("document.getElementById('acctlmt').click();");
	 	            WindowHandle.slowDown(1);
	 	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("documentDate_ui"))),inputData.getByIndex(26));
	 	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("expiryDate_ui"))),inputData.getByIndex(27));
	 	            WindowHandle.selectDropdownWithJS(driver,wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("drawingPowerInd"))),inputData.getByIndex(28));

	 	    		
	 	    	            
	    			 
	 	           try {
	 	                WebElement submit1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Submit")));
	 	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submit1);
	 	                
	 	               try {
	 	                  WindowHandle.ValidationFrame(driver);
	 	                 String successLog = WindowHandle.checkForSuccessElements(driver);
	 	                if(successLog != null){
	 	                    System.out.println("✅ Test Success");
	 	                }

	 	                  WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='AcctNum']")));
	 	  	    	      String labelText = label.getText(); // Get the text of the label

	 	  	    	       Assert.assertFalse(labelText == null || labelText.isEmpty(), "Test Failed: Account Number label text is empty!");
	 	  	    	      
	 	                  String excelfilePath = excelpath;
	 	                  String sheetName = sheetname;
	 	                  int rowNum = row;
	 	                  String columnName = "Created_AccountID";

	 	                  ExcelUtils2.updateExcel(excelfilePath, sheetName, rowNum, columnName, labelText);

	 	              } catch (Exception e) {
	 	                  System.out.println("❌ Account id creation Error: " + e.getMessage());
	 	                  Assert.fail("Test Failed: Exception occurred -> " + e.getMessage());
	 	              }

	 	           //   driver.switchTo().window(mainwindow);
	 				 driver.switchTo().defaultContent();
	 					LogOut.performLogout(driver, wait);
	 				 
	 	  	
	 	  		} catch (Exception e) {
	 	  		    System.out.println("❌ Error: " + e.getMessage());
	 	  		}
	 	      }

}