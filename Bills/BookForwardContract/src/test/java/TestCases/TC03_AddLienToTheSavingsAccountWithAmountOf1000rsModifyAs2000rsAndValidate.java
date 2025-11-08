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
import CASA.AddAndModifyTheLien;
import CASA.BalanceInquire;
import CASA.Cashdeposit;
import CASA.ClosureOfTheAccount;
import CASA.CreateSavingsAccount;
import CASA.VerificationOfLien;
import CASA.VerificationOfSavingsAccount;
import CASA.VerificationofCashDeposit;
import Utilities.ExcelUtils;
import Utilities.LogOut;
import Utilities.Login;
import Utilities.RowData;

public class TC03_AddLienToTheSavingsAccountWithAmountOf1000rsModifyAs2000rsAndValidate {
	
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
	            RowData id = ExcelUtils.getRowAsRowData("TC03", 1);
	            List<String> verifyData = ExcelUtils.getRowAsList("TC03", 2);
	            List<String> cash = ExcelUtils.getRowAsList("TC03", 3);
	            RowData tranId = ExcelUtils.getRowAsRowData("TC03", 3);
	            List<String> bal = ExcelUtils.getRowAsList("TC03", 4);
	            List<String> li = ExcelUtils.getRowAsList("TC03", 5);
	            List<String> Mli = ExcelUtils.getRowAsList("TC03", 6);
	            List<String> clo = ExcelUtils.getRowAsList("TC03", 7);
	            
	        	String sheetname = "TC03";
	        	int balrow = 4;
	        	int balcol = 74;
	        	int carow = 3;
	        	int row = 1;
        	 	int acctid = 1;
        	 	int lirow = 5;
        	 	int mlirow = 6;

	            try {
	            	
	                login.First();
	                new CreateSavingsAccount(driver).execute(id,sheetname,row,excelPath);
	                
	            } catch (Exception e) {
	                failures.add("ClosureOfTheAccount failed: " + e.getMessage());
	            }
	            
	            ExcelUtils.loadExcel(excelPath); 
	            id = ExcelUtils.getRowAsRowData("TC03", 1);
	           
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
	            tranId = ExcelUtils.getRowAsRowData("TC03", 3);
	           
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
	                new AddAndModifyTheLien(driver).execute(id, li);
	            } catch (Exception e) {
	                failures.add("AddLien failed: " + e.getMessage());
	            }

	            
	            try {
	        	 	LogOut.performLogout(driver, wait);
	        	 	login.Second();

	                new VerificationOfLien(driver).execute(id, li,sheetname,acctid,lirow,excelPath,lirow);
	                
	            } catch (Exception e) {
	                failures.add("AddVerificationOfLien failed: " + e.getMessage());
	            }
	            
	            try {
	                new AddAndModifyTheLien(driver).execute(id,Mli);
	            } catch (Exception e) {
	                failures.add("Modification Of Lien failed: " + e.getMessage());
	            }
	            
	            try {
	        	 	LogOut.performLogout(driver, wait);
	        	 	login.First();
	                new VerificationOfLien(driver).execute(id, Mli,sheetname,acctid,mlirow,excelPath,lirow);
	                
	            } catch (Exception e) {
	                failures.add("Modify:VerificationOfLien failed: " + e.getMessage());
	            }
	            
	            try {
	                new ClosureOfTheAccount(driver).execute(id, clo, bal);
	            } catch (Exception e) {
	                failures.add("ClosureOfTheAccount failed: " + e.getMessage());
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
	        	failures.add("TC03 failed: " + e.getMessage());
	         
	        }
	    }
 }
