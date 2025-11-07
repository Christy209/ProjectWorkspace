package OD;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import Annotation.ExcelData;
import Base.BaseTest;
import Base.DriverManager;
import Utilities.Dataproviders;
import Utilities.ExcelUtils2;
import Utilities.LogOut;
import Utilities.RowData;
import Utilities.WindowHandle;

public class ModifyOD extends BaseTest{
	
	protected static WebDriver driver = DriverManager.getDriver();
    protected static WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

    String excelpath = System.getProperty("user.dir") + "/resources/LoanLoan.xlsx";
    int row = 2;
    String sheetname = "Sheet3"; 
    
//	   @BeforeClass
//	    public  void setup() throws IOException {
//	        driver = DriverManager.getDriver();
//	        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
//
//	        String userID = DriverManager.getProperty("userid");
//	        String password = DriverManager.getProperty("password");
//
//	        DriverManager.login(userID, password);
//	        System.out.println("✅ Logged in as: " + userID);
//	    }
    
    @Test(dataProvider = "testcase", dataProviderClass = Dataproviders.class)
    @ExcelData(sheetName = "Sheet3", rowIndex = {2, 1})  
    public void CreateLoan(RowData rowData, RowData modifyData) throws Exception {
    	

		  driver.switchTo().defaultContent(); 
		  driver.switchTo().frame("loginFrame");
		 WindowHandle.slowDown(6);
		 

    	 WindowHandle.slowDown(4);
         WindowHandle.setValueWithJS(driver,wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menuSelect"))),rowData.getByIndex(1));
         WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
         ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

         wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
         wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));
 
   	        
   	     WebElement acctID = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tempForacid")));
         String AcctID = modifyData.getByHeader("Created_AccountID");
         ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", acctID, AcctID);
         WindowHandle.captureErrors(driver, 5);
        
            JavascriptExecutor js1 = (JavascriptExecutor) driver;
            js1.executeScript("document.getElementById('Accept').click();");

            WindowHandle.captureErrors(driver, 5);
            
            try {
            	 WindowHandle.slowDown(2);
 	            ((JavascriptExecutor) driver).executeScript("document.getElementById('miscodes').click();");
 	            
 	           WindowHandle.slowDown(2);
	            ((JavascriptExecutor) driver).executeScript("document.getElementById('documentdetails').click();");
	            
	            WindowHandle.slowDown(2);
	            	
	    			WebElement relatedPartyTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("relatedpartydetails")));
	    			JavascriptExecutor js = (JavascriptExecutor) driver;
	    			js.executeScript("arguments[0].click();", relatedPartyTab);
	    			
	    			   WindowHandle.slowDown(2);
	    	            ((JavascriptExecutor) driver).executeScript("document.getElementById('generaldetails2').click();");
	    		
	    	            WindowHandle.slowDown(2);
		    			 WebElement tdsch = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sbschemedetails")));
		    			 ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tdsch);

		    			 WindowHandle.safeClick(driver, wait, By.id("Validate"));
		    			 
		    			 WindowHandle.slowDown(2);
		    	            driver.findElement(By.id("linttmacct")).click();
		    	            
		    	            WindowHandle.slowDown(2);
			 	            ((JavascriptExecutor) driver).executeScript("document.getElementById('acctlmt').click();");
			 	             
			 	            WindowHandle.slowDown(2);
			 	           WebElement submit1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Submit")));
		 	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submit1);
		 	                
		 	               try {
		 	                  WindowHandle.ValidationFrame(driver);

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
		 	              driver.switchTo().defaultContent();
		 			       LogOut.performLogout(driver, wait);
            }catch(Exception e) {
            	System.out.println("Error Message"+e);
            }
}
}
