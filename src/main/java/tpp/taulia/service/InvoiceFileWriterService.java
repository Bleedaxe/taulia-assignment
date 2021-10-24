package tpp.taulia.service;

import tpp.taulia.io.InvoiceWriter;
import tpp.taulia.model.Invoice;
import tpp.taulia.model.InvoiceOutputType;

public interface InvoiceFileWriterService {

  InvoiceOutputType getSupportedType();

  InvoiceWriter getInvoiceWriter(String outputDirectory);

  void writeInvoice(Invoice invoice, InvoiceWriter invoiceWriter) throws Exception;
}
