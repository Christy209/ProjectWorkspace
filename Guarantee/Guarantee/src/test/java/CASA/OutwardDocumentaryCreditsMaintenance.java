package CASA;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import Utilities.DateUtils;
import Utilities.ErrorCapture;
import Utilities.ExcelUtils;
import Utilities.RowData;
import Utilities.Validation;
import Utilities.WindowHandle;

public class OutwardDocumentaryCreditsMaintenance {
	private WebDriver driver;
	 private WebDriverWait wait;

	    public OutwardDocumentaryCreditsMaintenance(WebDriver driver) {
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
		          String FunCode = inputData.getByIndex(2);
		          WindowHandle.selectDropdownIfValuePresent(driver,wait,(By.id("funcCode")),FunCode);
		          
		          WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("solId"))),inputData.getByIndex(3));
		          
		          WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("odcmNum"))),inputData.getByIndex(4));
		          
		          WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("odcmType"))),inputData.getByIndex(5));
		          
		          WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cifId"))),inputData.getByIndex(6));
		          
		          WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("dcCurrency"))),inputData.getByIndex(7));
		          
		  		WebElement Go = driver.findElement(By.id("Go"));
				Go.click();
				
				//ErrorCapture.checkForError(driver);
				
				if("V - Verify".equalsIgnoreCase(inputData.getByIndex(2))) {
					
			    	   Map<String, String> appData = getApplicationData(driver,wait); // Fetch application value
			    	   Validation.validateData(inputData.getHeaderMap(),appData);
			    	   
			    	   WindowHandle.slowDown(10);
			    	   WindowHandle.ValidationFrame(driver);

				  }
				
				  WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("acctId"))),inputData.getByIndex(8));
		          
		          WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("dcOpenValueAmt"))),inputData.getByIndex(9));
				///////////////////////////related party details//////////////////////////////
		  	    WebElement dcpartyTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("dcparty")));
			    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dcpartyTab);
			    dcpartyTab.click();
			    
			      WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("benfName"))),inputData.getByIndex(10));
		          
		          WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("benfAddr1"))),inputData.getByIndex(11));
		          
		          ///////////////////////////////////DC Details Tab///////////////////////////////////////////////
		          
			    WebElement dcDetailsTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("odcmdcdetails")));
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dcDetailsTab);
				dcDetailsTab.click();
				
				WindowHandle.selectDropdownIfValuePresent(driver,wait,(By.id("applicableRules")),inputData.getByIndex(12));
				
				WindowHandle.selectDropdownIfValuePresent(driver,wait,(By.id("tenor")),inputData.getByIndex(13));
				
				WindowHandle.selectDropdownIfValuePresent(driver,wait,(By.id("dueDateInd")),inputData.getByIndex(14));

			    String EXDate = inputData.getByIndex(15);
			    String formattedDate = DateUtils.toDDMMYYYY(EXDate);
			    WindowHandle.setValueWithJS(
			        driver,
			        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("expDate_ui"))),
			        formattedDate
			    );
		          
		        WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("plExpDate"))),inputData.getByIndex(16));
				
			    WebElement NextPage = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@id='NextPage']")));
				NextPage.click();
				
			    String LatestDate = inputData.getByIndex(17);
			    String LaformattedDate = DateUtils.toDDMMYYYY(LatestDate);
			    WindowHandle.setValueWithJS(
				        driver,
				        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("latestDateOfShipment_ui"))),
				        LaformattedDate
				    );
			    
			    WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("negotPeriod"))),
			    inputData.getByIndex(18));
			    
			    WindowHandle.selectDropdownIfValuePresent(driver,wait,(By.id("chargesBorneBy")),inputData.getByIndex(19));
			    
			    String confRequired = inputData.getByIndex(20);
				
				if(confRequired.equalsIgnoreCase("Confirm"))
				{
				WebElement confRequiredFlg = driver.findElement(By.xpath("//input[@id='confRequired' and @value='Y']"));
				confRequiredFlg.click();
				
				}else if(confRequired.equalsIgnoreCase("Without")){
					
				WebElement confRequiredFlg = driver.findElement(By.xpath("//input[@id='confRequired' and @value='N']"));
				confRequiredFlg.click();
				
				}else {
					WebElement confRequiredFlg = driver.findElement(By.xpath("//input[@id='confRequired' and @value='M']"));
					confRequiredFlg.click();
				}
				///////////////////////////////Charge details tab/////////////////////////////////////
			    WebElement ChargeDetailsTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("tfccharge")));
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ChargeDetailsTab);
				ChargeDetailsTab.click();
				
			    WebElement Submit = wait.until(ExpectedConditions.elementToBeClickable(By.id("Submit")));
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Submit);
				Submit.click();
				
				try{
			    	  WindowHandle.ValidationFrame(driver);
			    	      WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='compField']")));
			    	      String labelText = label.getText(); // Get the text of the label
			    	      String DCNo = labelText.replaceAll("[^0-9]", "");

			    	       Assert.assertFalse(labelText == null || labelText.isEmpty(), "Test Failed:Documentary Credit No is empty!");
			    	       System.out.println(labelText);  	 
			    	       ExcelUtils.updateExcel(excelpath, sheetname, row, "DCNo", DCNo); 
			    	       
		 	                @SuppressWarnings("unused")
							String expected;
		 	                switch (FunCode) {
		 	                    case "S - Issue":
		 	                        expected = labelText;
		 	                        break;
		 	                    case "M - Modify":
		 	                        expected = labelText;
		 	                        break;
		 	                    case "V - Verify":
		 	                        expected = labelText;
		 	                        break;
		 	                    case "X - Cancel":
		 	                        expected = labelText;
		 	                        break;
		 	                    default:
		 	                        Assert.fail("Unsupported FunCode: " + FunCode);
		 	                        return labelText;
		 	                }
		 	               return DCNo; 
		 	               
			    	}catch (Exception e) {
			    		
			    		System.out.println(e);
		   
			    	}
   
	    	}catch(Exception e) {
	    		
	    		System.out.println("Documentary credit NO creartin Error " + e.getMessage());
		    	//Assert.fail("Test Failed: Exception occurred -> " + e.getMessage());
	    	}
	    	
			return mainWindowHandle;
	    }
	    
	    private static Map<String, String> getApplicationData(WebDriver driver ,WebDriverWait wait) throws Exception {
			   
	    	String mainWindowHandle = driver.getWindowHandle();
		    Map<String, String> appData = new HashMap<>();
		    //////////////////////////////////////General tab/////////////////////////
		    
		    WebElement docNoElement = driver.findElement(By.xpath("//td[text()='Documentary Credit No.']/following-sibling::td"));    
		    String actualDocNo = docNoElement.getText().trim();
		    String actualType = actualDocNo.split("\\s+")[0];
		    appData.put("odcmNum", actualType);
		    
	        
		    WebElement DCType = driver.findElement(By.xpath("//td[text()='Documentary Credit Type']/following-sibling::td"));		            
		    String odcmType = DCType.getText().trim();
		    String actualDCType = odcmType.split("\\s+")[0];
		    appData.put("odcmType", actualDCType);
		    
		    WebElement CifId = driver.findElement(By.xpath("//td[text()='CIF ID']/following-sibling::td"));		            
		    String CIFID = CifId.getText().trim();
		    appData.put("cifId", CIFID);

		    // Documentary Credit Amt
		    WebElement amtElement = driver.findElement(By.xpath("//td[text()='Documentary Credit Amt.']/following-sibling::td"));		            
		    String actualAmt = amtElement.getText().trim();
		    appData.put("dcOpenValueAmt", actualAmt);

		    appData.put("acctId", ExcelUtils.getTextOrValue(driver, wait, "acctId","id"));
		    appData.put("dcOpenValueAmt", ExcelUtils.getTextOrValue(driver, wait, "dcOpenValueAmt","id"));
		    
		    WebElement PartyetailsTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("dcparty")));
        	((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", PartyetailsTab);
        	PartyetailsTab.click();
        	
		    appData.put("benfName", ExcelUtils.getTextOrValue(driver, wait, "benfName","id"));
		    appData.put("benfAddr1", ExcelUtils.getTextOrValue(driver, wait, "benfAddr1","id"));
		    
		    WebElement GuaranteeDetailsTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("odcmdcdetails")));
        	((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", GuaranteeDetailsTab);
        	GuaranteeDetailsTab.click();
        	
		    appData.put("applicableRules", ExcelUtils.getTextOrValue(driver, wait, "applicableRules","id"));
		    appData.put("tenor", ExcelUtils.getTextOrValue(driver, wait, "tenor","id"));
		    appData.put("dueDateInd", ExcelUtils.getTextOrValue(driver, wait, "dueDateInd","id"));
		    appData.put("expDate_ui", ExcelUtils.getTextOrValue(driver, wait, "expDate_ui","id"));
		    appData.put("plExpDate", ExcelUtils.getTextOrValue(driver, wait, "plExpDate","id"));
		    
		    WebElement NextPage = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@id='NextPage']")));
			NextPage.click();
			
			appData.put("latestDateOfShipment_ui", ExcelUtils.getTextOrValue(driver, wait, "latestDateOfShipment_ui","id"));
		    appData.put("negotPeriod", ExcelUtils.getTextOrValue(driver, wait, "negotPeriod","id"));
		    appData.put("chargesBorneBy", ExcelUtils.getTextOrValue(driver, wait, "chargesBorneBy","id"));
		    //appData.put("confRequired", ExcelUtils.getTextOrValue(driver, wait, "confRequired","id"));
			
		    WebElement chargetab = wait.until(ExpectedConditions.elementToBeClickable(By.id("tfccharge")));
        	((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", chargetab);
        	chargetab.click();
        	
        	WindowHandle.slowDown(5);
	    	   WebElement Submit = driver.findElement(By.xpath("//input[@id='Submit']"));
	    	   ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Submit);
	    	   Submit.click();
	    	   
	    	   WindowHandle.slowDown(5);
	    	   if (WindowHandle.HandlePopupIfExists(driver)){
	    		   
	   	   		WebElement accept = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accept")));
	   	   		accept.click();
	   	   		driver.switchTo().window(mainWindowHandle);
	   		    }

			return appData;
	    }
}
