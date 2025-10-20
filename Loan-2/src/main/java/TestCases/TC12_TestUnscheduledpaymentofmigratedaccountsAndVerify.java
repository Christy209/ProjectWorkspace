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

import NegativeTestCase.Unscheduledpaymentofmigratedaccounts;
import NegativeTestCase.Verifyunscheduledpaymentofmigratedaccounts;

public class TC12_TestUnscheduledpaymentofmigratedaccountsAndVerify {
	
	 @Test
	    public void runTestSuiteInOrder() {
	        TestNG testng = new TestNG();
	        XmlSuite suite = new XmlSuite();
	        suite.setName("UnScheduledPaymentCreationSuite");

	        XmlTest test = new XmlTest(suite);
	        test.setName("UnScheduledPaymentStepsExecution");

	        List<XmlClass> classes = new ArrayList<>();


	       classes.add(new XmlClass(Unscheduledpaymentofmigratedaccounts.class.getName()));              // HLAUPAY
	       classes.add(new XmlClass(Verifyunscheduledpaymentofmigratedaccounts.class.getName()));               // HLAUPAY
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


