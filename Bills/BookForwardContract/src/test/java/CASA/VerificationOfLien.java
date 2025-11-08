package CASA;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import Utilities.DateUtils;
import Utilities.RowData;
import Utilities.WindowHandle;

public class VerificationOfLien {
	private WebDriver driver;
	 private WebDriverWait wait;

	    public VerificationOfLien(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
	    }
	        
	        public void execute(RowData id ,List<String> li,String sheetname,int acctid,int lirow,String excelpath,int Olirow) throws Exception {
	        	
	        	JavascriptExecutor js = (JavascriptExecutor) driver;
	        	
			 	driver.switchTo().defaultContent();
		        driver.switchTo().frame("loginFrame");
		        
		    	WebElement searchbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuSelect")));
		        searchbox.click();
		        searchbox.sendKeys(li.get(1));

		       WebElement element4 = driver.findElement(By.id("menuSearcherGo"));
		       element4.click();
		       
		       WindowHandle.handleAlertIfPresent(driver);
		       try {
		           // Switch to required frames
		           wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
		           wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));
		           
		           WebElement Function  = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("funcCode")));
		           Function.sendKeys("V - Verify");
		                    
		           WebElement AcctId = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("acctId")));
		           AcctId.sendKeys(id.getByHeader("Created_AccountID"));
		           
	               WebElement GoButton = driver.findElement(By.xpath("//input[@type='button' and @id='Accept']"));
		           js.executeScript("arguments[0].click();", GoButton);
		           
		           WindowHandle.slowDown(2);
		           try {  
		        	   WindowHandle.ValidationFrame(driver);
		        	   
		        	   String fieldValue = WindowHandle.getValueByLabel(driver, "A/c. ID");  // e.g. "003000500000340 INR 003 RAJESH ROY..."
		        	   String acctId = fieldValue.split("\\s+")[0];   

		        	   String xpathModule =
		        			    "//td[@class='textlabel' and normalize-space(text())='Module Type']"
		        			  + "/following-sibling::td[@class='textfielddisplaylabel']";
		        	   String moduleCode  = xpathModule.split("\\s+")[0];
		        	   
		        	   wait.until(ExpectedConditions.visibilityOfElementLocated(
		        	       By.xpath("//td[contains(normalize-space(.),'.00')]/ancestor::tr")
		        	   ));

		        	   // Find that row
		        	   WebElement dataRow = driver.findElement(
		        	       By.xpath("//td[contains(normalize-space(.),'.00')]/ancestor::tr")
		        	   );
		        	   
		        	   List<String> values = dataRow.findElements(By.tagName("td")).stream()
		        	       .map(td -> td.getText().replace("\u00A0","").trim())
		        	       .filter(text -> !text.isEmpty())
		        	       .collect(Collectors.toList());

		        	   int last = values.size() - 1;
		        	   String del      = values.get(last);
		        	   String contact  = values.get(last - 1);
		        	   String reason   = values.get(last - 2);
		        	   String expDate  = values.get(last - 3);
		        	   String effDate  = values.get(last - 4);
		        	   String oldLien  = values.get(last - 5);
		        	   String newLien  = values.get(last - 6);

		        	   FileInputStream fis = new FileInputStream(excelpath);
		               Workbook workbook = new XSSFWorkbook(fis);
		               Sheet sheet = workbook.getSheet(sheetname);

		               Row row1 = sheet.getRow(acctid); // Contains account id
		               Row row = sheet.getRow(lirow);  // Contains lien details
		               Row oldrow = sheet.getRow(Olirow);
		               
		               Map<String, String> actualData = new HashMap<>();
		               actualData.put("AcctId", acctId);
		               actualData.put("ModuleType", moduleCode);
		               actualData.put("del", del);
		               actualData.put("contact", contact);
		               actualData.put("reason", reason);
		               actualData.put("expDate", expDate);
		               actualData.put("effDate", effDate);
		               actualData.put("oldLien", oldLien);
		               actualData.put("newLien", newLien);
		               
		               validateAppData(actualData, row1, row,oldrow);

		               workbook.close();
		               fis.close();
		               
		               WindowHandle.slowDown(1);
		               wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Submit"))).click();
		               
		               WindowHandle.ValidationFrame(driver);
			           
			           WebElement msgElem = wait.until(ExpectedConditions.visibilityOfElementLocated(
			        		    By.xpath("//font[@face='Arial, Helvetica, sans-serif']")
			        		));

			        		String actualMessage = msgElem.getText().trim();
			        		String expectedMessage = "Record verified successfully.";

			        		Assert.assertEquals(
			        		    actualMessage,
			        		    expectedMessage,
			        		    "Unexpected message displayed: expected '" + expectedMessage + "', but found '" + actualMessage + "'"
			        		);
			        		
		         }catch (Exception e) {System.out.println("Printing UI values failed : "+e);}

		           
	        }catch (Exception e) {
	        	System.out.println("Verification of lien failed : "+e);
	        }

	     }
	        private static void validateAppData(Map<String, String> appData, Row row1, Row row,Row oldrow) throws ParseException {
	        	    
	            Map<String, String> expectedData = new HashMap<>();

	            String date = getCellValue(row, 6);
	            String Function = getCellValue(row, 2);
		           String formatedate = DateUtils.toDDMMYYYY(date);
	            // Safe reads: Excel values may be null otherwise
		        expectedData.put("AcctId",     getCellValue(row1, 74));
		        expectedData.put("ModuleType", getCellValue(row, 10));
	            expectedData.put("del",     getCellValue(row, 8));
	            expectedData.put("contact", getCellValue(row, 7));
	            expectedData.put("reason",  getCellValue(row, 4));
	            expectedData.put("expDate", formatedate);
	            expectedData.put("effDate", getCellValue(row, 5));
	            expectedData.put("oldLien", getCellValue(row, 11));
	            if("M - Modify".equalsIgnoreCase(Function)) {
	            	expectedData.put("oldLien", getCellValue(oldrow, 3));
	            }
	            expectedData.put("newLien", getCellValue(row, 3));
	            
	            int matchedCount = 0;
	            int mismatchedCount = 0;

	            for (Map.Entry<String, String> entry : expectedData.entrySet()) {
	                String key = entry.getKey();
	                String expected = entry.getValue();
	                String actual = appData.getOrDefault(key, "");

	                String normExpected = normalize(expected.replace(",", ""));
	                String normActual = normalize(actual.replace(",", ""));

	                // Skip validation if expected is blank
	                if (expected.isEmpty()) {
	                    System.out.println("⚠️ Skipping because Excel is empty.");
	                    continue;
	                }

	                if (isNumeric(normExpected) && isNumeric(normActual)) {
	                    BigDecimal expectedNum = new BigDecimal(normExpected);
	                    BigDecimal actualNum = new BigDecimal(normActual);
	                    if (expectedNum.compareTo(actualNum) != 0) {
	                        System.out.println("❌ Mismatch in " + key + ": expected [" + expected + "] but found [" + actual + "]");
	                        mismatchedCount++;
	                    } else {
	                        System.out.println("✅ " + key + " matched: " + actual);
	                        matchedCount++;
	                    }
	                } else {
	                    if (!normExpected.equals(normActual)) {
	                        System.out.println("❌ Mismatch in " + key + ": expected [" + expected + "] but found [" + actual + "]");
	                        mismatchedCount++;
	                    } else {
	                        System.out.println("✅ " + key + " matched: " + actual);
	                        matchedCount++;
	                    }
	                }
	            }

	            System.out.println("Total Matched: " + matchedCount);
	            System.out.println("Total Mismatched: " + mismatchedCount);

	            Assert.assertEquals(mismatchedCount, 0, "❌ Test Failed: Mismatches found in data validation!");
	        }

	        private static String normalize(String value) {
	            if (value == null || value.trim().isEmpty()) return "";
	            value = value.trim().replaceAll("\\.0+$", ""); // Remove trailing .0
	            if (value.matches("\\d{2}[-/]\\d{2}[-/]\\d{4}")) {
	                return value.replace("-", "/"); // Normalize date separators
	            }
	            return value.toLowerCase();
	        }

	        private static boolean isNumeric(String str) {
	            try {
	                new BigDecimal(str);
	                return true;
	            } catch (NumberFormatException e) {
	                return false;
	            }
	        }
	        private static String getCellValue(Row row, int colIndex) {
	            if (row == null) return "";
	            return row.getCell(colIndex, MissingCellPolicy.CREATE_NULL_AS_BLANK)
	                      .toString()
	                      .trim();
	        }
	}
