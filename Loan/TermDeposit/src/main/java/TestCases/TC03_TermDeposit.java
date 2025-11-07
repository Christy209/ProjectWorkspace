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

import TermDeposit.CloseTermDeposit;
import TermDeposit.ModificationOfTermDepositAftersVerification;
import TermDeposit.ModifyTermDepositBeforeVerification;
import TermDeposit.OpeningTermDepositAccount;
import TermDeposit.VerificationOfTermDeposit;
import TermDeposit.VerifyAfterModification;

public class TC03_TermDeposit {

    @Test
    public void runTestSuiteInOrder() {
        TestNG testng = new TestNG();
        XmlSuite suite = new XmlSuite();
        suite.setName("TermDepositCreationSuite");

        XmlTest test = new XmlTest(suite);
        test.setName("TermDepositExecution");

        List<XmlClass> classes = new ArrayList<>();

        // Order of execution - important
       classes.add(new XmlClass(OpeningTermDepositAccount.class.getName()));              // HOAACTD
       classes.add(new XmlClass(ModifyTermDepositBeforeVerification.class.getName()));   // HOAACMTD
       classes.add(new XmlClass(VerificationOfTermDeposit.class.getName()));   //HOAACVTD
       //classes.add(new XmlClass(ModificationOfTermDepositAftersVerification.class.getName())); // HACMTD
       //classes.add(new XmlClass(VerifyAfterModification.class.getName()));    // HACMTD         
       //classes.add(new XmlClass(CloseTermDeposit.class.getName()));//HAACTD
        
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
