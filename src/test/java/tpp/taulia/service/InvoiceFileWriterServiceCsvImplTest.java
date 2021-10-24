package tpp.taulia.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tpp.taulia.helper.InvoiceHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class InvoiceFileWriterServiceCsvImplTest {

  private final InvoiceFileWriterService invoiceFileWriterService = new InvoiceFileWriterServiceCsvImpl();

  @Test
  void writeInvoice_withTwoDifferentBuyers_shouldCreateTwoFilesWithCorrespondingData(
      @TempDir Path outputDirectory) throws Exception {

    var test1 = InvoiceHelper.getInvoice("test1");
    var test1Second = InvoiceHelper.getInvoice("test1", "second");
    var test2 = InvoiceHelper.getInvoice("test2");

    try (var invoiceWriter = invoiceFileWriterService.getInvoiceWriter(outputDirectory.toString())) {
      invoiceFileWriterService.writeInvoice(test1, invoiceWriter);
      invoiceFileWriterService.writeInvoice(test2, invoiceWriter);
      invoiceFileWriterService.writeInvoice(test1Second, invoiceWriter);
    }

    var files = new File(outputDirectory.toString()).listFiles();
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
            InvoiceHelper.INVOICE_CSV_HEADER,
            "test1,image-name,invoice-image,2021-10-22,invoice-number,42,BGN,status,taulia",
            "test1,second,invoice-image,2021-10-22,invoice-number,42,BGN,status,taulia");

    var test2Lines = Files.readAllLines(test2File.toPath());
    assertThat(test2Lines)
        .contains(
            InvoiceHelper.INVOICE_CSV_HEADER,
            "test2,image-name,invoice-image,2021-10-22,invoice-number,42,BGN,status,taulia");
  }

  private void assertFileNames(File test1File, File test2File) {
    assertThat(test1File).hasName("test1.csv");
    assertThat(test2File).hasName("test2.csv");
  }
}
