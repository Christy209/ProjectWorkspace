package PAYMENTS;

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
import org.testng.Assert;
import Utilities.ErrorCapture;
import Utilities.ExcelUtils;
import Utilities.RowData;
import Utilities.Validation;
import Utilities.WindowHandle;

public class MNTFWC_Verification {
	private WebDriver driver;
	private WebDriverWait wait;

	    public MNTFWC_Verification(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
	    }
	    	    
	    public Map<String, String> execute(RowData vr,RowData inputData , String sheetname,int row,String excelpath) throws Exception {
	    	
	        Map<String, String> result = new HashMap<>();
	        String labelText = "";
	        WindowHandle.slowDown(4);

	            // ---- navigate and enter values ----
	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),vr.getByIndex(1));
	            WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
	            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

	            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
	            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));

	            String FunCode = vr.getByIndex(2);
	            WindowHandle.selectDropdownIfValuePresent(driver,wait,(By.id("funcCode")),FunCode);

	            WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FwdCntrctNo"))),inputData.getByHeader("FwdCntrctNo"));
	            
	            WebElement Go = driver.findElement(By.id("Accept"));
	            Go.click();
	            
	            if(!FunCode.equalsIgnoreCase("I - Inquire")) {
	            String errorMsg = ErrorCapture.checkApplicationErrors(driver);
	            if (errorMsg != null && !errorMsg.trim().isEmpty()) {
	                result.put("errorMsg", errorMsg);
	                return result;
	            }
	            }
	            
	            if(FunCode.equalsIgnoreCase("V - Verify")) {
	            
				Map<String, String> appData = getApplicationData(inputData, driver,wait); // Fetch application value
				Validation.validateData(inputData.getHeaderMap(),appData);
				
				WindowHandle.slowDown(5);

				  try{
					   WindowHandle.ValidationFrame(driver);
					   WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='compField']")));
					   labelText = label.getText(); // Get the text of the label
					   //String FwdCntrctNo = labelText.replaceAll("[^0-9]", "");
					   // Extract only the Forward Contract No (alphanumeric at start)
		                Pattern pattern = Pattern.compile("^[A-Z0-9]+"); // match letters+digits at the start
		                Matcher matcher = pattern.matcher(labelText);

		                 String FwdCntrctNo = "";
		                if (matcher.find()) {
		                    FwdCntrctNo = matcher.group(); // ✅ This will be "003FXTODP2500010"
		                } else {
		                    System.out.println("⚠️ Could not extract Forward Contract No from label: " + labelText);
		                }

					   Assert.assertFalse(labelText == null || labelText.isEmpty(), "Test Failed:Forward Contract No is empty!"); 
					   ExcelUtils.updateExcel(excelpath, sheetname, row, "FwdCntrctNo", FwdCntrctNo); 
					   
					   result.put("labelText", labelText);
				  }catch(Exception e) {
					  
				  }
	            }
				  if(FunCode.equalsIgnoreCase("I - Inquire")) {
					  
					  Map<String, String> AppData = getApplicationData(inputData, driver,wait); // Fetch application value
						Validation.validateData(inputData.getHeaderMap(),AppData);
						
						result.putAll(AppData);
				  }
				  
				return result;

}
	    private static Map<String, String> getApplicationData(RowData vr,WebDriver driver ,WebDriverWait wait) throws Exception {
			   
	    	String mainWindowHandle = driver.getWindowHandle();
		    Map<String, String> appData = new HashMap<>();
		    String funcode = vr.getByIndex(2);
		    //////////////////////////////////////General tab/////////////////////////
		    
		    WebElement FWDCElement = driver.findElement(By.xpath("//td[text()='Forward Contract No.']/following-sibling::td"));    
		    String actualFWDCNo = FWDCElement.getText().trim();
		    String actualType = actualFWDCNo.split("\\s+")[0];
		    appData.put("FwdCntrctNumber", actualType);
		    
	        
		    WebElement FWDCType = driver.findElement(By.xpath("//td[text()='Forward Contract Type']/following-sibling::td"));		            
		    String odcmType = FWDCType.getText().trim();
		    String actualDCType = odcmType.split("\\s+")[0];
		    appData.put("FwdCntrctType", actualDCType);
		    
		    WebElement CifId = driver.findElement(By.xpath("//td[text()='CIF ID']/following-sibling::td"));		            
		    String CIFID = CifId.getText().trim();
		    if (CIFID.contains(" ")) {
		        CIFID = CIFID.split(" ")[0].trim();
		    }
		    appData.put("cifId", CIFID);

		    appData.put("acctId", ExcelUtils.getTextOrValue(driver, wait, "acctId","id"));
		    appData.put("FwdCntrctAmt", ExcelUtils.getTextOrValue(driver, wait, "FwdCntrctAmt","id"));
		    appData.put("validfrmDate", ExcelUtils.getTextOrValue(driver, wait, "validfrmDate_ui","id"));
		    appData.put("validtoDate", ExcelUtils.getTextOrValue(driver, wait, "validtoDate_ui","id"));
		    appData.put("Toccy", ExcelUtils.getTextOrValue(driver, wait, "Toccy","id"));
		    appData.put("rateCode", ExcelUtils.getTextOrValue(driver, wait, "rateCode","id"));
		    
		    
		    WebElement LimitTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("tfclimit")));
        	((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", LimitTab);
        	LimitTab.click();
        	
		    appData.put("limitIdPrefix", ExcelUtils.getTextOrValue(driver, wait, "limitIdPrefix","id"));
		    appData.put("rateCode1", ExcelUtils.getTextOrValue(driver, wait, "rateCode","id"));
		    
		    WebElement documentDetailsTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("tfcdoc")));
        	((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", documentDetailsTab);
        	documentDetailsTab.click();
//
        	if(!funcode.equalsIgnoreCase("A - Booking of Contract")) {
		    WebElement ExtentionTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("mntfwcextn")));
        	((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ExtentionTab);
        	ExtentionTab.click();
//        	
		    appData.put("validityFrmDate", ExcelUtils.getTextOrValue(driver, wait, "validityFrmDate_ui","id"));
		    appData.put("validityToDate", ExcelUtils.getTextOrValue(driver, wait, "validityToDate_ui","id"));
		    appData.put("newRateCode", ExcelUtils.getTextOrValue(driver, wait, "newRateCode","id"));
        	}
        	
        	if(funcode.equalsIgnoreCase("I - Inquire")) {
        		
		    WebElement chargetab = wait.until(ExpectedConditions.elementToBeClickable(By.id("tfccharge")));
        	((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", chargetab);
        	chargetab.click();
        	
        	 }
        	
        	if(funcode.equalsIgnoreCase("V - Verify")) {
        	WindowHandle.slowDown(2);
	    	   WebElement Submit = driver.findElement(By.xpath("//input[@id='Submit']"));
	    	   ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Submit);
	    	   Submit.click();
	    	   
	    	   WindowHandle.slowDown(2);
	    	   if (WindowHandle.HandlePopupIfExists(driver)){
	    		   
	   	   		WebElement accept = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accept")));
	   	   		accept.click();
	   	   		driver.switchTo().window(mainWindowHandle);
	   	   		
	   		    }
        	}
        	
        	if(funcode.equalsIgnoreCase("I - Inquire")) {
        	WindowHandle.slowDown(2);
	    	   WebElement Submit = driver.findElement(By.xpath("//input[@id='Submit']"));
	    	   ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Submit);
	    	   Submit.click();
	    	   
	    	   WindowHandle.slowDown(2);
	    	   if (WindowHandle.HandlePopupIfExists(driver)){
	    		   
	   	   		WebElement accept = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accept")));
	   	   		accept.click();
	   	   		driver.switchTo().window(mainWindowHandle);
	   	   		
	   		    }
        	}
			return appData;
	    }   
}
