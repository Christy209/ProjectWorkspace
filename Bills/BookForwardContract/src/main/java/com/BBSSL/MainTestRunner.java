package com.BBSSL;

import org.testng.TestNG;
import java.util.Collections;

public class MainTestRunner {
//    public static void main(String[] args) {
//        TestNG testng = new TestNG();
//        testng.setTestSuites(List.of("testng.xml"));
//        testng.run();
//    }
	 public static void main(String[] args) {
	        String suite = "testng.xml"; // default suite

	        if (args.length > 0) {
	            suite = args[0]; // use passed-in suite
	        }

	        TestNG testng = new TestNG();
	        testng.setTestSuites(Collections.singletonList(suite));
	        testng.run();
	    }
}