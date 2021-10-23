package tpp.taulia.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import tpp.taulia.exception.InvalidInvoiceFileWriterServiceTypeException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InvoiceFileWriterServiceFactory {

  public static InvoiceFileWriterService createByType(
      @NonNull String type, @NonNull String outputDirectory) {
    switch (type.toLowerCase()) {
      case "csv":
        return new InvoiceFileWriterServiceCsvImpl(outputDirectory);
      case "xml":
        ImageWriteService imageWriteService = new ImageWriteServiceImpl(outputDirectory);
        return new InvoiceFileWriterServiceXmlImpl(outputDirectory, imageWriteService);
      default:
        throw new InvalidInvoiceFileWriterServiceTypeException(type);
    }
  }
}
