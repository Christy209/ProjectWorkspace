package Utilities;

import java.io.File;
import java.io.FileWriter;

public class TestDocumentGenerator {

    private static final String DOCUMENT_FOLDER =
            System.getProperty("user.dir") + "/allure-results/document-view/";

    public static String createDocument(String testName, String content) throws Exception {

        File folder = new File(DOCUMENT_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String filePath = DOCUMENT_FOLDER + testName + ".html";

        FileWriter writer = new FileWriter(filePath);
        writer.write("<html><body>");
        writer.write("<h2>" + testName + "</h2>");
        writer.write("<pre>" + content + "</pre>");
        writer.write("</body></html>");
        writer.close();

        return filePath;
    }
}
