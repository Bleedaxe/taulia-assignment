package tpp.taulia.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import tpp.taulia.exception.InvalidInvoiceFileWriterServiceTypeException;

import static org.assertj.core.api.Assertions.assertThat;

class InvoiceFileWriterServiceFactoryTest {

  @Test
  void createByType_withCsvType_shouldReturnInstanceOfInvoiceFileWriterServiceCsvImpl() {
    var invoiceFileWriterService = InvoiceFileWriterServiceFactory.createByType("csv", "output");
    assertThat(invoiceFileWriterService).isInstanceOf(InvoiceFileWriterServiceCsvImpl.class);
  }

  @Test
  void createByType_withXmlType_shouldReturnInstanceOfInvoiceFileWriterServiceXmlImpl() {
    var invoiceFileWriterService = InvoiceFileWriterServiceFactory.createByType("xml", "output");
    assertThat(invoiceFileWriterService).isInstanceOf(InvoiceFileWriterServiceXmlImpl.class);
  }

  @Test
  void createByType_withInvalidType_shouldThrow() {
    Assertions.assertThatThrownBy(
            () -> InvoiceFileWriterServiceFactory.createByType("invalid", "output"))
        .isInstanceOf(InvalidInvoiceFileWriterServiceTypeException.class);
  }
}
