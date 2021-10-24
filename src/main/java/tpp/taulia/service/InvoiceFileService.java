package tpp.taulia.service;

import tpp.taulia.model.InvoiceOutputType;

public interface InvoiceFileService {

  void processFile(String path, InvoiceOutputType invoiceOutputType, String outputDirectory);
}
