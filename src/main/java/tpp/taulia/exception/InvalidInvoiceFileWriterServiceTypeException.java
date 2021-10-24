package tpp.taulia.exception;

import tpp.taulia.model.InvoiceOutputType;

public class InvalidInvoiceFileWriterServiceTypeException extends RuntimeException {

  private static final String MESSAGE_FORMAT = "No file writer service found for type %s";

  public InvalidInvoiceFileWriterServiceTypeException(InvoiceOutputType type) {
    super(String.format(MESSAGE_FORMAT, type));
  }
}
