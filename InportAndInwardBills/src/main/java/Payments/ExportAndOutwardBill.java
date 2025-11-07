package Payments;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Utilities.ErrorCapture;
import Utilities.ExcelUtils;
import Utilities.RowData;
import Utilities.Validation;
import Utilities.WindowHandle;

public class ExportAndOutwardBill{
	
	private WebDriver driver;
	private WebDriverWait wait;

	    public ExportAndOutwardBill(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
	    }
	    public Map<String, String> execute(RowData inputData, RowData no, String sheetName, int i, String excelPath) throws Exception {
	        WindowHandle.slowDown(2);

		        Map<String, String> result = new HashMap<>();
		        String errorMsg  = "";
		        String labelText = "";
		        JavascriptExecutor js = (JavascriptExecutor) driver;

	            // -------------------- Navigate Menu --------------------
	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))), inputData.getByIndex(1));
	            WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
	            js.executeScript("arguments[0].click();", searchButton);

	            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
	            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));

	            String funCode = inputData.getByIndex(2);
	            WindowHandle.selectDropdownIfValuePresent(driver, wait, By.id("funcCode"), funCode);

	            // -------------------- Bill ID Handling --------------------
	            String billIdField = "billId";
	            if (funCode.equalsIgnoreCase("G - Lodge")) {
	                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billType"))), inputData.getByIndex(4));
	                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billCcy"))), inputData.getByIndex(5));
	                WebElement yesRadio = driver.findElement(By.id("underDc"));
	                js.executeScript("arguments[0].click();", yesRadio);
	                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("dcNo"))), inputData.getByIndex(6));
	                String initialBillId = inputData.getByIndex(7);
	                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id(billIdField))), initialBillId);
	                System.out.println("BillID initially set as " + initialBillId);
	               
	            } else {
	                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id(billIdField))), no.getByHeader("LABELTEXT_BILLID"));
	            }
	            
	            String nextBillId = no.getByHeader("LABELTEXT_BILLID");
                if (nextBillId != null && !nextBillId.trim().isEmpty()) {
                    WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id(billIdField))), nextBillId);
                    System.out.println("BillID overwritten with LABELTEXT_BILLID " + nextBillId);
                }

	            // -------------------- Click Submit & Check UI Errors --------------------
	            WebElement Go = wait.until(ExpectedConditions.elementToBeClickable(By.id("Submit")));
	            js.executeScript("arguments[0].click();", Go);

	            errorMsg = ErrorCapture.checkApplicationErrors(driver);
                if (errorMsg != null && !errorMsg.trim().isEmpty()) {
                	result.put("errorMsg", errorMsg);
                    return result;
                }

	            // -------------------- FunCode Handling -----------------------//
	            if ("R - Realize".equalsIgnoreCase(funCode)) {
	                    WindowHandle.slowDown(1);
	                    WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("brAcct"))), inputData.getByIndex(3));
	                    fillBillTabs(driver, wait, inputData);
	                    Map<String, String> submitResult = clickSubmitAndHandlePopup(excelPath, sheetName, i);
                        if (submitResult != null) {
                            result.putAll(submitResult);
                        }
                    }
	            

	                        if (funCode.equalsIgnoreCase("A - Accept")) 
	                        {
							    proceedToTenderSection(driver, wait, inputData);
							    Map<String, String> submitResult = clickSubmitAndHandlePopup(excelPath, sheetName, i);
						        if (submitResult != null) {
						            result.putAll(submitResult);
						        }
						    }
							                      
	                        if (funCode.equalsIgnoreCase("E - Amend Bill")) 
	                        	{
	                        	proceedToEventSection(driver, wait, inputData);
	                        	 Map<String, String> submitResult = clickSubmitAndHandlePopup(excelPath, sheetName, i);
	                             if (submitResult != null) {
	                                 result.putAll(submitResult);
	                             }
	                         }
	                        if(funCode.equalsIgnoreCase("V - Verify")) {
	          	        	  
	      		        	  Map<String, String> appData = getApplicationData(no,driver, wait);
	      	                  Validation.validateData(inputData.getHeaderMap(), appData);
	                        
	      		              	Map<String, String> submitResult = clickSubmitAndHandlePopup(excelPath, sheetName, i);
	      		              	 if (submitResult != null) {
	                                   result.putAll(submitResult);             
	                               }
	                           }
	      	          

	                
	            // -------------------- General Details Flow --------------------------------------------//
	                        if (!funCode.equalsIgnoreCase("V - Verify")) {            
	           
	                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sAccName"))), inputData.getByIndex(8));
	                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sAddr1"))), inputData.getByIndex(9));
	                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("boeAmt"))), inputData.getByIndex(10));
	                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billCountry"))), inputData.getByIndex(11));
	                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("otherBankRefNo"))), inputData.getByIndex(21));
	                driver.findElement(By.id("Validate")).click();

	                // Party Details
	                WindowHandle.slowDown(2);
	                WebElement PartyTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("fbmparty")));
	                js.executeScript("arguments[0].scrollIntoView(true); arguments[0].click();", PartyTab);

	                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Dcity"))), inputData.getByIndex(12));
	                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Dstate"))), inputData.getByIndex(13));
	                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Dcntry"))), inputData.getByIndex(14));
	                driver.findElement(By.id("Validate")).click();

	                // Tender Details
	                WindowHandle.slowDown(2);
	                WebElement tenderTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("fbmtenor")));
	                js.executeScript("arguments[0].scrollIntoView(true); arguments[0].click();", tenderTab);
	                WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dueDateInd"))), inputData.getByIndex(15));
	                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("billDate_ui"))), inputData.getByIndex(16));
	                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("shpmntDate_ui"))), inputData.getByIndex(16));
	                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("acceptDate_ui"))), inputData.getByIndex(16));

	                // Bill Details
	                WindowHandle.slowDown(2);
	                js.executeScript("document.getElementById('fbmbill').click();");
	                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("carrierCode"))), inputData.getByIndex(18));
	                WebElement NextPage = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@id='NextPage']")));
	                NextPage.click();
	                WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("docStatus"))), inputData.getByIndex(19));
	                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("invoiceAmt"))), inputData.getByIndex(20));

	                fillBillTabs(driver, wait, inputData);

			              	Map<String, String> submitResult = clickSubmitAndHandlePopup(excelPath, sheetName, i);
			              	 if (submitResult != null) {
	                             result.putAll(submitResult);             
	                         }
			result.put("labelText", labelText);               }
		        return result;
	    }


   public static void fillBillTabs(WebDriver driver, WebDriverWait wait, RowData inputData) {
                  
                    try {
          
   //---------------------------------------------Event Details------------------------------------------------------------------------------------------//
     		    	 WindowHandle.slowDown(2);
     	              ((JavascriptExecutor) driver).executeScript("document.getElementById('fbmevent').click();");

  //-------------------------------------------------Charge Details-----------------------------------------------------------------------------------//
      		    	  WindowHandle.slowDown(2);
      	              WebElement ChargeTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tfccharge")));
      	   		    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true); arguments[0].click();", ChargeTab);
  
 //--------------------------------------------------Transaction Details----------------------------------------------------------------------------------------//
     		    	     WindowHandle.slowDown(2);
     		    	    WebElement TransactionTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("fbmtran")));
     		   		    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true); arguments[0].click();", TransactionTab);
     		   		    
  //--------------------------------------------------OutwardMessage Details----------------------------------------------------------------------------------------//
    		    	     WindowHandle.slowDown(2);
    		    	    WebElement OutwardTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tfcmsg")));
    		   		    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true); arguments[0].click();", OutwardTab);
    		   		
    		    }catch(Exception e) {
    		    	System.out.println("");
    		    }
					
        
	    }
	  
	    private static Map<String, String> getApplicationData(RowData inputData, WebDriver driver, WebDriverWait wait) throws Exception {
	        Map<String, String> appData = new HashMap<>();
	        String funCode = inputData.getByIndex(2);
	        
	        System.out.println("Verify functioncode :" + funCode );
        
	        JavascriptExecutor js = (JavascriptExecutor) driver;

	        	
	        WebElement billTypeElement = wait.until(ExpectedConditions.presenceOfElementLocated(
	            By.xpath("//td[contains(normalize-space(text()),'Bill Type')]/following-sibling::td[@class='textfielddisplaylabel']")));
	        String rawBillType = (String) js.executeScript("return arguments[0].textContent;", billTypeElement);
	        Pattern billTypePattern = Pattern.compile("\\b[A-Z]{5,7}\\b");
	        Matcher billTypeMatcher = billTypePattern.matcher(rawBillType);
	        String billType = "";
	        if (billTypeMatcher.find()) {
	            billType = billTypeMatcher.group();
	        }

	        appData.put("billType", billType);
	        System.out.println("✅ Captured Bill Type: " + billType);

	        WebElement billIdElement = wait.until(ExpectedConditions.presenceOfElementLocated(
	        	    By.xpath("//td[contains(normalize-space(text()),'Bill ID')]/following-sibling::td[@class='textfielddisplaylabel']")));
	        	String rawBillId = (String) js.executeScript("return arguments[0].textContent;", billIdElement);
	        	Pattern billIdPattern = Pattern.compile("\\d+");
	        	Matcher billIdMatcher = billIdPattern.matcher(rawBillId);
	        	String billID = "";
	        	if (billIdMatcher.find()) {
	        	    billID = billIdMatcher.group();
	        	}
	        	appData.put("billId", billID);
	        	System.out.println("✅ Captured Bill ID: " + billID);



	        	if ("G - Lodge".equalsIgnoreCase(funCode)) {    
	        appData.put("sAccName", ExcelUtils.getTextOrValue(driver, wait, "sAccName","id"));
	        appData.put("sAddr1", ExcelUtils.getTextOrValue(driver, wait, "sAddr1","id"));
	        appData.put("boeAmt", ExcelUtils.getTextOrValue(driver, wait, "boeAmt","id"));
	        appData.put("billCountry", ExcelUtils.getTextOrValue(driver, wait, "billCountry","id"));


	        WebElement fbmpartyTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("fbmparty")));
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", fbmpartyTab);
	        fbmpartyTab.click();
	        
	        appData = navigateThroughTabsAndCaptureData(driver, wait, appData);
	        	}
	        	
	        	if ("A - Accept".equalsIgnoreCase(funCode) || "E - Amend Bill".equalsIgnoreCase(funCode)) {
	        	    captureBillTabsData(driver, wait, appData);
	        	}

	        	return appData;

	    }
  
	        	
	       public static Map<String, String> navigateThroughTabsAndCaptureData(WebDriver driver, WebDriverWait wait, Map<String, String> appData) {
	        	WebElement fbmtenorTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("fbmtenor")));
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", fbmtenorTab);
	        fbmtenorTab.click();
	        
	        captureBillTabsData(driver, wait, appData);
			return appData;

	       }       
      private static void captureBillTabsData(WebDriver driver, WebDriverWait wait, Map<String, String> appData) {
	        

	        appData.put("billDate_ui", ExcelUtils.getTextOrValue(driver, wait, "billDate_ui","id"));
	        appData.put("shpmntDate_ui", ExcelUtils.getTextOrValue(driver, wait, "shpmntDate_ui","id"));
	        

	        WebElement fbmbillTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("fbmbill")));
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", fbmbillTab);
	        fbmbillTab.click();

	        appData.put("carrierCode", ExcelUtils.getTextOrValue(driver, wait, "carrierCode","id"));
	        WebElement NextPage = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@id='NextPage']")));
	        NextPage.click();
	        appData.put("docStatus", ExcelUtils.getTextOrValue(driver, wait, "docStatus","id"));
	        appData.put("invoiceAmt", ExcelUtils.getTextOrValue(driver, wait, "invoiceAmt","id"));
       	
	        WebElement fbmeventTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("fbmevent")));
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", fbmeventTab);
	        fbmeventTab.click();
	        
	        WebElement tfcchargeTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("tfccharge")));
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", tfcchargeTab);
	        tfcchargeTab.click();
	        
	        WebElement fbmtranTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("fbmtran")));
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", fbmtranTab);
	        fbmtranTab.click();
	        
	        WindowHandle.slowDown(2);
    	    WebElement OutwardTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tfcmsg")));
   		    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true); arguments[0].click();", OutwardTab);

        	}

	       

 private Map<String, String> clickSubmitAndHandlePopup(String excelPath, String sheetName, int row) {
     Map<String, String> resultMap = new HashMap<>();
    String mainWindowHandle = driver.getWindowHandle();
     String labelText = "";

     try {
         WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(By.id("Submit")));
         ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submit);
         ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submit);

         if (WindowHandle.HandlePopupIfExists(driver, Duration.ofSeconds(5))) {
             WebDriverWait waitPopup = new WebDriverWait(driver, Duration.ofSeconds(10));
             try {
                 WebElement acceptBtn = waitPopup.until(ExpectedConditions.elementToBeClickable(
                         By.xpath("//input[@value='Accept']")));
                 ((JavascriptExecutor) driver).executeScript("arguments[0].click();", acceptBtn);
             } catch (Exception ignored) {
              
             }
             driver.switchTo().window(mainWindowHandle);
         }


   
         WindowHandle.ValidationFrame(driver);

         WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='compField']")));
         labelText = label.getText().trim();

         if (labelText == null || labelText.isEmpty()) {
             System.out.println("[WARN] Documentary Credit No label is empty!");
         } else {
         
         	   String DCNo = labelText.replaceAll("[^0-9]", "");
                 ExcelUtils.updateExcel(excelPath, sheetName, row, "LABELTEXT_BILLID", DCNo);
                 resultMap.put("labelText", labelText);
         }
         
     } catch (Exception e) {
         System.out.println("[ERROR] clickSubmitAndHandlePopup failed: " + e.getMessage());
         resultMap.put("errorMsg", e.getMessage());
     }

     resultMap.put("labelText", labelText);
     return resultMap;

 }

	    private void proceedToTenderSection(WebDriver driver, WebDriverWait wait, RowData inputData) {
	        try {
	            WindowHandle.slowDown(2);
 	            ((JavascriptExecutor) driver).executeScript("document.getElementById('fbmbill').click();");
 	            fillBillTabs(driver, wait, inputData);
	        } catch (Exception e) {
	            System.out.println("⚠️ Could not proceed to Tender section: " + e.getMessage());
	        }
	    }
	    
		private void proceedToEventSection(WebDriver driver, WebDriverWait wait, RowData inputData) {
	        try {
	            WindowHandle.slowDown(2);
	   		    
	   		 fillBillTabs(driver, wait, inputData);
	        } catch (Exception e) {
	            System.out.println("⚠️ Could not proceed to Tender section: " + e.getMessage());
	        }
	    }
	    
	  
	    }
