package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.lingala.zip4j.exception.ZipException;
import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import static com.codeborne.pdftest.assertj.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThat;

public class FilesTests {

  @Test
  void txtFile() throws Exception {
    try (InputStream tx = getClass().getClassLoader().getResourceAsStream("TxtFile.txt")) {
      String result = new String(tx.readAllBytes(), StandardCharsets.UTF_8);
      assertThat(result).contains("capital");
    }
    System.out.println("txtFile - passed");
  }

  @Test
  void pdfFile() throws Exception {
    PDF pd = new PDF(getClass().getClassLoader().getResourceAsStream("PdfFile.pdf"));
    assertThat(pd.text).contains("Капуста");
    System.out.println("pdfFile - passed");
  }

  @Test
  void exelFile() throws Exception {
    try (InputStream stream = getClass().getClassLoader().getResourceAsStream("exelTest.xlsx")) {
      XLS parsed = new XLS(stream);
      assertThat(parsed.excel
              .getSheetAt(1)
              .getRow(13)
              .getCell(5)
              .getStringCellValue())
              .isEqualTo("Торт");
      System.out.println("exelFile - passed");
    }
  }

  @Test
  void ZipTest() throws Exception {
    ZipFile zipFile = new ZipFile("./src/test/resources/TxtFile.zip");
    if (zipFile.isEncrypted())
      zipFile.setPassword("qwer".toCharArray());
    zipFile.extractAll("./src/test/resources/");
    try (FileInputStream stream = new FileInputStream("./src/test/resources/TxtFile.txt")) {
      String result = new String(stream.readAllBytes(), "UTF-8");
      assertThat(result).contains("capital");
      System.out.println("zipFile - passed");
    }
  }

  @Test
  void checkDocx() throws Exception {
    try (InputStream file = getClass().getClassLoader().getResourceAsStream("doc.docx")) {
      XWPFDocument docfile = new XWPFDocument(file);
      XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(docfile);
      String docText = xwpfWordExtractor.getText();
      assertThat(docText).contains("Соль - 2 ч. ложки (по вкусу)");
      System.out.println("docFile - passed");
    }
  }
}