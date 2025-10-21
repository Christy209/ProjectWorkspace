package Utilities;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import org.testng.annotations.DataProvider;
import Annotation.ExcelData;

public class Dataproviders {
	
	@DataProvider(name = "Inputs")
	public static Object[][] getData(Method method) throws Exception {
	    ExcelData excelData = method.getAnnotation(ExcelData.class);
	    if (excelData == null) {
	        throw new RuntimeException("Missing @ExcelData annotation on method: " + method.getName());
	    }

	    String sheetName = excelData.sheetName();
	    int[] rowIndexes = excelData.rowIndex();

	    InputStream is = new FileInputStream(System.getProperty("user.dir") + "/Resource/SBData.xlsx");
	    ExcelUtils xlutil = new ExcelUtils(is);

	    Object[] rowGroup = new Object[rowIndexes.length]; // could be 1 or 2 or more rows

	    for (int i = 0; i < rowIndexes.length; i++) {
	        int totalCols = xlutil.getCellCount(sheetName, rowIndexes[i]);
	        String[] rowData = new String[totalCols];

	        for (int j = 0; j < totalCols; j++) {
	            rowData[j] = xlutil.getCellData(sheetName, rowIndexes[i], j);
	        }

	        rowGroup[i] = rowData;
	    }

	    return new Object[][] { rowGroup };
	}

	@DataProvider(name = "testcase")
	public static Object[][] tTestcase(Method method) throws Exception {
	    ExcelData excelData = method.getAnnotation(ExcelData.class);
	    if (excelData == null) {
	        throw new RuntimeException("Missing @ExcelData annotation on method: " + method.getName());
	    }

	    String sheetName = excelData.sheetName();
	    int[] rowIndexes = excelData.rowIndex();

	    InputStream is = new FileInputStream(System.getProperty("user.dir") + "/Resource/TestData.xlsx");
	    ExcelUtils xlutil = new ExcelUtils(is);

	    Object[] rowGroup = new Object[rowIndexes.length]; // could be 1 or 2 or more rows

	    for (int i = 0; i < rowIndexes.length; i++) {
	        int totalCols = xlutil.getCellCount(sheetName, rowIndexes[i]);
	        String[] rowData = new String[totalCols];

	        for (int j = 0; j < totalCols; j++) {
	            rowData[j] = xlutil.getCellData(sheetName, rowIndexes[i], j);
	        }

	        rowGroup[i] = rowData;
	    }

	    return new Object[][] { rowGroup };
	}
	@DataProvider(name = "Testcase")
	public static Object[][] Testcase(Method method) throws Exception {
	    ExcelData excelData = method.getAnnotation(ExcelData.class);
	    if (excelData == null) {
	        throw new RuntimeException("Missing @ExcelData annotation on method: " + method.getName());
	    }

	    String sheetName = excelData.sheetName();
	    int[] rowIndexes = excelData.rowIndex();

	    InputStream is = new FileInputStream(System.getProperty("user.dir") + "/Resource/TestData.xlsx");
	    ExcelUtils xlutil = new ExcelUtils(is);

	    // Get header row
	    int totalCols = xlutil.getCellCount(sheetName, 0);
	    String[] headers = new String[totalCols];
	    for (int j = 0; j < totalCols; j++) {
	        headers[j] = xlutil.getCellData(sheetName, 0, j);
	    }

	    Object[] rowGroup = new Object[rowIndexes.length];

	    for (int i = 0; i < rowIndexes.length; i++) {
	        String[] rowData = new String[totalCols];
	        for (int j = 0; j < totalCols; j++) {
	            rowData[j] = xlutil.getCellData(sheetName, rowIndexes[i], j);
	        }
	        rowGroup[i] = new RowData(headers, rowData);
	    }

	    return new Object[][] { rowGroup };
	}

	    }

	

