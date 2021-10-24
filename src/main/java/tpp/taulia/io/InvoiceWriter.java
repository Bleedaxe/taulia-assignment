package tpp.taulia.io;

import tpp.taulia.model.Invoice;

public interface InvoiceWriter extends AutoCloseable {

    String getOutputDirectory();

    void writeInvoice(Invoice invoice) throws Exception;
}
