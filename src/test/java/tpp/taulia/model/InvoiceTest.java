package tpp.taulia.model;

import org.junit.jupiter.api.Test;
import tpp.taulia.util.CsvUtil;
import tpp.taulia.util.TestUtil;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;

class InvoiceTest {

  @Test
  public void serializeToCsv_withJackson_shouldProduceProperFile() throws IOException {
    var invoice = TestUtil.getInvoice("buyer-1");

    var writer = CsvUtil.getCsvWriterFor(Invoice.class);

    try (StringWriter stringWriter = new StringWriter()) {
      writer.writeValue(stringWriter, invoice);
      assertThat(stringWriter.toString())
          .contains(
              TestUtil.INVOICE_CSV_HEADER,
              "buyer-1,image-name,invoice-image,22-10-2021,invoice-number,42,BGN,status,taulia");
    }
  }

  @Test
  public void parsingCsv_withJackson_shouldDeserializeToExpectedObject() throws IOException {
    var expected = TestUtil.getInvoice("buyer-1");

    try (var stringReader =
        new StringReader(
            TestUtil.INVOICE_CSV_HEADER
                + "buyer-1,image-name,invoice-image,22-10-2021,invoice-number,42,BGN,status,taulia")) {
      var reader = CsvUtil.getCsvReaderFor(Invoice.class);
      var objectMappingIterator = reader.<Invoice>readValues(stringReader);
      var invoices = objectMappingIterator.readAll();

      assertThat(invoices).hasSize(1);
      var invoice = invoices.get(0);

      assertThat(invoice).isEqualTo(expected);
    }
  }
}