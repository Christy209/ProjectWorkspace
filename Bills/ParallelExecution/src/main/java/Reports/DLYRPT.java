package Reports;
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

public class DLYRPT extends BaseTest {

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

    @Test(dataProvider = "UnifiedData", dataProviderClass = Dataproviders.class)
    @ExcelData(sheetName = "Sheet5", rowIndex = {-1})
    public void runDLYRPT(RowData inputData) throws Exception {
    	
    
        try {
            String menuValue = inputData.getByIndex(2);

            // ✅ Select menu
            WebElement searchbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuSelect")));
            String jsSetInput = "var input = arguments[0];" +
                    "input.value='" + menuValue + "';" +
                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); input.dispatchEvent(evt1);" +
                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); input.dispatchEvent(evt2);" +
                    "var evt3 = document.createEvent('HTMLEvents'); evt3.initEvent('blur', true, false); input.dispatchEvent(evt3);";
            ((JavascriptExecutor) driver).executeScript(jsSetInput, searchbox);

            WebElement searchButton = driver.findElement(By.id("menuSearcherGo"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

            // ✅ Switch to frames
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("CoreServer"));
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));

            String moduleName = inputData.getByIndex(3);
            String reportName = inputData.getByIndex(3);

            WebElement reportField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ModuleName")));
            String jsReport = "var el = arguments[0];" +
                    "el.value='" + moduleName + "';" +
                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
            ((JavascriptExecutor) driver).executeScript(jsReport, reportField);

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
//                    WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                    WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
//                }
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
//                    WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                    WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                    WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                    WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                    WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                    WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
//                }
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
//                    WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                    WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                    WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                    WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                    WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                    WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                    WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                    WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
//                }
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
//                    WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                    WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
//                }
            }
            }
            if (reportName.equalsIgnoreCase("CCAD")) {
                try {
                	
                	  String fromGLSubCode = inputData.getByIndex(5);
	                    WebElement fromGLSubCodeField = wait.until(
	                            ExpectedConditions.presenceOfElementLocated(By.id("fromGLSubCode")));

	                    // Clear existing value first
	                    ((JavascriptExecutor) driver).executeScript("arguments[0].value='';", fromGLSubCodeField);

	                    // Now set value and trigger events
	                    String jsFrom = "var el = arguments[0];" +
	                                    "el.value='" + fromGLSubCode + "';" +
	                                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
	                                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
	                    ((JavascriptExecutor) driver).executeScript(jsFrom, fromGLSubCodeField);

	                    // ---- TO GL SUB HEAD CODE ----
	                    String toGLSubCode = inputData.getByIndex(6);
	                    WebElement toGLSubCodeField = wait.until(
	                            ExpectedConditions.presenceOfElementLocated(By.id("toGLSubCode")));

	                    // Clear existing value first
	                    ((JavascriptExecutor) driver).executeScript("arguments[0].value='';", toGLSubCodeField);

	                    // Now set value and trigger events
	                    String jsTo = "var el = arguments[0];" +
	                                  "el.value='" + toGLSubCode + "';" +
	                                  "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
	                                  "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
	                    ((JavascriptExecutor) driver).executeScript(jsTo, toGLSubCodeField);

    	            
          } catch (Exception e) {
                    System.out.println("⚠ Could not set SchmType for CCAD: " + e.getMessage());
//                    WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                    WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
//                }
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
//                    WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                    WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                    WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                    WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
                    System.out.println("⚠ Could not set SchmType for DEMRECC0: " + e.getMessage());
//                    WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                    WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
//                }
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
//                    WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                    WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                    WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                    WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
//                }
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
//                    WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                    WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
//                }
            }
            }
                
                if (reportName.equalsIgnoreCase("DORINACTIVE")) {
                    try {
                        // Read value from Excel
                        String accountStatus = inputData.getByIndex(10); // e.g., "A-Active" / "I-Inactive" / "D-Dormant"

                        // Wait until dropdown is present
                        WebElement statusDropdown = wait.until(
                                ExpectedConditions.presenceOfElementLocated(By.id("AcctStatus"))
                        );

                        // Clear first (optional, not needed for <select> but safe)
                        // new Select(statusDropdown).deselectAll(); // only works for multi-select

                        // Select by visible text
                        Select select = new Select(statusDropdown);
                        select.selectByVisibleText(accountStatus);

                        System.out.println("✅ Selected Account Status: " + accountStatus);

                    } catch (Exception e) {
                        System.out.println("⚠ Could not select Account Status for DORINACTIVE: " + e.getMessage());
//                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Unable to select Account Status", "ERROR");
//                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "AccountStatus_Select_Error", "ERROR");
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
//                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                            WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                            WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                                WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                                    WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                    WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
                                        System.out.println("⚠ Could not set SchmType for GENAD_OFCAC: " + e.getMessage());
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
//                                    }
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
//                                    }
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
//                                    }
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
                                    }
                                }
                                
                                
                                if (reportName.equalsIgnoreCase("LAIND")) {
                                    try {
                                    	
                                    	  String fromGLSubCode = inputData.getByIndex(5);
                    	                    WebElement fromGLSubCodeField = wait.until(
                    	                            ExpectedConditions.presenceOfElementLocated(By.id("fromGLSubCode")));

                    	                    // Clear existing value first
                    	                    ((JavascriptExecutor) driver).executeScript("arguments[0].value='';", fromGLSubCodeField);

                    	                    // Now set value and trigger events
                    	                    String jsFrom = "var el = arguments[0];" +
                    	                                    "el.value='" + fromGLSubCode + "';" +
                    	                                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	                    ((JavascriptExecutor) driver).executeScript(jsFrom, fromGLSubCodeField);

                    	                    // ---- TO GL SUB HEAD CODE ----
                    	                    String toGLSubCode = inputData.getByIndex(6);
                    	                    WebElement toGLSubCodeField = wait.until(
                    	                            ExpectedConditions.presenceOfElementLocated(By.id("toGLSubCode")));

                    	                    // Clear existing value first
                    	                    ((JavascriptExecutor) driver).executeScript("arguments[0].value='';", toGLSubCodeField);

                    	                    // Now set value and trigger events
                    	                    String jsTo = "var el = arguments[0];" +
                    	                                  "el.value='" + toGLSubCode + "';" +
                    	                                  "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                                  "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	                    ((JavascriptExecutor) driver).executeScript(jsTo, toGLSubCodeField);

                        	            
                              } catch (Exception e) {
                                        System.out.println("⚠ Could not set SchmType for LAIND: " + e.getMessage());
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
//                                    }
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
                                    }
                                }


                                if (reportName.equalsIgnoreCase("MAN_ENT")) {
                                    try {
                                    	
                                    	  String fromGLSubCode = inputData.getByIndex(5);
                    	                    WebElement fromGLSubCodeField = wait.until(
                    	                            ExpectedConditions.presenceOfElementLocated(By.id("fromGLSubCode")));

                    	                    // Clear existing value first
                    	                    ((JavascriptExecutor) driver).executeScript("arguments[0].value='';", fromGLSubCodeField);

                    	                    // Now set value and trigger events
                    	                    String jsFrom = "var el = arguments[0];" +
                    	                                    "el.value='" + fromGLSubCode + "';" +
                    	                                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	                    ((JavascriptExecutor) driver).executeScript(jsFrom, fromGLSubCodeField);

                    	                    // ---- TO GL SUB HEAD CODE ----
                    	                    String toGLSubCode = inputData.getByIndex(6);
                    	                    WebElement toGLSubCodeField = wait.until(
                    	                            ExpectedConditions.presenceOfElementLocated(By.id("toGLSubCode")));

                    	                    // Clear existing value first
                    	                    ((JavascriptExecutor) driver).executeScript("arguments[0].value='';", toGLSubCodeField);

                    	                    // Now set value and trigger events
                    	                    String jsTo = "var el = arguments[0];" +
                    	                                  "el.value='" + toGLSubCode + "';" +
                    	                                  "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                                  "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	                    ((JavascriptExecutor) driver).executeScript(jsTo, toGLSubCodeField);

                        	            
                              } catch (Exception e) {
                                        System.out.println("⚠ Could not set SchmType for MAN_ENT: " + e.getMessage());
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
                                    }
                                }
                                
                                if (reportName.equalsIgnoreCase("MTR")) {
                                    try {
                                        // Read value from Excel
                                        String accountStatus = inputData.getByIndex(17); // e.g., "A-Active" / "I-Inactive" / "D-Dormant"

                                        // Wait until dropdown is present
                                        WebElement statusDropdown = wait.until(
                                                ExpectedConditions.presenceOfElementLocated(By.id("TranType2"))
                                        );

                                        // Clear first (optional, not needed for <select> but safe)
                                        // new Select(statusDropdown).deselectAll(); // only works for multi-select

                                        // Select by visible text
                                        Select select = new Select(statusDropdown);
                                        select.selectByVisibleText(accountStatus);

                                        System.out.println("✅ Selected Account Status: " + accountStatus);
                                        
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Unable to select Account Status", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "AccountStatus_Select_Error", "ERROR");
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
                                    }
                                }
                                
