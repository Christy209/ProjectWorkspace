package Reports;

import java.io.File;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Annotation.ExcelData;
import Base.BaseTest;
import Base.DriverManager;
import Utilities.Dataproviders;
import Utilities.RowData;
import Utilities.WindowHandle;
import Utilities.WindowHandle2;
import Utilities.WordReportUtil;

public class HPR extends BaseTest {

    protected static WebDriver driver;
    protected static WebDriverWait wait;

    @BeforeClass
    public void setup() throws IOException {
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        String userID = DriverManager.getProperty("userid");
        String password = DriverManager.getProperty("password");

        DriverManager.login(userID, password);
        System.out.println("✅ Logged in as: " + userID);
    }

    @Test(dataProvider = "Testcase", dataProviderClass = Dataproviders.class)
    @ExcelData(sheetName = "Sheet3", rowIndex = {-1})
    public void runDLYRPT_HPR(RowData inputData) throws Exception {
        String menuValue = inputData.getByIndex(2);   // "DLYRPT"
        String reportName = inputData.getByIndex(3);  // Report Name

        try {
            System.out.println("▶ Starting DLYRPT for: " + reportName);
            boolean dlyrptSuccess = executeDLYRPT(menuValue, reportName, inputData);

            if (dlyrptSuccess) {
                System.out.println("✅ Report executed: " + reportName);
                System.out.println("➡ Moving to HPR now...");

                boolean hprSuccess = executeHPR(inputData);
                if (hprSuccess) {
                    System.out.println("✅ HPR execution successful");
                } else {
                    System.out.println("❌ HPR execution failed");
                }
            } else {
                System.out.println("⚠ DLYRPT did not return success → Skipping HPR");
            }

        } catch (Exception e) {
            System.out.println("❌ DLYRPT Failed for report " + reportName + " : " + e.getMessage());
            throw e; 
        }
    }

    //----------------------------DLYRPT------------------------------------------//
    
