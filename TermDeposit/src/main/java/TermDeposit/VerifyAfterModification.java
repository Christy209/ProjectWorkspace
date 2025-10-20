package TermDeposit;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

public class VerifyAfterModification extends BaseTest {

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
	     @ExcelData(sheetName = "Nainital", rowIndex = {1, 5})
	     
	     
	     public static void VerifyTDAccount(RowData createData,RowData verifyData) throws Exception {
	    	
		
	        try {
	        	
	           WindowHandle.handleAlertIfPresent( driver);
	           WebElement searchbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuSelect")));
	           searchbox.click();
	           searchbox.sendKeys(verifyData.getByIndex(1));
	           WebElement element4 = driver.findElement(By.id("menuSearcherGo")); 
	           element4.click();
	        } catch (Exception e) {
	        	System.out.println("Error: " + e.getMessage());
	        }
	        
	        
	        try {
	        	
	            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
	            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));
	            WebElement funCode1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("mode")));
	            funCode1.sendKeys(verifyData.getByIndex(2));
	            WindowHandle.slowDown(2);
	       
	              WebElement acctID = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tempForacid")));
	              acctID.sendKeys(verifyData.getByIndex(3));

	             
		    	   WebElement go = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='Accept']")));
		    	   ((JavascriptExecutor) driver).executeScript("arguments[0].click();", go);

	          
	               Map<String, String> appData = getApplicationData(driver); // Fetch application value
		    	   Validation.validateData(createData.getHeaderMap(),appData);
		    	   
		    	   WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='acctNum']")));
			       	String labelText = label.getText(); // Get the text of the label

			        Assert.assertFalse(labelText == null || labelText.isEmpty(), "Test Failed: Account Number label text is empty!");
			        System.out.println("TD Account Verified Successfully..! AcctNo: " + labelText);
			    
			        String excelfilePath = System.getProperty("user.dir") + "/Resource/TD Testcase.xlsx"; // Path to your Excel file
			        String sheetName = "Nainital"; // Update with your sheet name
			        int rowNum = 1;  // Row where you want to store
			        //int colNum = 66;  // Column number where Account Number should be stored
			        String columnName = "Created_AccountID"; 
			        ExcelUtils.updateExcel(excelfilePath, sheetName,rowNum, columnName, labelText);
			
	        
	        } catch (Exception e) {
	        	System.out.println("Error: " + e.getMessage());
	        }

	    
	     }
	                                            //General Details
		            
	           private static Map<String, String> getApplicationData(WebDriver driver) throws Exception {
			    Map<String, String> appData = new HashMap<>();
			    String mainWindowHandle = driver.getWindowHandle();
		    		    
		        //////////////////////////////////////General tab/////////////////////////
		    		    
		        WebElement generalDetailsTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("tdgen")));
			    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", generalDetailsTab);
			    generalDetailsTab.click();
		         
			    appData.put("A/c. Name", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='acctName']","xpath"));
			    appData.put("A/c. Short Name", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='acctShortName']","xpath"));
			    appData.put("A/c. Opening Date", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='acctOpenDate_ui']","xpath"));
			    appData.put("A/c. Manager ID", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='acctMgrAtAcct']","xpath"));
			    appData.put("Acct_Statement", ExcelUtils.getTextOrValue(driver, wait, "pbPsFlg","id"));
