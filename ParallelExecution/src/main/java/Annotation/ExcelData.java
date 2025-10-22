package Annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelData {
    String sheetName();
    int[] rowIndex();  // which rows you want
}
//@Retention(RetentionPolicy.RUNTIME)
//public @interface ExcelData {
//    String fileName() default "";  // NEW: optional Excel file name
//    String sheetName();
//    int[] rowIndex();
//}
