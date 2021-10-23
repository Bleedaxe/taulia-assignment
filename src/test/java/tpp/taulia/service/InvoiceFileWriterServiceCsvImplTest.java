package tpp.taulia.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tpp.taulia.helper.TestHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class InvoiceFileWriterServiceCsvImplTest {

  @Test
  void writeInvoice_withTwoDifferentBuyers_shouldCreateTwoFilesWithCorrespondingData(
      @TempDir Path tempDir) throws IOException {

    var test1 = TestHelper.getInvoice("test1");
    var test1Second = TestHelper.getInvoice("test1", "second");
    var test2 = TestHelper.getInvoice("test2");

    try (var invoiceFileWriterServiceCsv =
        new InvoiceFileWriterServiceCsvImpl(tempDir.toString())) {
      invoiceFileWriterServiceCsv.writeInvoice(test1);
      invoiceFileWriterServiceCsv.writeInvoice(test2);
      invoiceFileWriterServiceCsv.writeInvoice(test1Second);
    }

    var files = new File(tempDir.toString()).listFiles();
    assertThat(files).isNotNull().hasSize(2);

    var test1File = files[0];
    var test2File = files[1];

    assertFileNames(test1File, test2File);
    assertFileContent(test1File, test2File);
  }

  private void assertFileContent(File test1File, File test2File) throws IOException {
    var test1Lines = Files.readAllLines(test1File.toPath());
    assertThat(test1Lines)
        .contains(
            TestHelper.INVOICE_CSV_HEADER,
            "test1,image-name,invoice-image,22-10-2021,invoice-number,42,BGN,status,taulia",
            "test1,second,invoice-image,22-10-2021,invoice-number,42,BGN,status,taulia");

    var test2Lines = Files.readAllLines(test2File.toPath());
    assertThat(test2Lines)
        .contains(
            TestHelper.INVOICE_CSV_HEADER,
            "test2,image-name,invoice-image,22-10-2021,invoice-number,42,BGN,status,taulia");
  }

  private void assertFileNames(File test1File, File test2File) {
    assertThat(test1File.getName()).isEqualTo("test1.csv");
    assertThat(test2File.getName()).isEqualTo("test2.csv");
  }
}
