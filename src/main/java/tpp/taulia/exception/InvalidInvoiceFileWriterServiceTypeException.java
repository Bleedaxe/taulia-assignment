package tpp.taulia.exception;

public class InvalidInvoiceFileWriterServiceTypeException extends RuntimeException {

  private static final String MESSAGE_FORMAT = "No file writer service found for type %s";

  public InvalidInvoiceFileWriterServiceTypeException(String type) {
    super(String.format(MESSAGE_FORMAT, type));
  }
}