//                                if(reportName.equalsIgnoreCase("NPA_SMA"))
//                                	try {
//                                		  
//                              	      WebElement expiryDate_ui = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("expiryDate_ui")));
//                              	      js.executeScript(setValueScript, expiryDate_ui, inputData.getByIndex(44));
//                              	      
//                                	}
//                                	}
                                
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
                        	            
//                        	        	String peroids = inputData.getByIndex(17);
//                        	            WebElement peroidsField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("months")));
//                        	            String jssschm = "var el = arguments[0];" +
//                        	                    "el.value='" + peroids + "';" +
//                        	                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
//                        	                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
//                        	            ((JavascriptExecutor) driver).executeScript(jssschm, peroidsField);
//                        	            
                        	            
                                    } catch (Exception e) {
                                        System.out.println("⚠ Could not set SchmType for ODEXPY: " + e.getMessage());
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
                                    }
                                }
                                
                                if (reportName.equalsIgnoreCase("OFL_KCCPROD")) {
                                    try {
                                    	
                                    	  String fromGLSubCode = inputData.getByIndex(5);
                    	                    WebElement fromGLSubCodeField = wait.until(
                    	                            ExpectedConditions.presenceOfElementLocated(By.id("fromGLSubCode")));

                    	                    // Clear existing value first
                    	                    ((JavascriptExecutor) driver).executeScript("arguments[0].value='';", fromGLSubCodeField);

                    	                    // Now set value and trigger events
                    	                    String jsFrom = "var el = arguments[0];" +
                    	                                    "el.value='" + fromGLSubCode + "';" +
                    	                                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	                    ((JavascriptExecutor) driver).executeScript(jsFrom, fromGLSubCodeField);

                    	                    // ---- TO GL SUB HEAD CODE ----
                    	                    String toGLSubCode = inputData.getByIndex(6);
                    	                    WebElement toGLSubCodeField = wait.until(
                    	                            ExpectedConditions.presenceOfElementLocated(By.id("toGLSubCode")));

                    	                    // Clear existing value first
                    	                    ((JavascriptExecutor) driver).executeScript("arguments[0].value='';", toGLSubCodeField);

                    	                    // Now set value and trigger events
                    	                    String jsTo = "var el = arguments[0];" +
                    	                                  "el.value='" + toGLSubCode + "';" +
                    	                                  "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); el.dispatchEvent(evt1);" +
                    	                                  "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); el.dispatchEvent(evt2);";
                    	                    ((JavascriptExecutor) driver).executeScript(jsTo, toGLSubCodeField);

                        	            
                              } catch (Exception e) {
                                        System.out.println("⚠ Could not set SchmType for OFL_KCCPROD: " + e.getMessage());
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
//                                    }
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
                        	            
                        	            String accountStatus = inputData.getByIndex(17); // e.g., "A-Active" / "I-Inactive" / "D-Dormant"

                                        // Wait until dropdown is present
                                        WebElement statusDropdown = wait.until(
                                                ExpectedConditions.presenceOfElementLocated(By.id("TranType"))
                                        );

                                        // Clear first (optional, not needed for <select> but safe)
                                        // new Select(statusDropdown).deselectAll(); // only works for multi-select

                                        // Select by visible text
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
                                    }
                                }
                                

                                if (reportName.equalsIgnoreCase("PACS_RECOVERY")) {
                                    try {
                                String accountStatus = inputData.getByIndex(17); // e.g., "A-Active" / "I-Inactive" / "D-Dormant"

                                // Wait until dropdown is present
                                WebElement statusDropdown = wait.until(
                                        ExpectedConditions.presenceOfElementLocated(By.id("TranType3"))
                                );

                                // Clear first (optional, not needed for <select> but safe)
                                // new Select(statusDropdown).deselectAll(); // only works for multi-select

                                // Select by visible text
                                Select select = new Select(statusDropdown);
                                select.selectByVisibleText(accountStatus);

                                System.out.println("✅ Selected Account Status: " + accountStatus);
                	            
                                    } catch (Exception e) {
                                        System.out.println("⚠ Could not set SchmType for REP34A: " + e.getMessage());
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
                                    }
                                }

                                
                                if (reportName.equalsIgnoreCase("SMSREG")) {
                                    try {
                                String accountStatus = inputData.getByIndex(17); 
                                WebElement statusDropdown = wait.until(
                                        ExpectedConditions.presenceOfElementLocated(By.id("DelFlg"))
                                );
                                // new Select(statusDropdown).deselectAll(); // only works for multi-select
                                Select select = new Select(statusDropdown);
                                select.selectByVisibleText(accountStatus);
                                //System.out.println("✅ Selected Account Status: " + accountStatus);
                                String accountStatus1 = inputData.getByIndex(18);
                             WebElement statusDropdown1 = wait.until(
                                     ExpectedConditions.presenceOfElementLocated(By.id("EntityCreFlg"))
                             );                          
                             Select select1 = new Select(statusDropdown1);
                             select1.selectByVisibleText(accountStatus1);
                              //  System.out.println("✅ Selected Account Status: " + accountStatus);
                                    } catch (Exception e) {
                                        System.out.println("⚠ Could not set SchmType for SMSREG: " + e.getMessage());
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
//                                    }
                                }
                                }
                                
                                if (reportName.equalsIgnoreCase("STATECSREJTRAN")) {
                                    try {
                                        String accountStatus = inputData.getByIndex(17); 
                                        WebElement statusDropdown = wait.until(
                                                ExpectedConditions.presenceOfElementLocated(By.id("PartTranType"))
                                        );
                                        // new Select(statusDropdown).deselectAll(); // only works for multi-select
                                        Select select = new Select(statusDropdown);
                                        select.selectByVisibleText(accountStatus);
                                      //  System.out.println("✅ Selected Account Status: " + accountStatus);                                  
                                    } catch (Exception e) {
                                        System.out.println("⚠ Could not set SchmType for ST-ADOUTS: " + e.getMessage());
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
                                    }
                                }
                                
                                

                                if (reportName.equalsIgnoreCase("STATECSTRANS")) {
                                    try {
                                        String accountStatus = inputData.getByIndex(17); 
                                        WebElement statusDropdown = wait.until(
                                                ExpectedConditions.presenceOfElementLocated(By.id("PartTranType"))
                                        );
                                        // new Select(statusDropdown).deselectAll(); // only works for multi-select
                                        Select select = new Select(statusDropdown);
                                        select.selectByVisibleText(accountStatus);
                                      //  System.out.println("✅ Selected Account Status: " + accountStatus);
                                        
                                    } catch (Exception e) {
                                        System.out.println("⚠ Could not set SchmType for STATECSTRANS: " + e.getMessage());
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
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
//                                        WordReportUtil.logMessageToWord("DLYRPT", "Error - Please Enter Schm Type", "ERROR");
//                                        WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "SchmType_Missing", "ERROR");
                                    }
                                }
   
                                // --- Submit report button ---
                                WebElement submitBtn = wait.until(ExpectedConditions.elementToBeClickable(
                                        By.xpath("//input[@value='Submit' or @type='submit']")));
                                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitBtn);

                        
