package CASA;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import Base.DriverManager;
import Utilities.ExcelUtils;
import Utilities.RowData;
import Utilities.Validation;
import Utilities.WindowHandle;

public class OutwardGuaranteeMaintenance {
	private WebDriver driver;
	private WebDriverWait wait;

	    public OutwardGuaranteeMaintenance(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
	    }
	    	    
	    public String execute(RowData inputData , String sheetname,int row,String excelpath) throws Exception {
	    	String mainWindowHandle = driver.getWindowHandle();
	    	WindowHandle.slowDown(4);
	    	
	    	WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),inputData.getByIndex(1));

	    	WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
	    	((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);


    	 	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    	    wait.until(ExpectedConditions.alertIsPresent());
    	    Alert alert = driver.switchTo().alert();
    	    System.out.println("Alert text: " + alert.getText());
    	    alert.accept();
    	    System.out.println("Alert accepted.");
    	    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("usertxt")));
	    	
	        String userID = DriverManager.getProperty("userid2");
	        String password = DriverManager.getProperty("password2");
    	    driver.findElement(By.id("usertxt")).sendKeys(userID);
    	    driver.findElement(By.id("passtxt")).sendKeys(password);
    	    driver.findElement(By.id("Submit")).click();

    	    
	    	try {
		    	  wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
		          wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));
		          
		          String FunCode = inputData.getByIndex(2);
		          WindowHandle.selectDropdownIfValuePresent(driver,wait,(By.id("funcCode")),FunCode);
		          
		          WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("solId"))),inputData.getByIndex(3));
		          
		          WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("bgSrlNum"))),inputData.getByIndex(4));
		          
		          WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("bgType"))),inputData.getByIndex(5));
		          
		          WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cifId"))),inputData.getByIndex(6));
		          
		          WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ccy"))),inputData.getByIndex(7));

		  		WebElement Go = driver.findElement(By.id("Accept"));
				Go.click();
				
				if("V - Verify".equalsIgnoreCase(inputData.getByIndex(2))) {
					
			    	   Map<String, String> appData = getApplicationData(driver,wait); // Fetch application value
			    	   Validation.validateData(inputData.getHeaderMap(),appData);
			    	   
			    	   WindowHandle.slowDown(10);
			    	   WindowHandle.ValidationFrame(driver);
			    	   
				  }
				else {
		          
				WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("acctId"))),inputData.getByIndex(8));
				WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("bgAmt"))),inputData.getByIndex(9));
				
				//////////////////////////////////////party details tab//////////////////
			    WebElement ogmpartyTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("ogmparty")));
			    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ogmpartyTab);
			    ogmpartyTab.click();
			    
			    WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("benName"))),inputData.getByIndex(10));
				WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("benAddr1"))),inputData.getByIndex(11));
			    
				//////////////////////////////////////Guarantee details tab///////////////////////////////////
				WebElement ogmgrnteeTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("ogmgrntee")));
			    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ogmgrnteeTab);
			    ogmgrnteeTab.click();
			    
			    WindowHandle.selectDropdownIfValuePresent(driver,wait,(By.id("applcRules")),inputData.getByIndex(12));
			    WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("purpOfGrntee"))),inputData.getByIndex(13));
				
			    ///////////////////////////////////////////////////////////////////
				WebElement ogmtracerTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("ogmtracer")));
			    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ogmtracerTab);
			    ogmtracerTab.click();
			    
		  		WebElement Sumbit = driver.findElement(By.id("Submit"));
		  		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Sumbit);
		  		Sumbit.click();
				}
			    try{
			    	  WindowHandle.ValidationFrame(driver);
			    	      WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='compField']")));
			    	      String labelText = label.getText(); // Get the text of the label
			    	      String guaranteeNo = labelText.replaceAll("[^0-9]", "");

			    	       Assert.assertFalse(labelText == null || labelText.isEmpty(), "Test Failed: 	Guarantee No is empty!");
			    	       System.out.println(labelText);  	 
			    	       ExcelUtils.updateExcel(excelpath, sheetname, row, "GuaranteeNo", guaranteeNo); 
			    	       
		 	                @SuppressWarnings("unused")
							String expected;
		 	                switch (FunCode) {
		 	                    case "A - Add":
		 	                        expected = labelText;
		 	                        break;
		 	                    case "M - Modify":
		 	                        expected = labelText;
		 	                        break;
		 	                    case "V - Verify":
		 	                        expected = labelText;
		 	                        break;
		 	                    case "D - Delete":
		 	                        expected = labelText;
		 	                        break;
		 	                    case "X - Cancel":
		 	                        expected = labelText;
		 	                        break;
		 	                    default:
		 	                        Assert.fail("Unsupported FunCode: " + FunCode);
		 	                        return labelText;
		 	                }
			    	}catch (Exception e) {
			    	System.out.println("Guarantee NO creartin Error " + e.getMessage());
			    	Assert.fail("Test Failed: Exception occurred -> " + e.getMessage());
		   
			    	}
			    
	    	}catch(Exception e) {}
	    	
			return mainWindowHandle;
	    }
	    
	    private static Map<String, String> getApplicationData(WebDriver driver ,WebDriverWait wait) throws Exception {
			   
		    Map<String, String> appData = new HashMap<>();
		    //////////////////////////////////////General tab/////////////////////////
		    appData.put("acctId", ExcelUtils.getTextOrValue(driver, wait, "acctId","id"));
		    appData.put("bgAmt", ExcelUtils.getTextOrValue(driver, wait, "bgAmt","id"));
		    
		    WebElement PartyetailsTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("ogmparty")));
        	((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", PartyetailsTab);
        	PartyetailsTab.click();
        	
		    appData.put("benName", ExcelUtils.getTextOrValue(driver, wait, "benName","id"));
		    appData.put("benAddr1", ExcelUtils.getTextOrValue(driver, wait, "benAddr1","id"));
		    
		    WebElement GuaranteeDetailsTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("ogmgrntee")));
        	((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", GuaranteeDetailsTab);
        	GuaranteeDetailsTab.click();
        	
		    appData.put("applcRules", ExcelUtils.getTextOrValue(driver, wait, "applcRules","id"));
		    appData.put("purpOfGrntee", ExcelUtils.getTextOrValue(driver, wait, "purpOfGrntee","id"));
		    
	    	   WebElement Submit = driver.findElement(By.xpath("//input[@id='Submit']"));
	    	   ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Submit);
	    	   Submit.click();

			return appData;
	    }}
