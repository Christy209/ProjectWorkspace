package CASA;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import Utilities.ExcelUtils;
import Utilities.ExpectedErrorException;
import Utilities.RowData;
import Utilities.WindowHandle;

public class CashWithdrawal {
	  private WebDriver driver;
	    private WebDriverWait wait;

	    public CashWithdrawal(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
	    }
	    public void execute(RowData id, RowData baln, RowData error,List<String> cw, String sheetname, int cwrow, String excelpath) throws Exception {

	            driver.switchTo().defaultContent();
	            driver.switchTo().frame("loginFrame");
	            WebElement searchbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuSelect")));
	            searchbox.click();
	            searchbox.sendKeys(cw.get(1));
	            WebElement element4 = driver.findElement(By.id("menuSearcherGo"));
	            element4.click();
	            WindowHandle.handleAlertIfPresent(driver);

	            try {
	                wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
	                wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));

	                WebElement FunCode = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("funcCode")));
	                FunCode.sendKeys(cw.get(2));

	                WebElement TransactionType = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tranTypeSubType")));
	                TransactionType.sendKeys(cw.get(3));

	                driver.findElement(By.id("Go")).click();
	            } catch (Exception e) {
	                System.out.println("Error in navigation frame: " + e.getMessage());
	            }

	            try {
	                WindowHandle.slowDown(1);
	                WebElement AcctID = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("acctId")));
	                AcctID.sendKeys(id.getByHeader("Created_AccountID"));

	                WebElement Currency = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("refCrncy")));
	                Currency.sendKeys(cw.get(4));

	                WindowHandle.slowDown(1);
	                WebElement Amount = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("refAmt")));
	                Amount.sendKeys(cw.get(5));

	                WebElement TranParticularCode = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tranParticularsCode")));
	                TranParticularCode.sendKeys(cw.get(6));

	                WebElement denominationButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("DENOMDTLS")));
	                denominationButton.click();

	                wait.until(ExpectedConditions.elementToBeClickable(denominationButton)).click();
	                WebElement denomCount = wait.until(ExpectedConditions.elementToBeClickable(By.id("arrDenomCount")));
	                JavascriptExecutor js1 = (JavascriptExecutor) driver;
	                js1.executeScript("arguments[0].scrollIntoView(true);", denomCount);
	                denomCount.sendKeys("\u0008");
	                denomCount.sendKeys(cw.get(8));

	                WebElement OkButton = driver.findElement(By.xpath("//input[@type='button' and @id='OK']"));
	                OkButton.click();

	                WindowHandle.slowDown(1);
	                WebElement post = driver.findElement(By.id("Post"));
	                post.click();

	                String mainWindow = driver.getWindowHandle();
	                WindowHandle.handlePopupIfExists(driver);

	                try {
	                	
			                try {
			                    WebElement acceptButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Accept")));
			                    if (acceptButton.isDisplayed() && acceptButton.isEnabled()) {
			                        acceptButton.click();
			                        System.out.println("Clicked Accept button.");
			                    }
			                } catch (Exception e) {
			                    System.out.println("Accept button not present, may have been auto-handled.");
			                }

		                    driver.switchTo().window(mainWindow);
	                    
	                		WindowHandle.slowDown(1);
	                        WindowHandle.ValidationFrame(driver);
	                        WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody//tr//label[1]")));
	                        String labelText = label.getText();

	                        if (labelText == null || labelText.isEmpty()) {
	                            System.out.println("Error: Failed to create transaction.");
	                        } else {
	                            System.out.println("TranID Created Successfully..! TranID: " + labelText);
	                        }

	                        ExcelUtils.updateExcel(excelpath, sheetname, cwrow, "Created_AccountID", labelText);

	                } catch (Exception e) {
	                	
	                    WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("table")));
	                    String fullMsg = table.getText();
	                    System.out.println("Captured popup message:\n" + fullMsg);

	                    WebElement cancel = wait.until(ExpectedConditions.elementToBeClickable(By.id("cancel")));
	                    cancel.click();
	                    
	                    driver.switchTo().window(mainWindow);

	                    if (fullMsg.contains(error.getByHeader("ErrorCode"))) {
	                        System.out.println("Expected error received.");
	                        throw new ExpectedErrorException(error.getByHeader("ErrorCode"));
	                    } else {
	                        throw new RuntimeException("Unexpected error: " + fullMsg);
	                    }
	                }
                   
	            } catch (Exception e) {
	                System.out.println("CashWithDrawal error: " + e.getMessage());
	                throw e;  // propagate if needed
	            }

	    }
}
