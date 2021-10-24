package tpp.taulia.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tpp.taulia.helper.InvoiceHelper;
import tpp.taulia.model.Invoice;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class InvoiceFileWriterServiceXmlImplTest {

  @TempDir Path outputDirectory;

  @Mock private ImageWriterService imageWriterService;

  @InjectMocks private InvoiceFileWriterServiceXmlImpl invoiceFileWriterService;

  @Test
  void writeInvoice_withTwoDifferentBuyers_shouldCreateTwoFilesWithCorrespondingData()
      throws Exception {

    var first = InvoiceHelper.getInvoice("test1", "first");
    var second = InvoiceHelper.getInvoice("test1", "second");
    var third = InvoiceHelper.getInvoice("test2", "third");

    try (var invoiceWriter =
        invoiceFileWriterService.getInvoiceWriter(outputDirectory.toString())) {
      invoiceFileWriterService.writeInvoice(first, invoiceWriter);
      invoiceFileWriterService.writeInvoice(third, invoiceWriter);
      invoiceFileWriterService.writeInvoice(second, invoiceWriter);
    }

    var files = new File(outputDirectory.toString()).listFiles();
    assertThat(files).isNotNull().hasSize(2);

    var test1File = files[0];
    var test2File = files[1];

    assertFileNames(test1File, test2File);
    assertFileContent(test1File, test2File);
    verifyImageCreation(first, second, third);
  }

  private void verifyImageCreation(Invoice... invoices) throws IOException {
    for (Invoice invoice : invoices) {
      var path = outputDirectory.resolve(invoice.getImageName()).toString();
      Mockito.verify(imageWriterService).writeImageToFileSystem(path, invoice.getInvoiceImage());
    }
  }

  private void assertFileContent(File test1File, File test2File) throws IOException {
    var test1Lines = getAllLineForFile(test1File);

    assertFileStructure(test1Lines);
    assertThat(test1Lines)
        .contains(
            "<Invoice>",
            "<Buyer>test1</Buyer>",
            "<ImageName>first</ImageName>",
            "<InvoiceDueDate>2021-10-22</InvoiceDueDate>",
            "<InvoiceNumber>invoice-number</InvoiceNumber>",
            "<InvoiceAmount>42</InvoiceAmount>",
            "<InvoiceCurrency>BGN</InvoiceCurrency>",
            "<InvoiceStatus>status</InvoiceStatus>",
            "<Supplier>taulia</Supplier>",
            "</Invoice>",
            "<Invoice>",
            "<Buyer>test1</Buyer>",
            "<ImageName>second</ImageName>",
            "<InvoiceDueDate>2021-10-22</InvoiceDueDate>",
            "<InvoiceNumber>invoice-number</InvoiceNumber>",
            "<InvoiceAmount>42</InvoiceAmount>",
            "<InvoiceCurrency>BGN</InvoiceCurrency>",
            "<InvoiceStatus>status</InvoiceStatus>",
            "<Supplier>taulia</Supplier>",
            "</Invoice>");

    var test2Lines = getAllLineForFile(test2File);
    assertFileStructure(test2Lines);
    assertThat(test2Lines)
        .contains(
            "<Invoice>",
            "<Buyer>test2</Buyer>",
            "<ImageName>third</ImageName>",
            "<InvoiceDueDate>2021-10-22</InvoiceDueDate>",
            "<InvoiceNumber>invoice-number</InvoiceNumber>",
            "<InvoiceAmount>42</InvoiceAmount>",
            "<InvoiceCurrency>BGN</InvoiceCurrency>",
            "<InvoiceStatus>status</InvoiceStatus>",
            "<Supplier>taulia</Supplier>",
            "<Invoice>");
  }

  private void assertFileStructure(List<String> fileContent) {
    assertThat(fileContent)
        .contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "<InvoiceList>", "</InvoiceList>");
  }

  private void assertFileNames(File test1File, File test2File) {
    assertThat(test1File).hasName("test1.xml");
    assertThat(test2File).hasName("test2.xml");
  }

  private List<String> getAllLineForFile(File test1File) throws IOException {
    return Files.readAllLines(test1File.toPath()).stream()
        .map(String::trim)
        .collect(Collectors.toList());
  }
}
