package TestCases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import Reports.HPR;

public class TC01_Report_DLYRPTTestCase {

    @Test
    public void runDLYRPT_HPRSuite() {
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
