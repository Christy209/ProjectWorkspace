package Utilities;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.DataProvider;

import Annotation.ExcelData;

public class Dataproviders {

    @DataProvider(name = "UnifiedData")
    public static Object[][] getData(Method method) throws Exception {
        ExcelData excelData = method.getAnnotation(ExcelData.class);
        if (excelData == null) {
            throw new RuntimeException("❌ Missing @ExcelData annotation on method: " + method.getName());
        }

        String sheetName = excelData.sheetName();
        int[] rowIndexes = excelData.rowIndex();

        InputStream is = new FileInputStream(System.getProperty("user.dir") + "/resources/SBData.xlsx");
        ExcelUtils xlutil = new ExcelUtils(is);

        // ✅ Read headers (first row)
        int totalCols = xlutil.getCellCount(sheetName, 0);
        String[] headers = new String[totalCols];
        for (int j = 0; j < totalCols; j++) {
            headers[j] = xlutil.getCellData(sheetName, 0, j);
        }

        List<RowData> rows = new ArrayList<>();

        if (rowIndexes.length == 1 && rowIndexes[0] == -1) {
            // ✅ Take all rows
            int totalRows = xlutil.getRowCount(sheetName);
            for (int i = 1; i < totalRows; i++) { // skip header row
                String[] rowData = new String[totalCols];
                for (int j = 0; j < totalCols; j++) {
                    rowData[j] = xlutil.getCellData(sheetName, i, j);
                }
                rows.add(new RowData(headers, rowData));
            }
        } else {
            // ✅ Take specific row indexes
            for (int i : rowIndexes) {
                String[] rowData = new String[totalCols];
                for (int j = 0; j < totalCols; j++) {
                    rowData[j] = xlutil.getCellData(sheetName, i, j);
                }
                rows.add(new RowData(headers, rowData));
            }
        }

        // ✅ Convert List<RowData> → Object[][]
        Object[][] data = new Object[rows.size()][1];
        for (int i = 0; i < rows.size(); i++) {
            data[i][0] = rows.get(i);
        }

        return data;
    }
    @DataProvider(name = "testcase")
    public static Object[][] provideRowData1(Method method) throws Exception {
        ExcelData excelData = method.getAnnotation(ExcelData.class);
        if (excelData == null) {
            throw new RuntimeException("Missing @ExcelData annotation on method: " + method.getName());
        }

        String sheetName = excelData.sheetName();
        int[] rowIndexes = excelData.rowIndex();

        InputStream is = new FileInputStream(System.getProperty("user.dir") + "/resources/LoanLoan.xlsx");
       //InputStream is = new FileInputStream(System.getProperty("user.dir") + "/resources/TD Testcase 3.xlsx");
        ExcelUtils xlutil = new ExcelUtils(is);

        // Read headers
        int totalCols = xlutil.getCellCount(sheetName, 0);
        String[] headers = new String[totalCols];
        for (int j = 0; j < totalCols; j++) {
            headers[j] = xlutil.getCellData(sheetName, 0, j);
        }

        // Build RowData[] to match method parameters
        Object[][] testData = new Object[1][rowIndexes.length];
        for (int i = 0; i < rowIndexes.length; i++) {
            String[] rowData = new String[totalCols];
            for (int j = 0; j < totalCols; j++) {
                rowData[j] = xlutil.getCellData(sheetName, rowIndexes[i], j);
            }
            testData[0][i] = new RowData(headers, rowData); // assign RowData object
        }

        return testData;
    }

    @DataProvider(name = "Testcase")
    public static Object[][] provideRowData(Method method) throws Exception {
        ExcelData excelData = method.getAnnotation(ExcelData.class);
        if (excelData == null) {
            throw new RuntimeException("Missing @ExcelData annotation on method: " + method.getName());
        }

        String sheetName = excelData.sheetName();
        int[] rowIndexes = excelData.rowIndex();

        InputStream is = new FileInputStream(System.getProperty("user.dir") + "/resources/Bhantida_Reports_Excel.xlsx");
        ExcelUtils xlutil = new ExcelUtils(is);

        // ✅ Read headers
        int totalCols = xlutil.getCellCount(sheetName, 0);
        String[] headers = new String[totalCols];
        for (int j = 0; j < totalCols; j++) {
            headers[j] = xlutil.getCellData(sheetName, 0, j);
        }

        // ✅ If -1 passed, take all rows except header
        if (rowIndexes.length == 1 && rowIndexes[0] == -1) {
            int totalRows = xlutil.getRowCount(sheetName);
            rowIndexes = new int[totalRows - 1]; // exclude header row (0)
            for (int i = 1; i < totalRows; i++) {
                rowIndexes[i - 1] = i;
            }
        }

        // ✅ Build RowData[] to match method parameters
        Object[][] testData = new Object[rowIndexes.length][1];
        for (int i = 0; i < rowIndexes.length; i++) {
            String[] rowData = new String[totalCols];
            for (int j = 0; j < totalCols; j++) {
                rowData[j] = xlutil.getCellData(sheetName, rowIndexes[i], j);
            }
            testData[i][0] = new RowData(headers, rowData);
        }

        return testData;
    }

    }

