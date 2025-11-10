package CustomMenuTests;

import org.testng.annotations.Test;
import Utilities.ExcelUpdater;

public class CustomMenuTest {

    @Test
    public void runTest() {
        String excelPath = System.getProperty("user.dir") + "/Resource/CustomData.xlsx";

        ExcelUpdater.updateCell(excelPath, "Sheet1", 0, 0, "UpdatedValue");
        ExcelUpdater.updateCell(excelPath, "Sheet1", 1, 1, "AnotherValue");
        ExcelUpdater.updateCell(excelPath, "Sheet1", 2, 2, "JenkinsUpdate");
    }
}