    private boolean executeDLYRPT(String menuValue, String reportName, RowData inputData) throws Exception {
        goToMenu(menuValue);
        handleUnexpectedAlertIfAny();

     
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("CoreServer"));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));

        System.out.println("▶ Filling DLYRPT fields for report: " + reportName);
        
        WebElement reportField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ModuleName")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + reportName + "';", reportField);
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"input[value='Go']\").click();");

        if (reportName.equalsIgnoreCase("ACCBAL")) {
            try {
            	
            	String schmTypeValue = inputData.getByIndex(4);
	            WebElement schmTypeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmType")));
	            String jsschm = "var el = arguments[0];" +
	                    "el.value='" + schmTypeValue + "';" +
	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
	            ((JavascriptExecutor) driver).executeScript(jsschm, schmTypeField);

	        
            } catch (Exception e) {
                System.out.println("⚠ Could not set SchmType for ACCBAL: " + e.getMessage());
        }
        }
        
        if (reportName.equalsIgnoreCase("ACCLIST")) {
            try {
            	String schmTypeValue = inputData.getByIndex(4);
	            WebElement schmTypeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmType")));
	            String jsschm = "var el = arguments[0];" +
	                    "el.value='" + schmTypeValue + "';" +
	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
	            ((JavascriptExecutor) driver).executeScript(jsschm, schmTypeField);
	            
	            String fromGLSubCode = inputData.getByIndex(5);
	            WebElement fromGLSubCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("fromGLSubCode")));
	            String jssubCode = "var el = arguments[0];" +
	                    "el.value='" + fromGLSubCode + "';" +
	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
	            ((JavascriptExecutor) driver).executeScript(jssubCode, fromGLSubCodeField);
	            
	            String toGLSubCode = inputData.getByIndex(6);
	            WebElement toGLSubCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("toGLSubCode")));
	            String jssubCode2 = "var el = arguments[0];" +
	                    "el.value='" + toGLSubCode + "';" +
	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
	            ((JavascriptExecutor) driver).executeScript(jssubCode2, toGLSubCodeField);
	            
      } catch (Exception e) {
                System.out.println("⚠ Could not set SchmType for ACCLIST: " + e.getMessage());
               }
        }

        if (reportName.equalsIgnoreCase("ACCTREG")) {
            try {
            	String schmTypeValue = inputData.getByIndex(4);
	            WebElement schmTypeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmType")));
	            String jsschm = "var el = arguments[0];" +
	                    "el.value='" + schmTypeValue + "';" +
	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
	            ((JavascriptExecutor) driver).executeScript(jsschm, schmTypeField);

	        
            } catch (Exception e) {
                System.out.println("⚠ Could not set SchmType for ACCTREG: " + e.getMessage());
            }
        }

        
        if (reportName.equalsIgnoreCase("BCOUTST")) {
            try {
            	String SchmCode = inputData.getByIndex(7);
	            WebElement SchmCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmCode")));
	            String jsschmCode = "var el = arguments[0];" +
	                    "el.value='" + SchmCode + "';" +
	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
	            ((JavascriptExecutor) driver).executeScript(jsschmCode, SchmCodeField);

	        
            } catch (Exception e) {
                System.out.println("⚠ Could not set SchmType for BCOUSTST: " + e.getMessage());
        }
        }
        
        if (reportName.equalsIgnoreCase("BNKPATTI")) {
            try {
            	String zoneCode = inputData.getByIndex(8);
	            WebElement zoneCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("zoneCode")));
	            String jsschm = "var el = arguments[0];" +
	                    "el.value='" + zoneCode + "';" +
	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
	            ((JavascriptExecutor) driver).executeScript(jsschm, zoneCodeField);

	        
            } catch (Exception e) {
                System.out.println("⚠ Could not set SchmType for BNKPATTI: " + e.getMessage());
            }
        }


        
        if (reportName.equalsIgnoreCase("BOOKAMT")) {
            try {
            	String SchmCode = inputData.getByIndex(7);
	            WebElement SchmCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmCode")));
	            String jsschm = "var el = arguments[0];" +
	                    "el.value='" + SchmCode + "';" +
	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
	            ((JavascriptExecutor) driver).executeScript(jsschm, SchmCodeField);

	        
            } catch (Exception e) {
                System.out.println("⚠ Could not set SchmType for BOOKATM: " + e.getMessage());
            }
        }
        
        if (reportName.equalsIgnoreCase("BRMGRRPT")) {
            try {
            	String SchmCode = inputData.getByIndex(7);
	            WebElement SchmCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmCode")));
	            String jsschm = "var el = arguments[0];" +
	                    "el.value='" + SchmCode + "';" +
	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
	            ((JavascriptExecutor) driver).executeScript(jsschm, SchmCodeField);

	        
            } catch (Exception e) {
                System.out.println("⚠ Could not set SchmType for BRMGRRPT: " + e.getMessage());
          }
        }
        
        if (reportName.equalsIgnoreCase("BRPATTI")) {
            try {
            	String zoneCode = inputData.getByIndex(8);
	            WebElement zoneCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("zoneCode")));
	            String jsschm = "var el = arguments[0];" +
	                    "el.value='" + zoneCode + "';" +
	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
	            ((JavascriptExecutor) driver).executeScript(jsschm, zoneCodeField);

	        
            } catch (Exception e) {
                System.out.println("⚠ Could not set SchmType for BRPATTI: " + e.getMessage());
        }
        }
        
        if (reportName.equalsIgnoreCase("CASASCH")) {
            try {
            	
	            String fromGLSubCode = inputData.getByIndex(5);
	            WebElement fromGLSubCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("fromGLSubCode")));
	            String jssubCode = "var el = arguments[0];" +
	                    "el.value='" + fromGLSubCode + "';" +
	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
	            ((JavascriptExecutor) driver).executeScript(jssubCode, fromGLSubCodeField);
	            
	            String toGLSubCode = inputData.getByIndex(6);
	            WebElement toGLSubCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("toGLSubCode")));
	            String jssubCode2 = "var el = arguments[0];" +
	                    "el.value='" + toGLSubCode + "';" +
	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
	            ((JavascriptExecutor) driver).executeScript(jssubCode2, toGLSubCodeField);
	            
      } catch (Exception e) {
                System.out.println("⚠ Could not set SchmType for CASASCH: " + e.getMessage());       
                }
        }
    
        if (reportName.equalsIgnoreCase("CCAD")) {
            try {
            	
            	  String fromGLSubCode = inputData.getByIndex(5);
                    WebElement fromGLSubCodeField = wait.until(
                            ExpectedConditions.presenceOfElementLocated(By.id("fromGLSubCode")));

                    // Clear existing value first
                    ((JavascriptExecutor) driver).executeScript("arguments[0].value='';", fromGLSubCodeField);
                    String jsFrom = "var el = arguments[0];" +
                                    "el.value='" + fromGLSubCode + "';" +
                                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    ((JavascriptExecutor) driver).executeScript(jsFrom, fromGLSubCodeField);

                    String toGLSubCode = inputData.getByIndex(6);
                    WebElement toGLSubCodeField = wait.until(
                            ExpectedConditions.presenceOfElementLocated(By.id("toGLSubCode")));

                    ((JavascriptExecutor) driver).executeScript("arguments[0].value='';", toGLSubCodeField);
                    String jsTo = "var el = arguments[0];" +
                                  "el.value='" + toGLSubCode + "';" +
                                  "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                                  "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    ((JavascriptExecutor) driver).executeScript(jsTo, toGLSubCodeField);

	            
      } catch (Exception e) {
                System.out.println("⚠ Could not set SchmType for CCAD: " + e.getMessage());
        }
        }

        if (reportName.equalsIgnoreCase("CHQ_PAY")) {
            try {
            	String SchmCode = inputData.getByIndex(7);
	            WebElement SchmCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmCode")));
	            String jsschm = "var el = arguments[0];" +
	                    "el.value='" + SchmCode + "';" +
	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
	            ((JavascriptExecutor) driver).executeScript(jsschm, SchmCodeField);

	        
            } catch (Exception e) {
                System.out.println("⚠ Could not set SchmType for CHQ_PAY: " + e.getMessage());
           }
        }
        
        if (reportName.equalsIgnoreCase("DEMREC")) {
            try {
            	String SchmCode = inputData.getByIndex(7);
	            WebElement SchmCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmCode")));
	            String jsschm = "var el = arguments[0];" +
	                    "el.value='" + SchmCode + "';" +
	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
	            ((JavascriptExecutor) driver).executeScript(jsschm, SchmCodeField);

	        
            } catch (Exception e) {
                System.out.println("⚠ Could not set SchmType for DEMREC: " + e.getMessage());           
                }
        }
        
        if (reportName.equalsIgnoreCase("DEMRECCO")) {
            try {
            	String SchmCode = inputData.getByIndex(7);
	            WebElement SchmCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmCode")));
	            String jsschm = "var el = arguments[0];" +
	                    "el.value='" + SchmCode + "';" +
	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
	            ((JavascriptExecutor) driver).executeScript(jsschm, SchmCodeField);

	        
            } catch (Exception e) {
                System.out.println("⚠ Could not set SchmType for DEMRECCO: " + e.getMessage());     
                }
        }
        
        if (reportName.equalsIgnoreCase("DEP_AC_DET")) {
            try {
            	
            	String schmTypeValue = inputData.getByIndex(4);
	            WebElement schmTypeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmType")));
	            String jsschm = "var el = arguments[0];" +
	                    "el.value='" + schmTypeValue + "';" +
	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
	            ((JavascriptExecutor) driver).executeScript(jsschm, schmTypeField);
	            
            	String SchmCode = inputData.getByIndex(7);
	            WebElement SchmCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmCode")));
	            String jsschm1 = "var el = arguments[0];" +
	                    "el.value='" + SchmCode + "';" +
	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
	            ((JavascriptExecutor) driver).executeScript(jsschm1, SchmCodeField);

	        
            } catch (Exception e) {
                System.out.println("⚠ Could not set SchmType for DEP_AC_DET: " + e.getMessage());
           }
        }
        
        if (reportName.equalsIgnoreCase("DICGC_NEW")) {
            try {
            	
            	String schmTypeValue = inputData.getByIndex(4);
	            WebElement schmTypeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmType")));
	            String jsschm = "var el = arguments[0];" +
	                    "el.value='" + schmTypeValue + "';" +
	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
	            ((JavascriptExecutor) driver).executeScript(jsschm, schmTypeField);
	            
            	
            } catch (Exception e) {
                System.out.println("⚠ Could not set SchmType for DICGC_NEW: " + e.getMessage());
       }
        }

        
        
        if (reportName.equalsIgnoreCase("DORACT")) {
            try {
            	
            	String FiureId = inputData.getByIndex(9);
	            WebElement FiureIdField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FiureId")));
	            String jsschm = "var el = arguments[0];" +
	                    "el.value='" + FiureId + "';" +
	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
	            ((JavascriptExecutor) driver).executeScript(jsschm, FiureIdField);
	            
            	
            } catch (Exception e) {
                System.out.println("⚠ Could not set SchmType for DICGC_NEW: " + e.getMessage());
       }
        }
            
            if (reportName.equalsIgnoreCase("DORINACTIVE")) {
                try {
                    String accountStatus = inputData.getByIndex(10); 
                    WebElement statusDropdown = wait.until(
                            ExpectedConditions.presenceOfElementLocated(By.id("AcctStatus"))
                    );
                    Select select = new Select(statusDropdown);
                    select.selectByVisibleText(accountStatus);

                } catch (Exception e) {
                    System.out.println("⚠ Could not select Account Status for DORINACTIVE: " + e.getMessage());
                    }
            }
            
            if (reportName.equalsIgnoreCase("DORTRN")) {
                try {
                	
                	String FiureId = inputData.getByIndex(9);
    	            WebElement FiureIdField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FiureId")));
    	            String jsschm = "var el = arguments[0];" +
    	                    "el.value='" + FiureId + "';" +
    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
    	            ((JavascriptExecutor) driver).executeScript(jsschm, FiureIdField);
    	            
                	
                } catch (Exception e) {
                    System.out.println("⚠ Could not set SchmType for DORTRN: " + e.getMessage());
                }
            }
                
                if (reportName.equalsIgnoreCase("EXCASH")) {
                    try {
                    	
                    	String FiureId = inputData.getByIndex(11);
        	            WebElement FiureIdField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("TranAmt")));
        	            String jsschm = "var el = arguments[0];" +
        	                    "el.value='" + FiureId + "';" +
        	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
        	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
        	            ((JavascriptExecutor) driver).executeScript(jsschm, FiureIdField);
        	            
                    	
                    } catch (Exception e) {
                        System.out.println("⚠ Could not set SchmType for EXCASH: " + e.getMessage());
                   }
                }
                    if (reportName.equalsIgnoreCase("FORM_15G")) {
                        try {
                        	
                        	String cifId = inputData.getByIndex(12);
            	            WebElement cifIdField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cifId")));
            	            String jsschm = "var el = arguments[0];" +
            	                    "el.value='" + cifId + "';" +
            	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
            	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
            	            ((JavascriptExecutor) driver).executeScript(jsschm, cifIdField);
            	            
                        	
                        } catch (Exception e) {
                            System.out.println("⚠ Could not set SchmType for FORM_15G: " + e.getMessage());
                        }
                    }
                        if (reportName.equalsIgnoreCase("FORM_15H")) {
                            try {
                            	
                            	String cifId = inputData.getByIndex(12);
                	            WebElement cifIdField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cifId")));
                	            String jsschm = "var el = arguments[0];" +
                	                    "el.value='" + cifId + "';" +
                	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                	            ((JavascriptExecutor) driver).executeScript(jsschm, cifIdField);
                	            
                            	
                            } catch (Exception e) {
                                System.out.println("⚠ Could not set SchmType for FORM_15G: " + e.getMessage());
                           }
                        }
                          
                            if (reportName.equalsIgnoreCase("GENAD")) {
                                try {
                                	String SchmCode = inputData.getByIndex(7);
                    	            WebElement SchmCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmCode")));
                    	            String jsschm = "var el = arguments[0];" +
                    	                    "el.value='" + SchmCode + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm, SchmCodeField);

                    	        
                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for GENAD: " + e.getMessage());
                               }
                            }
                
                            if (reportName.equalsIgnoreCase("GENAD_OFCAC")) {
                                try {
                                 String fromGLSubCode = inputData.getByIndex(5);
                    	            WebElement fromGLSubCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("fromGLSubCode")));
                    	            String jssubCode = "var el = arguments[0];" +
                    	                    "el.value='" + fromGLSubCode + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jssubCode, fromGLSubCodeField);
                    	            
                    	            String toGLSubCode = inputData.getByIndex(6);
                    	            WebElement toGLSubCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("toGLSubCode")));
                    	            String jssubCode2 = "var el = arguments[0];" +
                    	                    "el.value='" + toGLSubCode + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jssubCode2, toGLSubCodeField);
                    	            
                          } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for GENAD_OFCAC: " + e.getMessage());                                    WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
                                }
                            }
                            
                            if (reportName.equalsIgnoreCase("GL STAT")) {
                                try {
                                 String fromGLSubCode = inputData.getByIndex(5);
                    	            WebElement fromGLSubCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("fromGLSubCode")));
                    	            String jssubCode = "var el = arguments[0];" +
                    	                    "el.value='" + fromGLSubCode + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jssubCode, fromGLSubCodeField);
                    	            
                    	            String toGLSubCode = inputData.getByIndex(6);
                    	            WebElement toGLSubCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("toGLSubCode")));
                    	            String jssubCode2 = "var el = arguments[0];" +
                    	                    "el.value='" + toGLSubCode + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jssubCode2, toGLSubCodeField);
                    	            
                          } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for GL STAT: " + e.getMessage());
                          }
                            }
                            
                            if (reportName.equalsIgnoreCase("INTFAIL")) {
                                try {
                                	
                                	String schmTypeValue = inputData.getByIndex(4);
                    	            WebElement schmTypeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmType")));
                    	            String jsschm = "var el = arguments[0];" +
                    	                    "el.value='" + schmTypeValue + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm, schmTypeField);
                    	            
                                	
                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for INTFAIL: " + e.getMessage());
                                }
                            }
                            
                            
                            if (reportName.equalsIgnoreCase("JDREP1")) {
                                try {
                                	String SchmCode = inputData.getByIndex(7);
                    	            WebElement SchmCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmCode")));
                    	            String jsschm = "var el = arguments[0];" +
                    	                    "el.value='" + SchmCode + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm, SchmCodeField);
                    	            
                    	            String FiureId = inputData.getByIndex(9);
                    	            WebElement FiureIdField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FiureId")));
                    	            String jsschm1 = "var el = arguments[0];" +
                    	                    "el.value='" + FiureId + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm1, FiureIdField);
                    	            
                    	            
	                                } catch (Exception e) {
	                                    System.out.println("⚠ Could not set SchmType for GENAD: " + e.getMessage());
	                }
	                            }
                            if (reportName.equalsIgnoreCase("JDREP2")) {
                                try {
                                	String SchmCode = inputData.getByIndex(7);
                    	            WebElement SchmCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmCode")));
                    	            String jsschm = "var el = arguments[0];" +
                    	                    "el.value='" + SchmCode + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm, SchmCodeField);
                    	            
                    	            String FiureId = inputData.getByIndex(9);
                    	            WebElement FiureIdField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FiureId")));
                    	            String jsschm1 = "var el = arguments[0];" +
                    	                    "el.value='" + FiureId + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm1, FiureIdField);
                    	            
                    	            
                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for GENAD: " + e.getMessage());
                            }
                            }
                            
                            if (reportName.equalsIgnoreCase("JDREP3")) {
                                try {
                    	            
                    	            String FiureId = inputData.getByIndex(9);
                    	            WebElement FiureIdField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FiureId")));
                    	            String jsschm1 = "var el = arguments[0];" +
                    	                    "el.value='" + FiureId + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm1, FiureIdField);
                    	            
                    	            
                    	            String FiureId1 = inputData.getByIndex(11);
                    	            WebElement FiureIdField1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("TranAmt")));
                    	            String jsschm = "var el = arguments[0];" +
                    	                    "el.value='" + FiureId1 + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm, FiureIdField1);
                    	            
                    	        
                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for GENAD: " + e.getMessage());
                          }
                            }
                            
                            if (reportName.equalsIgnoreCase("JDREP5")) {
                                try {
                    	            
                                	String schmTypeValue = inputData.getByIndex(4);
                    	            WebElement schmTypeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmType")));
                    	            String jsschm = "var el = arguments[0];" +
                    	                    "el.value='" + schmTypeValue + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm, schmTypeField);
                    	            
                                	
                    	            
                    	            String FiureId = inputData.getByIndex(9);
                    	            WebElement FiureIdField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FiureId")));
                    	            String jsschm1 = "var el = arguments[0];" +
                    	                    "el.value='" + FiureId + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm1, FiureIdField);
                    	            
                    	            
                    	            String FiureId1 = inputData.getByIndex(11);
                    	            WebElement FiureIdField1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("TranAmt")));
                    	            String jssch = "var el = arguments[0];" +
                    	                    "el.value='" + FiureId1 + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jssch, FiureIdField1);
                    	            
                    	        
                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for GENAD: " + e.getMessage());
                             }
                            }
                            
                            if (reportName.equalsIgnoreCase("KCCLIM")) {
                                try {
                                	String SchmCode = inputData.getByIndex(7);
                    	            WebElement SchmCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmCode")));
                    	            String jsschm = "var el = arguments[0];" +
                    	                    "el.value='" + SchmCode + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm, SchmCodeField);
                    	            
                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for KCCLIM: " + e.getMessage());
                              }
                            }
                            
                            if (reportName.equalsIgnoreCase("KCC_DATA")) {
                                try {
                                	String SchmCode = inputData.getByIndex(7);
                    	            WebElement SchmCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmCode")));
                    	            String jsschm = "var el = arguments[0];" +
                    	                    "el.value='" + SchmCode + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm, SchmCodeField);
                    	            
                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for KCC_DATA: " + e.getMessage());
                         }
                            }
                            
                            
                            if (reportName.equalsIgnoreCase("KYC")) {
                                try {
                    	            
                                	String schmTypeValue = inputData.getByIndex(4);
                    	            WebElement schmTypeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmType")));
                    	            String jsschm = "var el = arguments[0];" +
                    	                    "el.value='" + schmTypeValue + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm, schmTypeField);
                    	            
                                	
                    	            
                    	            String cifId = inputData.getByIndex(12);
                    	            WebElement cifIdField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cifId")));
                    	            String jsschm1 = "var el = arguments[0];" +
                    	                    "el.value='" + cifId + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm1, cifIdField);
                    	            
                                	
                    	            
                    	            String cifId1 = inputData.getByIndex(13);
                    	            WebElement cifIdField1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cifId2")));
                    	            String jssc= "var el = arguments[0];" +
                    	                    "el.value='" + cifId1 + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jssc, cifIdField1);
                    	            
                                	
                    	        
                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for GENAD: " + e.getMessage());
                             }
                            }
                            
                            
                            if (reportName.equalsIgnoreCase("KYC_NEW")) {
                                try {
                                	String SchmCode = inputData.getByIndex(14);
                    	            WebElement SchmCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("GlSubHeadCode")));
                    	            String jsschm = "var el = arguments[0];" +
                    	                    "el.value='" + SchmCode + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm, SchmCodeField);
                    	            
                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for KYC_NEW: " + e.getMessage());
                            }
                            }
                            
                            
                            if (reportName.equalsIgnoreCase("LAIND")) {
                                try {
                                	
                                	  String fromGLSubCode = inputData.getByIndex(5);
                	                    WebElement fromGLSubCodeField = wait.until(
                	                            ExpectedConditions.presenceOfElementLocated(By.id("fromGLSubCode")));

                	                    ((JavascriptExecutor) driver).executeScript("arguments[0].value='';", fromGLSubCodeField);
                	                    String jsFrom = "var el = arguments[0];" +
                	                                    "el.value='" + fromGLSubCode + "';" +
                	                                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                	                                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                	                    ((JavascriptExecutor) driver).executeScript(jsFrom, fromGLSubCodeField);

                	                    String toGLSubCode = inputData.getByIndex(6);
                	                    WebElement toGLSubCodeField = wait.until(
                	                            ExpectedConditions.presenceOfElementLocated(By.id("toGLSubCode")));

                	                    ((JavascriptExecutor) driver).executeScript("arguments[0].value='';", toGLSubCodeField);

                	                    String jsTo = "var el = arguments[0];" +
                	                                  "el.value='" + toGLSubCode + "';" +
                	                                  "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                	                                  "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                	                    ((JavascriptExecutor) driver).executeScript(jsTo, toGLSubCodeField);

                    	            
                          } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for LAIND: " + e.getMessage());
                       }
                            }
                            if (reportName.equalsIgnoreCase("LIENDETAIL")) {
                                try {
                                	String SchmCode = inputData.getByIndex(15);
                    	            WebElement SchmCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("chargeType")));
                    	            String jsschm = "var el = arguments[0];" +
                    	                    "el.value='" + SchmCode + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm, SchmCodeField);
                    	            
                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for LIENDETAIL: " + e.getMessage());
                            }
                            }


                            if (reportName.equalsIgnoreCase("MAN_ENT")) {
                                try {
                                	
                                	  String fromGLSubCode = inputData.getByIndex(5);
                	                    WebElement fromGLSubCodeField = wait.until(
                	                            ExpectedConditions.presenceOfElementLocated(By.id("fromGLSubCode")));

                	                    ((JavascriptExecutor) driver).executeScript("arguments[0].value='';", fromGLSubCodeField);

                	                    String jsFrom = "var el = arguments[0];" +
                	                                    "el.value='" + fromGLSubCode + "';" +
                	                                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                	                                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                	                    ((JavascriptExecutor) driver).executeScript(jsFrom, fromGLSubCodeField);

                	                    String toGLSubCode = inputData.getByIndex(6);
                	                    WebElement toGLSubCodeField = wait.until(
                	                            ExpectedConditions.presenceOfElementLocated(By.id("toGLSubCode")));

                	                    ((JavascriptExecutor) driver).executeScript("arguments[0].value='';", toGLSubCodeField);

                	                    String jsTo = "var el = arguments[0];" +
                	                                  "el.value='" + toGLSubCode + "';" +
                	                                  "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                	                                  "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                	                    ((JavascriptExecutor) driver).executeScript(jsTo, toGLSubCodeField);

                    	            
                          } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for MAN_ENT: " + e.getMessage());
                          }
                            }
                            
                            
                            if (reportName.equalsIgnoreCase("MINISTMT")) {
                                try {
                                	String Acctno = inputData.getByIndex(16);
                    	            WebElement SchmCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Acctno")));
                    	            String jsschm = "var el = arguments[0];" +
                    	                    "el.value='" + Acctno + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm, SchmCodeField);
                    	            
                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for MINISTMT: " + e.getMessage());
                           }
                            }
                            
                            if (reportName.equalsIgnoreCase("MTR")) {
                                try {
                                
                                    String accountStatus = inputData.getByIndex(17); 

                                    WebElement statusDropdown = wait.until(
                                            ExpectedConditions.presenceOfElementLocated(By.id("TranType2"))
                                    );

                                    Select select = new Select(statusDropdown);
                                    select.selectByVisibleText(accountStatus);
                                    
                                    String SchmCode = inputData.getByIndex(18);
                    	            WebElement SchmCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("AssetItems")));
                    	            String jsschm = "var el = arguments[0];" +
                    	                    "el.value='" + SchmCode + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm, SchmCodeField);
                    	            
                    	            String SchmCode1 = inputData.getByIndex(19);
                    	            WebElement SchmCodeField1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("chargeType")));
                    	            String jsschm1 = "var el = arguments[0];" +
                    	                    "el.value='" + SchmCode1 + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm1, SchmCodeField1);
                    	            

                                } catch (Exception e) {
                                    System.out.println("⚠ Could not select Account Status for MTR: " + e.getMessage());
                            }
                            }
                            
                            if (reportName.equalsIgnoreCase("NONOP")) {
                                try {
                                	String GlSubHeadCode = inputData.getByIndex(5);
                    	            WebElement SchmCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("GlSubHeadCode")));
                    	            String jsschm = "var el = arguments[0];" +
                    	                    "el.value='" + GlSubHeadCode + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm, SchmCodeField);
                    	            
                    	        	String peroids = inputData.getByIndex(20);
                    	            WebElement peroidsField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("months")));
                    	            String jssschm = "var el = arguments[0];" +
                    	                    "el.value='" + peroids + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jssschm, peroidsField);
                    	            
                    	            
                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for NONOP: " + e.getMessage());
                            }
                            }
                                                
                            if (reportName.equalsIgnoreCase("OCR")) {
                                try {
                                	String zoneCode = inputData.getByIndex(8);
                    	            WebElement zoneCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("zoneCode")));
                    	            String jsschm = "var el = arguments[0];" +
                    	                    "el.value='" + zoneCode + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm, zoneCodeField);

                    	        
                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for OCR: " + e.getMessage());

                                }
                            }
                            if (reportName.equalsIgnoreCase("ODEXPY")) {
                                try {
                                	String SchmCode = inputData.getByIndex(5);
                    	            WebElement SchmCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("GlSubHeadCode")));
                    	            String jsschm = "var el = arguments[0];" +
                    	                    "el.value='" + SchmCode + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm, SchmCodeField);
                    	            
