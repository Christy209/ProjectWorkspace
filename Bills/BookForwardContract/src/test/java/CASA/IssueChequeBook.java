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

import Utilities.RowData;
import Utilities.WindowHandle;

public class IssueChequeBook {
	private WebDriver driver;
	 private WebDriverWait wait;

	    public IssueChequeBook(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
	    }
	        
	        public void execute(RowData id ,List<String> issue) throws Exception {

	        	driver.switchTo().defaultContent();
		        driver.switchTo().frame("loginFrame");
		        
		    	WebElement searchbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuSelect")));
		        searchbox.sendKeys(issue.get(1));
		        
		       WebElement element4 = driver.findElement(By.id("menuSearcherGo"));
		       element4.click();
		       
		       WindowHandle.handleAlertIfPresent(driver);
		       try {
		           // Switch to required frames
		           wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
		           wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));
		           
		            // Select Function Code
		        	WebElement FunCode = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("funcCode")));
			           FunCode.sendKeys(issue.get(2));

		            // Enter Account Number
		            WebElement account = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("acctNum")));
		            String foracid = id.getByHeader("Created_AccountID");
		            account.clear();
		            account.sendKeys(foracid);
		            
		            String ackValue = issue.get(3); // "Y" or "N"
		            WebElement radio = wait.until(ExpectedConditions.presenceOfElementLocated(
		                By.xpath("//input[@type='radio' and @value='" + ackValue + "']")));

		            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", radio);
		            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", radio);

		            // Enter To End Cheque Serial No.
		            
//		            WebElement issuedate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("ichba.issueDate_ui")));
//		            issuedate.clear();
//		            issuedate.sendKeys(issue.get(4));
		            
		            WebElement beginChqAlphaFrom = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("ichba.beginChqAlphaFrom")));
		            beginChqAlphaFrom.clear();
		            beginChqAlphaFrom.sendKeys(issue.get(5));
		            
		            WebElement beginChqSrlNumFrom = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("beginChqSrlNumFrom")));
		            beginChqSrlNumFrom.clear();
		            beginChqSrlNumFrom.sendKeys(issue.get(6));

		            WebElement noOfChqLvs = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noOfChqLvs")));
		            noOfChqLvs.clear();
		            noOfChqLvs.sendKeys(issue.get(7));

		            WebElement lvsPerBook = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("lvsPerBook")));
		            lvsPerBook.clear();
		            lvsPerBook.sendKeys(issue.get(8));
		            
		            // Click Accept
		            wait.until(ExpectedConditions.elementToBeClickable(By.id("Accept"))).click();
		            WindowHandle.slowDown(2);

		            wait.until(ExpectedConditions.elementToBeClickable(By.id("Submit"))).click();
		            
		    	   	String acctid = id.getByHeader("Created_AccountID");
		            WindowHandle.ValidationFrame(driver);
		            WebElement msgLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='compField']")));
		            String actualMessage = msgLabel.getText().trim();
		            String expectedMessage = "Selected cheque books issued successfully to A/c. ID. - " + acctid;
		            
		            Assert.assertEquals(actualMessage, expectedMessage,"Failed: message was not found! Actual:" + actualMessage);
		            

		        } catch (Exception e) {
		            System.out.println("Error during cheque book issue: " + e.getMessage());
		        }
		       
	        }
	        }