//			    appData.put("FreqType", ExcelUtils.getTextOrValue(driver, wait, "pbPsFreqType","id"));
//			    appData.put("FreqWeek", ExcelUtils.getTextOrValue(driver, wait, "pbPsFreqWeek","id"));
//			    appData.put("FreqDay", ExcelUtils.getTextOrValue(driver, wait, "pbPsFreqDay","id"));
//			    appData.put("FreqStartDD", ExcelUtils.getTextOrValue(driver, wait, "pbPsFreqStartDD","id"));
//			    appData.put("FreqHldyStat", ExcelUtils.getTextOrValue(driver, wait, "pbPsFreqHldyStat","id"));
//			    appData.put("DispatchMode", ExcelUtils.getTextOrValue(driver, wait, "despatchMode","id"));
			    appData.put("acctOpenDate_ui", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='acctOpenDate_ui']","xpath"));
			    appData.put("modeOfOperCode", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='modeOfOperCode']","xpath"));
			    appData.put("relativeStaffId", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='relativeStaffId']","xpath"));
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
	           
	            appData.put("valueDate_ui", ExcelUtils.getTextOrValue(driver, wait, "valueDate_ui","id"));
	            appData.put("depAmt", ExcelUtils.getTextOrValue(driver, wait, "depAmt","id"));
	            appData.put("DepositPeriod", ExcelUtils.getTextOrValue(driver, wait, "DepositPeriod","id"));
			    appData.put("maturityDate_ui", ExcelUtils.getTextOrValue(driver, wait, "maturityDate_ui","id"));
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
	    		    appData.put("maxRenew", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='maxRenew']","xpath"));
	    		    appData.put("renewMths", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='renewMths']","xpath")); 
	    		    appData.put("renewSchm", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='renewSchm']","xpath")); 
	    		    appData.put("renewGLSubHead", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='renewGLSubHead']","xpath")); 
	    		    appData.put("renewCrncy", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='renewCrncy']","xpath"));
	    		    
	    			
	    			  ///////////////////////////////////Nominee tab//////////////////////////////////////////
	    		    
	    		    WebElement NomineeTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("nominationdetails")));
	                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", NomineeTab);
	                NomineeTab.click();
	        
	                appData.put("regValue", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='regValue']","xpath"));
	    		    appData.put("nomName", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='nomName']","xpath"));
	    		    appData.put(" relation", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='relation']","xpath"));
	    		    appData.put("nomAddrLine1", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='nomAddrLine1']","xpath"));
	    		    appData.put(" nomCityCode ", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='nomCityCode']","xpath"));
	    		    appData.put("nomStateCode", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='nomStateCode']","xpath"));
	    		    appData.put("nomCntryCode", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='nomCntryCode']","xpath"));
	    		    appData.put("nomPostalCode", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='nomPostalCode']","xpath"));
	    		    appData.put("nomPcnt", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='nomPcnt']","xpath"));
	    		    
