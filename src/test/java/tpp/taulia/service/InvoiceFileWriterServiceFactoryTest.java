package tpp.taulia.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tpp.taulia.exception.InvalidInvoiceFileWriterServiceTypeException;
import tpp.taulia.model.InvoiceOutputType;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class InvoiceFileWriterServiceFactoryTest {

  @Autowired
  private InvoiceFileWriterServiceFactory invoiceFileWriterServiceFactory;

  @Test
  void createByType_withCsvType_shouldReturnInstanceOfInvoiceFileWriterServiceCsvImpl() {
    var invoiceFileWriterService =
        invoiceFileWriterServiceFactory.createByType(InvoiceOutputType.CSV);
    assertThat(invoiceFileWriterService).isInstanceOf(InvoiceFileWriterServiceCsvImpl.class);
  }

  @Test
  void createByType_withXmlType_shouldReturnInstanceOfInvoiceFileWriterServiceXmlImpl() {
    var invoiceFileWriterService =
        invoiceFileWriterServiceFactory.createByType(InvoiceOutputType.XML);
    assertThat(invoiceFileWriterService).isInstanceOf(InvoiceFileWriterServiceXmlImpl.class);
  }

  @Test
  void createByType_withInvalidType_shouldThrow() {
    Assertions.assertThatThrownBy(
            () -> invoiceFileWriterServiceFactory.createByType(InvoiceOutputType.INVALID))
        .isInstanceOf(InvalidInvoiceFileWriterServiceTypeException.class);
  }
}