//                                int totalRows = ExcelUtils.getRowCount("DLYRPT"); // ✅ static now
//                                // --- Loop through all rows ---
//                                for (int i = 1; i <= totalRows; i++) {  // assuming row index starts at 1
//                                    inputData = ExcelUtils.getRowAsRowData("DLYRPT", i); // static method
//                                    reportName = inputData.getByHeader("REPORT_NAME");   // read report name
//                                    System.out.println("📄 Running report: " + reportName);}

                                   
                                    // --- Error handling ---
                                    boolean isErrorPresent = false;
                                    String errorText = "";
                                    List<WebElement> possibleErrors = driver.findElements(
                                            By.xpath("//*[contains(text(),'Error') or contains(text(),'Message') or contains(@class,'errormessage')]")
                                    );
                                    for (WebElement el : possibleErrors) {
                                        if (el.isDisplayed()) {
                                            isErrorPresent = true;
                                            errorText = el.getText();
                                            break;
                                        }
                                    }

                                    if (isErrorPresent) {
                                        System.out.println("⚠️ Error found after Submit: " + errorText);
                                        
                                        

                                        // Navigate back to DLYRPT menu with the same report name
                                        WindowHandle.navigateToDlyRptMenu(driver, wait, reportName);
                                       
                                        
                                    } else {
                                        System.out.println("✅ No error found, proceeding with HPR flow...");
                                        // ... your HPR flow here
                                    
                                    
                             
                                
		            // ✅ Capture result page screenshot for SUCCESS
           // runReport(driver, reportName);
            
           // WordReportUtil.captureScreenshotToWord(driver, reportName, "");
             //WordReportUtil.captureScreenshotToWord(driver, moduleName, reportName, "SUCCESS");
            //WordReportUtil.captureScreenshotToWord(driver,moduleName,reportName, "SUCCESS");
          
            // ✅ HPR flow
            driver.switchTo().defaultContent();
            driver.switchTo().frame("loginFrame");
            WindowHandle.slowDown(3);

            WebDriverWait waitShort = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement searchbox1 = waitShort.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuSelect")));
            String jsSetInput1 = "var input = arguments[0];" +
                    "input.value='HPR';" +
                    "var evt1 = document.createEvent('HTMLEvents'); evt1.initEvent('input', true, false); input.dispatchEvent(evt1);" +
                    "var evt2 = document.createEvent('HTMLEvents'); evt2.initEvent('change', true, false); input.dispatchEvent(evt2);" +
                    "var evt3 = document.createEvent('HTMLEvents'); evt3.initEvent('blur', true, false); input.dispatchEvent(evt3);";
            ((JavascriptExecutor) driver).executeScript(jsSetInput1, searchbox1);

            WebElement searchButton1 = driver.findElement(By.id("menuSearcherGo"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton1);

            waitShort.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("CoreServer"));
            waitShort.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("FINW")));

            WebElement goButton = waitShort.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='Go']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", goButton);

            waitShort.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table")));
            List<WebElement> rows = driver.findElements(By.xpath("//table//tr"));

            if (rows.isEmpty() || rows.size() <= 1) {
//                WordReportUtil.logMessageToWord("PrintQueue", "No reports found in HPR table", "ERROR");
//                WordReportUtil.captureScreenshotToWord(driver, "PrintQueue", "No_Report", "ERROR");
            } else {

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

                if (!rowMap.isEmpty()) {
                    LocalDateTime latestDate = Collections.max(rowMap.keySet());
                    WebElement latestRow = rowMap.get(latestDate);
                    WebElement checkbox = latestRow.findElement(By.xpath(".//td[1]//input[@type='checkbox']"));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);                   
                   // WordReportUtil.logMessageToWord("PrintQueue", "Selected latest report: " + latestDate, "SUCCESS");
                   // WordReportUtil.captureScreenshotToWord(driver, "PrintQueue", "Latest_Report_Selected", "SUCCESS");

                    WebElement printScreenBtn = waitShort.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//input[@value='Print Screen']")));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", printScreenBtn);
                    
                   // WordReportUtil.logMessageToWord("PrintQueue", "Print Screen clicked successfully", "SUCCESS");
                   // WordReportUtil.captureScreenshotToWord(driver, "PrintQueue", "After_PrintScreen", "SUCCESS");
                    

                    try {
                        boolean firstPopupHandled = WindowHandle.handleIEDownloadPopup("OPEN");
                        if (firstPopupHandled) {
                            System.out.println("✅ First popup handled");
                            Thread.sleep(400); // small delay before second popup

                            boolean secondPopupHandled = WindowHandle.handleIEDownloadPopup("OPEN");
                            if (secondPopupHandled) {
                                System.out.println("✅ Second popup handled");

                                boolean excelRunning = WindowHandle2.isExcelRunning();
                                System.out.println("Excel running: " + excelRunning);

                                if (excelRunning) {
                                    // ✅ This is the main call to your new WindowHandle2 class
                                   // WindowHandle2.handleExcelPopupsAndClose(driver, reportName);
                                } else {
                                    System.out.println("⚠️ Excel did not open - skipping Excel handling");
                                }
                            } else {
                                System.out.println("⚠️ Second popup did not appear");
                            }
                        } else {
                            System.out.println("⚠️ First popup did not appear");
                        }
                    } catch (Exception e) {
                        System.err.println("❌ Exception occurred:");                       
                        //WordReportUtil.logMessageToWord("PrintQueue", "Error handling popup sequence", "ERROR");
                        e.printStackTrace();
                        driver.switchTo().defaultContent();
                        driver.switchTo().frame("loginFrame");
                        WindowHandle.slowDown(2);

                    } finally {
                        driver.switchTo().defaultContent();
                        driver.switchTo().frame("loginFrame");
                        WindowHandle.slowDown(2);
                        System.out.println("🔄 Switched back to loginFrame after HPR");
                        
                        try {
                            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
                            System.out.println("ℹ️ Popup text: " + alert.getText());
                            alert.accept();
                            System.out.println("✅ Alert handled successfully.");
                        } catch (TimeoutException te) {
                            System.out.println("ℹ️ No alert appeared while navigating to DLYRPT.");
                        }
                        
                    }



                } else {
//                    WordReportUtil.logMessageToWord("PrintQueue", "No valid dates found in HPR table", "ERROR");
//                    WordReportUtil.captureScreenshotToWord(driver, "PrintQueue", "No_Dates_Found", "ERROR");
//                    WordReportUtil.logMessageToWord("PrintQueue", "Report not found in hpr", "ERROR");
                }
            }
            }             
        } catch (Exception e) {
            // If any exception occurs during execution, log as error with screenshot
        	//WordReportUtil.captureScreenshotToWord(driver, "DLYRPT", "Unknown Report", "ERROR");
            e.printStackTrace();
        	 //String reportName = inputData.getByIndex(4);
        	//WordReportUtil.captureResultPage(driver, reportName, true);
        } finally {
            driver.switchTo().defaultContent();
            driver.switchTo().frame("loginFrame");
            WindowHandle.slowDown(2);
            System.out.println("🔄 Switched back to loginFrame after HPR");
            try {
                Alert alert = wait.until(ExpectedConditions.alertIsPresent());
                System.out.println("ℹ️ Popup text: " + alert.getText());
                alert.accept();
                System.out.println("✅ Alert handled successfully.");
            } catch (TimeoutException te) {
                System.out.println("ℹ️ No alert appeared while navigating to DLYRPT.");
            }
            
        }

  
}

}

   