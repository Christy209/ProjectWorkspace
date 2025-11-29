package Payments;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Utilities.ErrorCapture;
import Utilities.ExcelUtils;
import Utilities.RowData;
import Utilities.Validation;
import Utilities.WindowHandle;

public class ImportAndInwardBills {
	private WebDriver driver;
	private WebDriverWait wait;

	    public ImportAndInwardBills(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
	    }
	    	    
	    public Map<String, String> execute(RowData inputData, RowData no, String sheetName, int i, String excelPath) throws Exception {
	        WindowHandle.slowDown(2);

	        Map<String, String> result = new HashMap<>();
	        String errorMsg  = "";

	    try {    
	            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))), inputData.getByIndex(1));
	            WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
	            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);
	    }catch(Exception e) {
	    	System.out.println("");
	    }
	            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
	            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));

	            String funCode = inputData.getByIndex(2);
	            WindowHandle.selectDropdownIfValuePresent(driver, wait, By.id("funcCode"), funCode);
	            JavascriptExecutor js = (JavascriptExecutor) driver;
	            
	            if(funCode.equalsIgnoreCase("G - Lodge")) {
	            
	 	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billType"))),inputData.getByIndex(3));
	 	            WebElement yesRadio = driver.findElement(By.id("underDc"));
	 	            js.executeScript("arguments[0].click();", yesRadio);
	 	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("dcNo"))),inputData.getByIndex(4));		 	          
		           	          
		            
	            }else {
	            	 WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billId"))),no.getByHeader("LABELTEXT_BILLID"));   	           
	            }	
	            
	            String nextBillId = no.getByHeader("LABELTEXT_BILLID");
	            if (nextBillId != null && !nextBillId.trim().isEmpty()
	                    && (funCode.equalsIgnoreCase("K - Delink/Crystallization")
	                        || funCode.equalsIgnoreCase("O - Recovery") 
	                        || funCode.equalsIgnoreCase("N - Dishonor")
	                        || funCode.equalsIgnoreCase("E - Amend Bill"))) {

               WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billId"))),nextBillId);
	                System.out.println("BillID overwritten with LABELTEXT_BILLID " + nextBillId);	               	                
	            }
	            
	            WebElement Go = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Accept")));
    		    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", Go);

    		    
    		    errorMsg = ErrorCapture.checkApplicationErrors(driver);
                if (errorMsg != null && !errorMsg.trim().isEmpty()) {
                	result.put("errorMsg", errorMsg);
                    return result;
                }
	            
	    //-----------------------------------Amend Bill---------------------------------------//
	        	
		    	if (funCode.equalsIgnoreCase("E - Amend Bill")) {
                 
                    	 WindowHandle.slowDown(1);
                    	 proceedToEventSection(driver, wait, inputData);
                         Map<String, String> submitResult = clickSubmitAndHandlePopup(excelPath, sheetName, i);
                         if (submitResult != null) {
                             result.putAll(submitResult);
                         }
                     }
	  
		    	//-----------------------------------Protest------------------------------------------------------------------------------//
		    	if (funCode.equalsIgnoreCase("Q - Protest")) {
            
                    	 WindowHandle.slowDown(1);                    	
                         Map<String, String> submitResult = clickSubmitAndHandlePopup(excelPath, sheetName, i);
                         if (submitResult != null) {
                             result.putAll(submitResult);             
                         }

                     }
		          
			
	            //--------------------------------Delink Details----------------------------------------------------------------------------//
		    	if (funCode.equalsIgnoreCase("K - Delink/Crystallization")) {

                    	 WindowHandle.slowDown(1);
                           WebElement DelinkAccId = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("DelinkAccId")));
                           WindowHandle.setValueWithJS(driver, DelinkAccId, inputData.getByIndex(3));

                           WebElement RealiseAccId = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("RealiseAccId")));
                           WindowHandle.setValueWithJS(driver, RealiseAccId, inputData.getByIndex(4));
                          
                           proceedToEventSection(driver, wait, inputData);
                           Map<String, String> submitResult= clickSubmitAndHandlePopup(excelPath, sheetName, i);
                           if (submitResult != null) {
	                             result.putAll(submitResult);             
	                         }
	                     }
		//--------------------------------------Recovery--------------------------------------------------------//
		    	
		    	if (funCode.equalsIgnoreCase("N - Dishonor")) {
                   
                    	 WindowHandle.slowDown(1);
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
	          
    		  
    		    	
    		    	

	    //-------------------------------General details---------------------------------------------//
    		    	if (!funCode.equalsIgnoreCase("N - Dishonor") 
    		    	        && !funCode.equalsIgnoreCase("V - Verify") 
    		    	        && !funCode.equalsIgnoreCase("Q - Protest") 
    		    	        && !funCode.equalsIgnoreCase("K - Delink/Crystallization")) {
	    	   WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("boeAmt"))),inputData.getByIndex(5));
	           WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("billCountry"))),inputData.getByIndex(6));
	           WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("otherBankRefNo"))),inputData.getByIndex(7));
	           driver.findElement(By.id("Validate")).click();
    		    			
	  //-----------------------------------Party Details------------------------------------------------//
	           WindowHandle.slowDown(2);
 		        WebElement PartyTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("fbmparty")));
 	   		    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true); arguments[0].click();", PartyTab);

 	  //-----------------------------------Tenor Details-------------------------------------------------//
 	   		    
	    		        WindowHandle.slowDown(2);
	    	   		    WebElement tenderTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("fbmtenor")));
	    	   		    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true); arguments[0].click();", tenderTab);
    	   	            
    	   	          	WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("billDate_ui"))),inputData.getByIndex(8));        
    	   	            WindowHandle.slowDown(1);
    	                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("shpmntDate_ui"))),inputData.getByIndex(9)); 
    	   	            WindowHandle.slowDown(1);
    	                WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("acceptDate_ui"))),inputData.getByIndex(10));
	          
    	 //-------------------------------------Bill Details---------------------------------------------------//

    	              WindowHandle.slowDown(2);
    	             WebElement BillTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("miibbill")));
          	   		 ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true); arguments[0].click();", BillTab);
         	          WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("carrierCode"))),inputData.getByIndex(11));
         	             
         	          WebElement NextPage = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@id='NextPage']")));
                      NextPage.click();
                         
         	          WindowHandle.selectByVisibleText(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("docStatus"))),inputData.getByIndex(12));
         	          WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("invoiceAmt"))),inputData.getByIndex(13));
         	          
         	          
         	         fillFromEventOnwards(driver, wait, inputData, excelPath, sheetName, i);
         	          
         	      	Map<String, String> submitResult= clickSubmitAndHandlePopup(excelPath, sheetName, i);
         	       if (submitResult != null) {
                       result.putAll(submitResult);             
                   }
    		    	//	    }catch (Exception e) {
