package tpp.taulia.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tpp.taulia.io.CsvInvoiceWriter;
import tpp.taulia.io.InvoiceWriter;
import tpp.taulia.model.Invoice;
import tpp.taulia.model.InvoiceOutputType;

@Slf4j
@Service
public class InvoiceFileWriterServiceCsvImpl implements InvoiceFileWriterService {

  @Override
  public InvoiceOutputType getSupportedType() {
    return InvoiceOutputType.CSV;
  }

  @Override
  public InvoiceWriter getInvoiceWriter(String outputDirectory) {
    return new CsvInvoiceWriter(outputDirectory);
  }

  @Override
  public void writeInvoice(Invoice invoice, InvoiceWriter invoiceWriter) throws Exception {
    invoiceWriter.writeInvoice(invoice);
  }
}
