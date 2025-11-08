package CASA;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import Utilities.ExcelUtils;
import Utilities.RowData;
import Utilities.WindowHandle;

public class BalanceInquire {
	  private WebDriver driver;
	    private WebDriverWait wait;

	    public BalanceInquire(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
	    }
	    public  void execute(RowData id, List<String> bal,String sheetname,int balrow,int balcol,String excelpath ) throws Exception {
	    	
	    	   try {
	    	        // Switch back to default content before menu search
	    	        driver.switchTo().defaultContent();
	    	        driver.switchTo().frame("loginFrame");
	    	        
	               WebElement searchbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuSelect")));
	               searchbox.click();
	               searchbox.sendKeys(bal.get(1));

	               WebElement element4 = driver.findElement(By.id("menuSearcherGo"));
	               element4.click();
	               WindowHandle.handleAlertIfPresent(driver);
	               
	           } catch (Exception e) {
	               System.out.println("Error in menu selection: " + e.getMessage());
	           }

	           wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
	           wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));

	           WebElement AccountId = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("foracid")));
	           AccountId.sendKeys(id.getByHeader("Created_AccountID"));

	           WebElement okButton = driver.findElement(By.xpath("//input[@type='button' and @id='Submit']"));
	           JavascriptExecutor js = (JavascriptExecutor) driver;
	           js.executeScript("arguments[0].click();", okButton);
	           
	           WebElement effectiveAvailAmt = driver.findElement(By.id("accbal_eff_avail_amt15"));
	           String balanceText = effectiveAvailAmt.getText();

	           // Print or validate the extracted text
	           System.out.println("Effective Available Amount: " + balanceText);
	           
	           	String excelFilePath = excelpath;
	  	        String sheetName = sheetname;
	  	        int targetRow = balrow;      ///4  
	  	        int targetColumn = balcol; //74

	  	        try {
	  				  ExcelUtils.setCellData(excelFilePath, sheetName, targetRow, targetColumn, balanceText);


	   	   } catch (Exception e) {
	   	       System.out.println("❌ Popup handling failed: " + e.getMessage());
	   	   }
	  	        
	  	      WebElement OkButton = driver.findElement(By.xpath("//input[@type='button' and @id='Ok']"));
	          JavascriptExecutor js2 = (JavascriptExecutor) driver;
	          js2.executeScript("arguments[0].click();", OkButton);
	          driver.switchTo().defaultContent();
	    }
		 public static void writeBalanceToNextColumn(String filePath, String sheetName, int baseColumnIndex, int targetRowIndex, String balance) throws IOException {
			    FileInputStream fis = new FileInputStream(filePath);
			    Workbook workbook = new XSSFWorkbook(fis);
			    Sheet sheet = workbook.getSheet(sheetName);

			    Row row = sheet.getRow(targetRowIndex);
			    if (row == null) row = sheet.createRow(targetRowIndex);

			    // Start after the Created_AccountID column (i.e., baseColumnIndex + 1)
			    int colIndex = baseColumnIndex + 1;

			    // Find the next empty column in the 4th row
			    while (row.getCell(colIndex) != null && !row.getCell(colIndex).getStringCellValue().isEmpty()) {
			        colIndex++;
			    }

			    Cell cell = row.createCell(colIndex);
			    cell.setCellValue(balance);

			    fis.close();
			    FileOutputStream fos = new FileOutputStream(filePath);
			    workbook.write(fos);
			    fos.close();
			    workbook.close();
			}

}
