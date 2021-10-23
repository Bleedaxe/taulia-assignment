package tpp.taulia.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tpp.taulia.util.TestUtil;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class InvoiceFileWriterServiceXmlImplTest {

  @Test
  void writeInvoice_withTwoDifferentBuyers_shouldCreateTwoFilesWithCorrespondingData(
      @TempDir Path tempDir) throws IOException, XMLStreamException {

    var test1 = TestUtil.getInvoice("test1");
    var test1Second = TestUtil.getInvoice("test1", "second");
    var test2 = TestUtil.getInvoice("test2");

    try (var invoiceFileWriterService = new InvoiceFileWriterServiceXmlImpl(tempDir.toString())) {
      invoiceFileWriterService.writeInvoice(test1);
      invoiceFileWriterService.writeInvoice(test2);
      invoiceFileWriterService.writeInvoice(test1Second);
    }

    var files = new File(tempDir.toString()).listFiles();
    assertThat(files).isNotNull().hasSize(2);

    var test1File = files[0];
    var test2File = files[1];

    assertFileNames(test1File, test2File);
    assertFileContent(test1File, test2File);
  }

  private void assertFileContent(File test1File, File test2File) throws IOException {
    var test1Lines = getAllLineForFile(test1File);

    assertFileStructure(test1Lines);
    assertThat(test1Lines)
        .contains(
            "<Invoice>",
            "<Buyer>test1</Buyer>",
            "<ImageName>image-name</ImageName>",
            "<InvoiceImage>invoice-image</InvoiceImage>",
            "<InvoiceDueDate>22-10-2021</InvoiceDueDate>",
            "<InvoiceNumber>invoice-number</InvoiceNumber>",
            "<InvoiceAmount>42</InvoiceAmount>",
            "<InvoiceCurrency>BGN</InvoiceCurrency>",
            "<InvoiceStatus>status</InvoiceStatus>",
            "<Supplier>taulia</Supplier>",
            "</Invoice>",
            "<Invoice>",
            "<Buyer>test1</Buyer>",
            "<ImageName>second</ImageName>",
            "<InvoiceImage>invoice-image</InvoiceImage>",
            "<InvoiceDueDate>22-10-2021</InvoiceDueDate>",
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
            "<ImageName>image-name</ImageName>",
            "<InvoiceImage>invoice-image</InvoiceImage>",
            "<InvoiceDueDate>22-10-2021</InvoiceDueDate>",
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
    assertThat(test1File.getName()).isEqualTo("test1.xml");
    assertThat(test2File.getName()).isEqualTo("test2.xml");
  }

  private List<String> getAllLineForFile(File test1File) throws IOException {
    return Files.readAllLines(test1File.toPath()).stream()
        .map(String::trim)
        .collect(Collectors.toList());
  }
}