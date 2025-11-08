package TestCases;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Base.DriverManager;
import CASA.BalanceInquire;
import CASA.CashWithdrawal;
import CASA.Cashdeposit;
import CASA.ClosureOfTheAccount;
import CASA.CollectCharges;
import CASA.CreateSavingsAccount;
import CASA.InterestCalculation;
import CASA.TransactionInquire;
import CASA.VerificationOfCashWithdrawal;
import CASA.VerificationOfClosure;
import CASA.VerificationOfSavingsAccount;
import CASA.VerificationofCashDeposit;
import CASA.VerifyCharge;
import Utilities.ExcelUtils;
import Utilities.LogOut;
import Utilities.Login;
import Utilities.RowData;
import Utilities.SharedContext;

public class TC01_CreateSavingsAccountWith5000AmtAndCloseTheAccount {
    
    private WebDriver driver;
    private WebDriverWait  wait;
    
    @BeforeClass
    public void setup() throws Exception {

        driver = DriverManager.getDriver(); 
        wait = new WebDriverWait(driver, Duration.ofSeconds(3)); // initialize driver from DriverManager
    }
    
    @Test
    public void runTest() throws Exception {
        List<String> failures = new ArrayList<>();
        
        try {
            // Setup
            String excelPath = System.getProperty("user.dir") + "/Resource/TestData.xlsx";
            ExcelUtils.loadExcel(excelPath);
            
            Login login = new Login();
            RowData id = ExcelUtils.getRowAsRowData("Sheet1", 1);
            List<String> verifyData = ExcelUtils.getRowAsList("Sheet1", 2);
            List<String> cash = ExcelUtils.getRowAsList("Sheet1", 3);
            RowData tranId = ExcelUtils.getRowAsRowData("Sheet1", 3);
            List<String> bal = ExcelUtils.getRowAsList("Sheet1", 4);
            RowData baln = ExcelUtils.getRowAsRowData("Sheet1", 4);
            RowData error = ExcelUtils.getRowAsRowData("Sheet1", 4);
            List<String> intcal = ExcelUtils.getRowAsList("Sheet1", 5);
            List<String> charg = ExcelUtils.getRowAsList("Sheet1", 6);
            List<String> cw = ExcelUtils.getRowAsList("Sheet1", 7);
            RowData cl = ExcelUtils.getRowAsRowData("Sheet1", 8);
            List<String> clo = ExcelUtils.getRowAsList("Sheet1", 8);
            
            String sheetname = "Sheet1";
        	int balrow = 4;
        	int balcol = 74;
        	int carow = 3;
        	int cwrow = 7;
        	int idrow = 1;
        	
            try {
            	
                login.First();
                int row = 1;
                new CreateSavingsAccount(driver).execute(id,sheetname,row,excelPath);
                
            } catch (Exception e) {
                failures.add("ClosureOfTheAccount failed: " + e.getMessage());
            }
            
            ExcelUtils.loadExcel(excelPath); 
            id = ExcelUtils.getRowAsRowData("Sheet1", 1);
           
            try {
            	LogOut.performLogout(driver, wait);
            	login.Second();
                new VerificationOfSavingsAccount(driver).execute(id, verifyData);
            } catch (Exception e) {
                failures.add("VerificationOfSavingsAccount failed: " + e.getMessage());
            }

            try {
                new Cashdeposit(driver).execute(id,cash,sheetname,carow,excelPath);
            } catch (Exception e) {
                failures.add("Cashdeposit failed: " + e.getMessage());
            }
            ExcelUtils.loadExcel(excelPath); 
            tranId = ExcelUtils.getRowAsRowData("Sheet1", 3);
            
            try {
        	 	LogOut.performLogout(driver, wait);
        	 	login.First();
        	 	new VerificationofCashDeposit(driver).execute(id, tranId, cash, sheetname,idrow,carow,excelPath);
            } catch (Exception e) {
                failures.add("VerificationofCashDeposit failed: " + e.getMessage());
            }

            try {
            	
                new BalanceInquire(driver).execute(id, bal,sheetname,balrow,balcol,excelPath);
            } catch (Exception e) {
                failures.add("BalanceInquire before interest failed: " + e.getMessage());
            }

            try {
                new InterestCalculation(driver).execute(id, intcal);
            } catch (Exception e) {
                failures.add("InterestCalculation failed: " + e.getMessage());
            }

            try {
                new BalanceInquire(driver).execute(id, bal,sheetname,balrow,balcol,excelPath); // after interest
            } catch (Exception e) {
                failures.add("BalanceInquire after interest failed: " + e.getMessage());
            }

            try {
                new CollectCharges(driver).execute(id, charg);
            } catch (Exception e) {
                failures.add("CollectCharges failed: " + e.getMessage());
            }
            
            try {
            	LogOut.performLogout(driver, wait);
           		login.Second();
                new VerifyCharge(driver).execute(id, charg);
            } catch (Exception e) {
                failures.add("VerifyCharge failed: " + e.getMessage());
            }

            try {
                new BalanceInquire(driver).execute(id, bal,sheetname,balrow,balcol,excelPath);
            } catch (Exception e) {
                failures.add("BalanceInquire after charge failed: " + e.getMessage());
            }

            try {
                new CashWithdrawal(driver).execute(id,baln,error,cw,sheetname,cwrow,excelPath);
            } catch (Exception e) {
                failures.add("CashWithdrawal failed: " + e.getMessage());
            }
            ExcelUtils.loadExcel(excelPath); 
            tranId = ExcelUtils.getRowAsRowData("Sheet1", 7);
            
            try {
            	LogOut.performLogout(driver, wait);
            	login.First();
                new VerificationOfCashWithdrawal(driver).execute(id,tranId, cw,sheetname,idrow,cwrow,excelPath);
            } catch (Exception e) {
                failures.add("VerificationOfCashWithdrawal failed: " + e.getMessage());
            }

            try {
                new BalanceInquire(driver).execute(id, bal,sheetname,balrow,balcol,excelPath);
            } catch (Exception e) {
                failures.add("BalanceInquire after cash withdrawal failed: " + e.getMessage());
            }

            try {
                new ClosureOfTheAccount(driver).execute(id, clo, bal);
            } catch (Exception e) {
                failures.add("ClosureOfTheAccount failed: " + e.getMessage());
            }
           
            try {
            	LogOut.performLogout(driver, wait);
                login.Second();
                new VerificationOfClosure(driver).execute(id, cl);
            } catch (Exception e) {
                failures.add("VerificationOfClosure failed: " + e.getMessage());
            }
            // Final step: Transaction Inquiry
            try {
                new TransactionInquire(driver).execute(id);
                System.out.println("🟡 Final Alert: " + SharedContext.alertMessage);

                if ("The account has been closed.".equalsIgnoreCase(SharedContext.alertMessage.trim())) {
                    System.out.println("✅ Test Passed: Expected alert received.");
                } else {
                	Assert.fail("❌ Test Failed: Expected alert not received.\n→ Got alert: " + SharedContext.alertMessage);
                }
            } catch (Exception e) {
            	failures.add("TransactionInquire failed: " + e.getMessage());
            }
            LogOut.performLogout(driver, wait);

        } catch (Exception e) {
        	failures.add("TC01 failed: " + e.getMessage());
         
        }
   
    }
    
}
