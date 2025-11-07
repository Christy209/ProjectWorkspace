package TestCases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import NegativeTestCase.Testscheduledpaymentofmigratedaccounts;
import NegativeTestCase.VerifyTestScheduleforMigratedAccounts;


public class TC11_TestscheduledpaymentofmigratedaccountsAndVerify {
	
	  @Test
	    public void runTestSuiteInOrder() {
	        TestNG testng = new TestNG();
	        XmlSuite suite = new XmlSuite();
	        suite.setName("ScheduledPaymentCreationSuite");

	        XmlTest test = new XmlTest(suite);
	        test.setName("ScheduledPaymentStepsExecution");

	        List<XmlClass> classes = new ArrayList<>();


	       classes.add(new XmlClass(Testscheduledpaymentofmigratedaccounts.class.getName()));              // HLASPAY
	       classes.add(new XmlClass(VerifyTestScheduleforMigratedAccounts.class.getName()));               // HLASPAY
	        test.setXmlClasses(classes);
	        testng.setXmlSuites(Collections.singletonList(suite));

	        // Optional: Capture failed tests
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
	    }
	}
