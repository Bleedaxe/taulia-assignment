package tpp.taulia.service;

import tpp.taulia.model.Invoice;

public interface InvoiceFileWriterService extends AutoCloseable {

    void writeInvoice(Invoice invoice) throws Exception;
}
