package TestCases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.testng.Reporter;
import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import Base.BaseTest;
import Reports.HPR;
import Utilities.ExcelUtils;

public class TC01_Report_DLYRPTTestCase extends BaseTest {

	 @Test
	    public void runTest() throws Exception {
	        // Load Excel ONCE before reading
	        String excelPath = System.getProperty("user.dir") + "/resources/Bhantida_Reports_Excel.xlsx";
	        ExcelUtils.loadExcel(excelPath);
	        Reporter.log("Excel file loaded: " + excelPath, true);

	        // Now run the suite in order
	        runTestSuiteInOrder();
	    }
	 
    @Test
    private void runTestSuiteInOrder()  {
        TestNG testng = new TestNG();
        XmlSuite suite = new XmlSuite();
        suite.setName("DLYRPTSuite");

        XmlTest test = new XmlTest(suite);
        test.setName("DLYRPTExecution");

        List<XmlClass> classes = new ArrayList<>();
        classes.add(new XmlClass(HPR.class.getName())); 
        test.setXmlClasses(classes);

        testng.setXmlSuites(Collections.singletonList(suite));

        testng.run();
    }
}
