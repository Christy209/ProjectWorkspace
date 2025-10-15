package PAYMENTS;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import Utilities.DateUtils;
import Utilities.ExcelUtils;
import Utilities.RowData;
import Utilities.WindowHandle;

public class ForwardContractMaintenance_MNTFWC {

	private WebDriver driver;
	private WebDriverWait wait;

	    public ForwardContractMaintenance_MNTFWC(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
	    }
	    	    
	    public Map<String, String>  execute(RowData inputData ,RowData no,String sheetname,int row,String excelpath) throws Exception {
	        Map<String, String> result = new HashMap<>();
	        String labelText = "";
	        String FwdCntrctNo = "";
	        String mainWindowHandle = driver.getWindowHandle();
	        WindowHandle.slowDown(2);
	        try {
	            // ---- navigate and enter values ----
	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),inputData.getByIndex(1));
	            WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
	            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

	            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
	            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));

	            String FunCode = inputData.getByIndex(2);
	            WindowHandle.selectDropdownIfValuePresent(driver,wait,(By.id("funcCode")),FunCode);
	            
	            if(FunCode.equalsIgnoreCase("A - Booking of Contract")) {
	            	
	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("solId"))),inputData.getByIndex(3));
	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FwdCntrctType"))),inputData.getByIndex(5));
	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cifId"))),inputData.getByIndex(6));
	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ccy"))),inputData.getByIndex(7));
	            }else{
	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FwdCntrctNo"))),no.getByHeader("FwdCntrctNo"));
	            }

//	            WebElement Go = driver.findElement(By.id("Accept"));
//	            Go.click();

    		    WebElement Go = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Accept")));
    		    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", Go);
	            ////////////////////////////////////////////////Extension tab///////////////////////////////////////////////  
	            if(FunCode.equalsIgnoreCase("E - Extension of Contract")) {
	            	
	            	String validfrmDate = inputData.getByIndex(16);
		    	    if (validfrmDate != null && !validfrmDate.trim().isEmpty()) {
		    	    String ValidfrmDate = DateUtils.toDDMMYYYY(validfrmDate);
		    	    WindowHandle.setValueWithJS(
		    	        driver,
		    	        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='validityFrmDate_ui']"))),
		    	        ValidfrmDate);
		    	    }  
		    	    String validtoDate = inputData.getByIndex(17);
		    	    if (validfrmDate != null && !validfrmDate.trim().isEmpty()) {
		    	    String ValidtoDate = DateUtils.toDDMMYYYY(validtoDate);
		    	    WindowHandle.setValueWithJS(
		    	        driver,
		    	        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='validityToDate_ui']"))),
		    	        ValidtoDate);
		    	    }
		            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("newRateCode"))),
		    	    inputData.getByIndex(18));
		            
		            WebElement pageBody = driver.findElement(By.tagName("body"));
		            pageBody.click(); 
		            
		    	    WebElement LimitTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("tfclimit")));
		    	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", LimitTab);
		    	    LimitTab.click();
	            }else if(FunCode.equalsIgnoreCase("E - Extension of Contract") &&
	            		(FunCode.equalsIgnoreCase("M - Modify"))){
	            
	            ///////////////////////////////////general tab////////////////////////////////////////////////////////
	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("acctId"))),
	            inputData.getByIndex(8));
	            
	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FwdCntrctAmt"))),
	            inputData.getByIndex(9));
	            
	    	    String validfrmDate = inputData.getByIndex(10);
	    	    if (validfrmDate != null && !validfrmDate.trim().isEmpty()) {
	    	    String ValidfrmDate = DateUtils.toDDMMYYYY(validfrmDate);
	    	    WindowHandle.setValueWithJS(
	    	        driver,
	    	        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("validfrmDate_ui"))),
	    	        ValidfrmDate
	    	    );
	    	    }  
	    	    String validtoDate = inputData.getByIndex(11);
	    	    if (validfrmDate != null && !validfrmDate.trim().isEmpty()) {
	    	    String ValidtoDate = DateUtils.toDDMMYYYY(validtoDate);
	    	    WindowHandle.setValueWithJS(
	    	        driver,
	    	        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("validtoDate_ui"))),
	    	        ValidtoDate
	    	    );
	    	    }
	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Toccy"))),
	            inputData.getByIndex(12));
	            
	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("rateCode"))),
	            inputData.getByIndex(13));
	            ///////////////////////////LimitTab/////////////////////////////////////////////////////
	    	    WebElement LimitTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("tfclimit")));
	    	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", LimitTab);
	    	    LimitTab.click();
	            
	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("limitIdPrefix"))),
	            inputData.getByIndex(14));
	            
	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("limitIdSuffix"))),
	            inputData.getByIndex(15));
	            //////////////////////////////////////////////ChargeTab//////////////////////////////////////////
	    	    WebElement ChargeTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("tfccharge")));
	    	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ChargeTab);
	    	    ChargeTab.click();
	    	    
	            }else {

	    		    WebElement submit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Submit")));
	    		    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submit);
	    		   
	    		    try {
	    		        WebElement errorLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("anc1")));
	    		        Assert.fail("Test failed due to error: " + errorLink.getText());
	    		    } catch (TimeoutException e) {
	    		        System.out.println("No error encountered. Proceeding...");
	    		    }
	    		    if (WindowHandle.HandlePopupIfExists(driver)){
	    		    	WindowHandle.slowDown(5);
		    	   		WebElement accept = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accept")));
		    	   		((JavascriptExecutor) driver).executeScript("arguments[0].click();", accept);
		    	   		driver.switchTo().window(mainWindowHandle);
	    	   		
		    	   		WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
		    	   		WindowHandle.ValidationFrame(driver);
	    		    try {
		    	   		
		    	   		WebElement errorElement = wait2.until(ExpectedConditions.visibilityOfElementLocated(
	    		        By.xpath("//tr[@class='alert']//td[2]")));
		    		    String errorMsg = errorElement.getText().trim();
		    		    System.out.println("Captured Error: " + errorMsg);

	    		    if (!errorMsg.isEmpty()) {
	    		        Assert.fail("❌ UI Error Message Displayed: " + errorMsg);
	    		    }
	    		    } catch (TimeoutException e) {
	    		        // No error appeared, continue test
	    		        System.out.println("✅ No UI Error Message Displayed. Proceeding...");
	    		    }
	    		    }
	    		    
	    		    try{
				   WindowHandle.ValidationFrame(driver);
				   WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='compField']")));
				   labelText = label.getText(); // Get the text of the label
				   FwdCntrctNo = labelText.replaceAll("[^0-9]", "");

				   Assert.assertFalse(labelText == null || labelText.isEmpty(), "Test Failed:Forward Contract No is empty!"); 
				   ExcelUtils.updateExcel(excelpath, sheetname, row, "FwdCntrctNo", FwdCntrctNo); 
				    	       
				   @SuppressWarnings("unused")
					String expected;
	                switch (FunCode) {
	                    case "A - Booking of Contract":
	                    case "M - Modify":
	                    case "V - Verify":
	                    case "X - Cancel":
	                    case "E - Extension of Contract":
	                        expected = labelText;
	                        break;
	                    default:
	                        Assert.fail("Unsupported FunCode: " + FunCode);

	        	            result.put("DCNo", FwdCntrctNo);
	        	            result.put("LabelText", labelText);
	                }	 	               
			 	               
				    	}catch (Exception e) {
				    		
				    		System.out.println(e);
			   
				    	}
	            }
	        }catch(Exception e) {
	        	
	        System.out.println("MNTFWC :" + e);
	        }
	        return result;	
	        }
}
	    