//            //System.out.println("");
//           // e.printStackTrace();
//            result.put("errorMsg", e.getMessage() != null ? e.getMessage() : "Unexpected error");
//        }
    		    //	}
    		    	}
					return result;
	    }
	    
       private void fillFromEventOnwards(WebDriver driver, WebDriverWait wait, RowData inputData, String excelPath, String sheetName, int row) {
         	              
    	   JavascriptExecutor js = (JavascriptExecutor) driver;    
         	                
	      //------------------------------------Event Details-----------------------------------------------------------//
         	            WindowHandle.slowDown(2);
       	              ((JavascriptExecutor) driver).executeScript("document.getElementById('fbmevent').click();");

       	  //-----------------------------------Charge Details-----------------------------------------------------------//
       	              
       	      WindowHandle.slowDown(2);
  	          WebElement ChargeTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tfccharge")));
  	   		  ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true); arguments[0].click();", ChargeTab);  	   		    
  	   	      WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("chargeId"))),inputData.getByIndex(14));  	   	      
  	   	      WebElement goButton = driver.findElement(By.id("Recalculate"));
              ((JavascriptExecutor) driver).executeScript("arguments[0].click();", goButton);

            
         //--------------------------------Transaction Details--------------------------------------------------------//
                WindowHandle.slowDown(2);
	    	    WebElement TransactionTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("fbmtran")));
	   		    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true); arguments[0].click();", TransactionTab);
	   		    
	   		    
	   //-----------------------------------Document Tab-----------------------------------------------------------------//
	   		    
	   		   WindowHandle.slowDown(2);
	    	    WebElement DocumentTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("miibdoc")));
	   		    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true); arguments[0].click();", DocumentTab);
	   		    
	   		WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("docNo"))),inputData.getByIndex(15));
	   		WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("original"))),inputData.getByIndex(16));
	   		
	   	   
	   		WebElement nextPage = driver.findElement(By.xpath("//img[@id='docDet_NextPage']"));
	   		nextPage.click();

            WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("docNo"))),inputData.getByIndex(15));
	   		WindowHandle.setValueWithJS(driver, wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("original"))),inputData.getByIndex(16));
	   		
	   		WebElement lowLimitField = driver.findElement(By.id("docDet_LowLimit")); 
	   		js.executeScript("arguments[0].value='';", lowLimitField);
	   		
	   		lowLimitField.sendKeys("2");
	   		lowLimitField.sendKeys(Keys.ENTER);
	   		wait.until(ExpectedConditions.stalenessOf(lowLimitField));
	   		
	   		
	   	    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("docNo"))); 
	    	WebElement docNo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("docNo")));
	    	WebElement original = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("original")));
	    	
	    	WindowHandle.setValueWithJS(driver, docNo, inputData.getByIndex(15));
	    	WindowHandle.setValueWithJS(driver, original, inputData.getByIndex(16));
	    	
	   	   driver.findElement(By.id("Validate")).click();


	    }
	    
	    private static Map<String, String> getApplicationData(RowData inputData, WebDriver driver, WebDriverWait wait) throws Exception {
	        Map<String, String> appData = new HashMap<>();
	        String funCode = inputData.getByIndex(2);
	        
	        System.out.println("Verify functioncode :" + funCode );
	        
	        JavascriptExecutor js = (JavascriptExecutor) driver;

	        // ========================== BILL TYPE ==========================
	        
	        
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

	        // ========================== BILL ID ==========================
	        
	        
	        WebElement billIdElement = wait.until(ExpectedConditions.presenceOfElementLocated(
	            By.xpath("//td[contains(normalize-space(text()),'Bill ID')]/following-sibling::td[@class='textfielddisplaylabel']")));
	        String rawBillId = (String) js.executeScript("return arguments[0].textContent;", billIdElement);
	        Pattern billIdPattern = Pattern.compile("\\b\\w{2,}\\d{3,}_\\d{4}\\b");
	        Matcher billIdMatcher = billIdPattern.matcher(rawBillId);
	        String billID = "";
	        if (billIdMatcher.find()) {
	            billID = billIdMatcher.group();
	       }
	        appData.put("billId", billID);
	        System.out.println("✅ Captured Bill ID: " + billID);
	        
	        if (!funCode.equalsIgnoreCase("N - Dishonor")){
	            appData.put("boeAmt", ExcelUtils.getTextOrValue(driver, wait, "boeAmt","id"));
	            appData.put("billCountry", ExcelUtils.getTextOrValue(driver, wait, "billCountry","id"));
	            appData.put("otherBankRefNo", ExcelUtils.getTextOrValue(driver, wait, "otherBankRefNo","id"));
	            
	           

		        WebElement fbmpartyTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("fbmparty")));
		        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", fbmpartyTab);
		        fbmpartyTab.click();
	        }
	
		        WebElement fbmtenorTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("fbmtenor")));
		        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", fbmtenorTab);
		        fbmtenorTab.click();
		        
		        appData.put("billDate_ui", ExcelUtils.getTextOrValue(driver, wait, "billDate_ui","id"));
		        appData.put("shpmntDate_ui", ExcelUtils.getTextOrValue(driver, wait, "shpmntDate_ui","id"));
		        appData.put("acceptDate_ui", ExcelUtils.getTextOrValue(driver, wait, "acceptDate_ui","id"));
		        
	
		        WebElement miibbillTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("miibbill")));
		        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", miibbillTab);
		        miibbillTab.click();
		        
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
		        appData.put("chargeId", ExcelUtils.getTextOrValue(driver, wait, "chargeId","id"));
		        
		        WebElement fbmtranTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("fbmtran")));
		        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", fbmtranTab);
		        fbmtranTab.click();
		        
		        WebElement miibdocTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("miibdoc")));
		        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", miibdocTab);
		        miibdocTab.click();
		        appData.put("docNo", ExcelUtils.getTextOrValue(driver, wait, "docNo","id"));
		        appData.put("original", ExcelUtils.getTextOrValue(driver, wait, "original","id"));
		       
		        if (!funCode.equalsIgnoreCase("K - Delink/Crystallization")) {
		            appData.put("DelinkAccId", ExcelUtils.getTextOrValue(driver, wait, "DelinkAccId","id"));
		            appData.put("RealiseAccId", ExcelUtils.getTextOrValue(driver, wait, "RealiseAccId","id"));
		        }
		        
	        return appData;
	    }
	   
	    private Map<String, String> clickSubmitAndHandlePopup(String excelPath, String sheetName, int row) throws IOException {
	        Map<String, String> resultMap = new HashMap<>();
	     //  String mainWindowHandle = driver.getWindowHandle();
	        String labelText = "";

	     //   try {
	            WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(By.id("Submit")));
	            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submit);
	            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submit);

	            if (WindowHandle.HandlePopupAndClickAccept(driver, Duration.ofSeconds(20))) {
	                System.out.println("Popup detected and Accept clicked.");
	            } else {
	                System.out.println("No popup appeared.");
	            }
                 WindowHandle.ValidationFrame(driver);
	            
	            WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='compField']")));
	            labelText = label.getText().trim();

	            if (labelText == null || labelText.isEmpty()) {
	                System.out.println("[WARN] Documentary Credit No label is empty!");
	            } else {
	                Pattern pattern = Pattern.compile("\\b[A-Z0-9]+_\\d+\\b", Pattern.CASE_INSENSITIVE);
	                Matcher matcher = pattern.matcher(labelText);
	                if (matcher.find()) {
	                    String DCNo = matcher.group();
	                    ExcelUtils.updateExcel(excelPath, sheetName, row, "LABELTEXT_BILLID", DCNo);
	                    System.out.println("[INFO] ✅ Bill ID updated to Excel: " + DCNo);
	               }
	            }
	            
//	        } catch (Exception e) {
//	            System.out.println("[ERROR] clickSubmitAndHandlePopup failed: " + e.getMessage());
//	            resultMap.put("errorMsg", e.getMessage());
//	        }

	        resultMap.put("labelText", labelText);
	        return resultMap;
	    }

	            
	           
	    private void proceedToEventSection(WebDriver driver, WebDriverWait wait, RowData inputData) {
	        try {
	            WindowHandle.slowDown(2);
 	              ((JavascriptExecutor) driver).executeScript("document.getElementById('fbmevent').click();");
 	              
 	             WindowHandle.slowDown(2);
 	    	    WebElement TransactionTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("fbmtran")));
 	   		    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true); arguments[0].click();", TransactionTab);
 	   		    

	        } catch (Exception e) {
	            System.out.println("⚠️ Could not proceed to Tender section: " + e.getMessage());
	        }
	    }
	    
}