//                    	        	String peroids = inputData.getByIndex(17);
//                    	            WebElement peroidsField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("months")));
//                    	            String jssschm = "var el = arguments[0];" +
//                    	                    "el.value='" + peroids + "';" +
//                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
//                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
//                    	            ((JavascriptExecutor) driver).executeScript(jssschm, peroidsField);
//                    	            
                    	            
                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for ODEXPY: " + e.getMessage());
                                    }
                            }
                            
                            if (reportName.equalsIgnoreCase("OFLVSSB")) {
                                try {
                            String SchmCode = inputData.getByIndex(18);
            	            WebElement SchmCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("AssetItems")));
            	            String jsschm = "var el = arguments[0];" +
            	                    "el.value='" + SchmCode + "';" +
            	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
            	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
            	            ((JavascriptExecutor) driver).executeScript(jsschm, SchmCodeField);
                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for OFLVSSB: " + e.getMessage());
                             }
                            }
                            
                            if (reportName.equalsIgnoreCase("OFL_KCCPROD")) {
                                try {
                                	
                                	  String fromGLSubCode = inputData.getByIndex(5);
                	               WebElement fromGLSubCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("fromGLSubCode")));

                	                    ((JavascriptExecutor) driver).executeScript("arguments[0].value='';", fromGLSubCodeField);

                	                    String jsFrom = "var el = arguments[0];" +
                	                                    "el.value='" + fromGLSubCode + "';" +
                	                                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                	                                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                	                    ((JavascriptExecutor) driver).executeScript(jsFrom, fromGLSubCodeField);

                	                    String toGLSubCode = inputData.getByIndex(6);
                	                    WebElement toGLSubCodeField = wait.until(
                	                            ExpectedConditions.presenceOfElementLocated(By.id("toGLSubCode")));

                	                    ((JavascriptExecutor) driver).executeScript("arguments[0].value='';", toGLSubCodeField);

                	                    String jsTo = "var el = arguments[0];" +
                	                                  "el.value='" + toGLSubCode + "';" +
                	                                  "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                	                                  "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                	                    ((JavascriptExecutor) driver).executeScript(jsTo, toGLSubCodeField);

                    	            
                          } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for OFL_KCCPROD: " + e.getMessage());

                          }
                            }
                            

                            if (reportName.equalsIgnoreCase("OLDNEW")) {
                                try {
                    	            
                                	String schmTypeValue = inputData.getByIndex(4);
                    	            WebElement schmTypeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmType")));
                    	            String jsschm = "var el = arguments[0];" +
                    	                    "el.value='" + schmTypeValue + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm, schmTypeField);
                    	        
                    	        
                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for OLDNEW: " + e.getMessage());
                             }
                            }
                            
                            if (reportName.equalsIgnoreCase("OVR")) {
                                try {
                                	String zoneCode = inputData.getByIndex(8);
                    	            WebElement zoneCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("zoneCode")));
                    	            String jsschm = "var el = arguments[0];" +
                    	                    "el.value='" + zoneCode + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm, zoneCodeField);

                    	        
                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for OVR: " + e.getMessage());
                                }
                            }
                            
                            
                            if (reportName.equalsIgnoreCase("PACSINTRPT")) {
                                try {
                            String SchmCode = inputData.getByIndex(18);
            	            WebElement SchmCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("AssetItems")));
            	            String jsschm = "var el = arguments[0];" +
            	                    "el.value='" + SchmCode + "';" +
            	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
            	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
            	            ((JavascriptExecutor) driver).executeScript(jsschm, SchmCodeField);
                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for PACSINTRPT: " + e.getMessage());
                           }
                            }
                            
                            if (reportName.equalsIgnoreCase("PACS_ACBAL")) {
                                try {
                                	
                                
                                	String SchmCode = inputData.getByIndex(7);
                    	            WebElement SchmCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmCode")));
                    	            String jsschm = "var el = arguments[0];" +
                    	                    "el.value='" + SchmCode + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm, SchmCodeField);
                    	            
                            String SchmCode1 = inputData.getByIndex(18);
            	            WebElement SchmCodeField1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("AssetItems")));
            	            String jsschm1 = "var el = arguments[0];" +
            	                    "el.value='" + SchmCode1 + "';" +
            	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
            	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
            	            ((JavascriptExecutor) driver).executeScript(jsschm1, SchmCodeField1);
                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for PACSINTRPT: " + e.getMessage());
                              }
                            }
                            
                            if (reportName.equalsIgnoreCase("PACS_DBOOK")) {
                                try {
                                	
                                
                                	String SchmCode = inputData.getByIndex(7);
                    	            WebElement SchmCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmCode")));
                    	            String jsschm = "var el = arguments[0];" +
                    	                    "el.value='" + SchmCode + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm, SchmCodeField);
                    	            
                            String SchmCode1 = inputData.getByIndex(18);
            	            WebElement SchmCodeField1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("AssetItems")));
            	            String jsschm1 = "var el = arguments[0];" +
            	                    "el.value='" + SchmCode1 + "';" +
            	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
            	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
            	            ((JavascriptExecutor) driver).executeScript(jsschm1, SchmCodeField1);
                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for PACSINTRPT: " + e.getMessage());
                            }
                            }
                            
                            if (reportName.equalsIgnoreCase("PHBOOK")) {
                                try {
                    	            
                                	String schmTypeValue = inputData.getByIndex(4);
                    	            WebElement schmTypeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmType")));
                    	            String jsschm = "var el = arguments[0];" +
                    	                    "el.value='" + schmTypeValue + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm, schmTypeField);
                    	        
                    	        
                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for PHBOOK: " + e.getMessage());
                             }
                            }
                            
                            if (reportName.equalsIgnoreCase("REP28")) {
                                try {
                    	            
                                	String schmTypeValue = inputData.getByIndex(21);
                    	            WebElement schmTypeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FromAmt")));
                    	            String jsschm = "var el = arguments[0];" +
                    	                    "el.value='" + schmTypeValue + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm, schmTypeField);
                    	        
                    	            String schmTypeValue1 = inputData.getByIndex(22);
                    	            WebElement schmTypeField1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ToAmt")));
                    	            String jsschm1 = "var el = arguments[0];" +
                    	                    "el.value='" + schmTypeValue1 + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm1, schmTypeField1);
                    	            
                    	            String schmTypeValue2 = inputData.getByIndex(4);
                    	            WebElement schmTypeField2 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmType")));
                    	            String jsschm2 = "var el = arguments[0];" +
                    	                    "el.value='" + schmTypeValue2 + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm2, schmTypeField2);
                    	            
                    	            String accountStatus = inputData.getByIndex(17); 

                                    WebElement statusDropdown = wait.until(
                                            ExpectedConditions.presenceOfElementLocated(By.id("TranType"))
                                    );

                                
                                    Select select = new Select(statusDropdown);
                                    select.selectByVisibleText(accountStatus);

                                    System.out.println("✅ Selected Account Status: " + accountStatus);
                    	            
                    	            
                    	            String FiureId = inputData.getByIndex(9);
                    	            WebElement FiureIdField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FiureId")));
                    	            String jsschm3 = "var el = arguments[0];" +
                    	                    "el.value='" + FiureId + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm3, FiureIdField);
                    	            

                    	        
                    	        
                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for REP28: " + e.getMessage());
                             }
                            }
                            

                            if (reportName.equalsIgnoreCase("PACS_RECOVERY")) {
                                try {
                            String accountStatus = inputData.getByIndex(17); 

                            WebElement statusDropdown = wait.until(
                                    ExpectedConditions.presenceOfElementLocated(By.id("TranType3"))
                            );

                            Select select = new Select(statusDropdown);
                            select.selectByVisibleText(accountStatus);

                            System.out.println("✅ Selected Account Status: " + accountStatus);
            	            
                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for REP34A: " + e.getMessage());
                        }
                            }
            	        
                            if (reportName.equalsIgnoreCase("REP34A")) {
                                try {
                    	            
                                	String schmTypeValue = inputData.getByIndex(21);
                    	            WebElement schmTypeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FromAmt")));
                    	            String jsschm = "var el = arguments[0];" +
                    	                    "el.value='" + schmTypeValue + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm, schmTypeField);
                    	        
                    	            String schmTypeValue1 = inputData.getByIndex(22);
                    	            WebElement schmTypeField1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ToAmt")));
                    	            String jsschm1 = "var el = arguments[0];" +
                    	                    "el.value='" + schmTypeValue1 + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm1, schmTypeField1);
                    	            
                    	            String schmTypeValue2 = inputData.getByIndex(4);
                    	            WebElement schmTypeField2 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmType")));
                    	            String jsschm2 = "var el = arguments[0];" +
                    	                    "el.value='" + schmTypeValue2 + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm2, schmTypeField2);
                    	            
                    	         
                    	            
                    	            String FiureId = inputData.getByIndex(9);
                    	            WebElement FiureIdField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FiureId")));
                    	            String jsschm3 = "var el = arguments[0];" +
                    	                    "el.value='" + FiureId + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm3, FiureIdField);
                    	            

                    	        
                    	        
                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for REP34A: " + e.getMessage());
                           }
                            }
                            
                            
                            if (reportName.equalsIgnoreCase("SCHMBAL")) {
                                try {
                                	   String schmTypeValue2 = inputData.getByIndex(4);
                       	            WebElement schmTypeField2 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmType")));
                       	            String jsschm2 = "var el = arguments[0];" +
                       	                    "el.value='" + schmTypeValue2 + "';" +
                       	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                       	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                       	            ((JavascriptExecutor) driver).executeScript(jsschm2, schmTypeField2);

                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for REP34A: " + e.getMessage());
                              }
                            }
                       	       
                            if (reportName.equalsIgnoreCase("SFT")) {
                                try {
                    	            
                                	String schmTypeValue = inputData.getByIndex(21);
                    	            WebElement schmTypeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FromAmt")));
                    	            String jsschm = "var el = arguments[0];" +
                    	                    "el.value='" + schmTypeValue + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm, schmTypeField);
                    	        
                    	            String schmTypeValue1 = inputData.getByIndex(22);
                    	            WebElement schmTypeField1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ToAmt")));
                    	            String jsschm1 = "var el = arguments[0];" +
                    	                    "el.value='" + schmTypeValue1 + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm1, schmTypeField1);
                    	    
                    	            String schmTypeValue2 = inputData.getByIndex(4);
                       	            WebElement schmTypeField2 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmType")));
                       	            String jsschm2 = "var el = arguments[0];" +
                       	                    "el.value='" + schmTypeValue2 + "';" +
                       	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                       	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                       	            ((JavascriptExecutor) driver).executeScript(jsschm2, schmTypeField2);

                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for SFT: " + e.getMessage());
                              }
                            }
                            
                            if (reportName.equalsIgnoreCase("SFT016")) {
                                try {
                                	String schmTypeValue2 = inputData.getByIndex(4);
                       	            WebElement schmTypeField2 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmType")));
                       	            String jsschm2 = "var el = arguments[0];" +
                       	                    "el.value='" + schmTypeValue2 + "';" +
                       	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                       	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                       	            ((JavascriptExecutor) driver).executeScript(jsschm2, schmTypeField2);
                       	            
                       	     	String FiureId = inputData.getByIndex(11);
                	            WebElement FiureIdField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("TranAmt")));
                	            String jsschm = "var el = arguments[0];" +
                	                    "el.value='" + FiureId + "';" +
                	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                	            ((JavascriptExecutor) driver).executeScript(jsschm, FiureIdField);
                	            
                            	

                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for SFT: " + e.getMessage());
                                    
                                }
                            }

                            
                            if (reportName.equalsIgnoreCase("SMSREG")) {
                                try {
			                            String accountStatus = inputData.getByIndex(17); 
			                            WebElement statusDropdown = wait.until(
			                                    ExpectedConditions.presenceOfElementLocated(By.id("DelFlg"))
			                            );
                            
	                            Select select = new Select(statusDropdown);
	                            select.selectByVisibleText(accountStatus);
	                           
	                            String accountStatus1 = inputData.getByIndex(18);
	                            WebElement statusDropdown1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("EntityCreFlg")));
	                                                   
	                            Select select1 = new Select(statusDropdown1);
	                            select1.selectByVisibleText(accountStatus1);
	                          
                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for SMSREG: " + e.getMessage());
                                }                                 
                            }

                            
                            if (reportName.equalsIgnoreCase("ST-ADOUTS")) {
                                try {
                                	
                                	  String fromGLSubCode = inputData.getByIndex(5);
                	                    WebElement fromGLSubCodeField = wait.until(
                	                            ExpectedConditions.presenceOfElementLocated(By.id("fromGLSubCode")));
                	                    ((JavascriptExecutor) driver).executeScript("arguments[0].value='';", fromGLSubCodeField);
                	                    String jsFrom = "var el = arguments[0];" +
                	                                    "el.value='" + fromGLSubCode + "';" +
                	                                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                	                                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                	                    ((JavascriptExecutor) driver).executeScript(jsFrom, fromGLSubCodeField);
                	                   
                	                    String toGLSubCode = inputData.getByIndex(6);
                	                    WebElement toGLSubCodeField = wait.until(
                	                            ExpectedConditions.presenceOfElementLocated(By.id("toGLSubCode")));
                	                    ((JavascriptExecutor) driver).executeScript("arguments[0].value='';", toGLSubCodeField);
                	                    String jsTo = "var el = arguments[0];" +
                	                                  "el.value='" + toGLSubCode + "';" +
                	                                  "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                	                                  "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                	                   
                	                    ((JavascriptExecutor) driver).executeScript(jsTo, toGLSubCodeField);                        	            
                          } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for ST-ADOUTS: " + e.getMessage());
                        }
                            }
                            
                            if (reportName.equalsIgnoreCase("STATECSREJTRAN")) {
                                try {
	                                    String accountStatus = inputData.getByIndex(18); 
	                                    WebElement statusDropdown = wait.until(
	                                            ExpectedConditions.presenceOfElementLocated(By.id("PartTranType"))
	                                    );
	                                    Select select = new Select(statusDropdown);
	                                    select.selectByVisibleText(accountStatus);
                                                                  
                                } catch (Exception e) {
                                    System.out.println(" Could not set SchmType for ST-ADOUTS: " + e.getMessage());
                                
                                }
                            }
                            
                            

                            if (reportName.equalsIgnoreCase("STATECSTRANS")) {
                                try {
                                    String accountStatus = inputData.getByIndex(18); 
                                    WebElement statusDropdown = wait.until(
                                            ExpectedConditions.presenceOfElementLocated(By.id("PartTranType"))
                                    );
                                    
                                    Select select = new Select(statusDropdown);
                                    select.selectByVisibleText(accountStatus);
  
                                    
                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for STATECSTRANS: " + e.getMessage());
                            }
                            }

                            
                            if (reportName.equalsIgnoreCase("STLA")) {
                                try {
                                	
                                
                                	String SchmCode = inputData.getByIndex(7);
                    	            WebElement SchmCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmCode")));
                    	            String jsschm = "var el = arguments[0];" +
                    	                    "el.value='" + SchmCode + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm, SchmCodeField);
                    	            
                    	            String SchmCode1= inputData.getByIndex(8);
                    	            WebElement SchmCodeField1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ToSchmCode")));
                    	            String jsschm1 = "var el = arguments[0];" +
                    	                    "el.value='" + SchmCode1 + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm1, SchmCodeField1);
                    	            
                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for STLA: " + e.getMessage());
                                }
                            }
                            
                            
                            if (reportName.equalsIgnoreCase("SUPP")) {
                                try {
                                	String schmTypeValue2 = inputData.getByIndex(4);
                       	            WebElement schmTypeField2 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmType")));
                       	            String jsschm2 = "var el = arguments[0];" +
                       	                    "el.value='" + schmTypeValue2 + "';" +
                       	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                       	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                       	            ((JavascriptExecutor) driver).executeScript(jsschm2, schmTypeField2);
                       	            
                       	      } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for SUPP: " + e.getMessage());

                                }
                            }

                            
                            if (reportName.equalsIgnoreCase("TDASCHM")) {
                                try {
                                	
                                	  String fromGLSubCode = inputData.getByIndex(5);
                	                    WebElement fromGLSubCodeField = wait.until(
                	                            ExpectedConditions.presenceOfElementLocated(By.id("fromGLSubCode")));
                	                    ((JavascriptExecutor) driver).executeScript("arguments[0].value='';", fromGLSubCodeField);
                	                    String jsFrom = "var el = arguments[0];" +
                	                                    "el.value='" + fromGLSubCode + "';" +
                	                                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                	                                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                	                    ((JavascriptExecutor) driver).executeScript(jsFrom, fromGLSubCodeField);
                	                   
                	                    String toGLSubCode = inputData.getByIndex(6);
                	                    WebElement toGLSubCodeField = wait.until(
                	                            ExpectedConditions.presenceOfElementLocated(By.id("toGLSubCode")));
                	                    ((JavascriptExecutor) driver).executeScript("arguments[0].value='';", toGLSubCodeField);
                	                    String jsTo = "var el = arguments[0];" +
                	                                  "el.value='" + toGLSubCode + "';" +
                	                                  "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                	                                  "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                	                    ((JavascriptExecutor) driver).executeScript(jsTo, toGLSubCodeField);
                   	            
                          } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for TDASCHM: " + e.getMessage());
                              }
                            }
                            
                            
                            
                            if (reportName.equalsIgnoreCase("TDRPT")) {
                                try {
                                	String SchmCode = inputData.getByIndex(5);
                    	            WebElement SchmCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("GlSubHeadCode")));
                    	            String jsschm = "var el = arguments[0];" +
                    	                    "el.value='" + SchmCode + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm, SchmCodeField);
                    	            
                                } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for KYC_NEW: " + e.getMessage());
                             }
                            }
                            
                            
                            if (reportName.equalsIgnoreCase("TDSINT")) {
                                try {
                                	String schmTypeValue2 = inputData.getByIndex(4);
                       	            WebElement schmTypeField2 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmType")));
                       	            String jsschm2 = "var el = arguments[0];" +
                       	                    "el.value='" + schmTypeValue2 + "';" +
                       	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                       	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                       	            ((JavascriptExecutor) driver).executeScript(jsschm2, schmTypeField2);
                       	            

                                	String SchmCode = inputData.getByIndex(7);
                    	            WebElement SchmCodeField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmCode")));
                    	            String jsschm = "var el = arguments[0];" +
                    	                    "el.value='" + SchmCode + "';" +
                    	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	            ((JavascriptExecutor) driver).executeScript(jsschm, SchmCodeField);                         	            
                       	      } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for TDSINT: " + e.getMessage());
                           }
                            }
                            
                            
                            
                            if (reportName.equalsIgnoreCase("TDSINT")) {
                                try {
                                	String schmTypeValue2 = inputData.getByIndex(4);
                       	            WebElement schmTypeField2 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SchmType")));
                       	            String jsschm2 = "var el = arguments[0];" +
                       	                    "el.value='" + schmTypeValue2 + "';" +
                       	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                       	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                       	            ((JavascriptExecutor) driver).executeScript(jsschm2, schmTypeField2);
                       	           
                       	      } catch (Exception e) {
                                    System.out.println("⚠ Could not set SchmType for TDSINT: " + e.getMessage());
                              }
                            }
       
                            
                            //-------------------submit--------------------------------------//
		                            
		        WebElement submitBtn = wait.until(ExpectedConditions.elementToBeClickable(
		                By.xpath("//input[@value='Submit' or @type='submit']")));
		        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitBtn);
		        
        
      //--------------------------------ERROR MESSAGE CHECK--------------------------------//
        
	        List<WebElement> errorMsgs = driver.findElements(By.xpath("//*[contains(text(),'Error') or contains(text(),'FAT')]"));
	        if (!errorMsgs.isEmpty()) {
	            System.out.println("❌ DLYRPT failed for " + reportName + " - Error displayed: " + errorMsgs.get(0).getText());
	            WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "AfterSubmit_Error", "ERROR");
	            return false; 
	        }
	
	    
	        System.out.println("✅ DLYRPT executed successfully for " + reportName);
	        return true;
	    }	
    

    //--------------------------------- HPR FLOW IF DLYRPT SUCCESS----------------------------//
    
    private boolean executeHPR(RowData inputData) {
        String reportName = inputData.getByIndex(3);

        try {
            // ------------------- 1. DLYRPT screenshot -------------------//
        
                new WebDriverWait(driver, Duration.ofSeconds(15))
                        .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//body")));
                WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "AfterSubmit", "SUCCESS");
           

            // ------------------- 2. Navigate to HPR -------------------//
            goToMenu("HPR");
            handleUnexpectedAlertIfAny();

            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("CoreServer"));
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));

            WebElement goButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='Go']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", goButton);

            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table")));
            List<WebElement> rows = driver.findElements(By.xpath("//table//tr"));

            if (rows.isEmpty() || rows.size() <= 1) {
                System.out.println("⚠ No reports found in HPR table");
                return false;
            }

            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            Map<LocalDateTime, WebElement> rowMap = new HashMap<>();

            for (int i = 0; i < rows.size(); i++) {
                try {
                    rows = driver.findElements(By.xpath("//table//tr")); // re-fetch
                    WebElement row = rows.get(i);
                    List<WebElement> cells = row.findElements(By.tagName("td"));

                    if (cells.size() >= 4) {
                        String dateStr = cells.get(3).getText().trim();
                        if (dateStr.isEmpty() || dateStr.contains("User ID") || dateStr.contains("Page")) continue;

                        try {
                            LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
                            rowMap.put(dateTime, row);
                        } catch (Exception e) {
                            System.out.println("⚠️ Skipping unparsable date: " + dateStr);
                        }
                    }
                } catch (StaleElementReferenceException stale) {
                    i--; 
                }
            }

            if (rowMap.isEmpty()) {
                System.out.println("⚠ No valid dates found in HPR table");
                return true;
            }

            LocalDateTime latestDate = Collections.max(rowMap.keySet());
            WebElement latestRow = rowMap.get(latestDate);
            WebElement checkbox = latestRow.findElement(By.xpath(".//td[1]//input[@type='checkbox']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);

            WordReportUtil.captureScreenshotToWord(driver, "PrintQueue", "Latest_Report_Selected", "SUCCESS");

            WebElement printScreenBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='Print Screen']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", printScreenBtn);
            System.out.println("▶ Clicked Print Screen");

            // ------------------- Final Screenshot -------------------//
           boolean popupAppeared = WindowHandle.handleIEDownloadPopup("OPEN");

            if (!popupAppeared) {
                
                WordReportUtil.captureScreenshotToWord(driver, "PrintQueue", "After_PrintScreen_NoPopup", "SUCCESS");
                System.out.println("✅ Screenshot taken after Print Screen (no popup)");

            } else {
                System.out.println("⚠ Popup appeared → checking Excel...");

                Thread.sleep(2000);
                
               WindowHandle2.handleExcelPopups(driver, reportName);
                
                if (WindowHandle2.isExcelRunning()) {
                    
                    File excelShot = WindowHandle2.captureExcelScreenshot(reportName); 

                    if (excelShot != null) {
                        WordReportUtil.insertImageToWord("ExcelReport", "Excel Screenshot", excelShot, "Excel Window Captured");
                    }

                   
                   // WindowHandle2.handleExcelPopups(driver, reportName);
                    WindowHandle2.ensureExcelClosed();
                   
                } else {
                 
                    WordReportUtil.captureScreenshotToWord(driver, "PrintQueue", "After_PrintScreen_NoExcel", "SUCCESS");
                    //System.out.println("⚠ Excel did not open → browser screenshot captured instead");
                }
            }

            // ------------------- Cleanup -------------------
            driver.switchTo().defaultContent();
            driver.switchTo().frame("loginFrame");
            WindowHandle.slowDown(2);

            try {
                Alert alert = wait.until(ExpectedConditions.alertIsPresent());
                System.out.println("ℹ️ Alert text: " + alert.getText());
                alert.accept();
            } catch (TimeoutException te) {
                System.out.println("ℹ️ No alert appeared after HPR flow");
            }

            return true;

        } catch (Exception e) {
            System.out.println("❌ HPR failed: " + e.getMessage());
            return false;
        }
    }
    		//------------------------Menu code-----------------------
    private void goToMenu(String menuValue) throws Exception {
        driver.switchTo().defaultContent();
        driver.switchTo().frame("loginFrame");
        WindowHandle.slowDown(3);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        handleUnexpectedAlertIfAny();

        WebElement searchbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuSelect")));
        String jsSetInput = "var input = arguments[0];" +
                "input.value='" + menuValue + "';" +
                "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); input.dispatchEvent(evt1);" +
                "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); input.dispatchEvent(evt2);" +
                "var evt3 = document.createEvent('HTMLEvents'); evt3.initEvent('blur', true, false); input.dispatchEvent(evt3);";
        ((JavascriptExecutor) driver).executeScript(jsSetInput, searchbox);

        WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);
    }

    //-----------------Unexpected Alert Handling-------------------------//
    private void handleUnexpectedAlertIfAny() {
        try {
            WebDriverWait waitAlert = new WebDriverWait(driver, Duration.ofSeconds(2));
            waitAlert.until(ExpectedConditions.alertIsPresent());

            Alert alert = driver.switchTo().alert();
            String msg = alert.getText();
            System.out.println("⚠ Unexpected alert found: " + msg);

            alert.accept(); 
        } catch (NoAlertPresentException e) {
            
        } catch (Exception e) {
            
        }
    }
}
