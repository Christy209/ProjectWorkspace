package TestCases;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Base.DriverManager;
import CASA.BalanceInquire;
import CASA.Cashdeposit;
import CASA.CreateSavingsAccount;
import CASA.IssueChequeBook;
import CASA.StopAndRevokePayment;
import CASA.VerificationOfIssueChequeBook;
import CASA.VerificationOfSavingsAccount;
import CASA.VerificationOfStopAndRevokePayment;
import CASA.VerificationofCashDeposit;
import Utilities.ExcelUtils;
import Utilities.LogOut;
import Utilities.Login;
import Utilities.RowData;

public class TC04_IssueChequeBookToTheSavingsAccountWith10LeavesAfterTransactionStopTheChequeAndRevoke {
	
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
            RowData id = ExcelUtils.getRowAsRowData("TC04", 1);
            List<String> verifyData = ExcelUtils.getRowAsList("TC04", 2);
            List<String> cash = ExcelUtils.getRowAsList("TC04", 3);
            RowData tranId = ExcelUtils.getRowAsRowData("TC04", 3);
            List<String> bal = ExcelUtils.getRowAsList("TC04", 4);
            List<String> issue = ExcelUtils.getRowAsList("TC04", 5);
            List<String> stop = ExcelUtils.getRowAsList("TC04", 6);
            RowData refno = ExcelUtils.getRowAsRowData("TC04", 7);
            
        	String sheetname = "TC04";
        	int balrow = 4;
        	int balcol = 74;
        	int carow = 3;
        	int row = 1;
        	int refnorow = 6;

            try {
            	
                login.First();
                
                assertTrue(driver.getCurrentUrl().contains("expectedUrlAfterLogin"),
                        "After First login: wrong URL – " + driver.getCurrentUrl());
                    assertTrue(driver.getTitle().length() > 0,
                        "After First login: title not loaded");
                    
                new CreateSavingsAccount(driver).execute(id,sheetname,row,excelPath);
                
            } catch (Exception e) {
                failures.add("ClosureOfTheAccount failed: " + e.getMessage());
            }
            
            ExcelUtils.loadExcel(excelPath); 
            id = ExcelUtils.getRowAsRowData("TC04", 1);
           
            try {
            	LogOut.performLogout(driver, wait);
            	login.Second();
            	
            	 assertTrue(driver.getCurrentUrl().contains("expectedPostLoginUrl"),
                         "After Second login: wrong URL – " + driver.getCurrentUrl());
            	 
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
            tranId = ExcelUtils.getRowAsRowData("TC04", 3);
           
            try {
        	 	LogOut.performLogout(driver, wait);
        	 	login.First(); // or wherever your "Cash Deposit" data is
        	 	new VerificationofCashDeposit(driver).execute(id, tranId, cash, sheetname,row,carow,excelPath);
            } catch (Exception e) {
                failures.add("VerificationofCashDeposit failed: " + e.getMessage());
            }

            try {

                new BalanceInquire(driver).execute(id, bal,sheetname,balrow,balcol,excelPath);
            } catch (Exception e) {
                failures.add("BalanceInquire failed: " + e.getMessage());
            }
            
            try {

                new IssueChequeBook(driver).execute(id,issue);
            } catch (Exception e) {
                failures.add("IssueChequeBook failed: " + e.getMessage());
            }
            
            try {
            	LogOut.performLogout(driver, wait);
            	login.Second(); // or wherever your "Cash Deposit" data is
                new VerificationOfIssueChequeBook(driver).execute(id,issue);
            } catch (Exception e) {
                failures.add("VerificationOfIssueChequeBook failed: " + e.getMessage());
            }
            
            try {

                new StopAndRevokePayment(driver).execute(id,stop,excelPath,sheetname,refnorow);
            } catch (Exception e) {
                failures.add("StopPayment failed: " + e.getMessage());
            }
            
            try {
            	LogOut.performLogout(driver, wait);
            	login.First(); // or wherever your "Cash Deposit" data is
                new VerificationOfStopAndRevokePayment(driver).execute(stop,refno);
            } catch (Exception e) {
                failures.add("VerificationOf StopPayment failed: " + e.getMessage());
            }
            
            if (!failures.isEmpty()) {
                StringBuilder sb = new StringBuilder("❌ Test Case Failed:\n");
                for (String f : failures) {
                    sb.append("→ ").append(f).append("\n");
                }
                System.err.println(sb);
                fail(sb.toString()); // this will mark test as failed
            }
            
        } catch (Exception e) {
            failures.add("TC04 failed: " + e.getMessage());
        }
        
        }

}
