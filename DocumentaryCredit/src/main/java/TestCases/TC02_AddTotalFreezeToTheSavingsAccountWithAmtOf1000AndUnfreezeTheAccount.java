package TestCases;

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
import CASA.CashWithdrawal;
import CASA.AddFreeze;
import CASA.Cashdeposit;
import CASA.CreateSavingsAccount;
import CASA.VerificationOfCashWithdrawal;
import CASA.VerificationOfFreeze;
import CASA.VerificationOfSavingsAccount;
import CASA.VerificationofCashDeposit;
import Utilities.ExcelUtils;
import Utilities.ExpectedErrorException;
import Utilities.LogOut;
import Utilities.Login;
import Utilities.RowData;

public class TC02_AddTotalFreezeToTheSavingsAccountWithAmtOf1000AndUnfreezeTheAccount {
	
	  
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
            RowData id = ExcelUtils.getRowAsRowData("TC02", 1);
            List<String> verifyData = ExcelUtils.getRowAsList("TC02", 2);
            List<String> cash = ExcelUtils.getRowAsList("TC02", 3);
            RowData tranId = ExcelUtils.getRowAsRowData("TC02", 3);
            List<String> bal = ExcelUtils.getRowAsList("TC02", 4);
            List<String> fr = ExcelUtils.getRowAsList("TC02", 5);
            RowData baln = ExcelUtils.getRowAsRowData("TC02", 4);
            RowData error = ExcelUtils.getRowAsRowData("TC02", 6);
            List<String> cw = ExcelUtils.getRowAsList("TC02", 6);
            List<String> unfr = ExcelUtils.getRowAsList("TC02", 7);
            
        	String sheetname = "TC02";
        	int balrow = 4;
        	int balcol = 74;
        	int carow = 3;
        	int row = 1;
        	int cwrow = 6;
        	int idrow = 1;

            try {
            	
                login.First();
                new CreateSavingsAccount(driver).execute(id,sheetname,row,excelPath);
                
            } catch (Exception e) {
                failures.add("ClosureOfTheAccount failed: " + e.getMessage());
            }
            
            ExcelUtils.loadExcel(excelPath); 
            id = ExcelUtils.getRowAsRowData("TC02", 1);
           
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
            tranId = ExcelUtils.getRowAsRowData("TC02", 3);
           
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
                new AddFreeze(driver).execute(id, fr);
            } catch (Exception e) {
                failures.add("AddFreeze failed: " + e.getMessage());
            }

            
            try {
        	 	LogOut.performLogout(driver, wait);
        	 	login.Second();
        	 	int acctid = 1;
        	 	int frid = 5;
                new VerificationOfFreeze(driver).execute(id, fr,sheetname,acctid,frid);
                
            } catch (Exception e) {
                failures.add("VerificationOfFreeze failed: " + e.getMessage());
            }
            
            try {
                new CashWithdrawal(driver).execute(id,baln,error,cw,sheetname,cwrow,excelPath);
            } catch (ExpectedErrorException e) {
                // ✅ Expected behavior, mark as pass or ignore
                System.out.println("✅ Test passed with expected error: " + e.getMessage());
            } catch (Exception e) {
                failures.add("CashWithdrawal failed: " + e.getMessage());
            }
            
            ExcelUtils.loadExcel(excelPath); 
            tranId = ExcelUtils.getRowAsRowData("TC02", 6);
            
            try {
                new AddFreeze(driver).execute(id, unfr);
            } catch (Exception e) {
                failures.add("Unfreeze failed: " + e.getMessage());
            }

            
            try {
        	 	LogOut.performLogout(driver, wait);
        	 	login.First();
        	 	int acctid = 1;
        	 	int frid = 7;
                new VerificationOfFreeze(driver).execute(id, unfr,sheetname,acctid,frid);
                
            } catch (Exception e) {
                failures.add("Unfreeze - VerificationOfFreeze failed: " + e.getMessage());
            }

            try {
                new CashWithdrawal(driver).execute(id,baln,error,cw,sheetname,cwrow,excelPath);
            } catch (ExpectedErrorException e) {
                // ✅ Expected behavior, mark as pass or ignore
                System.out.println("✅ Test passed with expected error: " + e.getMessage());
            } catch (Exception e) {
                failures.add("After unfreezed of CashWithdrawal failed: " + e.getMessage());
            }
            
            ExcelUtils.loadExcel(excelPath); 
            tranId = ExcelUtils.getRowAsRowData("TC02", 6);
            
            try {
            	LogOut.performLogout(driver, wait);
            	login.Second();
                new VerificationOfCashWithdrawal(driver).execute(id,tranId, cw,sheetname,idrow,cwrow,excelPath);
            } catch (Exception e) {
                failures.add("After unfreezed of VerificationOfCashWithdrawal failed: " + e.getMessage());
            }
            
            LogOut.performLogout(driver, wait);
            
            if (!failures.isEmpty()) {
                StringBuilder sb = new StringBuilder("❌ Test Case Failed:\n");
                for (String f : failures) {
                    sb.append("→ ").append(f).append("\n");
                }
                System.err.println(sb);
                fail(sb.toString()); // this will mark test as failed
            }

        } catch (Exception e) {
        	 //e.printStackTrace();
        	failures.add("TC02 failed: " + e.getMessage());
         
        }
   
        }
    }


