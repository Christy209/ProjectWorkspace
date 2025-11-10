package CustomMenu;

import java.text.ParseException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import Utilities.DateUtils;
import Utilities.ExcelUtils;
import Utilities.WindowHandle;

public class PMSBY {
	 private WebDriver driver;
	 private WebDriverWait wait;

	    public PMSBY(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
	    }
	    public void execute(List<String> data,String excelpath,String sheetname,int row) throws Exception {
	    	
		 	driver.switchTo().defaultContent();
	        driver.switchTo().frame("loginFrame");

	        WindowHandle.slowDown(1);
	    	WebElement searchbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuSelect")));
	        searchbox.click();
	        searchbox.sendKeys(data.get(1));

	       WebElement element4 = driver.findElement(By.id("menuSearcherGo"));
	       element4.click();
	       
	       WindowHandle.handleAlertIfPresent(driver);
	       try {
	           // Switch to required frames
	           wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("CoreServer")));
	           wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));

	           String FunCode = data.get(2);
	           WebElement funCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("funcCode")));
	           WindowHandle.selectByVisibleText(funCode,data.get(2));

	           String cif = data.get(3);
	           wait.until(ExpectedConditions.presenceOfElementLocated(By.name("pmsby.CustId"))).sendKeys(data.get(3));
	           
	           wait.until(ExpectedConditions.presenceOfElementLocated(By.id("accountno"))).sendKeys(data.get(4));
	           
	           WebElement go = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Accept")));
	           ((JavascriptExecutor) driver).executeScript("arguments[0].click();", go);
	           ////error/////////
	           if (!"I-Inquire".equalsIgnoreCase(FunCode)) {
	           WindowHandle.Go(driver, wait);
	           }
	           String error = data.get(12);
	           if ("I-Inquire".equalsIgnoreCase(FunCode)) {
	        	   if (error != null && !error.trim().isEmpty()) {
	        	   WindowHandle.ValidationFrame(driver);
                   By headerLocator = By.xpath("//a[@onmouseover='displayHand(this)']");
                   WebElement errorElem = wait.until(ExpectedConditions.visibilityOfElementLocated(headerLocator));
                   String Actual = errorElem.getText().trim();
                   System.out.println(Actual);
                   Assert.assertEquals(
                		   Actual,
                		   error,
                	        "❌ Inquiry error mismatch: expected='" + error + "', but was='" + Actual + "'"
                   );
	           }else {
	        	   VerificationValidation(data);
	           }
	           }
	           if("A-Add".equalsIgnoreCase(FunCode)){
	        	   
	           WebElement Medical = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Medical")));
	           WindowHandle.selectByVisibleText(Medical,data.get(5));
	           
	           WebElement DisorderDet = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("DisorderDet")));
	           DisorderDet.sendKeys(data.get(6));
	           
	           WebElement Minorflg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Minorflg")));
	           WindowHandle.selectByVisibleText(Minorflg,data.get(7));
	           
	           WebElement Relation = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Relation")));
	           WindowHandle.selectByVisibleText(Relation,data.get(8));
	            
	           WebElement NomName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("NomName")));
	           NomName.clear();
	           NomName.sendKeys(data.get(9));
	           
	            String STexception = data.get(10);	            
	            if("Yes".equalsIgnoreCase(STexception)) {
	            	
                WebElement STException = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//input[@id='STExcept' and @value='Y']")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", STException);
	            }else {
	            	
	            	WebElement STException = wait.until(ExpectedConditions.elementToBeClickable(
	                        By.xpath("//input[@id='STExcept' and @value='N']")));
	                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", STException);
	            }
	            
	             String Dob = data.get(11);
	             String formatted = DateUtils.toDDMMYYYY(Dob);
	         
	            WebElement DOBMinor = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("DOBMinor_ui")));
	            DOBMinor.sendKeys(formatted);
	           
	           }

	           
	           if("V-Verify".equalsIgnoreCase(FunCode)){
	            	VerificationValidation(data);
	            }
	            
	            if("M-Modify".equalsIgnoreCase(FunCode)){
	            	
	               WebElement Medical = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Medical")));
	  	           WindowHandle.selectByVisibleText(Medical,data.get(5));
	  	           
	  	           WebElement DisorderDet = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("DisorderDet")));
	  	           DisorderDet.sendKeys(data.get(6));
	  	           
	  	           WebElement Minorflg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Minorflg")));
	  	           WindowHandle.selectByVisibleText(Minorflg,data.get(7));
	  	           
	  	           WebElement Relation = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Relation")));
	  	           WindowHandle.selectByVisibleText(Relation,data.get(8));
	  	            
	  	           WebElement NomName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("NomName")));
	  	           NomName.clear();
	  	           NomName.sendKeys(data.get(9));
	            }
	            
	            if (!"I-Inquire".equalsIgnoreCase(FunCode)) {
	            	  String msg = null;
	            	  
	                WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(By.id("Submit")));
	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submit);

	                WindowHandle.UIError(driver, wait);
	                WebElement messageCell = wait.until(ExpectedConditions.visibilityOfElementLocated(
	 	                   By.cssSelector("table.resultpage td:nth-child(2)")
	 	                   ));
	 	                    msg = messageCell.getText().trim();

	 	                    ExcelUtils.updateExcel(excelpath, sheetname, row, "Result", msg);
	 	                    
	 	                // Only runs if no UI error occurred and msg is populated
	 	                String expected;
	 	                switch (FunCode) {
	 	                    case "A-Add":
	 	                        expected = "Record added successfully,Please verify the Customer Id:" + cif;
	 	                        break;
	 	                    case "M-Modify":
	 	                        expected = "Record Modified successfully for the Customer Id:" + cif;
	 	                        break;
	 	                    case "V-Verify":
	 	                        expected = "Record Verified for the Customer Id:" + cif;
	 	                        break;
	 	                    case "D-Delete":
	 	                        expected = "Record Deleted successfully for the Customer Id:" + cif;
	 	                        break;
	 	                    case "X-Cancel":
	 	                        expected = "Record Cancel successfully for the Customer Id:" + cif;
	 	                        break;
	 	                    default:
	 	                        Assert.fail("Unsupported FunCode: " + FunCode);
	 	                        return;
	 	                }

	 	                Assert.assertEquals(msg, expected,
	 	                    "❌ [" + FunCode + "] Result message mismatch: expected='" + expected + "', but was='" + msg + "'");
	 	            }

	 	       }catch(Exception e)
	 	       {
	 	    	   Assert.fail("❌ Test failed due to Error: " + e.getMessage());
	 	       }
	    }
	    
	    
	    @SuppressWarnings("deprecation")
		public void VerificationValidation (List<String> data) throws ParseException {
	    	
	    	WebElement Medical = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Medical")));
	    	WebElement DisorderDet = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("DisorderDet")));
	    	WebElement Minorflg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Minorflg")));
	    	WebElement Relation = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Relation")));
	    	WebElement NomName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("NomName")));
	    	WebElement DOBMinor = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("DOBMinor_ui")));
	    	
	    	String medical = Medical.getAttribute("value");
			String Disorderdet = DisorderDet.getAttribute("value");	    	
			String MinorFlg = Minorflg.getAttribute("value");
	    	String RelationType = Relation.getAttribute("value");
	    	String NomiName = NomName.getAttribute("value");
	    	String DOBminor = DOBMinor.getAttribute("value");
	    	
	    	Map<String, String> AppData = new HashMap<>();
	    	AppData.put("Medical", medical);
	    	AppData.put("Disorderdet", Disorderdet);
	    	AppData.put("MinorFlg", MinorFlg);
	    	AppData.put("RelationType", RelationType);
	    	AppData.put("NomiName", NomiName);
	    	AppData.put("DOBminor", DOBminor);
	    	
	    	Map<String, String> ExceptedData = new HashMap<>();
	    	ExceptedData.put("Medical", data.get(5));
	    	ExceptedData.put("Disorderdet", data.get(6));
	    	ExceptedData.put("MinorFlg", data.get(7));
	    	ExceptedData.put("RelationType", data.get(8));
	    	ExceptedData.put("NomiName", data.get(9));
            String Dob = data.get(11);
            String formatted = DateUtils.toDDMMYYYY(Dob);
            
	    	ExceptedData.put("DOBminor", formatted);
	    	
	    	Map<String, String> normExpected = normalize(ExceptedData);
	    	Map<String, String> normActual = normalize(AppData);
            
	    	  // Count matching entries
	        long matchCount = normActual.entrySet().stream()
	            .filter(e -> {
	                String expected = normExpected.get(e.getKey());
	                return e.getValue().equals(expected);
	            })
	            .count();

	        // Count mismatches or missing entries
	        Map<String, String> mismatches = normActual.entrySet().stream()
	            .filter(e -> !e.getValue().equals(normExpected.get(e.getKey())))
	            .collect(Collectors.toMap(
	                Map.Entry::getKey,
	                e -> {
	                    String expected = normExpected.get(e.getKey());
	                    return "Actual='" + e.getValue() + "' Expected='" + expected + "'";
	                }
	            ));

	        int total = normActual.size();
	        int failCount = mismatches.size();
	        
	        
	        System.out.printf("✅ Total fields: %d, Matches: %d, Failures: %d%n", total, matchCount, failCount);

	        if (!mismatches.isEmpty()) {
	            System.out.println("❌ Mismatch details:");
	            mismatches.forEach((field, detail) ->
	                System.out.printf(" • %s: %s%n", field, detail)
	            );
	           // Assert.fail("Field verification failed: " + failCount + " mismatch(es).");
	        }
	    }
	    
	    public static Map<String, String> normalize(Map<String, String> input) {
	        return input.entrySet().stream()
	            .collect(Collectors.toMap(
	                Map.Entry::getKey,
	                e -> e.getValue() == null
	                       ? null
	                       : e.getValue().trim().toLowerCase()
	            ));
	    }
	   }

