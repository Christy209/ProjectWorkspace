package TestCases;

import Base.BaseTest;
import Loan.*;
import Utilities.*;
import org.testng.*;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TC02_CreateLoanAccountwithSchmcodeforTwoWheelerRs2LandClosetheAccount extends BaseTest {

	/*
	 * @BeforeClass public void setup() { Reporter.log("Login successful", true);
	 * Reporter.log("Excel data loaded", true);
	 * Reporter.log("Stop & Revoke executed", true); }
	 */

    @Test
    public void runTest() throws Exception {
        // Load Excel ONCE before reading
        String excelPath = System.getProperty("user.dir") + "/resources/LoanLoan.xlsx";
        ExcelUtils.loadExcel(excelPath);
        Reporter.log("Excel file loaded: " + excelPath, true);

        // Now run the suite in order
        runTestSuiteInOrder();
    }

    private void runTestSuiteInOrder() {
        TestNG testng = new TestNG();
        XmlSuite suite = new XmlSuite();
        suite.setName("LoanCreationSuite");

        XmlTest test = new XmlTest(suite);
        test.setName("LoanStepsExecution");

        List<XmlClass> classes = new ArrayList<>();

        // Order of execution - important
        classes.add(new XmlClass(LoanAccountOpening.class.getName()));            // HOAACLA
        classes.add(new XmlClass(Loanverification.class.getName()));             // HOAACVLA
        classes.add(new XmlClass(LoanDisbustment.class.getName()));              // HLADIS
        classes.add(new XmlClass(LoanClose.class.getName()));                    // HLACLOS
        // classes.add(new XmlClass(DemandSatisficationProcess.class.getName())); // HLADSP

        test.setXmlClasses(classes);
        testng.setXmlSuites(Collections.singletonList(suite));

        // Capture failed tests
        List<ITestResult> failedTests = new ArrayList<>();
        testng.addListener(new IReporter() {
            @Override
            public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
                for (ISuite suiteResult : suites) {
                    suiteResult.getResults().forEach((name, result) -> {
                        ITestContext context = result.getTestContext();
                        failedTests.addAll(context.getFailedTests().getAllResults());
                    });
                }
            }
        });

        // Execute all steps
        testng.run();

//        if (!failedTests.isEmpty()) {
//            Assert.fail("❌ Some steps failed. Check TestNG report for details.");
//        }
    }
}
