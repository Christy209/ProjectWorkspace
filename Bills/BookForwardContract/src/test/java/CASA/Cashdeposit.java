package CASA;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import Base.BaseTest;
import Utilities.ExcelUtils;
import Utilities.RowData;
import Utilities.WindowHandle;

public class Cashdeposit extends BaseTest {
	    private WebDriver driver;
	    private WebDriverWait wait;

	    public Cashdeposit(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
	    }
	 public void execute(RowData id,List<String> cash,String sheetname,int carow,String excelpath) 
	 {
		 	driver.switchTo().defaultContent();
	        driver.switchTo().frame("loginFrame");
	        
		   	WebElement searchbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuSelect")));
	        searchbox.click();
	        searchbox.sendKeys(cash.get(1));
	        WebElement element4 = driver.findElement(By.id("menuSearcherGo"));
	        element4.click();
	        
	        WindowHandle.handleAlertIfPresent(driver);
	        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
	        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));
	        
	        WebElement FunCode = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("funcCode")));
	        FunCode.sendKeys(cash.get(2));
	        WebElement TransactionType = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tranTypeSubType")));
	        TransactionType.sendKeys(cash.get(3));
	        driver.findElement(By.id("Go")).click();

	       try {
	    	   
	    	   WindowHandle.slowDown(2);
	    	   WebElement AcctID 	 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("acctId")));
		       String acctId = id.getByHeader("Created_AccountID");
		       AcctID.sendKeys(acctId);
 	   
	    	   WebElement Currency 	 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("refCrncy")));
	    	   Currency.sendKeys(cash.get(4));
	    	   
	    	   WindowHandle.slowDown(2);
	    	   WebElement Amount = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("refAmt")));
	    	   Amount.sendKeys(cash.get(5));
	    	   
	    	   WebElement TranParticularCode 	 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tranParticularsCode")));
	    	   TranParticularCode.sendKeys(cash.get(6));
	    	   WebElement denominationButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("DENOMDTLS")));
	    	   denominationButton.click();
	    	
	    	wait.until(ExpectedConditions.elementToBeClickable(denominationButton)).click();
	    	WebElement denomCount = wait.until(ExpectedConditions.elementToBeClickable(By.id("arrDenomCount")));
	    	JavascriptExecutor js1 = (JavascriptExecutor) driver;
	    	js1.executeScript("arguments[0].scrollIntoView(true);", denomCount);
	    	denomCount.sendKeys("\u0008");
	    	denomCount.sendKeys(cash.get(8));
	    	
	    	WebElement OkButton = driver.findElement(By.xpath("//input[@type='button' and @id='OK']"));
	    	OkButton.click();
	    	WebElement POST = driver.findElement(By.xpath("//input[@type='button' and @id='Post']"));
	    	POST.click();
	    	
	    	String mainWindow = driver.getWindowHandle();
	    	WindowHandle.handlePopupIfExists(driver);
	    	 WebElement acceptButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("Accept")));
	    	 acceptButton.click();
	
	    	 wait.until(ExpectedConditions.numberOfWindowsToBe(1));
	    	 driver.switchTo().window(mainWindow);
	       } catch (Exception e) {

	          	System.out.println("Error: " + e.getMessage());

	          }
 
		   	try{
		   		WindowHandle.ValidationFrame(driver);
		       	WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody//tr//label[1]")));
		       	String labelText = label.getText(); // Get the text of the label
		       	
		       	if (labelText == null || labelText.isEmpty()) {
		       	    System.out.println("Error: TranID label is empty.");
		       	    Assert.fail("Test Failed: TranID label text is empty!");
		       	    
		       	} else {
		       	    System.out.println("TranID Created Successfully..! TranID: " + labelText);
		       	    String excelfilePath = excelpath; // Path to your Excel file
			        String sheetName = sheetname; // Update with your sheet name
			        int rowNum = carow;  // Row where you want to store
			        //int colNum = 0;  // Column number where Account Number should be stored
			        String columnName = "Created_AccountID"; 
		       	    ExcelUtils.updateExcel(excelfilePath, sheetName, rowNum, columnName, labelText);
		       	}

			}catch (Exception e) {
				System.out.println("TranID creartin Error " + e.getMessage());
				Assert.fail("Test Failed: Exception occurred -> " + e.getMessage());
			}
	 }
	
}