//	    		  //  if (verifyData.getByIndex(4).equalsIgnoreCase("Yes")) { 
//	    		    	
//	    		    
//	    		    WebElement Add	= wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nomDetail_AddNew")));
//	                JavascriptExecutor js = (JavascriptExecutor) driver;
//	                js.executeScript("arguments[0].click();", Add); 
//	                WindowHandle.slowDown(2);
//	                
//	    		   
//	    		    appData.put("Nominee Name", ExcelUtils.getTextOrValue(driver, wait, "//input[@name='nominationdetails.nomName']","xpath"));
//	    		    appData.put("Relationship", ExcelUtils.getTextOrValue(driver, wait, "//input[@name='nominationdetails.relation']","xpath"));
//	    		    appData.put("Address", ExcelUtils.getTextOrValue(driver, wait, "//input[@name='nominationdetails.nomAddrLine1']","xpath"));
//	    		    appData.put("City", ExcelUtils.getTextOrValue(driver, wait, "//input[@name='nominationdetails.nomCityCode']","xpath"));
//	    		    appData.put("State", ExcelUtils.getTextOrValue(driver, wait, "//input[@name='nominationdetails.nomStateCode']","xpath"));
//	    		    appData.put("Country", ExcelUtils.getTextOrValue(driver, wait, "//input[@name='nominationdetails.nomCntryCode']","xpath"));
//	    		    appData.put("Postal Code", ExcelUtils.getTextOrValue(driver, wait, "//input[@name='nominationdetails.nomPostalCode']","xpath"));
//	    		    appData.put("Nomination Percent", ExcelUtils.getTextOrValue(driver, wait, "//input[@name='nominationdetails.nomPcnt']","xpath"));
//	    		       		
	    	/*	} else if (NomineeflgNo.isSelected()) {
	    			payInterestValue = "No";
	    		}	        		   
			
	    			appData.put("Nominee Flag", Nomineeflg); */
	    		
			   

	        
	          
			    

	            ///////////////////////////////////////////RelatedParty Details///////////////////////////////////////////////////////
	        
	    		    List<Map<String, String>> allRecords = new ArrayList<>();
	    		       WebElement RelatedPartydetailstab = wait.until(ExpectedConditions.elementToBeClickable(By.id("relatedpartydetails")));
	    		       ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", RelatedPartydetailstab);
	    		       ((JavascriptExecutor) driver).executeScript("arguments[0].click();", RelatedPartydetailstab);
	    		       
	    		       @SuppressWarnings("unused")
	    				boolean hasRecords = false;
	    		   
	    		       do {
	    		           try {

	    		        	   appData.put("relnCode", ExcelUtils.getTextOrValue(driver, wait, "relnCode", "id"));
	    		        	   appData.put("relnType", ExcelUtils.getTextOrValue(driver, wait, "relnType", "id"));
	    		               appData.put("RelatedFreqType", ExcelUtils.getTextOrValue(driver, wait, "relatedpartydetails.pbPsFreqType", "name"));
	    		               appData.put("RelatedFreqWeek", ExcelUtils.getTextOrValue(driver, wait, "relatedpartydetails.pbPsFreqWeek", "name"));
	    		               appData.put("RelatedFreqDay", ExcelUtils.getTextOrValue(driver, wait, "relatedpartydetails.pbPsFreqDay", "name"));
	    		               appData.put("RelatedFreqStartDD", ExcelUtils.getTextOrValue(driver, wait, "relatedpartydetails.pbPsFreqStartDD", "name"));
	    		               appData.put("RelatedFreqHldyStat", ExcelUtils.getTextOrValue(driver, wait, "relatedpartydetails.pbPsFreqHldyStat", "name"));
	    		               appData.put("RelateddispMode", ExcelUtils.getTextOrValue(driver, wait, "relatedpartydetails.despMode", "name"));
	    		               appData.put("Related-CustTitle", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='custTitle']", "xpath"));
	    		               appData.put("Related - CustName", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='custName']", "xpath"));
	    		               appData.put("Related -CustAddrLine1", ExcelUtils.getTextOrValue(driver, wait, "//input[@id='custAddrLine1']", "xpath"));

	    		               if (appData.get("Relation Code") != null && !appData.get("Relation Code").isEmpty()) {
	    		                   allRecords.add(new HashMap<>(appData));  // Store a copy to prevent overwriting
	    		                   hasRecords = true;
	    		                   System.out.println("✅ Record " + allRecords.size() + ": " + appData);
	    		               } else {
	    		                   System.out.println("❌ No valid related party records found. Exiting.");
	    		                   break;
	    		               }
	    		               WindowHandle.handleAlertIfPresent(driver);

	    		               List<WebElement> nextButtonList = driver.findElements(By.id("relParty_NextRec"));
	    		               if (!nextButtonList.isEmpty() && nextButtonList.get(0).isEnabled()) {
	    		                   WebElement nextButton = nextButtonList.get(0);
	    		                   nextButton.click();
	    		                   wait.until(ExpectedConditions.stalenessOf(nextButton)); // Ensure the page refreshes
	    		               } else {
	    		                   System.out.println("🔴 No more records found. Moving to the next tab.");
	    		                   break;
	    		               }
	    		           } catch (Exception e) {
	    		               System.out.println("🚫 Element not found. Exiting.");
	    		               break;
	    		           }
	    		       } while (true);
	    		       
	           
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
			   
			   
			   //////////////////////////////////////////////Additional Info/////////////////////////////////////////////////////
		       try {
			   WebElement AdditionalInfo = driver.findElement(By.xpath("//a[@id='acmai']"));
			     ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", AdditionalInfo);
			     ((JavascriptExecutor) driver).executeScript("arguments[0].click();", AdditionalInfo);
			     WindowHandle.slowDown(2);
	        
			   		WebElement submit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Submit")));
			   		submit.click();
					}catch (Exception e) {
						System.out.println("Error " + e.getMessage());
						
					}
		       WindowHandle.handlePopupIfExists(driver);
		       WindowHandle.handleAlertIfPresent(driver);
		       WebElement acceptButton = driver.findElement(By.id("Accept"));
		       acceptButton.click();
		       driver.switchTo().window(mainWindowHandle);
		    

			 	try{
			   		
				    driver.switchTo().defaultContent(); // or use driver.switchTo().frame(0); if no name
				    driver.switchTo().frame("loginFrame");
				    driver.switchTo().frame("CoreServer");
				    driver.switchTo().frame("FINW");
			       	

				    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
				    WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
				        By.xpath("//td[contains(text(),'verified successfully')]")
				    ));

				    // Extract and print the message
				    String messageText = successMessage.getText();
				    System.out.println("✅ Success message: " + messageText);

				    // Validate the message (you can also use Regex if ID needs to be extracted)
				    if (messageText.contains("verified successfully")) {
				        System.out.println("✅ Verification successful.");
				    } else {
				        System.out.println("❌ Verification message not found.");
				    }

			        LogOut.performLogout(driver, wait);
			        WindowHandle.slowDown(2);		
				}catch (Exception e) {
					System.out.println("Account id creartin Error " + e.getMessage());
					Assert.fail("Test Failed: Exception occurred -> " + e.getMessage());
				}
			 	
			 	
		  		
					return appData;
			   }
	}


